package com.zetaplugins.essentialz.commands.moderation;

import com.zetaplugins.essentialz.EssentialZ;
import com.zetaplugins.essentialz.util.MessageManager;
import com.zetaplugins.essentialz.util.PluginMessage;
import com.zetaplugins.essentialz.util.commands.CommandPlayerNotFoundException;
import com.zetaplugins.essentialz.util.commands.EszCommand;
import com.zetaplugins.zetacore.annotations.AutoRegisterCommand;
import com.zetaplugins.zetacore.commands.ArgumentList;
import com.zetaplugins.zetacore.commands.exceptions.CommandException;
import com.zetaplugins.zetacore.commands.exceptions.CommandUsageException;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

@AutoRegisterCommand(
        commands = "sudo",
        description = "Force a player to run a command.",
        usage = "/sudo <player> <command>",
        permission = "essentialz.sudo"
)
public class SudoCommand extends EszCommand {

    public SudoCommand(EssentialZ plugin) {
        super(plugin);
    }

    @Override
    public boolean execute(CommandSender sender, Command command, String label, ArgumentList args) throws CommandException {
        Player player = args.getPlayer(0, getPlugin());
        String cmd = args.getJoinedString(1);

        if (args.getArg(0) == null) throw new CommandUsageException("/sudo <player> <commandToExecute>");
        if (player == null) throw new CommandPlayerNotFoundException();

        if (cmd.isEmpty()) throw new CommandUsageException("/sudo <player> <commandToExecute>");

        player.performCommand(cmd);
        sender.sendMessage(getMessageManager().getAndFormatMsg(
                PluginMessage.SUDO_SUCCESS,
                new MessageManager.Replaceable<>("{player}", player.getName()),
                new MessageManager.Replaceable<>("{command}", cmd)
        ));
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, Command command, ArgumentList args) {
        if (args.getCurrentArgIndex() == 0) return getPlayerOptions(args.getCurrentArg());
        else if (args.getCurrentArgIndex() == 1) return getCommandOptions(args.getCurrentArg());
        else return List.of();
    }

    private List<String> getCommandOptions(String currentArg) {
        return getPlugin().getServer().getCommandMap().getKnownCommands().values().stream()
                .map(Command::getName)
                .filter(cmdName -> cmdName.toLowerCase().startsWith(currentArg.toLowerCase()))
                .toList();
    }
}
