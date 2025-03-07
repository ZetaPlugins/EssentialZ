package org.strassburger.essentialz.util;

import org.bukkit.event.Listener;
import org.strassburger.essentialz.EssentialZ;
import org.strassburger.essentialz.events.EntityDamageListener;

public class EventManager {
    private final EssentialZ plugin;

    public EventManager(EssentialZ plugin) {
        this.plugin = plugin;
    }

    /**
     * Registers all listeners
     */
    public void registerListeners() {
        registerListener(new EntityDamageListener(plugin));
    }

    /**
     * Registers a listener
     *
     * @param listener The listener to register
     */
    private void registerListener(Listener listener) {
        plugin.getServer().getPluginManager().registerEvents(listener, plugin);
    }
}
