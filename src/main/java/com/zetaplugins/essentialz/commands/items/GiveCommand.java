package com.zetaplugins.essentialz.commands.items;

import com.zetaplugins.zetacore.annotations.AutoRegisterCommand;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import com.zetaplugins.essentialz.EssentialZ;
import com.zetaplugins.essentialz.util.MessageManager;
import com.zetaplugins.essentialz.util.commands.ArgumentList;
import com.zetaplugins.essentialz.util.commands.CommandPermissionException;
import com.zetaplugins.essentialz.util.commands.CommandUsageException;
import com.zetaplugins.essentialz.util.commands.CustomCommand;

import java.util.List;

@AutoRegisterCommand(commands = "give")
public class GiveCommand extends CustomCommand {

    public GiveCommand(EssentialZ plugin) {
        super(plugin);
    }

    @Override
    public boolean execute(CommandSender sender, Command command, ArgumentList args) throws CommandPermissionException, CommandUsageException {
        // Command: /give [player] <item> [amount]
        Player targetPlayer = args.getPlayer(0, getPlugin());
        if (targetPlayer == null) throw new CommandUsageException("/give [player] <item> [amount]");

        String materialName = args.getArg(1);
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

        int amount = args.getInt(2, 1);

        ItemStack itemStack = new ItemStack(material, amount);
        targetPlayer.getInventory().addItem(itemStack);

        sender.sendMessage(getPlugin().getMessageManager().getAndFormatMsg(
                MessageManager.Style.ITEMS,
                "giveConfirmation",
                "&7Gave {ac}{amount}x {material}&7 to {ac}{player}&7.",
                new MessageManager.Replaceable<>("{amount}", String.valueOf(amount)),
                new MessageManager.Replaceable<>("{material}", material.name()),
                new MessageManager.Replaceable<>("{player}", targetPlayer.getName())
        ));

        return true;
    }

    @Override
    public boolean isAuthorized(CommandSender sender) {
        return sender.hasPermission("essentialz.give");
    }

    @Override
    public List<String> tabComplete(CommandSender sender, Command command, ArgumentList args) {
        if (args.getCurrentArgIndex() == 0) {
            return getPlayerOptions(getPlugin(), args.getCurrentArg());
        } else if (args.getCurrentArgIndex() == 1) {
            return getDisplayOptions(getPlugin().getGiveMaterialManager().getPossibleMaterials(), args.getCurrentArg());
        } else if (args.getCurrentArgIndex() == 2) {
            return getDisplayOptions(List.of("1", "16", "32", "64"), args.getCurrentArg());
        }
        return List.of();
    }
}
