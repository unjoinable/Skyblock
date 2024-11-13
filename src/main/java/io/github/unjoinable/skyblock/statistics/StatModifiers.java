package io.github.unjoinable.skyblock.statistics;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A class representing a collection of stat modifiers and their effective value.
 */
public class StatModifiers {
    private final List<StatModifier> modifiers;
    private double effectiveValue;

    /**
     * Constructs a new StatModifiers instance with the given list of modifiers.
     *
     * @param modifiers the initial list of modifiers
     */
    public StatModifiers(@NotNull List<StatModifier> modifiers) {
        this.modifiers = modifiers;
        calculateEffectiveValue();
    }

    /**
     * Constructs a new StatModifiers instance with an empty list of modifiers.
     */
    public StatModifiers() {
        this.modifiers = new ArrayList<>();
    }

    /**
     * Calculates the effective value of the stat modifiers.
     */
    private void calculateEffectiveValue() {
        var base = 0;
        var multiply = 1;
        var additive = 0;

        for (StatModifier modifier : modifiers) {
            switch (modifier.type()) {
                case BASE -> base += modifier.value();
                case MULTIPLICATIVE -> multiply *= modifier.value();
                case ADDITIVE -> additive += modifier.value();
            }
        }

        effectiveValue = base * (1 + additive) * multiply;
    }

    /**
     * Adds a modifier to the list of modifiers and recalculates the effective value.
     *
     * @param modifier the modifier to add
     */
    public void addModifier(@NotNull StatModifier modifier) {
        modifiers.add(modifier);
        calculateEffectiveValue();
    }

    /**
     * Removes a modifier from the list of modifiers and recalculates the effective value.
     *
     * @param modifier the modifier to remove
     */
    public void removeModifier(@NotNull StatModifier modifier) {
        modifiers.remove(modifier);
        calculateEffectiveValue();
    }

    /**
     * Removes a collection of modifiers from the list of modifiers and recalculates the effective value.
     *
     * @param modifiers the collection of modifiers to remove
     */
    public void removeModifiers(@NotNull Collection<StatModifier> modifiers) {
        this.modifiers.removeAll(modifiers);
        calculateEffectiveValue();
    }

    /**
     * Adds a collection of modifiers to the list of modifiers and recalculates the effective value.
     *
     * @param modifiers the collection of modifiers to add
     */
    public void addModifiers(@NotNull Collection<StatModifier> modifiers) {
        this.modifiers.addAll(modifiers);
        calculateEffectiveValue();
    }

    /**
     * Returns the effective value of the stat modifiers.
     *
     * @return the effective value
     */
    public double getEffectiveValue() {
        return effectiveValue;
    }
}
