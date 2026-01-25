package com.zetaplugins.essentialz.features.tpa;

import com.zetaplugins.essentialz.EssentialZ;
import com.zetaplugins.essentialz.util.MessageManager;
import com.zetaplugins.essentialz.util.PluginMessage;
import com.zetaplugins.zetacore.annotations.InjectManager;
import com.zetaplugins.zetacore.annotations.Manager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.form.ModalForm;
import org.geysermc.cumulus.form.SimpleForm;
import org.geysermc.cumulus.util.FormImage;
import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.floodgate.api.player.FloodgatePlayer;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles Bedrock forms for TPA functionality.
 */
@Manager
public class TpaBedrockFormHandler {

    private final EssentialZ plugin;

    @InjectManager
    private TpaManager tpaManager;

    @InjectManager
    private TpaToggleManager toggleManager;

    @InjectManager
    private MessageManager messageManager;

    public TpaBedrockFormHandler(EssentialZ plugin) {
        this.plugin = plugin;
    }

    /**
     * Sends a player selection form for TPA.
     * @param sender The player sending the request
     * @param type The type of TPA request
     */
    public void sendPlayerSelectionForm(Player sender, TpaRequestType type) {
        FloodgatePlayer floodgatePlayer = FloodgateApi.getInstance().getPlayer(sender.getUniqueId());
        if (floodgatePlayer == null) return;

        boolean showHeads = plugin.getConfig().getBoolean("tpa.bedrock.showPlayerHeads", true);

        SimpleForm.Builder builder = SimpleForm.builder()
                .title(type == TpaRequestType.TPA ? "Teleport To Player" : "Teleport Player Here")
                .content("Select a player:");

        List<Player> availablePlayers = new ArrayList<>();

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.equals(sender)) continue;
            if (!toggleManager.isEnabled(player.getUniqueId())) continue;

            availablePlayers.add(player);

