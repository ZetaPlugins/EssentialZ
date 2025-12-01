package com.zetaplugins.essentialz.commands.movement.warps;

import com.zetaplugins.essentialz.EssentialZ;
import com.zetaplugins.essentialz.storage.Storage;
import com.zetaplugins.essentialz.storage.model.WarpData;
import com.zetaplugins.essentialz.util.MessageManager;
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
        commands = "warp",
        description = "Teleport to a warp.",
        usage = "/warp <name>",
        permission = "essentialz.warp",
        aliases = {"warpto" }
)
public class WarpCommand extends EszCommand {

    @InjectManager
    private Storage storage;

    public WarpCommand(EssentialZ plugin) {
        super(plugin);
    }

    @Override
    public boolean execute(CommandSender commandSender, Command command, String label, ArgumentList args) throws CommandException {
        if (!(commandSender instanceof Player player)) throw new CommandSenderMustBePlayerException();

        String warpName = args.getArg(0);
        if (warpName == null || warpName.isEmpty()) throw new CommandUsageException("Usage: /warp <name>");

        WarpData warpData = storage.getWarpsRepository().load(warpName);
        if (warpData == null) {
            commandSender.sendMessage(getMessageManager().getAndFormatMsg(
                    MessageManager.Style.ERROR,
                    "warpNotFound",
                    "{ac}Warp '{warpName}' does not exist.",
                    new MessageManager.Replaceable<>("{warpName}", warpName)
            ));
            return true;
        }

        player.teleport(warpData.getLocation(), PlayerTeleportEvent.TeleportCause.PLUGIN);
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender commandSender, Command command, ArgumentList args) {
        if (args.getCurrentArgIndex() == 0) {
            return getDisplayOptions(storage.getWarpsRepository().getAllWarpNames(), args.getCurrentArg());
        }
        return List.of();
    }
}
