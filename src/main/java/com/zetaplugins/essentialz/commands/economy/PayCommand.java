package com.zetaplugins.essentialz.commands.economy;

import com.zetaplugins.essentialz.EssentialZ;
import com.zetaplugins.essentialz.config.economy.EconomyConfig;
import com.zetaplugins.essentialz.features.economy.EconomyUtil;
import com.zetaplugins.essentialz.features.economy.manager.EconomyManager;
import com.zetaplugins.essentialz.util.EszConfig;
import com.zetaplugins.essentialz.util.MessageManager;
import com.zetaplugins.essentialz.util.PluginMessage;
import com.zetaplugins.essentialz.util.commands.EszCommand;
import com.zetaplugins.zetacore.annotations.AutoRegisterCommand;
import com.zetaplugins.zetacore.annotations.InjectManager;
import com.zetaplugins.zetacore.commands.ArgumentList;
import com.zetaplugins.zetacore.commands.exceptions.CommandException;
import com.zetaplugins.zetacore.commands.exceptions.CommandSenderMustBePlayerException;
import com.zetaplugins.zetacore.services.config.ConfigService;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

@AutoRegisterCommand(
        commands = "pay",
        description = "Pay another player money",
        usage = "/pay <player> <amount>",
        permission = "essentialz.pay"
)
public class PayCommand extends EszCommand {

    @InjectManager
    private EconomyManager economyManager;

    @InjectManager
    private MessageManager messageManager;

    @InjectManager
    private ConfigService configService;

    public PayCommand(EssentialZ plugin) {
        super(plugin);
    }

    @Override
    public boolean execute(CommandSender sender, Command command, String s, ArgumentList args) throws CommandException {
        if (!(sender instanceof Player player)) throw new CommandSenderMustBePlayerException();

        Player targetPlayer = args.getPlayer(0, getPlugin());
        String stringAamount = args.getArg(1);
        double amount = EconomyUtil.parseNumber(
                stringAamount,
                configService.getConfig(EconomyConfig.class).getCurrencyFormat()
        );

        double senderBalance = economyManager.getBalance(player);
        if (senderBalance < 0) throw new RuntimeException("Sender balance is negative!");
        if (senderBalance < amount) {
            sender.sendMessage(messageManager.getAndFormatMsg(PluginMessage.INSUFFICIENT_FUNDS));
            return false;
        }

        economyManager.withdraw(player, amount);
        economyManager.deposit(targetPlayer, amount);

        sender.sendMessage(messageManager.getAndFormatMsg(
                PluginMessage.PAY_SENDER,
                new MessageManager.Replaceable<>("{player}", targetPlayer.getName()),
                new MessageManager.Replaceable<>("{amount}", economyManager.format(amount))
        ));
        targetPlayer.sendMessage(messageManager.getAndFormatMsg(
                PluginMessage.PAY_RECEIVER,
                new MessageManager.Replaceable<>("{player}", player.getName()),
                new MessageManager.Replaceable<>("{amount}", economyManager.format(amount))
        ));
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, Command command, ArgumentList args) {
        if (args.getCurrentArgIndex() == 0) {
            return getPlayerOptions(args.getCurrentArg());
        } else if (args.getCurrentArgIndex() == 1) {
            return getDisplayOptions(List.of("<amount>"), args.getCurrentArg());
        } else {
            return List.of();
        }
    }
}
