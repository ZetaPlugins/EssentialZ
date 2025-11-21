package com.zetaplugins.essentialz.commands.communication;

import com.zetaplugins.essentialz.EssentialZ;
import com.zetaplugins.essentialz.util.MessageManager;
import com.zetaplugins.essentialz.util.commands.EszCommand;
import com.zetaplugins.essentialz.util.permissions.Permission;
import com.zetaplugins.zetacore.annotations.AutoRegisterCommand;
import com.zetaplugins.zetacore.commands.ArgumentList;
import com.zetaplugins.zetacore.commands.exceptions.CommandSenderMustBePlayerException;
import com.zetaplugins.zetacore.commands.exceptions.CommandUsageException;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

@AutoRegisterCommand(
        commands = "ignore",
        description = "Ignore messages from a specific player.",
        usage = "/ignore <player>",
        permission = "essentialz.ignore"
)
public class IgnoreCommand extends EszCommand {

    public IgnoreCommand(EssentialZ plugin) {
        super(plugin);
    }

    @Override
    public boolean execute(CommandSender sender, Command command, String label, ArgumentList args) throws CommandSenderMustBePlayerException, CommandUsageException {
        if (!(sender instanceof Player player)) throw new CommandSenderMustBePlayerException();

        Player targetPlayer = args.getPlayer(0, getPlugin());
        if (targetPlayer == null) throw new CommandUsageException("/ignore <player>");

        if (player.getUniqueId().equals(targetPlayer.getUniqueId())) {
            sender.sendMessage(getPlugin().getMessageManager().getAndFormatMsg(
                    MessageManager.Style.ERROR,
                    "cannotIgnoreYourself",
                    "{ac}You cannot ignore yourself."
            ));
            return false;
        }

        boolean isIgnoringNow = getPlugin().getStorage().togglePlayerIgnore(player.getUniqueId(), targetPlayer.getUniqueId());
        sender.sendMessage(getPlugin().getMessageManager().getAndFormatMsg(
                MessageManager.Style.SUCCESS,
                isIgnoringNow ? "ignoreAdded" : "ignoreRemoved",
                isIgnoringNow ? "&7You are now ignoring {ac}{player}&7." : "&7You have unignored {ac}{player}&7.",
                new MessageManager.Replaceable<>("{player}", targetPlayer.getName())
        ));
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, Command command, ArgumentList args) {
        if (args.getCurrentArgIndex() == 0) {
            return getPlayerOptions(args.getCurrentArg());
        }
        return List.of();
    }
}
