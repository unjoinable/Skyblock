package net.skyblock.stats.definition;

import net.minestom.server.entity.Entity;
import net.minestom.server.entity.damage.DamageType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * Immutable damage record that efficiently stores damage properties.
 *
 * @param value The base damage value
 * @param source The entity that caused the damage (can be null for server-based damage)
 * @param target The entity that receives the damage
 * @param attributes Map of additional damage attributes
 */
public record Damage(
        double value,
        @Nullable Entity source,
        @NotNull Entity target,
        @NotNull Map<DamageAttribute<?>, Object> attributes) {

    /**
     * Creates basic damage with just a value.
     *
     * @param value The base damage value
     * @param target The entity that receives the damage
     */
    public Damage(double value, @NotNull Entity target) {
        this(value, null, target, Map.of());
    }

    /**
     * Creates damage with specified source and target.
     *
     * @param value The base damage value
     * @param source The entity that caused the damage
     * @param target The entity that receives the damage
     */
    public Damage(double value, @Nullable Entity source, @NotNull Entity target) {
        this(value, source, target, Map.of());
    }

    /**
     * Builder pattern for creating complex damage properties efficiently.
     *
     * @param value The base damage value
     * @param target The entity that receives the damage
     * @return A new builder for creating Damage instances
     */
    public static Builder builder(double value, @NotNull Entity target) {
        return new Builder(value, null, target);
    }

    /**
     * Builder pattern for creating complex damage properties efficiently.
     *
     * @param value The base damage value
     * @param source The entity that caused the damage
     * @param target The entity that receives the damage
     * @return A new builder for creating Damage instances
     */
    public static Builder builder(double value, @Nullable Entity source, @NotNull Entity target) {
        return new Builder(value, source, target);
    }

    /**
     * Returns new Damage with an additional attribute.
     *
     * @param attribute The attribute to add or update
     * @param value The value for the attribute
     * @param <T> The type of the attribute value
     * @return New Damage with the updated attribute
     */
    public <T> Damage withAttribute(DamageAttribute<T> attribute, T value) {
        Map<DamageAttribute<?>, Object> newAttributes = new HashMap<>(attributes);
        newAttributes.put(attribute, value);
        return new Damage(this.value, source, target, newAttributes);
    }

    /**
     * Returns new Damage with an additional attribute using AttributeValue.
     * Provides a more fluent API when used with DamageAttribute.of() methods.
     *
     * @param attributeValue The attribute value pair to add
     * @param <T> The type of the attribute value
     * @return New Damage with the updated attribute
     */
    public <T> Damage withAttribute(DamageAttribute.AttributeValue<T> attributeValue) {
        return withAttribute(attributeValue.getAttribute(), attributeValue.getValue());
    }

    /**
     * Returns new Damage with a modified damage value.
     *
     * @param newValue The new damage value
     * @return New Damage with the updated value
     */
    public Damage withValue(double newValue) {
        return new Damage(newValue, source, target, attributes);
    }

    /**
     * Returns new Damage with a modified source.
     *
     * @param newSource The new damage source
     * @return New Damage with the updated source
     */
    public Damage withSource(@Nullable Entity newSource) {
        return new Damage(value, newSource, target, attributes);
    }

    /**
     * Returns new Damage with a modified target.
     *
     * @param newTarget The new damage target
     * @return New Damage with the updated target
     */
    public Damage withTarget(@NotNull Entity newTarget) {
        return new Damage(value, source, newTarget, attributes);
    }

    /**
     * Checks if this damage has a specific attribute.
     *
     * @param attribute The attribute to check for
     * @return true if the attribute exists, false otherwise
     */
    public boolean hasAttribute(DamageAttribute<?> attribute) {
        return attributes.containsKey(attribute);
    }

    /**
     * Gets the value of a specific attribute.
     *
     * @param attribute The attribute to get the value for
     * @param <T> The type of the attribute value
     * @return The attribute value or null if not present
     */
    @SuppressWarnings("unchecked")
    public <T> T getAttribute(DamageAttribute<T> attribute) {
        return (T) attributes.get(attribute);
    }

    /**
     * Gets the value of a specific attribute or a default value if not present.
     *
     * @param attribute The attribute to get the value for
     * @param defaultValue The default value to return if the attribute doesn't exist
     * @param <T> The type of the attribute value
     * @return The attribute value or the default value
     */
    @SuppressWarnings("unchecked")
    public <T> T getAttribute(DamageAttribute<T> attribute, T defaultValue) {
        return (T) attributes.getOrDefault(attribute, defaultValue);
    }

    /**
     * Helper method to check if this damage is of a specific boolean type.
     *
     * @param typeAttribute The damage type attribute to check
     * @return true if this damage has the specified type, false otherwise
     */
    public boolean isDamageType(DamageAttribute<Boolean> typeAttribute) {
        return getAttribute(typeAttribute, false);
    }

    /**
     * Converts this Damage instance to a Minestom Damage object.
     * This is used for integration with the Minestom damage system.
     *
     * @return A Minestom Damage object representing this damage
     * The damage type is set to GENERIC
     * The position is set to the source's position, or if no source exists, the target's position
     * The value is converted to a float
     */
    public net.minestom.server.entity.damage.Damage toMinestomDamage() {
        return new net.minestom.server.entity.damage.Damage(
                DamageType.GENERIC,
                source,
                target,
                source != null ? source.getPosition() : target.getPosition(),
                ((float) value)
        );
    }

    /**
     * Builder class for creating Damage instances efficiently.
     */
    public static class Builder {
        private final double value;
        private final @Nullable Entity source;
        private final @NotNull Entity target;
        private final Map<DamageAttribute<?>, Object> attributes;

        private Builder(double value, @Nullable Entity source, @NotNull Entity target) {
            this.value = value;
            this.source = source;
            this.target = target;
            this.attributes = new HashMap<>();
        }

        /**
         * Adds an attribute to the damage.
         *
         * @param attribute The attribute to add
         * @param value The value for the attribute
         * @param <T> The type of the attribute value
         * @return This builder instance for method chaining
         */
        public <T> Builder attribute(DamageAttribute<T> attribute, T value) {
            attributes.put(attribute, value);
            return this;
        }

        /**
         * Adds an attribute to the damage using AttributeValue.
         * Provides a more fluent API when used with DamageAttribute.of() methods.
         *
         * @param attributeValue The attribute value pair to add
         * @param <T> The type of the attribute value
         * @return This builder instance for method chaining
         */
        public <T> Builder attribute(DamageAttribute.AttributeValue<T> attributeValue) {
            attributes.put(attributeValue.getAttribute(), attributeValue.getValue());
            return this;
        }

        /**
         * Sets the damage type by adding the corresponding boolean attribute.
         *
         * @param typeAttribute The damage type attribute to set
         * @return This builder instance for method chaining
         */
        public Builder damageType(DamageAttribute<Boolean> typeAttribute) {
            attributes.put(typeAttribute, true);
            return this;
        }

        /**
         * Builds the final Damage instance.
         *
         * @return A new immutable Damage
         */
        public Damage build() {
            return new Damage(value, source, target, attributes);
        }
    }
}