package com.zetaplugins.essentialz.commands.tpa;

import com.zetaplugins.essentialz.EssentialZ;
import com.zetaplugins.essentialz.features.tpa.*;
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
        commands = "tpaccept",
        description = "Accept a teleport request",
        usage = "/tpaccept",
        permission = "essentialz.tpaccept",
        aliases = {"tpyes"}
)
public class TpAcceptCommand extends EszCommand {

    @InjectManager
    private TpaManager tpaManager;

    @InjectManager
    private TpaToggleManager toggleManager;

    @InjectManager
    private TpaBedrockFormHandler bedrockFormHandler;

    private boolean floodgateEnabled = false;

    public TpAcceptCommand(EssentialZ plugin) {
        super(plugin);
        initFloodgate();
    }

    private void initFloodgate() {
        if (getPlugin().getServer().getPluginManager().getPlugin("floodgate") != null) {
            try {
                Class.forName("org.geysermc.floodgate.api.FloodgateApi");
                floodgateEnabled = true;
            } catch (ClassNotFoundException e) {
                floodgateEnabled = false;
            }
        }
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

        // Bedrock player with multiple requests - show form
        if (floodgateEnabled && isBedrockPlayer(player) && requests.size() > 1) {
            bedrockFormHandler.sendPendingRequestsForm(player);
            return true;
        }

        // Accept the most recent request
        TeleportRequest request = requests.get(requests.size() - 1);
        acceptRequest(player, request);
        tpaManager.removeRequest(request);

        TpaUtils.playSound(player, getPlugin().getConfig().getString("tpa.sounds.accept", "ENTITY_PLAYER_LEVELUP"), getPlugin());

        return true;
    }

    private void acceptRequest(Player acceptor, TeleportRequest request) {
        Player senderPlayer = request.getSenderPlayer();
        Player targetPlayer = request.getTargetPlayer();

        if (senderPlayer == null || !senderPlayer.isOnline()) {
            acceptor.sendMessage(getMessageManager().getAndFormatMsg(PluginMessage.PLAYER_NOT_FOUND));
            return;
        }

        if (targetPlayer == null || !targetPlayer.isOnline()) {
            return;
        }

        if (request.getType() == TpaRequestType.TPA) {
            senderPlayer.teleport(targetPlayer.getLocation());
        } else {
            targetPlayer.teleport(senderPlayer.getLocation());
        }

        senderPlayer.sendMessage(getMessageManager().getAndFormatMsg(
                PluginMessage.TPA_ACCEPTED_SENDER,
                new MessageManager.Replaceable<>("{player}", targetPlayer.getName())
        ));

        targetPlayer.sendMessage(getMessageManager().getAndFormatMsg(
                PluginMessage.TPA_ACCEPTED_TARGET,
                new MessageManager.Replaceable<>("{player}", senderPlayer.getName())
        ));
    }

    @Override
    public List<String> tabComplete(CommandSender sender, Command command, ArgumentList args) {
        return List.of();
    }

    private boolean isBedrockPlayer(Player player) {
        try {
            return org.geysermc.floodgate.api.FloodgateApi.getInstance().isFloodgatePlayer(player.getUniqueId());
        } catch (Exception e) {
            return false;
        }
    }
}
