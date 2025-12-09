package com.zetaplugins.essentialz.config.economy;

import com.zetaplugins.zetacore.annotations.NestedConfig;

import java.util.Map;

@NestedConfig
public class CommandFeesConfigSection {
    private boolean enabled = false;
    private Map<String, Double> fees = Map.of();

    public boolean isEnabled() {
        return enabled;
    }

    public Map<String, Double> getFees() {
        return fees;
    }
}
