package com.zetaplugins.essentialz.features.economy.manager;

import com.zetaplugins.essentialz.features.economy.EconomyConfig;
import com.zetaplugins.essentialz.features.economy.EconomyUtil;
import com.zetaplugins.essentialz.storage.Storage;
import com.zetaplugins.essentialz.storage.model.PlayerData;
import com.zetaplugins.essentialz.util.EszConfig;
import com.zetaplugins.zetacore.annotations.InjectManager;
import com.zetaplugins.zetacore.annotations.PostManagerConstruct;
import com.zetaplugins.zetacore.services.config.ConfigService;
import org.bukkit.OfflinePlayer;

public class BuiltinEconomyManager implements EconomyManager {
    private EconomyConfig currencyConfig;

    @InjectManager
    private ConfigService configService;

    @InjectManager
    private Storage storage;

    @PostManagerConstruct
    public void init() {
        currencyConfig = new EconomyConfig(configService.getConfig(EszConfig.ECONOMY));
    }

    @Override
    public double getBalance(OfflinePlayer player) {
        return storage.load(player.getUniqueId()).getBalance();
    }

    @Override
    public void deposit(OfflinePlayer player, double amount) {
        if (amount < 0) amount = 0;
        PlayerData playerData = storage.load(player.getUniqueId());
        double currentBalance = playerData.getBalance();
        playerData.setBalance(currentBalance + amount);
        storage.save(playerData);
    }

    @Override
    public boolean withdraw(OfflinePlayer player, double amount) {
        if (amount < 0) amount = 0;
        PlayerData playerData = storage.load(player.getUniqueId());
        double currentBalance = playerData.getBalance();
        if (currentBalance < amount) return false;
        playerData.setBalance(currentBalance - amount);
        storage.save(playerData);
        return true;
    }

    @Override
    public void setBalance(OfflinePlayer player, double amount) {
        if (amount < 0) amount = 0;
        PlayerData playerData = storage.load(player.getUniqueId());
        playerData.setBalance(amount);
        storage.save(playerData);
    }

    /**
     * Format a monetary amount according to the economy's formatting rules.
     *
     * @param amount The amount to format.
     * @return The formatted amount as a string.
     */
    @Override
    public String format(double amount) {
        return EconomyUtil.formatCurrency(amount, currencyConfig);
    }
}
