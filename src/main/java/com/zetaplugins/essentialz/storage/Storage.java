package com.zetaplugins.essentialz.storage;

import com.zetaplugins.essentialz.EssentialZ;
import com.zetaplugins.essentialz.storage.model.PlayerData;

import java.util.UUID;

public abstract class Storage {

    // Private field to store the Plugin instance
    private final EssentialZ plugin;

    // Constructor to initialize the Plugin instance
    public Storage(EssentialZ plugin) {
        this.plugin = plugin;
    }

    // Getter method for the Plugin instance (if needed)
    protected EssentialZ getPlugin() {
        return plugin;
    }

    /**
     * Initializes the storage system.
     */
    public abstract void init();

    /**
     * Saves the player data to the storage system.
     *
     * @param playerData The player data to save.
     */
    public abstract void save(PlayerData playerData);

    /**
     * Loads the player data from the storage system.
     *
     * @param uuid The UUID of the player to load.
     * @return The player data of the player.
     */
    public abstract PlayerData load(String uuid);

    /**
     * Loads the player data from the storage system.
     *
     * @param uuid The UUID of the player to load.
     * @return The player data of the player.
     */
    public abstract PlayerData load(UUID uuid);

    /**
     * Clear all player data from the storage system.
     */
    public abstract void clearDatabase();

    /**
     * Migrate the database to the latest version.
     */
    protected abstract void migrateDatabase();

    /**
     * Toggles the ignore status of a target player for a specific player.
     * @param playerUuid The UUID of the player who is toggling the ignore status.
     * @param targetUuid The UUID of the target player to be ignored or unignored.
     * @return true if the target player is now ignored, false if unignored.
     */
    public abstract boolean togglePlayerIgnore(UUID playerUuid, UUID targetUuid);

    /**
     * Checks if a player is ignoring a target player.
     * @param playerUuid The UUID of the player.
     * @param targetUuid The UUID of the target player.
     * @return true if the player is ignoring the target player, false otherwise.
     */
    public abstract boolean isPlayerIgnoring(UUID playerUuid, UUID targetUuid);
}