package com.zetaplugins.essentialz.commands.movement;

import com.zetaplugins.essentialz.EssentialZ;
import com.zetaplugins.essentialz.util.LanguageManager;
import com.zetaplugins.essentialz.util.MessageManager;
import com.zetaplugins.essentialz.util.commands.EszCommand;
import com.zetaplugins.essentialz.util.permissions.Permission;
import com.zetaplugins.zetacore.annotations.AutoRegisterCommand;
import com.zetaplugins.zetacore.annotations.InjectManager;
import com.zetaplugins.zetacore.commands.ArgumentList;
import com.zetaplugins.zetacore.commands.exceptions.CommandPermissionException;
import com.zetaplugins.zetacore.commands.exceptions.CommandUsageException;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

@AutoRegisterCommand(
        commands = "gamemode",
        description = "Change your or another player's gamemode.",
        usage = "/gamemode <mode> [player]",
        permission = "essentialz.gamemode",
        aliases = {"gm"}
)
public class GamemodeCommand extends EszCommand {

    @InjectManager
    private LanguageManager languageManager;

    public GamemodeCommand(EssentialZ plugin) {
        super(plugin);
    }

    @Override
    public boolean execute(CommandSender sender, Command command, String label, ArgumentList args) throws CommandPermissionException, CommandUsageException {
        String rawGamemode = args.getArg(0);
        GameMode gamemode = parseGamemode(rawGamemode);

        if (gamemode == null) {
            throw new CommandUsageException("/gamemode <survival|creative|adventure|spectator> [player]");
        }

        Player targetPlayer = args.getPlayer(1, getPlugin());

        if (targetPlayer != null && !(sender instanceof Player player && player.getUniqueId().equals(targetPlayer.getUniqueId()))) {
            if (!Permission.GAMEMODE_OTHERS.has(sender)) {
                throw new CommandPermissionException(Permission.GAMEMODE_OTHERS.getNode());
            }

            targetPlayer.setGameMode(gamemode);
            sendConfirmationMessage(targetPlayer, translateGamemode(gamemode));
            sender.sendMessage(getMessageManager().getAndFormatMsg(
                    MessageManager.Style.MOVEMENT,
                    "gamemodeSetOther",
                    "&7Set {ac}{player}&7's gamemode to {ac}{gamemode}&7.",
                    new MessageManager.Replaceable<>("{player}", targetPlayer.getName()),
                    new MessageManager.Replaceable<>("{gamemode}", gamemode.name().toLowerCase())
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
            case SURVIVAL -> languageManager.getString("survival", "survival");
            case CREATIVE -> languageManager.getString("creative", "creative");
            case ADVENTURE -> languageManager.getString("adventure", "adventure");
            case SPECTATOR -> languageManager.getString("spectator", "spectator");
        };
    }

    private void sendConfirmationMessage(Player player, String gameMode) {
        player.sendMessage(getMessageManager().getAndFormatMsg(
                MessageManager.Style.MOVEMENT,
                "gamemodeSet",
                "&7Set your gamemode to {ac}{gamemode}&7.",
                new MessageManager.Replaceable<>("{gamemode}", gameMode)
        ));
    }

    @Override
    public List<String> tabComplete(CommandSender sender, Command command, ArgumentList args) {
        if (args.getCurrentArgIndex() == 0) {
            return getDisplayOptions(List.of("survival", "creative", "adventure", "spectator"), args.getCurrentArg());
        }
        if (args.getCurrentArgIndex() == 1 && Permission.GAMEMODE_OTHERS.has(sender)) {
            return getPlayerOptions(args.getCurrentArg());
        }
        return List.of();
    }
}
