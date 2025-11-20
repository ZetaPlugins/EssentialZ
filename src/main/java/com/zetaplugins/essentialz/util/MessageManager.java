package com.zetaplugins.essentialz.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import com.zetaplugins.essentialz.EssentialZ;

import java.util.*;

public class MessageManager {
    private final EssentialZ plugin;
    private final Map<String, String> colorMap;

    public MessageManager(EssentialZ plugin) {
        this.plugin = plugin;
        colorMap = new HashMap<>();
        colorMap.put("&0", "<black>");
        colorMap.put("&1", "<dark_blue>");
        colorMap.put("&2", "<dark_green>");
        colorMap.put("&3", "<dark_aqua>");
        colorMap.put("&4", "<dark_red>");
        colorMap.put("&5", "<dark_purple>");
        colorMap.put("&6", "<gold>");
        colorMap.put("&7", "<gray>");
        colorMap.put("&8", "<dark_gray>");
        colorMap.put("&9", "<blue>");
        colorMap.put("&a", "<green>");
        colorMap.put("&b", "<aqua>");
        colorMap.put("&c", "<red>");
        colorMap.put("&d", "<light_purple>");
        colorMap.put("&e", "<yellow>");
        colorMap.put("&f", "<white>");
        colorMap.put("&k", "<obfuscated>");
        colorMap.put("&l", "<bold>");
        colorMap.put("&m", "<strikethrough>");
        colorMap.put("&n", "<underline>");
        colorMap.put("&o", "<italic>");
        colorMap.put("&r", "<reset>");
    }

    /**
     * Formats a message with placeholders
     *
     * @param msg The message to format
     * @param replaceables The placeholders to replace
     * @return The formatted message
     */
    public Component formatMsg(String msg, Replaceable<?>... replaceables) {
        MiniMessage mm = MiniMessage.miniMessage();

        msg = replaceColorCodes(msg);

        ResolverResult result = buildResolvers(mm, msg, Style.NONE, replaceables);

        return mm.deserialize("<!i>" + result.msg(), result.resolvers());
    }

    /**
     * Gets and formats a message from the config
     *
     * @param style The style of the message
     * @param path The path to the message in the config
     * @param fallback The fallback message
     * @param replaceables The placeholders to replace
     * @return The formatted message
     */
    public Component getAndFormatMsg(Style style, String path, String fallback, Replaceable<?>... replaceables) {
        if (path.startsWith("messages.")) path = path.substring("messages.".length());

        MiniMessage mm = MiniMessage.miniMessage();
        String msg = plugin.getLanguageManager().getString(path, fallback);

        msg = style.getPrefix(plugin) + msg;

        msg = msg.replace("{prefix}", style.getPrefix(plugin));

        msg = replaceColorCodes(msg, style);

        ResolverResult result = buildResolvers(mm, msg, style, replaceables);

        return mm.deserialize("<!i>" + result.msg(), result.resolvers());
    }

    private ResolverResult buildResolvers(MiniMessage mm, String msg, Style valueStyle, Replaceable<?>... replaceables) {
        List<TagResolver> resolvers = new ArrayList<>();
        Style replacementStyle = valueStyle == null ? Style.NONE : valueStyle;

        for (Replaceable<?> r : replaceables) {
            String rawName = r.placeholder().replaceAll("^\\{?|\\}?$", "");
            String tagName = rawName.toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9_-]", "_");

            String tagToken = "<" + tagName + ">";

            msg = msg.replace(r.placeholder(), tagToken);

            String valueStr = Objects.toString(r.value());
            Component replacementComponent = r.deserialize()
                    ? mm.deserialize(replaceColorCodes(valueStr, replacementStyle))
                    : Component.text(valueStr);

            resolvers.add(TagResolver.resolver(tagName, Tag.inserting(replacementComponent)));
        }

        return new ResolverResult(msg, resolvers.toArray(TagResolver[]::new));
    }

    private String replaceColorCodes(String msg) {
        return replaceColorCodes(msg, Style.NONE);
    }

    private String replaceColorCodes(String msg, Style style) {
        msg = msg.replace("{ac}", "<" + style.getAccentColor(plugin) + ">");

        for (Style currentStyle : Style.values()) {
            String styleAccentColor = currentStyle.getAccentColor(plugin);
            msg = msg.replace("{ac_" + currentStyle.getId() + "}", "<" + styleAccentColor + ">");
        }

        for (Map.Entry<String, String> entry : colorMap.entrySet()) {
            msg = msg.replace(entry.getKey(), entry.getValue());
        }

        return msg;
    }

    public record Replaceable<T>(String placeholder, T value, boolean deserialize) {
        public Replaceable(String placeholder, T value) {
            this(placeholder, value, false);
        }
    }

    private record ResolverResult(String msg, TagResolver[] resolvers) {}

    /**
     * The style of a message
     */
    public enum Style {
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
        WORLDCONTROL("worldcontrol")
        ;

        public final String id;

        Style(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }

        /**
         * Gets the accent color of the style
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
         * @param id The id of the style
         * @return The style
         */
        public static Style of(String id) {
            for (Style style : values()) {
                if (style.id.equals(id)) {
                    return style;
                }
            }
            return null;
        }
    }
}
