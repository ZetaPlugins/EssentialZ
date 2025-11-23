package com.zetaplugins.essentialz.storage;

import com.zetaplugins.essentialz.EssentialZ;
import com.zetaplugins.essentialz.storage.connectionPool.ConnectionPool;
import com.zetaplugins.essentialz.storage.connectionPool.MySQLConnectionPool;
import com.zetaplugins.essentialz.util.ConfigManager;
import com.zetaplugins.zetacore.annotations.InjectManager;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * Storage class for MySQL database.
 */
public final class MySQLStorage extends MySQLSyntaxStorage {
    private final MySQLConnectionPool connectionPool;

    @InjectManager
    private ConfigManager configManager;

    public MySQLStorage(EssentialZ plugin) {
        super(plugin);

        FileConfiguration config = configManager.getStorageConfig();

        final String HOST = config.getString("host");
        final String PORT = config.getString("port");
        final String DATABASE = config.getString("database");
        final String USERNAME = config.getString("username");
        final String PASSWORD = config.getString("password");

        connectionPool = new MySQLConnectionPool(HOST, PORT, DATABASE, USERNAME, PASSWORD);
    }

    @Override
    public ConnectionPool getConnectionPool() {
        return connectionPool;
    }

    @Override
    protected void migrateDatabase() {
        // possible future migrations can be handled here
    }
}