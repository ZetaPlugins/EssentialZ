package com.zetaplugins.essentialz.util.commands;

import com.zetaplugins.zetacore.annotations.InjectManager;
import com.zetaplugins.zetacore.commands.PluginCommand;
import com.zetaplugins.zetacore.commands.exceptions.CommandPermissionException;
import com.zetaplugins.zetacore.commands.exceptions.CommandSenderMustBeOrSpecifyPlayerException;
import com.zetaplugins.zetacore.commands.exceptions.CommandSenderMustBePlayerException;
import com.zetaplugins.zetacore.commands.exceptions.CommandUsageException;
import com.zetaplugins.essentialz.EssentialZ;
import com.zetaplugins.essentialz.util.MessageManager;

public abstract class EszCommand extends PluginCommand<EssentialZ> {

    @InjectManager
    private MessageManager messageManager;

    public EszCommand(EssentialZ plugin) {
        super(plugin);

        registerExceptionHandler(
                CommandUsageException.class,
                (ctx, e) -> {
                    ctx.getSender().sendMessage(messageManager.getAndFormatMsg(
                            com.zetaplugins.essentialz.util.MessageManager.Style.ERROR,
                            "usageError",
                            "{ac}Usage: {usage}",
                            new MessageManager.Replaceable<>("{usage}", e.getUsage())
                    ));
                    return false;
                }
        );

        registerExceptionHandler(
                CommandPermissionException.class,
                (ctx, e) -> {
                    ctx.getSender().sendMessage(messageManager.getAndFormatMsg(
                            com.zetaplugins.essentialz.util.MessageManager.Style.ERROR,
                            "noPermsError",
                            "{ac}You do not have permission to do this!"
                    ));
                    return false;
                }
        );

        registerExceptionHandler(
                CommandSenderMustBePlayerException.class,
                (ctx, e) -> {
                    ctx.getSender().sendMessage(messageManager.getAndFormatMsg(
                            com.zetaplugins.essentialz.util.MessageManager.Style.ERROR,
                            "playerOnly",
                            "{ac}You must be a player to use this command."
                    ));
                    return false;
                }
        );

        registerExceptionHandler(
                CommandSenderMustBeOrSpecifyPlayerException.class,
                (ctx, e) -> {
                    ctx.getSender().sendMessage(messageManager.getAndFormatMsg(
                            MessageManager.Style.ERROR,
                            "specifyPlayerOrBePlayer",
                            "{ac}You must specify a player or be a player to use this command."
                    ));
                    return false;
                }
        );
    }

    protected MessageManager getMessageManager() {
        return messageManager;
    }

}
