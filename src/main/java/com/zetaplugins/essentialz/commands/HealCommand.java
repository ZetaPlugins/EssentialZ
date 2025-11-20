package com.zetaplugins.essentialz.commands;

import com.zetaplugins.essentialz.EssentialZ;
import com.zetaplugins.essentialz.util.MessageManager;
import com.zetaplugins.essentialz.util.commands.ArgumentList;
import com.zetaplugins.essentialz.util.commands.CommandPermissionException;
import com.zetaplugins.essentialz.util.commands.CommandUsageException;
import com.zetaplugins.essentialz.util.commands.CustomCommand;
import com.zetaplugins.essentialz.util.permissions.Permission;
import com.zetaplugins.zetacore.annotations.AutoRegisterCommand;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Objects;

@AutoRegisterCommand(
        commands = "heal",
        description = "Heal a player to full health",
        usage = "/heal [player]",
        permission = "essentialz.heal"
)
public class HealCommand extends CustomCommand {

    public HealCommand(EssentialZ plugin) {
        super(plugin);
    }

    @Override
    public boolean execute(CommandSender sender, Command command, ArgumentList args) throws CommandPermissionException, CommandUsageException {
        Player targetPlayer = args.getPlayer(0, sender instanceof Player ? (Player) sender : null, getPlugin());

        if (targetPlayer == null) {
            sender.sendMessage(getPlugin().getMessageManager().getAndFormatMsg(
                    MessageManager.Style.ERROR,
                    "specifyPlayerOrBePlayer",
                    "{ac}You must specify a player or be a player to use this command."
            ));
            return false;
        }

        boolean isSelfHeal = (sender instanceof Player && ((Player) sender).getUniqueId().equals(targetPlayer.getUniqueId()));

        if (!isSelfHeal && !Permission.HEAL_OTHERS.has(sender)) {
            sender.sendMessage(getPlugin().getMessageManager().getAndFormatMsg(
                    MessageManager.Style.ERROR,
                    "noPermissionHealOthers",
                    "{ac}You do not have permission to heal other players."
            ));
            return false;
        }

        double maxHealth = targetPlayer.getAttribute(Attribute.GENERIC_MAX_HEALTH) != null
                ? Objects.requireNonNull(targetPlayer.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getDefaultValue()
                : 20.0;
        targetPlayer.setHealth(maxHealth);

        if (isSelfHeal) {
            sender.sendMessage(getPlugin().getMessageManager().getAndFormatMsg(
                    MessageManager.Style.SUCCESS,
                    "healedSelf",
                    "&7You have been healed to full health."
            ));
        } else {
            sender.sendMessage(getPlugin().getMessageManager().getAndFormatMsg(
                    MessageManager.Style.SUCCESS,
                    "healedOther",
                    "&7You have healed {ac}{player}&7 to full health.",
                    new MessageManager.Replaceable<>("{player}", targetPlayer.getName())
            ));
        }

        return true;
    }

    @Override
    public boolean isAuthorized(CommandSender sender) {
        return Permission.HEAL.has(sender);
    }

    @Override
    public List<String> tabComplete(CommandSender sender, Command command, ArgumentList args) {
        if (args.getCurrentArgIndex() == 0 && Permission.HEAL_OTHERS.has(sender)) {
            return getPlayerOptions(getPlugin(), args.getCurrentArg());
        }
        return List.of();
    }
}
