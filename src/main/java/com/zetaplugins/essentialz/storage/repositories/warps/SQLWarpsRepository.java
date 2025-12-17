package com.zetaplugins.essentialz.storage.repositories.warps;

import com.zetaplugins.essentialz.EssentialZ;
import com.zetaplugins.essentialz.storage.connectionPool.ConnectionPool;
import com.zetaplugins.essentialz.storage.model.WarpData;
import com.zetaplugins.essentialz.storage.repositories.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public abstract class SQLWarpsRepository extends Repository implements WarpsRepository {

    public SQLWarpsRepository(EssentialZ plugin, ConnectionPool connectionPool) {
        super(plugin, connectionPool);
    }

    @Override
    public void initializeTable() {
        try (Connection connection = getConnection()) {
            if (connection == null) return;
            try (Statement statement = connection.createStatement()) {
                StringBuilder warpsTableSql = new StringBuilder();
                warpsTableSql.append("CREATE TABLE IF NOT EXISTS warps (")
                        .append("name TEXT PRIMARY KEY, ")
                        .append("world TEXT, ")
                        .append("x DOUBLE, ")
                        .append("y DOUBLE, ")
                        .append("z DOUBLE, ")
                        .append("yaw FLOAT, ")
                        .append("pitch FLOAT")
                        .append(");");
                statement.executeUpdate(warpsTableSql.toString());
            } catch (SQLException e) {
                getPlugin().getLogger().log(Level.SEVERE, "Failed to create warps SQL table:", e);
            }
        } catch (SQLException e) {
            getPlugin().getLogger().log(Level.SEVERE, "Failed to create warps SQL table:", e);
        }
    }

    @Override
    public WarpData load(String warpName) {
        final String sql = "SELECT * FROM warps WHERE name = ?";

        try (Connection connection = getConnection()) {
            if (connection == null) return null;

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, warpName);
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

                    return new WarpData(warpName, world, x, y, z, yaw, pitch);
                } catch (SQLException e) {
                    getPlugin().getLogger().log(Level.SEVERE, "Failed to load warp from SQL database:", e);
                    return null;
                }
            } catch (SQLException e) {
                getPlugin().getLogger().log(Level.SEVERE, "Failed to load warp from SQL database:", e);
                return null;
            }
        } catch (SQLException e) {
            getPlugin().getLogger().log(Level.SEVERE, "Failed to load warp from SQL database:", e);
            return null;
        }
    }

    @Override
    public void save(WarpData warpData) {
        final String sql = getInserOrReplaceWarpStatement();

        try (Connection connection = getConnection()) {
            if (connection == null) return;

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, warpData.getName());
                statement.setString(2, warpData.getWorld());
                statement.setDouble(3, warpData.getX());
                statement.setDouble(4, warpData.getY());
                statement.setDouble(5, warpData.getZ());
                statement.setFloat(6, warpData.getYaw());
                statement.setFloat(7, warpData.getPitch());
                statement.executeUpdate();
            } catch (SQLException e) {
                getPlugin().getLogger().log(Level.SEVERE, "Failed to save warp to SQL database:", e);
            }
        } catch (SQLException e) {
            getPlugin().getLogger().log(Level.SEVERE, "Failed to save warp to SQL database:", e);
        }
    }

    protected abstract String getInserOrReplaceWarpStatement();

    @Override
    public boolean delete(String warpName) {
        final String sql = "DELETE FROM warps WHERE name = ?";

        try (Connection connection = getConnection()) {
            if (connection == null) return false;

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, warpName);
                int rowsAffected = statement.executeUpdate();
                return rowsAffected > 0;
            } catch (SQLException e) {
                getPlugin().getLogger().log(Level.SEVERE, "Failed to delete warp from SQL database:", e);
                return false;
            }
        } catch (SQLException e) {
            getPlugin().getLogger().log(Level.SEVERE, "Failed to delete warp from SQL database:", e);
            return false;
        }
    }

    @Override
    public List<String> getAllWarpNames() {
        List<String> warpNames = new ArrayList<>();
        final String sql = "SELECT name FROM warps";

        try (Connection connection = getConnection()) {
            if (connection == null) return warpNames;

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        warpNames.add(resultSet.getString("name"));
                    }
                } catch (SQLException e) {
                    getPlugin().getLogger().log(Level.SEVERE, "Failed to retrieve warp names from SQL database:", e);
                }
            } catch (SQLException e) {
                getPlugin().getLogger().log(Level.SEVERE, "Failed to retrieve warp names from SQL database:", e);
            }
        } catch (SQLException e) {
            getPlugin().getLogger().log(Level.SEVERE, "Failed to retrieve warp names from SQL database:", e);
        }

        return warpNames;
    }
}
