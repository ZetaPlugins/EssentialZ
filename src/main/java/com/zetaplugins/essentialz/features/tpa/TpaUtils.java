package com.zetaplugins.essentialz.features.tpa;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility methods for TPA functionality.
 */
public final class TpaUtils {

    private TpaUtils() {}

    /**
     * Finds a player by name with partial matching support.
     * @param name The name to search for
     * @return The matched player or null if no unique match found
     */
    public static Player findPlayer(String name) {
        // Try exact match first
        Player exact = Bukkit.getPlayerExact(name);
        if (exact != null) return exact;

        // Try partial match
        List<Player> matches = new ArrayList<>();
        String lowerName = name.toLowerCase();

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getName().toLowerCase().startsWith(lowerName)) {
                matches.add(player);
            }
        }

        // Return if single match found
        if (matches.size() == 1) {
            return matches.get(0);
        }

        // Try contains match if no startsWith matches
        if (matches.isEmpty()) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.getName().toLowerCase().contains(lowerName)) {
                    matches.add(player);
                }
            }

            if (matches.size() == 1) {
                return matches.get(0);
            }
        }

        return null;
    }

    /**
     * Plays a sound to a player if sounds are enabled.
     * @param player The player to play the sound to
     * @param soundName The name of the sound
     * @param plugin The plugin instance
     */
    public static void playSound(Player player, String soundName, JavaPlugin plugin) {
        if (player == null || soundName == null) return;

        if (!plugin.getConfig().getBoolean("tpa.sounds.enabled", true)) return;

        try {
            Sound sound = Sound.valueOf(soundName);
            player.playSound(player.getLocation(), sound, 1.0f, 1.0f);
        } catch (IllegalArgumentException e) {
            plugin.getLogger().warning("Invalid TPA sound: " + soundName);
        }
    }
}
