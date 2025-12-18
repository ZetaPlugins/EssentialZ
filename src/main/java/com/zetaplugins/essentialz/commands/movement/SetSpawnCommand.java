package com.zetaplugins.essentialz.commands.movement;

import com.zetaplugins.essentialz.EssentialZ;
import com.zetaplugins.essentialz.features.SpawnManager;
import com.zetaplugins.essentialz.util.MessageManager;
import com.zetaplugins.essentialz.util.PluginMessage;
import com.zetaplugins.essentialz.util.commands.EszCommand;
import com.zetaplugins.zetacore.annotations.AutoRegisterCommand;
import com.zetaplugins.zetacore.annotations.InjectManager;
import com.zetaplugins.zetacore.commands.ArgumentList;
import com.zetaplugins.zetacore.commands.exceptions.CommandException;
import com.zetaplugins.zetacore.commands.exceptions.CommandSenderMustBePlayerException;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

@AutoRegisterCommand(
        commands = "setspawn",
        permission = "essentialz.setspawn",
        description = "Sets the server spawn point to your current location.",
        usage = "/setspawn"
)
public class SetSpawnCommand extends EszCommand {

    @InjectManager
    private SpawnManager spawnManager;

    public SetSpawnCommand(EssentialZ plugin) {
        super(plugin);
    }

    @Override
    public boolean execute(CommandSender commandSender, Command command, String s, ArgumentList argumentList) throws CommandException {
        if (!(commandSender instanceof Player player)) throw new CommandSenderMustBePlayerException();

        spawnManager.setSpawnLocation(player.getLocation());
        commandSender.sendMessage(getMessageManager().getAndFormatMsg(
                PluginMessage.SPAWN_SET_SUCCESS,
                new MessageManager.Replaceable<>("{x}", String.valueOf(player.getLocation().getBlockX())),
                new MessageManager.Replaceable<>("{y}", String.valueOf(player.getLocation().getBlockY())),
                new MessageManager.Replaceable<>("{z}", String.valueOf(player.getLocation().getBlockZ()))
        ));
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender commandSender, Command command, ArgumentList argumentList) {
        return List.of();
    }
}
