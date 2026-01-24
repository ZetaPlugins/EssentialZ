package com.zetaplugins.essentialz.features.tpa;

import com.zetaplugins.essentialz.EssentialZ;
import com.zetaplugins.zetacore.annotations.Manager;
import com.zetaplugins.zetacore.annotations.PostManagerConstruct;

import java.io.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Manages TPA toggle state for players.
 */
@Manager
public class TpaToggleManager {

    private final EssentialZ plugin;
    private final Set<UUID> disabledPlayers;
    private final File toggleFile;

    public TpaToggleManager(EssentialZ plugin) {
        this.plugin = plugin;
        this.disabledPlayers = new HashSet<>();
        this.toggleFile = new File(plugin.getDataFolder(), "tpa-toggles.dat");
    }

    @PostManagerConstruct
    public void init() {
        loadToggles();
    }

    /**
     * Checks if TPA is enabled for a player.
     * @param player The player's UUID
     * @return true if TPA is enabled for the player
     */
    public boolean isEnabled(UUID player) {
        return !disabledPlayers.contains(player);
    }

    /**
     * Toggles TPA state for a player.
     * @param player The player's UUID
     */
    public void toggle(UUID player) {
        if (disabledPlayers.contains(player)) {
            disabledPlayers.remove(player);
        } else {
            disabledPlayers.add(player);
        }
        saveToggles();
    }

    /**
     * Sets the TPA state for a player.
     * @param player The player's UUID
     * @param enabled Whether TPA should be enabled
     */
    public void setEnabled(UUID player, boolean enabled) {
        if (enabled) {
            disabledPlayers.remove(player);
        } else {
            disabledPlayers.add(player);
        }
        saveToggles();
    }

    /**
     * Loads toggle states from file.
     */
    public void loadToggles() {
        if (!toggleFile.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(toggleFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    UUID uuid = UUID.fromString(line.trim());
                    disabledPlayers.add(uuid);
                } catch (IllegalArgumentException e) {
                    plugin.getLogger().warning("Invalid UUID in tpa-toggles.dat: " + line);
                }
            }
        } catch (IOException e) {
            plugin.getLogger().severe("Failed to load TPA toggles: " + e.getMessage());
        }
    }

    /**
     * Saves toggle states to file.
     */
    public void saveToggles() {
        try {
            if (!toggleFile.exists()) {
                toggleFile.getParentFile().mkdirs();
                toggleFile.createNewFile();
            }

            try (PrintWriter writer = new PrintWriter(new FileWriter(toggleFile))) {
                for (UUID uuid : disabledPlayers) {
                    writer.println(uuid.toString());
                }
            }
        } catch (IOException e) {
            plugin.getLogger().severe("Failed to save TPA toggles: " + e.getMessage());
        }
    }
}
