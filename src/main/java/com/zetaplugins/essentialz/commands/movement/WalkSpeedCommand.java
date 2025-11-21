package com.zetaplugins.essentialz.commands.movement;

import com.zetaplugins.essentialz.util.permissions.Permission;
import com.zetaplugins.zetacore.annotations.AutoRegisterCommand;
import com.zetaplugins.zetacore.commands.ArgumentList;
import com.zetaplugins.zetacore.commands.exceptions.CommandPermissionException;
import com.zetaplugins.zetacore.commands.exceptions.CommandUsageException;
import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.zetaplugins.essentialz.EssentialZ;
import com.zetaplugins.essentialz.util.MessageManager;
import com.zetaplugins.essentialz.util.commands.EszCommand;

import java.util.List;

@AutoRegisterCommand(
        commands = "walkspeed",
        description = "Set your or another player's walk speed.",
        usage = "/walkspeed <speed> [player]",
        permission = "essentialz.walkspeed",
        aliases = {"speed"}
)
public class WalkSpeedCommand extends EszCommand {
    public WalkSpeedCommand(EssentialZ plugin) {
        super(plugin);
    }

    @Override
    public boolean execute(CommandSender sender, Command command, String label, ArgumentList args) throws CommandUsageException, CommandPermissionException {
        int speed = 2;// Default walk speed is 0.2 not 0.1 for whatever reason

        try {
            if (args.hasArg(0)) speed = args.getInt(0);
        } catch (NumberFormatException e) {
            throw new CommandUsageException("/walkspeed <speed> [player]");
        }

        speed = Math.clamp(speed, 1, 10);

        Player targetPlayer = args.getPlayer(1, getPlugin());

        if (targetPlayer != null && !(sender instanceof Player player && player.getUniqueId().equals(targetPlayer.getUniqueId()))) {
            if (!Permission.WALKSPEED_OTHERS.has(sender)) {
                throw new CommandPermissionException(Permission.WALKSPEED_OTHERS.getNode());
            }

            targetPlayer.setWalkSpeed((float) speed / 10);
            sendConfirmMessage(targetPlayer, speed);
            sender.sendMessage(getPlugin().getMessageManager().getAndFormatMsg(
                    MessageManager.Style.MOVEMENT,
                    "walkSpeedSetOther",
                    "&7Set {ac}{player}&7's walk speed to {ac}{speed}&7.",
                    new MessageManager.Replaceable<>("{player}", targetPlayer.getName()),
                    new MessageManager.Replaceable<>("{speed}", String.valueOf(speed))
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

        player.setWalkSpeed((float) speed / 10);
        sendConfirmMessage(player, speed);
        return true;
    }

    private void sendConfirmMessage(Player player, int speed) {
        player.sendMessage(getPlugin().getMessageManager().getAndFormatMsg(
                MessageManager.Style.MOVEMENT,
                "walkSpeedSet",
                "&7Set your walk speed to {ac}{speed}&7.",
                new MessageManager.Replaceable<>("{speed}", String.valueOf(speed))
        ));
    }

    @Override
    public List<String> tabComplete(CommandSender sender, Command command, ArgumentList args) {
        if (args.getCurrentArgIndex() == 0) return List.of("1", "2", "3", "4", "5", "6", "7", "8", "9", "10");
        if (args.getCurrentArgIndex() == 1 && Permission.WALKSPEED_OTHERS.has(sender)) return getPlayerOptions(args.getCurrentArg());
        return List.of();
    }
}
