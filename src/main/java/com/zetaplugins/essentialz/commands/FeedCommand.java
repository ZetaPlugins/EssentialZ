package com.zetaplugins.essentialz.commands;

import com.zetaplugins.essentialz.EssentialZ;
import com.zetaplugins.essentialz.util.MessageManager;
import com.zetaplugins.essentialz.util.PluginMessage;
import com.zetaplugins.essentialz.util.commands.EszCommand;
import com.zetaplugins.essentialz.util.permissions.Permission;
import com.zetaplugins.zetacore.annotations.AutoRegisterCommand;
import com.zetaplugins.zetacore.commands.ArgumentList;
import com.zetaplugins.zetacore.commands.exceptions.CommandSenderMustBeOrSpecifyPlayerException;
import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

@AutoRegisterCommand(
        commands = "feed",
        description = "Feed a player to full hunger",
        usage = "/feed [player]",
        permission = "essentialz.feed"
)
public class FeedCommand extends EszCommand {

    public FeedCommand(EssentialZ plugin) {
        super(plugin);
    }

    @Override
    public boolean execute(CommandSender sender, Command command, String label, ArgumentList args) throws CommandException, CommandSenderMustBeOrSpecifyPlayerException {
        Player targetPlayer = args.getPlayer(0, sender instanceof Player ? (Player) sender : null, getPlugin());

        if (targetPlayer == null) throw new CommandSenderMustBeOrSpecifyPlayerException();

        boolean isSelfFeed = (sender instanceof Player && ((Player) sender).getUniqueId().equals(targetPlayer.getUniqueId()));

        if (!isSelfFeed && !Permission.HEAL_OTHERS.has(sender)) {
            sender.sendMessage(getMessageManager().getAndFormatMsg(PluginMessage.NO_PERMISSION_FEED_OTHERS));
            return false;
        }

        targetPlayer.setFoodLevel(20);
        targetPlayer.setSaturation(20f);

        if (isSelfFeed) {
            sender.sendMessage(getMessageManager().getAndFormatMsg(PluginMessage.FEED_SELF));
        } else {
            sender.sendMessage(getMessageManager().getAndFormatMsg(
                    PluginMessage.FEED_OTHER,
                    new MessageManager.Replaceable<>("{player}", targetPlayer.getName())
            ));
        }

        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, Command command, ArgumentList args) {
        if (args.getCurrentArgIndex() == 0 && Permission.FEED_OTHERS.has(sender)) {
            return getPlayerOptions(args.getCurrentArg());
        }
        return List.of();
    }
}
