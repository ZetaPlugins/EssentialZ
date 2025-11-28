package com.zetaplugins.essentialz.features.economy;

import com.zetaplugins.essentialz.EssentialZ;
import com.zetaplugins.zetacore.services.di.ManagerRegistry;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.ServicePriority;

/**
 * Provider class for VaultEconomyImpl instances so no class not found errors occur if Vault is not present.
 */
public class VaultEconomyImplProvider {
    private VaultEconomyImplProvider() {}

    /**
     * Create a VaultEconomyImpl with injected dependencies.
     * @param registry The manager registry to use for dependency injection.
     * @return A VaultEconomyImpl instance.
     */
    public static VaultEconomyImpl make(ManagerRegistry registry) {
        var impl = new VaultEconomyImpl();
        registry.injectManagers(impl);
        return impl;
    }

    /**
     * Register the VaultEconomyImpl with the EssentialZ plugin.
     * @param plugin The EssentialZ plugin instance.
     * @param registry The manager registry to use for dependency injection.
     */
    public static void register(EssentialZ plugin, ManagerRegistry registry) {
        plugin.getServer().getServicesManager().register(Economy.class, make(registry), plugin, ServicePriority.High);
    }
}
