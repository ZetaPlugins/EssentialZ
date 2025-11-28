package com.zetaplugins.essentialz.features.economy;

import org.bukkit.configuration.file.FileConfiguration;

public class EconomyConfig {
    private final String symbol;
    private final int decimalPlaces;
    private final String thousandSeparator;
    private final String decimalSeparator;
    private final String symbolPosition;
    private final double startingBalance;

    public EconomyConfig(String symbol, int decimalPlaces, String thousandSeparator, String decimalSeparator, String symbolPosition, double startingBalance) {
        this.symbol = symbol;
        this.decimalPlaces = decimalPlaces;
        this.thousandSeparator = thousandSeparator;
        this.decimalSeparator = decimalSeparator;
        this.symbolPosition = symbolPosition;
        this.startingBalance = startingBalance;
    }

    public EconomyConfig(FileConfiguration config) {
        this.symbol = config.getString("currencyFormat.symbol", "$");
        this.decimalPlaces = config.getInt("currencyFormat.decimalPlaces", 2);
        this.thousandSeparator = config.getString("currencyFormat.thousandSeparator", ",");
        this.decimalSeparator = config.getString("currencyFormat.decimalSeparator", ".");
        this.symbolPosition = config.getString("currencyFormat.symbolPosition", "before");
        this.startingBalance = config.getDouble("startingBalance", 1000.0);
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
}
