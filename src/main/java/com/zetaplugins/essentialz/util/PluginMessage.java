package com.zetaplugins.essentialz.util;

public enum PluginMessage {
    PLAYER_NOT_FOUND("playerNotFound", "{ac}Player not found.", MessageStyle.ERROR),
    SPECIFY_PLAYER_OR_BE_PLAYER("specifyPlayerOrBePlayer", "{ac}You must specify a player or be a player to use this command.", MessageStyle.ERROR),
    USAGE_ERROR("usageError", "{ac}Usage: {usage}", MessageStyle.ERROR),
    NO_PERMS_ERROR("noPermsError", "{ac}You do not have permission to do this!", MessageStyle.ERROR),
    PLAYER_ONLY("playerOnly", "{ac}You must be a player to use this command.", MessageStyle.ERROR),
    NO_RULES_FILE_FOUND("noRulesFileFound", "{ac}No rules file found. Please contact an administrator.", MessageStyle.ERROR),
    ERROR_READING_RULES_FILE("errorReadingRulesFile", "{ac}An error occurred while reading the rules file.", MessageStyle.ERROR),
    PLAYTIME("playTime", "&7{player} has played for {ac}{time}&7.", MessageStyle.DEFAULT),
    PLAYTIME_SELF("playTimeSelf", "&7You have played for {ac}{time}&7.", MessageStyle.DEFAULT),
    ABOUT_MESSAGE("aboutMessage", "\n{ac}<b><grey>></grey> EssentialZ</b> <grey>v%version%</grey>\n\n{ac} <u><click:open_url:'https://docs.zetaplugins.com/essentialz/'>Documentation</click></u>  {ac}<u><click:open_url:'https://strassburger.org/discord'>Support Discord</click></u>\n", MessageStyle.DEFAULT),
    RELOAD_SUCCESS("reloaded", "&7EssentialZ has been successfully reloaded!", MessageStyle.SUCCESS),
    NO_PERMISSION_HEAL_OTHERS("noPermissionHealOthers", "{ac}You do not have permission to heal other players.", MessageStyle.ERROR),
    HEAL_SELF("healedSelf", "&7You have been healed to full health.", MessageStyle.SUCCESS),
    HEAL_OTHER("healedOther", "&7You have healed {ac}{player}&7 to full health.", MessageStyle.SUCCESS),
    NO_PERMISSION_FEED_OTHERS("noPermissionFeedOthers", "{ac}You do not have permission to feed other players.", MessageStyle.ERROR),
    FEED_SELF("feededSelf", "&7Your hunger has been fully restored.", MessageStyle.SUCCESS),
    FEED_OTHER("feededOther", "&7You have fully restored {ac}{player}&7's hunger.", MessageStyle.SUCCESS),
    PWEATHER_RESET("pweatherReset", "&7Your weather has been reset to the server default.", MessageStyle.WORLDCONTROL),
    PWEATHER_RESET_OTHER("pweatherResetOther", "&7You have reset {ac}{player}&7's weather to the server default.", MessageStyle.WORLDCONTROL),
    PWEATHER_SET("pweatherSet", "&7Your weather has been set to {ac}{weather}&7.", MessageStyle.WORLDCONTROL),
    PWEATHER_SET_OTHER("pweatherSetOther", "&7You have set {ac}{player}&7's weather to {ac}{weather}&7.", MessageStyle.WORLDCONTROL),
    WALKSPEED_SET_OTHER("walkSpeedSetOther", "&7Set {ac}{player}&7's walk speed to {ac}{speed}&7.", MessageStyle.MOVEMENT),
    WALKSPEED_SET("walkSpeedSet", "&7Set your walk speed to {ac}{speed}&7.", MessageStyle.MOVEMENT),
    GAMEMODE_SET_OTHER("gamemodeSetOther", "&7Set {ac}{player}&7's gamemode to {ac}{gamemode}&7.", MessageStyle.MOVEMENT),
    GAMEMODE_SET("gamemodeSet", "&7Set your gamemode to {ac}{gamemode}&7.", MessageStyle.MOVEMENT),
    FLYSPEED_SET_OTHER("flySpeedSetOther", "&7Set {ac}{player}&7's fly speed to {ac}{speed}&7.", MessageStyle.MOVEMENT),
    FLYSPEED_SET("flySpeedSet", "&7Set your fly speed to {ac}{speed}&7.", MessageStyle.MOVEMENT),
    ALLOWED_TO_FLY("allowedToFly", "&7You are now allowed to fly!", MessageStyle.MOVEMENT),
    NOT_ALLOWED_TO_FLY("notAllowedToFly", "&7You are no longer allowed to fly.", MessageStyle.MOVEMENT),
    ALLOWED_TO_FLY_OTHER("allowedToFlyOther", "&7{ac}{player}&7 is now allowed to fly!", MessageStyle.MOVEMENT),
    NOT_ALLOWED_TO_FLY_OTHER("notAllowedToFlyOther", "&7{ac}{player}&7 is no longer allowed to fly.", MessageStyle.MOVEMENT),
    WARP_NOT_FOUND("warpNotFound", "{ac}Warp '{warpName}' does not exist.", MessageStyle.ERROR),
    WARP_SET_SUCCESS("setWarpSucess", "&7Warp {ac}{warpName}&7 has been set at {ac}{x} {y} {z}&7.", MessageStyle.MOVEMENT),
    WARP_DELETE_SUCCESS("deleteWarpSuccess", "&7Warp {ac}{warpName}&7 has been deleted.", MessageStyle.MOVEMENT),
    SUDO_SUCCESS("sudoSuccess", "&7Successfully forced {ac}{player} &7to run the command: {ac}{command}&7.", MessageStyle.MODERATION),
    GODMODE_TOGGLED_OTHER("godModeToggledOther", "&7God mode for {ac}{player}&7 is now {ac}{status}&7.", MessageStyle.MODERATION),
    GODMODE_TOGGLED_SELF("godModeToggled", "&7God mode is now {ac}{status}&7.", MessageStyle.MODERATION),
    CHAT_CLEARED("chatCleared", "{spaces}{prefix}&7Chat has been cleared by {ac}{player}&7.", MessageStyle.MODERATION)
    /*
     * Still missing:
     * - items
     * - economy
     * - communication
     */
    ;

