package com.zetaplugins.essentialz.commands.items;

import com.zetaplugins.essentialz.EssentialZ;
import com.zetaplugins.essentialz.util.MessageManager;
import com.zetaplugins.essentialz.util.commands.ArgumentList;
import com.zetaplugins.essentialz.util.commands.CommandPermissionException;
import com.zetaplugins.essentialz.util.commands.CommandUsageException;
import com.zetaplugins.essentialz.util.commands.CustomCommand;
import com.zetaplugins.essentialz.util.permissions.Permission;
import com.zetaplugins.zetacore.annotations.AutoRegisterCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.List;

@AutoRegisterCommand(
        commands = "trash",
        description = "Open a trash can inventory to delete items.",
        usage = "/trash",
        aliases = {"bin"},
        permission = "essentialz.trash"
)
public class Trash extends CustomCommand {

    public Trash(EssentialZ plugin) {
        super(plugin);
    }

    @Override
    public boolean execute(CommandSender sender, Command command, ArgumentList args) throws CommandPermissionException, CommandUsageException {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(getPlugin().getMessageManager().getAndFormatMsg(
                    MessageManager.Style.ERROR,
                    "playerOnly",
                    "{ac}You must be a player to use this command."
            ));
            return false;
        }

        Inventory trashInventory = Bukkit.createInventory(
                null,
                5 * 9,
                getPlugin().getMessageManager().getAndFormatMsg(
                        MessageManager.Style.NONE,
                        "trashInventoryTitle",
                        "&8Trash Can"
                )
        );

        player.openInventory(trashInventory);

        return true;
    }

    @Override
    public boolean isAuthorized(CommandSender sender) {
        return Permission.TRASH.has(sender);
    }

    @Override
    public List<String> tabComplete(CommandSender sender, Command command, ArgumentList args) {
        return List.of();
    }
}
