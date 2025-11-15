package com.zetaplugins.essentialz.features;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.Nullable;
import com.zetaplugins.essentialz.EssentialZ;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GiveMaterialManager {
    private final EssentialZ plugin;
    private final Map<String, Material> normalizedLookup;

    public GiveMaterialManager(EssentialZ plugin) {
        this.plugin = plugin;
        this.normalizedLookup = buildNormalizedLookup();
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
     * Get a list of all possible material keys/names from the materials config and vanilla materials,
     * @return List of possible material keys/names
     */
    public List<String> getPossibleMaterials() {
        List<String> configMaterials = getConfigMaterials().keySet().stream().toList();
        List<String> vanillaMaterials = Stream.of(Material.values())
                .filter(Material::isItem)
                .map(Material::name)
                .map(String::toLowerCase)
                .toList();
        List<String> normalizedNames = normalizedLookup.keySet().stream()
                .map(String::toLowerCase)
                .toList();
        return Stream.concat(
                        Stream.concat(configMaterials.stream(), vanillaMaterials.stream()),
                        normalizedNames.stream()
                )
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
        if (name == null) return null;
        String normalized = normalize(name);
        return normalizedLookup.get(normalized);
    }

    private String normalize(String name) {
        return name.replaceAll("[^A-Za-z0-9]", "").toUpperCase();
    }

    private Map<String, Material> buildNormalizedLookup() {
        Map<String, Material> map = new HashMap<>();
        Arrays.stream(Material.values())
                .filter(Material::isItem)
                .forEach(mat -> {
                    String normalized = normalize(mat.name());
                    map.put(normalized, mat);
                });
        return map;
    }
}