package com.zetaplugins.essentialz.storage.repositories.homes;

import com.zetaplugins.essentialz.storage.model.HomeData;

import java.util.List;
import java.util.UUID;

/**
 * Repository interface for managing homes in the storage system.
 */
public interface HomesRepository {
    /**
     * Gets a hme by its owner and name.
     * @param owner The UUID of the owner.
     * @param homeName The name of the warp.
     * @return The home object, or null if not found.
     */
    HomeData load(UUID owner, String homeName);

    /**
     * Saves a warp to the storage system.
     * @param homeData The warp to save.
     */
    void save(HomeData homeData);

    /**
     * Deletes a home from the storage system.
     * @param owner The UUID of the owner.
     * @param homeName The name of the home to delete.
     */
    boolean delete(UUID owner, String homeName);

    /**
     * Gets a list of all homes for a specific owner.
     * @param owner The UUID of the owner.
     * @return A list of all homes
     */
    List<HomeData> getAllHomes(UUID owner);

    /**
     * Gets the count of homes for a specific owner.
     * @param owner The UUID of the owner.
     * @return The count of homes
     */
    int getHomeCount(UUID owner);
}
