package com.zetaplugins.essentialz.util;

import com.zetaplugins.zetacore.services.config.PluginConfig;

public enum EszConfig implements PluginConfig {
    MAIN("config"),
    STORAGE("storage"),
    CHAT("chat"),
    MATERIALS("materials"),
    COMMANDS("commands"),
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
