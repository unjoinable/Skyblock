package net.skyblock.stats;

import it.unimi.dsi.fastutil.booleans.BooleanArrayList;
import it.unimi.dsi.fastutil.floats.FloatArrayList;

/**
 * Ultimate optimized stat system
 */
public final class StatProfile {
    private static final int STAT_COUNT = Statistic.values().length;
    private static final float[] STAT_CAPS = new float[STAT_COUNT];

    static {
        for (Statistic stat : Statistic.values()) {
            if (stat.isCapped()) {
                STAT_CAPS[stat.ordinal()] = stat.getCapValue();
            }
        }
    }

    private final FloatArrayList base;
    private final FloatArrayList additive;
    private final FloatArrayList multiplicative;
    private final FloatArrayList bonus;
    private final FloatArrayList cachedValues;
    private final BooleanArrayList isDirty;

    /**
     * Creates a new StatProfile instance with the option to initialize base stats.
     * <p>
     * This constructor initializes the various stat modifier arrays (base, additive, multiplicative, bonus)
     * and sets up the dirty flags for all stats. If the {@code initBaseStats} parameter is {@code true},
     * the base values for each stat will be set according to the values defined in the {@link Statistic} enum.
     * Otherwise, the base values are initialized to 0, and the profile is empty.
     *
     * @param initBaseStats whether or not to initialize the base stats for each statistic from the {@link Statistic} enum.
     *                      If {@code true}, base stats are set to their predefined values; otherwise, they are set to 0.
     */
    public StatProfile(boolean initBaseStats) {
        this.base = new FloatArrayList(STAT_COUNT);
        this.additive = new FloatArrayList(STAT_COUNT);
        this.multiplicative = new FloatArrayList(STAT_COUNT);
        this.bonus = new FloatArrayList(STAT_COUNT);
        this.cachedValues = new FloatArrayList(STAT_COUNT);
        this.isDirty = new BooleanArrayList(STAT_COUNT);

        // Initialize all arrays
        for (int i = 0; i < STAT_COUNT; i++) {
            base.add(0f);
            additive.add(0f);
            multiplicative.add(1f); // Multiplicative identity
            bonus.add(0f);
            cachedValues.add(0f);
            isDirty.add(true);
        }

        if (initBaseStats) {
            for (Statistic stat : Statistic.values()) {
                base.set(stat.ordinal(), stat.getBaseValue());
            }
        }
    }

    /**
     * Creates a new StatProfile instance without initializing base stats.
     * <p>
     * This constructor initializes the stat modifier arrays (base, additive, multiplicative, bonus)
     * and dirty flags for each stat. The base stats are not initialized, meaning all base values are set to 0.
     *
     * This constructor can be useful when you don't want to apply the predefined base values immediately.
     */
    public StatProfile() {
        this(false);
    }

    /**
     * Gets the current value of a stat with all modifiers applied.
     * Formula: (base * (1 + additive) * multiplicative) + bonus
     */
    public float get(Statistic stat) {
        int id = stat.ordinal();
        if (!isDirty.getBoolean(id)) {
            return cachedValues.getFloat(id);
        }

        float value = calculateStatValue(id);
        cachedValues.set(id, value);
        isDirty.set(id, false);
        return value;
    }

    /**
     * Adds a stat modifier of specified type.
     * @param type Stat modifier type (BASE/ADDITIVE/MULTIPLICATIVE/BONUS)
     * @param amount Value to add (positive or negative)
     */
    public void addStat(Statistic stat, StatValueType type, float amount) {
        int id = stat.ordinal();
        switch (type) {
            case BASE -> base.set(id, base.getFloat(id) + amount);
            case ADDITIVE -> additive.set(id, additive.getFloat(id) + amount);
            case MULTIPLICATIVE -> multiplicative.set(id, multiplicative.getFloat(id) * (1 + amount));
            case BONUS -> bonus.set(id, bonus.getFloat(id) + amount);
        }
        isDirty.set(id, true);
    }

    /**
     * Removes a stat modifier of specified type.
     * @param type Stat modifier type (BASE/ADDITIVE/MULTIPLICATIVE/BONUS)
     * @param amount Value to remove (positive or negative)
     */
    public void removeStat(Statistic stat, StatValueType type, float amount) {
        addStat(stat, type, -amount);
    }

    /**
     * Combines another StatProfile into this one (additive stacking).
     * Multiplicative values are multiplied together.
     * Uses Statistic.values() to ensure all stats are processed properly.
     */
    public void combineWith(StatProfile other) {
        for (Statistic stat : Statistic.values()) {
            int i = stat.ordinal();
            base.set(i, base.getFloat(i) + other.base.getFloat(i));
            additive.set(i, additive.getFloat(i) + other.additive.getFloat(i));
            multiplicative.set(i, multiplicative.getFloat(i) * other.multiplicative.getFloat(i));
            bonus.set(i, bonus.getFloat(i) + other.bonus.getFloat(i));
            isDirty.set(i, true);
        }
    }

    /**
     * Forces recalculation of all stats on next access.
     */
    public void invalidateAll() {
        for (Statistic stat : Statistic.values()) {
            isDirty.set(stat.ordinal(), true);
        }
    }

    /**
     * Creates an exact independent copy of this profile.
     * Uses Statistic.values() to ensure all stats are copied properly.
     */
    public StatProfile copy() {
        StatProfile copy = new StatProfile(false);
        for (Statistic stat : Statistic.values()) {
            int i = stat.ordinal();
            copy.base.set(i, base.getFloat(i));
            copy.additive.set(i, additive.getFloat(i));
            copy.multiplicative.set(i, multiplicative.getFloat(i));
            copy.bonus.set(i, bonus.getFloat(i));
            copy.isDirty.set(i, true); // New copies should recalculate
        }
        return copy;
    }

    private float calculateStatValue(int statId) {
        float value = (base.getFloat(statId) * (1 + additive.getFloat(statId)))
                * multiplicative.getFloat(statId)
                + bonus.getFloat(statId);

        Statistic stat = Statistic.values()[statId];
        return stat.isCapped() ? Math.min(value, STAT_CAPS[statId]) : value;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("StatProfile {\n");
        for (Statistic stat : Statistic.values()) {
            int id = stat.ordinal();
            float baseVal = base.getFloat(id);
            float addVal = additive.getFloat(id);
            float multiVal = multiplicative.getFloat(id);
            float bonusVal = bonus.getFloat(id);
            float finalVal = get(stat);
            sb.append(String.format("  %s -> base=%.2f, additive=%.2f, multiplicative=%.2f, bonus=%.2f, final=%.2f%n",
                    stat.name(), baseVal, addVal, multiVal, bonusVal, finalVal));
        }
        sb.append("}");
        return sb.toString();
    }
}