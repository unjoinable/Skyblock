package net.skyblock.stats.definition;

/**
 * A generic attribute key that can be associated with damage instances.
 * These attributes modify or add special effects to damage calculations.
 *
 * @param <T> The type of value this attribute holds
 */
public class DamageAttribute<T> implements DamageAttributes {
    private final T defaultValue;
    private final String name;

    /**
     * Creates a new damage attribute with the specified default value.
     *
     * @param defaultValue The default value for this attribute
     * @param name The name of the attribute for debugging purposes
     */
    private DamageAttribute(T defaultValue, String name) {
        this.defaultValue = defaultValue;
        this.name = name;
    }

    /**
     * Creates a new damage attribute with the specified default value.
     *
     * @param defaultValue The default value for this attribute
     * @param <T> The type of value this attribute holds
     * @return A new damage attribute
     */
    public static <T> DamageAttribute<T> of(T defaultValue) {
        return new DamageAttribute<>(defaultValue, null);
    }

    /**
     * Creates a new damage attribute with the specified default value and name.
     *
     * @param defaultValue The default value for this attribute
     * @param name The name of the attribute for debugging purposes
     * @param <T> The type of value this attribute holds
     * @return A new damage attribute
     */
    public static <T> DamageAttribute<T> of(T defaultValue, String name) {
        return new DamageAttribute<>(defaultValue, name);
    }

    /**
     * Creates a value instance of this attribute with the default value.
     * Useful for fluent chaining with withAttribute().
     *
     * @return A new AttributeValue with this attribute and its default value
     */
    public AttributeValue<T> withDefaultValue() {
        return new AttributeValue<>(this, defaultValue);
    }

    /**
     * Creates a value instance of this attribute with a custom value.
     * Useful for fluent chaining with withAttribute().
     *
     * @param value The custom value for this attribute instance
     * @return A new AttributeValue with this attribute and the specified value
     */
    public AttributeValue<T> withValue(T value) {
        return new AttributeValue<>(this, value);
    }

    /**
     * Gets the default value for this attribute.
     *
     * @return The default value
     */
    public T getDefaultValue() {
        return defaultValue;
    }

    /**
     * Gets the name of this attribute.
     *
     * @return The name of the attribute or null if not specified
     */
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        if (name != null) {
            return name;
        }
        return super.toString();
    }

    /**
     * Value holder class that combines an attribute with its value.
     * Useful for fluent API patterns.
     *
     * @param <T> The type of the value
     */
    public static class AttributeValue<T> {
        private final DamageAttribute<T> attribute;
        private final T value;

        private AttributeValue(DamageAttribute<T> attribute, T value) {
            this.attribute = attribute;
            this.value = value;
        }

        /**
         * Gets the attribute key.
         *
         * @return The attribute key
         */
        public DamageAttribute<T> getAttribute() {
            return attribute;
        }

        /**
         * Gets the value for this attribute.
         *
         * @return The value
         */
        public T getValue() {
            return value;
        }
    }
}