package com.zetaplugins.essentialz.features.economy.manager;

import com.zetaplugins.essentialz.EssentialZ;
import com.zetaplugins.essentialz.config.economy.CurrencyFormatConfigSection;
import com.zetaplugins.essentialz.config.economy.EconomyConfig;
import com.zetaplugins.essentialz.features.economy.EconomyUtil;
import com.zetaplugins.essentialz.storage.Storage;
import com.zetaplugins.essentialz.storage.model.PlayerData;
import com.zetaplugins.essentialz.util.EszConfig;
import com.zetaplugins.zetacore.annotations.InjectManager;
import com.zetaplugins.zetacore.annotations.PostManagerConstruct;
import com.zetaplugins.zetacore.services.config.ConfigService;
import org.bukkit.OfflinePlayer;

import java.util.Map;
import java.util.UUID;

public class BuiltinEconomyManager implements EconomyManager {
    private CurrencyFormatConfigSection currencyConfig;

    @InjectManager
    private ConfigService configService;

    @InjectManager
    private Storage storage;

    @InjectManager
    private EssentialZ plugin;

    @PostManagerConstruct
    public void init() {
        currencyConfig = configService.getConfig(EconomyConfig.class).getCurrencyFormat();
    }

    @Override
    public double getBalance(OfflinePlayer player) {
        PlayerData playerData = storage.getPlayerRepository().load(player.getUniqueId());
        if (playerData == null) return 0.0;
        else return playerData.getBalance();
    }

    @Override
    public void deposit(OfflinePlayer player, double amount) {
        if (amount < 0) amount = 0;
        PlayerData playerData = storage.getPlayerRepository().load(player.getUniqueId());
        if (playerData == null) playerData = new PlayerData(player.getUniqueId());
        double currentBalance = playerData.getBalance();
        playerData.setBalance(currentBalance + amount);
        storage.getPlayerRepository().save(playerData);
    }

    @Override
    public boolean withdraw(OfflinePlayer player, double amount) {
        if (amount < 0) amount = 0;
        PlayerData playerData = storage.getPlayerRepository().load(player.getUniqueId());
        if (playerData == null) playerData = new PlayerData(player.getUniqueId());
        double currentBalance = playerData.getBalance();
        if (currentBalance < amount) return false;
        playerData.setBalance(currentBalance - amount);
        storage.getPlayerRepository().save(playerData);
        return true;
    }

    @Override
    public void setBalance(OfflinePlayer player, double amount) {
        if (amount < 0) amount = 0;
        PlayerData playerData = storage.getPlayerRepository().load(player.getUniqueId());
        if (playerData == null) playerData = new PlayerData(player.getUniqueId());
        playerData.setBalance(amount);
        storage.getPlayerRepository().save(playerData);
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

    @Override
    public Map<UUID, Double> getTopBalances(int topN) {
        return storage.getPlayerRepository().getTopBalances(topN);
    }
}
