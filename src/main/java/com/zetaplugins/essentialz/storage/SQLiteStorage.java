package com.zetaplugins.essentialz.storage;

import com.zetaplugins.essentialz.EssentialZ;
import com.zetaplugins.essentialz.storage.connectionPool.SQLiteConnectionPool;
import com.zetaplugins.essentialz.storage.repositories.ignores.IgnoresRepository;
import com.zetaplugins.essentialz.storage.repositories.ignores.SQLIgnoresRepository;
import com.zetaplugins.essentialz.storage.repositories.player.PlayerRepository;
import com.zetaplugins.essentialz.storage.repositories.player.SQLPlayerRepository;
import com.zetaplugins.essentialz.storage.repositories.warps.SQLiteWarpsRepository;
import com.zetaplugins.essentialz.storage.repositories.warps.WarpsRepository;

/**
 * SQLiteStorage is a concrete implementation of the Storage class that uses SQLite as the database backend.
 */
public final class SQLiteStorage extends Storage {
    private final SQLPlayerRepository playerRepository;
    private final SQLIgnoresRepository ignoresRepository;
    private final SQLiteWarpsRepository warpsRepository;

    public SQLiteStorage(EssentialZ plugin) {
        super(plugin);
        var connectionPool = new SQLiteConnectionPool(plugin.getDataFolder().getPath() + "/userData.db");
        playerRepository = new SQLPlayerRepository(plugin, connectionPool);
        ignoresRepository = new SQLIgnoresRepository(plugin, connectionPool);
        warpsRepository = new SQLiteWarpsRepository(plugin, connectionPool);
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
