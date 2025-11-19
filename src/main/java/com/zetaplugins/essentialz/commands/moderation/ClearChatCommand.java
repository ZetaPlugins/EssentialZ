package com.zetaplugins.essentialz.commands.moderation;

import com.zetaplugins.essentialz.util.permissions.Permission;
import com.zetaplugins.zetacore.annotations.AutoRegisterCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import com.zetaplugins.essentialz.EssentialZ;
import com.zetaplugins.essentialz.util.MessageManager;
import com.zetaplugins.essentialz.util.commands.ArgumentList;
import com.zetaplugins.essentialz.util.commands.CommandPermissionException;
import com.zetaplugins.essentialz.util.commands.CommandUsageException;
import com.zetaplugins.essentialz.util.commands.CustomCommand;

import java.util.List;

@AutoRegisterCommand(
        commands = "clearchat",
        description = "Clears the chat for all players.",
        usage = "/clearchat",
        permission = "essentialz.clearchat",
        aliases = {"cc"}
)
public class ClearChatCommand extends CustomCommand {
    public ClearChatCommand(EssentialZ plugin) {
        super(plugin);
    }

    @Override
    public boolean execute(CommandSender sender, Command command, ArgumentList args) throws CommandPermissionException, CommandUsageException {
        String spaces = " \n".repeat(100);
        Bukkit.broadcast(getPlugin().getMessageManager().getAndFormatMsg(
                MessageManager.Style.MODERATION,
                "chatCleared",
                "{spaces}{prefix}&7Chat has been cleared by {ac}{player}&7.",
                new MessageManager.Replaceable<>("{spaces}", spaces),
                new MessageManager.Replaceable<>("{player}", sender.getName())
        ));
        return false;
    }

    @Override
    public boolean isAuthorized(CommandSender sender) {
        return Permission.CLEARCHAT.has(sender);
    }

    @Override
    public List<String> tabComplete(CommandSender sender, Command command, ArgumentList args) {
        return List.of();
    }
}
