package com.zetaplugins.essentialz.commands.items;

import com.zetaplugins.essentialz.EssentialZ;
import com.zetaplugins.essentialz.util.commands.ArgumentList;
import com.zetaplugins.essentialz.util.commands.CommandPermissionException;
import com.zetaplugins.essentialz.util.commands.CommandUsageException;
import com.zetaplugins.essentialz.util.commands.CustomCommand;
import com.zetaplugins.zetacore.annotations.AutoRegisterCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

@AutoRegisterCommand(
        commands = {"craftingtable", "wb", "ct", "cartographytable", "smithingtable", "loom", "grindstone", "stonecutter", "anvil", "enchantingtable", "et"},
        description = "Open a workstation GUI.",
        usage = "/<command>",
        permission = "essentialz.workstation.%command%"
)
public class WorkstationCommands extends CustomCommand {

    public WorkstationCommands(EssentialZ plugin) {
        super(plugin);
    }

    @Override
    public boolean execute(CommandSender sender, Command command, ArgumentList args) throws CommandPermissionException, CommandUsageException {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(getPlugin().getMessageManager().getAndFormatMsg(
                    com.zetaplugins.essentialz.util.MessageManager.Style.ERROR,
                    "playerOnly",
                    "{ac}You must be a player to use this command."
            ));
            return false;
        }
        switch (command.getName()) {
            case "craftingtable", "ct", "wb" -> {
                player.openWorkbench(null, true);
                return true;
            }
            case "cartographytable" -> {
                player.openCartographyTable(null, true);
                return true;
            }
            case "smithingtable" -> {
                player.openSmithingTable(null, true);
                return true;
            }
            case "loom" -> {
                player.openLoom(null, true);
                return true;
            }
            case "grindstone" -> {
                player.openGrindstone(null, true);
                return true;
            }
            case "stonecutter" -> {
                player.openStonecutter(null, true);
                return true;
            }
            case "anvil" -> {
                player.openAnvil(null, true);
                return true;
            }
            case "enchantingtable", "et" -> {
                player.openEnchanting(null, true);
                return true;
            }
            default -> {
                return false;
            }
        }
    }

    @Override
    public boolean isAuthorized(CommandSender sender) {
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, Command command, ArgumentList args) {
        return List.of();
    }
}
