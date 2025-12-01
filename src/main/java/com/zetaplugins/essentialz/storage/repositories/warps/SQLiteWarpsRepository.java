package com.zetaplugins.essentialz.storage.repositories.warps;

import com.zetaplugins.essentialz.EssentialZ;
import com.zetaplugins.essentialz.storage.connectionPool.ConnectionPool;

public class SQLiteWarpsRepository extends SQLWarpsRepository {

    public SQLiteWarpsRepository(EssentialZ plugin, ConnectionPool connectionPool) {
        super(plugin, connectionPool);
    }

    @Override
    protected String getInserOrReplaceWarpStatement() {
        return "INSERT OR REPLACE INTO warps (name, world, x, y, z, yaw, pitch) VALUES (?, ?, ?, ?, ?, ?, ?)";
    }
}
