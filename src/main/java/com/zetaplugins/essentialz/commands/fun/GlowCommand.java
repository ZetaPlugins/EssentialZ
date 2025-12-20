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
        commands = "glow",
        description = "Make yourself or another player glow.",
        usage = "/glow [player]",
        permission = "essentialz.glow"
)
public class GlowCommand extends EszCommand {

    public GlowCommand(EssentialZ plugin) {
        super(plugin);
    }

    @Override
    public boolean execute(CommandSender commandSender, Command command, String s, ArgumentList args) throws CommandException {
        Player targetPlayer = args.getPlayer(0, getPlugin());
        if (targetPlayer == null && commandSender instanceof Player) targetPlayer = (Player) commandSender;
        else if (targetPlayer == null) throw new CommandSenderMustBeOrSpecifyPlayerException();

        boolean newGlowState = !targetPlayer.isGlowing();
        targetPlayer.setGlowing(newGlowState);

        if (newGlowState) {
            commandSender.sendMessage(getMessageManager().getAndFormatMsg(
                    PluginMessage.GLOW_ENABLED,
                    new MessageManager.Replaceable<>("{player}", targetPlayer.getName())
            ));
        } else {
            commandSender.sendMessage(getMessageManager().getAndFormatMsg(
                    PluginMessage.GLOW_DISABLED,
                    new MessageManager.Replaceable<>("{player}", targetPlayer.getName())
            ));
        }

        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender commandSender, Command command, ArgumentList args) {
        if (args.getCurrentArgIndex() == 0 && Permission.GLOW.has(commandSender)) {
            return getPlayerOptions(args.getCurrentArg());
        }
        return List.of();
    }
}
