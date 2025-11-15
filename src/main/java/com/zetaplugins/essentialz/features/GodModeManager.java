package com.zetaplugins.essentialz.features;

import org.bukkit.OfflinePlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GodModeManager {
    private final List<UUID> godModePlayers = new ArrayList<>();

    /**
     * Checks if a player is in god mode
     * @param player The player to check
     * @return Weather the player is in god mode
     */
    public boolean isInGodMode(OfflinePlayer player) {
        return isInGodMode(player.getUniqueId());
    }

    /**
     * Checks if a player is in god mode
     * @param player UUID of the player to check
     * @return Weather the player is in god mode
     */
    public boolean isInGodMode(UUID player) {
        return godModePlayers.contains(player);
    }

    /**
     * Sets a player's god mode
     * @param player The player to set the god mode of
     * @param godMode Weather the player should be in god mode
     */
    public void setGodMode(OfflinePlayer player, boolean godMode) {
        setGodMode(player.getUniqueId(), godMode);
    }

    /**
     * Sets a player's god mode
     * @param player UUID of the player to set the god mode of
     * @param godMode Weather the player should be in god mode
     */
    public void setGodMode(UUID player, boolean godMode) {
        if (godMode) {
            godModePlayers.add(player);
        } else {
            godModePlayers.remove(player);
        }
    }
}
