package com.zetaplugins.essentialz.commands.movement;

import com.zetaplugins.essentialz.util.permissions.Permission;
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
        commands = {"gmc", "gms", "gma", "gmsp"},
        description = "Shortcut commands to change gamemodes.",
        usage = "/<command> [player]",
        permission = "essentialz.gamemode"
)
public class GamemodeShortcutCommand extends CustomCommand {
    public GamemodeShortcutCommand(EssentialZ plugin) {
        super(plugin);
    }

    @Override
    public boolean execute(CommandSender sender, Command command, ArgumentList args) throws CommandPermissionException, CommandUsageException {
        Player targetPlayer = args.getPlayer(0, getPlugin());

        if (targetPlayer != null && !(sender instanceof Player player && player.getUniqueId().equals(targetPlayer.getUniqueId()))) {
            if (!Permission.GAMEMODE_OTHERS.has(sender)) {
                throw new CommandPermissionException(Permission.GAMEMODE_OTHERS.getNode());
            }

            sender.sendMessage(getPlugin().getMessageManager().getAndFormatMsg(
                    MessageManager.Style.MOVEMENT,
                    "gamemodeSetOther",
                    "&7Set {ac}{player}&7's gamemode to {ac}{gamemode}&7.",
                    new MessageManager.Replaceable<>("{player}", targetPlayer.getName()),
                    new MessageManager.Replaceable<>("{gamemode}", commandNameToGamemode(command.getName()))
            ));

            setGamemode(targetPlayer, command.getName());
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

        return setGamemode(player, command.getName());
    }

    private String commandNameToGamemode(String commandName) {
        return switch (commandName) {
            case "gmc" -> getPlugin().getLanguageManager().getString("creative", "creative");
            case "gms" -> getPlugin().getLanguageManager().getString("survival", "survival");
            case "gma" -> getPlugin().getLanguageManager().getString("adventure", "adventure");
            case "gmsp" -> getPlugin().getLanguageManager().getString("spectator", "spectator");
            default -> null;
        };
    }

    private boolean setGamemode(Player player, String commandName) {
        String gameMode = commandNameToGamemode(commandName);
        switch (commandName) {
            case "gmc":
                player.setGameMode(GameMode.CREATIVE);
                sendConfirmationMessage(player, gameMode);
                return true;
            case "gms":
                player.setGameMode(GameMode.SURVIVAL);
                sendConfirmationMessage(player, gameMode);
                return true;
            case "gma":
                player.setGameMode(GameMode.ADVENTURE);
                sendConfirmationMessage(player, gameMode);
                return true;
            case "gmsp":
                player.setGameMode(GameMode.SPECTATOR);
                sendConfirmationMessage(player, gameMode);
                return true;
        }

        return false;
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
        return Permission.GAMEMODE.has(sender);
    }

    @Override
    public List<String> tabComplete(CommandSender sender, Command command, ArgumentList args) {
        if (args.getCurrentArgIndex() == 0 && Permission.GAMEMODE_OTHERS.has(sender)) {
            return getPlayerOptions(getPlugin(), args.getCurrentArg());
        }
        return List.of();
    }
}
