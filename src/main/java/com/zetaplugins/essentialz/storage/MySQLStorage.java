package com.zetaplugins.essentialz.storage;

import com.zetaplugins.essentialz.EssentialZ;
import com.zetaplugins.essentialz.config.storage.StorageConfig;
import com.zetaplugins.essentialz.storage.connectionPool.MySQLConnectionPool;
import com.zetaplugins.essentialz.storage.repositories.ignores.IgnoresRepository;
import com.zetaplugins.essentialz.storage.repositories.ignores.SQLIgnoresRepository;
import com.zetaplugins.essentialz.storage.repositories.player.PlayerRepository;
import com.zetaplugins.essentialz.storage.repositories.player.SQLPlayerRepository;
import com.zetaplugins.essentialz.storage.repositories.warps.MySQLWarpsRepository;
import com.zetaplugins.essentialz.storage.repositories.warps.WarpsRepository;
import com.zetaplugins.zetacore.annotations.InjectManager;
import com.zetaplugins.zetacore.services.config.ConfigService;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * MySQLStorage is a concrete implementation of the Storage class that uses MySQL as the database backend.
 */
public final class MySQLStorage extends Storage {
    private final SQLPlayerRepository playerRepository;
    private final SQLIgnoresRepository ignoresRepository;
    private final MySQLWarpsRepository warpsRepository;

    @InjectManager
    private ConfigService configManager;

    /**
     * Constructs a MySQLStorage instance using the provided plugin and storage configuration.
     * @param plugin The EssentialZ plugin instance.
     * @param storageConfig The configuration file containing MySQL connection details.
     */
    public MySQLStorage(EssentialZ plugin, StorageConfig storageConfig) {
        super(plugin);

        var connectionPool = new MySQLConnectionPool(
                storageConfig.getHost(),
                String.valueOf(storageConfig.getPort()),
                storageConfig.getDatabase(),
                storageConfig.getUsername(),
                storageConfig.getPassword()
        );
        playerRepository = new SQLPlayerRepository(plugin, connectionPool);
        ignoresRepository = new SQLIgnoresRepository(plugin, connectionPool);
        warpsRepository = new MySQLWarpsRepository(plugin, connectionPool);
    }

    @Override
    public void initializeDatabase() {
        playerRepository.initializeTable();
        ignoresRepository.initializeTable();
        warpsRepository.initializeTable();
    }

    @Override
    public PlayerRepository getPlayerRepository() {
        return playerRepository;
    }

    @Override
    public IgnoresRepository getIgnoresRepository() {
        return ignoresRepository;
    }

    @Override
    public WarpsRepository getWarpsRepository() {
        return warpsRepository;
    }
}
