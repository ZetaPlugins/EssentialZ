package com.zetaplugins.essentialz.storage.repositories;

import com.zetaplugins.essentialz.EssentialZ;
import com.zetaplugins.essentialz.storage.connectionPool.ConnectionPool;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Abstract class for repositories handling database operations.
 */
public abstract class Repository {
    private final EssentialZ plugin;
    private final ConnectionPool connectionPool;

    /**
     * Constructor to initialize the Repository with the plugin instance and connection pool.
     * @param plugin The EssentialZ plugin instance.
     * @param connectionPool The connection pool for database connections.
     */
    public Repository(EssentialZ plugin, ConnectionPool connectionPool) {
        this.plugin = plugin;
        this.connectionPool = connectionPool;
    }

    /**
     * Gets a connection from the connection pool.
     * @return A database connection.
     * @throws SQLException If a database access error occurs.
     */
    protected Connection getConnection() throws SQLException {
        return connectionPool.getConnection();
    }

    protected EssentialZ getPlugin() {
        return plugin;
    }

    /**
     * Initializes the database table(s) for the repository.
     */
    public abstract void initializeTable();
}
