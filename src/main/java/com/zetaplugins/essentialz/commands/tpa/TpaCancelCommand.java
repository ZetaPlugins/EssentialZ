package com.zetaplugins.essentialz.commands.tpa;

import com.zetaplugins.essentialz.EssentialZ;
import com.zetaplugins.essentialz.features.tpa.TeleportRequest;
import com.zetaplugins.essentialz.features.tpa.TpaManager;
import com.zetaplugins.essentialz.util.MessageManager;
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
        commands = "tpacancel",
        description = "Cancel all outgoing teleport requests",
        usage = "/tpacancel",
        permission = "essentialz.tpacancel"
)
public class TpaCancelCommand extends EszCommand {

    @InjectManager
    private TpaManager tpaManager;

    public TpaCancelCommand(EssentialZ plugin) {
        super(plugin);
    }

    @Override
    public boolean execute(CommandSender sender, Command command, String label, ArgumentList args) throws CommandSenderMustBePlayerException {
        if (!(sender instanceof Player player)) {
            throw new CommandSenderMustBePlayerException();
        }

        List<TeleportRequest> requests = tpaManager.getOutgoingRequests(player.getUniqueId());

        if (requests.isEmpty()) {
            sender.sendMessage(getMessageManager().getAndFormatMsg(PluginMessage.TPA_NO_OUTGOING_REQUESTS));
            return true;
        }

        // Cancel all outgoing requests
        for (TeleportRequest request : requests) {
            tpaManager.removeRequest(request);
        }

        sender.sendMessage(getMessageManager().getAndFormatMsg(
                PluginMessage.TPA_REQUESTS_CANCELLED,
                new MessageManager.Replaceable<>("{count}", String.valueOf(requests.size()))
        ));

        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, Command command, ArgumentList args) {
        return List.of();
    }
}
