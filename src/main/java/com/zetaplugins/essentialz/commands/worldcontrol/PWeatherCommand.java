package com.zetaplugins.essentialz.commands.worldcontrol;

import com.zetaplugins.essentialz.EssentialZ;
import com.zetaplugins.essentialz.util.LanguageManager;
import com.zetaplugins.essentialz.util.MessageManager;
import com.zetaplugins.essentialz.util.commands.EszCommand;
import com.zetaplugins.zetacore.annotations.AutoRegisterCommand;
import com.zetaplugins.zetacore.annotations.InjectManager;
import com.zetaplugins.zetacore.commands.ArgumentList;
import com.zetaplugins.zetacore.commands.exceptions.CommandException;
import com.zetaplugins.zetacore.commands.exceptions.CommandSenderMustBeOrSpecifyPlayerException;
import org.bukkit.WeatherType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

@AutoRegisterCommand(
        commands = "pweather",
        description = "Sets the weather for a specific player",
        usage = "/pweather <clear|rain> <player>",
        aliases = {"playerweather"},
        permission = "essentialz.pweather"
)
public class PWeatherCommand extends EszCommand {

    @InjectManager
    private MessageManager messageManager;
    @InjectManager
    private LanguageManager languageManager;

    public PWeatherCommand(EssentialZ plugin) {
        super(plugin);
    }

    @Override
    public boolean execute(CommandSender sender, Command command, String label, ArgumentList args) throws CommandException {
        EszWeatherType eszWeatherType = args.getEnumIgnoreCase(0, EszWeatherType.class, EszWeatherType.CLEAR);
        Player targetPlayer = args.getPlayer(1, (sender instanceof Player) ? (Player) sender : null, getPlugin());

        if (targetPlayer == null) throw new CommandSenderMustBeOrSpecifyPlayerException();

        targetPlayer.setPlayerWeather(eszWeatherType.getWeatherType());

        if (sender.equals(targetPlayer)) {
            targetPlayer.sendMessage(messageManager.getAndFormatMsg(
                    MessageManager.Style.WORLDCONTROL,
                    "pweatherSet",
                    "&7Your weather has been set to {ac}{weather}&7.",
                    new MessageManager.Replaceable<>("{weather}", eszWeatherType.getDisplayName(languageManager))
            ));
        } else {
            sender.sendMessage(messageManager.getAndFormatMsg(
                    MessageManager.Style.WORLDCONTROL,
                    "pweatherSetOther",
                    "&7You have set {ac}{player}&7's weather to {ac}{weather}&7.",
                    new MessageManager.Replaceable<>("{player}", targetPlayer.getName()),
                    new MessageManager.Replaceable<>("{weather}", eszWeatherType.getDisplayName(languageManager))
            ));
        }
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, Command command, ArgumentList args) {
        if (args.getCurrentArgIndex() == 0) {
            return getDisplayOptions(
                    Arrays.stream(EszWeatherType.values())
                            .map(eszWeatherType -> eszWeatherType.name().toLowerCase())
                            .toList(),
                    args.getCurrentArg()
            );
        }
        if (args.getCurrentArgIndex() == 1) {
            return getPlayerOptions(args.getCurrentArg());
        }
        return List.of();
    }

    private enum EszWeatherType {
        CLEAR(WeatherType.CLEAR),
        RAIN(WeatherType.DOWNFALL),
        ;

        private final WeatherType weatherType;

        EszWeatherType(WeatherType weatherType) {
            this.weatherType = weatherType;
        }

        public WeatherType getWeatherType() {
            return weatherType;
        }

        public String getDisplayName(LanguageManager languageManager) {
            return languageManager.getString("weather_" + this.name(), this.name().toLowerCase());
        }
    }
}
