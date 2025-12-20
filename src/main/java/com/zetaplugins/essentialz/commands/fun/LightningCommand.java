package com.zetaplugins.essentialz.commands.fun;

import com.zetaplugins.essentialz.EssentialZ;
import com.zetaplugins.essentialz.util.MessageManager;
import com.zetaplugins.essentialz.util.PluginMessage;
import com.zetaplugins.essentialz.util.commands.EszCommand;
import com.zetaplugins.essentialz.util.permissions.Permission;
import com.zetaplugins.zetacore.annotations.AutoRegisterCommand;
import com.zetaplugins.zetacore.commands.ArgumentList;
import com.zetaplugins.zetacore.commands.exceptions.CommandException;
import com.zetaplugins.zetacore.commands.exceptions.CommandSenderMustBeOrSpecifyPlayerException;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

@AutoRegisterCommand(
        commands = "lightning",
        description = "Strike a player with lightning.",
        usage = "/lightning <player> [amount]",
        permission = "essentialz.command.lightning"
)
public class LightningCommand extends EszCommand {
    private static final int MAX_LIGHTNING_STRIKES = 1000;

    public LightningCommand(EssentialZ plugin) {
        super(plugin);
    }

    @Override
    public boolean execute(CommandSender commandSender, Command command, String s, ArgumentList args) throws CommandException {
        Player targetPlayer = args.getPlayer(0, getPlugin());
        if (targetPlayer == null && commandSender instanceof Player) targetPlayer = (Player) commandSender;
        else if (targetPlayer == null) throw new CommandSenderMustBeOrSpecifyPlayerException();

        int amount = args.getInt(1, 1);
        if (amount < 1 || amount > MAX_LIGHTNING_STRIKES) {
            commandSender.sendMessage(getMessageManager().getAndFormatMsg(
                    PluginMessage.INVALID_LIGHTNING_AMOUNT,
                    new MessageManager.Replaceable<>("{max}", String.valueOf(MAX_LIGHTNING_STRIKES))
            ));
            return true;
        }
        for (int i = 0; i < amount; i++) targetPlayer.getWorld().strikeLightning(targetPlayer.getLocation());

        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender commandSender, Command command, ArgumentList args) {
        if (args.getCurrentArgIndex() == 0 && Permission.LIGHTNING.has(commandSender)) {
            return getPlayerOptions(args.getCurrentArg());
        } else if (args.getCurrentArgIndex() == 1 && Permission.LIGHTNING.has(commandSender)) {
            return List.of("1", "2", "3", "4", "5");
        }
        return List.of();
    }
}
