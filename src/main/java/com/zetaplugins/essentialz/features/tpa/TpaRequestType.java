package com.zetaplugins.essentialz.features.tpa;

/**
 * Represents the type of a TPA request.
 */
public enum TpaRequestType {
    TPA("tpa"),
    TPA_HERE("tpahere");

    private final String name;

    TpaRequestType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
