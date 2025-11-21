package com.zetaplugins.essentialz.commands.communication;

import com.zetaplugins.essentialz.EssentialZ;
import com.zetaplugins.essentialz.util.commands.EszCommand;
import com.zetaplugins.zetacore.annotations.AutoRegisterCommand;
import com.zetaplugins.zetacore.commands.ArgumentList;
import com.zetaplugins.zetacore.commands.exceptions.CommandUsageException;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.List;

@AutoRegisterCommand(
        commands = "broadcast",
        description = "Broadcast a message to the server",
        permission = "essentialz.broadcast",
        aliases = {"bc", "bcast"}
)
public class Broadcast extends EszCommand {

    public Broadcast(EssentialZ plugin) {
        super(plugin);
    }

    @Override
    public boolean execute(CommandSender sender, Command command, String label, ArgumentList args) throws CommandUsageException {
        if (args.size() < 1) throw new CommandUsageException("/" + command.getName() + " <message>");
        String message = args.getJoinedString(0);
        String broadCastFormat = getPlugin().getConfigManager().getChatConfig().getString("broadcastFormat");
        if (broadCastFormat == null) broadCastFormat = "&8[<#F06292>Broadcast&8] &7{message}";
        String formattedMessage = broadCastFormat.replace("{message}", message);
        Bukkit.broadcast(getPlugin().getMessageManager().formatMsg(formattedMessage));
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, Command command, ArgumentList args) {
        return List.of();
    }
}
