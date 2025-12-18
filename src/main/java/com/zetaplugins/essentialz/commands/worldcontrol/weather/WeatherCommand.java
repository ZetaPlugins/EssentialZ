package com.zetaplugins.essentialz.commands.worldcontrol.weather;

import com.zetaplugins.essentialz.EssentialZ;
import com.zetaplugins.essentialz.util.MessageManager;
import com.zetaplugins.essentialz.util.PluginMessage;
import com.zetaplugins.essentialz.util.commands.EszCommand;
import com.zetaplugins.zetacore.annotations.AutoRegisterCommand;
import com.zetaplugins.zetacore.commands.ArgumentList;
import com.zetaplugins.zetacore.commands.exceptions.CommandException;
import com.zetaplugins.zetacore.commands.exceptions.CommandSenderMustBePlayerException;
import com.zetaplugins.zetacore.commands.exceptions.CommandUsageException;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

@AutoRegisterCommand(
        commands = "weather",
        permission = "essentialz.weather",
        description = "Manage the weather in your world.",
        usage = "/weather <clear|rain|thunder> [duration]"
)
public class WeatherCommand extends EszCommand {

    public WeatherCommand(EssentialZ plugin) {
        super(plugin);
    }

    @Override
    public boolean execute(CommandSender commandSender, Command command, String s, ArgumentList args) throws CommandException {
        if (!(commandSender instanceof Player player)) throw new CommandSenderMustBePlayerException();

        if (args.size() < 1) throw new CommandUsageException("/weather <clear|rain|thunder> [duration]");
        String weatherType = args.getString(0, "").toLowerCase();

        if (!weatherType.equals("clear") && !weatherType.equals("rain") && !weatherType.equals("thunder")) {
            throw new CommandUsageException("/weather <clear|rain|thunder> [duration]");
        }

        int duration = 0; // default duration (5 minutes)
        if (args.size() >= 2) {
            String inputDuration = args.getString(1, "5m");
            try {
                duration = parseDurationToTicks(inputDuration);
            } catch (NumberFormatException e) {
                commandSender.sendMessage(getMessageManager().getAndFormatMsg(PluginMessage.INVALID_DURATION_FORMAT));
                return true;
            }
        }

        if (duration < 0) {
            commandSender.sendMessage(getMessageManager().getAndFormatMsg(PluginMessage.DURATION_MUST_BE_POSITIVE));
            return true;
        }

        switch (weatherType) {
            case "clear" -> player.getWorld().setStorm(false);
            case "rain" -> {
                player.getWorld().setStorm(true);
                player.getWorld().setThundering(false);
            }
            case "thunder" -> {
                player.getWorld().setStorm(true);
                player.getWorld().setThundering(true);
            }
        }

        if (duration != 0) {
            player.getWorld().setWeatherDuration(duration);
            commandSender.sendMessage(getMessageManager().getAndFormatMsg(
                    PluginMessage.WEATHER_CHANGED_TIME,
                    new MessageManager.Replaceable<>("{weather}", weatherType),
                    new MessageManager.Replaceable<>("{duration}", String.valueOf(duration / 20))
            ));
        } else {
            commandSender.sendMessage(getMessageManager().getAndFormatMsg(
                    PluginMessage.WEATHER_CHANGED,
                    new MessageManager.Replaceable<>("{weather}", weatherType)
            ));
        }


        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender commandSender, Command command, ArgumentList args) {
        if (args.getCurrentArgIndex() == 0) return List.of("clear", "rain", "thunder");
        else if (args.getCurrentArgIndex() == 1) return List.of("30s", "1m", "5m", "10m", "30m", "1h");
        else return List.of();
    }

    private int parseDurationToTicks(String input) throws NumberFormatException {
        int multiplier;
        if (input.endsWith("s")) {
            multiplier = 20; // seconds to ticks
            input = input.substring(0, input.length() - 1);
        } else if (input.endsWith("m")) {
            multiplier = 20 * 60; // minutes to ticks
            input = input.substring(0, input.length() - 1);
        } else if (input.endsWith("h")) {
            multiplier = 20 * 60 * 60; // hours to ticks
            input = input.substring(0, input.length() - 1);
        } else {
            throw new NumberFormatException("Invalid duration format");
        }
        int value = Integer.parseInt(input);
        return value * multiplier;
    }
}
