package com.zetaplugins.essentialz.features;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.Nullable;
import com.zetaplugins.essentialz.EssentialZ;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GiveMaterialManager {
    private final EssentialZ plugin;

    public GiveMaterialManager(EssentialZ plugin) {
        this.plugin = plugin;
    }

    /**
     * Get a Material by its key from the materials config. If not found, try to parse the key as a Material name.
     * @param key The key to look up
     * @return The Material, or null if not found
     */
    public @Nullable Material getMaterialByKey(String key) {
        Map<String, Material> materials = getConfigMaterials();
        if (materials.containsKey(key)) return materials.get(key);
        else return parseMaterial(key);
    }

    /**
     * Get a list of all possible material keys/names from the materials config and vanilla materials.
     * @return List of possible material keys/names
     */
    public List<String> getPossibleMaterials() {
        List<String> configMaterials = getConfigMaterials().keySet().stream().toList();
        List<String> vanillaMaterials = Stream.of(Material.values())
                .map(Material::name)
                .map(String::toLowerCase)
                .toList();
        return Stream.concat(configMaterials.stream(), vanillaMaterials.stream())
                .distinct()
                .sorted()
                .toList();
    }

    private Map<String, Material> getConfigMaterials() {
        FileConfiguration config = plugin.getConfigManager().getMaterialsConfig();
        return config.getValues(false).entrySet().stream()
                .filter(entry -> entry.getValue() instanceof String)
                .map(entry -> {
                    String key = entry.getKey();
                    String materialName = (String) entry.getValue();
                    Material material = parseMaterial(materialName);
                    if (material == null) {
                        plugin.getLogger().warning("Invalid material '" + materialName
                                + "' for key '" + key + "' in materials config.");
                    }
                    return new AbstractMap.SimpleEntry<>(key, material);
                })
                .filter(entry -> entry.getValue() != null)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private Material parseMaterial(String name) {
        try {
            return Material.valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
