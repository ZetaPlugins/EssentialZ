package com.zetaplugins.essentialz.commands.worldcontrol;

import com.zetaplugins.essentialz.EssentialZ;
import com.zetaplugins.essentialz.util.MessageManager;
import com.zetaplugins.essentialz.util.PluginMessage;
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
        commands = "sun",
        permission = "essentialz.weather",
        description = "Set the weather to clear in your world.",
        usage = "/sun"
)
public class SunCommand extends EszCommand {

    public SunCommand(EssentialZ plugin) {
        super(plugin);
    }

    @Override
    public boolean execute(CommandSender commandSender, Command command, String s, ArgumentList argumentList) throws CommandException {
        if (!(commandSender instanceof Player player)) throw new CommandSenderMustBePlayerException();

        player.getWorld().setStorm(false);
        player.getWorld().setThundering(false);

        player.sendMessage(getMessageManager().getAndFormatMsg(
                PluginMessage.WEATHER_CHANGED,
                new MessageManager.Replaceable<>("{weather}", "clear")
        ));
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender commandSender, Command command, ArgumentList argumentList) {
        return List.of();
    }
}
