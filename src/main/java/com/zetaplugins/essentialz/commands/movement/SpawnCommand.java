package com.zetaplugins.essentialz.commands.movement;

import com.zetaplugins.essentialz.EssentialZ;
import com.zetaplugins.essentialz.features.SpawnManager;
import com.zetaplugins.essentialz.util.PluginMessage;
import com.zetaplugins.essentialz.util.commands.EszCommand;
import com.zetaplugins.zetacore.annotations.AutoRegisterCommand;
import com.zetaplugins.zetacore.annotations.InjectManager;
import com.zetaplugins.zetacore.commands.ArgumentList;
import com.zetaplugins.zetacore.commands.exceptions.CommandException;
import com.zetaplugins.zetacore.commands.exceptions.CommandSenderMustBePlayerException;
import com.zetaplugins.zetacore.util.LocationDeserializationException;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.List;
import java.util.logging.Level;

@AutoRegisterCommand(
        commands = "spawn",
        permission = "essentialz.spawn",
        description = "Teleports you to the server spawn point.",
        usage = "/spawn"
)
public class SpawnCommand extends EszCommand {

    @InjectManager
    private SpawnManager spawnManager;

    public SpawnCommand(EssentialZ plugin) {
        super(plugin);
    }

    @Override
    public boolean execute(CommandSender commandSender, Command command, String s, ArgumentList argumentList) throws CommandException {
        if (!(commandSender instanceof org.bukkit.entity.Player player)) throw new CommandSenderMustBePlayerException();

        try {
            Location spawnLocation = spawnManager.getSpawnLocation();
            player.teleport(spawnLocation, PlayerTeleportEvent.TeleportCause.PLUGIN);
            return true;
        } catch (LocationDeserializationException e) {
            getPlugin().getLogger().log(Level.WARNING, "Failed to teleport player to spawn: " + e.getMessage());
            commandSender.sendMessage(getMessageManager().getAndFormatMsg(PluginMessage.NO_SPAWN_SET));
            return true;
        }
    }

    @Override
    public List<String> tabComplete(CommandSender commandSender, Command command, ArgumentList argumentList) {
        return List.of();
    }
}
