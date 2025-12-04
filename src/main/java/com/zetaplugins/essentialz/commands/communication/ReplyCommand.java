package com.zetaplugins.essentialz.commands.communication;

import com.zetaplugins.essentialz.EssentialZ;
import com.zetaplugins.essentialz.features.LastMsgManager;
import com.zetaplugins.essentialz.storage.Storage;
import com.zetaplugins.essentialz.storage.model.PlayerData;
import com.zetaplugins.essentialz.util.MessageManager;
import com.zetaplugins.essentialz.util.MessageStyle;
import com.zetaplugins.essentialz.util.commands.EszCommand;
import com.zetaplugins.zetacore.annotations.AutoRegisterCommand;
import com.zetaplugins.zetacore.annotations.InjectManager;
import com.zetaplugins.zetacore.commands.ArgumentList;
import com.zetaplugins.zetacore.commands.exceptions.CommandSenderMustBePlayerException;
import com.zetaplugins.zetacore.commands.exceptions.CommandUsageException;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

@AutoRegisterCommand(
        commands = "reply",
        description = "Reply to the last player who messaged you.",
        usage = "/reply <message>",
        aliases = {"r"},
        permission = "essentialz.msg"
)
public class ReplyCommand extends EszCommand {

    @InjectManager
    private LastMsgManager lastMsgManager;
    @InjectManager
    private Storage storage;

    public ReplyCommand(EssentialZ plugin) {
        super(plugin);
    }

    @Override
    public boolean execute(CommandSender sender, Command command, String label, ArgumentList args) throws CommandSenderMustBePlayerException, CommandUsageException {
        if (!(sender instanceof Player player)) throw new CommandSenderMustBePlayerException();

        UUID lastMsgUUID = lastMsgManager.getLastMsg(player.getUniqueId());
        if (lastMsgUUID == null) {
            sender.sendMessage(getMessageManager().getAndFormatMsg(
                    MessageStyle.ERROR,
                    "noRecentMessages",
                    "{ac}You have no recent messages to reply to."
            ));
            return false;
        }

        Player targetPlayer = getPlugin().getServer().getPlayer(lastMsgUUID);
        if (targetPlayer == null || !targetPlayer.isOnline()) {
            sender.sendMessage(getMessageManager().getAndFormatMsg(
                    MessageStyle.ERROR,
                    "playerNotFound",
                    "{ac}Player not found."
            ));
            return false;
        }

        PlayerData targetPlayerData = storage.getPlayerRepository().load(targetPlayer.getUniqueId());
        if (targetPlayerData == null) {
            sender.sendMessage(getMessageManager().getAndFormatMsg(
                    MessageStyle.ERROR,
                    "playerDataNotFound",
                    "{ac}The player data for {player} could not be found. Please try again later."
            ));
            return false;
        }
        if (!targetPlayerData.isEnableDms()) {
            sender.sendMessage(getMessageManager().getAndFormatMsg(
                    MessageStyle.ERROR,
                    "playerDmsDisabled",
                    "{ac}{player} has disabled private messages.",
                    new MessageManager.Replaceable<>("{player}", targetPlayer.getName())
            ));
            return false;
        }

        boolean isIgnoring = storage.getIgnoresRepository().isPlayerIgnoring(targetPlayer.getUniqueId(), player.getUniqueId());
        if (isIgnoring) {
            sender.sendMessage(getMessageManager().getAndFormatMsg(
                    MessageStyle.ERROR,
                    "playerIsIgnoringYou",
                    "{ac}{player} is ignoring you. Your message was not sent.",
                    new MessageManager.Replaceable<>("{player}", targetPlayer.getName())
            ));
            return false;
        }

        boolean youAreIgnoring = storage.getIgnoresRepository().isPlayerIgnoring(player.getUniqueId(), targetPlayer.getUniqueId());
        if (youAreIgnoring) {
            sender.sendMessage(getMessageManager().getAndFormatMsg(
                    MessageStyle.ERROR,
                    "youAreIgnoringPlayer",
                    "{ac}You are ignoring {player}. Unignore them to send messages.",
                    new MessageManager.Replaceable<>("{player}", targetPlayer.getName())
            ));
            return false;
        }

        String message = args.getJoinedString(0);

        if (message.isEmpty()) throw new CommandUsageException("/" + command.getName() + " <message>");

        boolean allowToUseColor = sender.hasPermission("essentialz.msg.color");

        sender.sendMessage(getMessageManager().getAndFormatMsg(
                MessageStyle.COMMUNICATION,
                "privateMessageSent",
                "&7&8[&7To &r{ac}{recipient}&7&8]&7: {message}",
                new MessageManager.Replaceable<>("{recipient}", targetPlayer.getName()),
                new MessageManager.Replaceable<>("{message}", message, allowToUseColor)
        ));

        targetPlayer.sendMessage(getMessageManager().getAndFormatMsg(
                MessageStyle.COMMUNICATION,
                "privateMessageReceived",
                "&7&8[&7From &r{ac}{sender}&7&8]&7: {message}",
                new MessageManager.Replaceable<>("{sender}", sender.getName()),
                new MessageManager.Replaceable<>("{message}", message, allowToUseColor)
        ));

        return false;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, Command command, ArgumentList args) {
        return List.of();
    }
}