    private final String key;
    private final String defaultMessage;
    private final MessageStyle defaultStyle;

    /**
     * Constructor for PluginMessage
     * @param key The key of the message
     * @param defaultMessage The default message (Without the {style.X} tag)
     * @param defaultStyle The default message style
     */
    PluginMessage(String key, String defaultMessage, MessageStyle defaultStyle) {
        this.key = key;
        this.defaultMessage = defaultMessage;
        this.defaultStyle = defaultStyle;
    }

    public String getKey() {
        return key;
    }

    public String getDefaultMessage() {
        return defaultMessage;
    }

    public MessageStyle getDefaultStyle() {
        return defaultStyle;
    }

    /**
     * Get the message style from the language file message
     * @param languageManager The language manager to get the message from
     * @return The message style or the default style if not found
     */
    public MessageStyle getStyle(LanguageManager languageManager) {
        if (languageManager == null) return defaultStyle;
        String message = languageManager.getString(key);
        if (message == null) return defaultStyle;
        String styleId = findStyleInMessage(message);
        if (styleId == null) return defaultStyle;

        try {
            return MessageStyle.of(styleId);
        } catch (IllegalArgumentException e) {
            return defaultStyle;
        }
    }

    private String findStyleInMessage(String message) {
        String stylePrefix = "{style.";
        int styleStart = message.indexOf(stylePrefix);
        if (styleStart != -1) {
            int styleEnd = message.indexOf("}", styleStart);
            if (styleEnd != -1) {
                return message.substring(styleStart + stylePrefix.length(), styleEnd);
            }
        }
        return null;
    }
}
