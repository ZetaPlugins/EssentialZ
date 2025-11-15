package com.zetaplugins.essentialz.util.commands;

public class CommandPermissionException extends RuntimeException {
    /**
     * @param permission The permission that is required to execute the command
     */
    public CommandPermissionException(String permission) {
        super(permission);
    }

    /**
     * @return The permission that is required to execute the command
     */
    public String getPermission() {
        return getMessage();
    }
}
