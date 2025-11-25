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

import java.util.List;

@AutoRegisterCommand(
        commands = "setwarp",
        description = "Create a warp at your current location.",
        usage = "/setwarp <name>",
        permission = "essentialz.setwarp",
        aliases = {"createwarp" }
)
public class SetWarpCommand extends EszCommand {

    @InjectManager
    private Storage storage;

    public SetWarpCommand(EssentialZ plugin) {
        super(plugin);
    }

    @Override
    public boolean execute(CommandSender commandSender, Command command, String label, ArgumentList args) throws CommandException {
        if (!(commandSender instanceof Player player)) throw new CommandSenderMustBePlayerException();

        String warpName = args.getArg(0);
        if (warpName == null || warpName.isEmpty()) throw new CommandUsageException("/setwarp <name>");

        storage.saveWarp(new WarpData(warpName, player.getLocation()));

        player.sendMessage(getMessageManager().getAndFormatMsg(
                MessageManager.Style.MOVEMENT,
                "setWarpSucess",
                "&7Warp {ac}{warpName}&7 has been set at {ac}{x} {y} {z}&7.",
                new MessageManager.Replaceable<>("{warpName}", warpName),
                new MessageManager.Replaceable<>("{x}", String.valueOf(player.getLocation().getBlockX())),
                new MessageManager.Replaceable<>("{y}", String.valueOf(player.getLocation().getBlockY())),
                new MessageManager.Replaceable<>("{z}", String.valueOf(player.getLocation().getBlockZ()))
        ));
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender commandSender, Command command, ArgumentList args) {
        return List.of();
    }
}
