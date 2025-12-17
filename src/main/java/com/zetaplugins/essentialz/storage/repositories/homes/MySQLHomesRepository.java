package com.zetaplugins.essentialz.storage.repositories.homes;

import com.zetaplugins.essentialz.EssentialZ;
import com.zetaplugins.essentialz.storage.connectionPool.ConnectionPool;

public class MySQLHomesRepository extends SQLHomesRepository {
    public MySQLHomesRepository(EssentialZ plugin, ConnectionPool connectionPool) {
        super(plugin, connectionPool);
    }

    @Override
    protected String getInserOrReplaceHomeStatement() {
        return "INSERT INTO homes (owner, homeName, world, x, y, z, yaw, pitch) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE " +
                "world = VALUES(world), " +
                "x = VALUES(x), " +
                "y = VALUES(y), " +
                "z = VALUES(z), " +
                "yaw = VALUES(yaw), " +
                "pitch = VALUES(pitch)";
    }
}
