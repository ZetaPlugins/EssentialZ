package com.zetaplugins.essentialz.listeners;

import com.zetaplugins.essentialz.features.tpa.TpaManager;
import com.zetaplugins.zetacore.annotations.AutoRegisterListener;
import com.zetaplugins.zetacore.annotations.InjectManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Listener for cleaning up TPA requests when players quit.
 */
@AutoRegisterListener
public class TpaPlayerQuitListener implements Listener {

    @InjectManager
    private TpaManager tpaManager;

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        tpaManager.clearPlayerRequests(event.getPlayer().getUniqueId());
    }
}
