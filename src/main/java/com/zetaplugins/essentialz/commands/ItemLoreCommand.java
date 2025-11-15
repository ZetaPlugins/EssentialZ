package com.zetaplugins.essentialz.commands;

import com.zetaplugins.zetacore.annotations.AutoRegisterCommand;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import com.zetaplugins.essentialz.EssentialZ;
import com.zetaplugins.essentialz.util.MessageManager;
import com.zetaplugins.essentialz.util.commands.ArgumentList;
import com.zetaplugins.essentialz.util.commands.CommandPermissionException;
import com.zetaplugins.essentialz.util.commands.CommandUsageException;
import com.zetaplugins.essentialz.util.commands.CustomCommand;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@AutoRegisterCommand(commands = "itemlore")
public class ItemLoreCommand extends CustomCommand {
    public ItemLoreCommand(EssentialZ plugin) {
        super(plugin);
    }

    @Override
    public boolean execute(CommandSender sender, Command command, ArgumentList args) throws CommandPermissionException, CommandUsageException {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(getPlugin().getMessageManager().getAndFormatMsg(
                    MessageManager.Style.ERROR,
                    "playerOnly",
                    "{ac}You must be a player to use this command."
            ));
            return false;
        }

        ItemStack item = player.getInventory().getItemInMainHand();

        if (item.getType() == Material.AIR) {
            sender.sendMessage(getPlugin().getMessageManager().getAndFormatMsg(
                    MessageManager.Style.ERROR,
                    "mustHoldAnItem",
                    "{ac}You must be holding an item."
            ));
            return false;
        }

        if (args.size() < 1) {
            throw new CommandUsageException("/itemlore <add|set|remove> <line> [lore]");
        }

        String action = args.getArg(0);

        if (!List.of("add", "set", "remove", "insert").contains(action)) {
            throw new CommandUsageException("/itemlore <add|set|remove> <line> [lore]");
        }

        switch (action) {
            case "add": {
                if (args.size() < 2) {
                    throw new CommandUsageException("/itemlore add <lore>");
                }
                String lore = getLoreFromArgs(args, 1);
                return handleAddLore(player, item, lore);
            }
            case "set": {
                if (args.size() < 3) {
                    throw new CommandUsageException("/itemlore set <line> <lore>");
                }
                try {
                    int line = Integer.parseInt(args.getArg(1)) - 1;
                    String lore = getLoreFromArgs(args, 2);
                    return handleSetLore(player, item, line, lore);
                } catch (NumberFormatException e) {
                    throw new CommandUsageException("/itemlore set <line> <lore>");
                }
            }
            case "remove": {
                if (args.size() < 2) {
                    throw new CommandUsageException("/itemlore remove <line>");
                }
                try {
                    int line = Integer.parseInt(args.getArg(1)) - 1;
                    return handleRemoveLore(player, item, line);
                } catch (NumberFormatException e) {
                    throw new CommandUsageException("/itemlore remove <line>");
                }
            }
            case "insert": {
                if (args.size() < 3) {
                    throw new CommandUsageException("/itemlore insert <line> <lore>");
                }

                try {
                    int line = Integer.parseInt(args.getArg(1)) - 1;
                    String lore = getLoreFromArgs(args, 2);
                    return handleInsertLore(player, item, line, lore);
                } catch (NumberFormatException e) {
                    throw new CommandUsageException("/itemlore insert <line> <lore>");
                }
            }
        }

