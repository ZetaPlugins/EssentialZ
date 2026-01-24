package com.zetaplugins.essentialz.features.tpa;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Represents a teleport request between two players.
 */
public class TeleportRequest {

    private final UUID sender;
    private final UUID target;
    private final TpaRequestType type;
    private final long creationTime;
    private final long expiryTime;

    public TeleportRequest(UUID sender, UUID target, TpaRequestType type, int expirySeconds) {
        this.sender = sender;
        this.target = target;
        this.type = type;
        this.creationTime = System.currentTimeMillis();
        this.expiryTime = creationTime + (expirySeconds * 1000L);
    }

    public UUID getSender() {
        return sender;
    }

    public UUID getTarget() {
        return target;
    }

    public TpaRequestType getType() {
        return type;
    }

    public long getCreationTime() {
        return creationTime;
    }

    public long getExpiryTime() {
        return expiryTime;
    }

    public boolean isExpired() {
        return System.currentTimeMillis() > expiryTime;
    }

    /**
     * Gets the sender player if online.
     * @return The sender player or null if offline
     */
    public Player getSenderPlayer() {
        return Bukkit.getPlayer(sender);
    }

    /**
     * Gets the target player if online.
     * @return The target player or null if offline
     */
    public Player getTargetPlayer() {
        return Bukkit.getPlayer(target);
    }
}
