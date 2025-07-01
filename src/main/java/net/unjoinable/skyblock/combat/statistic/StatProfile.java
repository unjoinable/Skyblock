package net.unjoinable.skyblock.combat.statistic;

import java.util.Arrays;
import java.util.BitSet;
import java.util.Map;

/**
 * Ultra-optimized statistic profile implementation with enhanced performance characteristics.
 * <p>
 * This version includes significant optimizations over the original implementation:
 * <ul>
 *   <li>Precomputed statistic metadata caching</li>
 *   <li>Efficient BitSet dirty flag propagation</li>
 *   <li>Memory layout optimization for CPU cache efficiency</li>
 *   <li>Bulk array operations for fast copying</li>
 *   <li>Reduced method calls in hot paths</li>
 *   <li>BitSet operations for boolean state tracking</li>
 * </ul>
 *
 * <p>Performance Characteristics:</p>
 * <ul>
 *   <li>O(1) stat lookup for clean values</li>
 *   <li>Zero allocations during stat updates</li>
 *   <li>BitSet operations for unlimited statistic support</li>
 *   <li>Array-based storage with data locality</li>
 *   <li>Bit-packed boolean state flags</li>
 * </ul>
 */
public class StatProfile {
    private static final Statistic[] STATS = Statistic.values();
    private static final int STAT_COUNT = STATS.length;

    // Storage arrays with direct index access
    private final double[] base = new double[STAT_COUNT];
    private final double[] additive = new double[STAT_COUNT];
    private final double[] multiplicative = new double[STAT_COUNT];
    private final double[] bonus = new double[STAT_COUNT];
    private final double[] cached = new double[STAT_COUNT];

    // BitSet flags for boolean states (no more 64 stat limit)
    private final BitSet cappedFlags = new BitSet(STAT_COUNT);
    private final double[] capValue = new double[STAT_COUNT];

    // BitSet dirty flag tracking
    private final BitSet dirtyFlags = new BitSet(STAT_COUNT);

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
            multiplicative[id] = 1.0;

