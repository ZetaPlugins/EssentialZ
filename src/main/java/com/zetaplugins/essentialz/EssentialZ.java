package com.zetaplugins.essentialz;

import com.zetaplugins.essentialz.features.economy.VaultEconomyImplProvider;
import com.zetaplugins.essentialz.features.economy.manager.BuiltinEconomyManager;
import com.zetaplugins.essentialz.features.economy.manager.EconomyManager;
import com.zetaplugins.essentialz.features.economy.manager.UnusedEconomyManager;
import com.zetaplugins.essentialz.features.economy.manager.VaultEconomyManager;
import com.zetaplugins.essentialz.storage.MySQLStorage;
import com.zetaplugins.essentialz.storage.SQLiteStorage;
import com.zetaplugins.essentialz.storage.Storage;
import com.zetaplugins.essentialz.util.EszConfig;
import com.zetaplugins.essentialz.util.permissions.Permission;
import com.zetaplugins.zetacore.services.bStats.Metrics;
import com.zetaplugins.zetacore.services.commands.AutoCommandRegistrar;
import com.zetaplugins.zetacore.services.config.ConfigService;
import com.zetaplugins.zetacore.services.di.ManagerRegistry;
import com.zetaplugins.zetacore.services.events.AutoEventRegistrar;
import com.zetaplugins.zetacore.services.updatechecker.ModrinthUpdateChecker;
import com.zetaplugins.zetacore.services.updatechecker.UpdateChecker;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public final class EssentialZ extends JavaPlugin {
    private static final String PACKAGE_PREFIX = "com.zetaplugins.essentialz";
    private static final List<String> ECONOMY_COMMANDS = List.of("balance", "pay", "baltop");

    private ConfigService configManager;
    private ManagerRegistry managerRegistry;

    private final boolean hasPlaceholderApi = Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null;
    private final boolean hasVault = Bukkit.getPluginManager().getPlugin("Vault") != null;

    @Override
    public void onEnable() {
        Permission.registerAll();

        getConfig().options().copyDefaults(true);
        saveDefaultConfig();
        configManager = new ConfigService(this);

        managerRegistry = new ManagerRegistry(this, true, PACKAGE_PREFIX);

        managerRegistry.registerInstance(configManager);
        managerRegistry.registerInstance(Storage.class, createPlayerDataStorage());
        // Registry will automatically create and register other managers as needed

        initUpdateChecker();
        initEconomy();

        registerCommands();
        registerListeners();

        initBstats();

        getLogger().info("EssentialZ enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("EssentialZ disabled!");
    }

    private void initUpdateChecker() {
        UpdateChecker updateChecker = new ModrinthUpdateChecker(this, "FTVoyulY");
        managerRegistry.registerInstance(UpdateChecker.class, updateChecker);
        updateChecker.checkForUpdates(true);
    }

    private void initEconomy() {
        boolean economyEnabled = configManager.getConfig(EszConfig.ECONOMY).getBoolean("enabled", true);
        if (hasVault && economyEnabled) {
            getLogger().info("Vault detected, enabling Vault economy support.");
            VaultEconomyImplProvider.register(this, managerRegistry);
            managerRegistry.registerInstance(EconomyManager.class, new VaultEconomyManager());
        } else if (economyEnabled) {
            getLogger().warning("Vault not detected, using built-in economy manager.");
            managerRegistry.registerInstance(EconomyManager.class, new BuiltinEconomyManager());
        } else {
            getLogger().info("Economy system disabled.");
            managerRegistry.registerInstance(EconomyManager.class, new UnusedEconomyManager());
        }
    }

    private Storage createPlayerDataStorage() {
        return switch (configManager.getConfig(EszConfig.STORAGE).getString("type").toLowerCase()) {
            case "mysql" -> {
                getLogger().info("Using MySQL storage");
                yield new MySQLStorage(this, configManager.getConfig(EszConfig.STORAGE));
            }
            case "sqlite" -> {
                getLogger().info("Using SQLite storage");
                yield new SQLiteStorage(this);
            }
            default -> {
                getLogger().warning("Invalid storage type in config.yml! Using SQLite storage as fallback.");
                yield new SQLiteStorage(this);
            }
        };
    }

    private void registerCommands() {
        List<String> registeredCommands = new AutoCommandRegistrar.Builder()
                .setPlugin(this)
                .setPackagePrefix(PACKAGE_PREFIX)
                .setManagerRegistry(managerRegistry)
                .build()
                .registerAllCommands(this::shouldRegisterCommand);

        FileConfiguration config = this.configManager.getConfig(EszConfig.COMMANDS);
        registeredCommands.sort(String::compareTo);
        for (String command : registeredCommands) {
            if (!config.contains(command)) {
                config.set(command, true);
            }
        }
        this.configManager.saveConfig(EszConfig.COMMANDS, config);

        getLogger().info("Registered " + registeredCommands.size() + " commands.");
    }

    private void registerListeners() {
        List<String> registeredEventListeners = new AutoEventRegistrar.Builder()
                .setPlugin(this)
                .setPackagePrefix(PACKAGE_PREFIX)
                .setManagerRegistry(managerRegistry)
                .build()
                .registerAllListeners();
        getLogger().info("Registered " + registeredEventListeners.size() + " event listeners.");
    }

    /**
     * Determine if a command should be registered based on the config settings.
     * @param commandName The name of the command.
     * @return True if the command should be registered, false otherwise.
     */
    private boolean shouldRegisterCommand(String commandName) {
        boolean economyEnabled = configManager.getConfig(EszConfig.ECONOMY).getBoolean("enabled", true);
        if (ECONOMY_COMMANDS.contains(commandName) && !economyEnabled) return false;
        return configManager.getConfig(EszConfig.COMMANDS).getBoolean(commandName, true);
    }

    private void initBstats() {
        final int pluginId = 28226;
        Metrics metrics = new Metrics(this, pluginId);

        metrics.addCustomChart(new Metrics.SimplePie("storage_type", () -> configManager.getConfig(EszConfig.STORAGE).getString("type")));
        metrics.addCustomChart(new Metrics.SimplePie("language", () -> getConfig().getString("lang")));
        metrics.addCustomChart(new Metrics.SimplePie("economy_system", () -> {
            EconomyManager economyManager = managerRegistry.getOrCreate(EconomyManager.class);
            if (economyManager instanceof VaultEconomyManager) return "vault";
            else if (economyManager instanceof BuiltinEconomyManager) return "builtin";
            else return "disabled";
        }));
        metrics.addCustomChart(new Metrics.SimplePie("chat_enabled", () -> {
            boolean chatEnabled = configManager.getConfig(EszConfig.CHAT).getBoolean("enableCustomChat", true);
            return chatEnabled ? "true" : "false";
        }));

        getLogger().info("bStats metrics initialized.");
    }
}
