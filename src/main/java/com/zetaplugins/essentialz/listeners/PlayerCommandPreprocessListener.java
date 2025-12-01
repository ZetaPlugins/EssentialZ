package com.zetaplugins.essentialz.listeners;

import com.zetaplugins.essentialz.features.economy.EconomyConfig;
import com.zetaplugins.essentialz.features.economy.manager.EconomyManager;
import com.zetaplugins.essentialz.util.EszConfig;
import com.zetaplugins.essentialz.util.MessageManager;
import com.zetaplugins.zetacore.annotations.AutoRegisterListener;
import com.zetaplugins.zetacore.annotations.InjectManager;
import com.zetaplugins.zetacore.services.config.ConfigService;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

@AutoRegisterListener
public class PlayerCommandPreprocessListener implements Listener {

    @InjectManager
    private MessageManager messageManager;

    @InjectManager
    private ConfigService configService;

    @InjectManager
    private EconomyManager economyManager;

    @EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        String message = event.getMessage().toLowerCase();
        Player sender = event.getPlayer();
        EconomyConfig economyConfig = new EconomyConfig(configService.getConfig(EszConfig.ECONOMY));
        String commandName = message.contains("/") ? message.split(" ")[0].substring(1) : message;
        double cmdFee = economyConfig.getCommandFee(commandName);

        if (cmdFee <= 0) return;

        double senderBalance = economyManager.getBalance(sender);
        if (senderBalance < cmdFee) {
            sender.sendMessage(messageManager.getAndFormatMsg(
                    MessageManager.Style.ERROR,
                    "insufficientFundsCommandFee",
                    "{ac}You do not have enough funds to pay the command fee of {fee}!",
                    new MessageManager.Replaceable<>("{fee}", economyManager.format(cmdFee))
            ));
            event.setCancelled(true);
        } else {
            economyManager.withdraw(sender, cmdFee);
        }
    }
}
