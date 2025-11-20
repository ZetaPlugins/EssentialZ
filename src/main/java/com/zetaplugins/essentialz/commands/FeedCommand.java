package com.zetaplugins.essentialz.commands;

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
        commands = "feed",
        description = "Feed a player to full hunger",
        usage = "/feed [player]",
        permission = "essentialz.feed"
)
public class FeedCommand extends CustomCommand {

    public FeedCommand(EssentialZ plugin) {
        super(plugin);
    }

    @Override
    public boolean execute(CommandSender sender, Command command, ArgumentList args) throws CommandPermissionException, CommandUsageException {
        Player targetPlayer = args.getPlayer(0, sender instanceof Player ? (Player) sender : null, getPlugin());

        if (targetPlayer == null) {
            sender.sendMessage(getPlugin().getMessageManager().getAndFormatMsg(
                    MessageManager.Style.ERROR,
                    "specifyPlayerOrBePlayer",
                    "{ac}You must specify a player or be a player to use this command."
            ));
            return false;
        }

        boolean isSelfFeed = (sender instanceof Player && ((Player) sender).getUniqueId().equals(targetPlayer.getUniqueId()));

        if (!isSelfFeed && !Permission.HEAL_OTHERS.has(sender)) {
            sender.sendMessage(getPlugin().getMessageManager().getAndFormatMsg(
                    MessageManager.Style.ERROR,
                    "noPermissionFeedOthers",
                    "{ac}You do not have permission to feed other players."
            ));
            return false;
        }

        targetPlayer.setFoodLevel(20);
        targetPlayer.setSaturation(20f);

        if (isSelfFeed) {
            sender.sendMessage(getPlugin().getMessageManager().getAndFormatMsg(
                    MessageManager.Style.SUCCESS,
                    "feededSelf",
                    "&7Your hunger has been fully restored."
            ));
        } else {
            sender.sendMessage(getPlugin().getMessageManager().getAndFormatMsg(
                    MessageManager.Style.SUCCESS,
                    "feededOther",
                    "&7You have fully restored {ac}{player}&7's hunger.",
                    new MessageManager.Replaceable<>("{player}", targetPlayer.getName())
            ));
        }

        return true;
    }

    @Override
    public boolean isAuthorized(CommandSender sender) {
        return Permission.FEED.has(sender);
    }

    @Override
    public List<String> tabComplete(CommandSender sender, Command command, ArgumentList args) {
        if (args.getCurrentArgIndex() == 0 && Permission.FEED_OTHERS.has(sender)) {
            return getPlayerOptions(getPlugin(), args.getCurrentArg());
        }
        return List.of();
    }
}
