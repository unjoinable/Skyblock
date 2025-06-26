package net.unjoinable.skyblock.item.attribute;

import net.minestom.server.codec.Codec;
import net.unjoinable.skyblock.item.attribute.traits.ItemAttribute;
import net.unjoinable.skyblock.registry.registries.CodecRegistry;
import net.unjoinable.skyblock.utility.AttributeContainerCodec;
import net.unjoinable.skyblock.utility.NamespaceId;

import java.util.*;
import java.util.stream.Stream;

/**
 * An immutable container for item stack attributes.
 *
 * <p>This container stores attributes by their ID and provides
 * methods to retrieve and check for attributes. Modifications are handled
 * through the Builder pattern, ensuring immutability of the main container.</p>
 */
public final class AttributeContainer implements Iterable<ItemAttribute> {
    /**
     * Retrieves the shared immutable instance of an empty attribute container.
     */
    public static final AttributeContainer EMPTY = new AttributeContainer(Collections.emptyMap());
    public static final Codec<AttributeContainer> CODEC = new AttributeContainerCodec(CodecRegistry.withDefaults());

    private final Map<NamespaceId, ItemAttribute> attributeMap;

    /**
     * Constructs an immutable AttributeContainer using given map.
     *
     * @param attributeMap map of attribute IDs to attributes
     */
    private AttributeContainer(Map<NamespaceId, ItemAttribute> attributeMap) {
        this.attributeMap = Map.copyOf(attributeMap);
    }

    /**
     * Retrieves the shared immutable instance of an empty attribute container.
     *
     * @return the shared empty AttributeContainer
     */
    public static AttributeContainer empty() {
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
    public Optional<ItemAttribute> get(NamespaceId id) {
        return Optional.ofNullable(attributeMap.get(id));
    }

    /**
     * Returns an attribute of the specified class type, if present.
     *
     * @param type the class of the attribute to retrieve
     * @param <T> the type of the attribute
     * @return an Optional containing the attribute instance if found, or an empty Optional if not present
     */
    public <T extends ItemAttribute> Optional<T> get(Class<T> type) {
        for (ItemAttribute attribute : attributeMap.values()) {
            if (type.isInstance(attribute)) {
                return Optional.of(type.cast(attribute));
            }
        }
        return Optional.empty();
    }

    /**
     * Determines whether an attribute with the specified ID is present in the container.
     *
     * @param id the attribute ID to check for presence
     * @return true if an attribute with the given ID exists; false otherwise
     */
    public boolean contains(NamespaceId id) {
        return attributeMap.containsKey(id);
    }

    /**
     * Determines whether an attribute of the specified class type is present in the container.
     *
     * @param type the class of the attribute to check for
     * @return true if an attribute of the given class type exists; false otherwise
     */
    public boolean contains(Class<? extends ItemAttribute> type) {
        for (ItemAttribute attribute : attributeMap.values()) {
            if (type.isInstance(attribute)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns an unmodifiable view of the attribute map keyed by attribute ID.
     *
     * @return an unmodifiable map of attribute IDs to their corresponding attributes
     */
    public Map<NamespaceId, ItemAttribute> asMap() {
        return attributeMap;
    }

    /**
     * Returns a stream of all attributes contained in this container.
     *
     * @return a stream of all item attributes
     */
    public Stream<ItemAttribute> stream() {
        return attributeMap.values().stream();
    }

    /**
     * Returns the number of attributes in this container.
     *
     * @return number of attributes in this container
     */
    public int size() {
        return attributeMap.size();
    }

    /**
     * Creates a new builder pre-populated with the attributes from this container.
     *
     * @return a builder containing copies of the current attributes
     */
    public Builder toBuilder() {
        Builder builder = new Builder();
        builder.attributes.addAll(attributeMap.values());
        return builder;
    }

    /**
     * Returns a new builder for creating an {@code AttributeContainer}.
     *
     * @return a new {@code AttributeContainer.Builder} instance
     */
    public static Builder builder() {
        return new Builder();
    }

    @Override
    public Iterator<ItemAttribute> iterator() {
        return attributeMap.values().iterator();
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
        public Builder with(ItemAttribute attribute) {
            Objects.requireNonNull(attribute, "Attribute cannot be null");
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
        public Builder without(NamespaceId id) {
            attributes.removeIf(a -> a.id().equals(id));
            return this;
        }

        /**
         * Removes the attribute of the specified class type from the builder, if present.
         *
         * @param type the attribute class to remove
         * @return this builder instance for chaining
         */
        public Builder without(Class<? extends ItemAttribute> type) {
            attributes.removeIf(a -> a.getClass() == type);
            return this;
        }

        /**
         * Creates an immutable AttributeContainer containing the current set of attributes in the builder.
         *
         * @return a new immutable AttributeContainer with the builder's attributes
         */
        public AttributeContainer build() {
            Map<NamespaceId, ItemAttribute> attributeMap = HashMap.newHashMap(attributes.size());

            for (ItemAttribute attr : attributes) {
                attributeMap.put(attr.id(), attr);
            }

            return new AttributeContainer(attributeMap);
        }
    }
}