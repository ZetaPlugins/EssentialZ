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
        commands = {"day", "night", "noon", "midnight", "dawn", "dusk"},
        description = "Shortcut commands to set the time.",
        usage = "/<command>",
        permission = "essentialz.time"
)
public class TimeShortcutCommand extends EszCommand {

    public TimeShortcutCommand(EssentialZ plugin) {
        super(plugin);
    }

    @Override
    public boolean execute(CommandSender commandSender, Command command, String s, ArgumentList argumentList) throws CommandException {
        if (!(commandSender instanceof Player player)) throw new CommandSenderMustBePlayerException();

        int ticks;
        switch (command.getName().toLowerCase()) {
            case "day" -> ticks = 1000;
            case "night" -> ticks = 13000;
            case "dawn" -> ticks = 0;
            case "dusk" -> ticks = 12000;
            case "noon" -> ticks = 6000;
            case "midnight" -> ticks = 18000;
            default -> {
                commandSender.sendMessage(getMessageManager().getAndFormatMsg(PluginMessage.INVALID_TIME_FORMAT));
                return true;
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
    public List<String> tabComplete(CommandSender commandSender, Command command, ArgumentList argumentList) {
        return List.of();
    }
}
