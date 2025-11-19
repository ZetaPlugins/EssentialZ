package com.zetaplugins.essentialz.commands.moderation;

import com.zetaplugins.essentialz.util.permissions.Permission;
import com.zetaplugins.zetacore.annotations.AutoRegisterCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.zetaplugins.essentialz.EssentialZ;
import com.zetaplugins.essentialz.util.MessageManager;
import com.zetaplugins.essentialz.util.commands.ArgumentList;
import com.zetaplugins.essentialz.util.commands.CommandPermissionException;
import com.zetaplugins.essentialz.util.commands.CommandUsageException;
import com.zetaplugins.essentialz.util.commands.CustomCommand;

import java.util.List;

@AutoRegisterCommand(
        commands = "godmode",
        description = "Toggles god mode for yourself or another player.",
        usage = "/godmode [player]",
        aliases = {"god"}
)
public class GodModeCommand extends CustomCommand {
    public GodModeCommand(EssentialZ plugin) {
        super(plugin);
    }

    @Override
    public boolean execute(CommandSender sender, Command command, ArgumentList args) throws CommandPermissionException, CommandUsageException {
        Player targetPlayer = args.getPlayer(0, getPlugin());

        if (targetPlayer != null && !(sender instanceof Player player && player.getUniqueId().equals(targetPlayer.getUniqueId()))) {
            if (!Permission.GODMODE_OTHERS.has(sender)) {
                throw new CommandPermissionException(Permission.GODMODE_OTHERS.getNode());
            }

            boolean isInGodMode = getPlugin().getGodModeManager().isInGodMode(targetPlayer);
            getPlugin().getGodModeManager().setGodMode(targetPlayer, !isInGodMode);

            sendConfirmMessage(targetPlayer, !isInGodMode);
            sender.sendMessage(getPlugin().getMessageManager().getAndFormatMsg(
                    MessageManager.Style.MODERATION,
                    "godModeToggledOther",
                    "&7God mode for {ac}{player}&7 is now {ac}{status}&7.",
                    new MessageManager.Replaceable<>("{player}", targetPlayer.getName()),
                    new MessageManager.Replaceable<>("{status}", !isInGodMode ? "enabled" : "disabled")
            ));
            return true;
        }

        if (!(sender instanceof Player player)) {
            sender.sendMessage(getPlugin().getMessageManager().getAndFormatMsg(
                    MessageManager.Style.ERROR,
                    "specifyPlayerOrBePlayer",
                    "{ac}You must specify a player or be a player to use this command."
            ));
            return false;
        }

        boolean isInGodMode = getPlugin().getGodModeManager().isInGodMode(player);
        getPlugin().getGodModeManager().setGodMode(player, !isInGodMode);

        sendConfirmMessage(player, !isInGodMode);
        return true;
    }

    public void sendConfirmMessage(Player player, boolean isInGodMode) {
        player.sendMessage(getPlugin().getMessageManager().getAndFormatMsg(
                MessageManager.Style.MODERATION,
                "godModeToggled",
                "&7God mode is now {ac}{status}&7.",
                new MessageManager.Replaceable<>("{status}", isInGodMode ? "enabled" : "disabled")
        ));
    }

    @Override
    public boolean isAuthorized(CommandSender sender) {
        return Permission.GODMODE.has(sender);
    }

    @Override
    public List<String> tabComplete(CommandSender sender, Command command, ArgumentList args) {
        if (args.getCurrentArgIndex() == 0 && Permission.GODMODE_OTHERS.has(sender)) {
            return getPlayerOptions(getPlugin(), args.getCurrentArg());
        }
        return List.of();
    }
}
