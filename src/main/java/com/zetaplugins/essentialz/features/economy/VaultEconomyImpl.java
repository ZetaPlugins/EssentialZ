package com.zetaplugins.essentialz.features.economy;

import com.zetaplugins.essentialz.storage.Storage;
import com.zetaplugins.essentialz.storage.model.PlayerData;
import com.zetaplugins.essentialz.util.EszConfig;
import com.zetaplugins.zetacore.annotations.InjectManager;
import com.zetaplugins.zetacore.annotations.PostManagerConstruct;
import com.zetaplugins.zetacore.services.config.ConfigService;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.List;

public class VaultEconomyImpl implements Economy {
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
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getName() {
        return "EssentialZ";
    }

    @Override
    public boolean hasBankSupport() {
        return false;
    }

    @Override
    public int fractionalDigits() {
        return -1;
    }

    @Override
    public String format(double v) {
        return EconomyUtil.formatCurrency(v, currencyConfig);
    }

    @Override
    public String currencyNamePlural() {
        return currencyConfig.getSymbol();
    }

    @Override
    public String currencyNameSingular() {
        return currencyConfig.getSymbol();
    }

    @Override
    public boolean hasAccount(String s) {
        return hasAccount(Bukkit.getOfflinePlayer(s));
    }

    @Override
    public boolean hasAccount(OfflinePlayer offlinePlayer) {
        return storage.getPlayerRepository().load(offlinePlayer.getUniqueId()) != null;
    }

    /**
     * @param s
     * @param s1
     * @deprecated
     */
    @Override
    public boolean hasAccount(String s, String s1) {
        return hasAccount(s);
    }

    @Override
    public boolean hasAccount(OfflinePlayer offlinePlayer, String s) {
        return hasAccount(offlinePlayer);
    }

    /**
     * @param s
     * @deprecated
     */
    @Override
    public double getBalance(String s) {
        return getBalance(Bukkit.getOfflinePlayer(s));
    }

    @Override
    public double getBalance(OfflinePlayer offlinePlayer) {
        PlayerData playerData = storage.getPlayerRepository().load(offlinePlayer.getUniqueId());
        if (playerData == null) return 0;
        else return playerData.getBalance();
    }

    /**
     * @param s
     * @param s1
     * @deprecated
     */
    @Override
    public double getBalance(String s, String s1) {
        return getBalance(Bukkit.getOfflinePlayer(s));
    }

    @Override
    public double getBalance(OfflinePlayer offlinePlayer, String s) {
        return getBalance(offlinePlayer);
    }

    /**
     * @param s
     * @param v
     * @deprecated
     */
    @Override
    public boolean has(String s, double v) {
        return has(Bukkit.getOfflinePlayer(s), v);
    }

    @Override
    public boolean has(OfflinePlayer offlinePlayer, double v) {
        if (v < 0) throw new IllegalArgumentException("Amount cannot be negative");
        return getBalance(offlinePlayer) >= v;
    }

    /**
     * @param s
     * @param s1
     * @param v
     * @deprecated
     */
    @Override
    public boolean has(String s, String s1, double v) {
        return has(Bukkit.getOfflinePlayer(s), s1, v);
    }

    @Override
    public boolean has(OfflinePlayer offlinePlayer, String s, double v) {
        return has(offlinePlayer, v);
    }

    /**
     * @param s
     * @param v
     * @deprecated
     */
    @Override
    public EconomyResponse withdrawPlayer(String s, double v) {
        return withdrawPlayer(Bukkit.getOfflinePlayer(s), v);
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, double v) {
        if (v < 0) throw new IllegalArgumentException("Amount cannot be negative");
        PlayerData playerData = storage.getPlayerRepository().load(offlinePlayer.getUniqueId());
        if (playerData == null) {
            return new EconomyResponse(
                    0,
                    0,
                    EconomyResponse.ResponseType.FAILURE,
                    "Account does not exist"
            );
        }
        if (playerData.getBalance() < v) {
            return new EconomyResponse(
                    0,
                    playerData.getBalance(),
                    EconomyResponse.ResponseType.FAILURE,
                    "Insufficient funds"
            );
        }

        playerData.setBalance(playerData.getBalance() - v);
        storage.getPlayerRepository().save(playerData);
        return new EconomyResponse(
                v,
                playerData.getBalance(),
                EconomyResponse.ResponseType.SUCCESS,
                null
        );
    }

