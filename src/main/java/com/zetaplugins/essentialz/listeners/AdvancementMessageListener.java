package com.zetaplugins.essentialz.listeners;

import com.zetaplugins.essentialz.config.chat.ChatConfig;
import com.zetaplugins.zetacore.annotations.AutoRegisterListener;
import com.zetaplugins.zetacore.annotations.InjectManager;
import com.zetaplugins.zetacore.services.config.ConfigService;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;

@AutoRegisterListener
public class AdvancementMessageListener implements Listener {

    @InjectManager
    private ConfigService configService;

    @EventHandler
    public void onAdvancement(PlayerAdvancementDoneEvent event) {
        if (!configService.getConfig(ChatConfig.class).isEnableAdvancementMessages()) {
            event.message(null);
        }
    }
}
