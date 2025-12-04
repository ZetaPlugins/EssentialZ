package com.zetaplugins.essentialz.features.economy.manager;

import com.zetaplugins.zetacore.annotations.Manager;
import org.bukkit.OfflinePlayer;

import java.util.Map;
import java.util.UUID;

/**
 * Interface for managing player economy.
 */
@Manager
public interface EconomyManager {
    /**
     * Get the balance of a player.
     * @param player The player to get the balance of.
     * @return The balance of the player.
     */
    double getBalance(OfflinePlayer player);

    /**
     * Deposit an amount to a player's balance.
     * @param player The player to deposit to.
     * @param amount The amount to deposit.
     */
    void deposit(OfflinePlayer player, double amount);

    /**
     * Withdraw an amount from a player's balance.
     * @param player The player to withdraw from.
     * @param amount The amount to withdraw.
     * @return True if the withdrawal was successful, false otherwise.
     */
    boolean withdraw(OfflinePlayer player, double amount);

    /**
     * Set a player's balance to a specific amount.
     * @param player The player to set the balance for.
     * @param amount The amount to set the balance to.
     */
    void setBalance(OfflinePlayer player, double amount);

    /**
     * Format a monetary amount according to the economy's formatting rules.
     * @param amount The amount to format.
     * @return The formatted amount as a string.
     */
    String format(double amount);

    /**
     * Get the top N players with the highest balances.
     * @param topN The number of top players to retrieve.
     * @return A map of player UUIDs to their balances sorted in descending order.
     */
    Map<UUID, Double> getTopBalances(int topN);
}
