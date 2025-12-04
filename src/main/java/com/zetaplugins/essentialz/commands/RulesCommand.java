package com.zetaplugins.essentialz.commands;

import com.zetaplugins.essentialz.EssentialZ;
import com.zetaplugins.essentialz.features.RulesManager;
import com.zetaplugins.essentialz.util.MessageManager;
import com.zetaplugins.essentialz.util.PluginMessage;
import com.zetaplugins.essentialz.util.commands.EszCommand;
import com.zetaplugins.zetacore.annotations.AutoRegisterCommand;
import com.zetaplugins.zetacore.annotations.InjectManager;
import com.zetaplugins.zetacore.commands.ArgumentList;
import com.zetaplugins.zetacore.commands.exceptions.CommandException;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.List;

@AutoRegisterCommand(
        commands = "rules",
        permission = "essentialz.rules",
        description = "Displays the server rules to the sender.",
        usage = "/rules"
)
public class RulesCommand extends EszCommand {

    @InjectManager
    private MessageManager messageManager;
    @InjectManager
    private RulesManager rulesManager;

    public RulesCommand(EssentialZ plugin) {
        super(plugin);
    }

    @Override
    public boolean execute(CommandSender sender, Command command, String s, ArgumentList args) throws CommandException {
        if (!rulesManager.fileExists()) {
            sender.sendMessage(messageManager.getAndFormatMsg(PluginMessage.NO_RULES_FILE_FOUND));
            return true;
        }

        List<String> lines = rulesManager.getLines();
        if (lines == null) {
            sender.sendMessage(messageManager.getAndFormatMsg(PluginMessage.ERROR_READING_RULES_FILE));
            return true;
        }

        for (String line : lines) {
            sender.sendMessage(messageManager.formatMsg(line));
        }

        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, Command command, ArgumentList args) {
        return List.of();
    }
}
