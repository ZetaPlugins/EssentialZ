package org.strassburger.essentialz.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.strassburger.essentialz.EssentialZ;

public class EntityDamageListener implements Listener {
    private final EssentialZ plugin;

    public EntityDamageListener(EssentialZ plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (plugin.getGodModeManager().isInGodMode(player)) event.setCancelled(true);
        }
    }
}
