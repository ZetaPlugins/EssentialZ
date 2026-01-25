package com.zetaplugins.essentialz;

import com.zetaplugins.essentialz.config.chat.ChatConfig;
import com.zetaplugins.essentialz.config.economy.EconomyConfig;
import com.zetaplugins.essentialz.config.main.MainConfig;
import com.zetaplugins.essentialz.config.storage.StorageConfig;
import com.zetaplugins.essentialz.features.economy.VaultEconomyImplProvider;
import com.zetaplugins.essentialz.features.economy.manager.BuiltinEconomyManager;
import com.zetaplugins.essentialz.features.economy.manager.EconomyManager;
import com.zetaplugins.essentialz.features.economy.manager.UnusedEconomyManager;
import com.zetaplugins.essentialz.features.economy.manager.VaultEconomyManager;
import com.zetaplugins.essentialz.features.papi.EconomyPlaceholders;
import com.zetaplugins.essentialz.features.papi.OtherPlaceholders;
import com.zetaplugins.essentialz.storage.MySQLStorage;
import com.zetaplugins.essentialz.storage.SQLiteStorage;
import com.zetaplugins.essentialz.storage.Storage;
import com.zetaplugins.essentialz.util.EszConfig;
import com.zetaplugins.essentialz.util.permissions.Permission;
import com.zetaplugins.zetacore.ZetaCorePlugin;
import com.zetaplugins.zetacore.services.bStats.Metrics;
import com.zetaplugins.zetacore.services.commands.AutoCommandRegistrar;
import com.zetaplugins.zetacore.services.config.ConfigService;
import com.zetaplugins.zetacore.services.di.ManagerRegistry;
import com.zetaplugins.zetacore.services.events.AutoEventRegistrar;
import com.zetaplugins.zetacore.services.papi.PapiExpansionService;
import com.zetaplugins.zetacore.services.updatechecker.HangarUpdateChecker;
import com.zetaplugins.zetacore.services.updatechecker.UpdateChecker;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;
import java.util.UUID;

public final class EssentialZ extends ZetaCorePlugin {
    public static final UUID CONSOLE_UUID = UUID.fromString("00000000-0000-0000-0000-000000000000");
    private static final String PACKAGE_PREFIX = "com.zetaplugins.essentialz";
    private static final List<String> ECONOMY_COMMANDS = List.of("balance", "pay", "baltop");

    private ConfigService configManager;
    private ManagerRegistry managerRegistry;

    private final boolean hasPlaceholderApi = Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null;
    private final boolean hasVault = Bukkit.getPluginManager().getPlugin("Vault") != null;
    private final boolean hasFloodgate = Bukkit.getPluginManager().getPlugin("floodgate") != null;

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

        initPlaceholderAPI();
        initBstats();
        initFloodgate();

        getLogger().info("EssentialZ enabled!");
    }

    private void initFloodgate() {
        if (hasFloodgate) {
            boolean formsEnabled = getConfig().getBoolean("tpa.bedrock.formsEnabled", true);
            if (formsEnabled) {
                getLogger().info("Floodgate detected, TPA Bedrock forms enabled.");
            } else {
                getLogger().info("Floodgate detected, but TPA Bedrock forms are disabled in config.");
            }
        }
    }

    @Override
    public void onDisable() {
        getLogger().info("EssentialZ disabled!");
    }

    private void initUpdateChecker() {
        UpdateChecker updateChecker = new HangarUpdateChecker(this, "KartoffelChipss", "EssentialZ");
        managerRegistry.registerInstance(UpdateChecker.class, updateChecker);
        updateChecker.checkForUpdates(true);
    }

    private void initEconomy() {
        boolean economyEnabled = configManager.getConfig(EconomyConfig.class).isEnabled();
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
        return switch (configManager.getConfig(StorageConfig.class).getType().toLowerCase()) {
            case "mysql" -> {
                getLogger().info("Using MySQL storage");
                yield new MySQLStorage(this, configManager.getConfig(StorageConfig.class));
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
        boolean economyEnabled = configManager.getConfig(EconomyConfig.class).isEnabled();
        if (ECONOMY_COMMANDS.contains(commandName) && !economyEnabled) return false;
        return configManager.getConfig(EszConfig.COMMANDS).getBoolean(commandName, true);
    }

    private void initPlaceholderAPI() {
        if (!hasPlaceholderApi) return;

        boolean success = new PapiExpansionService(this)
                .setIdentifier("essentialz")
                .setAuthor("ZetaPlugins")
                .addAnnotatedPlaceholders(managerRegistry.getOrCreate(EconomyPlaceholders.class))
                .addAnnotatedPlaceholders(managerRegistry.getOrCreate(OtherPlaceholders.class))
                .register();

        if (success) getLogger().info("PlaceholderAPI expansion registered successfully.");
        else getLogger().warning("Failed to register PlaceholderAPI expansion.");
    }

    private void initBstats() {
        final int pluginId = 28226;
        Metrics metrics = createBStatsMetrics(pluginId);

        MainConfig mainConfig = configManager.getConfig(MainConfig.class);

        metrics.addCustomChart(new Metrics.SimplePie("storageType", () -> configManager.getConfig(StorageConfig.class).getType()));
        metrics.addCustomChart(new Metrics.SimplePie("language", mainConfig::getLanguage));
        metrics.addCustomChart(new Metrics.SimplePie("economySystem", () -> {
            EconomyManager economyManager = managerRegistry.getOrCreate(EconomyManager.class);
            if (economyManager instanceof VaultEconomyManager) return "vault";
            else if (economyManager instanceof BuiltinEconomyManager) return "builtin";
            else return "disabled";
        }));
        metrics.addCustomChart(new Metrics.SimplePie("chatEnabled", () -> {
            boolean chatEnabled = configManager.getConfig(ChatConfig.class).isEnableCustomChat();
            return chatEnabled ? "true" : "false";
        }));

        getLogger().info("bStats metrics initialized.");
    }
}
