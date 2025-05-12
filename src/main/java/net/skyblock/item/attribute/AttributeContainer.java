package net.skyblock.item.attribute;

import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMaps;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.skyblock.item.attribute.base.ItemAttribute;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Stream;

/**
 * An immutable container for item stack attributes.
 *
 * <p>This container stores attributes by their class type and provides
 * methods to retrieve and check for attributes. Modifications are handled
 * through the Builder pattern, ensuring immutability of the main container.</p>
 */
public final class AttributeContainer {
    /**
     * Retrieves the shared immutable instance of an empty attribute container.
     */
    private static final AttributeContainer EMPTY = new AttributeContainer(Object2ObjectMaps.emptyMap(), Object2ObjectMaps.emptyMap());

    private final Object2ObjectMap<String, ItemAttribute> attributeMap;
    private final Object2ObjectMap<Class<? extends ItemAttribute>, ItemAttribute> attributeByClassMap;

    /**
     * Constructs an immutable AttributeContainer using given maps.
     *
     * @param attributeMap          map of attribute IDs to attributes
     * @param attributeByClassMap   map of attribute classes to attributes
     */
    private AttributeContainer(
            @NotNull Object2ObjectMap<String, ItemAttribute> attributeMap,
            @NotNull Object2ObjectMap<Class<? extends ItemAttribute>, ItemAttribute> attributeByClassMap
    ) {
        this.attributeMap = Object2ObjectMaps.unmodifiable(attributeMap);
        this.attributeByClassMap = Object2ObjectMaps.unmodifiable(attributeByClassMap);
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
        return attribute != null ? Optional.of(type.cast(attribute)) : Optional.empty();
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
        builder.attributes.addAll(attributeMap.values());
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
        private final List<ItemAttribute> attributes = new ArrayList<>(8); // Initial capacity for small collections

        /**
         * Adds or replaces an attribute in the builder by both its ID and class type.
         *
         * @param attribute the attribute to add or replace; must not be null
         * @return this builder instance for chaining
         * @throws NullPointerException if the attribute is null
         */
        public @NotNull Builder with(@NotNull ItemAttribute attribute) {
            Objects.requireNonNull(attribute, "Attribute cannot be null");
            // Remove existing attributes with the same ID or class
            attributes.removeIf(a -> a.id().equals(attribute.id()) || a.getClass() == attribute.getClass());
            attributes.add(attribute);
            return this;
        }

        /**
         * Removes the attribute with the specified ID from the builder.
         *
         * @param id the attribute ID to remove
         * @return this builder instance for chaining
         */
        public @NotNull Builder without(@NotNull String id) {
            attributes.removeIf(a -> a.id().equals(id));
            return this;
        }

        /**
         * Removes the attribute of the specified class type from the builder, if present.
         *
         * @param type the attribute class to remove
         * @return this builder instance for chaining
         */
        public @NotNull Builder without(@NotNull Class<? extends ItemAttribute> type) {
            attributes.removeIf(a -> a.getClass() == type);
            return this;
        }

        /**
         * Retrieves the attribute associated with the specified ID.
         *
         * @param id the unique identifier of the attribute
         * @return the corresponding attribute, or null if no attribute with the given ID exists
         */
        public @Nullable ItemAttribute get(@NotNull String id) {
            for (ItemAttribute attr : attributes) {
                if (attr.id().equals(id)) {
                    return attr;
                }
            }
            return null;
        }

        /**
         * Retrieves an attribute by its class type.
         *
         * @param type the class of the attribute to retrieve
         * @param <T> the type of the attribute
         * @return an Optional containing the attribute instance if present, or an empty Optional if not found
         */
        public <T extends ItemAttribute> @NotNull Optional<T> get(@NotNull Class<T> type) {
            for (ItemAttribute attr : attributes) {
                if (type.isInstance(attr)) {
                    return Optional.of(type.cast(attr));
                }
            }
            return Optional.empty();
        }

        /**
         * Determines whether an attribute with the specified ID is present in the builder.
         *
         * @param id the attribute ID to check for presence
         * @return true if an attribute with the given ID exists; false otherwise
         */
        public boolean contains(@NotNull String id) {
            return get(id) != null;
        }

        /**
         * Determines whether an attribute of the specified class type is present in the builder.
         *
         * @param type the class of the attribute to check for
         * @return true if an attribute of the given class type exists; false otherwise
         */
        public boolean contains(@NotNull Class<? extends ItemAttribute> type) {
            return get(type).isPresent();
        }

        /**
         * Creates an immutable AttributeContainer containing the current set of attributes in the builder.
         *
         * @return a new immutable AttributeContainer with the builder's attributes
         */
        public @NotNull AttributeContainer build() {
            Object2ObjectOpenHashMap<String, ItemAttribute> attributeMap = new Object2ObjectOpenHashMap<>(attributes.size());
            attributeMap.defaultReturnValue(null);
            Object2ObjectOpenHashMap<Class<? extends ItemAttribute>, ItemAttribute> attributeByClassMap = new Object2ObjectOpenHashMap<>(attributes.size());
            attributeByClassMap.defaultReturnValue(null);

            for (ItemAttribute attr : attributes) {
                attributeMap.put(attr.id(), attr);
                attributeByClassMap.put(attr.getClass(), attr);
            }

            return new AttributeContainer(attributeMap, attributeByClassMap);
        }
    }
}
