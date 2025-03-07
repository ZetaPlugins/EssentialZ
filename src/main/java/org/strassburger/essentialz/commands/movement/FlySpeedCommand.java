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

public class FlySpeedCommand extends CustomCommand {
    public FlySpeedCommand(String name, EssentialZ plugin) {
        super(name, plugin);
    }

    @Override
    public boolean execute(CommandSender sender, Command command, ArgumentList args) throws CommandPermissionException, CommandUsageException {
        int speed = 1;

        try {
            if (args.hasArg(0)) speed = args.getInt(0);
        } catch (NumberFormatException e) {
            throw new CommandUsageException("/flyspeed <speed> [player]");
        }

        speed = Math.clamp(speed, 1, 10);

        Player targetPlayer = args.getPlayer(1, getPlugin());

        if (targetPlayer != null && !(sender instanceof Player player && player.getUniqueId().equals(targetPlayer.getUniqueId()))) {
            if (!sender.hasPermission("essentialz.flyspeed.others")) {
                throw new CommandPermissionException("essentialz.flyspeed.others");
            }

            targetPlayer.setFlySpeed((float) speed / 10);
            sendConfirmMessage(targetPlayer, speed);
            sender.sendMessage(getPlugin().getMessageManager().getAndFormatMsg(
                    MessageManager.Style.MOVEMENT,
                    "flySpeedSetOther",
                    "&7Set {ac}{player}&7's fly speed to {ac}{speed}&7.",
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

        player.setFlySpeed((float) speed / 10);
        sendConfirmMessage(player, speed);
        return true;
    }

    private void sendConfirmMessage(Player player, int speed) {
        player.sendMessage(getPlugin().getMessageManager().getAndFormatMsg(
                MessageManager.Style.MOVEMENT,
                "flySpeedSet",
                "&7Set your fly speed to {ac}{speed}&7.",
                new MessageManager.Replaceable<>("{speed}", String.valueOf(speed))
        ));
    }

    @Override
    public boolean isAuthorized(CommandSender sender) {
        return sender.hasPermission("essentialz.flyspeed");
    }

    @Override
    public List<String> tabComplete(CommandSender sender, Command command, ArgumentList args) {
        if (args.getCurrentArgIndex() == 0) return List.of("1", "2", "3", "4", "5", "6", "7", "8", "9", "10");
        if (args.getCurrentArgIndex() == 1 && sender.hasPermission("essentialz.flyspeed.others")) return getPlayerOptions(getPlugin(), args.getCurrentArg());
        return List.of();
    }
}
