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

@AutoRegisterCommand(commands = "msg")
public class MsgCommand extends CustomCommand {

    public MsgCommand(EssentialZ plugin) {
        super(plugin);
    }

    @Override
    public boolean execute(CommandSender sender, Command command, ArgumentList args) throws CommandPermissionException, CommandUsageException {
        Player targetPlayer = args.getPlayer(0, getPlugin());
        String message = args.getJoinedString(1);

        if (targetPlayer == null) throw new CommandUsageException("/" + command.getName() + " <player> <message>");
        if (sender instanceof Player senderPlayer && senderPlayer.getUniqueId().equals(targetPlayer.getUniqueId())) {
            sender.sendMessage(getPlugin().getMessageManager().getAndFormatMsg(
                    MessageManager.Style.ERROR,
                    "cannotMessageYourself",
                    "{ac}You cannot send a private message to yourself."
            ));
            return false;
        }

        if (sender instanceof Player senderPlayer) {
            PlayerData senderPlayerData = getPlugin().getStorage().load(senderPlayer.getUniqueId());
            if (!senderPlayerData.isEnableDms()) {
                sender.sendMessage(getPlugin().getMessageManager().getAndFormatMsg(
                        MessageManager.Style.ERROR,
                        "yourDmsDisabled",
                        "{ac}You have disabled private messages. Enable them using /msgtoggle to send messages."
                ));
                return false;
            }
        }

        PlayerData targetPlayerData = getPlugin().getStorage().load(targetPlayer.getUniqueId());
        if (!targetPlayerData.isEnableDms()) {
            sender.sendMessage(getPlugin().getMessageManager().getAndFormatMsg(
                    MessageManager.Style.ERROR,
                    "playerDmsDisabled",
                    "{ac}{player} has disabled private messages.",
                    new MessageManager.Replaceable<>("{player}", targetPlayer.getName())
            ));
            return false;
        }

        boolean allowToUseColor = sender.hasPermission("essentialz.msg.color");

        sender.sendMessage(getPlugin().getMessageManager().getAndFormatMsg(
                MessageManager.Style.COMMUNICATION,
                "privateMessageSent",
                "&7&8[&7To &r{ac}{recipient}&7&8]&7: {message}",
                new MessageManager.Replaceable<>("{recipient}", targetPlayer.getName()),
                new MessageManager.Replaceable<>("{message}", message, allowToUseColor)
        ));

        targetPlayer.sendMessage(getPlugin().getMessageManager().getAndFormatMsg(
                MessageManager.Style.COMMUNICATION,
                "privateMessageReceived",
                "&7&8[&7From &r{ac}{sender}&7&8]&7: {message}",
                new MessageManager.Replaceable<>("{sender}", sender.getName()),
                new MessageManager.Replaceable<>("{message}", message, allowToUseColor)
        ));

        if (sender instanceof Player senderPlayer) {
            getPlugin().getLastMsgManager().setLastMsg(senderPlayer.getUniqueId(), targetPlayer.getUniqueId());
            getPlugin().getLastMsgManager().setLastMsg(targetPlayer.getUniqueId(), senderPlayer.getUniqueId());
        }

        return false;
    }

    @Override
    public boolean isAuthorized(CommandSender sender) {
        return sender.hasPermission("essentialz.msg");
    }

    @Override
    public List<String> tabComplete(CommandSender sender, Command command, ArgumentList args) {
        if (args.getCurrentArgIndex() == 0) {
            return getPlayerOptions(getPlugin(), args.getCurrentArg());
        }
        return List.of();
    }
}
