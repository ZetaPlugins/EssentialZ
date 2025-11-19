package com.zetaplugins.essentialz.commands.movement;

import com.zetaplugins.zetacore.annotations.AutoRegisterCommand;
import org.bukkit.GameMode;
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
        commands = "gamemode",
        description = "Change your or another player's gamemode.",
        usage = "/gamemode <mode> [player]",
        permission = "essentialz.gamemode",
        aliases = {"gm"}
)
public class GamemodeCommand extends CustomCommand {
    public GamemodeCommand(EssentialZ plugin) {
        super(plugin);
    }

    @Override
    public boolean execute(CommandSender sender, Command command, ArgumentList args) throws CommandPermissionException, CommandUsageException {
        String rawGamemode = args.getArg(0);
        GameMode gamemode = parseGamemode(rawGamemode);

        if (gamemode == null) {
            throw new CommandUsageException("/gamemode <survival|creative|adventure|spectator> [player]");
        }

        Player targetPlayer = args.getPlayer(1, getPlugin());

        if (targetPlayer != null && !(sender instanceof Player player && player.getUniqueId().equals(targetPlayer.getUniqueId()))) {
            if (!sender.hasPermission("essentialz.gamemode.others")) {
                throw new CommandPermissionException("essentialz.gamemode.others");
            }

            targetPlayer.setGameMode(gamemode);
            sendConfirmationMessage(targetPlayer, translateGamemode(gamemode));
            sender.sendMessage(getPlugin().getMessageManager().getAndFormatMsg(
                    MessageManager.Style.MOVEMENT,
                    "gamemodeSetOther",
                    "&7Set {ac}{player}&7's gamemode to {ac}{gamemode}&7.",
                    new MessageManager.Replaceable<>("{player}", targetPlayer.getName()),
                    new MessageManager.Replaceable<>("{gamemode}", gamemode.name().toLowerCase())
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

        player.setGameMode(gamemode);
        sendConfirmationMessage(player, translateGamemode(gamemode));
        return true;
    }

    private GameMode parseGamemode(String gamemode) {
        if (gamemode == null) return null;

        return switch (gamemode) {
            case "survival", "0", "s" -> GameMode.SURVIVAL;
            case "creative", "1", "c" -> GameMode.CREATIVE;
            case "adventure", "2", "a" -> GameMode.ADVENTURE;
            case "spectator", "3", "sp" -> GameMode.SPECTATOR;
            default -> null;
        };
    }

    private String translateGamemode(GameMode gameMode) {
        return switch (gameMode) {
            case SURVIVAL -> getPlugin().getLanguageManager().getString("survival", "survival");
            case CREATIVE -> getPlugin().getLanguageManager().getString("creative", "creative");
            case ADVENTURE -> getPlugin().getLanguageManager().getString("adventure", "adventure");
            case SPECTATOR -> getPlugin().getLanguageManager().getString("spectator", "spectator");
        };
    }

    private void sendConfirmationMessage(Player player, String gameMode) {
        player.sendMessage(getPlugin().getMessageManager().getAndFormatMsg(
                MessageManager.Style.MOVEMENT,
                "gamemodeSet",
                "&7Set your gamemode to {ac}{gamemode}&7.",
                new MessageManager.Replaceable<>("{gamemode}", gameMode)
        ));
    }

    @Override
    public boolean isAuthorized(CommandSender sender) {
        return sender.hasPermission("essentialz.gamemode");
    }

    @Override
    public List<String> tabComplete(CommandSender sender, Command command, ArgumentList args) {
        if (args.getCurrentArgIndex() == 0) {
            return getDisplayOptions(List.of("survival", "creative", "adventure", "spectator"), args.getCurrentArg());
        }
        if (args.getCurrentArgIndex() == 1 && sender.hasPermission("essentialz.gamemode.others")) {
            return getPlayerOptions(getPlugin(), args.getCurrentArg());
        }
        return List.of();
    }
}
