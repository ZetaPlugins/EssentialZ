package com.zetaplugins.essentialz.commands.movement.homes;

import com.zetaplugins.essentialz.EssentialZ;
import com.zetaplugins.essentialz.config.main.MainConfig;
import com.zetaplugins.essentialz.storage.Storage;
import com.zetaplugins.essentialz.storage.model.HomeData;
import com.zetaplugins.essentialz.storage.model.WarpData;
import com.zetaplugins.essentialz.util.MessageManager;
import com.zetaplugins.essentialz.util.PluginMessage;
import com.zetaplugins.essentialz.util.commands.EszCommand;
import com.zetaplugins.zetacore.annotations.AutoRegisterCommand;
import com.zetaplugins.zetacore.annotations.InjectManager;
import com.zetaplugins.zetacore.commands.ArgumentList;
import com.zetaplugins.zetacore.commands.exceptions.CommandException;
import com.zetaplugins.zetacore.commands.exceptions.CommandSenderMustBePlayerException;
import com.zetaplugins.zetacore.commands.exceptions.CommandUsageException;
import com.zetaplugins.zetacore.services.config.ConfigService;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;

import java.util.List;

@AutoRegisterCommand(
        commands = "sethome",
        permission = "essentialz.sethome",
        description = "Sets a home at your current location.",
        usage = "/sethome <name>"
)
public class SetHomeCommand extends EszCommand {

    @InjectManager
    private Storage storage;

    @InjectManager
    private ConfigService configService;

    public SetHomeCommand(EssentialZ plugin) {
        super(plugin);
    }

    private int getMaxHomesFromPerms(Player player, int defaultMaxHomes) {
        int maxHomes = defaultMaxHomes;
        for (String perm : player.getEffectivePermissions().stream().map(PermissionAttachmentInfo::getPermission).toList()) {
            if (!perm.startsWith("essentialz.maxhomes.")) continue;

            String suffix = perm.substring("essentialz.maxhomes.".length());
            try {
                int homes = Integer.parseInt(suffix);
                if (homes > maxHomes) maxHomes = homes;
            } catch (NumberFormatException ignored) { }
        }
        return maxHomes;
    }

    @Override
    public boolean execute(CommandSender commandSender, Command command, String s, ArgumentList args) throws CommandException {
        if (!(commandSender instanceof Player player)) throw new CommandSenderMustBePlayerException();

        String homeName = args.getArg(0);
        if (homeName == null || homeName.isEmpty()) throw new CommandUsageException("/sethome <name>");

        int homeCount = storage.getHomesRepository().getHomeCount(player.getUniqueId());
        int maxHomes = getMaxHomesFromPerms(player, configService.getConfig(MainConfig.class).getDefaultMaxHomes());
        if (homeCount >= maxHomes) {
            player.sendMessage(getMessageManager().getAndFormatMsg(
                    PluginMessage.HOME_SET_MAX_HOMES_REACHED,
                    new MessageManager.Replaceable<>("{maxHomes}", String.valueOf(maxHomes))
            ));
            return true;
        }

        storage.getHomesRepository().save(new HomeData(player.getUniqueId(), homeName, player.getLocation()));

        player.sendMessage(getMessageManager().getAndFormatMsg(
                PluginMessage.HOME_SET_SUCCESS,
                new MessageManager.Replaceable<>("{homeName}", homeName),
                new MessageManager.Replaceable<>("{x}", String.valueOf(player.getLocation().getBlockX())),
                new MessageManager.Replaceable<>("{y}", String.valueOf(player.getLocation().getBlockY())),
                new MessageManager.Replaceable<>("{z}", String.valueOf(player.getLocation().getBlockZ()))
        ));
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender commandSender, Command command, ArgumentList argumentList) {
        return List.of();
    }
}
