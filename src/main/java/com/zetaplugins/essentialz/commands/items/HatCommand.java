package com.zetaplugins.essentialz.commands.items;

import com.zetaplugins.essentialz.EssentialZ;
import com.zetaplugins.essentialz.util.MessageStyle;
import com.zetaplugins.essentialz.util.commands.EszCommand;
import com.zetaplugins.zetacore.annotations.AutoRegisterCommand;
import com.zetaplugins.zetacore.commands.ArgumentList;
import com.zetaplugins.zetacore.commands.exceptions.CommandException;
import com.zetaplugins.zetacore.commands.exceptions.CommandSenderMustBePlayerException;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@AutoRegisterCommand(
        commands = "hat",
        description = "Wear the item in your hand as a hat.",
        usage = "/hat",
        permission = "essentialz.hat"
)
public class HatCommand extends EszCommand {

    public HatCommand(EssentialZ plugin) {
        super(plugin);
    }

    @Override
    public boolean execute(CommandSender sender, Command command, String label, ArgumentList args) throws CommandException {
        if (!(sender instanceof Player player)) throw new CommandSenderMustBePlayerException();

        ItemStack item = player.getInventory().getItemInMainHand();

        if (item.isEmpty() || item.getType().isAir()) {
            sender.sendMessage(getMessageManager().getAndFormatMsg(
                    MessageStyle.ERROR,
                    "mustHoldAnItem",
                    "{ac}You must be holding an item."
            ));
            return true;
        }

        ItemStack helmet = player.getInventory().getHelmet();
        player.getInventory().setHelmet(item);
        player.getInventory().setItemInMainHand(helmet);

        sender.sendMessage(getMessageManager().getAndFormatMsg(
                MessageStyle.ITEMS,
                "hatSet",
                "&7You are now wearing your held item as a hat."
        ));
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, Command command, ArgumentList args) {
        return List.of();
    }
}
