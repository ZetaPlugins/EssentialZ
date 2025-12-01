package com.zetaplugins.essentialz.storage.repositories.warps;

import com.zetaplugins.essentialz.EssentialZ;
import com.zetaplugins.essentialz.storage.connectionPool.ConnectionPool;

public class MySQLWarpsRepository extends SQLWarpsRepository {

    public MySQLWarpsRepository(EssentialZ plugin, ConnectionPool connectionPool) {
        super(plugin, connectionPool);
    }

    @Override
    protected String getInserOrReplaceWarpStatement() {
        return "INSERT INTO warps (name, world, x, y, z, yaw, pitch) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE " +
                "world = VALUES(world), " +
                "x = VALUES(x), " +
                "y = VALUES(y), " +
                "z = VALUES(z), " +
                "yaw = VALUES(yaw), " +
                "pitch = VALUES(pitch)";
    }
}
