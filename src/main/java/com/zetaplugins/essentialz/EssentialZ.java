package com.zetaplugins.essentialz;

import com.zetaplugins.essentialz.features.EnchantmentManager;
import com.zetaplugins.essentialz.features.GiveMaterialManager;
import com.zetaplugins.essentialz.features.GodModeManager;
import com.zetaplugins.essentialz.features.LastMsgManager;
import com.zetaplugins.essentialz.features.economy.VaultEconomyImplProvider;
import com.zetaplugins.essentialz.features.economy.manager.BuiltinEconomyManager;
import com.zetaplugins.essentialz.features.economy.manager.EconomyManager;
import com.zetaplugins.essentialz.features.economy.manager.UnusedEconomyManager;
import com.zetaplugins.essentialz.features.economy.manager.VaultEconomyManager;
import com.zetaplugins.essentialz.storage.MySQLStorage;
import com.zetaplugins.essentialz.storage.SQLiteStorage;
import com.zetaplugins.essentialz.storage.Storage;
import com.zetaplugins.essentialz.util.EszConfig;
import com.zetaplugins.essentialz.util.LanguageManager;
import com.zetaplugins.essentialz.util.MessageManager;
import com.zetaplugins.essentialz.util.permissions.Permission;
import com.zetaplugins.zetacore.services.commands.AutoCommandRegistrar;
import com.zetaplugins.zetacore.services.config.ConfigService;
import com.zetaplugins.zetacore.services.di.ManagerRegistry;
import com.zetaplugins.zetacore.services.events.AutoEventRegistrar;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public final class EssentialZ extends JavaPlugin {
    private static final String PACKAGE_PREFIX = "com.zetaplugins.essentialz";
    private static final List<String> ECONOMY_COMMANDS = List.of("balance", "pay", "baltop");

    private ConfigService configManager;

    private final boolean hasPlaceholderApi = Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null;
    private final boolean hasVault = Bukkit.getPluginManager().getPlugin("Vault") != null;

    @Override
    public void onEnable() {
        Permission.registerAll();

        getConfig().options().copyDefaults(true);
        saveDefaultConfig();
        configManager = new ConfigService(this);

        ManagerRegistry managerRegistry = new ManagerRegistry(this);

        managerRegistry.registerInstance(configManager);
        managerRegistry.registerInstance(Storage.class, createPlayerDataStorage());
        managerRegistry.registerInstance(new LanguageManager(this));
        managerRegistry.registerInstance(new MessageManager(this));

        managerRegistry.registerInstance(new GiveMaterialManager(this));
        managerRegistry.registerInstance(new GodModeManager());
        managerRegistry.registerInstance(new LastMsgManager());
        managerRegistry.registerInstance(new EnchantmentManager());

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

        List<String> registeredCommands = new AutoCommandRegistrar.Builder()
                .setPlugin(this)
                .setPackagePrefix(PACKAGE_PREFIX)
                .setManagerRegistry(managerRegistry)
                .build()
                .registerAllCommands(this::shouldRegisterCommand);
        writeCommandsInConfig(registeredCommands);
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
        switch (configManager.getConfig(EszConfig.STORAGE).getString("type").toLowerCase()) {
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

    /**
     * Write the registered commands into the commands config file.
     * @param commands The list of registered commands.
     */
    private void writeCommandsInConfig(List<String> commands) {
        FileConfiguration config = this.configManager.getConfig(EszConfig.COMMANDS);
        commands.sort(String::compareTo);
        for (String command : commands) {
            if (!config.contains(command)) {
                config.set(command, true);
            }
        }
        this.configManager.saveConfig(EszConfig.COMMANDS, config);
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
}
