package com.zetaplugins.essentialz.events;

import com.zetaplugins.essentialz.EssentialZ;
import com.zetaplugins.essentialz.storage.model.PlayerData;
import com.zetaplugins.zetacore.annotations.AutoRegisterListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

@AutoRegisterListener
public class FirstPlayerJoinListener implements Listener {
    private final EssentialZ plugin;

    public FirstPlayerJoinListener(EssentialZ plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onFirstPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PlayerData playerData = plugin.getStorage().load(player.getUniqueId());
        if (playerData != null) {
            PlayerData newPlayerData = new PlayerData(player.getUniqueId());
            plugin.getStorage().save(newPlayerData);
        }
    }
}
