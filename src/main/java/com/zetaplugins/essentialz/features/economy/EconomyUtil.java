package com.zetaplugins.essentialz.features.economy;

import com.zetaplugins.essentialz.config.economy.CurrencyFormatConfigSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Map;

public class EconomyUtil {
    private static final Map<String, Double> SUFFIX_MULTIPLIERS = Map.of(
            "K", 1_000.0,
            "M", 1_000_000.0,
            "B", 1_000_000_000.0,
            "T", 1_000_000_000_000.0,
            "Q", 1_000_000_000_000_000.0
    );

    private EconomyUtil() {}

    public static String formatCurrency(double amount, CurrencyFormatConfigSection config) {
        return formatCurrency(amount, config.getSymbol(), config.getDecimalPlaces(), config.getThousandSeparator(), config.getDecimalSeparator(), config.getSymbolPosition());
    }

    public static String formatCurrency(double amount, FileConfiguration config) {
        String symbol = config.getString("currencyFormat.symbol", "$");
        int decimalPlaces = config.getInt("currencyFormat.decimalPlaces", 2);
        String thousandSeparator = config.getString("currencyFormat.thousandSeparator", ",");
        String decimalSeparator = config.getString("currencyFormat.decimalSeparator", ".");
        String symbolPosition = config.getString("currencyFormat.symbolPosition", "before");
        return formatCurrency(amount, symbol, decimalPlaces, thousandSeparator, decimalSeparator, symbolPosition);
    }

    public static String formatCurrency(double amount, String symbol, int decimalPlaces, String thousandSeparator, String decimalSeparator, String symbolPosition) {
        String formatString = "%,." + decimalPlaces + "f";
        String formattedAmount = String.format(formatString, amount);

        if (!thousandSeparator.equals(",")) {
            formattedAmount = formattedAmount.replace(",", "TEMP_THOUSAND_SEP");
        }
        if (!decimalSeparator.equals(".")) {
            formattedAmount = formattedAmount.replace(".", decimalSeparator);
        }
        if (!thousandSeparator.equals(",")) {
            formattedAmount = formattedAmount.replace("TEMP_THOUSAND_SEP", thousandSeparator);
        }

        if (symbolPosition.equalsIgnoreCase("before")) {
            return symbol + formattedAmount;
        } else {
            return formattedAmount + symbol;
        }
    }

    /**
     * Parses a string representing a number with optional suffixes (K, M, B) into a double.
     * @param input  The input string to parse.
     * @param config The currency configuration for symbol and separators.
     * @return The parsed double value.
     * @throws NumberFormatException If the input cannot be parsed into a valid number.
     */
    public static double parseNumber(String input, CurrencyFormatConfigSection config) throws NumberFormatException {
        String sanitizedInput = input.replace(config.getSymbol(), "")
                .replace(config.getThousandSeparator(), "")
                .replace(config.getDecimalSeparator(), ".")
                .trim();

        for (var entry : SUFFIX_MULTIPLIERS.entrySet()) {
            String suffix = entry.getKey();
            Double multiplier = entry.getValue();
            if (sanitizedInput.toUpperCase().endsWith(suffix)) {
                String numberPart = sanitizedInput.substring(0, sanitizedInput.length() - suffix.length()).trim();
                return Double.parseDouble(numberPart) * multiplier;
            }
        }

        return Double.parseDouble(sanitizedInput);
    }
}
