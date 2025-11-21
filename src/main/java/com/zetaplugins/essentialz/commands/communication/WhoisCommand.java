package com.zetaplugins.essentialz.commands.communication;

import com.zetaplugins.essentialz.EssentialZ;
import com.zetaplugins.essentialz.util.MessageManager;
import com.zetaplugins.essentialz.util.commands.EszCommand;
import com.zetaplugins.essentialz.util.permissions.Permission;
import com.zetaplugins.zetacore.annotations.AutoRegisterCommand;
import com.zetaplugins.zetacore.commands.ArgumentList;
import com.zetaplugins.zetacore.commands.exceptions.CommandUsageException;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.List;

@AutoRegisterCommand(
        commands = "whois",
        description = "Get information about a player.",
        usage = "/whois <player>",
        permission = "essentialz.whois"
)
public class WhoisCommand extends EszCommand {

    public WhoisCommand(EssentialZ plugin) {
        super(plugin);
    }

    @Override
    public boolean execute(CommandSender sender, Command command, String label, ArgumentList args) throws CommandUsageException {
        String targetName = args.getArg(0);

        if (targetName == null || targetName.isEmpty()) throw new CommandUsageException("/whois <player>");

        boolean found = false;

        for (var player : Bukkit.getOnlinePlayers()) {
            String realName = player.getName();
            String displayName = PlainTextComponentSerializer.plainText().serialize(player.displayName());

            // Check if input matches real name or display name
            if (realName.equalsIgnoreCase(targetName) || displayName.equalsIgnoreCase(targetName)) {
                sender.sendMessage(getPlugin().getMessageManager().getAndFormatMsg(
                        MessageManager.Style.NONE,
                        "whoisInfo",
                        """
                        &8┌─ <#81C784>Whois Info&r&8 ─────────────────
                        &8│ &7Nickname: <#4CAF50>{displayname}
                        &8│ &7Real Name: <#E9D502>{realname}
                        &8└─────────────────────────────
                        """,
                        new MessageManager.Replaceable<>("{displayname}", displayName),
                        new MessageManager.Replaceable<>("{realname}", realName)
                ));
                found = true;
            }
        }

        if (!found) {
            sender.sendMessage(getPlugin().getMessageManager().getAndFormatMsg(
                    MessageManager.Style.ERROR,
                    "noOneWithNameFound",
                    "{ac}Could not find online player with name or nickname '{name}'!",
                    new MessageManager.Replaceable<>("{name}", targetName)
            ));
            return false;
        }

        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, Command command, ArgumentList args) {
        if (args.getCurrentArgIndex() == 0) {
            return Bukkit.getOnlinePlayers().stream()
                    .map(player -> {
                        String realName = player.getName();
                        String displayName = PlainTextComponentSerializer.plainText().serialize(player.displayName());
                        return List.of(realName, displayName);
                    })
                    .flatMap(List::stream)
                    .filter(name -> name.toLowerCase().startsWith(args.getArg(0).toLowerCase()))
                    .toList();
        }
        return List.of();
    }
}
