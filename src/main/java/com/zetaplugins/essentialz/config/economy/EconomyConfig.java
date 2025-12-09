package com.zetaplugins.essentialz.config.economy;

import com.zetaplugins.zetacore.annotations.PluginConfig;

@PluginConfig("economy.yml")
public class EconomyConfig {
    private boolean enabled = true;
    private double startingBalance = 0.0;
    private CurrencyFormatConfigSection currencyFormat = new CurrencyFormatConfigSection();
    private CommandFeesConfigSection commandFees = new CommandFeesConfigSection();

    public boolean isEnabled() {
        return enabled;
    }

    public double getStartingBalance() {
        return startingBalance;
    }

    public CurrencyFormatConfigSection getCurrencyFormat() {
        return currencyFormat;
    }

    public CommandFeesConfigSection getCommandFees() {
        return commandFees;
    }
}
