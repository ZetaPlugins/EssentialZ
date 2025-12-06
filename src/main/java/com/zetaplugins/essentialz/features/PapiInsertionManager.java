package com.zetaplugins.essentialz.features;

import com.zetaplugins.zetacore.annotations.Manager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Manager responsible for inserting PlaceholderAPI placeholders into messages
 */
@Manager
public class PapiInsertionManager {
    /**
     * Inserts PlaceholderAPI placeholders into a message for a specific player
     * @param message The message to insert placeholders into
     * @param player The player to use for placeholder values
     * @return The message with placeholders inserted
     */
    public String insertPapi(String message, Player player) {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null) return message;
        else return me.clip.placeholderapi.PlaceholderAPI.setPlaceholders(player, message); // Full qualified name to avoid import issues
    }
}
