package com.zetaplugins.essentialz.commands.items;

import com.zetaplugins.essentialz.EssentialZ;
import com.zetaplugins.essentialz.config.main.MainConfig;
import com.zetaplugins.essentialz.inventory.holders.UnmodifiablePlayerInventoryHolder;
import com.zetaplugins.essentialz.util.PluginMessage;
import com.zetaplugins.essentialz.util.commands.EszCommand;
import com.zetaplugins.essentialz.util.permissions.Permission;
import com.zetaplugins.zetacore.annotations.AutoRegisterCommand;
import com.zetaplugins.zetacore.annotations.InjectManager;
import com.zetaplugins.zetacore.commands.ArgumentList;
import com.zetaplugins.zetacore.commands.exceptions.CommandException;
import com.zetaplugins.zetacore.commands.exceptions.CommandSenderMustBePlayerException;
import com.zetaplugins.zetacore.commands.exceptions.CommandUsageException;
import com.zetaplugins.zetacore.services.config.ConfigService;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

@AutoRegisterCommand(
        commands = "invsee",
        permission = "essentialz.invsee",
        description = "View or modify another player's inventory.",
        usage = "/invsee <player>"
)
public class InvseeCommand extends EszCommand {

    @InjectManager
    private ConfigService configService;

    public InvseeCommand(EssentialZ plugin) {
        super(plugin);
    }

    @Override
    public boolean execute(CommandSender commandSender, Command command, String s, ArgumentList args)
            throws CommandException {

        if (!(commandSender instanceof Player player)) {
            throw new CommandSenderMustBePlayerException();
        }

        if (args.size() == 0) {
            throw new CommandUsageException("Usage: /invsee <player>");
        }

        boolean shouldPlaySound = configService.getConfig(MainConfig.class).isInvseeSound();

        Player targetPlayer = args.getPlayer(0, getPlugin());
        if (targetPlayer == null) {
            player.sendMessage(getMessageManager().getAndFormatMsg(PluginMessage.PLAYER_NOT_FOUND));
            return true;
        }

        if (targetPlayer.getUniqueId() == player.getUniqueId()) {
            player.sendMessage(getMessageManager().getAndFormatMsg(PluginMessage.CANNOT_INVSEE_SELF));
            return true;
        }

        if (Permission.INVSEE_MODIFY.has(player)) {
            if (shouldPlaySound) player.playSound(player.getLocation(), Sound.BLOCK_CHEST_OPEN, 1.0f, 1.0f);

            player.openInventory(targetPlayer.getInventory());
        } else {
            Inventory inv = Bukkit.createInventory(
                    new UnmodifiablePlayerInventoryHolder(
                            player.getUniqueId(),
                            targetPlayer.getUniqueId()
                    ),
                    36,
                    getMessageManager().formatMsg(targetPlayer.getName() + "'s Inventory")
            );

            ItemStack[] mainContents = Arrays.copyOf(targetPlayer.getInventory().getContents(), 36);
            inv.setContents(mainContents);

            if (shouldPlaySound) player.playSound(player.getLocation(), Sound.BLOCK_CHEST_OPEN, 1.0f, 1.0f);

            player.openInventory(inv);
        }

        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender commandSender, Command command, ArgumentList args) {
        if (args.getCurrentArgIndex() == 0 && Permission.INVSEE.has(commandSender)) {
            return getPlayerOptions(args.getCurrentArg());
        }
        return List.of();
    }
}