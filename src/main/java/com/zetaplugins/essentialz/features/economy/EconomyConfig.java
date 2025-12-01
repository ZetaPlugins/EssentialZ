package com.zetaplugins.essentialz.features.economy;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class EconomyConfig {
    private final String symbol;
    private final int decimalPlaces;
    private final String thousandSeparator;
    private final String decimalSeparator;
    private final String symbolPosition;
    private final double startingBalance;
    private final boolean commandFeesEnabled;
    private final Map<String, Double> commandFees;

    public EconomyConfig(String symbol, int decimalPlaces, String thousandSeparator, String decimalSeparator, String symbolPosition, double startingBalance, boolean commandFeesEnabled, Map<String, Double> commandFees) {
        this.symbol = symbol;
        this.decimalPlaces = decimalPlaces;
        this.thousandSeparator = thousandSeparator;
        this.decimalSeparator = decimalSeparator;
        this.symbolPosition = symbolPosition;
        this.startingBalance = startingBalance;
        this.commandFeesEnabled = commandFeesEnabled;
        this.commandFees = commandFees;
    }

    public EconomyConfig(FileConfiguration config) {
        this.symbol = config.getString("currencyFormat.symbol", "$");
        this.decimalPlaces = config.getInt("currencyFormat.decimalPlaces", 2);
        this.thousandSeparator = config.getString("currencyFormat.thousandSeparator", ",");
        this.decimalSeparator = config.getString("currencyFormat.decimalSeparator", ".");
        this.symbolPosition = config.getString("currencyFormat.symbolPosition", "before");
        this.startingBalance = config.getDouble("startingBalance", 1000.0);
        this.commandFeesEnabled = config.getBoolean("commandFees.enabled", false);
        Set<String> keys = config.getConfigurationSection("commandFees.fees") != null ?
                config.getConfigurationSection("commandFees.fees").getKeys(false) : Set.of();
        this.commandFees = keys.stream().collect(Collectors.toMap(
                key -> key,
                key -> config.getDouble("commandFees.fees." + key, 0.0)
        ));
    }

    public String getSymbol() {
        return symbol;
    }

    public int getDecimalPlaces() {
        return decimalPlaces;
    }

    public String getThousandSeparator() {
        return thousandSeparator;
    }

    public String getDecimalSeparator() {
        return decimalSeparator;
    }

    public String getSymbolPosition() {
        return symbolPosition;
    }

    public double getStartingBalance() {
        return startingBalance;
    }

    public boolean isCommandFeesEnabled() {
        return commandFeesEnabled;
    }

    public Map<String, Double> getCommandFees() {
        return commandFees;
    }

    public double getCommandFee(String command) {
        if (!commandFeesEnabled) return 0.0;
        return commandFees.getOrDefault(command, 0.0);
    }
}
