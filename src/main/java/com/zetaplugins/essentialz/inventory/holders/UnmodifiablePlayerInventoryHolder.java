package com.zetaplugins.essentialz.inventory.holders;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class UnmodifiablePlayerInventoryHolder implements InventoryHolder {
    private final UUID viewer;
    private final UUID target;

    public UnmodifiablePlayerInventoryHolder(UUID viewer, UUID target) {
        this.viewer = viewer;
        this.target = target;
    }

    public UUID getViewer() {
        return viewer;
    }

    public UUID getTarget() {
        return target;
    }

    @Override
    public @NotNull Inventory getInventory() {
        return null; // Not used
    }
}
