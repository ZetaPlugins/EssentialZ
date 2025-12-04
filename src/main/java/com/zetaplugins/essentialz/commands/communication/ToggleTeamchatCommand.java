package com.zetaplugins.essentialz.commands.communication;

import com.zetaplugins.essentialz.EssentialZ;
import com.zetaplugins.essentialz.storage.Storage;
import com.zetaplugins.essentialz.storage.model.PlayerData;
import com.zetaplugins.essentialz.util.MessageStyle;
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
        commands = "teamchattoggle",
        description = "Toggle team chat on or off.",
        usage = "/teamchattoggle",
        aliases = {"tctoggle", "toggleteamchat"},
        permission = "essentialz.teamchat"
)
public class ToggleTeamchatCommand extends EszCommand {

    @InjectManager
    private Storage storage;

    public ToggleTeamchatCommand(EssentialZ plugin) {
        super(plugin);
    }

    @Override
    public boolean execute(CommandSender sender, Command command, String label, ArgumentList args) throws CommandSenderMustBePlayerException {
        if (!(sender instanceof Player player)) throw new CommandSenderMustBePlayerException();

        PlayerData playerData = storage.getPlayerRepository().load(player.getUniqueId());
        boolean newStatus = !playerData.isEnableTeamchat();
        playerData.setEnableTeamchat(newStatus);
        storage.getPlayerRepository().save(playerData);

        String statusMsgKey = newStatus ? "tcEnabled" : "tcDisabled";
        String statusMsgDefault = newStatus ? "&7You have {ac}enabled &7the team chat." : "&7You have {ac}disabled &7the team chat.";
        player.sendMessage(getMessageManager().getAndFormatMsg(
                MessageStyle.SUCCESS,
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
