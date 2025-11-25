package com.zetaplugins.essentialz.commands.movement;

import com.zetaplugins.essentialz.EssentialZ;
import com.zetaplugins.essentialz.util.commands.EszCommand;
import com.zetaplugins.zetacore.annotations.AutoRegisterCommand;
import com.zetaplugins.zetacore.commands.ArgumentList;
import com.zetaplugins.zetacore.commands.exceptions.CommandException;
import com.zetaplugins.zetacore.commands.exceptions.CommandSenderMustBePlayerException;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

@AutoRegisterCommand(
        commands = "top",
        description = "Teleport to the highest block at your current location.",
        usage = "/top",
        permission = "essentialz.top"
)
public class TopCommand extends EszCommand {

    public TopCommand(EssentialZ plugin) {
        super(plugin);
    }

    @Override
    public boolean execute(CommandSender sender, Command command, String label, ArgumentList args) throws CommandException {
        if (!(sender instanceof Player player)) throw new CommandSenderMustBePlayerException();

        int highestY = player.getWorld().getHighestBlockYAt(player.getLocation());
        Location location = player.getLocation();
        location.setY(highestY + 1);
        player.teleport(location);

        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, Command command, ArgumentList args) {
        return List.of();
    }
}
