package com.zetaplugins.essentialz.commands.movement.homes;

import com.zetaplugins.essentialz.EssentialZ;
import com.zetaplugins.essentialz.storage.Storage;
import com.zetaplugins.essentialz.storage.model.HomeData;
import com.zetaplugins.essentialz.storage.model.WarpData;
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
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.List;

@AutoRegisterCommand(
        commands = "home",
        permission = "essentialz.home",
        description = "Teleport to your home.",
        usage = "/home <name>"
)
public class HomeCommand extends EszCommand {

    @InjectManager
    private Storage storage;

    public HomeCommand(EssentialZ plugin) {
        super(plugin);
    }

    @Override
    public boolean execute(CommandSender commandSender, Command command, String s, ArgumentList args) throws CommandException {
        if (!(commandSender instanceof Player player)) throw new CommandSenderMustBePlayerException();

        String homeName = args.getArg(0);
        if (homeName == null || homeName.isEmpty()) throw new CommandUsageException("Usage: /home <name>");

        HomeData homeData = storage.getHomesRepository().load(player.getUniqueId(), homeName);
        if (homeData == null) {
            commandSender.sendMessage(getMessageManager().getAndFormatMsg(
                    PluginMessage.HOME_NOT_FOUND,
                    new MessageManager.Replaceable<>("{homeName}", homeName)
            ));
            return true;
        }

        player.teleport(homeData.getLocation(), PlayerTeleportEvent.TeleportCause.PLUGIN);
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
