package com.zetaplugins.essentialz.storage.repositories.warps;

import com.zetaplugins.essentialz.storage.model.WarpData;

import java.util.List;

/**
 * Repository interface for managing warps in the storage system.
 */
public interface WarpsRepository {
    /**
     * Gets a warp by its name.
     * @param warpName The name of the warp.
     * @return The warp object, or null if not found.
     */
    WarpData load(String warpName);

    /**
     * Saves a warp to the storage system.
     * @param warpData The warp to save.
     */
    void save(WarpData warpData);

    /**
     * Deletes a warp from the storage system.
     * @param warpName The name of the warp to delete.
     */
    boolean delete(String warpName);

    /**
     * Gets a list of all warp names.
     * @return A list of all warp names.
     */
    List<String> getAllWarpNames();
}
