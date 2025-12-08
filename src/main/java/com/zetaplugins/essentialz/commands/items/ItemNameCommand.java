package com.zetaplugins.essentialz.commands.items;

import com.zetaplugins.essentialz.EssentialZ;
import com.zetaplugins.essentialz.config.main.MainConfig;
import com.zetaplugins.essentialz.util.MessageManager;
import com.zetaplugins.essentialz.util.MessageStyle;
import com.zetaplugins.essentialz.util.commands.EszCommand;
import com.zetaplugins.zetacore.annotations.AutoRegisterCommand;
import com.zetaplugins.zetacore.annotations.InjectManager;
import com.zetaplugins.zetacore.commands.ArgumentList;
import com.zetaplugins.zetacore.commands.exceptions.CommandSenderMustBePlayerException;
import com.zetaplugins.zetacore.services.config.ConfigService;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

@AutoRegisterCommand(
        commands = "itemname",
        description = "Set the name of the item you are holding.",
        usage = "/itemname <name>",
        permission = "essentialz.itemname"
)
public class ItemNameCommand extends EszCommand {

    @InjectManager
    private ConfigService configService;

    public ItemNameCommand(EssentialZ plugin) {
        super(plugin);
    }

    @Override
    public boolean execute(CommandSender sender, Command command, String label, ArgumentList args) throws CommandSenderMustBePlayerException {
        if (!(sender instanceof Player player)) throw new CommandSenderMustBePlayerException();

        ItemStack item = player.getInventory().getItemInMainHand();

        if (item.getType() == Material.AIR) {
            sender.sendMessage(getMessageManager().getAndFormatMsg(
                    MessageStyle.ERROR,
                    "mustHoldAnItem",
                    "{ac}You must be holding an item."
            ));
            return false;
        }

        StringBuilder newName = new StringBuilder();
        for (String arg : args) {
            newName.append(arg);
            if (!arg.equals(args.getArg(args.size() - 1))) {
                newName.append(" ");
            }
        }

        int maxLength = configService.getConfig(MainConfig.class).getMaxItemNameLength();

        if (newName.length() > maxLength && maxLength != -1) {
            sender.sendMessage(getMessageManager().getAndFormatMsg(
                    MessageStyle.ERROR,
                    "itemNameTooLong",
                    "{ac}The item name is too long! The maximum length is {max} characters.",
                    new MessageManager.Replaceable<>("{max}", String.valueOf(maxLength))
            ));
            return false;
        }

        ItemMeta newMeta = item.getItemMeta();
        newMeta.displayName(getMessageManager().formatMsg(newName.toString()));
        item.setItemMeta(newMeta);

        sender.sendMessage(getMessageManager().getAndFormatMsg(
                MessageStyle.ITEMS,
                "itemNameSet",
                "&7Item name set to '{name}&r&7'.",
                new MessageManager.Replaceable<>("{name}", newName.toString())
        ));

        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, Command command, ArgumentList args) {
        return List.of();
    }
}
