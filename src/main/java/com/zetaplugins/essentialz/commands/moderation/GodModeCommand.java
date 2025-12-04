package com.zetaplugins.essentialz.commands.moderation;

import com.zetaplugins.essentialz.EssentialZ;
import com.zetaplugins.essentialz.features.GodModeManager;
import com.zetaplugins.essentialz.util.MessageManager;
import com.zetaplugins.essentialz.util.PluginMessage;
import com.zetaplugins.essentialz.util.commands.EszCommand;
import com.zetaplugins.essentialz.util.permissions.Permission;
import com.zetaplugins.zetacore.annotations.AutoRegisterCommand;
import com.zetaplugins.zetacore.annotations.InjectManager;
import com.zetaplugins.zetacore.commands.ArgumentList;
import com.zetaplugins.zetacore.commands.exceptions.CommandPermissionException;
import com.zetaplugins.zetacore.commands.exceptions.CommandSenderMustBeOrSpecifyPlayerException;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

@AutoRegisterCommand(
        commands = "godmode",
        description = "Toggles god mode for yourself or another player.",
        usage = "/godmode [player]",
        aliases = {"god"}
)
public class GodModeCommand extends EszCommand {

    @InjectManager
    private GodModeManager godModeManager;

    public GodModeCommand(EssentialZ plugin) {
        super(plugin);
    }

    @Override
    public boolean execute(CommandSender sender, Command command, String label, ArgumentList args) throws CommandPermissionException, CommandSenderMustBeOrSpecifyPlayerException {
        Player targetPlayer = args.getPlayer(0, getPlugin());

        if (targetPlayer != null && !(sender instanceof Player player && player.getUniqueId().equals(targetPlayer.getUniqueId()))) {
            if (!Permission.GODMODE_OTHERS.has(sender)) {
                throw new CommandPermissionException(Permission.GODMODE_OTHERS.getNode());
            }

            boolean isInGodMode = godModeManager.isInGodMode(targetPlayer);
            godModeManager.setGodMode(targetPlayer, !isInGodMode);

            sendConfirmMessage(targetPlayer, !isInGodMode);
            sender.sendMessage(getMessageManager().getAndFormatMsg(
                    PluginMessage.GODMODE_TOGGLED_OTHER,
                    new MessageManager.Replaceable<>("{player}", targetPlayer.getName()),
                    new MessageManager.Replaceable<>("{status}", !isInGodMode ? "enabled" : "disabled")
            ));
            return true;
        }

        if (!(sender instanceof Player player)) throw new CommandSenderMustBeOrSpecifyPlayerException();

        boolean isInGodMode = godModeManager.isInGodMode(player);
        godModeManager.setGodMode(player, !isInGodMode);

        sendConfirmMessage(player, !isInGodMode);
        return true;
    }

    public void sendConfirmMessage(Player player, boolean isInGodMode) {
        player.sendMessage(getMessageManager().getAndFormatMsg(
                PluginMessage.GODMODE_TOGGLED_SELF,
                new MessageManager.Replaceable<>("{status}", isInGodMode ? "enabled" : "disabled")
        ));
    }

    @Override
    public List<String> tabComplete(CommandSender sender, Command command, ArgumentList args) {
        if (args.getCurrentArgIndex() == 0 && Permission.GODMODE_OTHERS.has(sender)) {
            return getPlayerOptions(args.getCurrentArg());
        }
        return List.of();
    }
}
