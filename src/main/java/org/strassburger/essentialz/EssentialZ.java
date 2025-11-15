package org.strassburger.essentialz;

import com.zetaplugins.zetacore.services.events.AutoEventRegistrar;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.strassburger.essentialz.features.GodModeManager;
import org.strassburger.essentialz.util.commands.CommandManager;
import org.strassburger.essentialz.util.LanguageManager;
import org.strassburger.essentialz.util.MessageManager;

import java.util.List;

public final class EssentialZ extends JavaPlugin {
    private static final String PACKAGE_PREFIX = "org.strassburger.essentialz";

    private LanguageManager languageManager;
    private MessageManager messageManager;

    private GodModeManager godModeManager;

    private final boolean hasPlaceholderApi = Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null;

    @Override
    public void onEnable() {
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();

        languageManager = new LanguageManager(this);
        messageManager = new MessageManager(this);
        godModeManager = new GodModeManager();

        new CommandManager(this).registerCommands();
        List<String> registeredEvents = new AutoEventRegistrar(this, PACKAGE_PREFIX).registerAllListeners();
        getLogger().info("Registered " + registeredEvents.size() + " event listeners.");

        getLogger().info("EssentialZ enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("EssentialZ disabled!");
    }

    public LanguageManager getLanguageManager() {
        return languageManager;
    }

    public MessageManager getMessageManager() {
        return messageManager;
    }

    public GodModeManager getGodModeManager() {
        return godModeManager;
    }
}
