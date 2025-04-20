package com.github.unjoinable.skyblock.item.component.components;

import com.github.unjoinable.skyblock.item.component.trait.NonPersistentComponent;
import com.github.unjoinable.skyblock.stats.StatProfile;
import com.github.unjoinable.skyblock.stats.Statistic;
import com.github.unjoinable.skyblock.stats.StatValueType;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Component that manages the base stats of an item.
 */
public final class BaseStatsComponent implements NonPersistentComponent {
    private final StatProfile baseStats;

    /**
     * Creates a new BaseStatsComponent with default stats
     */
    public BaseStatsComponent() {
        this.baseStats = new StatProfile(false);
    }

    /**
     * Creates a new BaseStatsComponent with specified base stats
     * @param baseStats The base stats for this item
     */
    public BaseStatsComponent(@NotNull StatProfile baseStats) {
        this.baseStats = Objects.requireNonNull(baseStats);
    }

    /**
     * Gets the base stats of the item without modifiers
     * @return The base stat profile
     */
    public @NotNull StatProfile getBaseStats() {
        return baseStats;
    }

    /**
     * Returns a new component with the given base stat added or modified
     * @param stat The statistic to modify
     * @param value The value to set/add
     * @return A new component with the modification
     */
    public BaseStatsComponent withStat(@NotNull Statistic stat, float value) {
        return withStat(stat, StatValueType.BASE, value);
    }

    /**
     * Returns a new component with the given base stat added or modified
     * @param stat The statistic to modify
     * @param type The type of modification
     * @param value The value to apply
     * @return A new component with the modification
     */
    public BaseStatsComponent withStat(@NotNull Statistic stat, @NotNull StatValueType type, float value) {
        StatProfile newBase = baseStats.copy();
        newBase.addStat(stat, type, value);
        return new BaseStatsComponent(newBase);
    }
}