            String buttonText = player.getName();
            if (showHeads) {
                builder.button(buttonText, FormImage.Type.URL, "https://mc-heads.net/avatar/" + player.getName() + "/64");
            } else {
                builder.button(buttonText);
            }
        }

        if (availablePlayers.isEmpty()) {
            builder.content("No players available for teleportation.");
        }

        builder.validResultHandler(response -> {
            int index = response.clickedButtonId();
            if (index < 0 || index >= availablePlayers.size()) return;

            Player target = availablePlayers.get(index);
            if (type == TpaRequestType.TPA) {
                sendTpaConfirmationForm(sender, target);
            } else {
                sendTpaRequest(sender, target, TpaRequestType.TPA_HERE);
            }
        });

        floodgatePlayer.sendForm(builder);
    }

    private void sendTpaConfirmationForm(Player sender, Player target) {
        FloodgatePlayer floodgatePlayer = FloodgateApi.getInstance().getPlayer(sender.getUniqueId());
        if (floodgatePlayer == null) return;

        ModalForm form = ModalForm.builder()
                .title("Teleport Options")
                .content("Choose teleport type for " + target.getName())
                .button1("Teleport to " + target.getName())
                .button2("Teleport " + target.getName() + " here")
                .validResultHandler(response -> {
                    if (response.clickedButtonId() == 0) {
                        sendTpaRequest(sender, target, TpaRequestType.TPA);
                    } else {
                        sendTpaRequest(sender, target, TpaRequestType.TPA_HERE);
                    }
                })
                .build();

        floodgatePlayer.sendForm(form);
    }

    /**
     * Sends a request notification form to a Bedrock player.
     * @param recipient The player receiving the request
     * @param sender The player who sent the request
     * @param type The type of request
     */
    public void sendRequestNotification(Player recipient, Player sender, TpaRequestType type) {
        FloodgatePlayer floodgatePlayer = FloodgateApi.getInstance().getPlayer(recipient.getUniqueId());
        if (floodgatePlayer == null) return;

        String content = type == TpaRequestType.TPA
                ? sender.getName() + " wants to teleport to you."
                : sender.getName() + " wants to teleport you to them.";

        long startTime = System.currentTimeMillis();

        ModalForm.Builder builder = ModalForm.builder()
                .title("Teleport Request")
                .content(content)
                .button1("Accept")
                .button2("Deny");

        builder.validResultHandler(response -> {
            TeleportRequest request = tpaManager.getLatestRequest(recipient.getUniqueId());
            if (request == null || !request.getSender().equals(sender.getUniqueId())) {
                recipient.sendMessage(messageManager.getAndFormatMsg(PluginMessage.TPA_NO_PENDING_REQUESTS));
                return;
            }

            if (response.clickedButtonId() == 0) {
                acceptRequest(recipient, request);
            } else {
                denyRequest(recipient, request);
            }

            tpaManager.removeRequest(request);
        });

        builder.closedResultHandler(() -> retryRequestForm(recipient, sender, type, startTime));
        builder.invalidResultHandler(() -> retryRequestForm(recipient, sender, type, startTime));

        floodgatePlayer.sendForm(builder.build());
    }

    private void retryRequestForm(Player recipient, Player sender, TpaRequestType type, long startTime) {
        if ((System.currentTimeMillis() - startTime) < 30000) {
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                if (recipient.isOnline() && sender.isOnline()) {
                    sendRequestNotification(recipient, sender, type);
                }
            }, 20L);
        }
    }

    /**
     * Sends a form showing all pending requests.
     * @param player The player viewing requests
     */
    public void sendPendingRequestsForm(Player player) {
        FloodgatePlayer floodgatePlayer = FloodgateApi.getInstance().getPlayer(player.getUniqueId());
        if (floodgatePlayer == null) return;

        List<TeleportRequest> requests = tpaManager.getIncomingRequests(player.getUniqueId());

        if (requests.isEmpty()) {
            player.sendMessage(messageManager.getAndFormatMsg(PluginMessage.TPA_NO_PENDING_REQUESTS));
            return;
        }

        SimpleForm.Builder builder = SimpleForm.builder()
                .title("Pending Teleport Requests")
                .content("Select a request to manage:");

        for (TeleportRequest request : requests) {
            Player sender = Bukkit.getPlayer(request.getSender());
            if (sender != null) {
                String typeStr = request.getType() == TpaRequestType.TPA ? "to you" : "you to them";
                builder.button(sender.getName() + " - Teleport " + typeStr);
            }
        }

        builder.validResultHandler(response -> {
            int index = response.clickedButtonId();
            if (index < 0 || index >= requests.size()) return;

            TeleportRequest request = requests.get(index);
            sendRequestManagementForm(player, request);
        });

        floodgatePlayer.sendForm(builder);
    }

    /**
     * Sends a form for managing a specific request.
     * @param player The player managing the request
     * @param request The request to manage
     */
    public void sendRequestManagementForm(Player player, TeleportRequest request) {
        FloodgatePlayer floodgatePlayer = FloodgateApi.getInstance().getPlayer(player.getUniqueId());
        if (floodgatePlayer == null) return;

        Player sender = Bukkit.getPlayer(request.getSender());
        if (sender == null) {
            player.sendMessage(messageManager.getAndFormatMsg(PluginMessage.PLAYER_NOT_FOUND));
            return;
        }

        String content = "Request from " + sender.getName() + "\n"
                + "Type: " + (request.getType() == TpaRequestType.TPA
                ? "Teleport to you" : "Teleport you to them");

        long startTime = System.currentTimeMillis();

        ModalForm.Builder builder = ModalForm.builder()
                .title("Manage Request")
                .content(content)
                .button1("Accept")
                .button2("Deny");

        builder.validResultHandler(response -> {
            if (response.clickedButtonId() == 0) {
                acceptRequest(player, request);
            } else {
                denyRequest(player, request);
            }
            tpaManager.removeRequest(request);
        });

        builder.closedResultHandler(() -> retryManagementForm(player, request, startTime));
        builder.invalidResultHandler(() -> retryManagementForm(player, request, startTime));

        floodgatePlayer.sendForm(builder.build());
    }

    private void retryManagementForm(Player player, TeleportRequest request, long startTime) {
        if ((System.currentTimeMillis() - startTime) < 30000) {
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                if (player.isOnline() && !request.isExpired()) {
                    sendRequestManagementForm(player, request);
                }
            }, 20L);
        }
    }

    private void sendTpaRequest(Player sender, Player target, TpaRequestType type) {
        if (!toggleManager.isEnabled(target.getUniqueId())) {
            sender.sendMessage(messageManager.getAndFormatMsg(
                    PluginMessage.TPA_DISABLED_TARGET,
                    new MessageManager.Replaceable<>("{player}", target.getName())
            ));
            return;
        }

        TeleportRequest request = tpaManager.createRequest(
                sender.getUniqueId(),
                target.getUniqueId(),
                type
        );

        if (request == null) {
            sender.sendMessage(messageManager.getAndFormatMsg(PluginMessage.TPA_REQUEST_ALREADY_PENDING));
            return;
        }

        sender.sendMessage(messageManager.getAndFormatMsg(
                PluginMessage.TPA_REQUEST_SENT,
                new MessageManager.Replaceable<>("{player}", target.getName())
        ));

        if (FloodgateApi.getInstance().isFloodgatePlayer(target.getUniqueId())) {
            sendRequestNotification(target, sender, type);
        } else {
            String typeDescription = type == TpaRequestType.TPA ? "teleport to you" : "teleport you to them";
            target.sendMessage(messageManager.getAndFormatMsg(
                    PluginMessage.TPA_REQUEST_RECEIVED,
                    new MessageManager.Replaceable<>("{player}", sender.getName()),
                    new MessageManager.Replaceable<>("{type}", typeDescription)
            ));
            target.sendMessage(messageManager.getAndFormatMsg(PluginMessage.TPA_REQUEST_INSTRUCTIONS, false));
        }

        TpaUtils.playSound(target, plugin.getConfig().getString("tpa.sounds.request", "ENTITY_EXPERIENCE_ORB_PICKUP"), plugin);
    }

    private void acceptRequest(Player acceptor, TeleportRequest request) {
        Player senderPlayer = request.getSenderPlayer();
        Player targetPlayer = request.getTargetPlayer();

        if (senderPlayer == null || !senderPlayer.isOnline()) {
            acceptor.sendMessage(messageManager.getAndFormatMsg(PluginMessage.PLAYER_NOT_FOUND));
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

        senderPlayer.sendMessage(messageManager.getAndFormatMsg(
                PluginMessage.TPA_ACCEPTED_SENDER,
                new MessageManager.Replaceable<>("{player}", targetPlayer.getName())
        ));

        targetPlayer.sendMessage(messageManager.getAndFormatMsg(
                PluginMessage.TPA_ACCEPTED_TARGET,
                new MessageManager.Replaceable<>("{player}", senderPlayer.getName())
        ));

        TpaUtils.playSound(acceptor, plugin.getConfig().getString("tpa.sounds.accept", "ENTITY_PLAYER_LEVELUP"), plugin);
    }

    private void denyRequest(Player denier, TeleportRequest request) {
        Player senderPlayer = request.getSenderPlayer();

        if (senderPlayer != null && senderPlayer.isOnline()) {
            senderPlayer.sendMessage(messageManager.getAndFormatMsg(
                    PluginMessage.TPA_DENIED_SENDER,
                    new MessageManager.Replaceable<>("{player}", denier.getName())
            ));
        }

        denier.sendMessage(messageManager.getAndFormatMsg(PluginMessage.TPA_DENIED_TARGET));
        TpaUtils.playSound(denier, plugin.getConfig().getString("tpa.sounds.deny", "ENTITY_VILLAGER_NO"), plugin);
    }
}
