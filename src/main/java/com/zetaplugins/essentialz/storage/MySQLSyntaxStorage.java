package com.zetaplugins.essentialz.storage;

import com.zetaplugins.essentialz.EssentialZ;

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
        String insertOrUpdateQuery = "INSERT INTO players (uuid, enableTeamchat, enableDms) " +
                "VALUES (?, ?, ?) " +
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
    protected String getInserOrReplaceStatement() {
        return "INSERT INTO players (uuid, enableTeamchat, enableDms) " +
                "VALUES (?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE " +
                "enableTeamchat = VALUES(enableTeamchat), " +
                "enableDms = VALUES(enableDms)";
    }
}
