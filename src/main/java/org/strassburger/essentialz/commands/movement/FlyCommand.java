package org.strassburger.essentialz.commands.movement;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.strassburger.essentialz.EssentialZ;
import org.strassburger.essentialz.util.MessageManager;
import org.strassburger.essentialz.util.commands.ArgumentList;
import org.strassburger.essentialz.util.commands.CommandPermissionException;
import org.strassburger.essentialz.util.commands.CommandUsageException;
import org.strassburger.essentialz.util.commands.CustomCommand;

import java.util.List;

public class FlyCommand extends CustomCommand {
    public FlyCommand(String name, EssentialZ plugin) {
        super(name, plugin);
    }

    @Override
    public boolean execute(CommandSender sender, Command command, ArgumentList args) throws CommandPermissionException, CommandUsageException {
        Player targetPlayer = args.getPlayer(0, getPlugin());

        if (targetPlayer != null && !(sender instanceof Player player && player.getUniqueId().equals(targetPlayer.getUniqueId()))) {
            if (!sender.hasPermission("essentialz.fly.others")) {
                throw new CommandPermissionException("essentialz.fly.others");
            }

            targetPlayer.setAllowFlight(!targetPlayer.getAllowFlight());

            sendOthersConfirmationMessage(targetPlayer, sender);

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

        player.setAllowFlight(!player.getAllowFlight());

        sendSelfConfirmationMessage(player);

        return true;
    }

    public void sendSelfConfirmationMessage(Player player) {
        if (player.getAllowFlight()) {
            player.sendMessage(getPlugin().getMessageManager().getAndFormatMsg(
                    MessageManager.Style.MOVEMENT,
                    "allowedToFly",
                    "&7You are now allowed to fly!"
            ));
        } else {
            player.sendMessage(getPlugin().getMessageManager().getAndFormatMsg(
                    MessageManager.Style.MOVEMENT,
                    "disallowedToFly",
                    "&7You are no longer allowed to fly."
            ));
        }
    }

    public void sendOthersConfirmationMessage(Player targetPlayer, CommandSender sender) {
        sendSelfConfirmationMessage(targetPlayer);

        if (targetPlayer.getAllowFlight()) {
            sender.sendMessage(getPlugin().getMessageManager().getAndFormatMsg(
                    MessageManager.Style.MOVEMENT,
                    "allowedToFlyOther",
                    "&7{ac}{player}&7 is now allowed to fly!",
                    new MessageManager.Replaceable<>("{player}", targetPlayer.getName())
            ));
        } else {
            sender.sendMessage(getPlugin().getMessageManager().getAndFormatMsg(
                    MessageManager.Style.MOVEMENT,
                    "disallowedToFlyOther",
                    "&7{ac}{player}&7 is no longer allowed to fly!",
                    new MessageManager.Replaceable<>("{player}", targetPlayer.getName())
            ));
        }
    }

    @Override
    public boolean isAuthorized(CommandSender sender) {
        return sender.hasPermission("essentialz.fly");
    }

    @Override
    public List<String> tabComplete(CommandSender sender, Command command, ArgumentList args) {
        if (args.getCurrentArgIndex() == 0) {
            return getPlayerOptions(getPlugin(), args.getCurrentArg());
        }
        return List.of();
    }
}
