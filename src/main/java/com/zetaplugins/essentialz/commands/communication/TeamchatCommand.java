package com.zetaplugins.essentialz.commands.communication;

import com.zetaplugins.essentialz.EssentialZ;
import com.zetaplugins.essentialz.util.MessageManager;
import com.zetaplugins.essentialz.util.commands.ArgumentList;
import com.zetaplugins.essentialz.util.commands.CommandPermissionException;
import com.zetaplugins.essentialz.util.commands.CommandUsageException;
import com.zetaplugins.essentialz.util.commands.CustomCommand;
import com.zetaplugins.zetacore.annotations.AutoRegisterCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.List;

@AutoRegisterCommand(commands = "teamchat")
public class TeamchatCommand extends CustomCommand {

    public TeamchatCommand(EssentialZ plugin) {
        super(plugin);
    }

    @Override
    public boolean execute(CommandSender sender, Command command, ArgumentList args) throws CommandPermissionException, CommandUsageException {
        Collection<? extends Player> playersWithPerm = Bukkit.getOnlinePlayers()
                .stream()
                .filter(p -> p.hasPermission("essentialz.teamchat"))
                .toList();

        if (playersWithPerm.isEmpty()) {
            sender.sendMessage("No staff members are currently online.");
            return true;
        }

        String message = args.getJoinedString(0);

        boolean allowToUseColor = sender.hasPermission("essentialz.teamchat.color");

        for (Player p : playersWithPerm) {
            p.sendMessage(getPlugin().getMessageManager().getAndFormatMsg(
                    MessageManager.Style.TEAMCHAT,
                    "teamChatMessage",
                    "&8[&7From {ac}{player}&8]&7: &7{message}",
                    new MessageManager.Replaceable<>("{player}", sender.getName()),
                    new MessageManager.Replaceable<>("{message}", message, allowToUseColor)
            ));
        }

        return true;
    }

    @Override
    public boolean isAuthorized(CommandSender sender) {
        return sender.hasPermission("essentialz.teamchat");
    }

    @Override
    public List<String> tabComplete(CommandSender sender, Command command, ArgumentList args) {
        return List.of();
    }
}
