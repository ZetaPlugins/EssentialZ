package com.zetaplugins.essentialz.commands.communication;

import com.zetaplugins.essentialz.EssentialZ;
import com.zetaplugins.essentialz.util.MessageManager;
import com.zetaplugins.essentialz.util.commands.ArgumentList;
import com.zetaplugins.essentialz.util.commands.CommandPermissionException;
import com.zetaplugins.essentialz.util.commands.CommandUsageException;
import com.zetaplugins.essentialz.util.commands.CustomCommand;
import com.zetaplugins.essentialz.util.permissions.Permission;
import com.zetaplugins.zetacore.annotations.AutoRegisterCommand;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

@AutoRegisterCommand(
        commands = "nick",
        description = "Set or clear your nickname.",
        usage = "/nick <nickname>",
        aliases = {"nickname", "setnick"},
        permission = "essentialz.nick"
)
public class NickCommand extends CustomCommand {

    public NickCommand(EssentialZ plugin) {
        super(plugin);
    }

    @Override
    public boolean execute(CommandSender sender, Command command, ArgumentList args) throws CommandPermissionException, CommandUsageException {
        String nickName = args.getArg(0);

        Player targetPlayer = args.getPlayer(1, getPlugin());

        if (targetPlayer == null && !(sender instanceof Player)) {
            sender.sendMessage(getPlugin().getMessageManager().getAndFormatMsg(
                    MessageManager.Style.ERROR,
                    "playerOnly",
                    "{ac}You must be a player to use this command."
            ));
            return false;
        }

        if (targetPlayer == null) targetPlayer = (Player) sender;

        if (nickName == null || nickName.isEmpty()) {
            targetPlayer.displayName(Component.text(targetPlayer.getName()));
            targetPlayer.sendMessage(getPlugin().getMessageManager().getAndFormatMsg(
                    MessageManager.Style.SUCCESS,
                    "nicknameCleared",
                    "&7Your nickname has been {ac}cleared&7."
            ));
            return true;
        }

        int maxNickLenth = getPlugin().getConfigManager().getChatConfig().getInt("maxNicknameLength", 16);

        if (nickName.length() > maxNickLenth) {
            sender.sendMessage(getPlugin().getMessageManager().getAndFormatMsg(
                    MessageManager.Style.ERROR,
                    "nicknameTooLong",
                    "{ac}The nickname is too long! The maximum length is {maxLength} characters.",
                    new MessageManager.Replaceable<>("{maxLength}", String.valueOf(maxNickLenth))
            ));
            return false;
        }

        targetPlayer.displayName(Component.text(nickName));

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
                    "nicknameSetSelf",
                    "&7Your nickname has been set to '{ac}{nickname}&7'.",
                    new MessageManager.Replaceable<>("{nickname}", nickName)
            ));
        } else {
            sender.sendMessage(getPlugin().getMessageManager().getAndFormatMsg(
                    MessageManager.Style.SUCCESS,
                    "nicknameSetOtherConfirm",
                    "&7Set {ac}{player}&7's nickname to '{ac}{nickname}&7'.",
                    new MessageManager.Replaceable<>("{player}", targetPlayer.getName()),
                    new MessageManager.Replaceable<>("{nickname}", nickName)
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
        if (args.getCurrentArgIndex() == 1 && Permission.NICK_OTHERS.has(sender)) {
            return getPlayerOptions(getPlugin(), args.getCurrentArg());
        }
        return List.of();
    }
}
