package com.github.unjoinable.skyblock.item;

import com.github.unjoinable.skyblock.item.component.trait.NonPersistentComponent;
import com.github.unjoinable.skyblock.item.component.trait.StatModifierComponent;
import com.github.unjoinable.skyblock.item.enums.ModifierType;
import com.github.unjoinable.skyblock.stats.StatProfile;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Component that manages stat modifiers for an item.
 */
public final class StatModifierManager implements NonPersistentComponent {
    private final List<StatModifierComponent> modifiers;

    /**
     * Creates a new StatModifierManager with no modifiers
     */
    public StatModifierManager() {
        this.modifiers = new ArrayList<>();
    }

    /**
     * Creates a new StatModifierManager with initial modifiers
     * @param modifiers The initial stat modifiers
     */
    public StatModifierManager(@NotNull List<StatModifierComponent> modifiers) {
        this.modifiers = new ArrayList<>(modifiers);
    }

    /**
     * Gets an unmodifiable list of all stat modifiers
     * @return List of stat modifiers
     */
    public @NotNull List<StatModifierComponent> getModifiers() {
        return Collections.unmodifiableList(modifiers);
    }

    /**
     * Returns a new manager with the given modifier added
     * @param modifier The stat modifier to add
     * @return A new manager with the modifier
     */
    public StatModifierManager withModifier(@NotNull StatModifierComponent modifier) {
        List<StatModifierComponent> newModifiers = new ArrayList<>(modifiers);
        newModifiers.add(modifier);
        return new StatModifierManager(newModifiers);
    }

    /**
     * Returns a new manager with the given type of modifiers removed
     * @param type The type of modifiers to remove
     * @return A new manager without the modifiers
     */
    public StatModifierManager withoutModifier(@NotNull ModifierType type) {
        List<StatModifierComponent> newModifiers = new ArrayList<>(modifiers.size());

        for (StatModifierComponent modifier : modifiers) {
            if (modifier.getModifierType() != type) {
                newModifiers.add(modifier);
            }
        }

        return new StatModifierManager(newModifiers);
    }

    /**
     * Applies all modifiers to a base stat profile
     * @param baseStats The base stats to modify
     * @return A new StatProfile with all modifiers applied
     */
    public @NotNull StatProfile applyModifiers(@NotNull StatProfile baseStats) {
        StatProfile result = baseStats.copy();

        for (StatModifierComponent modifier : modifiers) {
            result.combineWith(modifier.getStatProfile());
        }

        return result;
    }
}