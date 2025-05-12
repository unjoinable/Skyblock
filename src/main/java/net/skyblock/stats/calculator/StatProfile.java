package net.skyblock.stats.calculator;

import net.skyblock.stats.definition.Statistic;
import net.skyblock.stats.definition.StatValueType;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a profile of character statistics with optimized performance characteristics.
 * <p>
 * This implementation uses array-based storage and bitwise dirty flag tracking for efficient
 * updates and calculations. Statistics are automatically recalculated only when needed.
 * </p>
 *
 * <p>Key features:</p>
 * <ul>
 *   <li>O(1) stat access for clean values</li>
 *   <li>Bitwise dirty flag tracking (supports up to 64 statistics)</li>
 *   <li>Array-based storage for memory efficiency</li>
 *   <li>Automatic cap enforcement during recalculation</li>
 * </ul>
 */
public class StatProfile {
    private static final int STAT_COUNT = Statistic.values().length;
    private static final int MAX_SUPPORTED_STATS = 64;

    private final double[] base = new double[STAT_COUNT];
    private final double[] additive = new double[STAT_COUNT];
    private final double[] multiplicative = new double[STAT_COUNT];
    private final double[] bonus = new double[STAT_COUNT];
    private final double[] cached = new double[STAT_COUNT];
    private long dirtyFlags = ~0L;

    /**
     * Constructs a new StatProfile with default values.
     * <p>
     * Initializes all statistics to their base values as defined in the {@link Statistic} enum
     * and marks all values as needing recalculation.
     * </p>
     */
    public StatProfile() {
        for (Statistic stat : Statistic.values()) {
            base[stat.ordinal()] = stat.getBaseValue();
        }
    }

    /**
     * Retrieves the current value of a statistic, recalculating if necessary.
     *
     * @param stat The statistic to retrieve, must not be {@code null}
     * @return The calculated value of the statistic, respecting defined caps
     * @throws IllegalArgumentException If the statistic's ordinal exceeds 63
     */
    public double get(@NotNull Statistic stat) {
        final int id = stat.ordinal();
        validateStatId(id);

        if (isDirty(id)) {
            recalculate(id);
        }
        return cached[id];
    }

    /**
     * Modifies a statistic value using the specified modifier type.
     *
     * @param stat   The statistic to modify, must not be {@code null}
     * @param type   The type of modification to apply, must not be {@code null}
     * @param amount The value to add to the modifier
     * @throws IllegalArgumentException If the statistic's ordinal exceeds 63
     */
    public void addStat(@NotNull Statistic stat, @NotNull StatValueType type, double amount) {
        final int id = stat.ordinal();
        validateStatId(id);

        switch (type) {
            case BASE -> base[id] += amount;
            case ADDITIVE -> additive[id] += amount;
            case MULTIPLICATIVE -> multiplicative[id] *= (1 + amount);
            case BONUS -> bonus[id] += amount;
        }
        markDirty(id);
    }

    /**
     * Combines another StatProfile into this one using additive stacking rules.
     *
     * @param other The profile to combine with this one, must not be {@code null}
     * @throws IllegalArgumentException If the other profile is {@code null}
     */
    public void combineWith(@NotNull StatProfile other) {
        if (other == null) {
            throw new IllegalArgumentException("Other profile cannot be null");
        }

        for (int i = 0; i < STAT_COUNT; i++) {
            base[i] += other.base[i];
            additive[i] += other.additive[i];
            multiplicative[i] *= other.multiplicative[i];
            bonus[i] += other.bonus[i];
            markDirty(i);
        }
    }

    /**
     * Creates a deep copy of this StatProfile.
     *
     * @return A new StatProfile instance with identical values and dirty flags
     */
    @NotNull
    public StatProfile copy() {
        StatProfile copy = new StatProfile();
        System.arraycopy(base, 0, copy.base, 0, STAT_COUNT);
        System.arraycopy(additive, 0, copy.additive, 0, STAT_COUNT);
        System.arraycopy(multiplicative, 0, copy.multiplicative, 0, STAT_COUNT);
        System.arraycopy(bonus, 0, copy.bonus, 0, STAT_COUNT);
        System.arraycopy(cached, 0, copy.cached, 0, STAT_COUNT);
        copy.dirtyFlags = this.dirtyFlags;
        return copy;
    }

    /**
     * Recalculates the final value for a specific statistic ID.
     *
     * @param id The ordinal ID of the statistic to recalculate
     */
    private void recalculate(int id) {
        final Statistic stat = Statistic.values()[id];
        final double calculated = (base[id] * (1 + additive[id]))
                * multiplicative[id]
                + bonus[id];

        cached[id] = stat.isCapped()
                ? Math.min(calculated, stat.getCapValue())
                : calculated;

        clearDirty(id);
    }

    /* Bitwise Operations */

    /**
     * Marks a statistic as needing recalculation.
     *
     * @param statId The ordinal ID of the statistic
     */
    private void markDirty(int statId) {
        dirtyFlags |= (1L << statId);
    }

    /**
     * Clears the dirty flag for a statistic.
     *
     * @param statId The ordinal ID of the statistic
     */
    private void clearDirty(int statId) {
        dirtyFlags &= ~(1L << statId);
    }

    /**
     * Checks if a statistic needs recalculation.
     *
     * @param statId The ordinal ID of the statistic
     * @return {@code true} if the statistic needs recalculation
     */
    private boolean isDirty(int statId) {
        return (dirtyFlags & (1L << statId)) != 0;
    }

    /**
     * Validates that a statistic ID is within supported range.
     *
     * @param statId The ordinal ID to validate
     * @throws IllegalArgumentException If ID exceeds supported maximum
     */
    private void validateStatId(int statId) {
        if (statId >= MAX_SUPPORTED_STATS) {
            throw new IllegalArgumentException(
                    "Statistic ID " + statId + " exceeds maximum supported value of "
                            + (MAX_SUPPORTED_STATS - 1)
            );
        }
    }
}