    /**
     * @param s
     * @param s1
     * @param v
     * @deprecated
     */
    @Override
    public EconomyResponse withdrawPlayer(String s, String s1, double v) {
        return withdrawPlayer(Bukkit.getOfflinePlayer(s), s1, v);
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, String s, double v) {
        return withdrawPlayer(offlinePlayer, v);
    }

    /**
     * @param s
     * @param v
     * @deprecated
     */
    @Override
    public EconomyResponse depositPlayer(String s, double v) {
        return depositPlayer(Bukkit.getOfflinePlayer(s), v);
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, double v) {
        if (v < 0) throw new IllegalArgumentException("Amount cannot be negative");
        PlayerData playerData = storage.getPlayerRepository().load(offlinePlayer.getUniqueId());
        if (playerData == null) {
            return new EconomyResponse(
                    0,
                    0,
                    EconomyResponse.ResponseType.FAILURE,
                    "Account does not exist"
            );
        }
        playerData.setBalance(playerData.getBalance() + v);
        storage.getPlayerRepository().save(playerData);
        return new EconomyResponse(
                v,
                playerData.getBalance(),
                EconomyResponse.ResponseType.SUCCESS,
                null
        );
    }

    /**
     * @param s
     * @param s1
     * @param v
     * @deprecated
     */
    @Override
    public EconomyResponse depositPlayer(String s, String s1, double v) {
        return depositPlayer(Bukkit.getOfflinePlayer(s), s1, v);
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, String s, double v) {
        return depositPlayer(offlinePlayer, v);
    }

    /**
     * @param s
     * @param s1
     * @deprecated
     */
    @Override
    public EconomyResponse createBank(String s, String s1) {
        throw new UnsupportedOperationException("Bank support is not implemented");
    }

    @Override
    public EconomyResponse createBank(String s, OfflinePlayer offlinePlayer) {
        throw new UnsupportedOperationException("Bank support is not implemented");
    }

    @Override
    public EconomyResponse deleteBank(String s) {
        throw new UnsupportedOperationException("Bank support is not implemented");
    }

    @Override
    public EconomyResponse bankBalance(String s) {
        throw new UnsupportedOperationException("Bank support is not implemented");
    }

    @Override
    public EconomyResponse bankHas(String s, double v) {
        throw new UnsupportedOperationException("Bank support is not implemented");
    }

    @Override
    public EconomyResponse bankWithdraw(String s, double v) {
        throw new UnsupportedOperationException("Bank support is not implemented");
    }

    @Override
    public EconomyResponse bankDeposit(String s, double v) {
        throw new UnsupportedOperationException("Bank support is not implemented");
    }

    /**
     * @param s
     * @param s1
     * @deprecated
     */
    @Override
    public EconomyResponse isBankOwner(String s, String s1) {
        throw new UnsupportedOperationException("Bank support is not implemented");
    }

    @Override
    public EconomyResponse isBankOwner(String s, OfflinePlayer offlinePlayer) {
        throw new UnsupportedOperationException("Bank support is not implemented");
    }

    /**
     * @param s
     * @param s1
     * @deprecated
     */
    @Override
    public EconomyResponse isBankMember(String s, String s1) {
        throw new UnsupportedOperationException("Bank support is not implemented");
    }

    @Override
    public EconomyResponse isBankMember(String s, OfflinePlayer offlinePlayer) {
        throw new UnsupportedOperationException("Bank support is not implemented");
    }

    @Override
    public List<String> getBanks() {
        throw new UnsupportedOperationException("Bank support is not implemented");
    }

    /**
     * @param s
     * @deprecated
     */
    @Override
    public boolean createPlayerAccount(String s) {
        throw new UnsupportedOperationException("User accounts are created automatically");
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer offlinePlayer) {
        throw new UnsupportedOperationException("User accounts are created automatically");
    }

    /**
     * @param s
     * @param s1
     * @deprecated
     */
    @Override
    public boolean createPlayerAccount(String s, String s1) {
        throw new UnsupportedOperationException("User accounts are created automatically");
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer offlinePlayer, String s) {
        throw new UnsupportedOperationException("User accounts are created automatically");
    }
}
