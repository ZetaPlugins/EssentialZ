package com.zetaplugins.essentialz.util.commands;

import com.zetaplugins.essentialz.EssentialZ;
import com.zetaplugins.essentialz.util.MessageManager;
import com.zetaplugins.essentialz.util.PluginMessage;
import com.zetaplugins.zetacore.annotations.InjectManager;
import com.zetaplugins.zetacore.commands.PluginCommand;
import com.zetaplugins.zetacore.commands.exceptions.CommandPermissionException;
import com.zetaplugins.zetacore.commands.exceptions.CommandSenderMustBeOrSpecifyPlayerException;
import com.zetaplugins.zetacore.commands.exceptions.CommandSenderMustBePlayerException;
import com.zetaplugins.zetacore.commands.exceptions.CommandUsageException;

public abstract class EszCommand extends PluginCommand<EssentialZ> {

    @InjectManager
    private MessageManager messageManager;

    public EszCommand(EssentialZ plugin) {
        super(plugin);

        registerExceptionHandler(
                CommandUsageException.class,
                (ctx, e) -> {
                    ctx.getSender().sendMessage(messageManager.getAndFormatMsg(PluginMessage.USAGE_ERROR));
                    return false;
                }
        );

        registerExceptionHandler(
                CommandPermissionException.class,
                (ctx, e) -> {
                    ctx.getSender().sendMessage(messageManager.getAndFormatMsg(PluginMessage.NO_PERMS_ERROR));
                    return false;
                }
        );

        registerExceptionHandler(
                CommandSenderMustBePlayerException.class,
                (ctx, e) -> {
                    ctx.getSender().sendMessage(messageManager.getAndFormatMsg(PluginMessage.PLAYER_ONLY));
                    return false;
                }
        );

        registerExceptionHandler(
                CommandSenderMustBeOrSpecifyPlayerException.class,
                (ctx, e) -> {
                    ctx.getSender().sendMessage(messageManager.getAndFormatMsg(PluginMessage.SPECIFY_PLAYER_OR_BE_PLAYER));
                    return false;
                }
        );

        registerExceptionHandler(
                CommandPlayerNotFoundException.class,
                (ctx, e) -> {
                    ctx.getSender().sendMessage(messageManager.getAndFormatMsg(PluginMessage.PLAYER_NOT_FOUND));
                    return false;
                }
        );
    }

    protected MessageManager getMessageManager() {
        return messageManager;
    }

}
