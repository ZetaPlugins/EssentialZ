package com.zetaplugins.essentialz.storage.repositories.ignores;

import java.util.UUID;

/**
 * Repository interface for managing player ignore relationships.
 */
public interface IgnoresRepository {
    /**
     * Toggles the ignore status of a target player for a specific player.
     * @param playerUuid The UUID of the player who is toggling the ignore status.
     * @param targetUuid The UUID of the target player to be ignored or unignored.
     * @return true if the target player is now ignored, false if unignored.
     */
    boolean togglePlayerIgnore(UUID playerUuid, UUID targetUuid);

    /**
     * Checks if a player is ignoring a target player.
     * @param playerUuid The UUID of the player.
     * @param targetUuid The UUID of the target player.
     * @return true if the player is ignoring the target player, false otherwise.
     */
    boolean isPlayerIgnoring(UUID playerUuid, UUID targetUuid);
}
