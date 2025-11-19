package com.zetaplugins.essentialz.util.permissions;

import org.bukkit.Bukkit;
import org.bukkit.permissions.Permissible;
import org.bukkit.permissions.PermissionDefault;

/**
 * Interface representing a permission node.
 */
public interface PermissionNode {

    /**
     * Gets the permission node string.
     * @return The permission node.
     */
    String getNode();

    /**
     * Gets the description of the permission node.
     * @return The description.
     */
    String getDescription();

    /**
     * Gets the default permission setting.
     * @return The default permission.
     */
    PermissionDefault getPermissionDefault();

    /**
     * Checks if a player has this permission.
     *
     * @param player The player to check.
     * @return true if the player has this permission, false otherwise.
     */
    default boolean has(Permissible player) {
        return player.hasPermission(getNode());
    }

    /**
     * Registers the permission node with the Bukkit permission manager.
     */
    default void register() {
        Bukkit.getPluginManager().addPermission(
                new org.bukkit.permissions.Permission(getNode(), getDescription(), getPermissionDefault())
        );
    }
}
