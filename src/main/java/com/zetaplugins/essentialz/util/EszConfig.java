package com.zetaplugins.essentialz.util;

import com.zetaplugins.zetacore.services.config.PluginConfigFile;

public enum EszConfig implements PluginConfigFile {
    /**
     * @deprecated use {@link com.zetaplugins.essentialz.config.main.MainConfig} instead where possible
     */
    MAIN("config"),
    /**
     * @deprecated use {@link com.zetaplugins.essentialz.config.storage.StorageConfig} instead where possible
     */
    STORAGE("storage"),
    /**
     * @deprecated use {@link com.zetaplugins.essentialz.config.chat.ChatConfig} instead where possible
     */
    CHAT("chat"),
    MATERIALS("materials"),
    COMMANDS("commands"),
    /**
     * @deprecated use {@link com.zetaplugins.essentialz.config.economy.EconomyConfig} instead where possible
     */
    ECONOMY("economy"),
    ;

    private final String fileName;

    EszConfig(String filename) {
        this.fileName = filename;
    }

    @Override
    public String getFileName() {
        return fileName;
    }
}
