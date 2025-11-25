package com.zetaplugins.essentialz.listeners;

import com.zetaplugins.essentialz.features.GodModeManager;
import com.zetaplugins.zetacore.annotations.AutoRegisterListener;
import com.zetaplugins.zetacore.annotations.InjectManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

@AutoRegisterListener
public class GodModeDamageListener implements Listener {

    @InjectManager
    private GodModeManager godModeManager;

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (godModeManager.isInGodMode(player)) event.setCancelled(true);
        }
    }
}
