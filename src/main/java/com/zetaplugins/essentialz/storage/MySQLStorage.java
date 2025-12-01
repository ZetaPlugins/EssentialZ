package com.zetaplugins.essentialz.storage;

import com.zetaplugins.essentialz.EssentialZ;
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
    public MySQLStorage(EssentialZ plugin, FileConfiguration storageConfig) {
        super(plugin);

        final String HOST = storageConfig.getString("host");
        final String PORT = storageConfig.getString("port");
        final String DATABASE = storageConfig.getString("database");
        final String USERNAME = storageConfig.getString("username");
        final String PASSWORD = storageConfig.getString("password");

        var connectionPool = new MySQLConnectionPool(HOST, PORT, DATABASE, USERNAME, PASSWORD);
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
