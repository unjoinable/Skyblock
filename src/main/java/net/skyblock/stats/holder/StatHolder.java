package net.skyblock.stats.holder;

import net.skyblock.stats.calculator.StatProfile;
import org.jetbrains.annotations.NotNull;

/**
 * Represents an object that contains game statistics and can provide them as a StatProfile.
 * Implemented by players and mobs I suppose.
 */
public interface StatHolder {

    /**
     * Gets the StatProfile containing all stats from this holder.
     * @return An immutable snapshot of current stats
     */
    @NotNull StatProfile getStatProfile();

}
