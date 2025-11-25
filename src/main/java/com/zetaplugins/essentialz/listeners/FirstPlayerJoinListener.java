package com.zetaplugins.essentialz.listeners;

import com.zetaplugins.essentialz.storage.Storage;
import com.zetaplugins.essentialz.storage.model.PlayerData;
import com.zetaplugins.zetacore.annotations.AutoRegisterListener;
import com.zetaplugins.zetacore.annotations.InjectManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

@AutoRegisterListener
public class FirstPlayerJoinListener implements Listener {

    @InjectManager
    private Storage storage;

    @EventHandler
    public void onFirstPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PlayerData playerData = storage.load(player.getUniqueId());
        if (playerData != null) {
            PlayerData newPlayerData = new PlayerData(player.getUniqueId());
            storage.save(newPlayerData);
        }
    }
}
