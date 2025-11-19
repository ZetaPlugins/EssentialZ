package com.zetaplugins.essentialz.commands;

import com.zetaplugins.essentialz.util.permissions.Permission;
import com.zetaplugins.zetacore.annotations.AutoRegisterCommand;
import org.bukkit.Statistic;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.zetaplugins.essentialz.EssentialZ;
import com.zetaplugins.essentialz.util.MessageManager;
import com.zetaplugins.essentialz.util.commands.ArgumentList;
import com.zetaplugins.essentialz.util.commands.CommandPermissionException;
import com.zetaplugins.essentialz.util.commands.CommandUsageException;
import com.zetaplugins.essentialz.util.commands.CustomCommand;

import java.util.List;

@AutoRegisterCommand(
        commands = "playtime",
        description = "Get the playtime of a player",
        permission = "essentialz.playtime",
        usage = "/playtime [player]"
)
public class PlayTimeCommand extends CustomCommand {
    public PlayTimeCommand(EssentialZ plugin) {
        super(plugin);
    }

    @Override
    public boolean execute(CommandSender sender, Command command, ArgumentList args) throws CommandPermissionException, CommandUsageException {
        if (args.hasArg(0)) {
            Player target = getPlugin().getServer().getPlayer(args.getArg(0));

            if (target == null) {
                sender.sendMessage(getPlugin().getMessageManager().getAndFormatMsg(
                        MessageManager.Style.ERROR,
                        "playerNotFound",
                        "&cPlayer not found."
                ));
                return false;
            }

            sender.sendMessage(getPlugin().getMessageManager().getAndFormatMsg(
                    MessageManager.Style.DEFAULT,
                    "playTime",
                    "&7{player} has played for {ac}{time}&7.",
                    new MessageManager.Replaceable<>("{player}", target.getName()),
                    new MessageManager.Replaceable<>("{time}", getPlayTime(target))
            ));
            return true;
        }

        if (!(sender instanceof Player player)) {
            sender.sendMessage(getPlugin().getMessageManager().getAndFormatMsg(
                    MessageManager.Style.ERROR,
                    "specifyPlayerOrBePlayer",
                    "&cYou must specify a player or be a player to use this command."
            ));
            return false;
        }

        sender.sendMessage(getPlugin().getMessageManager().getAndFormatMsg(
                MessageManager.Style.DEFAULT,
                "playTimeSelf",
                "&7You have played for {ac}{time}&7.",
                new MessageManager.Replaceable<>("{time}", getPlayTime(player))
        ));
        return true;
    }

    private String getPlayTime(Player player) {
        int playTime = player.getStatistic(Statistic.PLAY_ONE_MINUTE);
        int seconds = playTime / 20;

        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;

        return String.format("%dh %dm", hours, minutes);
    }

    @Override
    public boolean isAuthorized(CommandSender sender) {
        return Permission.PLAYTIME.has(sender);
    }

    @Override
    public List<String> tabComplete(CommandSender sender, Command command, ArgumentList args) {
        if (args.getCurrentArgIndex() == 0 || args.getCurrentArgIndex() == 1) {
            return getPlayerOptions(getPlugin(), args.getCurrentArg());
        }
        return List.of();
    }
}
