package com.zetaplugins.essentialz.commands.moderation;

import com.zetaplugins.essentialz.EssentialZ;
import com.zetaplugins.essentialz.util.MessageManager;
import com.zetaplugins.essentialz.util.PluginMessage;
import com.zetaplugins.essentialz.util.commands.EszCommand;
import com.zetaplugins.zetacore.annotations.AutoRegisterCommand;
import com.zetaplugins.zetacore.commands.ArgumentList;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.List;

@AutoRegisterCommand(
        commands = "clearchat",
        description = "Clears the chat for all players.",
        usage = "/clearchat",
        permission = "essentialz.clearchat",
        aliases = {"cc"}
)
public class ClearChatCommand extends EszCommand {

    public ClearChatCommand(EssentialZ plugin) {
        super(plugin);
    }

    @Override
    public boolean execute(CommandSender sender, Command command, String label, ArgumentList args) {
        String spaces = " \n".repeat(100);
        Bukkit.broadcast(getMessageManager().getAndFormatMsg(
                PluginMessage.CHAT_CLEARED,
                new MessageManager.Replaceable<>("{spaces}", spaces),
                new MessageManager.Replaceable<>("{player}", sender.getName())
        ));
        return false;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, Command command, ArgumentList args) {
        return List.of();
    }
}
