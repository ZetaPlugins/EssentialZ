package com.zetaplugins.essentialz.config.storage;

import com.zetaplugins.zetacore.annotations.PluginConfig;

@PluginConfig("storage.yml")
public class StorageConfig {
    private String type = "SQLite";
    private String host = "localhost";
    private int port = 3306;
    private String database = "essentialz";
    private String username = "root";
    private String password = "password";

    public String getType() {
        return type;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getDatabase() {
        return database;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
