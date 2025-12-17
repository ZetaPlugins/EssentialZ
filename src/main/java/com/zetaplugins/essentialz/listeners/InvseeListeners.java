package com.zetaplugins.essentialz.listeners;

import com.zetaplugins.essentialz.inventory.holders.UnmodifiablePlayerInventoryHolder;
import com.zetaplugins.zetacore.annotations.AutoRegisterListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

@AutoRegisterListener
public class InvseeListeners implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getInventory().getHolder() instanceof UnmodifiablePlayerInventoryHolder) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        if (event.getInventory().getHolder() instanceof UnmodifiablePlayerInventoryHolder) {
            event.setCancelled(true);
        }
    }
}
