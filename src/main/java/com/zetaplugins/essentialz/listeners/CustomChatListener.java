package com.zetaplugins.essentialz.listeners;

import com.zetaplugins.essentialz.config.chat.ChatConfig;
import com.zetaplugins.essentialz.features.papi.PapiInsertionManager;
import com.zetaplugins.essentialz.util.EszConfig;
import com.zetaplugins.essentialz.util.MessageManager;
import com.zetaplugins.essentialz.util.MessageStyle;
import com.zetaplugins.essentialz.util.permissions.Permission;
import com.zetaplugins.zetacore.annotations.AutoRegisterListener;
import com.zetaplugins.zetacore.annotations.InjectManager;
import com.zetaplugins.zetacore.services.config.ConfigService;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

@AutoRegisterListener
public class CustomChatListener implements Listener {

    @InjectManager
    private ConfigService configManager;
    @InjectManager
    private MessageManager messageManager;
    @InjectManager
    private PapiInsertionManager papiInsertionManager;

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerChat(AsyncChatEvent event) {
        ChatConfig chatConfig = configManager.getConfig(ChatConfig.class);
        if (!chatConfig.isEnableCustomChat()) return;
        event.setCancelled(true);
        Player player = event.getPlayer();

        if (!Permission.CHAT.has(event.getPlayer())) {
            player.sendMessage(messageManager.getAndFormatMsg(
                    MessageStyle.ERROR,
                    "notAllowedToChat",
                    "{ac}You are not allowed to chat."
            ));
            return;
        }

        boolean allowColors = Permission.CHAT_COLOR.has(event.getPlayer());

        String rawText = PlainTextComponentSerializer.plainText().serialize(event.message());
        String format = chatConfig.getChatFormat();
        String papiProcessedFormat = papiInsertionManager.insertPapi(format, player);

        String rawDisplayName = PlainTextComponentSerializer.plainText().serialize(player.displayName());

        for (var onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.sendMessage(messageManager.formatMsg(
                    papiProcessedFormat,
                    new MessageManager.Replaceable<>("{player_displayname}", rawDisplayName),
                    new MessageManager.Replaceable<>("{player_name}", event.getPlayer().getName()),
                    new MessageManager.Replaceable<>("{message}", rawText, allowColors)
            ));
        }
    }
}
