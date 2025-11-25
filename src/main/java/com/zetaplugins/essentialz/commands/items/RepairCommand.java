package com.zetaplugins.essentialz.commands.items;

import com.zetaplugins.essentialz.EssentialZ;
import com.zetaplugins.essentialz.util.MessageManager;
import com.zetaplugins.essentialz.util.commands.EszCommand;
import com.zetaplugins.zetacore.annotations.AutoRegisterCommand;
import com.zetaplugins.zetacore.commands.ArgumentList;
import com.zetaplugins.zetacore.commands.exceptions.CommandSenderMustBePlayerException;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

@AutoRegisterCommand(
        commands = "repair",
        description = "Repair your held item",
        usage = "/repair [hand|all]",
        permission = "essentialz.repair"
)
public class RepairCommand extends EszCommand {

    public RepairCommand(EssentialZ plugin) {
        super(plugin);
    }

    @Override
    public boolean execute(CommandSender sender, Command command, String label, ArgumentList args) throws CommandSenderMustBePlayerException {
        String operation = args.getString(0, "hand").toLowerCase();

        if (!(sender instanceof Player player)) throw new CommandSenderMustBePlayerException();

        if (operation.equals("all")) {
            int repairedCount = repairAllItems(player);
            sender.sendMessage(getMessageManager().getAndFormatMsg(
                    MessageManager.Style.SUCCESS,
                    "itemsRepairedAll",
                    "&7Repaired {ac}{count} &7items in your inventory.",
                    new MessageManager.Replaceable<>("{count}", String.valueOf(repairedCount))
            ));
            return true;
        }

        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getType().isAir()) {
            sender.sendMessage(getMessageManager().getAndFormatMsg(
                    MessageManager.Style.ERROR,
                    "repairMustHoldAnItem",
                    "{ac}You must be holding an item to repair."
            ));
            return false;
        }

        boolean success = repair(item);

        if (!success) {
            sender.sendMessage(getMessageManager().getAndFormatMsg(
                    MessageManager.Style.ERROR,
                    "itemNotRepairable",
                    "{ac}The item you are holding cannot be repaired."
            ));
            return false;
        }

        sender.sendMessage(getMessageManager().getAndFormatMsg(
                MessageManager.Style.SUCCESS,
                "itemRepaired",
                "&7Your held item has been repaired."
        ));

        return true;
    }

    /**
     * Repairs all items in a player's inventory
     * @param player The player whose items to repair
     * @return The number of items repaired
     */
    private int repairAllItems(Player player) {
        int repairedCount = 0;
        for (var item : player.getInventory().getContents()) {
            if (repair(item)) repairedCount++;
        }
        return repairedCount;
    }

    /**
     * Repairs a single item
     * @param item The item to repair
     * @return True if the item was repaired, false otherwise
     */
    private boolean repair(ItemStack item) {
        if (item == null) return false;
        ItemMeta meta = item.getItemMeta();
        if (meta instanceof Damageable damageable) {
            if (damageable.getDamage() == 0) return false;
            damageable.setDamage(0);
            item.setItemMeta(meta);
            return true;
        }
        return false;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, Command command, ArgumentList args) {
        if (args.getCurrentArgIndex() == 0) {
            return List.of("hand", "all");
        }
        return List.of();
    }
}
