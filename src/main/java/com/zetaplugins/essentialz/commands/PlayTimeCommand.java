package com.zetaplugins.essentialz.commands;

import com.zetaplugins.essentialz.EssentialZ;
import com.zetaplugins.essentialz.util.MessageManager;
import com.zetaplugins.essentialz.util.PluginMessage;
import com.zetaplugins.essentialz.util.commands.EszCommand;
import com.zetaplugins.zetacore.annotations.AutoRegisterCommand;
import com.zetaplugins.zetacore.commands.ArgumentList;
import com.zetaplugins.zetacore.commands.exceptions.CommandSenderMustBeOrSpecifyPlayerException;
import org.bukkit.Statistic;
import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

import static com.zetaplugins.essentialz.util.PluginMessage.PLAYER_NOT_FOUND;
import static com.zetaplugins.essentialz.util.PluginMessage.PLAYTIME_SELF;

@AutoRegisterCommand(
        commands = "playtime",
        description = "Get the playtime of a player",
        permission = "essentialz.playtime",
        usage = "/playtime [player]"
)
public class PlayTimeCommand extends EszCommand {

    public PlayTimeCommand(EssentialZ plugin) {
        super(plugin);
    }

    @Override
    public boolean execute(CommandSender sender, Command command, String label, ArgumentList args) throws CommandException, CommandSenderMustBeOrSpecifyPlayerException {
        if (args.hasArg(0)) {
            Player target = getPlugin().getServer().getPlayer(args.getArg(0));

            if (target == null) {
                sender.sendMessage(getMessageManager().getAndFormatMsg(PLAYER_NOT_FOUND));
                return false;
            }

            sender.sendMessage(getMessageManager().getAndFormatMsg(
                    PluginMessage.PLAYTIME,
                    new MessageManager.Replaceable<>("{player}", target.getName()),
                    new MessageManager.Replaceable<>("{time}", getPlayTime(target))
            ));
            return true;
        }

        if (!(sender instanceof Player player)) throw new CommandSenderMustBeOrSpecifyPlayerException();

        sender.sendMessage(getMessageManager().getAndFormatMsg(
                PLAYTIME_SELF,
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
    public List<String> tabComplete(CommandSender sender, Command command, ArgumentList args) {
        if (args.getCurrentArgIndex() == 0 || args.getCurrentArgIndex() == 1) {
            return getPlayerOptions(args.getCurrentArg());
        }
        return List.of();
    }

}
