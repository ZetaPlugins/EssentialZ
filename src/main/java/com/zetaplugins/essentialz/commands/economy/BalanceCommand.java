package com.zetaplugins.essentialz.commands.economy;

import com.zetaplugins.essentialz.EssentialZ;
import com.zetaplugins.essentialz.features.economy.EconomyConfig;
import com.zetaplugins.essentialz.features.economy.EconomyUtil;
import com.zetaplugins.essentialz.features.economy.manager.EconomyManager;
import com.zetaplugins.essentialz.util.EszConfig;
import com.zetaplugins.essentialz.util.MessageManager;
import com.zetaplugins.essentialz.util.PluginMessage;
import com.zetaplugins.essentialz.util.commands.EszCommand;
import com.zetaplugins.essentialz.util.permissions.Permission;
import com.zetaplugins.zetacore.annotations.AutoRegisterCommand;
import com.zetaplugins.zetacore.annotations.InjectManager;
import com.zetaplugins.zetacore.commands.ArgumentList;
import com.zetaplugins.zetacore.commands.exceptions.CommandException;
import com.zetaplugins.zetacore.commands.exceptions.CommandPermissionException;
import com.zetaplugins.zetacore.commands.exceptions.CommandSenderMustBeOrSpecifyPlayerException;
import com.zetaplugins.zetacore.services.config.ConfigService;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

@AutoRegisterCommand(
        commands = "balance",
        description = "Check your balance or another player's balance or modify it if you have permission.",
        usage = "/balance [player] <get|set|add|remove> <amount>",
        permission = "essentialz.balance",
        aliases = { "bal", "money" }
)
public class BalanceCommand extends EszCommand {

    @InjectManager
    private EconomyManager economyManager;

    @InjectManager
    private ConfigService configService;

    public BalanceCommand(EssentialZ plugin) {
        super(plugin);
    }

    @Override
    public boolean execute(CommandSender sender, Command command, String s, ArgumentList args) throws CommandException {
        Player targetPlayer = args.getPlayer(0, getPlugin());
        Action action = args.getEnumIgnoreCase(1, Action.class, Action.GET);
        String amountString = args.getString(2, "0");
        try {
            double amount = EconomyUtil.parseNumber(amountString, new EconomyConfig(configService.getConfig(EszConfig.ECONOMY)));

            boolean isOtherPlayer = targetPlayer != null && !(sender instanceof Player player && player.getUniqueId().equals(targetPlayer.getUniqueId()));

            if (isOtherPlayer && action.equals(Action.GET) && !Permission.BALANCE_OTHERS.has(sender)) {
                throw new CommandPermissionException(Permission.BALANCE_OTHERS.getNode());
            }

            if (!isOtherPlayer && !(sender instanceof Player)) throw new CommandSenderMustBeOrSpecifyPlayerException();

            Player player = isOtherPlayer ? targetPlayer : (Player) sender;

            switch (action) {
                case GET -> {
                    double balance = economyManager.getBalance(player);
                    if (isOtherPlayer) {
                        sender.sendMessage(getMessageManager().getAndFormatMsg(
                                PluginMessage.OTHER_PLAYER_BALANCE,
                                new MessageManager.Replaceable<>("{player}", targetPlayer.getName()),
                                new MessageManager.Replaceable<>("{balance}", economyManager.format(balance))
                        ));
                    } else {
                        player.sendMessage(getMessageManager().getAndFormatMsg(
                                PluginMessage.YOUR_BALANCE,
                                new MessageManager.Replaceable<>("{balance}", economyManager.format(balance))
                        ));
                    }
                    return true;
                }
                case SET -> {
                    if (!Permission.BALANCE_MODIFY.has(sender)) {
                        throw new CommandPermissionException(Permission.BALANCE_MODIFY.getNode());
                    }
                    economyManager.setBalance(player, amount);
                    sender.sendMessage(getMessageManager().getAndFormatMsg(
                            PluginMessage.SET_BALANCE,
                            new MessageManager.Replaceable<>("{player}", player.getName()),
                            new MessageManager.Replaceable<>("{balance}", economyManager.format(amount))
                    ));
                    return true;
                }
                case ADD -> {
                    if (!Permission.BALANCE_MODIFY.has(sender)) {
                        throw new CommandPermissionException(Permission.BALANCE_MODIFY.getNode());
                    }
                    economyManager.deposit(player, amount);
                    sender.sendMessage(getMessageManager().getAndFormatMsg(
                            PluginMessage.ADD_BALANCE,
                            new MessageManager.Replaceable<>("{player}", player.getName()),
                            new MessageManager.Replaceable<>("{amount}", economyManager.format(amount))
                    ));
                    return true;
                }
                case REMOVE -> {
                    if (!Permission.BALANCE_MODIFY.has(sender)) {
                        throw new CommandPermissionException(Permission.BALANCE_MODIFY.getNode());
                    }
                    boolean success = economyManager.withdraw(player, amount);
                    if (success) {
                        sender.sendMessage(getMessageManager().getAndFormatMsg(
                                PluginMessage.REMOVE_BALANCE,
                                new MessageManager.Replaceable<>("{player}", player.getName()),
                                new MessageManager.Replaceable<>("{amount}", economyManager.format(amount))
                        ));
                    } else {
                        sender.sendMessage(getMessageManager().getAndFormatMsg(
                                PluginMessage.INSUFFICIENT_FUNDS_TO_WITHDRAW,
                                new MessageManager.Replaceable<>("{player}", player.getName()),
                                new MessageManager.Replaceable<>("{amount}", economyManager.format(amount))
                        ));
                    }
                }
            }

            return false;
        } catch (NumberFormatException e) {
            sender.sendMessage(getMessageManager().getAndFormatMsg(
                    PluginMessage.INVALID_NUMBER,
                    new MessageManager.Replaceable<>("{number}", amountString)
            ));
            return false;
        }
    }

    @Override
    public List<String> tabComplete(CommandSender sender, Command command, ArgumentList args) {
        if (args.getCurrentArgIndex() == 0
                && (Permission.BALANCE_OTHERS.has(sender) || Permission.BALANCE_MODIFY.has(sender))) {
            return getPlayerOptions(args.getCurrentArg());
        } else if (args.getCurrentArgIndex() == 1) {
            List<String> actions = null;
            if (!Permission.BALANCE_MODIFY.has(sender)) actions = List.of(Action.GET.name().toLowerCase());
            else actions = Arrays.stream(Action.values())
                    .map(Enum::name)
                    .map(String::toLowerCase)
                    .toList();
            return getDisplayOptions(actions, args.getCurrentArg());
        } else if (args.getCurrentArgIndex() == 2) {
            Action action = args.getEnum(1, Action.class, null);
            if (action != null && action != Action.GET) {
                return List.of("<amount>");
            }
        }
        return List.of();
    }

    private enum Action {
        GET,
        SET,
        ADD,
        REMOVE
    }
}
