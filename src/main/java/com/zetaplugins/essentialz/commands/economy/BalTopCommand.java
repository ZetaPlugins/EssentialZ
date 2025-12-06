package com.zetaplugins.essentialz.commands.economy;

import com.zetaplugins.essentialz.EssentialZ;
import com.zetaplugins.essentialz.features.economy.manager.EconomyManager;
import com.zetaplugins.essentialz.util.MessageManager;
import com.zetaplugins.essentialz.util.PluginMessage;
import com.zetaplugins.essentialz.util.commands.EszCommand;
import com.zetaplugins.zetacore.annotations.AutoRegisterCommand;
import com.zetaplugins.zetacore.annotations.InjectManager;
import com.zetaplugins.zetacore.commands.ArgumentList;
import com.zetaplugins.zetacore.commands.exceptions.CommandException;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

@AutoRegisterCommand(
        commands = "baltop",
        description = "Show the top balances on the server",
        usage = "/baltop",
        permission = "essentialz.baltop"
)
public class BalTopCommand extends EszCommand {

    @InjectManager
    private EconomyManager economyManager;
    @InjectManager
    private MessageManager messageManager;

    public BalTopCommand(EssentialZ plugin) {
        super(plugin);
    }

    @Override
    public boolean execute(CommandSender sender, Command command, String s, ArgumentList args) throws CommandException {
        var topBalances = economyManager.getTopBalances(10);
        sender.sendMessage(messageManager.getAndFormatMsg(PluginMessage.BALTOP_HEADER, false));
        int i = 1;
        for (UUID uuuid : topBalances.keySet()) {
            Player player = getPlugin().getServer().getPlayer(uuuid);
            OfflinePlayer offlinePlayer = getPlugin().getServer().getOfflinePlayer(uuuid);
            String playerName = (player != null) ? player.getName() : offlinePlayer.getName();
            double balance = topBalances.get(uuuid);
            sender.sendMessage(messageManager.getAndFormatMsg(
                    PluginMessage.BALTOP_ENTRY,
                    false,
                    new MessageManager.Replaceable<>("{rank}", String.valueOf(i++)),
                    new MessageManager.Replaceable<>("{player}", playerName),
                    new MessageManager.Replaceable<>("{balance}", economyManager.format(balance))
            ));
        }
        sender.sendMessage(messageManager.getAndFormatMsg(PluginMessage.BALTOP_FOOTER, false));
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, Command command, ArgumentList args) {
        return List.of();
    }
}
