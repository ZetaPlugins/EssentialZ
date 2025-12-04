package com.zetaplugins.essentialz.commands.communication;

import com.zetaplugins.essentialz.EssentialZ;
import com.zetaplugins.essentialz.util.MessageManager;
import com.zetaplugins.essentialz.util.MessageStyle;
import com.zetaplugins.essentialz.util.commands.EszCommand;
import com.zetaplugins.essentialz.util.permissions.Permission;
import com.zetaplugins.zetacore.annotations.AutoRegisterCommand;
import com.zetaplugins.zetacore.commands.ArgumentList;
import com.zetaplugins.zetacore.commands.exceptions.CommandSenderMustBePlayerException;
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
public class UnnickCommand extends EszCommand {

    public UnnickCommand(EssentialZ plugin) {
        super(plugin);
    }

    @Override
    public boolean execute(CommandSender sender, Command command, String label, ArgumentList args) throws CommandSenderMustBePlayerException {
        Player targetPlayer = args.getPlayer(0, getPlugin());

        if (targetPlayer == null && !(sender instanceof Player)) throw new CommandSenderMustBePlayerException();

        if (targetPlayer == null) targetPlayer = (Player) sender;

        targetPlayer.displayName(targetPlayer.name());

        boolean setForSelf = (sender instanceof Player player) && targetPlayer.getUniqueId().equals(player.getUniqueId());

        if (!setForSelf && !Permission.NICK_OTHERS.has(sender)) {
            sender.sendMessage(getMessageManager().getAndFormatMsg(
                    MessageStyle.ERROR,
                    "notAllowedToNickOthers",
                    "{ac}You do not have permission to set other players' nicknames."
            ));
            return false;
        }

        if (setForSelf) {
            targetPlayer.sendMessage(getMessageManager().getAndFormatMsg(
                    MessageStyle.SUCCESS,
                    "nicknameCleared",
                    "&7Your nickname has been {ac}cleared&7."
            ));
        } else {
            sender.sendMessage(getMessageManager().getAndFormatMsg(
                    MessageStyle.SUCCESS,
                    "nicknameClearedOther",
                    "&7You have {ac}cleared &7the nickname of {ac}{player}&7.",
                    new MessageManager.Replaceable<>("{player}", targetPlayer.getName())
            ));
        }
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, Command command, ArgumentList args) {
        if (args.getCurrentArgIndex() == 0 && Permission.NICK_OTHERS.has(sender)) {
            return getPlayerOptions(args.getCurrentArg());
        }
        return List.of();
    }
}
