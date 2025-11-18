package com.zetaplugins.essentialz.storage;

import com.zetaplugins.essentialz.EssentialZ;
import com.zetaplugins.essentialz.storage.connectionPool.ConnectionPool;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

public abstract class SQLStorage extends Storage {
    private static final String CSV_SEPARATOR = ",";

    public SQLStorage(EssentialZ plugin) {
        super(plugin);
    }

    @Override
    public void init() {
        try (Connection connection = getConnection()) {
            if (connection == null) return;
            try (Statement statement = connection.createStatement()) {
                StringBuilder sql = new StringBuilder();
                sql.append("CREATE TABLE IF NOT EXISTS players (")
                        .append("uuid CHAR(36) PRIMARY KEY, ")
                        .append("enableTeamchat BOOLEAN DEFAULT TRUE, ")
                        .append("enableDms BOOLEAN DEFAULT TRUE")
                        .append(");");
                statement.executeUpdate(sql.toString());

                migrateDatabase();
            } catch (SQLException e) {
                getPlugin().getLogger().log(Level.SEVERE, "Failed to initialize SQL database:", e);
            }
        } catch (SQLException e) {
            getPlugin().getLogger().log(Level.SEVERE, "Failed to initialize SQL database:", e);
        }
    }

    public abstract ConnectionPool getConnectionPool();

    public Connection getConnection() throws SQLException {
        return getConnectionPool().getConnection();
    }

    @Override
    public PlayerData load(UUID uuid) {
        final String sql = "SELECT * FROM players WHERE uuid = ?";

        try (Connection connection = getConnection()) {
            if (connection == null) return null;

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, uuid.toString());
                statement.setQueryTimeout(30);

                try (ResultSet resultSet = statement.executeQuery()) {

                    if (!resultSet.next()) {
                        Player player = Bukkit.getPlayer(uuid);
                        if (player == null) return null;
                        PlayerData newPlayerData = new PlayerData(uuid);
                        save(newPlayerData);
                        return newPlayerData;
                    }

                    return mapResultSetToPlayerData(resultSet, uuid);
                } catch (SQLException e) {
                    getPlugin().getLogger().log(Level.SEVERE, "Failed to load player data from SQL database:", e);
                    return null;
                }
            } catch (SQLException e) {
                getPlugin().getLogger().log(Level.SEVERE, "Failed to load player data from SQL database:", e);
                return null;
            }
        } catch (SQLException e) {
            getPlugin().getLogger().log(Level.SEVERE, "Failed to load player data from SQL database:", e);
            return null;
        }
    }

    private PlayerData mapResultSetToPlayerData(ResultSet resultSet, UUID uuid) throws SQLException {
        PlayerData playerData = new PlayerData(uuid);
        playerData.setEnableTeamchat(resultSet.getBoolean("enableTeamchat"));
        playerData.setEnableDms(resultSet.getBoolean("enableDms"));
        playerData.clearModifiedFields();
        return playerData;
    }

    @Override
    public void save(PlayerData playerData) {
        if (!playerData.hasChanges()) return;

        try (Connection connection = getConnection()) {
            if (connection == null) return;

            boolean exists = checkIfEntryExists(connection, playerData.getUuid());

            if (exists) {
                updatePlayerData(connection, playerData);
            } else {
                insertPlayerData(connection, playerData);
            }
        } catch (SQLException e) {
            getPlugin().getLogger().log(Level.SEVERE, "Failed to save player data:", e);
        }
    }

    /**
     * Check if a player entry exists in the database
     * @param connection Connection to the database
     * @param uuid UUID of the player to check
     * @return True if the player entry exists, false otherwise
     */
    private boolean checkIfEntryExists(Connection connection, String uuid) {
        final String selectQuery = "SELECT 1 FROM players WHERE uuid = ?";

        try (PreparedStatement selectStmt = connection.prepareStatement(selectQuery);) {
            selectStmt.setString(1, uuid);
            try (ResultSet resultSet = selectStmt.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            getPlugin().getLogger().log(Level.SEVERE, "Failed to check if player entry exists:", e);
            return false;
        }
    }

    /**
     * Insert player data into the database
     * @param connection Connection to the database
     * @param playerData Player data to insert
     * @return True if the insert was successful, false otherwise
     */
    private boolean insertPlayerData(Connection connection, PlayerData playerData) {
        final String insertQuery = "INSERT INTO players (uuid, enableTeamchat, enableDms) " +
                "VALUES (?, ?, ?)";

        try (PreparedStatement insertStmt = connection.prepareStatement(insertQuery)) {
            insertStmt.setString(1, playerData.getUuid());
            insertStmt.setBoolean(2, playerData.isEnableTeamchat());
            insertStmt.setBoolean(3, playerData.isEnableDms());
            insertStmt.executeUpdate();

            playerData.clearModifiedFields();

            return true;
        } catch (SQLException e) {
            getPlugin().getLogger().log(Level.SEVERE, "Failed to insert player data:", e);
            return false;
        }
    }

    /**
     * Update player data in the database
     * @param connection Connection to the database
     * @param playerData Player data to update
     * @return True if the update was successful, false otherwise
     */
    private boolean updatePlayerData(Connection connection, PlayerData playerData) {
        StringBuilder updateQuery = new StringBuilder("UPDATE players SET ");
        List<Object> params = new ArrayList<>();

        for (String field : playerData.getModifiedFields()) {
            updateQuery.append(field).append(" = ?, ");
            switch (field) {
                case "enableTeamchat" -> params.add(playerData.isEnableTeamchat());
                case "enableDms" -> params.add(playerData.isEnableDms());
            }
        }

        updateQuery.setLength(updateQuery.length() - 2); // Remove the last comma and space
        updateQuery.append(" WHERE uuid = ?");
        params.add(playerData.getUuid());

        try (PreparedStatement updateStmt = connection.prepareStatement(updateQuery.toString())) {
            for (int i = 0; i < params.size(); i++) {
                updateStmt.setObject(i + 1, params.get(i));
            }
            updateStmt.executeUpdate();

            playerData.clearModifiedFields();
            return true;
        } catch (SQLException e) {
            getPlugin().getLogger().log(Level.SEVERE, "Failed to update player data:", e);
            return false;
        }
    }

    @Override
    public PlayerData load(String uuid) {
        return load(UUID.fromString(uuid));
    }

    protected abstract String getInserOrReplaceStatement();

    @Override
    public void clearDatabase() {
        try (Connection connection = getConnection()) {
            if (connection == null) return;

            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate("DELETE FROM players;");
            } catch (SQLException e) {
                getPlugin().getLogger().log(Level.SEVERE, "Failed to clear SQL database:", e);
            }
        } catch (SQLException e) {
            getPlugin().getLogger().log(Level.SEVERE, "Failed to clear SQL database:", e);
        }
    }
}
