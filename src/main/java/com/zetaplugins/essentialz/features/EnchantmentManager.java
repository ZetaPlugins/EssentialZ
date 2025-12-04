package com.zetaplugins.essentialz.features;

import com.zetaplugins.zetacore.annotations.Manager;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import net.kyori.adventure.key.Key;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@Manager
public class EnchantmentManager {

    /**
     * Get a list of all enchantments.
     * @return A list of all Enchantment objects.
     */
    public List<Enchantment> getAllEnchantments() {
        var enchantments = RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT);
        return enchantments.stream().toList();
    }

    /**
     * Get a list of all enchantment key names.
     * @return A list of enchantment key names as strings.
     */
    public List<String> getAllEnchantmentNames() {
        var enchantments = RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT);
        return enchantments.stream()
                .map(enchantment -> enchantment.getKey().asString())
                .map(s -> s.replace("minecraft:", ""))
                .toList();
    }

    /**
     * Get an enchantment by its key name.
     * @param keyName The key name of the enchantment (e.g., "minecraft:sharpness").
     * @return The Enchantment object, or null if not found.
     */
    public Enchantment getEnchantmentByKeyName(String keyName) {
        if (keyName == null || keyName.isEmpty()) return null;
        String formattedKeyName = keyName.contains(":") ? keyName : "minecraft:" + keyName;
        var enchantments = RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT);
        return enchantments.get(Key.key(formattedKeyName));
    }

    public List<String> getEnchantmentKeyNamesFromItem(ItemStack item) {
        var enchantments = item.getEnchantments().keySet();
        return enchantments.stream()
                .map(enchantment -> enchantment.getKey().asString())
                .map(s -> s.replace("minecraft:", ""))
                .toList();
    }
}
