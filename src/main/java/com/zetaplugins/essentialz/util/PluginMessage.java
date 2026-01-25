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
    CHAT_CLEARED("chatCleared", "{spaces}{prefix}&7Chat has been cleared by {ac}{player}&7.", MessageStyle.MODERATION),
    INSUFFICIENT_FUNDS("insufficientFunds", "{ac}You do not have enough funds!", MessageStyle.ERROR),
    PAY_SENDER("paySender", "&7You have paid {ac}{player} {amount}&7.", MessageStyle.ECONOMY),
    PAY_RECEIVER("payReceiver", "&7You have received {ac}{amount} &7from {ac}{player}&7.", MessageStyle.ECONOMY),
    BALTOP_HEADER("balTopHeader", "\n&8┌─ {ac}Top Balances&r&8 ────────────────────", MessageStyle.ECONOMY),
    BALTOP_ENTRY("balTopEntry", "&8│ {ac}{rank}. &7{player} &8- {ac}{balance}", MessageStyle.ECONOMY),
    BALTOP_FOOTER("balTopFooter", "&8└─────────────────────────────\n", MessageStyle.ECONOMY),
    OTHER_PLAYER_BALANCE("otherPlayerBalance", "{ac}{player}&7's balance is {ac}{balance}", MessageStyle.ECONOMY),
    YOUR_BALANCE("yourBalance", "&7Your balance is {ac}{balance}", MessageStyle.ECONOMY),
    SET_BALANCE("setBalance", "&7Set {ac}{player}&7's balance to {ac}{balance}", MessageStyle.ECONOMY),
    ADD_BALANCE("addBalance", "&7Added {ac}{amount} &7to {ac}{player}&7's balance", MessageStyle.ECONOMY),
    REMOVE_BALANCE("removeBalance", "&7Removed {ac}{amount} &7from {ac}{player}&7's balance", MessageStyle.ECONOMY),
    INSUFFICIENT_FUNDS_TO_WITHDRAW("insufficientFundsWithdraw", "{ac}{player} does not have enough funds to remove {amount}", MessageStyle.ERROR),
    INVALID_NUMBER("invalidNumber", "{ac}'{input}' is not a valid number.", MessageStyle.ERROR),
    MUST_HOLD_AN_ITEM("mustHoldAnItem", "{ac}You must be holding an item!", MessageStyle.ERROR),
    INVALID_ENCHANTMENT("invalidEnchantment", "{ac}The enchantment '{enchantment}' does not exist.", MessageStyle.ERROR),
    ITEM_LACKS_ENCHANTMENT("itemLacksEnchantment", "{ac}The item you are holding does not have the '{enchantment}' enchantment.", MessageStyle.ERROR),
    UNENCHAT_SUCCESS("unenchantSuccess", "&7Successfully removed {ac}{enchantment} &7from your item.", MessageStyle.ITEMS),
    TRASH_INVENTORY_TITLE("trashInventoryTitle", "&8Trash Can", MessageStyle.ITEMS),
    SKULL_GIVEN("skullGiven", "&7Seccessfully gave {ac}{player}&7's head to {ac}{receiver}&7.", MessageStyle.ITEMS),
    SKULL_RECEIVED("skullReceived", "&7You received {ac}{player}&7's head.", MessageStyle.ITEMS),
    ITEMS_REPAIRED_ALL("itemsRepairedAll", "&7Repaired {ac}{count} &7items in your inventory.", MessageStyle.ITEMS),
    REPAIR_MUST_HOLD_ITEM("repairMustHoldAnItem", "{ac}You must be holding an item to repair.", MessageStyle.ERROR),
    ITEM_NOT_REPAIRABLE("itemNotRepairable", "{ac}The item you are holding cannot be repaired.", MessageStyle.ERROR),
    ITEM_REPAIRED("itemRepaired", "&7Your held item has been repaired.", MessageStyle.ITEMS),
    ITEM_CANNOT_BE_STACKED("itemCannotBeStacked", "{ac}The item you are holding cannot be stacked.", MessageStyle.ERROR),
    STACKED_ITEMS("stackedItems", "&7Stacked {ac}{amount}&7 items in your hand.", MessageStyle.ITEMS),
    ITEM_NAME_TOO_LONG("itemNameTooLong", "{ac}The item name is too long! The maximum length is {max} characters.", MessageStyle.ERROR),
    ITEM_NAME_SET("itemNameSet", "&7Item name set to '{name}&r&7'.", MessageStyle.ITEMS),
    LORE_TOO_LONG("loreTooLong", "{ac}The lore is too long! The maximum length is {max} lines.", MessageStyle.ERROR),
    LORE_ADDED("loreAdded", "&7Added '{lore}&r&7' to the lore.", MessageStyle.ITEMS),
    LORE_SET("loreSet", "&7Set lore line {ac}{line}&7 to '{lore}&r&7'.", MessageStyle.ITEMS),
    LORE_REMOVED("loreRemoved", "&7Removed lore line {ac}{line}&7.", MessageStyle.ITEMS),
    LORE_INSERTED("loreInserted", "&7Inserted '{lore}&r&7' at line {ac}{line}&7.", MessageStyle.ITEMS),
    LORE_LINE_DOES_NOT_EXIST("loreLineDoesNotExist", "{ac}This lore line does not exist!", MessageStyle.ERROR),
    INVALID_MATERIAL("invalidMaterial", "{ac}'{material}' is not a valid material.", MessageStyle.ERROR),
    GIVE_YOURSELF_CONFIRMATION("giveYourselfConfirmation", "&7You have been given {ac}{amount}x {material}&7.", MessageStyle.ITEMS),
    HAT_SET("hatSet", "&7You are now wearing your held item as a hat.", MessageStyle.ITEMS),
    INVALID_MOB_TYPE("invalidMobType", "{ac}You have to specify a valid mob type.", MessageStyle.ERROR),
    GIVE_SPAWNER_SUCCESS("giveSpawnerSuccess", "&7Gave {ac}{amount} {mobType} &7spawner(s) to {ac}{player}&7.", MessageStyle.ITEMS),
    GIVE_CONFIRMATION("giveConfirmation", "&7Gave {ac}{amount}x {material}&7 to {ac}{player}&7.", MessageStyle.ITEMS),
    ENCHANT_SUCCESS("enchantSuccess", "&7Successfully enchanted your item with {ac}{enchantment} &7level {ac}{level}&7.", MessageStyle.ITEMS),
    HOME_SET_SUCCESS("setHomeSucess", "&7Home {ac}{homeName}&7 has been set at {ac}{x} {y} {z}&7.", MessageStyle.MOVEMENT),
    HOME_DELETE_SUCCESS("deleteHomeSuccess", "&7Home {ac}{homeName}&7 has been deleted.", MessageStyle.MOVEMENT),
    HOME_DELETE_FAIL("homeDeleteFail", "{ac}You do not have a home named '{homeName}'.", MessageStyle.ERROR),
    HOME_NOT_FOUND("homeNotFound", "{ac}Home '{homeName}' does not exist.", MessageStyle.ERROR),
    NO_HOMES_SET("noHomesSet", "{ac}You have not set any homes yet.", MessageStyle.ERROR),
    HOMES_LIST_HEADER("homesListHeader", "\n&8┌─ {ac}Your Homes&r&8 ─────────────────────", MessageStyle.MOVEMENT),
    HOMES_LIST_ENTRY("homesListEntry", "&8│ &7{index}. {ac}{homeName} &8- &7{x} {y} {z}", MessageStyle.MOVEMENT),
    HOMES_LIST_FOOTER("homesListFooter", "&8└─────────────────────────────\n", MessageStyle.MOVEMENT),
    HOME_SET_MAX_HOMES_REACHED("homeSetMaxHomesReached", "{ac}You have reached the maximum number of homes you can set ({maxHomes}).", MessageStyle.ERROR),
    NO_ENDERCHEST_EDIT_ACCESS("noEnderchestEditAccess", "{ac}You do not have permission to modify other players' enderchests.", MessageStyle.ERROR),
    CANNOT_INVSEE_SELF("cannotInvseeSelf", "{ac}You cannot view your own inventory with /invsee.", MessageStyle.ERROR),
    SOCIALSPY_ENABLED("socialSpyEnabled", "&7Socialspy enabled for {ac}{player}&7.", MessageStyle.MODERATION),
    SOCIALSPY_DISABLED("socialSpyDisabled", "&7Socialspy disabled for {ac}{player}&7.", MessageStyle.MODERATION),
    SOCIALSPY_DISABLED_ALL("socialSpyDisabledAll", "&7Socialspy disabled for all targets.", MessageStyle.MODERATION),
    SOCIALSPY_FORMAT("socialSpyFormat", "&8[{ac}\uD83D\uDC41&8] &8[{ac}{sender} &7→ {ac}{recipient}&8] &7{message}", MessageStyle.MODERATION),
    SPAWN_SET_SUCCESS("setSpawnSuccess", "&7Server spawn has been set at {ac}{x} {y} {z}&7.", MessageStyle.MOVEMENT),
    NO_SPAWN_SET("noSpawnSet", "{ac}The server spawn point is not set.", MessageStyle.ERROR),
    INVALID_DURATION_FORMAT("invalidDurationFormat", "{ac}The duration format is invalid. Use formats like '10s', '5m', '1h'.", MessageStyle.ERROR),
    WEATHER_CHANGED_TIME("weatherChangedTime", "&7Weather changed to {ac}{weather}&7 for {ac}{duration} &7seconds.", MessageStyle.WORLDCONTROL),
    WEATHER_CHANGED("weatherChanged", "&7Weather changed to {ac}{weather}&7.", MessageStyle.WORLDCONTROL),
    DURATION_MUST_BE_POSITIVE("durationMustBePositive", "{ac}The duration must be a positive number.", MessageStyle.ERROR),
    CURRENT_TIME("currentTime", "&7The current time is {ac}{time} &7({ticks} ticks).", MessageStyle.WORLDCONTROL),
    INVALID_TIME_FORMAT("invalidTimeFormat", "{ac}The time format is invalid. Use 'day', 'night', 'dawn', 'dusk', 'HH:MM', or 'Xticks'.", MessageStyle.ERROR),
    TIME_SET("timeSet", "&7Time set to {ac}{time} &7({ticks} ticks).", MessageStyle.WORLDCONTROL),
    INVALID_LIGHTNING_AMOUNT("invalidLightningAmount", "{ac}The amount of lightning strikes must be between 1 and {max}.", MessageStyle.ERROR),
    LIGHTNING_STRIKES("lightningStrikes", "&7Struck {ac}{player} &7with lightning {ac}{amount} &7time(s).", MessageStyle.FUN),
    GLOW_ENABLED("glowEnabled", "&7Glow effect enabled for {ac}{player}&7.", MessageStyle.FUN),
    GLOW_DISABLED("glowDisabled", "&7Glow effect disabled for {ac}{player}&7.", MessageStyle.FUN),
    DEATH_MESSAGE("deathMessage", "&8[&c☠&8] &7{message}", MessageStyle.NONE),

    // TPA Messages
    TPA_REQUEST_SENT("tpaRequestSent", "&7Teleport request sent to {ac}{player}&7.", MessageStyle.MOVEMENT),
    TPA_REQUEST_RECEIVED("tpaRequestReceived", "{ac}{player}&7 wants to {type}.", MessageStyle.MOVEMENT),
    TPA_REQUEST_INSTRUCTIONS("tpaRequestInstructions", "&7Type <u><click:run_command:'/tpaccept'>{ac}/tpaccept</click></u> &7to accept or <u><click:run_command:'/tpdeny'>{ac}/tpdeny</click></u> &7to deny.", MessageStyle.MOVEMENT),
    TPA_ACCEPTED_SENDER("tpaAcceptedSender", "{ac}{player}&7 accepted your teleport request.", MessageStyle.SUCCESS),
    TPA_ACCEPTED_TARGET("tpaAcceptedTarget", "&7You accepted {ac}{player}&7's teleport request.", MessageStyle.SUCCESS),
    TPA_DENIED_SENDER("tpaDeniedSender", "{ac}{player}&7 denied your teleport request.", MessageStyle.ERROR),
    TPA_DENIED_TARGET("tpaDeniedTarget", "&7You denied the teleport request.", MessageStyle.MOVEMENT),
    TPA_REQUEST_ALREADY_PENDING("tpaRequestAlreadyPending", "{ac}You already have a pending request to this player.", MessageStyle.ERROR),
    TPA_NO_PENDING_REQUESTS("tpaNoPendingRequests", "{ac}You have no pending teleport requests.", MessageStyle.ERROR),
    TPA_NO_OUTGOING_REQUESTS("tpaNoOutgoingRequests", "{ac}You have no outgoing teleport requests.", MessageStyle.ERROR),
    TPA_REQUESTS_CANCELLED("tpaRequestsCancelled", "&7Cancelled {ac}{count}&7 outgoing request(s).", MessageStyle.MOVEMENT),
    TPA_CANNOT_SELF("tpaCannotSelf", "{ac}You cannot teleport to yourself.", MessageStyle.ERROR),
    TPA_DISABLED_TARGET("tpaDisabledTarget", "{ac}{player} has teleport requests disabled.", MessageStyle.ERROR),
    TPA_TOGGLE_ENABLED("tpaToggleEnabled", "&7You have {ac}enabled&7 teleport requests.", MessageStyle.SUCCESS),
    TPA_TOGGLE_DISABLED("tpaToggleDisabled", "&7You have {ac}disabled&7 teleport requests.", MessageStyle.WARNING),
    TPA_REQUEST_EXPIRED("tpaRequestExpired", "&7Your teleport request to {ac}{player}&7 has expired.", MessageStyle.WARNING),
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
