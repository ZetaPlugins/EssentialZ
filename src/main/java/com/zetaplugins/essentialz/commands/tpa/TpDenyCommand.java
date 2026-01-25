package com.zetaplugins.essentialz.commands.tpa;

import com.zetaplugins.essentialz.EssentialZ;
import com.zetaplugins.essentialz.features.tpa.TeleportRequest;
import com.zetaplugins.essentialz.features.tpa.TpaManager;
import com.zetaplugins.essentialz.features.tpa.TpaUtils;
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
        commands = "tpdeny",
        description = "Deny a teleport request",
        usage = "/tpdeny",
        permission = "essentialz.tpdeny",
        aliases = {"tpno"}
)
public class TpDenyCommand extends EszCommand {

    @InjectManager
    private TpaManager tpaManager;

    public TpDenyCommand(EssentialZ plugin) {
        super(plugin);
    }

    @Override
    public boolean execute(CommandSender sender, Command command, String label, ArgumentList args) throws CommandSenderMustBePlayerException {
        if (!(sender instanceof Player player)) {
            throw new CommandSenderMustBePlayerException();
        }

        List<TeleportRequest> requests = tpaManager.getIncomingRequests(player.getUniqueId());

        if (requests.isEmpty()) {
            sender.sendMessage(getMessageManager().getAndFormatMsg(PluginMessage.TPA_NO_PENDING_REQUESTS));
            return true;
        }

        // Deny the most recent request
        TeleportRequest request = requests.get(requests.size() - 1);
        denyRequest(player, request);
        tpaManager.removeRequest(request);

        TpaUtils.playSound(player, getPlugin().getConfig().getString("tpa.sounds.deny", "ENTITY_VILLAGER_NO"), getPlugin());

        return true;
    }

    private void denyRequest(Player denier, TeleportRequest request) {
        Player senderPlayer = request.getSenderPlayer();

        if (senderPlayer != null && senderPlayer.isOnline()) {
            senderPlayer.sendMessage(getMessageManager().getAndFormatMsg(
                    PluginMessage.TPA_DENIED_SENDER,
                    new MessageManager.Replaceable<>("{player}", denier.getName())
            ));
        }

        denier.sendMessage(getMessageManager().getAndFormatMsg(PluginMessage.TPA_DENIED_TARGET));
    }

    @Override
    public List<String> tabComplete(CommandSender sender, Command command, ArgumentList args) {
        return List.of();
    }
}
