package com.zetaplugins.essentialz.listeners;

import com.zetaplugins.essentialz.util.EszConfig;
import com.zetaplugins.essentialz.util.MessageManager;
import com.zetaplugins.essentialz.util.MessageStyle;
import com.zetaplugins.zetacore.annotations.AutoRegisterListener;
import com.zetaplugins.zetacore.annotations.InjectManager;
import com.zetaplugins.zetacore.annotations.InjectPlugin;
import com.zetaplugins.zetacore.services.config.ConfigService;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

@AutoRegisterListener
public class JoinMessageListener implements Listener {

    @InjectPlugin
    private JavaPlugin plugin;

    @InjectManager
    private MessageManager messageManager;
    @InjectManager
    private ConfigService configService;

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        FileConfiguration chatConfig = configService.getConfig(EszConfig.CHAT);

        if (!chatConfig.getBoolean("enableJoinMessages", true)) {
            event.joinMessage(null);
            return;
        }

        if (!event.getPlayer().hasPlayedBefore() && chatConfig.getBoolean("specialWelcomeJoinMessage", true)) {
            event.joinMessage(messageManager.getAndFormatMsg(
                    MessageStyle.NONE,
                    "welcomeMessage",
                    "&8[&a+&8] &7Welcome {player} to the server for the first time!",
                    new MessageManager.Replaceable<>("{player}", event.getPlayer().getName())
            ));
            return;
        }

        event.joinMessage(messageManager.getAndFormatMsg(
                MessageStyle.NONE,
                "joinMessage",
                "&8[&a+&8] &7{player} joined the game.",
                new MessageManager.Replaceable<>("{player}", event.getPlayer().getName())
        ));
    }
}
