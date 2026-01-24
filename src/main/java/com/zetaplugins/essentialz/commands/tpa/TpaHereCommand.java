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
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@AutoRegisterCommand(
        commands = "tpahere",
        description = "Request another player to teleport to you",
        usage = "/tpahere <player>",
        permission = "essentialz.tpahere"
)
public class TpaHereCommand extends EszCommand {

    @InjectManager
    private TpaManager tpaManager;

    @InjectManager
    private TpaToggleManager toggleManager;

    private TpaBedrockFormHandler bedrockFormHandler;
    private boolean floodgateEnabled = false;

    public TpaHereCommand(EssentialZ plugin) {
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

    private TpaBedrockFormHandler getBedrockFormHandler() {
        if (bedrockFormHandler == null && floodgateEnabled) {
            bedrockFormHandler = new TpaBedrockFormHandler(getPlugin(), tpaManager, toggleManager, getMessageManager());
        }
        return bedrockFormHandler;
    }

    @Override
    public boolean execute(CommandSender sender, Command command, String label, ArgumentList args) throws CommandSenderMustBePlayerException {
        if (!(sender instanceof Player player)) {
            throw new CommandSenderMustBePlayerException();
        }

        Player target = args.getPlayer(0, getPlugin());

        // Bedrock player with no args - show form
        if (target == null && floodgateEnabled && isBedrockPlayer(player)) {
            getBedrockFormHandler().sendPlayerSelectionForm(player, TpaRequestType.TPA_HERE);
            return true;
        }

        if (target == null) {
            // Try partial match with raw argument
            String rawArg = args.getJoinedString(0);
            if (rawArg.isEmpty()) {
                sender.sendMessage(getMessageManager().getAndFormatMsg(
                        PluginMessage.USAGE_ERROR,
                        new MessageManager.Replaceable<>("{usage}", "/tpahere <player>")
                ));
                return true;
            }
            target = TpaUtils.findPlayer(rawArg);
        }
        if (target == null) {
            sender.sendMessage(getMessageManager().getAndFormatMsg(PluginMessage.PLAYER_NOT_FOUND));
            return true;
        }

        if (target.equals(player)) {
            sender.sendMessage(getMessageManager().getAndFormatMsg(PluginMessage.TPA_CANNOT_SELF));
            return true;
        }

        if (!toggleManager.isEnabled(target.getUniqueId())) {
            sender.sendMessage(getMessageManager().getAndFormatMsg(
                    PluginMessage.TPA_DISABLED_TARGET,
                    new MessageManager.Replaceable<>("{player}", target.getName())
            ));
            return true;
        }

        TeleportRequest request = tpaManager.createRequest(
                player.getUniqueId(),
                target.getUniqueId(),
                TpaRequestType.TPA_HERE
        );

        if (request == null) {
            sender.sendMessage(getMessageManager().getAndFormatMsg(PluginMessage.TPA_REQUEST_ALREADY_PENDING));
            return true;
        }

        sender.sendMessage(getMessageManager().getAndFormatMsg(
                PluginMessage.TPA_REQUEST_SENT,
                new MessageManager.Replaceable<>("{player}", target.getName())
        ));

        if (floodgateEnabled && isBedrockPlayer(target)) {
            getBedrockFormHandler().sendRequestNotification(target, player, TpaRequestType.TPA_HERE);
        } else {
            target.sendMessage(getMessageManager().getAndFormatMsg(
                    PluginMessage.TPA_REQUEST_RECEIVED,
                    new MessageManager.Replaceable<>("{player}", player.getName()),
                    new MessageManager.Replaceable<>("{type}", "teleport you to them")
            ));
            target.sendMessage(getMessageManager().getAndFormatMsg(PluginMessage.TPA_REQUEST_INSTRUCTIONS, false));
        }

        TpaUtils.playSound(target, getPlugin().getConfig().getString("tpa.sounds.request", "ENTITY_EXPERIENCE_ORB_PICKUP"), getPlugin());

        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, Command command, ArgumentList args) {
        if (args.getCurrentArgIndex() == 0) {
            return Bukkit.getOnlinePlayers().stream()
                    .filter(p -> !p.equals(sender))
                    .filter(p -> toggleManager.isEnabled(p.getUniqueId()))
                    .map(Player::getName)
                    .filter(name -> name.toLowerCase().startsWith(args.getCurrentArg().toLowerCase()))
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    private boolean isBedrockPlayer(Player player) {
        try {
            return org.geysermc.floodgate.api.FloodgateApi.getInstance().isFloodgatePlayer(player.getUniqueId());
        } catch (Exception e) {
            return false;
        }
    }
}
