package org.strassburger.essentialz.util.commands;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.strassburger.essentialz.EssentialZ;
import org.strassburger.essentialz.commands.movement.FlyCommand;
import org.strassburger.essentialz.commands.movement.FlySpeedCommand;
import org.strassburger.essentialz.commands.movement.GamemodeShortcutCommand;
import org.strassburger.essentialz.commands.movement.WalkSpeedCommand;
import org.strassburger.essentialz.util.MessageManager;
import org.strassburger.essentialz.commands.*;

public class CommandManager {
    private final EssentialZ plugin;

    public CommandManager(EssentialZ plugin) {
        this.plugin = plugin;
    }

    /**
     * Registers all commands
     */
    public void registerCommands() {
        registerCommand(new PlayTimeCommand("playtime", plugin));
        registerCommand(new SkullCommand("skull", plugin));
        registerCommand(new ItemNameCommand("itemname", plugin));
        registerCommand(new ItemLoreCommand("itemlore", plugin));
        registerCommand(new FlyCommand("fly", plugin));
        registerCommand(new FlySpeedCommand("flyspeed", plugin));
        registerCommand(new WalkSpeedCommand("walkspeed", plugin));
        registerCommand(new GamemodeShortcutCommand("gmc", plugin));
        registerCommand(new GamemodeShortcutCommand("gms", plugin));
        registerCommand(new GamemodeShortcutCommand("gma", plugin));
        registerCommand(new GamemodeShortcutCommand("gmsp", plugin));
    }

    /**
     * Registers a command
     *
     * @param command The command to register
     */
    private void registerCommand(CustomCommand command) {
        registerCommand(command.getName(), command, command);
    }

    /**
     * Registers a command
     *
     * @param name The name of the command
     * @param executor The executor of the command
     * @param tabCompleter The tab completer of the command
     */
    private void registerCommand(String name, CommandExecutor executor, TabCompleter tabCompleter) {
        PluginCommand command = plugin.getCommand(name);

        if (command != null) {
            command.setExecutor(executor);
            command.setTabCompleter(tabCompleter);
            command.permissionMessage(plugin.getMessageManager().getAndFormatMsg(
                    MessageManager.Style.ERROR,
                    "noCommandPermsError",
                    "{ac}You do not have permission to execute this command!"
            ));
        }
    }
}
