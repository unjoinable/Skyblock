package io.github.unjoinable.skyblock.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.NotNull;

public class MiniString {
    private static final MiniMessage miniMessage = MiniMessage.miniMessage();

    public static @NotNull Component toComponent(@NotNull String message) {
        return miniMessage.deserialize(message)
                .decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE)
                .colorIfAbsent(NamedTextColor.GRAY);
    }

    public static @NotNull Component toComponent(@NotNull String message, @NotNull TagResolver... tagResolver) {
        return miniMessage.deserialize(message, tagResolver)
                .decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE)
                .colorIfAbsent(NamedTextColor.GRAY);
    }

    public static @NotNull Component toComponent(@NotNull String message, @NotNull TagResolver tagResolver) {
        return miniMessage.deserialize(message, tagResolver)
                .decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE)
                .colorIfAbsent(NamedTextColor.GRAY);
    }
}
