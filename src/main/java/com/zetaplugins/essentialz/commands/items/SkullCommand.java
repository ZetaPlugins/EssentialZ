package com.zetaplugins.essentialz.commands.items;

import com.zetaplugins.essentialz.EssentialZ;
import com.zetaplugins.essentialz.util.MessageManager;
import com.zetaplugins.essentialz.util.MessageStyle;
import com.zetaplugins.essentialz.util.commands.EszCommand;
import com.zetaplugins.zetacore.annotations.AutoRegisterCommand;
import com.zetaplugins.zetacore.commands.ArgumentList;
import com.zetaplugins.zetacore.commands.exceptions.CommandUsageException;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.List;

@AutoRegisterCommand(
        commands = "skull",
        description = "Gives a player head of the specified player.",
        usage = "/skull <player> [player]",
        permission = "essentialz.skull"
)
public class SkullCommand extends EszCommand {

    public SkullCommand(EssentialZ plugin) {
        super(plugin);
    }

    @Override
    public boolean execute(CommandSender sender, Command command, String label, ArgumentList args) throws CommandUsageException {
        if (args.size() < 1) {
            throw new CommandUsageException("/skull <player> [player]");
        }

        OfflinePlayer skullPlayer = getPlugin().getServer().getOfflinePlayer(args.getArg(0));

        if (args.hasArg(1)) {
            Player targetPlayer = args.getPlayer(1, getPlugin());

            if (targetPlayer == null) {
                sender.sendMessage(getMessageManager().getAndFormatMsg(
                        MessageStyle.ERROR,
                        "playerNotFound",
                        "{ac}Player not found."
                ));
                return false;
            }

            targetPlayer.getInventory().addItem(getSkull(skullPlayer));

            sender.sendMessage(getMessageManager().getAndFormatMsg(
                    MessageStyle.ITEMS,
                    "skullGiven",
                    "&7Seccessfully gave {ac}{player}&7's head to {ac}{receiver}&7.",
                    new MessageManager.Replaceable<>("{receiver}", targetPlayer.getName()),
                    new MessageManager.Replaceable<>("{player}", skullPlayer.getName())
            ));
            return true;
        }

        if (!(sender instanceof Player targetPlayer)) {
            sender.sendMessage(getMessageManager().getAndFormatMsg(
                    MessageStyle.ERROR,
                    "specifyPlayerOrBePlayer",
                    "{ac}You must specify a player or be a player to use this command."
            ));
            return false;
        }

        targetPlayer.getInventory().addItem(getSkull(skullPlayer));

        sender.sendMessage(getMessageManager().getAndFormatMsg(
                MessageStyle.ITEMS,
                "skullReceived",
                "&7You received {ac}{player}&7's head.",
                new MessageManager.Replaceable<>("{player}", skullPlayer.getName())
        ));
        return false;
    }

    private ItemStack getSkull(OfflinePlayer player) {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        meta.setOwningPlayer(player);
        head.setItemMeta(meta);
        return head;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, Command command, ArgumentList args) {
        if (args.getCurrentArgIndex() == 0 || args.getCurrentArgIndex() == 1) {
            return getPlayerOptions(args.getCurrentArg());
        }

        return List.of();
    }
}
