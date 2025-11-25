package com.zetaplugins.essentialz.commands.movement;

import com.zetaplugins.essentialz.EssentialZ;
import com.zetaplugins.essentialz.util.MessageManager;
import com.zetaplugins.essentialz.util.commands.EszCommand;
import com.zetaplugins.essentialz.util.permissions.Permission;
import com.zetaplugins.zetacore.annotations.AutoRegisterCommand;
import com.zetaplugins.zetacore.commands.ArgumentList;
import com.zetaplugins.zetacore.commands.exceptions.CommandPermissionException;
import com.zetaplugins.zetacore.commands.exceptions.CommandUsageException;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

@AutoRegisterCommand(
        commands = "flyspeed",
        description = "Set your or another player's fly speed (1-10).",
        usage = "/flyspeed <speed> [player]",
        permission = "essentialz.flyspeed"
)
public class FlySpeedCommand extends EszCommand {

    public FlySpeedCommand(EssentialZ plugin) {
        super(plugin);
    }

    @Override
    public boolean execute(CommandSender sender, Command command, String label, ArgumentList args) throws CommandPermissionException, CommandUsageException {
        int speed = 1;

        try {
            if (args.hasArg(0)) speed = args.getInt(0);
        } catch (NumberFormatException e) {
            throw new CommandUsageException("/flyspeed <speed> [player]");
        }

        speed = Math.clamp(speed, 1, 10);

        Player targetPlayer = args.getPlayer(1, getPlugin());

        if (targetPlayer != null && !(sender instanceof Player player && player.getUniqueId().equals(targetPlayer.getUniqueId()))) {
            if (!Permission.FLYSPEED_OTHERS.has(sender)) {
                throw new CommandPermissionException(Permission.FLYSPEED_OTHERS.getNode());
            }

            targetPlayer.setFlySpeed((float) speed / 10);
            sendConfirmMessage(targetPlayer, speed);
            sender.sendMessage(getMessageManager().getAndFormatMsg(
                    MessageManager.Style.MOVEMENT,
                    "flySpeedSetOther",
                    "&7Set {ac}{player}&7's fly speed to {ac}{speed}&7.",
                    new MessageManager.Replaceable<>("{player}", targetPlayer.getName()),
                    new MessageManager.Replaceable<>("{speed}", String.valueOf(speed))
            ));
            return true;
        }

        if (!(sender instanceof Player player)) {
            sender.sendMessage(getMessageManager().getAndFormatMsg(
                    MessageManager.Style.ERROR,
                    "specifyPlayerOrBePlayer",
                    "{ac}You must specify a player or be a player to use this command."
            ));
            return false;
        }

        player.setFlySpeed((float) speed / 10);
        sendConfirmMessage(player, speed);
        return true;
    }

    private void sendConfirmMessage(Player player, int speed) {
        player.sendMessage(getMessageManager().getAndFormatMsg(
                MessageManager.Style.MOVEMENT,
                "flySpeedSet",
                "&7Set your fly speed to {ac}{speed}&7.",
                new MessageManager.Replaceable<>("{speed}", String.valueOf(speed))
        ));
    }

    @Override
    public List<String> tabComplete(CommandSender sender, Command command, ArgumentList args) {
        if (args.getCurrentArgIndex() == 0) return List.of("1", "2", "3", "4", "5", "6", "7", "8", "9", "10");
        if (args.getCurrentArgIndex() == 1 && Permission.FLYSPEED_OTHERS.has(sender)) return getPlayerOptions(args.getCurrentArg());
        return List.of();
    }
}
