package com.zetaplugins.essentialz.features;

import com.zetaplugins.essentialz.config.main.MainConfig;
import com.zetaplugins.essentialz.util.EszConfig;
import com.zetaplugins.zetacore.annotations.InjectManager;
import com.zetaplugins.zetacore.annotations.Manager;
import com.zetaplugins.zetacore.services.config.ConfigService;
import com.zetaplugins.zetacore.util.LocationDeserializationException;
import com.zetaplugins.zetacore.util.LocationSerializer;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

@Manager
public class SpawnManager {

    @InjectManager
    private ConfigService configService;

    /**
     * Sets the server spawn location in the configuration.
     * @param location The location to set as the spawn point.
     */
    public void setSpawnLocation(Location location) {
        FileConfiguration config = configService.getConfig(EszConfig.MAIN);
        config.set("spawnLocation", LocationSerializer.serializeLocation(location));
        configService.saveConfig(EszConfig.MAIN, config);
    }

    /**
     * Gets the server spawn location from the configuration.
     * @return The spawn location as a Location object.
     */
    public Location getSpawnLocation() throws LocationDeserializationException {
        MainConfig config = configService.getConfig(MainConfig.class);
        return LocationSerializer.deserializeLocation(config.getSpawnLocation());
    }
}
