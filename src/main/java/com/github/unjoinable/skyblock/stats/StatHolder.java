package com.github.unjoinable.skyblock.stats;

import org.jetbrains.annotations.NotNull;

/**
 * Represents an object that contains game statistics and can provide them as a StatProfile.
 * Implemented by players, items, equipment, buffs, etc.
 */
public interface StatHolder {

    /**
     * Gets the StatProfile containing all stats from this holder.
     * @return An immutable snapshot of current stats
     */
    @NotNull StatProfile getStatProfile();

    /**
     * Gets the combined stats from this holder after applying all modifiers.
     * @return A live StatProfile that automatically updates with changes
     */
    default StatProfile getStats() {
        return getStatProfile().copy(); // Default returns immutable copy
    }

}
