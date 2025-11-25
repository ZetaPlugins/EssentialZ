package com.zetaplugins.essentialz.commands.communication;

import com.zetaplugins.essentialz.EssentialZ;
import com.zetaplugins.essentialz.storage.Storage;
import com.zetaplugins.essentialz.storage.model.PlayerData;
import com.zetaplugins.essentialz.util.MessageManager;
import com.zetaplugins.essentialz.util.commands.EszCommand;
import com.zetaplugins.zetacore.annotations.AutoRegisterCommand;
import com.zetaplugins.zetacore.annotations.InjectManager;
import com.zetaplugins.zetacore.commands.ArgumentList;
import com.zetaplugins.zetacore.commands.exceptions.CommandSenderMustBePlayerException;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

@AutoRegisterCommand(
        commands = "msgtoggle",
        description = "Toggle receiving private messages.",
        usage = "/msgtoggle",
        aliases = {"togglemsg", "dmtoggle", "pmtoggle"},
        permission = "essentialz.msg.toggle"
)
public class ToggleMsgCommand extends EszCommand {

    @InjectManager
    private Storage storage;

    public ToggleMsgCommand(EssentialZ plugin) {
        super(plugin);
    }

    @Override
    public boolean execute(CommandSender sender, Command command, String label, ArgumentList args) throws CommandSenderMustBePlayerException {
        if (!(sender instanceof Player player)) throw new CommandSenderMustBePlayerException();

        PlayerData playerData = storage.load(player.getUniqueId());
        boolean newStatus = !playerData.isEnableDms();
        playerData.setEnableDms(newStatus);
        storage.save(playerData);

        String statusMsgKey = newStatus ? "dmsEnabled" : "dmsDisabled";
        String statusMsgDefault = newStatus ? "&7You have {ac}enabled &7private messages." : "&7You have {ac}disabled &7private messages.";
        player.sendMessage(getMessageManager().getAndFormatMsg(
                MessageManager.Style.SUCCESS,
                statusMsgKey,
                statusMsgDefault
        ));
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, Command command, ArgumentList args) {
        return List.of();
    }
}
