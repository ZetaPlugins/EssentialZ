package com.zetaplugins.essentialz.util.commands;

public class CommandUsageException extends Exception {
    /**
     * @param usage The correct usage of the command
     */
    public CommandUsageException(String usage) {
        super(usage);
    }

    /**
     * @return The correct usage of the command
     */
    public String getUsage() {
        return getMessage();
    }
}
