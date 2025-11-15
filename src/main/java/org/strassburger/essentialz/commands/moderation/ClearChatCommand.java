package org.strassburger.essentialz.commands.moderation;

import com.zetaplugins.zetacore.annotations.AutoRegisterCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.strassburger.essentialz.EssentialZ;
import org.strassburger.essentialz.util.MessageManager;
import org.strassburger.essentialz.util.commands.ArgumentList;
import org.strassburger.essentialz.util.commands.CommandPermissionException;
import org.strassburger.essentialz.util.commands.CommandUsageException;
import org.strassburger.essentialz.util.commands.CustomCommand;

import java.util.List;

@AutoRegisterCommand(commands = "clearchat")
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
        return sender.hasPermission("essentialz.clearchat");
    }

    @Override
    public List<String> tabComplete(CommandSender sender, Command command, ArgumentList args) {
        return List.of();
    }
}
