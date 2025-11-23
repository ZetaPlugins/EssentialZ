package com.zetaplugins.essentialz.commands.communication;

import com.zetaplugins.essentialz.EssentialZ;
import com.zetaplugins.essentialz.storage.Storage;
import com.zetaplugins.essentialz.storage.model.PlayerData;
import com.zetaplugins.essentialz.util.MessageManager;
import com.zetaplugins.essentialz.util.commands.EszCommand;
import com.zetaplugins.zetacore.annotations.AutoRegisterCommand;
import com.zetaplugins.zetacore.annotations.InjectManager;
import com.zetaplugins.zetacore.commands.ArgumentList;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.List;

@AutoRegisterCommand(
        commands = "teamchat",
        description = "Send a message to all staff members online.",
        usage = "/teamchat <message>",
        aliases = {"tc"},
        permission = "essentialz.teamchat"
)
public class TeamchatCommand extends EszCommand {

    @InjectManager
    private MessageManager messageManager;
    @InjectManager
    private Storage storage;

    public TeamchatCommand(EssentialZ plugin) {
        super(plugin);
    }

    @Override
    public boolean execute(CommandSender sender, Command command, String label, ArgumentList args) {
        Collection<? extends Player> playersWithPerm = Bukkit.getOnlinePlayers()
                .stream()
                .filter(p -> p.hasPermission("essentialz.teamchat"))
                .toList();

        if (sender instanceof Player senderPlayer) {
            PlayerData senderPlayerData = storage.load(senderPlayer.getUniqueId());
            if (!senderPlayerData.isEnableTeamchat()) {
                sender.sendMessage(messageManager.getAndFormatMsg(
                        MessageManager.Style.ERROR,
                        "yourTeamchatDisabled",
                        "{ac}You have disabled team chat. Enable it using /tctoggle to send messages."
                ));
                return false;
            }
        }

        String message = args.getJoinedString(0);

        boolean allowToUseColor = sender.hasPermission("essentialz.teamchat.color");

        for (Player p : playersWithPerm) {
            PlayerData playerData = storage.load(p.getUniqueId());
            if (playerData != null && !playerData.isEnableTeamchat()) continue;

            p.sendMessage(messageManager.getAndFormatMsg(
                    MessageManager.Style.TEAMCHAT,
                    "teamChatMessage",
                    "&8[&7From {ac}{player}&8]&7: &7{message}",
                    new MessageManager.Replaceable<>("{player}", sender.getName()),
                    new MessageManager.Replaceable<>("{message}", message, allowToUseColor)
            ));
        }

        Bukkit.getConsoleSender().sendMessage(messageManager.getAndFormatMsg(
                MessageManager.Style.TEAMCHAT,
                "teamChatMessage",
                "&8[&7From {ac}{player}&8]&7: &7{message}",
                new MessageManager.Replaceable<>("{player}", sender.getName()),
                new MessageManager.Replaceable<>("{message}", message, allowToUseColor)
        ));

        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, Command command, ArgumentList args) {
        return List.of();
    }
}
