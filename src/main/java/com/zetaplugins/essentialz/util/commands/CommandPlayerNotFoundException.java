package com.zetaplugins.essentialz.util.commands;

import com.zetaplugins.zetacore.commands.exceptions.CommandException;

public class CommandPlayerNotFoundException extends CommandException {
    public CommandPlayerNotFoundException() {
        super("Player not found");
    }
}
