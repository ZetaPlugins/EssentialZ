package com.zetaplugins.essentialz.commands.items;

import com.zetaplugins.essentialz.EssentialZ;
import com.zetaplugins.essentialz.util.MessageManager;
import com.zetaplugins.essentialz.util.MessageStyle;
import com.zetaplugins.essentialz.util.PluginMessage;
import com.zetaplugins.essentialz.util.commands.EszCommand;
import com.zetaplugins.zetacore.annotations.AutoRegisterCommand;
import com.zetaplugins.zetacore.commands.ArgumentList;
import com.zetaplugins.zetacore.commands.exceptions.CommandSenderMustBePlayerException;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@AutoRegisterCommand(
        commands = "more",
        description = "Gives you more of the item you are holding.",
        usage = "/<command>",
        permission = "essentialz.more"
)
public class MoreCommand extends EszCommand {

    public MoreCommand(EssentialZ plugin) {
        super(plugin);
    }

    @Override
    public boolean execute(CommandSender sender, Command command, String label, ArgumentList args) throws CommandSenderMustBePlayerException {
        if (!(sender instanceof Player player)) throw new CommandSenderMustBePlayerException();

        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getType().isAir()) {
            sender.sendMessage(getMessageManager().getAndFormatMsg(PluginMessage.MUST_HOLD_AN_ITEM));
            return false;
        }

        if (item.getType().getMaxStackSize() == 1) {
            sender.sendMessage(getMessageManager().getAndFormatMsg(PluginMessage.ITEM_CANNOT_BE_STACKED));
            return false;
        }

        item.setAmount(item.getType().getMaxStackSize());
        player.getInventory().setItemInMainHand(item);

        sender.sendMessage(getMessageManager().getAndFormatMsg(
                PluginMessage.STACKED_ITEMS,
                new MessageManager.Replaceable<>("{amount}", String.valueOf(item.getAmount()))
        ));
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, Command command, ArgumentList args) {
        return List.of();
    }
}
