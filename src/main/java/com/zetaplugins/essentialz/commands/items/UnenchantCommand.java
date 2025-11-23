package com.zetaplugins.essentialz.commands.items;

import com.zetaplugins.essentialz.EssentialZ;
import com.zetaplugins.essentialz.features.EnchantmentManager;
import com.zetaplugins.essentialz.util.MessageManager;
import com.zetaplugins.essentialz.util.commands.EszCommand;
import com.zetaplugins.zetacore.annotations.AutoRegisterCommand;
import com.zetaplugins.zetacore.annotations.InjectManager;
import com.zetaplugins.zetacore.commands.ArgumentList;
import com.zetaplugins.zetacore.commands.exceptions.CommandSenderMustBePlayerException;
import com.zetaplugins.zetacore.commands.exceptions.CommandUsageException;
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
public class UnenchantCommand extends EszCommand {

    @InjectManager
    private MessageManager messageManager;

    @InjectManager
    private EnchantmentManager enchantmentManager;

    public UnenchantCommand(EssentialZ plugin) {
        super(plugin);
    }

    @Override
    public boolean execute(CommandSender sender, Command command, String label, ArgumentList args) throws CommandUsageException, CommandSenderMustBePlayerException {
        if (!(sender instanceof Player player)) throw new CommandSenderMustBePlayerException();

        ItemStack heldItem = player.getInventory().getItemInMainHand();
        if (heldItem.getType().isAir()) {
            sender.sendMessage(messageManager.getAndFormatMsg(
                    MessageManager.Style.ERROR,
                    "mustHoldAnItem",
                    "ac}You must be holding an item!"
            ));
            return false;
        }

        String enchantmentName = args.getArg(0);
        if (enchantmentName == null) throw new CommandUsageException("/<command> <enchantment>");

        Enchantment enchantment = enchantmentManager.getEnchantmentByKeyName(enchantmentName);

        if (enchantment == null) {
            sender.sendMessage(messageManager.getAndFormatMsg(
                    MessageManager.Style.ERROR,
                    "invalidEnchantment",
                    "{ac}The enchantment '{enchantment}' does not exist.",
                    new MessageManager.Replaceable<>("{enchantment}", enchantmentName)
            ));
            return false;
        }

        if (!heldItem.containsEnchantment(enchantment)) {
            sender.sendMessage(messageManager.getAndFormatMsg(
                    MessageManager.Style.ERROR,
                    "itemLacksEnchantment",
                    "{ac}The item you are holding does not have the '{enchantment}' enchantment.",
                    new MessageManager.Replaceable<>("{enchantment}", enchantment.getKey().getKey())
            ));
            return false;
        }

        heldItem.removeEnchantment(enchantment);
        sender.sendMessage(messageManager.getAndFormatMsg(
                MessageManager.Style.ITEMS,
                "unenchantSuccess",
                "&7Successfully removed {ac}{enchantment} &7from your item.",
                new MessageManager.Replaceable<>("{enchantment}", enchantment.getKey().getKey())
        ));
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, Command command, ArgumentList args) {
        if (args.getCurrentArgIndex() == 0) {
            if (!(sender instanceof Player player) || player.getInventory().getItemInMainHand().getType().isAir()) {
                return getDisplayOptions(
                        enchantmentManager.getAllEnchantmentNames(),
                        args.getCurrentArg()
                );
            }

            ItemStack heldItem = player.getInventory().getItemInMainHand();
            return getDisplayOptions(enchantmentManager.getEnchantmentKeyNamesFromItem(heldItem), args.getCurrentArg());
        }
        return List.of();
    }
}
