package com.zetaplugins.essentialz.events;

import com.zetaplugins.essentialz.util.EszConfig;
import com.zetaplugins.essentialz.util.MessageManager;
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

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerChat(AsyncChatEvent event) {
        if (!configManager.getConfig(EszConfig.CHAT).getBoolean("enableCustomChat", true)) return;
        event.setCancelled(true);
        Player player = event.getPlayer();

        if (!Permission.CHAT.has(event.getPlayer())) {
            player.sendMessage(messageManager.getAndFormatMsg(
                    MessageManager.Style.ERROR,
                    "notAllowedToChat",
                    "{ac}You are not allowed to chat."
            ));
            return;
        }

        boolean allowColors = Permission.CHAT_COLOR.has(event.getPlayer());

        String rawText = PlainTextComponentSerializer.plainText().serialize(event.message());
        String format = configManager.getConfig(EszConfig.CHAT).getString("chatFormat", "&7{player_displayname} &8» &f{message}");

        String rawDisplayName = PlainTextComponentSerializer.plainText().serialize(player.displayName());

        for (var onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.sendMessage(messageManager.formatMsg(
                    format,
                    new MessageManager.Replaceable<>("{player_displayname}", rawDisplayName),
                    new MessageManager.Replaceable<>("{player_name}", event.getPlayer().getName()),
                    new MessageManager.Replaceable<>("{message}", rawText, allowColors)
            ));
        }
    }
}
