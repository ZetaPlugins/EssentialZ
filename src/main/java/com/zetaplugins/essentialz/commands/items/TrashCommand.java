package com.zetaplugins.essentialz.commands.items;

import com.zetaplugins.essentialz.EssentialZ;
import com.zetaplugins.essentialz.util.MessageStyle;
import com.zetaplugins.essentialz.util.commands.EszCommand;
import com.zetaplugins.zetacore.annotations.AutoRegisterCommand;
import com.zetaplugins.zetacore.commands.ArgumentList;
import com.zetaplugins.zetacore.commands.exceptions.CommandSenderMustBePlayerException;
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
public class TrashCommand extends EszCommand {

    public TrashCommand(EssentialZ plugin) {
        super(plugin);
    }

    @Override
    public boolean execute(CommandSender sender, Command command, String label, ArgumentList args) throws CommandSenderMustBePlayerException {
        if (!(sender instanceof Player player)) throw new CommandSenderMustBePlayerException();

        Inventory trashInventory = Bukkit.createInventory(
                null,
                5 * 9,
                getMessageManager().getAndFormatMsg(
                        MessageStyle.NONE,
                        "trashInventoryTitle",
                        "&8Trash Can"
                )
        );

        player.openInventory(trashInventory);

        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, Command command, ArgumentList args) {
        return List.of();
    }
}
