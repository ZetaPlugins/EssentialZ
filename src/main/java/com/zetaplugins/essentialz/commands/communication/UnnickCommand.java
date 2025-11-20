package com.zetaplugins.essentialz.commands.communication;

import com.zetaplugins.essentialz.EssentialZ;
import com.zetaplugins.essentialz.util.MessageManager;
import com.zetaplugins.essentialz.util.commands.ArgumentList;
import com.zetaplugins.essentialz.util.commands.CommandPermissionException;
import com.zetaplugins.essentialz.util.commands.CommandUsageException;
import com.zetaplugins.essentialz.util.commands.CustomCommand;
import com.zetaplugins.essentialz.util.permissions.Permission;
import com.zetaplugins.zetacore.annotations.AutoRegisterCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

@AutoRegisterCommand(
        commands = "unnick",
        description = "Clear your nickname or another player's nickname.",
        usage = "/unnick [player]",
        aliases = {"clearnick", "removenick"},
        permission = "essentialz.nick"
)
public class UnnickCommand extends CustomCommand {

    public UnnickCommand(EssentialZ plugin) {
        super(plugin);
    }

    @Override
    public boolean execute(CommandSender sender, Command command, ArgumentList args) throws CommandPermissionException, CommandUsageException {
        Player targetPlayer = args.getPlayer(0, getPlugin());

        if (targetPlayer == null && !(sender instanceof Player)) {
            sender.sendMessage(getPlugin().getMessageManager().getAndFormatMsg(
                    MessageManager.Style.ERROR,
                    "playerOnly",
                    "{ac}You must be a player to use this command."
            ));
            return false;
        }

        if (targetPlayer == null) targetPlayer = (Player) sender;

        targetPlayer.displayName(targetPlayer.name());

        boolean setForSelf = (sender instanceof Player player) && targetPlayer.getUniqueId().equals(player.getUniqueId());

        if (!setForSelf && !Permission.NICK_OTHERS.has(sender)) {
            sender.sendMessage(getPlugin().getMessageManager().getAndFormatMsg(
                    MessageManager.Style.ERROR,
                    "notAllowedToNickOthers",
                    "{ac}You do not have permission to set other players' nicknames."
            ));
            return false;
        }

        if (setForSelf) {
            targetPlayer.sendMessage(getPlugin().getMessageManager().getAndFormatMsg(
                    MessageManager.Style.SUCCESS,
                    "nicknameCleared",
                    "&7Your nickname has been {ac}cleared&7."
            ));
        } else {
            sender.sendMessage(getPlugin().getMessageManager().getAndFormatMsg(
                    MessageManager.Style.SUCCESS,
                    "nicknameClearedOther",
                    "&7You have {ac}cleared &7the nickname of {ac}{player}&7.",
                    new MessageManager.Replaceable<>("{player}", targetPlayer.getName())
            ));
        }
        return true;
    }

    @Override
    public boolean isAuthorized(CommandSender sender) {
        return Permission.NICK.has(sender);
    }

    @Override
    public List<String> tabComplete(CommandSender sender, Command command, ArgumentList args) {
        if (args.getCurrentArgIndex() == 0 && Permission.NICK_OTHERS.has(sender)) {
            return getPlayerOptions(getPlugin(), args.getCurrentArg());
        }
        return List.of();
    }
}
