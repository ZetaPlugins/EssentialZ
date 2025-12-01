package com.zetaplugins.essentialz.features.economy.manager;

import com.zetaplugins.essentialz.EssentialZ;
import com.zetaplugins.essentialz.storage.Storage;
import com.zetaplugins.zetacore.annotations.InjectManager;
import com.zetaplugins.zetacore.annotations.InjectPlugin;
import com.zetaplugins.zetacore.annotations.PostManagerConstruct;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.Map;
import java.util.UUID;

public class VaultEconomyManager implements EconomyManager {
    private Economy economy;

    @InjectPlugin
    private EssentialZ plugin;

    @InjectManager
    private Storage storage;

    @PostManagerConstruct
    public void init() {
        RegisteredServiceProvider<Economy> rsp = plugin.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp != null) economy = rsp.getProvider();
    }

    public double getBalance(OfflinePlayer player) {
        if (economy == null) return 0.0;
        return economy.getBalance(player);
    }

    @Override
    public void deposit(OfflinePlayer player, double amount) {
        if (amount < 0) amount = 0;
        if (economy != null) economy.depositPlayer(player, amount);
    }

    @Override
    public boolean withdraw(OfflinePlayer player, double amount) {
        if (amount < 0) amount = 0;
        if (economy != null) return economy.withdrawPlayer(player, amount).transactionSuccess();
        return false;
    }

    @Override
    public void setBalance(OfflinePlayer player, double amount) {
        if (economy == null) return;

        double currentBalance = economy.getBalance(player);
        double difference = amount - currentBalance;

        if (difference > 0) economy.depositPlayer(player, difference);
        else if (difference < 0) economy.withdrawPlayer(player, -difference);
    }

    /**
     * Format a monetary amount according to the economy's formatting rules.
     *
     * @param amount The amount to format.
     * @return The formatted amount as a string.
     */
    @Override
    public String format(double amount) {
        return economy.format(amount);
    }

    public Economy getEconomy() {
        return economy;
    }

    @Override
    public Map<UUID, Double> getTopBalances(int topN) {
        return storage.getPlayerRepository().getTopBalances(topN);
    }
}
