package com.zetaplugins.essentialz.listeners;

import com.zetaplugins.essentialz.storage.Storage;
import com.zetaplugins.essentialz.storage.model.PlayerData;
import com.zetaplugins.essentialz.util.EszConfig;
import com.zetaplugins.zetacore.annotations.AutoRegisterListener;
import com.zetaplugins.zetacore.annotations.InjectManager;
import com.zetaplugins.zetacore.services.config.ConfigService;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

@AutoRegisterListener
public class FirstPlayerJoinListener implements Listener {

    @InjectManager
    private Storage storage;

    @InjectManager
    private ConfigService configService;

    @EventHandler
    public void onFirstPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PlayerData playerData = storage.getPlayerRepository().load(player.getUniqueId());
        if (playerData != null) {
            PlayerData newPlayerData = new PlayerData(player.getUniqueId());
            double startingBalance = configService.getConfig(EszConfig.ECONOMY)
                    .getDouble("startingBalance", 1000.0);
            newPlayerData.setBalance(startingBalance);
            storage.getPlayerRepository().save(newPlayerData);
        }
    }
}