            // Use BitSet operations for boolean state
            if (stat.isCapped()) {
                setCapped(id, true);
                capValue[id] = stat.capValue();
            }
        }

        // Mark all stats as dirty initially
        dirtyFlags.set(0, STAT_COUNT);
    }

    /**
     * Retrieves the current value of a statistic with automatic recalculation.
     *
     * @param stat The statistic to retrieve (non-null)
     * @return The calculated value respecting statistic caps
     */
    public double get(Statistic stat) {
        final int id = stat.ordinal();

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
     */
    public void addStat(Statistic stat, StatValueType type, double amount) {
        final int id = stat.ordinal();

        switch (type) {
            case BASE -> base[id] += amount;
            case ADDITIVE -> additive[id] += amount;
            case MULTIPLICATIVE -> multiplicative[id] *= (1 + amount);
            case BONUS -> bonus[id] += amount;
        }
        markDirty(id);
    }

    /**
     * Sets the exact value for a specific stat modifier.
     *
     * @param stat   Target statistic (non-null)
     * @param type   Modification type (non-null)
     * @param value  Exact value to set
     */
    public void setStat(Statistic stat, StatValueType type, double value) {
        final int id = stat.ordinal();

        switch (type) {
            case BASE -> base[id] = value;
            case ADDITIVE -> additive[id] = value;
            case MULTIPLICATIVE -> multiplicative[id] = value;
            case BONUS -> bonus[id] = value;
        }
        markDirty(id);
    }

    /**
     * Merges another profile into this one using bulk operations.
     *
     * @param other Profile to combine (non-null)
     * @implNote Efficiently combines dirty flags using BitSet OR
     */
    public void combineWith(StatProfile other) {
        for (int i = 0; i < STAT_COUNT; i++) {
            base[i] += other.base[i];
            additive[i] += other.additive[i];
            multiplicative[i] *= other.multiplicative[i];
            bonus[i] += other.bonus[i];
        }

        // Propagate dirty states using BitSet.or()
        dirtyFlags.or(other.dirtyFlags);
    }

    /**
     * Creates a deep copy of the profile using optimized array operations.
     *
     * @return New independent profile with identical state
     */
    public StatProfile copy() {
        StatProfile copy = new StatProfile();
        System.arraycopy(base, 0, copy.base, 0, STAT_COUNT);
        System.arraycopy(additive, 0, copy.additive, 0, STAT_COUNT);
        System.arraycopy(multiplicative, 0, copy.multiplicative, 0, STAT_COUNT);
        System.arraycopy(bonus, 0, copy.bonus, 0, STAT_COUNT);
        System.arraycopy(cached, 0, copy.cached, 0, STAT_COUNT);
        System.arraycopy(capValue, 0, copy.capValue, 0, STAT_COUNT);

        // Copy BitSet flags
        copy.dirtyFlags.clear();
        copy.dirtyFlags.or(this.dirtyFlags);

        copy.cappedFlags.clear();
        copy.cappedFlags.or(this.cappedFlags);

        return copy;
    }

    /**
     * Resets this profile to default values from the Statistic enum.
     * <p>
     * This method:
     * <ul>
     *   <li>sets base values to original Statistic base values</li>
     *   <li>Clears additive, multiplicative, and bonus modifiers</li>
     *   <li>Marks all statistics as dirty for recalculation</li>
     * </ul>
     *
     * @return This profile instance for method chaining
     */
    public StatProfile setToDefaults() {
        for (int i = 0; i < STAT_COUNT; i++) {
            base[i] = STATS[i].baseValue();
            additive[i] = 0.0;
            multiplicative[i] = 1.0;
            bonus[i] = 0.0;
        }

        dirtyFlags.set(0, STAT_COUNT);
        return this;
    }

    /**
     * Creates a new default profile with base values from the Statistic enum.
     *
     * @return A new StatProfile instance with default values
     */
    public static StatProfile createDefaultProfile() {
        return new StatProfile().setToDefaults();
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

        cached[id] = isCapped(id) ? Math.min(calculated, capValue[id]) : calculated;
        clearDirty(id);
    }

    /**
     * Bulk recalculates all dirty statistics in one pass.
     * Useful for performance critical sections where many stats may be dirty.
     */
    public void recalculateAll() {
        if (dirtyFlags.isEmpty()) {
            return;
        }

        for (int id = dirtyFlags.nextSetBit(0); id >= 0; id = dirtyFlags.nextSetBit(id + 1)) {
            recalculate(id);
        }
    }

    /* BitSet Dirty Flag Management */

    /**
     * Marks a statistic as needing recalculation.
     *
     * @param statId Target statistic ID
     */
    private void markDirty(int statId) {
        dirtyFlags.set(statId);
    }

    /**
     * Clears the dirty state for a statistic.
     *
     * @param statId Target statistic ID
     */
    private void clearDirty(int statId) {
        dirtyFlags.clear(statId);
    }

    /**
     * Checks if a statistic needs recalculation.
     *
     * @param statId Target statistic ID
     * @return True if the statistic is marked dirty
     */
    private boolean isDirty(int statId) {
        return dirtyFlags.get(statId);
    }

    /* BitSet Boolean Flag Management for Capped Status */

    /**
     * Sets the capped status for a statistic using BitSet operations.
     *
     * @param statId Target statistic ID
     * @param capped Whether the statistic is capped
     */
    private void setCapped(int statId, boolean capped) {
        cappedFlags.set(statId, capped);
    }

    /**
     * Checks if a statistic has a capped maximum value.
     *
     * @param statId Target statistic ID
     * @return True if the statistic has a capped maximum
     */
    private boolean isCapped(int statId) {
        return cappedFlags.get(statId);
    }

    /**
     * Loads statistic values from a map into this profile.
     *
     * @param statsMap Map containing statistic values to load
     * @param valueType Type of modifier to apply the values as
     * @return This profile instance for method chaining
     */
    public StatProfile loadFromMap(Map<Statistic, Double> statsMap, StatValueType valueType) {
        for (Map.Entry<Statistic, Double> entry : statsMap.entrySet()) {
            Statistic stat = entry.getKey();
            Double value = entry.getValue();
            if (stat == null || value == null) {
                continue;
            }
            setStat(stat, valueType, value);
        }
        return this;
    }

    /**
     * Resets this profile back to its initial constructor state.
     * <p>
     * This restores the profile to exactly the same state as when it was first created:
     * <ul>
     *   <li>All base values set to 0.0</li>
     *   <li>All additive values set to 0.0</li>
     *   <li>All multiplicative values set to 1.0</li>
     *   <li>All bonus values set to 0.0</li>
     *   <li>Cap configurations restored from Statistic definitions</li>
     *   <li>All stats marked as dirty</li>
     * </ul>
     *
     * @return This profile instance for method chaining
     */
    public StatProfile reset() {
        Arrays.fill(base, 0.0);
        Arrays.fill(additive, 0.0);
        Arrays.fill(multiplicative, 1.0);
        Arrays.fill(bonus, 0.0);
        Arrays.fill(cached, 0.0);
        Arrays.fill(capValue, 0.0);

        cappedFlags.clear();
        dirtyFlags.clear();

        for (Statistic stat : STATS) {
            int id = stat.ordinal();
            if (stat.isCapped()) {
                setCapped(id, true);
                capValue[id] = stat.capValue();
            }
        }

        dirtyFlags.set(0, STAT_COUNT);
        return this;
    }

    /**
     * Creates a new StatProfile from a map of statistic values.
     *
     * @param statsMap Map containing statistic values to load
     * @param valueType Type of modifier to apply the values as
     * @return A new StatProfile with the specified values
     */
    public static StatProfile fromMap(Map<Statistic, Double> statsMap, StatValueType valueType) {
        StatProfile profile = new StatProfile();
        return profile.loadFromMap(statsMap, valueType);
    }
}