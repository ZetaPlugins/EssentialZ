package com.zetaplugins.essentialz.commands.movement.homes;

import com.zetaplugins.essentialz.EssentialZ;
import com.zetaplugins.essentialz.storage.Storage;
import com.zetaplugins.essentialz.storage.model.HomeData;
import com.zetaplugins.essentialz.util.MessageManager;
import com.zetaplugins.essentialz.util.PluginMessage;
import com.zetaplugins.essentialz.util.commands.EszCommand;
import com.zetaplugins.zetacore.annotations.AutoRegisterCommand;
import com.zetaplugins.zetacore.annotations.InjectManager;
import com.zetaplugins.zetacore.commands.ArgumentList;
import com.zetaplugins.zetacore.commands.exceptions.CommandException;
import com.zetaplugins.zetacore.commands.exceptions.CommandSenderMustBePlayerException;
import com.zetaplugins.zetacore.commands.exceptions.CommandUsageException;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

@AutoRegisterCommand(
        commands = "delhome",
        permission = "essentialz.delhome",
        description = "Deletes a home.",
        usage = "/delhome <name>"
)
public class DelHomeCommand extends EszCommand {

    @InjectManager
    private Storage storage;

    public DelHomeCommand(EssentialZ plugin) {
        super(plugin);
    }

    @Override
    public boolean execute(CommandSender commandSender, Command command, String s, ArgumentList args) throws CommandException {
        if (!(commandSender instanceof Player player)) throw new CommandSenderMustBePlayerException();

        String homeName = args.getArg(0);
        if (homeName == null || homeName.isEmpty()) throw new CommandUsageException("Usage: /delhome <name>");

        boolean success = storage.getHomesRepository().delete(player.getUniqueId(), homeName);
        if (!success) {
            commandSender.sendMessage(getMessageManager().getAndFormatMsg(
                    PluginMessage.HOME_DELETE_FAIL,
                    new MessageManager.Replaceable<>("{homeName}", homeName)
            ));
            return true;
        }

        player.sendMessage(getMessageManager().getAndFormatMsg(
                PluginMessage.HOME_DELETE_SUCCESS,
                new MessageManager.Replaceable<>("{homeName}", homeName)
        ));
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender commandSender, Command command, ArgumentList args) {
        if (args.getCurrentArgIndex() == 0 && (commandSender instanceof Player player)) {
            return getDisplayOptions(
                    storage.getHomesRepository().getAllHomes(player.getUniqueId()).stream().map(HomeData::getName).toList(),
                    args.getCurrentArg()
            );
        }
        return List.of();
    }
}
