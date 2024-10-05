package io.github.unjoinable.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

/**
 * This class provides utility methods for working with MiniMessage, a powerful text formatting library.
 * @since 1.0.0
 * @author Minikloon
 */
public final class MiniMessageTemplate {
    /**
     * A processor for StringTemplate that converts a StringTemplate to a MiniMessage Component.
     * @since 1.0.0
     */
    public static final StringTemplate.Processor<Component, RuntimeException> MM = stringTemplate -> {
        String interpolated = STR.process(stringTemplate);
        return toComponent(interpolated);
    };

    /**
     * Converts a string into a MiniMessage Component.
     * The input string is deserialized using MiniMessage's deserialize method,
     * "<!i>" added to disable italics.
     * @param string The input string to be converted.
     * @return The resulting MiniMessage Component.
     * @since 1.0.0
     */
    public static Component toComponent(String string) {
        return MiniMessage.miniMessage().deserialize("<!i>" + string);
    }
}