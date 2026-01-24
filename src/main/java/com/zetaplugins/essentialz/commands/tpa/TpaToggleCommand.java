package com.zetaplugins.essentialz.commands.tpa;

import com.zetaplugins.essentialz.EssentialZ;
import com.zetaplugins.essentialz.features.tpa.TpaToggleManager;
import com.zetaplugins.essentialz.util.PluginMessage;
import com.zetaplugins.essentialz.util.commands.EszCommand;
import com.zetaplugins.zetacore.annotations.AutoRegisterCommand;
import com.zetaplugins.zetacore.annotations.InjectManager;
import com.zetaplugins.zetacore.commands.ArgumentList;
import com.zetaplugins.zetacore.commands.exceptions.CommandSenderMustBePlayerException;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

@AutoRegisterCommand(
        commands = "tpatoggle",
        description = "Toggle receiving teleport requests",
        usage = "/tpatoggle",
        permission = "essentialz.tpatoggle"
)
public class TpaToggleCommand extends EszCommand {

    @InjectManager
    private TpaToggleManager toggleManager;

    public TpaToggleCommand(EssentialZ plugin) {
        super(plugin);
    }

    @Override
    public boolean execute(CommandSender sender, Command command, String label, ArgumentList args) throws CommandSenderMustBePlayerException {
        if (!(sender instanceof Player player)) {
            throw new CommandSenderMustBePlayerException();
        }

        toggleManager.toggle(player.getUniqueId());

        if (toggleManager.isEnabled(player.getUniqueId())) {
            sender.sendMessage(getMessageManager().getAndFormatMsg(PluginMessage.TPA_TOGGLE_ENABLED));
        } else {
            sender.sendMessage(getMessageManager().getAndFormatMsg(PluginMessage.TPA_TOGGLE_DISABLED));
        }

        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, Command command, ArgumentList args) {
        return List.of();
    }
}
