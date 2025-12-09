package com.zetaplugins.essentialz.commands.items;

import com.zetaplugins.essentialz.EssentialZ;
import com.zetaplugins.essentialz.features.GiveMaterialManager;
import com.zetaplugins.essentialz.util.MessageManager;
import com.zetaplugins.essentialz.util.MessageStyle;
import com.zetaplugins.essentialz.util.PluginMessage;
import com.zetaplugins.essentialz.util.commands.EszCommand;
import com.zetaplugins.zetacore.annotations.AutoRegisterCommand;
import com.zetaplugins.zetacore.annotations.InjectManager;
import com.zetaplugins.zetacore.commands.ArgumentList;
import com.zetaplugins.zetacore.commands.exceptions.CommandUsageException;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@AutoRegisterCommand(
        commands = "give",
        description = "Gives a player a specified item.",
        usage = "/give [player] <item> [amount]",
        permission = "essentialz.give"
)
public class GiveCommand extends EszCommand {

    @InjectManager
    private GiveMaterialManager giveMaterialManager;

    public GiveCommand(EssentialZ plugin) {
        super(plugin);
    }

    @Override
    public boolean execute(CommandSender sender, Command command, String label, ArgumentList args) throws CommandUsageException {
        // Command: /give [player] <item> [amount]
        Player targetPlayer = args.getPlayer(0, getPlugin());
        if (targetPlayer == null) throw new CommandUsageException("/give [player] <item> [amount]");

        String materialName = args.getArg(1);
        if (materialName == null) throw new CommandUsageException("/give [player] <item> [amount]");
        Material material = giveMaterialManager.getMaterialByKey(materialName);
        if (material == null) {
            sender.sendMessage(getMessageManager().getAndFormatMsg(
                    PluginMessage.INVALID_MATERIAL,
                    new MessageManager.Replaceable<>("{material}", materialName)
            ));
            return false;
        }

        int amount = args.getInt(2, 1);

        ItemStack itemStack = new ItemStack(material, amount);
        targetPlayer.getInventory().addItem(itemStack);

        sender.sendMessage(getMessageManager().getAndFormatMsg(
                PluginMessage.GIVE_CONFIRMATION,
                new MessageManager.Replaceable<>("{amount}", String.valueOf(amount)),
                new MessageManager.Replaceable<>("{material}", material.name()),
                new MessageManager.Replaceable<>("{player}", targetPlayer.getName())
        ));

        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, Command command, ArgumentList args) {
        if (args.getCurrentArgIndex() == 0) {
            return getPlayerOptions(args.getCurrentArg());
        } else if (args.getCurrentArgIndex() == 1) {
            return getDisplayOptions(giveMaterialManager.getPossibleMaterials(), args.getCurrentArg());
        } else if (args.getCurrentArgIndex() == 2) {
            return getDisplayOptions(List.of("1", "16", "32", "64"), args.getCurrentArg());
        }
        return List.of();
    }
}