        return false;
    }

    private boolean handleAddLore(Player player, ItemStack item, String lore) {
        List<Component> currentLore = item.getItemMeta().lore();
        if (currentLore == null) currentLore = new ArrayList<>();

        int maxLoreLines = getPlugin().getConfig().getInt("maxLoreLines");
        if (currentLore.size() >= maxLoreLines || currentLore.size() >= 128) {
            player.sendMessage(getPlugin().getMessageManager().getAndFormatMsg(
                    MessageManager.Style.ERROR,
                    "loreTooLong",
                    "{ac}The lore is too long! The maximum length is {max} lines.",
                    new MessageManager.Replaceable<>("{max}", String.valueOf(maxLoreLines))
            ));
            return false;
        }

        currentLore.add(getPlugin().getMessageManager().formatMsg(lore));
        setItemLore(item, currentLore);

        player.sendMessage(getPlugin().getMessageManager().getAndFormatMsg(
                MessageManager.Style.DEFAULT,
                "loreAdded",
                "&7Added '{lore}&r&7' to the lore.",
                new MessageManager.Replaceable<>("{lore}", lore)
        ));
        return true;
    }

    private boolean handleSetLore(Player player, ItemStack item, int line, String lore) {
        List<Component> currentLore = item.getItemMeta().lore();
        if (currentLore == null) {
            sendLoreLineDoesNotExistMessage(player);
            return false;
        }

        if (line < 0 || line >= currentLore.size()) {
            sendLoreLineDoesNotExistMessage(player);
            return false;
        }

        currentLore.set(line, getPlugin().getMessageManager().formatMsg(lore));
        setItemLore(item, currentLore);

        player.sendMessage(getPlugin().getMessageManager().getAndFormatMsg(
                MessageManager.Style.DEFAULT,
                "loreSet",
                "&7Set lore line {ac}{line}&7 to '{lore}&r&7'.",
                new MessageManager.Replaceable<>("{line}", String.valueOf(line + 1)),
                new MessageManager.Replaceable<>("{lore}", lore)
        ));

        return true;
    }

    private boolean handleRemoveLore(Player player, ItemStack item, int line) {
        List<Component> currentLore = item.getItemMeta().lore();
        if (currentLore == null) {
            sendLoreLineDoesNotExistMessage(player);
            return false;
        }

        if (line < 0 || line >= currentLore.size()) {
            sendLoreLineDoesNotExistMessage(player);
            return false;
        }

        currentLore.remove(line);
        setItemLore(item, currentLore);

        player.sendMessage(getPlugin().getMessageManager().getAndFormatMsg(
                MessageManager.Style.DEFAULT,
                "loreRemoved",
                "&7Removed lore line {ac}{line}&7.",
                new MessageManager.Replaceable<>("{line}", String.valueOf(line + 1))
        ));

        return true;
    }

    private boolean handleInsertLore(Player player, ItemStack item, int line, String lore) {
        List<Component> currentLore = item.getItemMeta().lore();
        if (currentLore == null) {
            sendLoreLineDoesNotExistMessage(player);
            return false;
        }

        if (line < 0 || line >= currentLore.size()) {
            sendLoreLineDoesNotExistMessage(player);
            return false;
        }

        int maxLoreLines = getPlugin().getConfig().getInt("maxLoreLines");
        if (currentLore.size() >= maxLoreLines || currentLore.size() >= 128) {
            player.sendMessage(getPlugin().getMessageManager().getAndFormatMsg(
                    MessageManager.Style.ERROR,
                    "loreTooLong",
                    "{ac}The lore is too long! The maximum length is {max} lines.",
                    new MessageManager.Replaceable<>("{max}", String.valueOf(maxLoreLines))
            ));
            return false;
        }

        currentLore.add(line, getPlugin().getMessageManager().formatMsg(lore));
        setItemLore(item, currentLore);

        player.sendMessage(getPlugin().getMessageManager().getAndFormatMsg(
                MessageManager.Style.DEFAULT,
                "loreInserted",
                "&7Inserted '{lore}&r&7' at line {ac}{line}&7.",
                new MessageManager.Replaceable<>("{line}", String.valueOf(line + 1)),
                new MessageManager.Replaceable<>("{lore}", lore)
        ));

        return true;
    }

    /**
     * Get the lore from the arguments
     * @param args The arguments
     * @param startIndex The index to start at
     * @return The lore
     */
    private String getLoreFromArgs(ArgumentList args, int startIndex) {
        StringBuilder lore = new StringBuilder();
        for (int i = startIndex; i < args.size(); i++) {
            lore.append(args.getArg(i));
            if (i != args.size() - 1) {
                lore.append(" ");
            }
        }

        return lore.toString();
    }

    private void setItemLore(ItemStack item, List<Component> lore) {
        ItemMeta meta = item.getItemMeta();
        meta.lore(lore);
        item.setItemMeta(meta);
    }

    private void sendLoreLineDoesNotExistMessage(Player player) {
        player.sendMessage(getPlugin().getMessageManager().getAndFormatMsg(
                MessageManager.Style.ERROR,
                "loreLineDoesNotExist",
                "{ac}This lore line does not exist!"
        ));
    }

    @Override
    public boolean isAuthorized(CommandSender sender) {
        return sender.hasPermission("essentialz.itemlore");
    }

    @Override
    public List<String> tabComplete(CommandSender sender, Command command, ArgumentList args) {
        if (args.getCurrentArgIndex() == 0) {
            return getDisplayOptions(List.of("add", "set", "remove", "insert"), args.getCurrentArg());
        }
        if (args.getCurrentArgIndex() == 1 && (List.of("set", "remove", "insert").contains(args.getArg(0)))) {
            return getDisplayOptions(
                    getItemLoreLines(sender).stream().map(Object::toString).collect(Collectors.toList()),
                    args.getCurrentArg()
            );
        }
        return List.of();
    }

    private List<Integer> getItemLoreLines(CommandSender sender) {
        if (!(sender instanceof Player player)) return List.of();

        ItemStack item = player.getInventory().getItemInMainHand();

        if (item.getType() == Material.AIR) return List.of();

        List<Component> lore = item.getItemMeta().lore();

        if (lore == null) return List.of();

        List<Integer> lines = new ArrayList<>();
        for (int i = 0; i < lore.size(); i++) {
            lines.add(i + 1);
        }

        return lines;
    }
}
