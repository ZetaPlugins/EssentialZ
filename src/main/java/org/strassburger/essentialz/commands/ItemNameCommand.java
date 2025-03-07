package org.strassburger.essentialz.commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.strassburger.essentialz.EssentialZ;
import org.strassburger.essentialz.util.MessageManager;
import org.strassburger.essentialz.util.commands.ArgumentList;
import org.strassburger.essentialz.util.commands.CommandPermissionException;
import org.strassburger.essentialz.util.commands.CommandUsageException;
import org.strassburger.essentialz.util.commands.CustomCommand;

import java.util.List;

public class ItemNameCommand extends CustomCommand {
    public ItemNameCommand(String name, EssentialZ plugin) {
        super(name, plugin);
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

        if (item.getType() == Material.AIR) {
            sender.sendMessage(getPlugin().getMessageManager().getAndFormatMsg(
                    MessageManager.Style.ERROR,
                    "mustHoldAnItem",
                    "{ac}You must be holding an item."
            ));
            return false;
        }

        StringBuilder newName = new StringBuilder();
        for (String arg : args) {
            newName.append(arg);
            if (!arg.equals(args.getArg(args.size() - 1))) {
                newName.append(" ");
            }
        }

        int maxLength = getPlugin().getConfig().getInt("maxItemNameLength");

        if (newName.length() > maxLength && maxLength != -1) {
            sender.sendMessage(getPlugin().getMessageManager().getAndFormatMsg(
                    MessageManager.Style.ERROR,
                    "itemNameTooLong",
                    "{ac}The item name is too long! The maximum length is {max} characters.",
                    new MessageManager.Replaceable<>("{max}", String.valueOf(maxLength))
            ));
            return false;
        }

        ItemMeta newMeta = item.getItemMeta();
        newMeta.displayName(getPlugin().getMessageManager().formatMsg(newName.toString()));
        item.setItemMeta(newMeta);

        sender.sendMessage(getPlugin().getMessageManager().getAndFormatMsg(
                MessageManager.Style.DEFAULT,
                "itemNameSet",
                "&7Item name set to '{name}&r&7'.",
                new MessageManager.Replaceable<>("{name}", newName.toString())
        ));

        return true;
    }

    @Override
    public boolean isAuthorized(CommandSender sender) {
        return sender.hasPermission("essentialz.itemname");
    }

    @Override
    public List<String> tabComplete(CommandSender sender, Command command, ArgumentList args) {
        return List.of();
    }
}
