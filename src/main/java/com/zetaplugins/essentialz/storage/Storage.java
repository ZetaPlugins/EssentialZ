package com.zetaplugins.essentialz.storage;

import com.zetaplugins.essentialz.EssentialZ;

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
}