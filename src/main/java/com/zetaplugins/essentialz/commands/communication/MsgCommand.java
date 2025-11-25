package com.zetaplugins.essentialz.commands.communication;

import com.zetaplugins.essentialz.EssentialZ;
import com.zetaplugins.essentialz.features.LastMsgManager;
import com.zetaplugins.essentialz.storage.Storage;
import com.zetaplugins.essentialz.storage.model.PlayerData;
import com.zetaplugins.essentialz.util.MessageManager;
import com.zetaplugins.essentialz.util.commands.EszCommand;
import com.zetaplugins.zetacore.annotations.AutoRegisterCommand;
import com.zetaplugins.zetacore.annotations.InjectManager;
import com.zetaplugins.zetacore.commands.ArgumentList;
import com.zetaplugins.zetacore.commands.exceptions.CommandUsageException;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

@AutoRegisterCommand(
        commands = "msg",
        description = "Send a private message to another player.",
        usage = "/msg <player> <message>",
        aliases = {"message", "tell", "whisper", "pm", "dm"},
        permission = "essentialz.msg"
)
public class MsgCommand extends EszCommand {

    @InjectManager
    private Storage storage;
    @InjectManager
    private LastMsgManager lastMsgManager;

    public MsgCommand(EssentialZ plugin) {
        super(plugin);
    }

    @Override
    public boolean execute(CommandSender sender, Command command, String label, ArgumentList args) throws CommandUsageException {
        Player targetPlayer = args.getPlayer(0, getPlugin());
        String message = args.getJoinedString(1);

        if (message.isEmpty()) throw new CommandUsageException("/" + command.getName() + " <message>");

        if (targetPlayer == null) throw new CommandUsageException("/" + command.getName() + " <player> <message>");
        if (sender instanceof Player senderPlayer && senderPlayer.getUniqueId().equals(targetPlayer.getUniqueId())) {
            sender.sendMessage(getMessageManager().getAndFormatMsg(
                    MessageManager.Style.ERROR,
                    "cannotMessageYourself",
                    "{ac}You cannot send a private message to yourself."
            ));
            return false;
        }

        if (sender instanceof Player senderPlayer) {
            PlayerData senderPlayerData = storage.load(senderPlayer.getUniqueId());
            if (!senderPlayerData.isEnableDms()) {
                sender.sendMessage(getMessageManager().getAndFormatMsg(
                        MessageManager.Style.ERROR,
                        "yourDmsDisabled",
                        "{ac}You have disabled private messages. Enable them using /msgtoggle to send messages."
                ));
                return false;
            }

            boolean isIgnoring = storage.isPlayerIgnoring(targetPlayer.getUniqueId(), senderPlayer.getUniqueId());
            if (isIgnoring) {
                sender.sendMessage(getMessageManager().getAndFormatMsg(
                        MessageManager.Style.ERROR,
                        "playerIsIgnoringYou",
                        "{ac}{player} is ignoring you. Your message was not sent.",
                        new MessageManager.Replaceable<>("{player}", targetPlayer.getName())
                ));
                return false;
            }

            boolean youAreIgnoring = storage.isPlayerIgnoring(senderPlayer.getUniqueId(), targetPlayer.getUniqueId());
            if (youAreIgnoring) {
                sender.sendMessage(getMessageManager().getAndFormatMsg(
                        MessageManager.Style.ERROR,
                        "youAreIgnoringPlayer",
                        "{ac}You are ignoring {player}. Unignore them to send messages.",
                        new MessageManager.Replaceable<>("{player}", targetPlayer.getName())
                ));
                return false;
            }
        }

        PlayerData targetPlayerData = storage.load(targetPlayer.getUniqueId());
        if (!targetPlayerData.isEnableDms()) {
            sender.sendMessage(getMessageManager().getAndFormatMsg(
                    MessageManager.Style.ERROR,
                    "playerDmsDisabled",
                    "{ac}{player} has disabled private messages.",
                    new MessageManager.Replaceable<>("{player}", targetPlayer.getName())
            ));
            return false;
        }

        boolean allowToUseColor = sender.hasPermission("essentialz.msg.color");
        sender.sendMessage(getMessageManager().getAndFormatMsg(
                MessageManager.Style.COMMUNICATION,
                "privateMessageSent",
                "&7&8[&7To &r{ac}{recipient}&7&8]&7: {message}",
                new MessageManager.Replaceable<>("{recipient}", targetPlayer.getName()),
                new MessageManager.Replaceable<>("{message}", message, allowToUseColor)
        ));

        targetPlayer.sendMessage(getMessageManager().getAndFormatMsg(
                MessageManager.Style.COMMUNICATION,
                "privateMessageReceived",
                "&7&8[&7From &r{ac}{sender}&7&8]&7: {message}",
                new MessageManager.Replaceable<>("{sender}", sender.getName()),
                new MessageManager.Replaceable<>("{message}", message, allowToUseColor)
        ));

        if (sender instanceof Player senderPlayer) {
            lastMsgManager.setLastMsg(senderPlayer.getUniqueId(), targetPlayer.getUniqueId());
            lastMsgManager.setLastMsg(targetPlayer.getUniqueId(), senderPlayer.getUniqueId());
        }

        return false;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, Command command, ArgumentList args) {
        if (args.getCurrentArgIndex() == 0) {
            return getPlayerOptions(args.getCurrentArg());
        }
        return List.of();
    }
}
