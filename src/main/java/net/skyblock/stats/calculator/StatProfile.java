package net.skyblock.stats.calculator;

import net.skyblock.stats.definition.Statistic;
import net.skyblock.stats.definition.StatValueType;
import org.jetbrains.annotations.NotNull;

/**
 * Ultra-optimized statistic profile implementation with enhanced performance characteristics.
 * <p>
 * This version includes significant optimizations over the original implementation:
 * <ul>
 *   <li>Precomputed statistic metadata caching</li>
 *   <li>Efficient bitwise dirty flag propagation</li>
 *   <li>Memory layout optimization for CPU cache efficiency</li>
 *   <li>Bulk array operations for fast copying</li>
 *   <li>Reduced method calls in hot paths</li>
 * </ul>
 *
 * <p>Performance Characteristics:</p>
 * <ul>
 *   <li>O(1) stat lookup for clean values</li>
 *   <li>Zero allocations during stat updates</li>
 *   <li>Bitwise operations with 64-bit dirty flag long</li>
 *   <li>Array-based storage with data locality</li>
 * </ul>
 */
public class StatProfile {
    private static final Statistic[] STATS = Statistic.values();
    private static final int STAT_COUNT = STATS.length;
    private static final int MAX_SUPPORTED_STATS = 64; // Bitwise operation fails if we go beyond this, BitSet for future

    // Storage arrays with direct index access
    private final double[] base = new double[STAT_COUNT];
    private final double[] additive = new double[STAT_COUNT];
    private final double[] multiplicative = new double[STAT_COUNT];
    private final double[] bonus = new double[STAT_COUNT];
    private final double[] cached = new double[STAT_COUNT];

    // Precomputed statistic metadata
    private final boolean[] isCapped = new boolean[STAT_COUNT];
    private final double[] capValue = new double[STAT_COUNT];

    // Bitwise dirty flag tracking (1 bit per statistic)
    private long dirtyFlags = ~0L;

    /**
     * Constructs a new StatProfile with optimized initialization.
     * <p>
     * Initializes all modifiers to their default values:
     * <ul>
     *   <li>Base values from Statistic definitions</li>
     *   <li>Multiplicative modifiers to 1.0 (identity value)</li>
     *   <li>Precomputed cap configurations</li>
     * </ul>
     */
    public StatProfile() {
        for (Statistic stat : STATS) {
            int id = stat.ordinal();
            base[id] = stat.getBaseValue();
            multiplicative[id] = 1.0;  // Critical multiplicative identity
            isCapped[id] = stat.isCapped();
            capValue[id] = stat.getCapValue();
        }
    }

    /**
     * Retrieves the current value of a statistic with automatic recalculation.
     *
     * @param stat The statistic to retrieve (non-null)
     * @return The calculated value respecting statistic caps
     * @throws IllegalArgumentException If statistic ID exceeds supported maximum
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
     * Modifies a statistic using the specified operation type.
     *
     * @param stat   Target statistic (non-null)
     * @param type   Modification type (non-null)
     * @param amount Value to apply (use negative values for subtraction)
     * @throws IllegalArgumentException If statistic ID is invalid
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
     * Merges another profile into this one using bulk operations.
     *
     * @param other Profile to combine (non-null)
     * @implNote Efficiently combines dirty flags using bitwise OR
     */
    public void combineWith(@NotNull StatProfile other) {
        for (int i = 0; i < STAT_COUNT; i++) {
            base[i] += other.base[i];
            additive[i] += other.additive[i];
            multiplicative[i] *= other.multiplicative[i];
            bonus[i] += other.bonus[i];
        }
        dirtyFlags |= other.dirtyFlags;  // Propagate dirty states
    }

    /**
     * Creates a deep copy of the profile using optimized array operations.
     *
     * @return New independent profile with identical state
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
     * Sets all statistic modifiers to their base values from the Statistic enum.
     * <p>
     * This method:
     * <ul>
     *   <li>sets base values to original Statistic base values</li>
     *   <li>Clears additive, multiplicative, and bonus modifiers</li>
     *   <li>Marks all statistics as dirty for recalculation</li>
     * </ul>
     */
    public @NotNull StatProfile createDefaultProfile() {
        for (int i = 0; i < STAT_COUNT; i++) {
            base[i] = STATS[i].getBaseValue();
            additive[i] = 0.0;
            multiplicative[i] = 1.0;
            bonus[i] = 0.0;
        }

        // Mark all flags dirty to force recalculation
        dirtyFlags = ~0L;
        return this;
    }

    /**
     * Recalculates a statistic value using precomputed metadata.
     *
     * @param id Statistic ID to recalculate
     * @implNote Uses precomputed cap values to avoid virtual calls
     */
    private void recalculate(int id) {
        final double additiveFactor = 1.0 + additive[id];
        final double calculated = (base[id] * additiveFactor) * multiplicative[id] + bonus[id];

        cached[id] = isCapped[id] ? Math.min(calculated, capValue[id]) : calculated;
        clearDirty(id);
    }

    /* Bitwise Dirty Flag Management */

    /**
     * Marks a statistic as needing recalculation.
     *
     * @param statId Target statistic ID
     */
    private void markDirty(int statId) {
        dirtyFlags |= (1L << statId);
    }

    /**
     * Clears the dirty state for a statistic.
     *
     * @param statId Target statistic ID
     */
    private void clearDirty(int statId) {
        dirtyFlags &= ~(1L << statId);
    }

    /**
     * Checks if a statistic needs recalculation.
     *
     * @param statId Target statistic ID
     * @return True if the statistic is marked dirty
     */
    private boolean isDirty(int statId) {
        return (dirtyFlags & (1L << statId)) != 0;
    }

    /**
     * Validates statistic ID against supported range.
     *
     * @param statId ID to validate
     * @throws IllegalArgumentException For IDs >= 64
     */
    private void validateStatId(int statId) {
        if (statId >= MAX_SUPPORTED_STATS) {
            throw new IllegalArgumentException(
                    "Statistic ID " + statId + " exceeds maximum supported value of " + (MAX_SUPPORTED_STATS - 1)
            );
        }
    }
}