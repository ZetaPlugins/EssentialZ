package org.strassburger.essentialz.util.commands;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.strassburger.essentialz.EssentialZ;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Represents a list of command arguments
 */
public class ArgumentList implements Iterable<String> {
    private final String[] args;

    /**
     * Constructor for the ArgumentList class
     * @param args The list of arguments
     */
    public ArgumentList(String[] args) {
        this.args = args;
    }

    /**
     * Get the argument at the specified index
     * @param index The index of the argument
     * @return The argument at the specified index
     */
    public boolean hasArg(int index) {
        return args.length > index;
    }

    /**
     * Get the argument at the specified index
     * @param index The index of the argument
     * @return The argument at the specified index
     */
    public String getArg(int index) {
        if (hasArg(index)) return args[index];
        else return null;
    }

    /**
     * Get the player at the specified index
     * @param index The index of the player
     * @param plugin The instance of the plugin
     * @return The player at the specified index
     */
    public Player getPlayer(int index, EssentialZ plugin) {
        if (!hasArg(index)) return null;
        return plugin.getServer().getPlayer(args[index]);
    }

    /**
     * Get an integer at the specified index
     * @param index The index of the integer
     * @return The integer at the specified index
     * @throws NumberFormatException When the argument is not an integer
     */
    public int getInt(int index) throws NumberFormatException {
        if (!hasArg(index)) throw new NumberFormatException();
        return Integer.parseInt(args[index]);
    }

    /**
     * Get the last argument index
     * @return The last argument index
     */
    public int getCurrentArgIndex() {
        return args.length - 1;
    }

    /**
     * Get the last argument
     * @return The last argument
     */
    public String getCurrentArg() {
        return args[getCurrentArgIndex()];
    }

    /**
     * Get the number of arguments
     * @return The number of arguments
     */
    public int size() {
        return args.length;
    }

    /**
     * Get the arguments
     * @return The arguments
     */
    public String[] getArgs() {
        return args;
    }

    /**
     * Get the arguments as a list
     * @return The arguments as a list
     */
    public List<String> getAllArgs() {
        return List.of(args);
    }

    @Override
    public @NotNull Iterator<String> iterator() {
        return Arrays.asList(args).iterator();
    }
}
