package com.zetaplugins.essentialz.features.tpa;

import com.zetaplugins.essentialz.EssentialZ;
import com.zetaplugins.zetacore.annotations.Manager;
import com.zetaplugins.zetacore.annotations.PostManagerConstruct;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Manages TPA requests between players.
 */
@Manager
public class TpaManager {

    private final EssentialZ plugin;
    private final Map<UUID, List<TeleportRequest>> incomingRequests;
    private final Map<UUID, List<TeleportRequest>> outgoingRequests;

    private int requestExpiryTime = 120;

    public TpaManager(EssentialZ plugin) {
        this.plugin = plugin;
        this.incomingRequests = new ConcurrentHashMap<>();
        this.outgoingRequests = new ConcurrentHashMap<>();
    }

    @PostManagerConstruct
    public void init() {
        loadConfig();
        startExpiryTask();
    }

    private void loadConfig() {
        requestExpiryTime = plugin.getConfig().getInt("tpa.requestExpiryTime", 120);
    }

    private void startExpiryTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                cleanExpiredRequests();
            }
        }.runTaskTimer(plugin, 20L, 20L);
    }

    /**
     * Creates a new teleport request.
     * @param sender The UUID of the player sending the request
     * @param target The UUID of the player receiving the request
     * @param type The type of request (TPA or TPA_HERE)
     * @return The created request, or null if a request already exists
     */
    public TeleportRequest createRequest(UUID sender, UUID target, TpaRequestType type) {
        if (hasRequest(sender, target)) {
            return null;
        }

        TeleportRequest request = new TeleportRequest(sender, target, type, requestExpiryTime);

        incomingRequests.computeIfAbsent(target, k -> new ArrayList<>()).add(request);
        outgoingRequests.computeIfAbsent(sender, k -> new ArrayList<>()).add(request);

        return request;
    }

    /**
     * Checks if a request already exists between two players.
     * @param sender The sender's UUID
     * @param target The target's UUID
     * @return true if a non-expired request exists
     */
    public boolean hasRequest(UUID sender, UUID target) {
        List<TeleportRequest> requests = incomingRequests.get(target);
        if (requests == null) return false;

        return requests.stream()
                .anyMatch(r -> r.getSender().equals(sender) && !r.isExpired());
    }

    /**
     * Gets the latest non-expired request for a player.
     * @param target The target player's UUID
     * @return The latest request, or null if none exists
     */
    public TeleportRequest getLatestRequest(UUID target) {
        List<TeleportRequest> requests = incomingRequests.get(target);
        if (requests == null || requests.isEmpty()) return null;

        for (int i = requests.size() - 1; i >= 0; i--) {
            TeleportRequest request = requests.get(i);
            if (!request.isExpired()) {
                return request;
            }
        }
        return null;
    }

    /**
     * Gets all incoming non-expired requests for a player.
     * @param target The target player's UUID
     * @return List of incoming requests
     */
    public List<TeleportRequest> getIncomingRequests(UUID target) {
        List<TeleportRequest> requests = incomingRequests.get(target);
        if (requests == null) return new ArrayList<>();

        requests.removeIf(TeleportRequest::isExpired);
        return new ArrayList<>(requests);
    }

    /**
     * Gets all outgoing non-expired requests for a player.
     * @param sender The sender player's UUID
     * @return List of outgoing requests
     */
    public List<TeleportRequest> getOutgoingRequests(UUID sender) {
        List<TeleportRequest> requests = outgoingRequests.get(sender);
        if (requests == null) return new ArrayList<>();

        requests.removeIf(TeleportRequest::isExpired);
        return new ArrayList<>(requests);
    }

    /**
     * Removes a specific request.
     * @param request The request to remove
     */
    public void removeRequest(TeleportRequest request) {
        List<TeleportRequest> incoming = incomingRequests.get(request.getTarget());
        if (incoming != null) {
            incoming.remove(request);
            if (incoming.isEmpty()) {
                incomingRequests.remove(request.getTarget());
            }
        }

        List<TeleportRequest> outgoing = outgoingRequests.get(request.getSender());
        if (outgoing != null) {
            outgoing.remove(request);
            if (outgoing.isEmpty()) {
                outgoingRequests.remove(request.getSender());
            }
        }
    }

    /**
     * Cleans up all expired requests and notifies players.
     */
    public void cleanExpiredRequests() {
        incomingRequests.values().forEach(list -> list.removeIf(TeleportRequest::isExpired));
        outgoingRequests.values().forEach(list -> list.removeIf(TeleportRequest::isExpired));

        incomingRequests.entrySet().removeIf(entry -> entry.getValue().isEmpty());
        outgoingRequests.entrySet().removeIf(entry -> entry.getValue().isEmpty());
    }

    /**
     * Clears all requests for a player (called on quit).
     * @param player The player's UUID
     */
    public void clearPlayerRequests(UUID player) {
        incomingRequests.remove(player);
        outgoingRequests.remove(player);
    }

    public int getRequestExpiryTime() {
        return requestExpiryTime;
    }
}
