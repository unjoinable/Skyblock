package net.unjoinable.skyblock.statistic;

/**
 * Represents the type of stat value.
 * Final Value =  (Base * (1 + Additive) * Multiplicative) + Bonus
 */
public enum StatValueType {
    /**
     * Represents a base stat value.
     * A base stat value is the initial value of a stat before any modifiers are applied.
     */
    BASE,

    /**
     * Represents a multiplicative stat modifier.
     * A multiplicative stat modifier is a value that is multiplied with the base stat value to produce the final stat value.
     */
    MULTIPLICATIVE,

    /**
     * Represents an additive stat modifier.
     * An additive stat modifier is a value that is added to the base stat value to produce the final stat value.
     */
    ADDITIVE,

    /**
     * A bonus value that is added to the final result of a stat calculation.
     */
    BONUS
}
