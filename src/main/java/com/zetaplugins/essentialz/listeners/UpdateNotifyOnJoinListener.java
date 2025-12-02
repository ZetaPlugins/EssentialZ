package com.zetaplugins.essentialz.listeners;

import com.zetaplugins.essentialz.EssentialZ;
import com.zetaplugins.essentialz.util.MessageManager;
import com.zetaplugins.zetacore.annotations.AutoRegisterListener;
import com.zetaplugins.zetacore.annotations.InjectManager;
import com.zetaplugins.zetacore.annotations.InjectPlugin;
import com.zetaplugins.zetacore.services.updatechecker.UpdateChecker;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

@AutoRegisterListener
public class UpdateNotifyOnJoinListener implements Listener {

    @InjectManager
    private UpdateChecker updateChecker;
    @InjectManager
    private MessageManager messageManager;

    @InjectPlugin
    private EssentialZ plugin;

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (!player.isOp()
                || !plugin.getConfig().getBoolean("updateNotifications")
                || !updateChecker.isNewVersionAvailable()) {
            return;
        }

        player.sendMessage(messageManager.getAndFormatMsg(
                MessageManager.Style.DEFAULT,
                "updateAvailable",
                "&7A new version of {ac}EssentialZ &7is available! Download it from {ac}{url}&7.",
                new MessageManager.Replaceable<>("{url}", updateChecker.getLatestVersionUrl())
        ));
    }
}
