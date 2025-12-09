package com.zetaplugins.essentialz.config.economy;

import com.zetaplugins.zetacore.annotations.NestedConfig;

@NestedConfig
public class CurrencyFormatConfigSection {
    private String symbol = "$";
    private int decimalPlaces = 2;
    private String thousandSeparator = ",";
    private String decimalSeparator = ".";
    private String symbolPosition = "before";

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
}
