package com.zetaplugins.essentialz.commands;

import com.zetaplugins.essentialz.util.commands.ArgumentList;
import com.zetaplugins.essentialz.util.commands.CommandPermissionException;
import com.zetaplugins.essentialz.util.commands.CommandUsageException;
import com.zetaplugins.essentialz.util.commands.CustomCommand;
import com.zetaplugins.zetacore.annotations.AutoRegisterCommand;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import com.zetaplugins.essentialz.EssentialZ;
import com.zetaplugins.essentialz.util.MessageManager;
import com.zetaplugins.essentialz.util.commands.*;

import java.util.List;

@AutoRegisterCommand(commands = "skull")
public class SkullCommand extends CustomCommand {
    public SkullCommand(EssentialZ plugin) {
        super(plugin);
    }

    @Override
    public boolean execute(CommandSender sender, Command command, ArgumentList args) throws CommandPermissionException, CommandUsageException {
        if (args.size() < 1) {
            throw new CommandUsageException("/skull <player> [player]");
        }

        OfflinePlayer skullPlayer = getPlugin().getServer().getOfflinePlayer(args.getArg(0));

        if (args.hasArg(1)) {
            Player targetPlayer = args.getPlayer(1, getPlugin());

            if (targetPlayer == null) {
                sender.sendMessage(getPlugin().getMessageManager().getAndFormatMsg(
                        MessageManager.Style.ERROR,
                        "playerNotFound",
                        "{ac}Player not found."
                ));
                return false;
            }

            targetPlayer.getInventory().addItem(getSkull(skullPlayer));

            sender.sendMessage(getPlugin().getMessageManager().getAndFormatMsg(
                    MessageManager.Style.DEFAULT,
                    "skullGiven",
                    "&7Seccessfully gave {ac}{player}&7's head to {ac}{receiver}&7.",
                    new MessageManager.Replaceable<>("{receiver}", targetPlayer.getName()),
                    new MessageManager.Replaceable<>("{player}", skullPlayer.getName())
            ));
            return true;
        }

        if (!(sender instanceof Player targetPlayer)) {
            sender.sendMessage(getPlugin().getMessageManager().getAndFormatMsg(
                    MessageManager.Style.ERROR,
                    "specifyPlayerOrBePlayer",
                    "{ac}You must specify a player or be a player to use this command."
            ));
            return false;
        }

        targetPlayer.getInventory().addItem(getSkull(skullPlayer));

        sender.sendMessage(getPlugin().getMessageManager().getAndFormatMsg(
                MessageManager.Style.DEFAULT,
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
    public boolean isAuthorized(CommandSender sender) {
        return sender.hasPermission("essentialz.skull");
    }

    @Override
    public List<String> tabComplete(CommandSender sender, Command command, ArgumentList args) {
        if (args.getCurrentArgIndex() == 0 || args.getCurrentArgIndex() == 1) {
            return getPlayerOptions(getPlugin(), args.getCurrentArg());
        }

        return List.of();
    }
}
