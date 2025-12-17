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
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

@AutoRegisterCommand(
        commands = "homes",
        permission = "essentialz.homelist",
        description = "List all your homes.",
        usage = "/homes"
)
public class HomesCommand extends EszCommand {

    @InjectManager
    private Storage storage;

    public HomesCommand(EssentialZ plugin) {
        super(plugin);
    }

    @Override
    public boolean execute(CommandSender commandSender, Command command, String s, ArgumentList argumentList) throws CommandException {
        if (!(commandSender instanceof Player player)) throw new CommandSenderMustBePlayerException();

        List<HomeData> homes = storage.getHomesRepository().getAllHomes(player.getUniqueId());
        if (homes.isEmpty()) {
            player.sendMessage(getMessageManager().getAndFormatMsg(PluginMessage.NO_HOMES_SET));
            return true;
        }

        player.sendMessage(getMessageManager().getAndFormatMsg(PluginMessage.HOMES_LIST_HEADER, false));
        for (HomeData home : homes) {
            player.sendMessage(getMessageManager().getAndFormatMsg(
                    PluginMessage.HOMES_LIST_ENTRY,
                    false,
                    new MessageManager.Replaceable<>("{index}", String.valueOf(homes.indexOf(home) + 1)),
                    new MessageManager.Replaceable<>("{homeName}", home.getName()),
                    new MessageManager.Replaceable<>("{x}", String.valueOf(home.getLocation().getBlockX())),
                    new MessageManager.Replaceable<>("{y}", String.valueOf(home.getLocation().getBlockY())),
                    new MessageManager.Replaceable<>("{z}", String.valueOf(home.getLocation().getBlockZ()))
            ));
        }

        player.sendMessage(getMessageManager().getAndFormatMsg(PluginMessage.HOMES_LIST_FOOTER, false));

        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender commandSender, Command command, ArgumentList argumentList) {
        return List.of();
    }
}
