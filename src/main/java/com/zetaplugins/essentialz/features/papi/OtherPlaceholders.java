package com.zetaplugins.essentialz.features.papi;

import com.zetaplugins.essentialz.features.GodModeManager;
import com.zetaplugins.zetacore.annotations.InjectManager;
import com.zetaplugins.zetacore.annotations.Manager;
import com.zetaplugins.zetacore.annotations.Papi;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

@Manager
public class OtherPlaceholders {
    @InjectManager
    private GodModeManager godModeManager;

    @Papi(identifier = "godmode")
    public String godModePlaceholder(OfflinePlayer player) {
        boolean isInGodMode = godModeManager.isInGodMode(player.getUniqueId());
        return Boolean.toString(isInGodMode);
    }

    @Papi(identifier = "fly")
    public String flyPlaceholder(Player player) {
        boolean canFly = player.getAllowFlight();
        return Boolean.toString(canFly);
    }
}
