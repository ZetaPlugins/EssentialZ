package com.zetaplugins.essentialz.storage;

import com.zetaplugins.essentialz.EssentialZ;
import com.zetaplugins.essentialz.storage.model.PlayerData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Abstract class for Storage classes that share the same MySQL syntax.
 */
public abstract class MySQLSyntaxStorage extends SQLStorage {
    public MySQLSyntaxStorage(EssentialZ plugin) {
        super(plugin);
    }

    @Override
    public void save(PlayerData playerData) {
        String insertOrUpdateQuery = "INSERT INTO players (uuid, enableTeamchat, enableDms, ignoredPlayers) " +
                "VALUES (?, ?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE " +
                "enableTeamchat = VALUES(enableTeamchat), " +
                "enableDms = VALUES(enableDms)";

        try (Connection connection = getConnection()) {
            if (connection == null) return;

            try (PreparedStatement stmt = connection.prepareStatement(insertOrUpdateQuery)) {

                stmt.setString(1, playerData.getUuid());
                stmt.setBoolean(2, playerData.isEnableTeamchat());
                stmt.setBoolean(3, playerData.isEnableDms());

                stmt.executeUpdate();

                playerData.clearModifiedFields();
            } catch (SQLException e) {
                getPlugin().getLogger().severe("Failed to save player data to database: " + e.getMessage());
            }
        } catch (SQLException e) {
            getPlugin().getLogger().severe("Failed to save player data to database: " + e.getMessage());
        }
    }

    @Override
    protected String getInserOrReplacePlayerStatement() {
        return "INSERT INTO players (uuid, enableTeamchat, enableDms, balance) " +
                "VALUES (?, ?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE " +
                "enableTeamchat = VALUES(enableTeamchat), " +
                "enableDms = VALUES(enableDms)";
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
