package com.zetaplugins.essentialz.util;

import com.zetaplugins.essentialz.EssentialZ;

import java.util.Objects;

/**
 * The style of a message
 */
public enum MessageStyle {
    NONE("none"),
    DEFAULT("default"),
    ERROR("error"),
    WARNING("warning"),
    SUCCESS("success"),
    MOVEMENT("movement"),
    COMBAT("combat"),
    MODERATION("moderation"),
    ITEMS("items"),
    STATS("stats"),
    COMMUNICATION("communication"),
    TEAMCHAT("teamchat"),
    WORLDCONTROL("worldcontrol"),
    ECONOMY("economy"),
    FUN("fun");

    public final String id;

    MessageStyle(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    /**
     * Gets the accent color of the style
     *
     * @param plugin The plugin instance
     * @return The accent color
     */
    public String getAccentColor(EssentialZ plugin) {
        if (Objects.equals(getId(), "none")) return "#ba7cf8";
        String accentColor = plugin.getConfig().getString("styles." + getId() + ".accentColor");
        return accentColor == null ? "#ba7cf8" : accentColor;
    }

    /**
     * Gets the prefix of the style
     *
     * @param plugin The plugin instance
     * @return The prefix
     */
    public String getPrefix(EssentialZ plugin) {
        if (Objects.equals(getId(), "none")) return "";
        String prefix = plugin.getConfig().getString("styles." + getId() + ".prefix");
        return prefix == null ? "&8[<gradient:#ba7cf8:#ba7cf8>EssentialZ&8]" : prefix;
    }

    /**
     * Gets a style by its id
     *
     * @param id The id of the style
     * @return The style
     */
    public static MessageStyle of(String id) {
        for (MessageStyle style : values()) {
            if (style.id.equals(id)) {
                return style;
            }
        }
        return null;
    }
}
