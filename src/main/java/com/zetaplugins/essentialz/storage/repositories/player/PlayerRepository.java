package com.zetaplugins.essentialz.storage.repositories.player;

import com.zetaplugins.essentialz.storage.model.PlayerData;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.UUID;

/**
 * Repository interface for managing player data storage and retrieval.
 */
public interface PlayerRepository {
    /**
     * Saves the player data to the storage system.
     *
     * @param playerData The player data to save.
     */
    void save(PlayerData playerData);

    /**
     * Loads the player data from the storage system.
     *
     * @param uuid The UUID of the player to load.
     * @return The player data of the player.
     */
    @Nullable
    PlayerData load(String uuid);

    /**
     * Loads the player data from the storage system.
     *
     * @param uuid The UUID of the player to load.
     * @return The player data of the player.
     */
    @Nullable
    PlayerData load(UUID uuid);

    /**
     * Gets the top N players with the highest balances.
     * @param topN The number of top players to retrieve.
     * @return A map of player UUIDs and their balances, sorted by balance in descending order.
     */
    Map<UUID, Double> getTopBalances(int topN);
}
