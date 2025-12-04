package com.zetaplugins.essentialz.commands.movement.warps;

import com.zetaplugins.essentialz.EssentialZ;
import com.zetaplugins.essentialz.storage.Storage;
import com.zetaplugins.essentialz.util.MessageManager;
import com.zetaplugins.essentialz.util.PluginMessage;
import com.zetaplugins.essentialz.util.commands.EszCommand;
import com.zetaplugins.zetacore.annotations.AutoRegisterCommand;
import com.zetaplugins.zetacore.annotations.InjectManager;
import com.zetaplugins.zetacore.commands.ArgumentList;
import com.zetaplugins.zetacore.commands.exceptions.CommandException;
import com.zetaplugins.zetacore.commands.exceptions.CommandUsageException;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.List;

@AutoRegisterCommand(
        commands = "delwarp",
        description = "Delete a warp.",
        usage = "/delwarp <name>",
        permission = "essentialz.delwarp",
        aliases = {"deletewarp", "removewarp" }
)
public class DelWarpCommand extends EszCommand {

    @InjectManager
    private Storage storage;

    public DelWarpCommand(EssentialZ plugin) {
        super(plugin);
    }

    @Override
    public boolean execute(CommandSender commandSender, Command command, String s, ArgumentList argumentList) throws CommandException {
        String warpName = argumentList.getArg(0);
        if (warpName == null || warpName.isEmpty()) throw new CommandUsageException("/delwarp <name>");

        boolean success = storage.getWarpsRepository().delete(warpName);

        if (success) {
            commandSender.sendMessage(getMessageManager().getAndFormatMsg(
                    PluginMessage.WARP_DELETE_SUCCESS,
                    new MessageManager.Replaceable<>("{warpName}", warpName)
            ));
        } else {
            commandSender.sendMessage(getMessageManager().getAndFormatMsg(
                    PluginMessage.WARP_NOT_FOUND,
                    new MessageManager.Replaceable<>("{warpName}", warpName)
            ));
        }

        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender commandSender, Command command, ArgumentList args) {
        if (args.getCurrentArgIndex() == 0) return storage.getWarpsRepository().getAllWarpNames();
        return List.of();
    }
}
