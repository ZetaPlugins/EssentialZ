package com.zetaplugins.essentialz.storage;

import com.zetaplugins.essentialz.EssentialZ;
import com.zetaplugins.essentialz.storage.connectionPool.ConnectionPool;
import com.zetaplugins.essentialz.storage.connectionPool.SQLiteConnectionPool;

public final class SQLiteStorage extends SQLStorage {
    private final SQLiteConnectionPool connectionPool;

    public SQLiteStorage(EssentialZ plugin) {
        super(plugin);
        connectionPool = new SQLiteConnectionPool(getPlugin().getDataFolder().getPath() + "/userData.db");
    }

    @Override
    protected void migrateDatabase() {
        // possible future migrations can be handled here
    }

    @Override
    public ConnectionPool getConnectionPool() {
        return connectionPool;
    }

    @Override
    protected String getInserOrReplacePlayerStatement() {
        return "INSERT OR REPLACE INTO players (uuid, enableTeamchat, enableDms, balance) VALUES (?, ?, ?, ?)";
    }

    @Override
    protected String getInserOrReplaceWarpStatement() {
        return "INSERT OR REPLACE INTO warps (name, world, x, y, z, yaw, pitch) VALUES (?, ?, ?, ?, ?, ?, ?)";
    }
}
