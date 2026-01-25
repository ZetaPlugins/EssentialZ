package com.zetaplugins.essentialz.commands.moderation;

import com.zetaplugins.essentialz.EssentialZ;
import com.zetaplugins.essentialz.config.moderation.ModerationConfig;
import com.zetaplugins.essentialz.util.MessageManager;
import com.zetaplugins.essentialz.util.PluginMessage;
import com.zetaplugins.essentialz.util.commands.EszCommand;
import com.zetaplugins.essentialz.util.permissions.Permission;
import com.zetaplugins.zetacore.annotations.AutoRegisterCommand;
import com.zetaplugins.zetacore.annotations.InjectManager;
import com.zetaplugins.zetacore.commands.ArgumentList;
import com.zetaplugins.zetacore.commands.exceptions.CommandException;
import com.zetaplugins.zetacore.commands.exceptions.CommandUsageException;
import com.zetaplugins.zetacore.services.config.ConfigService;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

@AutoRegisterCommand(
        commands = "kick",
        description = "Kicks a player from the server.",
        usage = "/kick <player> [reason]",
        permission = "essentialz.kick"
)
public class KickCommand extends EszCommand {

    @InjectManager
    private ConfigService configService;

    @InjectManager
    private MessageManager messageManager;

    public KickCommand(EssentialZ plugin) {
        super(plugin);
    }

    @Override
    public boolean execute(CommandSender commandSender, Command command, String s, ArgumentList args) throws CommandException {
        Player targetPlayer = args.getPlayer(0, getPlugin());

        if (targetPlayer == null) throw new CommandUsageException("/kick <player> [reason]");

        if (Permission.MODERATION_BYPASS.has(targetPlayer)) {
            commandSender.sendMessage(messageManager.getAndFormatMsg(PluginMessage.CANNOT_KICK_PLAYER));
            return true;
        }

        String reason = "Kicked by an operator.";
        if (args.size() > 1) reason = args.getJoinedString(1);

        ModerationConfig config = configService.getConfig(ModerationConfig.class);
        String kickMessage = config.getKickMessageTemplate().replace("{reason}", reason);
        Component kickMessageComponent = messageManager.formatMsg(kickMessage);
        targetPlayer.kick(kickMessageComponent);
        commandSender.sendMessage(messageManager.getAndFormatMsg(
                PluginMessage.PLAYER_KICKED,
                new MessageManager.Replaceable<>("{player}", targetPlayer.getName()),
                new MessageManager.Replaceable<>("{reason}", reason)
        ));
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender commandSender, Command command, ArgumentList args) {
        if (args.getCurrentArgIndex() == 0) return getPlayerOptions(args.getCurrentArg());
        if (args.getCurrentArgIndex() == 1) return List.of("<reason>");
        return List.of();
    }
}
