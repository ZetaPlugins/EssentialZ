package com.zetaplugins.essentialz.util.permissions;

import org.bukkit.permissions.PermissionDefault;

public enum Permission implements PermissionNode {
    PLAYTIME("playtime", PermissionDefault.TRUE, "Allows the user to see the playtime of a player"),
    SKULL("skull", PermissionDefault.OP, "Allows the user to get the skull of a player"),
    ITEMNAME("itemname", PermissionDefault.OP, "Allows the user to change the name of an item"),
    ITEMLORE("itemlore", PermissionDefault.OP, "Allows the user to change the lore of an item"),
    FLY("fly", PermissionDefault.OP, "Allows the user to fly"),
    FLY_OTHERS("fly.others", PermissionDefault.OP, "Allows the user to give other players flight"),
    FLYSPEED("flyspeed", PermissionDefault.OP, "Allows the user to change the fly speed of a player"),
    FLYSPEED_OTHERS("flyspeed.others", PermissionDefault.OP, "Allows the user to change the fly speed of other players"),
    WALKSPEED("walkspeed", PermissionDefault.OP, "Allows the user to change the walk speed of a player"),
    WALKSPEED_OTHERS("walkspeed.others", PermissionDefault.OP, "Allows the user to change the walk speed of other players"),
    GAMEMODE("gamemode", PermissionDefault.OP, "Allows the user to change the gamemode of a player"),
    GAMEMODE_OTHERS("gamemode.others", PermissionDefault.OP, "Allows the user to change the gamemode of other players"),
    GODMODE("godmode", PermissionDefault.OP, "Allows the user to enable godmode"),
    GODMODE_OTHERS("godmode.others", PermissionDefault.OP, "Allows the user to enable godmode for other players"),
    CLEARCHAT("clearchat", PermissionDefault.OP, "Allows the user to clear the chat"),
    GIVE("give", PermissionDefault.OP, "Allows the user to give items to players"),
    MSG("msg", PermissionDefault.TRUE, "Allows the user to send private messages to other players"),
    MSG_COLOR("msg.color", PermissionDefault.OP, "Allows the user to use colors in private messages"),
    MSG_TOGGLE("msg.toggle", PermissionDefault.TRUE, "Allows the user to toggle private messages on or off"),
    TEAMCHAT("teamchat", PermissionDefault.OP, "Allows the user to use team chat"),
    TEAMCHAT_COLOR("teamchat.color", PermissionDefault.OP, "Allows the user to use colors in team chat"),
    BROADCAST("broadcast", PermissionDefault.OP, "Allows the user to broadcast messages to the server"),
    CHAT("chat", PermissionDefault.TRUE, "Allows the user to use chat (only works if custom chat is enabled)"),
    CHAT_COLOR("chat.color", PermissionDefault.OP, "Allows the user to use colors in chat (only works if custom chat is enabled)"),
    IGNORE("ignore", PermissionDefault.TRUE, "Allows the user to ignore other players"),
    NICK("nick", PermissionDefault.OP, "Allows the user to set their nickname"),
    NICK_OTHERS("nick.others", PermissionDefault.OP, "Allows the user to set the nickname of other players"),
    WHOIS("whois", PermissionDefault.TRUE, "Allows the user to find out what nickname a player has or what nickname is used by a player"),
    TRASH("trash", PermissionDefault.TRUE, "Allows the user to use the trash can to delete items"),
    REPAIR("repair", PermissionDefault.OP, "Allows the user to repair their items"),
    HEAL("heal", PermissionDefault.OP, "Allows the user to heal themselves"),
    HEAL_OTHERS("heal.others", PermissionDefault.OP, "Allows the user to heal other players"),
    FEED("feed", PermissionDefault.OP, "Allows the user to feed themselves"),
    FEED_OTHERS("feed.others", PermissionDefault.OP, "Allows the user to feed other players"),
    MORE("more", PermissionDefault.OP, "Allows the user to fill their held item to its max stack size"),
    GIVESPAWNER("givespawner", PermissionDefault.OP, "Allows the user to give mob spawners to players"),
    ENCHANT("enchant", PermissionDefault.OP, "Allows the user to enchant their held item"),
    PWEATHER("pweather", PermissionDefault.OP, "Allows the user to set the weather for a specific player"),
    SUDO("sudo", PermissionDefault.OP, "Allows the user to force other players to run commands"),
    HAT("hat", PermissionDefault.TRUE, "Allows the user to wear the item in their hand as a hat"),
    TOP("top", PermissionDefault.TRUE, "Allows the user to teleport to the highest block at their current location"),
    SETWARP("setwarp", PermissionDefault.OP, "Allows the user to create warps"),
    WARP("warp", PermissionDefault.TRUE, "Allows the user to teleport to warps"),
    DELWARP("delwarp", PermissionDefault.OP, "Allows the user to delete warps"),
    BALANCE("balance", PermissionDefault.TRUE, "Allows the user to check their balance"),
    BALANCE_OTHERS("balance.others", PermissionDefault.TRUE, "Allows the user to check another player's balance"),
    BALANCE_MODIFY("balance.set", PermissionDefault.OP, "Allows the user to modify another player's balance"),
    PAY("pay", PermissionDefault.TRUE, "Allows the user to pay another player"),
    BALTOP("baltop", PermissionDefault.TRUE, "Allows the user to see the top balances on the server"),
    ;

    private final String node;
    private final PermissionDefault permissionDefault;
    private final String description;

    /**
     * Constructs a Permission enum constant.
     * @param node The permission node string.
     * @param permissionDefault The default permission setting.
     * @param description The description of the permission.
     */
    Permission(String node, PermissionDefault permissionDefault, String description) {
        this.node = "essentialz." + node;
        this.permissionDefault = permissionDefault;
        this.description = description;
    }

    @Override
    public String getNode() {
        return node;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public PermissionDefault getPermissionDefault() {
        return permissionDefault;
    }

    @Override
    public String toString() {
        return getNode();
    }

    /**
     * Registers all permissions in this enum.
     */
    public static void registerAll() {
        for (Permission perm : values()) {
            perm.register();
        }
    }
}