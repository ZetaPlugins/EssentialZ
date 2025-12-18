package com.zetaplugins.essentialz.commands.worldcontrol.time;

import com.zetaplugins.essentialz.EssentialZ;
import com.zetaplugins.essentialz.util.MessageManager;
import com.zetaplugins.essentialz.util.PluginMessage;
import com.zetaplugins.essentialz.util.TimeConverter;
import com.zetaplugins.essentialz.util.commands.EszCommand;
import com.zetaplugins.zetacore.annotations.AutoRegisterCommand;
import com.zetaplugins.zetacore.commands.ArgumentList;
import com.zetaplugins.zetacore.commands.exceptions.CommandException;
import com.zetaplugins.zetacore.commands.exceptions.CommandSenderMustBePlayerException;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

@AutoRegisterCommand(
        commands = "time",
        permission = "essentialz.time",
        description = "Manage the time in your world.",
        usage = "/time [day|night|dawn|dusk|noon|midnight|18:20|10pm|3000ticks]"
)
public class TimeCommand extends EszCommand {

    public TimeCommand(EssentialZ plugin) {
        super(plugin);
    }

    @Override
    public boolean execute(CommandSender commandSender, Command command, String s, ArgumentList args) throws CommandException {
        if (!(commandSender instanceof Player player)) throw new CommandSenderMustBePlayerException();
        String timeToSet = args.getString(0, null);

        // Display time when no argument is provided
        if (timeToSet == null) {
            long currentTime = player.getWorld().getTime();
            player.sendMessage(getMessageManager().getAndFormatMsg(
                    PluginMessage.CURRENT_TIME,
                    new MessageManager.Replaceable<>("{time}", TimeConverter.ticksToTimeString(currentTime)),
                    new MessageManager.Replaceable<>("{ticks}", String.valueOf(currentTime))
            ));
            return true;
        }

        int ticks;
        switch (timeToSet.toLowerCase()) {
            case "day" -> ticks = 1000;
            case "night" -> ticks = 13000;
            case "dawn" -> ticks = 0;
            case "dusk" -> ticks = 12000;
            case "noon", "12:00" -> ticks = 6000;
            case "midnight", "00:00", "24:00" -> ticks = 18000;
            default -> {
                try {
                    if (timeToSet.endsWith("ticks")) ticks = Integer.parseInt(timeToSet.replace("ticks", "").trim());
                    else ticks = TimeConverter.timeStringToTicks(timeToSet);
                } catch (NumberFormatException e) {
                    commandSender.sendMessage(getMessageManager().getAndFormatMsg(PluginMessage.INVALID_TIME_FORMAT));
                    return true;
                }
            }
        }

        player.getWorld().setTime(ticks);
        player.sendMessage(getMessageManager().getAndFormatMsg(
                PluginMessage.TIME_SET,
                new MessageManager.Replaceable<>("{time}", TimeConverter.ticksToTimeString(ticks)),
                new MessageManager.Replaceable<>("{ticks}", String.valueOf(ticks))
        ));

        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender commandSender, Command command, ArgumentList args) {
        if (args.getCurrentArgIndex() == 0) {
            return List.of("day", "night", "dawn", "dusk", "noon", "midnight", "06:00", "12:00", "18:00", "00:00", "1000ticks", "13000ticks");
        }
        return List.of();
    }


}
