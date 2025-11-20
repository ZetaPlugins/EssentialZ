package com.zetaplugins.essentialz.commands.items;

import com.zetaplugins.essentialz.EssentialZ;
import com.zetaplugins.essentialz.util.MessageManager;
import com.zetaplugins.essentialz.util.commands.ArgumentList;
import com.zetaplugins.essentialz.util.commands.CommandPermissionException;
import com.zetaplugins.essentialz.util.commands.CommandUsageException;
import com.zetaplugins.essentialz.util.commands.CustomCommand;
import com.zetaplugins.essentialz.util.permissions.Permission;
import com.zetaplugins.zetacore.annotations.AutoRegisterCommand;
import org.bukkit.Material;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;

import java.util.ArrayList;
import java.util.List;

@AutoRegisterCommand(
        commands = "givespawner",
        description = "Gives a player a mob spawner of the specified type.",
        usage = "/givespawner <player> <mobType> [amount]",
        permission = "essentialz.givespawner"
)
public class GiveSpawnerCommand extends CustomCommand {

    public GiveSpawnerCommand(EssentialZ plugin) {
        super(plugin);
    }

    @Override
    public boolean execute(CommandSender sender, Command command, ArgumentList args) throws CommandPermissionException, CommandUsageException {
        Player targetPlayer = args.getPlayer(0, getPlugin());
        EntityType entityType = args.getEnum(1, EntityType.class);
        int amount = args.getInt(2, 1);

        if (args.size() < 2) throw new CommandUsageException("/<command> <player> <mobType> [amount]");

        if (entityType == null || !entityType.isSpawnable() || !entityType.isAlive()) {
            sender.sendMessage(getPlugin().getMessageManager().getAndFormatMsg(
                    MessageManager.Style.ERROR,
                    "invalidMobType",
                    "{ac}You have to specify a valid mob type."
            ));
            return false;
        }

        ItemStack spawnerItem = new ItemStack(Material.SPAWNER, amount);

        BlockStateMeta meta = (BlockStateMeta) spawnerItem.getItemMeta();
        CreatureSpawner state = (CreatureSpawner) meta.getBlockState();

        state.setSpawnedType(entityType);
        meta.setBlockState(state);
        spawnerItem.setItemMeta(meta);

        targetPlayer.getInventory().addItem(spawnerItem);

        sender.sendMessage(getPlugin().getMessageManager().getAndFormatMsg(
                MessageManager.Style.ITEMS,
                "givespawnerSuccess",
                "&7Gave {ac}{amount} {mobType} &7spawner(s) to {ac}{player}&7.",
                new MessageManager.Replaceable<>("{player}", targetPlayer.getName()),
                new MessageManager.Replaceable<>("{amount}", String.valueOf(amount)),
                new MessageManager.Replaceable<>("{mobType}", entityType.name())
        ));
        return true;
    }

    @Override
    public boolean isAuthorized(CommandSender sender) {
        return Permission.GIVESPAWNER.has(sender);
    }

    @Override
    public List<String> tabComplete(CommandSender sender, Command command, ArgumentList args) {
        if (args.getCurrentArgIndex() == 0) {
            return getPlayerOptions(getPlugin(), args.getCurrentArg());
        } else if (args.getCurrentArgIndex() == 1) {
            return new ArrayList<>(List.of(EntityType.values()))
                    .stream()
                    .filter(EntityType::isSpawnable)
                    .filter(EntityType::isAlive)
                    .map(EntityType::name)
                    .map(String::toLowerCase)
                    .filter(et -> et.startsWith(args.getCurrentArg().toLowerCase()))
                    .toList();
        } else if (args.getCurrentArgIndex() == 2) {
            return List.of("1", "16", "32", "64");
        }
        return List.of();
    }
}
