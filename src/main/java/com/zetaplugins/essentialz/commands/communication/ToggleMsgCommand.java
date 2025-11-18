package com.zetaplugins.essentialz.commands.communication;

import com.zetaplugins.essentialz.EssentialZ;
import com.zetaplugins.essentialz.storage.PlayerData;
import com.zetaplugins.essentialz.util.MessageManager;
import com.zetaplugins.essentialz.util.commands.ArgumentList;
import com.zetaplugins.essentialz.util.commands.CommandPermissionException;
import com.zetaplugins.essentialz.util.commands.CommandUsageException;
import com.zetaplugins.essentialz.util.commands.CustomCommand;
import com.zetaplugins.zetacore.annotations.AutoRegisterCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

@AutoRegisterCommand(commands = "msgtoggle")
public class ToggleMsgCommand extends CustomCommand {

    public ToggleMsgCommand(EssentialZ plugin) {
        super(plugin);
    }

    @Override
    public boolean execute(CommandSender sender, Command command, ArgumentList args) throws CommandPermissionException, CommandUsageException {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(getPlugin().getMessageManager().getAndFormatMsg(
                    MessageManager.Style.ERROR,
                    "bePlayerError",
                    "{ac}You must be a player to use this command."
            ));
            return false;
        }

        PlayerData playerData = getPlugin().getStorage().load(player.getUniqueId());
        boolean newStatus = !playerData.isEnableDms();
        playerData.setEnableDms(newStatus);
        getPlugin().getStorage().save(playerData);

        String statusMsgKey = newStatus ? "dmsEnabled" : "dmsDisabled";
        String statusMsgDefault = newStatus ? "&7You have {ac}enabled &7private messages." : "&7You have {ac}disabled &7private messages.";
        player.sendMessage(getPlugin().getMessageManager().getAndFormatMsg(
                MessageManager.Style.SUCCESS,
                statusMsgKey,
                statusMsgDefault
        ));
        return true;
    }

    @Override
    public boolean isAuthorized(CommandSender sender) {
        return sender.hasPermission("essentialz.msg.toggle");
    }

    @Override
    public List<String> tabComplete(CommandSender sender, Command command, ArgumentList args) {
        return List.of();
    }
}
