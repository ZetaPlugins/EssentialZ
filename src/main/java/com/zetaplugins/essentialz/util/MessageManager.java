package com.zetaplugins.essentialz.util;

import com.zetaplugins.essentialz.EssentialZ;
import com.zetaplugins.zetacore.annotations.InjectManager;
import com.zetaplugins.zetacore.annotations.Manager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

import java.util.*;

@Manager
public class MessageManager {
    private final EssentialZ plugin;
    private final Map<String, String> colorMap;

    @InjectManager
    private LanguageManager languageManager;

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

        ResolverResult result = buildResolvers(mm, msg, MessageStyle.NONE, replaceables);

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
    public Component getAndFormatMsg(MessageStyle style, String path, String fallback, Replaceable<?>... replaceables) {
        return getAndFormatMsg(style, path, fallback, true, replaceables);
    }

    /**
     * Gets and formats a message from the config
     *
     * @param style The style of the message
     * @param path The path to the message in the config
     * @param fallback The fallback message
     * @param addPrefix Whether to add the style prefix
     * @param replaceables The placeholders to replace
     * @return The formatted message
     */
    public Component getAndFormatMsg(MessageStyle style, String path, String fallback, boolean addPrefix, Replaceable<?>... replaceables) {
        if (path.startsWith("messages.")) path = path.substring("messages.".length());

        MiniMessage mm = MiniMessage.miniMessage();
        String msg = languageManager.getString(path, fallback);

        // Remove any style tags from the message
        msg = msg.replaceAll("\\{style\\.[^}]+}", "");

        if (addPrefix) msg = style.getPrefix(plugin) + msg;

        msg = msg.replace("{prefix}", style.getPrefix(plugin));

        msg = replaceColorCodes(msg, style);

        ResolverResult result = buildResolvers(mm, msg, style, replaceables);

        return mm.deserialize("<!i>" + result.msg(), result.resolvers());
    }

    /**
     * Gets and formats a message from the config using a PluginMessage
     *
     * @param message The PluginMessage to get the message from
     * @param replaceables The placeholders to replace
     * @return The formatted message
     */
    public Component getAndFormatMsg(PluginMessage message, Replaceable<?>... replaceables) {
        return getAndFormatMsg(message, true, replaceables);
    }

    /**
     * Gets and formats a message from the config using a PluginMessage
     *
     * @param pluginMessage The PluginMessage to get the message from
     * @param replaceables The placeholders to replace
     * @return The formatted message
     */
    public Component getAndFormatMsg(PluginMessage pluginMessage, boolean addPrefix, Replaceable<?>... replaceables) {
        return getAndFormatMsg(
                pluginMessage.getStyle(languageManager),
                pluginMessage.getKey(),
                pluginMessage.getDefaultMessage(),
                addPrefix,
                replaceables
        );
    }

    private ResolverResult buildResolvers(MiniMessage mm, String msg, MessageStyle valueStyle, Replaceable<?>... replaceables) {
        List<TagResolver> resolvers = new ArrayList<>();
        MessageStyle replacementStyle = valueStyle == null ? MessageStyle.NONE : valueStyle;

        for (Replaceable<?> r : replaceables) {
            String rawName = r.placeholder().replaceAll("^\\{?|\\}?$", "");
            String tagName = rawName.toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9_-]", "_");

            String tagToken = "<" + tagName + ">";

            msg = msg.replace(r.placeholder(), tagToken);

            Component replacementComponent;
            if (r.value() instanceof Component component) {
                replacementComponent = component;
            } else {
                String valueStr = Objects.toString(r.value());
                replacementComponent = r.deserialize()
                        ? mm.deserialize(replaceColorCodes(valueStr, replacementStyle))
                        : Component.text(valueStr);
            }

            resolvers.add(TagResolver.resolver(tagName, Tag.inserting(replacementComponent)));
        }

        return new ResolverResult(msg, resolvers.toArray(TagResolver[]::new));
    }

    private String replaceColorCodes(String msg) {
        return replaceColorCodes(msg, MessageStyle.NONE);
    }

    private String replaceColorCodes(String msg, MessageStyle style) {
        msg = msg.replace("{ac}", "<" + style.getAccentColor(plugin) + ">");

        for (MessageStyle currentStyle : MessageStyle.values()) {
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

}
