package io.github.unjoinable.skyblock.statistics.holders;

/**
 * Represents a collection of stat modifiers.
 * <p>
 * This class provides methods for adding and removing stat modifiers, as well as calculating the effective value of the modifiers.
 */
public class StatModifiers {
    /**
     * The default base value.
     */
    private static final double DEFAULT_BASE_VALUE = 0.0;

    /**
     * The default multiplicative value.
     */
    private static final double DEFAULT_MULTIPLY_VALUE = 1.0;

    /**
     * The default additive value.
     */
    private static final double DEFAULT_ADDITIVE_VALUE = 0.0;

    /**
     * The values of the stat modifiers.
     * <p> Index 0: base modifier
     * <p> Index 1: multiplicative modifier
     * <p> Index 2: additive modifier
     */
    private final double[] values;

    /**
     * The modifiers that have been added to this collection.
     * <p> Index 0: base modifier
     * <p> Index 1: multiplicative modifier
     * <p> Index 2: additive modifier
     */
    private final Object[] modifiers;

    /**
     * The cached effective value of the modifiers.
     */
    private double effectiveValue;

    /**
     * A flag indicating whether the effective value needs to be recalculated.
     */
    private boolean isEffectiveValueDirty;

    /**
     * Constructs a new StatModifiers instance with default values.
     */
    public StatModifiers() {
        values = new double[3];
        values[0] = DEFAULT_BASE_VALUE;
        values[1] = DEFAULT_MULTIPLY_VALUE;
        values[2] = DEFAULT_ADDITIVE_VALUE;
        modifiers = new Object[3];
        effectiveValue = (values[0] + values[2]) * values[1];
        isEffectiveValueDirty = true;
    }

    /**
     * Adds a stat modifier to this collection.
     *
     * @param modifier the modifier to add
     */
    public void addModifier(StatModifier modifier) {
        int index = modifier.type().getIndex();
        if (index == 1) {
            values[index] *= modifier.value();
        } else {
            values[index] += modifier.value();
        }
        modifiers[index] = modifier;
        isEffectiveValueDirty = true;
    }

    /**
     * Removes a stat modifier from this collection.
     *
     * @param modifier the modifier to remove
     */
    public void removeModifier(StatModifier modifier) {
        int index = modifier.type().getIndex();
        if (index == 1) {
            values[index] /= modifier.value();
        } else {
            values[index] -= modifier.value();
        }
        modifiers[index] = null;
        isEffectiveValueDirty = true;
    }

    /**
     * Adds multiple stat modifiers to this collection.
     *
     * @param modifiers the modifiers to add
     */
    public void addModifiers(StatModifier[] modifiers) {
        for (StatModifier modifier : modifiers) {
            int index = modifier.type().getIndex();
            if (index == 1) {
                values[index] *= modifier.value();
            } else {
                values[index] += modifier.value();
            }
            this.modifiers[index] = modifier;
        }
        isEffectiveValueDirty = true;
    }

    /**
     * Removes multiple stat modifiers from this collection.
     *
     * @param modifiers the modifiers to remove
     */
    public void removeModifiers(StatModifier[] modifiers) {
        for (StatModifier modifier : modifiers) {
            int index = modifier.type().getIndex();
            if (index == 1) {
                values[index] /= modifier.value();
            } else {
                values[index] -= modifier.value();
            }
            if (this.modifiers[index] == modifier) {
                this.modifiers[index] = null;
            }
        }
        isEffectiveValueDirty = true;
    }

    /**
     * Returns the effective value of the modifiers.
     *
     * @return the effective value
     */
    public double getEffectiveValue() {
        if (isEffectiveValueDirty) {
            updateEffectiveValue();
            isEffectiveValueDirty = false;
        }
        return effectiveValue;
    }

    /**
     * Updates the effective value of the modifiers.
     */
    private void updateEffectiveValue() {
        effectiveValue = (values[0] + values[2]) * values[1];
    }

    /**
     * Returns the modifiers that have been added to this collection.
     *
     * @return an array of StatModifier instances
     */
    public StatModifier[] getModifiers() {
        return (StatModifier[]) modifiers;
    }

    /**
     * Adds the modifiers from another StatModifiers instance to this collection.
     *
     * @param statModifiers the StatModifiers instance to add modifiers from
     */
    public void addStatModifiers(StatModifiers statModifiers) {
        if (statModifiers != null) {
            for (Object modifier : statModifiers.modifiers) {
                if (modifier != null) {
                    addModifier((StatModifier) modifier);
                }
            }
        }
    }

}
