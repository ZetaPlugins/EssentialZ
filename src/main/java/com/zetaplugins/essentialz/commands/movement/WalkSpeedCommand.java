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

@AutoRegisterCommand(commands = "walkspeed")
public class WalkSpeedCommand extends CustomCommand {
    public WalkSpeedCommand(EssentialZ plugin) {
        super(plugin);
    }

    @Override
    public boolean execute(CommandSender sender, Command command, ArgumentList args) throws CommandPermissionException, CommandUsageException {
        int speed = 2;// Default walk speed is 0.2 not 0.1 for whatever reason

        try {
            if (args.hasArg(0)) speed = args.getInt(0);
        } catch (NumberFormatException e) {
            throw new CommandUsageException("/walkspeed <speed> [player]");
        }

        speed = Math.clamp(speed, 1, 10);

        Player targetPlayer = args.getPlayer(1, getPlugin());

        if (targetPlayer != null && !(sender instanceof Player player && player.getUniqueId().equals(targetPlayer.getUniqueId()))) {
            if (!sender.hasPermission("essentialz.walkspeed.others")) {
                throw new CommandPermissionException("essentialz.walkspeed.others");
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
    public boolean isAuthorized(CommandSender sender) {
        return sender.hasPermission("essentialz.walkspeed");
    }

    @Override
    public List<String> tabComplete(CommandSender sender, Command command, ArgumentList args) {
        if (args.getCurrentArgIndex() == 0) return List.of("1", "2", "3", "4", "5", "6", "7", "8", "9", "10");
        if (args.getCurrentArgIndex() == 1 && sender.hasPermission("essentialz.walkspeed.others")) return getPlayerOptions(getPlugin(), args.getCurrentArg());
        return List.of();
    }
}
