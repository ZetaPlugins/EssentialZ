package com.zetaplugins.essentialz.commands.items;

import com.zetaplugins.essentialz.EssentialZ;
import com.zetaplugins.essentialz.util.MessageManager;
import com.zetaplugins.essentialz.util.commands.ArgumentList;
import com.zetaplugins.essentialz.util.commands.CommandPermissionException;
import com.zetaplugins.essentialz.util.commands.CommandUsageException;
import com.zetaplugins.essentialz.util.commands.CustomCommand;
import com.zetaplugins.essentialz.util.permissions.Permission;
import com.zetaplugins.zetacore.annotations.AutoRegisterCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@AutoRegisterCommand(
        commands = "more",
        description = "Gives you more of the item you are holding.",
        usage = "/<command>",
        permission = "essentialz.more"
)
public class MoreCommand extends CustomCommand {

    public MoreCommand(EssentialZ plugin) {
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

        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getType().isAir()) {
            sender.sendMessage(getPlugin().getMessageManager().getAndFormatMsg(
                    MessageManager.Style.ERROR,
                    "mustHoldAnItem",
                    "{ac}You must be holding an item!"
            ));
            return false;
        }

        if (item.getType().getMaxStackSize() == 1) {
            sender.sendMessage(getPlugin().getMessageManager().getAndFormatMsg(
                    MessageManager.Style.ERROR,
                    "itemCannotBeStacked",
                    "{ac}The item you are holding cannot be stacked!"
            ));
            return false;
        }

        item.setAmount(item.getType().getMaxStackSize());
        player.getInventory().setItemInMainHand(item);

        sender.sendMessage(getPlugin().getMessageManager().getAndFormatMsg(
                MessageManager.Style.ITEMS,
                "stackedItems",
                "&7Stacked {ac}{amount}&7 items in your hand.",
                new MessageManager.Replaceable<>("{amount}", String.valueOf(item.getAmount()))
        ));
        return true;
    }

    @Override
    public boolean isAuthorized(CommandSender sender) {
        return Permission.MORE.has(sender);
    }

    @Override
    public List<String> tabComplete(CommandSender sender, Command command, ArgumentList args) {
        return List.of();
    }
}
