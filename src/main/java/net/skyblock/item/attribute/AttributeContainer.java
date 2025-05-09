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
     * Constructs an immutable AttributeContainer from the provided Builder.
     *
     * Copies the builder's attribute maps and wraps them in unmodifiable views to ensure immutability.
     *
     * @param builder the Builder containing the attributes to initialize this container with
     */
    private AttributeContainer(@NotNull Builder builder) {
        this.attributeMap = Object2ObjectMaps.unmodifiable(new Object2ObjectOpenHashMap<>(builder.attributeMap));
        this.attributeByClassMap = Object2ObjectMaps.unmodifiable(new Object2ObjectOpenHashMap<>(builder.attributeByClassMap));
    }

    /**
     * Retrieves the shared immutable instance of an empty attribute container.
     *
     * @return the shared empty AttributeContainer
     */
    public static @NotNull AttributeContainer empty() {
        return EMPTY;
    }

    /**
     * Returns whether the container has no attributes.
     *
     * @return true if the container contains no attributes; false otherwise
     */
    public boolean isEmpty() {
        return attributeMap.isEmpty();
    }

    /**
     * Returns the attribute associated with the specified ID, if present.
     *
     * @param id the unique identifier of the attribute
     * @return an {@code Optional} containing the attribute if found, or an empty {@code Optional} if not present
     */
    public @NotNull Optional<ItemAttribute> get(@NotNull String id) {
        return Optional.ofNullable(attributeMap.get(id));
    }

    /**
     * Returns an attribute of the specified class type, if present.
     *
     * @param type the class of the attribute to retrieve
     * @param <T> the type of the attribute
     * @return an Optional containing the attribute instance if found, or an empty Optional if not present
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
     * Determines whether an attribute with the specified ID is present in the container.
     *
     * @param id the attribute ID to check for presence
     * @return true if an attribute with the given ID exists; false otherwise
     */
    public boolean contains(@NotNull String id) {
        return attributeMap.containsKey(id);
    }

    /**
     * Determines whether an attribute of the specified class type is present in the container.
     *
     * @param type the class of the attribute to check for
     * @return true if an attribute of the given class type exists; false otherwise
     */
    public boolean contains(@NotNull Class<? extends ItemAttribute> type) {
        return attributeByClassMap.containsKey(type);
    }

    /**
     * Returns an unmodifiable view of the attribute map keyed by attribute ID.
     *
     * @return an unmodifiable map of attribute IDs to their corresponding attributes
     */
    public @NotNull Map<String, ItemAttribute> asMap() {
        return Map.copyOf(attributeMap);
    }

    /**
     * Returns an unmodifiable view of the attribute map keyed by attribute class type.
     *
     * @return an unmodifiable map from attribute class to attribute instance
     */
    public @NotNull Map<Class<? extends ItemAttribute>, ItemAttribute> asTypeMap() {
        return Map.copyOf(attributeByClassMap);
    }

    /**
     * Returns a stream of all attributes contained in this container.
     *
     * @return a stream of all item attributes
     */
    public @NotNull Stream<ItemAttribute> stream() {
        return attributeMap.values().stream();
    }

    /**
     * Creates a new builder pre-populated with the attributes from this container.
     *
     * @return a builder containing copies of the current attributes
     */
    public @NotNull Builder toBuilder() {
        Builder builder = new Builder();
        // Directly copy the maps instead of calling with() to avoid side effects in event handlers
        builder.attributeMap.putAll(attributeMap);
        builder.attributeByClassMap.putAll(attributeByClassMap);
        return builder;
    }

    /**
     * Returns a new builder for creating an {@code AttributeContainer}.
     *
     * @return a new {@code AttributeContainer.Builder} instance
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
         * Adds or replaces an attribute in the builder by both its ID and class type.
         *
         * @param attribute the attribute to add or replace; must not be null
         * @return this builder instance for chaining
         * @throws NullPointerException if the attribute is null
         */
        public @NotNull Builder with(@NotNull ItemAttribute attribute) {
            Objects.requireNonNull(attribute, "Attribute cannot be null");

            attributeMap.put(attribute.id(), attribute);
            attributeByClassMap.put(attribute.getClass(), attribute);
            return this;
        }

        /**
         * Removes the attribute with the specified ID from the builder, also removing its class mapping if present.
         *
         * @param id the attribute ID to remove
         * @return this builder instance for chaining
         */
        public @NotNull Builder without(@NotNull String id) {
            ItemAttribute removed = attributeMap.remove(id);
            if (removed != null) {
                attributeByClassMap.remove(removed.getClass());
            }
            return this;
        }

        /**
         * Removes the attribute of the specified class type from the builder, if present.
         *
         * @param type the attribute class to remove
         * @return this builder instance for chaining
         */
        public @NotNull Builder without(@NotNull Class<? extends ItemAttribute> type) {
            ItemAttribute removed = attributeByClassMap.remove(type);
            if (removed != null) {
                attributeMap.remove(removed.id());
            }
            return this;
        }

        /**
         * Retrieves the attribute associated with the specified ID.
         *
         * @param id the unique identifier of the attribute
         * @return the corresponding attribute, or null if no attribute with the given ID exists
         */
        public @Nullable ItemAttribute get(@NotNull String id) {
            return attributeMap.get(id);
        }

        /**
         * Retrieves an attribute by its class type.
         *
         * @param type the class of the attribute to retrieve
         * @param <T> the type of the attribute
         * @return an Optional containing the attribute instance if present, or an empty Optional if not found
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
         * Determines whether an attribute with the specified ID is present in the container.
         *
         * @param id the attribute ID to check for presence
         * @return true if an attribute with the given ID exists; false otherwise
         */
        public boolean contains(@NotNull String id) {
            return attributeMap.containsKey(id);
        }

        /**
         * Determines whether an attribute of the specified class type is present in the container.
         *
         * @param type the class of the attribute to check for
         * @return true if an attribute of the given class type exists; false otherwise
         */
        public boolean contains(@NotNull Class<? extends ItemAttribute> type) {
            return attributeByClassMap.containsKey(type);
        }

        /**
         * Creates an immutable AttributeContainer containing the current set of attributes in the builder.
         *
         * @return a new immutable AttributeContainer with the builder's attributes
         */
        public @NotNull AttributeContainer build() {
            return new AttributeContainer(this);
        }
    }
}