package com.github.unjoinable.skyblock.statistics.holders;

import com.github.unjoinable.skyblock.statistics.Statistic;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A map-like class that stores StatModifiers instances for specific Statistics.
 * This class is designed to be more efficient than a traditional Map<Statistic, StatModifiers>
 * by using an array to store the StatModifiers instances, where the index of the array
 * corresponds to the ordinal value of the Statistic enum.
 */
public class StatModifiersMap {

    /**
     * The array of StatModifiers instances, where the index of the array corresponds to the
     * ordinal value of the Statistic enum.
     */
    private final StatModifiers[] statModifiers;

    /**
     * Constructs a new StatMap instance.
     */
    public StatModifiersMap() {
        statModifiers = new StatModifiers[Statistic.getValues().size()];
    }

    /**
     * Puts a StatModifiers instance into the StatMap for a specific Statistic.
     *
     * @param statistic the Statistic for which to put the StatModifiers instance
     * @param modifiers the StatModifiers instance to put into the StatMap
     */
    public void put(@NotNull Statistic statistic, @NotNull StatModifiers modifiers) {
        if (statModifiers[statistic.ordinal()] == null) {
            statModifiers[statistic.ordinal()] = new StatModifiers();
        }
        statModifiers[statistic.ordinal()].addStatModifiers(modifiers);
    }

    /**
     * Gets the StatModifiers instance for a specific Statistic from the StatMap.
     *
     * @param statistic the Statistic for which to get the StatModifiers instance
     * @return the StatModifiers instance for the specified Statistic, or null if no instance exists
     */
    public @Nullable StatModifiers get(@NotNull Statistic statistic) {
        return statModifiers[statistic.ordinal()];
    }

    /**
     * Checks if a StatModifiers instance exists for a specific Statistic in the StatMap.
     *
     * @param statistic the Statistic for which to check if a StatModifiers instance exists
     * @return true if a StatModifiers instance exists for the specified Statistic, false otherwise
     */
    public boolean has(@NotNull Statistic statistic) {
        return statModifiers[statistic.ordinal()] != null;
    }

    /**
     * Gets the StatModifiers instance for a specific Statistic from the StatMap, or creates a new instance if none exists.
     *
     * @param statistic the Statistic for which to get the StatModifiers instance
     * @return the existing or new StatModifiers instance for the specified Statistic
     */
    public @NotNull StatModifiers getOrNew(@NotNull Statistic statistic) {
        StatModifiers modifiers = get(statistic);
        if (modifiers == null) {
            modifiers = new StatModifiers();
            put(statistic, modifiers);
        }
        return modifiers;
    }

    /**
     * Adds all the StatModifiers instances from another StatMap to this StatMap.
     *
     * @param statMap the StatMap to add to this StatMap
     */
    public void addAll(@NotNull StatModifiersMap statMap) {
        for (Statistic statistic : Statistic.getValues()) {
            StatModifiers modifiers = statMap.get(statistic);
            if (modifiers != null) {
                put(statistic, modifiers);
            }
        }
    }

    /**
     * Removes all the StatModifiers instances from another StatMap to this StatMap.
     *
     * @param statMap the StatMap to remove from this StatMap
     */
    public void removeAll(@NotNull StatModifiersMap statMap) {
        for (Statistic statistic : Statistic.getValues()) {
            StatModifiers modifiers = statMap.get(statistic);
            if (modifiers != null) {
                statModifiers[statistic.ordinal()] = null;
            }
        }
    }

    /**
     * Gets all the stat modifiers in the map.
     *
     * @return an array of all stat modifiers in the map
     */
    public StatModifiers[] getAll() {
        return statModifiers;
    }
}
