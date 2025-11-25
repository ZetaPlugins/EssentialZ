package com.zetaplugins.essentialz.commands.movement;

import com.zetaplugins.essentialz.EssentialZ;
import com.zetaplugins.essentialz.util.MessageManager;
import com.zetaplugins.essentialz.util.commands.EszCommand;
import com.zetaplugins.essentialz.util.permissions.Permission;
import com.zetaplugins.zetacore.annotations.AutoRegisterCommand;
import com.zetaplugins.zetacore.commands.ArgumentList;
import com.zetaplugins.zetacore.commands.exceptions.CommandPermissionException;
import com.zetaplugins.zetacore.commands.exceptions.CommandSenderMustBeOrSpecifyPlayerException;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

@AutoRegisterCommand(
        commands = "fly",
        description = "Allow players to fly.",
        usage = "/fly <player>",
        permission = "essentialz.fly"
)
public class FlyCommand extends EszCommand {

    public FlyCommand(EssentialZ plugin) {
        super(plugin);
    }

    @Override
    public boolean execute(CommandSender sender, Command command, String label, ArgumentList args) throws CommandPermissionException, CommandSenderMustBeOrSpecifyPlayerException {
        Player targetPlayer = args.getPlayer(0, getPlugin());

        if (targetPlayer != null && !(sender instanceof Player player && player.getUniqueId().equals(targetPlayer.getUniqueId()))) {
            if (!Permission.FLY_OTHERS.has(sender)) {
                throw new CommandPermissionException(Permission.FLY_OTHERS.getNode());
            }

            targetPlayer.setAllowFlight(!targetPlayer.getAllowFlight());

            sendOthersConfirmationMessage(targetPlayer, sender);

            return true;
        }

        if (!(sender instanceof Player player)) throw new CommandSenderMustBeOrSpecifyPlayerException();

        player.setAllowFlight(!player.getAllowFlight());

        sendSelfConfirmationMessage(player);

        return true;
    }

    public void sendSelfConfirmationMessage(Player player) {
        if (player.getAllowFlight()) {
            player.sendMessage(getMessageManager().getAndFormatMsg(
                    MessageManager.Style.MOVEMENT,
                    "allowedToFly",
                    "&7You are now allowed to fly!"
            ));
        } else {
            player.sendMessage(getMessageManager().getAndFormatMsg(
                    MessageManager.Style.MOVEMENT,
                    "disallowedToFly",
                    "&7You are no longer allowed to fly."
            ));
        }
    }

    public void sendOthersConfirmationMessage(Player targetPlayer, CommandSender sender) {
        sendSelfConfirmationMessage(targetPlayer);

        if (targetPlayer.getAllowFlight()) {
            sender.sendMessage(getMessageManager().getAndFormatMsg(
                    MessageManager.Style.MOVEMENT,
                    "allowedToFlyOther",
                    "&7{ac}{player}&7 is now allowed to fly!",
                    new MessageManager.Replaceable<>("{player}", targetPlayer.getName())
            ));
        } else {
            sender.sendMessage(getMessageManager().getAndFormatMsg(
                    MessageManager.Style.MOVEMENT,
                    "disallowedToFlyOther",
                    "&7{ac}{player}&7 is no longer allowed to fly!",
                    new MessageManager.Replaceable<>("{player}", targetPlayer.getName())
            ));
        }
    }

    @Override
    public List<String> tabComplete(CommandSender sender, Command command, ArgumentList args) {
        if (args.getCurrentArgIndex() == 0 && Permission.FLY_OTHERS.has(sender)) {
            return getPlayerOptions(args.getCurrentArg());
        }
        return List.of();
    }
}
