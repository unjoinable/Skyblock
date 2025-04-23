package com.github.unjoinable.skyblock.utils;

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

public final class MiniString {
    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();
    private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("\\{}");
    private static final Component EMPTY_COMPONENT = Component.empty();

    private MiniString() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    @NotNull
    public static Component component(@Nullable String input, @Nullable Object... values) {
        if (input == null || input.isEmpty()) return EMPTY_COMPONENT;

        // Fast path for simple strings with no placeholders
        if (values == null || values.length == 0 || !input.contains("{}")) {
            return MINI_MESSAGE.deserialize(input);
        }

        // Process placeholders
        StringBuilder transformed = new StringBuilder(input.length() + values.length * 8);
        List<TagResolver> resolvers = new ArrayList<>(values.length);

        Matcher matcher = PLACEHOLDER_PATTERN.matcher(input);
        int lastEnd = 0;
        int valueIndex = 0;

        while (matcher.find() && valueIndex < values.length) {
            transformed.append(input, lastEnd, matcher.start())
                    .append("{value").append(valueIndex).append("}");

            Object value = values[valueIndex];
            resolvers.add(value instanceof Component ?
                    Placeholder.component("value" + valueIndex, (Component) value) :
                    Placeholder.parsed("value" + valueIndex, value == null ? "" : String.valueOf(value)));

            lastEnd = matcher.end();
            valueIndex++;
        }

        if (lastEnd < input.length()) {
            transformed.append(input, lastEnd, input.length());
        }

        return MINI_MESSAGE.deserialize(transformed.toString(), TagResolver.resolver(resolvers));
    }
}