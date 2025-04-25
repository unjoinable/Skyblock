package net.skyblock.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class for working with {@link Component} and the MiniMessage library.
 * <p>
 * This class provides a method to create a {@link Component} from a string with placeholders,
 * and is able to replace those placeholders with corresponding values.
 * <p>
 * Placeholders are represented by "{}" in the input string and are replaced by the
 * provided values in the order they appear. The result is a deserialized {@link Component}
 * that can be used in Minestom-based server code.
 */
public final class MiniString {
    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();
    private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("\\{}");
    private static final Component EMPTY_COMPONENT = Component.empty();

    /**
     * Private constructor to prevent instantiation of the utility class.
     * This class should not be instantiated as it only contains static methods.
     */
    private MiniString() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * Creates a {@link Component} from a string with optional placeholders.
     * <p>
     * The input string may contain placeholders represented by "{}". Each placeholder will be replaced
     * by the corresponding value from the {@code values} array in the order they appear. If the input string
     * doesn't contain placeholders, it will be simply deserialized using MiniMessage.
     * <p>
     * If the string is {@code null} or empty, an empty component is returned.
     *
     * @param input the string input with placeholders to be replaced, or {@code null} to return an empty component
     * @param values the values to replace the placeholders, in the order they appear
     * @return a {@link Component} representing the processed string with placeholders replaced
     */
    @NotNull
    public static Component component(@Nullable String input, @Nullable Object... values) {
        if (input == null || input.isEmpty()) return EMPTY_COMPONENT;

        if (values == null || values.length == 0 || !input.contains("{}")) {
            return MINI_MESSAGE.deserialize(input);
        }

        StringBuilder transformed = new StringBuilder(input.length() + values.length * 8);
        List<TagResolver> resolvers = new ArrayList<>(values.length);

        Matcher matcher = PLACEHOLDER_PATTERN.matcher(input);
        int lastEnd = 0;
        int valueIndex = 0;

        while (matcher.find() && valueIndex < values.length) {
            transformed.append(input, lastEnd, matcher.start())
                    .append("{value").append(valueIndex).append("}");

            Object value = values[valueIndex];

            if (value instanceof Component component) {
                resolvers.add(Placeholder.component("value" + valueIndex, component));
            }

            resolvers.add(Placeholder.parsed("value" + valueIndex, value == null ? "" : String.valueOf(value)));


            lastEnd = matcher.end();
            valueIndex++;
        }

        if (lastEnd < input.length()) {
            transformed.append(input, lastEnd, input.length());
        }

        return MINI_MESSAGE.deserialize(transformed.toString(), TagResolver.resolver(resolvers));
    }
}
