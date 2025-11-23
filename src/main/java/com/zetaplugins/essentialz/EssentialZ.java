package com.zetaplugins.essentialz;

import com.zetaplugins.essentialz.features.EnchantmentManager;
import com.zetaplugins.essentialz.features.LastMsgManager;
import com.zetaplugins.essentialz.storage.MySQLStorage;
import com.zetaplugins.essentialz.storage.SQLiteStorage;
import com.zetaplugins.essentialz.storage.Storage;
import com.zetaplugins.essentialz.util.permissions.Permission;
import com.zetaplugins.zetacore.services.commands.AutoCommandRegistrar;
import com.zetaplugins.zetacore.services.di.ManagerRegistry;
import com.zetaplugins.zetacore.services.events.AutoEventRegistrar;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import com.zetaplugins.essentialz.features.GiveMaterialManager;
import com.zetaplugins.essentialz.features.GodModeManager;
import com.zetaplugins.essentialz.util.ConfigManager;
import com.zetaplugins.essentialz.util.LanguageManager;
import com.zetaplugins.essentialz.util.MessageManager;

import java.util.List;

public final class EssentialZ extends JavaPlugin {
    private static final String PACKAGE_PREFIX = "com.zetaplugins.essentialz";

    private ConfigManager configManager;

    private final boolean hasPlaceholderApi = Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null;

    @Override
    public void onEnable() {
        Permission.registerAll();

        getConfig().options().copyDefaults(true);
        saveDefaultConfig();

        configManager = new ConfigManager(this);

        ManagerRegistry managerRegistry = new ManagerRegistry(this);

        managerRegistry.registerInstance(configManager);
        managerRegistry.registerInstance(Storage.class, createPlayerDataStorage());
        managerRegistry.registerInstance(new LanguageManager(this));
        managerRegistry.registerInstance(new MessageManager(this));

        managerRegistry.registerInstance(new GiveMaterialManager(this));
        managerRegistry.registerInstance(new GodModeManager());
        managerRegistry.registerInstance(new LastMsgManager());
        managerRegistry.registerInstance(new EnchantmentManager());

        List<String> registeredCommands = new AutoCommandRegistrar.Builder()
                .setPlugin(this)
                .setPackagePrefix(PACKAGE_PREFIX)
                .setManagerRegistry(managerRegistry)
                .build()
                .registerAllCommands();
        getLogger().info("Registered " + registeredCommands.size() + " commands.");

        List<String> registeredEvents = new AutoEventRegistrar.Builder()
                .setPlugin(this)
                .setPackagePrefix(PACKAGE_PREFIX)
                .setManagerRegistry(managerRegistry)
                .build()
                .registerAllListeners();
        getLogger().info("Registered " + registeredEvents.size() + " event listeners.");

        getLogger().info("EssentialZ enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("EssentialZ disabled!");
    }

    private Storage createPlayerDataStorage() {
        switch (configManager.getStorageConfig().getString("type").toLowerCase()) {
            case "mysql":
                getLogger().info("Using MySQL storage");
                return new MySQLStorage(this);
            case "sqlite":
                getLogger().info("Using SQLite storage");
                return new SQLiteStorage(this);
            default:
                getLogger().warning("Invalid storage type in config.yml! Using SQLite storage as fallback.");
                return new SQLiteStorage(this);
        }
    }
}
