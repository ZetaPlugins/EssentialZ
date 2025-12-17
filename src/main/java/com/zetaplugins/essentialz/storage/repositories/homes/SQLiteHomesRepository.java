package com.zetaplugins.essentialz.storage.repositories.homes;

import com.zetaplugins.essentialz.EssentialZ;
import com.zetaplugins.essentialz.storage.connectionPool.ConnectionPool;

public class SQLiteHomesRepository extends SQLHomesRepository {

    public SQLiteHomesRepository(EssentialZ plugin, ConnectionPool connectionPool) {
        super(plugin, connectionPool);
    }

    @Override
    protected String getInserOrReplaceHomeStatement() {
        return "INSERT OR REPLACE INTO homes (owner, homeName, world, x, y, z, yaw, pitch) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    }
}
