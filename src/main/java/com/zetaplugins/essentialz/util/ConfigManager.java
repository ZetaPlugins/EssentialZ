package com.zetaplugins.essentialz.util;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class ConfigManager {
    private final JavaPlugin plugin;

    public ConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public FileConfiguration getMaterialsConfig() {
        return getCustomConfig("materials");
    }

    public FileConfiguration getCustomConfig(String fileName) {
        File configFile = new File(plugin.getDataFolder(), fileName + ".yml");
        if (!configFile.exists()) {
            configFile.getParentFile().mkdirs();
            plugin.saveResource(fileName + ".yml", false);
        }

        return YamlConfiguration.loadConfiguration(configFile);
    }
}