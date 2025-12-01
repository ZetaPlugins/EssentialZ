package com.zetaplugins.essentialz.storage.repositories.ignores;

import com.zetaplugins.essentialz.EssentialZ;
import com.zetaplugins.essentialz.storage.connectionPool.ConnectionPool;
import com.zetaplugins.essentialz.storage.repositories.Repository;

import java.sql.*;
import java.util.UUID;
import java.util.logging.Level;

public class SQLIgnoresRepository extends Repository implements IgnoresRepository {

    public SQLIgnoresRepository(EssentialZ plugin, ConnectionPool connectionPool) {
        super(plugin, connectionPool);
    }

    @Override
    public void initializeTable() {
        try (Connection connection = getConnection()) {
            if (connection == null) return;
            try (Statement statement = connection.createStatement()) {
                StringBuilder ignoreTableSql = new StringBuilder();
                ignoreTableSql.append("CREATE TABLE IF NOT EXISTS ignores (")
                        .append("playerUuid CHAR(36), ")
                        .append("targetUuid CHAR(36), ")
                        .append("PRIMARY KEY (playerUuid, targetUuid), ")
                        .append("FOREIGN KEY (playerUuid) REFERENCES players(uuid) ON DELETE CASCADE, ")
                        .append("FOREIGN KEY (targetUuid) REFERENCES players(uuid) ON DELETE CASCADE")
                        .append(");");
                statement.executeUpdate(ignoreTableSql.toString());
            } catch (SQLException e) {
                getPlugin().getLogger().log(Level.SEVERE, "Failed to create ignores table:", e);
            }
        } catch (SQLException e) {
            getPlugin().getLogger().log(Level.SEVERE, "Failed to create ignores table:", e);
        }
    }

    @Override
    public boolean togglePlayerIgnore(UUID playerUuid, UUID targetUuid) {
        final String checkQuery = "SELECT 1 FROM ignores WHERE playerUuid = ? AND targetUuid = ?";
        final String insertQuery = "INSERT INTO ignores (playerUuid, targetUuid) VALUES (?, ?)";
        final String deleteQuery = "DELETE FROM ignores WHERE playerUuid = ? AND targetUuid = ?";

        try (Connection connection = getConnection()) {
            if (connection == null) return false;

            try (PreparedStatement checkStmt = connection.prepareStatement(checkQuery)) {
                checkStmt.setString(1, playerUuid.toString());
                checkStmt.setString(2, targetUuid.toString());

                try (ResultSet resultSet = checkStmt.executeQuery()) {
                    if (resultSet.next()) {
                        try (PreparedStatement deleteStmt = connection.prepareStatement(deleteQuery)) {
                            deleteStmt.setString(1, playerUuid.toString());
                            deleteStmt.setString(2, targetUuid.toString());
                            deleteStmt.executeUpdate();
                            return false;
                        }
                    } else {
                        try (PreparedStatement insertStmt = connection.prepareStatement(insertQuery)) {
                            insertStmt.setString(1, playerUuid.toString());
                            insertStmt.setString(2, targetUuid.toString());
                            insertStmt.executeUpdate();
                            return true;
                        }
                    }
                }
            }
        } catch (SQLException e) {
            getPlugin().getLogger().log(Level.SEVERE, "Failed to toggle player ignore status:", e);
            return false;
        }
    }

    @Override
    public boolean isPlayerIgnoring(UUID playerUuid, UUID targetUuid) {
        final String checkQuery = "SELECT 1 FROM ignores WHERE playerUuid = ? AND targetUuid = ?";

        try (Connection connection = getConnection()) {
            if (connection == null) return false;

            try (PreparedStatement checkStmt = connection.prepareStatement(checkQuery)) {
                checkStmt.setString(1, playerUuid.toString());
                checkStmt.setString(2, targetUuid.toString());

                try (ResultSet resultSet = checkStmt.executeQuery()) {
                    return resultSet.next();
                }
            }
        } catch (SQLException e) {
            getPlugin().getLogger().log(Level.SEVERE, "Failed to check if player is ignoring target:", e);
            return false;
        }
    }
}
