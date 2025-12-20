package com.zetaplugins.essentialz.listeners;

import com.zetaplugins.essentialz.config.chat.ChatConfig;
import com.zetaplugins.essentialz.util.MessageManager;
import com.zetaplugins.essentialz.util.PluginMessage;
import com.zetaplugins.zetacore.annotations.AutoRegisterListener;
import com.zetaplugins.zetacore.annotations.InjectManager;
import com.zetaplugins.zetacore.services.config.ConfigService;
import net.kyori.adventure.text.Component;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

@AutoRegisterListener
public class CustomDeathMessageListener implements Listener {

    @InjectManager
    private MessageManager messageManager;
    @InjectManager
    private ConfigService configService;

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        if (!configService.getConfig(ChatConfig.class).isEnableDeathMessages()) return;

        Component originalMessage = event.deathMessage();
        if (originalMessage == null) return;

        event.deathMessage(messageManager.getAndFormatMsg(
                PluginMessage.DEATH_MESSAGE,
                new MessageManager.Replaceable<>("{message}", originalMessage)
        ));
    }
}
