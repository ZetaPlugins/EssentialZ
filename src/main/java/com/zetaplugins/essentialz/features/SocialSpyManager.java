package com.zetaplugins.essentialz.features;

import com.zetaplugins.zetacore.annotations.Manager;

import java.util.*;

@Manager
public class SocialSpyManager {
    // Map of viewer UUIDs to a set of target UUIDs they are spying on
    private final Map<UUID, Set<UUID>> socialSpyMap;

    public SocialSpyManager() {
        this.socialSpyMap = new HashMap<>();
    }

    /**
     * Get all viewers spying on any of the specified targets.
     * @param targetUUIDs The UUIDs of the targets.
     * @return A list of viewer UUIDs spying on any of the targets.
     */
    public List<UUID> getAllSpyingViewers(UUID... targetUUIDs) {
        Set<UUID> targetSet = new HashSet<>(Arrays.asList(targetUUIDs));
        List<UUID> viewers = new ArrayList<>();
        for (Map.Entry<UUID, Set<UUID>> entry : socialSpyMap.entrySet()) {
            for (UUID targetUUID : targetSet) {
                if (entry.getValue().contains(targetUUID)) {
                    viewers.add(entry.getKey());
                    break; // No need to check other targets for this viewer
                }
            }
        }
        return viewers;
    }

    /**
     * Toggle spying status for a viewer on a target.
     * @param viewerUUID The UUID of the viewer.
     * @param targetUUID The UUID of the target.
     * @return True if now spying, false if no longer spying.
     */
    public boolean toggleSpyOn(UUID viewerUUID, UUID targetUUID) {
        Set<UUID> targets = socialSpyMap.computeIfAbsent(viewerUUID, k -> new HashSet<>());
        if (targets.contains(targetUUID)) {
            targets.remove(targetUUID);
            if (targets.isEmpty()) socialSpyMap.remove(viewerUUID);
            return false; // No longer spying
        } else {
            targets.add(targetUUID);
            return true; // Now spying
        }
    }

    /**
     * Turn off all spying for a viewer.
     * @param viewerUUID The UUID of the viewer.
     */
    public void turnOffAllSpying(UUID viewerUUID) {
        socialSpyMap.remove(viewerUUID);
    }
}
