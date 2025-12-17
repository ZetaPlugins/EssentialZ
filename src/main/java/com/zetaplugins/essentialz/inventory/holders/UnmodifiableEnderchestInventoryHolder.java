package com.zetaplugins.essentialz.inventory.holders;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class UnmodifiableEnderchestInventoryHolder implements InventoryHolder {
    private final UUID owner;

    public UnmodifiableEnderchestInventoryHolder(UUID owner) {
        this.owner = owner;
    }

    public UUID getOwner() {
        return owner;
    }

    @Override
    public @NotNull Inventory getInventory() {
        return null; // Not used
    }
}
