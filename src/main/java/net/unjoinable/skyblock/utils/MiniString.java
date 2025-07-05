package net.unjoinable.skyblock.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.minimessage.tag.standard.StandardTags;
import net.minestom.server.codec.Codec;

import java.util.ArrayList;
import java.util.List;

import static net.minestom.server.codec.Codec.STRING;

/**
 * Utility class for handling MiniMessage text formatting with custom statistic tags.
 * <p>
 * This class provides a centralized way to convert strings to Adventure Components
 * using MiniMessage formatting, with built-in support for statistic symbols and
 * automatic italic disabling.
 */
public final class MiniString {
    private static final TagResolver RESOLVER = createResolver();
    private static final MiniMessage MINI_MESSAGE = createMiniMessage();
    public static final Codec<Component> CODEC = STRING.transform(MINI_MESSAGE::deserialize, MINI_MESSAGE::serialize);

    private MiniString() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * Creates the TagResolver with fewer standard tags.
     *
     * @return configured TagResolver with standard tag support
     */
    private static TagResolver createResolver() {
        TagResolver.Builder resolverBuilder = TagResolver.builder();

        resolverBuilder.resolver(StandardTags.color());
        resolverBuilder.resolver(StandardTags.decorations());
        return resolverBuilder.build();
    }

    /**
     * Creates the MiniMessage instance with configured tags and preprocessor.
     *
     * @return configured MiniMessage instance
     */
    private static MiniMessage createMiniMessage() {
        return MiniMessage.builder()
                .tags(RESOLVER)
                .preProcessor(str -> "<!i>" + str)
                .build();
    }

    /**
     * Converts a MiniMessage string to an Adventure Component.
     * <p>
     * Automatically applies italic disabling and statistic symbol tag resolution.
     *
     * @param str the MiniMessage formatted string to convert
     * @return the parsed Adventure Component
     * @throws IllegalArgumentException if the string is null
     */
    public static Component asComponent(String str) {
        return MINI_MESSAGE.deserialize(str);
    }

    /**
     * Creates a new ListBuilder instance for building component lists.
     *
     * @return a new ListBuilder with default initial capacity
     */
    public static ListBuilder listBuilder() {
        return new ListBuilder();
    }

    /**
     * Builder class for creating lists of Components from MiniMessage strings.
     * <p>
     * Provides a fluent API for constructing lists of formatted components.
     */
    public static final class ListBuilder {
        private final List<Component> components;

        /**
         * Creates a new ListBuilder.
         */
        private ListBuilder() {
            this.components = new ArrayList<>();
        }

        /**
         * Adds a MiniMessage formatted string to the component list.
         *
         * @param str the MiniMessage formatted string to add
         * @return this builder instance for method chaining
         * @throws IllegalArgumentException if the string is null
         */
        public ListBuilder add(String str) {
            components.add(asComponent(str));
            return this;
        }

        /**
         * Adds a pre-built Component to the list.
         *
         * @param component the Component to add
         * @return this builder instance for method chaining
         * @throws IllegalArgumentException if the component is null
         */
        public ListBuilder add(Component component) {
            components.add(component);
            return this;
        }

        /**
         * Adds an empty line to the component list.
         *
         * @return this builder instance for method chaining
         */
        public ListBuilder addEmpty() {
            components.add(Component.empty());
            return this;
        }

        /**
         * Builds and returns the list of components.
         *
         * @return an immutable copy of the component list
         */
        public List<Component> build() {
            return List.copyOf(components);
        }
    }
}