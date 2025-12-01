package com.zetaplugins.essentialz.storage.repositories.player;

import com.zetaplugins.essentialz.EssentialZ;
import com.zetaplugins.essentialz.storage.connectionPool.ConnectionPool;
import com.zetaplugins.essentialz.storage.model.PlayerData;
import com.zetaplugins.essentialz.storage.repositories.Repository;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

public class SQLPlayerRepository extends Repository implements PlayerRepository {

    public SQLPlayerRepository(EssentialZ plugin, ConnectionPool connectionPool) {
        super(plugin, connectionPool);
    }

    @Override
    public void initializeTable() {
        try (Connection connection = getConnection()) {
            if (connection == null) return;
            try (Statement statement = connection.createStatement()) {
                StringBuilder playerTableSql = new StringBuilder();
                playerTableSql.append("CREATE TABLE IF NOT EXISTS players (")
                        .append("uuid CHAR(36) PRIMARY KEY, ")
                        .append("enableTeamchat BOOLEAN DEFAULT TRUE, ")
                        .append("enableDms BOOLEAN DEFAULT TRUE, ")
                        .append("balance DOUBLE DEFAULT 0.0")
                        .append(");");
                statement.executeUpdate(playerTableSql.toString());
            } catch (SQLException e) {
                getPlugin().getLogger().log(Level.SEVERE, "Failed to create players SQLite table:", e);
            }
        } catch (SQLException e) {
            getPlugin().getLogger().log(Level.SEVERE, "Failed to create players SQLite table:", e);
        }
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
     * Insert player data into the database
     * @param connection Connection to the database
     * @param playerData Player data to insert
     * @return True if the insert was successful, false otherwise
     */
    private boolean insertPlayerData(Connection connection, PlayerData playerData) {
        final String insertQuery = "INSERT INTO players (uuid, enableTeamchat, enableDms, balance) " +
                "VALUES (?, ?, ?, ?)";

        try (PreparedStatement insertStmt = connection.prepareStatement(insertQuery)) {
            insertStmt.setString(1, playerData.getUuid());
            insertStmt.setBoolean(2, playerData.isEnableTeamchat());
            insertStmt.setBoolean(3, playerData.isEnableDms());
            insertStmt.executeUpdate();

            playerData.clearModifiedFields();

            return true;
        } catch (SQLException e) {
            getPlugin().getLogger().log(Level.SEVERE, "Failed to insert player data into SQLite:", e);
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
                case "balance" -> params.add(playerData.getBalance());
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

    @Override @Nullable
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
                    getPlugin().getLogger().log(Level.SEVERE, "Failed to load player data from SQLite database:", e);
                    return null;
                }
            } catch (SQLException e) {
                getPlugin().getLogger().log(Level.SEVERE, "Failed to load player data from SQLite database:", e);
                return null;
            }
        } catch (SQLException e) {
            getPlugin().getLogger().log(Level.SEVERE, "Failed to load player data from SQLite database:", e);
            return null;
        }
    }

    private PlayerData mapResultSetToPlayerData(ResultSet resultSet, UUID uuid) throws SQLException {
        PlayerData playerData = new PlayerData(uuid);
        playerData.setEnableTeamchat(resultSet.getBoolean("enableTeamchat"));
        playerData.setEnableDms(resultSet.getBoolean("enableDms"));
        playerData.setBalance(resultSet.getDouble("balance"));
        playerData.clearModifiedFields();
        return playerData;
    }

    @Override @Nullable
    public PlayerData load(String uuid) {
        return load(UUID.fromString(uuid));
    }

    @Override
    public Map<UUID, Double> getTopBalances(int topN) {
        final String sql = "SELECT uuid, balance FROM players ORDER BY balance DESC LIMIT ?";
        Map<UUID, Double> topBalances = new java.util.LinkedHashMap<>();

        try (Connection connection = getConnection()) {
            if (connection == null) return topBalances;

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, topN);

                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        UUID uuid = UUID.fromString(resultSet.getString("uuid"));
                        double balance = resultSet.getDouble("balance");
                        topBalances.put(uuid, balance);
                    }
                }
            }
        } catch (SQLException e) {
            getPlugin().getLogger().log(Level.SEVERE, "Failed to retrieve top balances from SQLite database:", e);
        }

        return topBalances;
    }
}
