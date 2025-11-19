package com.zetaplugins.essentialz.commands.movement;

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
        commands = "fly",
        description = "Allow players to fly.",
        usage = "/fly <player>",
        permission = "essentialz.fly"
)
public class FlyCommand extends CustomCommand {
    public FlyCommand(EssentialZ plugin) {
        super(plugin);
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
        if (args.getCurrentArgIndex() == 0 && sender.hasPermission("essentialz.fly.others")) {
            return getPlayerOptions(getPlugin(), args.getCurrentArg());
        }
        return List.of();
    }
}
