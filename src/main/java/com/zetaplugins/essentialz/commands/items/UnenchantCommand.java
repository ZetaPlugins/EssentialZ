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
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@AutoRegisterCommand(
        commands = "unenchant",
        description = "Remove an enchantment from the item you are holding.",
        usage = "/<command> <enchantment>",
        permission = "essentialz.enchant"
)
public class UnenchantCommand extends CustomCommand {

    public UnenchantCommand(EssentialZ plugin) {
        super(plugin);
    }

    @Override
    public boolean execute(CommandSender sender, Command command, ArgumentList args) throws CommandPermissionException, CommandUsageException {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(getPlugin().getMessageManager().getAndFormatMsg(
                    MessageManager.Style.ERROR,
                    "bePlayerError",
                    "{ac}You must be a player to use this command."
            ));
            return false;
        }

        ItemStack heldItem = player.getInventory().getItemInMainHand();
        if (heldItem.getType().isAir()) {
            sender.sendMessage(getPlugin().getMessageManager().getAndFormatMsg(
                    MessageManager.Style.ERROR,
                    "mustHoldAnItem",
                    "ac}You must be holding an item!"
            ));
            return false;
        }

        String enchantmentName = args.getArg(0);
        if (enchantmentName == null) throw new CommandUsageException("/<command> <enchantment> [level]");

        Enchantment enchantment = getPlugin().getEnchantmentManager().getEnchantmentByKeyName(enchantmentName);

        if (enchantment == null) {
            sender.sendMessage(getPlugin().getMessageManager().getAndFormatMsg(
                    MessageManager.Style.ERROR,
                    "invalidEnchantment",
                    "{ac}The enchantment '{enchantment}' does not exist.",
                    new MessageManager.Replaceable<>("{enchantment}", enchantmentName)
            ));
            return false;
        }

        if (!heldItem.containsEnchantment(enchantment)) {
            sender.sendMessage(getPlugin().getMessageManager().getAndFormatMsg(
                    MessageManager.Style.ERROR,
                    "itemLacksEnchantment",
                    "{ac}The item you are holding does not have the '{enchantment}' enchantment.",
                    new MessageManager.Replaceable<>("{enchantment}", enchantment.getKey().getKey())
            ));
            return false;
        }

        heldItem.removeEnchantment(enchantment);
        sender.sendMessage(getPlugin().getMessageManager().getAndFormatMsg(
                MessageManager.Style.ITEMS,
                "unenchantSuccess",
                "&7Successfully removed {ac}{enchantment} &7from your item.",
                new MessageManager.Replaceable<>("{enchantment}", enchantment.getKey().getKey())
        ));
        return true;
    }

    @Override
    public boolean isAuthorized(CommandSender sender) {
        return Permission.ENCHANT.has(sender);
    }

    @Override
    public List<String> tabComplete(CommandSender sender, Command command, ArgumentList args) {
        if (args.getCurrentArgIndex() == 0) {
            if (!(sender instanceof Player player) || player.getInventory().getItemInMainHand().getType().isAir()) {
                return getDisplayOptions(
                        getPlugin().getEnchantmentManager().getAllEnchantmentNames(),
                        args.getCurrentArg()
                );
            }

            ItemStack heldItem = player.getInventory().getItemInMainHand();
            return getDisplayOptions(getPlugin().getEnchantmentManager().getEnchantmentKeyNamesFromItem(heldItem), args.getCurrentArg());
        }
        return List.of();
    }
}
