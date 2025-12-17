package com.zetaplugins.essentialz.commands.moderation;

import com.zetaplugins.essentialz.EssentialZ;
import com.zetaplugins.essentialz.features.SocialSpyManager;
import com.zetaplugins.essentialz.util.MessageManager;
import com.zetaplugins.essentialz.util.PluginMessage;
import com.zetaplugins.essentialz.util.commands.EszCommand;
import com.zetaplugins.essentialz.util.permissions.Permission;
import com.zetaplugins.zetacore.annotations.AutoRegisterCommand;
import com.zetaplugins.zetacore.annotations.InjectManager;
import com.zetaplugins.zetacore.commands.ArgumentList;
import com.zetaplugins.zetacore.commands.exceptions.CommandException;
import com.zetaplugins.zetacore.commands.exceptions.CommandUsageException;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

@AutoRegisterCommand(
        commands = "socialspy",
        permission = "essentialz.socialspy",
        description = "Toggle socialspy for a player.",
        usage = "/socialspy <player>"
)
public class SocialspyCommand extends EszCommand {

    @InjectManager
    private SocialSpyManager socialSpyManager;

    public SocialspyCommand(EssentialZ plugin) {
        super(plugin);
    }

    private UUID getViewerUUID(CommandSender sender) {
        if (sender instanceof Player player) return player.getUniqueId();
        else return EssentialZ.CONSOLE_UUID;
    }

    @Override
    public boolean execute(CommandSender commandSender, Command command, String s, ArgumentList args) throws CommandException {
        if (args.size() > 1) throw new CommandUsageException("Usage: /socialspy <player>");

        // toggle all off if no args
        if (args.size() == 0) {
            UUID viewerUuid = getViewerUUID(commandSender);
            socialSpyManager.turnOffAllSpying(viewerUuid);
            commandSender.sendMessage(getMessageManager().getAndFormatMsg(PluginMessage.SOCIALSPY_DISABLED_ALL));
            return true;
        }

        UUID viewerUuid = getViewerUUID(commandSender);
        Player targetPlayer = args.getPlayer(0, getPlugin());

        boolean nowSpying = socialSpyManager.toggleSpyOn(viewerUuid, targetPlayer.getUniqueId());

        if (nowSpying) {
            commandSender.sendMessage(getMessageManager().getAndFormatMsg(
                    PluginMessage.SOCIALSPY_ENABLED,
                    new MessageManager.Replaceable<>("{player}", targetPlayer.getName())
            ));
        } else {
            commandSender.sendMessage(getMessageManager().getAndFormatMsg(
                    PluginMessage.SOCIALSPY_DISABLED,
                    new MessageManager.Replaceable<>("{player}", targetPlayer.getName())
            ));
        }

        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender commandSender, Command command, ArgumentList args) {
        if (args.getCurrentArgIndex() == 0 && Permission.SOCIALSPY.has(commandSender)) {
            return getPlayerOptions(args.getCurrentArg());
        }
        return List.of();
    }
}
