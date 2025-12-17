package com.zetaplugins.essentialz.commands.items;

import com.zetaplugins.essentialz.EssentialZ;
import com.zetaplugins.essentialz.config.main.MainConfig;
import com.zetaplugins.essentialz.inventory.holders.UnmodifiableEnderchestInventoryHolder;
import com.zetaplugins.essentialz.util.PluginMessage;
import com.zetaplugins.essentialz.util.commands.EszCommand;
import com.zetaplugins.essentialz.util.permissions.Permission;
import com.zetaplugins.zetacore.annotations.AutoRegisterCommand;
import com.zetaplugins.zetacore.annotations.InjectManager;
import com.zetaplugins.zetacore.commands.ArgumentList;
import com.zetaplugins.zetacore.commands.exceptions.CommandException;
import com.zetaplugins.zetacore.commands.exceptions.CommandPermissionException;
import com.zetaplugins.zetacore.commands.exceptions.CommandSenderMustBePlayerException;
import com.zetaplugins.zetacore.services.config.ConfigService;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

import java.util.List;

@AutoRegisterCommand(
        commands = "enderchest",
        permission = "essentialz.enderchest",
        description = "Opens your enderchest or another player's enderchest.",
        usage = "/enderchest [player]",
        aliases = {"ec"}
)
public class EnderchestCommand extends EszCommand {

    @InjectManager
    private ConfigService configService;

    public EnderchestCommand(EssentialZ plugin) {
        super(plugin);
    }

    @Override
    public boolean execute(CommandSender commandSender, Command command, String s, ArgumentList args) throws CommandException {
        if (!(commandSender instanceof Player player)) throw new CommandSenderMustBePlayerException();

        boolean shouldPlaySound = configService.getConfig(MainConfig.class).isEnderchestSound();

        if (args.size() == 0) { // Simplest case: open own enderchest (no permission check needed here as it's handled by the command registration)
            if (shouldPlaySound) player.playSound(player.getLocation(), Sound.BLOCK_ENDER_CHEST_OPEN, 1.0f, 1.0f);
            player.openInventory(player.getEnderChest());
            return true;
        }

        // Open another player's enderchest
        if (!Permission.ENDERCHEST_OTHERS.has(commandSender)) {
            throw new CommandPermissionException(Permission.ENDERCHEST_OTHERS.getNode());
        }

        Player targetPlayer = args.getPlayer(0, getPlugin());
        if (targetPlayer == null) {
            player.sendMessage(getMessageManager().getAndFormatMsg(PluginMessage.PLAYER_NOT_FOUND));
            return true;
        }


        if (Permission.ENDERCHEST_OTHERS_MODIFY.has(player)) {
            if (shouldPlaySound) player.playSound(player.getLocation(), Sound.BLOCK_ENDER_CHEST_OPEN, 1.0f, 1.0f);
            player.openInventory(targetPlayer.getEnderChest());
        } else {
            Inventory inv = Bukkit.createInventory(
                    new UnmodifiableEnderchestInventoryHolder(player.getUniqueId()),
                    InventoryType.ENDER_CHEST
            );

            inv.setContents(targetPlayer.getEnderChest().getContents());
            if (shouldPlaySound) player.playSound(player.getLocation(), Sound.BLOCK_ENDER_CHEST_OPEN, 1.0f, 1.0f);
            player.openInventory(inv);
        }
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender commandSender, Command command, ArgumentList args) {
        if (args.getCurrentArgIndex() == 0 && Permission.ENDERCHEST_OTHERS.has(commandSender)) {
            return getPlayerOptions(args.getCurrentArg());
        }
        return List.of();
    }
}
