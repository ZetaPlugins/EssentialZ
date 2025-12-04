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
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

@AutoRegisterListener
public class LeaveMessageListener implements Listener {

    @InjectPlugin
    private JavaPlugin plugin;

    @InjectManager
    private MessageManager messageManager;
    @InjectManager
    private ConfigService configService;

    @EventHandler
    public void onPlayerJoin(PlayerQuitEvent event) {
        FileConfiguration chatConfig = configService.getConfig(EszConfig.CHAT);
        if (chatConfig.getBoolean("enableLeaveMessages", true)) {
            event.quitMessage(messageManager.getAndFormatMsg(
                    MessageStyle.NONE,
                    "quitMessage",
                    "&8[&c-&8] &7{player} left the game.",
                    new MessageManager.Replaceable<>("{player}", event.getPlayer().getName())
            ));
        } else {
            event.quitMessage(null);
        }
    }
}
