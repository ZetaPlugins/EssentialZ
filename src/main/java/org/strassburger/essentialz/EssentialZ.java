package org.strassburger.essentialz;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.strassburger.essentialz.util.commands.CommandManager;
import org.strassburger.essentialz.util.LanguageManager;
import org.strassburger.essentialz.util.MessageManager;

public final class EssentialZ extends JavaPlugin {
    private LanguageManager languageManager;
    private MessageManager messageManager;
    private final boolean hasPlaceholderApi = Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null;

    @Override
    public void onEnable() {
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();

        languageManager = new LanguageManager(this);
        messageManager = new MessageManager(this);

        new CommandManager(this).registerCommands();

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
}
