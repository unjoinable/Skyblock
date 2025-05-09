package net.skyblock.item.attribute;

import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMaps;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.skyblock.item.attribute.base.ItemAttribute;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * An immutable container for item stack attributes.
 *
 * <p>This container stores attributes by their class type and provides
 * methods to retrieve and check for attributes. Modifications are handled
 * through the Builder pattern, ensuring immutability of the main container.</p>
 */
public final class AttributeContainer {
    private static final AttributeContainer EMPTY = new AttributeContainer(new Builder());

    private final Object2ObjectMap<String, ItemAttribute> attributeMap;
    private final Object2ObjectMap<Class<? extends ItemAttribute>, ItemAttribute> attributeByClassMap;

    /**
     * Private constructor used by the Builder.
     *
     * @param builder the builder containing attributes to initialize with
     */
    private AttributeContainer(@NotNull Builder builder) {
        this.attributeMap = Object2ObjectMaps.unmodifiable(new Object2ObjectOpenHashMap<>(builder.attributeMap));
        this.attributeByClassMap = Object2ObjectMaps.unmodifiable(new Object2ObjectOpenHashMap<>(builder.attributeByClassMap));
    }

    /**
     * Returns the shared empty attribute container instance.
     *
     * @return a shared empty attribute container
     */
    public static @NotNull AttributeContainer empty() {
        return EMPTY;
    }

    /**
     * Checks if this container has any attributes.
     *
     * @return true if no attributes are present, false otherwise
     */
    public boolean isEmpty() {
        return attributeMap.isEmpty();
    }

    /**
     * Gets an attribute by its ID.
     *
     * @param id the unique identifier of the attribute
     * @return an Optional containing the attribute if present
     */
    public @NotNull Optional<ItemAttribute> get(@NotNull String id) {
        return Optional.ofNullable(attributeMap.get(id));
    }

    /**
     * Gets an attribute by its class type.
     *
     * @param type the class of the attribute to retrieve
     * @param <T> the type of attribute
     * @return an Optional containing the attribute if present
     */
    public <T extends ItemAttribute> @NotNull Optional<T> get(@NotNull Class<T> type) {
        ItemAttribute attribute = attributeByClassMap.get(type);
        if (attribute == null) {
            return Optional.empty();
        }

        //noinspection unchecked
        T castedAttribute = (T) attribute;
        return Optional.of(castedAttribute);
    }

    /**
     * Checks if an attribute with the given ID exists.
     *
     * @param id the attribute ID to check
     * @return true if an attribute with the ID exists, false otherwise
     */
    public boolean contains(@NotNull String id) {
        return attributeMap.containsKey(id);
    }

    /**
     * Checks if an attribute of the given type exists.
     *
     * @param type the attribute class to check
     * @return true if an attribute of that type exists, false otherwise
     */
    public boolean contains(@NotNull Class<? extends ItemAttribute> type) {
        return attributeByClassMap.containsKey(type);
    }

    /**
     * Returns a safe copy of the internal attribute map (id to attribute).
     * The returned map is unmodifiable to ensure immutability.
     *
     * @return unmodifiable copy of the attribute map
     */
    public @NotNull Map<String, ItemAttribute> asMap() {
        return Map.copyOf(attributeMap);
    }

    /**
     * Returns a safe copy of the internal attribute map (class to attribute).
     * The returned map is unmodifiable to ensure immutability.
     *
     * @return unmodifiable copy of the attribute type map
     */
    public @NotNull Map<Class<? extends ItemAttribute>, ItemAttribute> asTypeMap() {
        return Map.copyOf(attributeByClassMap);
    }

    /**
     * Returns a stream of all attributes in this container.
     * This allows for easy iteration.
     *
     * @return a stream of all attributes
     */
    public @NotNull Stream<ItemAttribute> stream() {
        return attributeMap.values().stream();
    }

    /**
     * Creates a new builder initialized with the current attributes.
     *
     * @return a builder instance with current attributes
     */
    public @NotNull Builder toBuilder() {
        Builder builder = new Builder();
        // Directly copy the maps instead of calling with() to avoid side effects in event handlers
        builder.attributeMap.putAll(attributeMap);
        builder.attributeByClassMap.putAll(attributeByClassMap);
        return builder;
    }

    /**
     * Creates a builder for constructing a new AttributeContainer.
     *
     * @return a new builder instance
     */
    public static @NotNull Builder builder() {
        return new Builder();
    }

    /**
     * Mutable builder for creating AttributeContainer instances.
     */
    public static final class Builder {
        final Object2ObjectMap<String, ItemAttribute> attributeMap = new Object2ObjectOpenHashMap<>();
        final Object2ObjectMap<Class<? extends ItemAttribute>, ItemAttribute> attributeByClassMap = new Object2ObjectOpenHashMap<>();

        /**
         * Adds or replaces an attribute in the builder.
         *
         * @param attribute the attribute to add
         * @return this builder for method chaining
         * @throws NullPointerException if attribute is null
         */
        public @NotNull Builder with(@NotNull ItemAttribute attribute) {
            Objects.requireNonNull(attribute, "Attribute cannot be null");

            attributeMap.put(attribute.id(), attribute);
            attributeByClassMap.put(attribute.getClass(), attribute);
            return this;
        }

        /**
         * Removes an attribute with the given ID from the builder.
         *
         * @param id the ID of the attribute to remove
         * @return this builder for method chaining
         */
        public @NotNull Builder without(@NotNull String id) {
            ItemAttribute removed = attributeMap.remove(id);
            if (removed != null) {
                attributeByClassMap.remove(removed.getClass());
            }
            return this;
        }

        /**
         * Removes an attribute of the given type from the builder.
         *
         * @param type the class of the attribute to remove
         * @return this builder for method chaining
         */
        public @NotNull Builder without(@NotNull Class<? extends ItemAttribute> type) {
            ItemAttribute removed = attributeByClassMap.remove(type);
            if (removed != null) {
                attributeMap.remove(removed.id());
            }
            return this;
        }

        /**
         * Gets an attribute by its ID.
         *
         * @param id the ID of the attribute to retrieve
         * @return the attribute or null if not found
         */
        public @Nullable ItemAttribute get(@NotNull String id) {
            return attributeMap.get(id);
        }

        /**
         * Gets an attribute by its class type.
         *
         * @param type the class of the attribute to retrieve
         * @param <T> the type of attribute
         * @return an Optional containing the attribute if present
         */
        public <T extends ItemAttribute> @NotNull Optional<T> get(@NotNull Class<T> type) {
            ItemAttribute attribute = attributeByClassMap.get(type);
            if (attribute == null) {
                return Optional.empty();
            }

            //noinspection unchecked
            T castedAttribute = (T) attribute;
            return Optional.of(castedAttribute);
        }

        /**
         * Checks if an attribute with the given ID exists.
         *
         * @param id the attribute ID to check
         * @return true if an attribute with the ID exists, false otherwise
         */
        public boolean contains(@NotNull String id) {
            return attributeMap.containsKey(id);
        }

        /**
         * Checks if an attribute of the given type exists.
         *
         * @param type the attribute class to check
         * @return true if an attribute of that type exists, false otherwise
         */
        public boolean contains(@NotNull Class<? extends ItemAttribute> type) {
            return attributeByClassMap.containsKey(type);
        }

        /**
         * Builds an immutable AttributeContainer with the current attributes.
         *
         * @return a new immutable AttributeContainer
         */
        public @NotNull AttributeContainer build() {
            return new AttributeContainer(this);
        }
    }
}