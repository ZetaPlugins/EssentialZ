package com.zetaplugins.essentialz.commands.items;

import com.zetaplugins.zetacore.annotations.AutoRegisterCommand;
import com.zetaplugins.zetacore.commands.ArgumentList;
import com.zetaplugins.zetacore.commands.exceptions.CommandSenderMustBePlayerException;
import com.zetaplugins.zetacore.commands.exceptions.CommandUsageException;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import com.zetaplugins.essentialz.EssentialZ;
import com.zetaplugins.essentialz.util.MessageManager;
import com.zetaplugins.essentialz.util.commands.EszCommand;

import java.util.List;

@AutoRegisterCommand(
        commands = "i",
        description = "Gives yourself an item.",
        usage = "/i <item> [amount]",
        permission = "essentialz.give"
)
public class ICommand extends EszCommand {

    public ICommand(EssentialZ plugin) {
        super(plugin);
    }

    @Override
    public boolean execute(CommandSender sender, Command command, String label, ArgumentList args) throws CommandSenderMustBePlayerException, CommandUsageException {
        if (!(sender instanceof Player player)) throw new CommandSenderMustBePlayerException();

        String materialName = args.getArg(0);
        if (materialName == null) throw new CommandUsageException("/give [player] <item> [amount]");
        Material material = getPlugin().getGiveMaterialManager().getMaterialByKey(materialName);
        if (material == null) {
            sender.sendMessage(getPlugin().getMessageManager().getAndFormatMsg(
                    MessageManager.Style.ERROR,
                    "invalidMaterial",
                    "{ac}'{material}' is not a valid material.",
                    new MessageManager.Replaceable<>("{material}", materialName)
            ));
            return false;
        }

        int amount = args.getInt(1, 1);

        ItemStack itemStack = new ItemStack(material, amount);
        player.getInventory().addItem(itemStack);

        sender.sendMessage(getPlugin().getMessageManager().getAndFormatMsg(
                MessageManager.Style.ITEMS,
                "giveYourselfConfirmation",
                "&7You have been given {ac}{amount}x {material}&7.",
                new MessageManager.Replaceable<>("{amount}", String.valueOf(amount)),
                new MessageManager.Replaceable<>("{material}", material.name())
        ));

        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, Command command, ArgumentList args) {
        if (args.getCurrentArgIndex() == 0) {
            return getDisplayOptions(getPlugin().getGiveMaterialManager().getPossibleMaterials(), args.getCurrentArg());
        } else if (args.getCurrentArgIndex() == 1) {
            return getDisplayOptions(List.of("1", "16", "32", "64"), args.getCurrentArg());
        }
        return List.of();
    }
}
