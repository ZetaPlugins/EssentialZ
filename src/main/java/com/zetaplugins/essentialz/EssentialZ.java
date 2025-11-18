package com.zetaplugins.essentialz;

import com.zetaplugins.essentialz.features.LastMsgManager;
import com.zetaplugins.essentialz.storage.MySQLStorage;
import com.zetaplugins.essentialz.storage.SQLiteStorage;
import com.zetaplugins.essentialz.storage.Storage;
import com.zetaplugins.zetacore.services.commands.AutoCommandRegistrar;
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

    private LanguageManager languageManager;
    private MessageManager messageManager;
    private ConfigManager configManager;

    private GodModeManager godModeManager;
    private GiveMaterialManager giveMaterialManager;
    private LastMsgManager lastMsgManager;

    private Storage storage;

    private final boolean hasPlaceholderApi = Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null;

    @Override
    public void onEnable() {
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();
        configManager = new ConfigManager(this);

        storage = createPlayerDataStorage();
        storage.init();

        languageManager = new LanguageManager(this);
        messageManager = new MessageManager(this);

        godModeManager = new GodModeManager();
        giveMaterialManager = new GiveMaterialManager(this);
        lastMsgManager = new LastMsgManager();

        List<String> registeredCommands = new AutoCommandRegistrar(this, PACKAGE_PREFIX).registerAllCommands();
        getLogger().info("Registered " + registeredCommands.size() + " commands.");
        List<String> registeredEvents = new AutoEventRegistrar(this, PACKAGE_PREFIX).registerAllListeners();
        getLogger().info("Registered " + registeredEvents.size() + " event listeners.");

        getLogger().info("EssentialZ enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("EssentialZ disabled!");
    }

    private Storage createPlayerDataStorage() {
        switch (getConfigManager().getStorageConfig().getString("type").toLowerCase()) {
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

    public Storage getStorage() {
        return storage;
    }

    public LanguageManager getLanguageManager() {
        return languageManager;
    }

    public MessageManager getMessageManager() {
        return messageManager;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public GiveMaterialManager getGiveMaterialManager() {
        return giveMaterialManager;
    }

    public GodModeManager getGodModeManager() {
        return godModeManager;
    }

    public LastMsgManager getLastMsgManager() {
        return lastMsgManager;
    }
}
