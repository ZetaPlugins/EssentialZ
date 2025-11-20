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

import java.util.ArrayList;
import java.util.List;

@AutoRegisterCommand(
        commands = "enchant",
        description = "Apply an enchantment to the item you are holding.",
        usage = "/<command> <enchantment> [level]",
        permission = "essentialz.enchant",
        aliases = {"enchantment"}
)
public class EnchantCommand extends CustomCommand {

    public EnchantCommand(EssentialZ plugin) {
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

        int level = args.getInt(1, enchantment.getMaxLevel());

        heldItem.addUnsafeEnchantment(enchantment, level);
        sender.sendMessage(getPlugin().getMessageManager().getAndFormatMsg(
                MessageManager.Style.ITEMS,
                "enchantSuccess",
                "&7Successfully enchanted your item with {ac}{enchantment} &7level {ac}{level}&7.",
                new MessageManager.Replaceable<>("{enchantment}", enchantment.getKey().getKey()),
                new MessageManager.Replaceable<>("{level}", String.valueOf(level))
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
            return getDisplayOptions(
                    getPlugin().getEnchantmentManager().getAllEnchantmentNames(),
                    args.getCurrentArg()
            );
        } else if (args.getCurrentArgIndex() == 1) {
            Enchantment enchantment = getPlugin().getEnchantmentManager().getEnchantmentByKeyName(args.getArg(0));
            if (enchantment == null) return List.of("1");

            List<String> levels = new ArrayList<>();
            for (int i = 1; i <= enchantment.getMaxLevel(); i++) {
                levels.add(String.valueOf(i));
            }

            return getDisplayOptions(levels, args.getCurrentArg());
        }
        return List.of();
    }
}
