package io.github.unjoinable.skyblock.statistics.holders;

/**
 * Represents the type of a stat value.
 */
public enum StatValueType {
    /**
     * Represents a base stat value.
     * A base stat value is the initial value of a stat before any modifiers are applied.
     */
    BASE(0),

    /**
     * Represents a multiplicative stat modifier.
     * A multiplicative stat modifier is a value that is multiplied with the base stat value to produce the final stat value.
     */
    MULTIPLICATIVE(1),

    /**
     * Represents an additive stat modifier.
     * An additive stat modifier is a value that is added to the base stat value to produce the final stat value.
     */
    ADDITIVE(2);

    /**
     * The index of the stat value type.
     */
    private final int index;

    /**
     * Constructs a new stat value type with the specified index.
     *
     * @param index the index of the stat value type
     */
    StatValueType(int index) {
        this.index = index;
    }

    /**
     * Returns the index of the stat value type.
     *
     * @return the index of the stat value type
     */
    public int getIndex() {
        return index;
    }
}
