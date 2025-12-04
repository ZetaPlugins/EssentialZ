package com.zetaplugins.essentialz.commands;

import com.zetaplugins.essentialz.EssentialZ;
import com.zetaplugins.essentialz.features.RulesManager;
import com.zetaplugins.essentialz.util.MessageManager;
import com.zetaplugins.essentialz.util.PluginMessage;
import com.zetaplugins.essentialz.util.commands.EszCommand;
import com.zetaplugins.essentialz.util.permissions.Permission;
import com.zetaplugins.zetacore.annotations.AutoRegisterCommand;
import com.zetaplugins.zetacore.annotations.InjectManager;
import com.zetaplugins.zetacore.commands.ArgumentList;
import com.zetaplugins.zetacore.commands.exceptions.CommandException;
import com.zetaplugins.zetacore.commands.exceptions.CommandPermissionException;
import com.zetaplugins.zetacore.services.config.ConfigService;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

@AutoRegisterCommand(
        commands = "essentialz",
        description = "Main command for the EssentialZ plugin.",
        usage = "/essentialz",
        aliases = {"esz"}
)
public class MainCommand extends EszCommand {

    @InjectManager
    private MessageManager messageManager;
    @InjectManager
    private ConfigService configService;
    @InjectManager
    private RulesManager rulesManager;

    public MainCommand(EssentialZ plugin) {
        super(plugin);
    }

    @Override
    public boolean execute(CommandSender sender, Command command, String s, ArgumentList args) throws CommandException {
        String subcommand = args.getString(0, "about").toLowerCase();
        switch (subcommand) {
            case "about" -> {
                sender.sendMessage(messageManager.getAndFormatMsg(
                        PluginMessage.ABOUT_MESSAGE,
                        false,
                        new MessageManager.Replaceable<>("%version%", getPlugin().getDescription().getVersion())
                ));
                return true;
            }
            case "reload" -> {
                if (!Permission.RELOAD.has(sender)) throw new CommandPermissionException(Permission.RELOAD.getNode());
                getPlugin().reloadConfig();
                configService.clearCache();
                rulesManager.reload();
                sender.sendMessage(messageManager.getAndFormatMsg(PluginMessage.RELOAD_SUCCESS));
                return true;
            }
            default -> throw new CommandException("Unknown subcommand. Use /essentialz help for a list of commands.");
        }
    }

    @Override
    public List<String> tabComplete(CommandSender sender, Command command, ArgumentList args) {
        if (args.getCurrentArgIndex() == 0) {
            List<String> options = new ArrayList<>(List.of("about"));
            if (Permission.RELOAD.has(sender)) options.add("reload");
            return options;
        }
        return List.of();
    }
}
