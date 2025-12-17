package com.zetaplugins.essentialz.config.main;

import com.zetaplugins.zetacore.annotations.PluginConfig;

import java.util.Map;

@PluginConfig("config.yml")
public class MainConfig {
    private String language = "en-US";
    private boolean updateNotifications = true;
    private int maxItemNameLength = 100;
    private int maxLoreLines = 10;
    private int defaultMaxHomes = 3;
    private Map<String, MessageStyleConfigSection> styles;

    public String getLanguage() {
        return language;
    }

    public boolean isUpdateNotifications() {
        return updateNotifications;
    }

    public int getMaxItemNameLength() {
        return maxItemNameLength;
    }

    public int getMaxLoreLines() {
        return maxLoreLines;
    }

    public int getDefaultMaxHomes() {
        return defaultMaxHomes;
    }

    public Map<String, MessageStyleConfigSection> getStyles() {
        return styles;
    }
}
