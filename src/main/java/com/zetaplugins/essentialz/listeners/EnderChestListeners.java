package com.zetaplugins.essentialz.listeners;

import com.zetaplugins.essentialz.inventory.holders.UnmodifiableEnderchestInventoryHolder;
import com.zetaplugins.essentialz.util.MessageManager;
import com.zetaplugins.essentialz.util.PluginMessage;
import com.zetaplugins.zetacore.annotations.AutoRegisterListener;
import com.zetaplugins.zetacore.annotations.InjectManager;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.InventoryHolder;

@AutoRegisterListener
public class EnderChestListeners implements Listener {

    @InjectManager
    private MessageManager messageManager;

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        InventoryHolder holder = event.getInventory().getHolder();

        if (holder instanceof UnmodifiableEnderchestInventoryHolder) {
            event.setCancelled(true); // read-only
            event.getWhoClicked().sendMessage(messageManager.getAndFormatMsg(PluginMessage.NO_ENDERCHEST_EDIT_ACCESS));
        }
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        if (event.getInventory().getHolder() instanceof UnmodifiableEnderchestInventoryHolder) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        InventoryHolder holder = event.getInventory().getHolder();
        Player player = (Player) event.getPlayer();

        if (holder instanceof UnmodifiableEnderchestInventoryHolder) {
            player.playSound(player.getLocation(), Sound.BLOCK_ENDER_CHEST_CLOSE, 1.0f, 1.0f);
        } else if (event.getInventory().getType() == InventoryType.ENDER_CHEST) {
            player.playSound(player.getLocation(), Sound.BLOCK_ENDER_CHEST_CLOSE, 1.0f, 1.0f);
        }
    }
}
