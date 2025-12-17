package com.zetaplugins.essentialz.storage.repositories.homes;

import com.zetaplugins.essentialz.EssentialZ;
import com.zetaplugins.essentialz.storage.connectionPool.ConnectionPool;
import com.zetaplugins.essentialz.storage.model.HomeData;
import com.zetaplugins.essentialz.storage.repositories.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

public abstract class SQLHomesRepository extends Repository implements HomesRepository {

    public SQLHomesRepository(EssentialZ plugin, ConnectionPool connectionPool) {
        super(plugin, connectionPool);
    }

    @Override
    public void initializeTable() {
        try (Connection connection = getConnection()) {
            if (connection == null) return;
            try (Statement statement = connection.createStatement()) {
                String sql =
                        "CREATE TABLE IF NOT EXISTS homes (" +
                                "owner CHAR(36) NOT NULL, " +
                                "homeName TEXT NOT NULL, " +
                                "world TEXT NOT NULL, " +
                                "x DOUBLE, " +
                                "y DOUBLE, " +
                                "z DOUBLE, " +
                                "yaw FLOAT, " +
                                "pitch FLOAT, " +
                                "PRIMARY KEY (owner, homeName), " +
                                "FOREIGN KEY (owner) REFERENCES players(uuid) ON DELETE CASCADE" +
                                ");";

                statement.executeUpdate(sql);
            } catch (SQLException e) {
                getPlugin().getLogger().log(Level.SEVERE, "Failed to create homes SQL table:", e);
            }
        } catch (SQLException e) {
            getPlugin().getLogger().log(Level.SEVERE, "Failed to create homes SQL table:", e);
        }
    }

    @Override
    public HomeData load(UUID owner, String homeName) {
        final String sql = "SELECT * FROM homes WHERE owner = ? AND homeName = ?";

        try (Connection connection = getConnection()) {
            if (connection == null) return null;

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, owner.toString());
                statement.setString(2, homeName);
                statement.setQueryTimeout(30);

                try (ResultSet resultSet = statement.executeQuery()) {

                    if (!resultSet.next()) {
                        return null;
                    }

                    String world = resultSet.getString("world");
                    double x = resultSet.getDouble("x");
                    double y = resultSet.getDouble("y");
                    double z = resultSet.getDouble("z");
                    float yaw = resultSet.getFloat("yaw");
                    float pitch = resultSet.getFloat("pitch");

                    return new HomeData(owner, homeName, world, x, y, z, yaw, pitch);
                } catch (SQLException e) {
                    getPlugin().getLogger().log(Level.SEVERE, "Failed to load home from SQL database:", e);
                    return null;
                }
            } catch (SQLException e) {
                getPlugin().getLogger().log(Level.SEVERE, "Failed to load home from SQL database:", e);
                return null;
            }
        } catch (SQLException e) {
            getPlugin().getLogger().log(Level.SEVERE, "Failed to load home from SQL database:", e);
            return null;
        }
    }

    @Override
    public void save(HomeData homeData) {
        final String sql = getInserOrReplaceHomeStatement();

        try (Connection connection = getConnection()) {
            if (connection == null) return;

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, homeData.getOwner().toString());
                statement.setString(2, homeData.getName());
                statement.setString(3, homeData.getWorld());
                statement.setDouble(4, homeData.getX());
                statement.setDouble(5, homeData.getY());
                statement.setDouble(6, homeData.getZ());
                statement.setFloat(7, homeData.getYaw());
                statement.setFloat(8, homeData.getPitch());
                statement.executeUpdate();
            } catch (SQLException e) {
                getPlugin().getLogger().log(Level.SEVERE, "Failed to save home to SQL database:", e);
            }
        } catch (SQLException e) {
            getPlugin().getLogger().log(Level.SEVERE, "Failed to save home to SQL database:", e);
        }
    }

    protected abstract String getInserOrReplaceHomeStatement();

    @Override
    public boolean delete(UUID owner, String homeName) {
        final String sql = "DELETE FROM homes WHERE owner = ? AND homeName = ?";

        try (Connection connection = getConnection()) {
            if (connection == null) return false;

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, owner.toString());
                statement.setString(2, homeName);
                int rowsAffected = statement.executeUpdate();
                return rowsAffected > 0;
            } catch (SQLException e) {
                getPlugin().getLogger().log(Level.SEVERE, "Failed to delete home from SQL database:", e);
                return false;
            }
        } catch (SQLException e) {
            getPlugin().getLogger().log(Level.SEVERE, "Failed to delete home from SQL database:", e);
            return false;
        }
    }

    @Override
    public List<HomeData> getAllHomes(UUID owner) {
        List<HomeData> homes = new ArrayList<>();
        final String sql = "SELECT * FROM homes WHERE owner = ?";

        try (Connection connection = getConnection()) {
            if (connection == null) return homes;

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, owner.toString());
                statement.setQueryTimeout(30);

                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        String homeName = resultSet.getString("homeName");
                        String world = resultSet.getString("world");
                        double x = resultSet.getDouble("x");
                        double y = resultSet.getDouble("y");
                        double z = resultSet.getDouble("z");
                        float yaw = resultSet.getFloat("yaw");
                        float pitch = resultSet.getFloat("pitch");

                        HomeData homeData = new HomeData(owner, homeName, world, x, y, z, yaw, pitch);
                        homes.add(homeData);
                    }
                } catch (SQLException e) {
                    getPlugin().getLogger().log(Level.SEVERE, "Failed to retrieve homes for owner \"" + owner.toString() + "\"  from SQL database:", e);
                }
            } catch (SQLException e) {
                getPlugin().getLogger().log(Level.SEVERE, "Failed to retrieve homes for owner \"" + owner.toString() + "\"  from SQL database:", e);
            }
        } catch (SQLException e) {
            getPlugin().getLogger().log(Level.SEVERE, "Failed to retrieve homes for owner \"" + owner.toString() + "\"  from SQL database:", e);
        }

        return homes;
    }

}
