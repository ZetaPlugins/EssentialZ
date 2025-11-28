package com.zetaplugins.essentialz.features.economy.manager;

import org.bukkit.OfflinePlayer;

public class UnusedEconomyManager implements EconomyManager {
    /**
     * Get the balance of a player.
     *
     * @param player The player to get the balance of.
     * @return The balance of the player.
     */
    @Override
    public double getBalance(OfflinePlayer player) {
        throw new UnsupportedOperationException("Economy system is disabled.");
    }

    /**
     * Deposit an amount to a player's balance.
     *
     * @param player The player to deposit to.
     * @param amount The amount to deposit.
     */
    @Override
    public void deposit(OfflinePlayer player, double amount) {
        throw new UnsupportedOperationException("Economy system is disabled.");
    }

    /**
     * Withdraw an amount from a player's balance.
     *
     * @param player The player to withdraw from.
     * @param amount The amount to withdraw.
     * @return True if the withdrawal was successful, false otherwise.
     */
    @Override
    public boolean withdraw(OfflinePlayer player, double amount) {
        throw new UnsupportedOperationException("Economy system is disabled.");
    }

    /**
     * Set a player's balance to a specific amount.
     *
     * @param player The player to set the balance for.
     * @param amount The amount to set the balance to.
     */
    @Override
    public void setBalance(OfflinePlayer player, double amount) {
        throw new UnsupportedOperationException("Economy system is disabled.");
    }

    /**
     * Format a monetary amount according to the economy's formatting rules.
     *
     * @param amount The amount to format.
     * @return The formatted amount as a string.
     */
    @Override
    public String format(double amount) {
        throw new UnsupportedOperationException("Economy system is disabled.");
    }
}
