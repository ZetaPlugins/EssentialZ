package org.strassburger.essentialz.util.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.strassburger.essentialz.EssentialZ;
import org.strassburger.essentialz.util.MessageManager;

import java.util.List;
import java.util.stream.Collectors;

public abstract class CustomCommand implements CommandExecutor, TabCompleter {
    private final EssentialZ plugin;
    private final String name;

    /**
     * Constructor for the CustomCommand class
     *
     * @param name The name of the command
     * @param plugin The instance of the plugin
     */
    public CustomCommand(String name, EssentialZ plugin) {
        this.plugin = plugin;
        this.name = name;
    }

    protected EssentialZ getPlugin() {
        return plugin;
    }

    /**
     * Get the name of the command
     *
     * @return The name of the command
     */
    public String getName() {
        return name;
    }

    /**
     * Execute the command
     *
     * @param sender The sender of the command
     * @param command The command that was executed
     * @param args The arguments of the command
     * @return Whether the command was executed successfully
     * @throws CommandPermissionException If the sender does not have permission to execute the command
     * @throws CommandUsageException If the command was used incorrectly
     */
    public abstract boolean execute(CommandSender sender, Command command, ArgumentList args) throws CommandPermissionException, CommandUsageException ;

    /**
     * Check if the sender is authorized to execute the command
     *
     * @param sender The sender of the command
     * @return Whether the sender is authorized to execute the command
     */
    public abstract boolean isAuthorized(CommandSender sender);

    /**
     * The Tabcompletion method for the command
     *
     * @param sender The sender of the command
     * @param command The command that is being tab completed
     * @param args The current arguments of the command
     * @return A list of possible completions
     */
    public abstract List<String> tabComplete(CommandSender sender, Command command, ArgumentList args);

    /**
     * Get a list of options that start with the input
     *
     * @param options The list of options
     * @param input The input to check against
     * @return A list of options that start with the input
     */
    public static List<String> getDisplayOptions(List<String> options, String input) {
        return options.stream()
                .filter(option -> startsWithIgnoreCase(option, input))
                .collect(Collectors.toList());
    }

    /**
     * Get a list of player options
     *
     * @param plugin The instance of the plugin
     * @return A list of player options
     */
    public static List<String> getPlayerOptions(EssentialZ plugin, String input) {
        return getDisplayOptions(
                plugin.getServer().getOnlinePlayers()
                        .parallelStream()
                        .map(Player::getName)
                        .collect(Collectors.toList()),
                input
        );
    }

    private static boolean startsWithIgnoreCase(String str, String prefix) {
        return str.regionMatches(true, 0, prefix, 0, prefix.length());
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!isAuthorized(commandSender)) {
            commandSender.sendMessage(plugin.getMessageManager().getAndFormatMsg(
                    MessageManager.Style.ERROR,
                    "noCommandPermsError",
                    "{ac}You do not have permission to execute this command!"
            ));
            return false;
        }

        try {
            return execute(commandSender, command, new ArgumentList(args));
        } catch (CommandUsageException e) {
            commandSender.sendMessage(plugin.getMessageManager().getAndFormatMsg(
                    MessageManager.Style.ERROR,
                    "usageError",
                    "{ac}Usage: {usage}",
                    new MessageManager.Replaceable<>("{usage}", e.getUsage())
            ));
            return false;
        } catch (CommandPermissionException e) {
            commandSender.sendMessage(plugin.getMessageManager().getAndFormatMsg(
                    MessageManager.Style.ERROR,
                    "noCommandPermsError",
                    "{ac}You do not have permission to execute this command!"
            ));
            return false;
        }
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return tabComplete(sender, command, new ArgumentList(args));
    }
}
