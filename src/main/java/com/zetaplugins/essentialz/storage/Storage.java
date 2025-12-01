package com.zetaplugins.essentialz.storage;

import com.zetaplugins.essentialz.EssentialZ;
import com.zetaplugins.essentialz.storage.repositories.ignores.IgnoresRepository;
import com.zetaplugins.essentialz.storage.repositories.player.PlayerRepository;
import com.zetaplugins.essentialz.storage.repositories.warps.WarpsRepository;
import com.zetaplugins.zetacore.annotations.PostManagerConstruct;

/**
 * Abstract class representing the storage system for EssentialZ plugin.
 * It provides methods to initialize the database and access various repositories.
 */
public abstract class Storage {
    private final EssentialZ plugin;

    /**
     * Constructs a Storage instance with the provided EssentialZ plugin.
     * @param plugin The EssentialZ plugin instance.
     */
    public Storage(EssentialZ plugin) {
        this.plugin = plugin;
    }

    protected EssentialZ getPlugin() {
        return plugin;
    }

    /**
     * Initializes the database by setting up necessary tables and structures.
     */
    @PostManagerConstruct
    public abstract void initializeDatabase();

    public abstract PlayerRepository getPlayerRepository();

    public abstract IgnoresRepository getIgnoresRepository();

    public abstract WarpsRepository getWarpsRepository();
}
