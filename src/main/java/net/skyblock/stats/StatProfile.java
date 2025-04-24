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
     */
    public void combineWith(StatProfile other) {
        for (int i = 0; i < STAT_COUNT; i++) {
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
        for (int i = 0; i < STAT_COUNT; i++) {
            isDirty.set(i, true);
        }
    }

    /**
     * Creates an exact independent copy of this profile.
     */
    public StatProfile copy() {
        StatProfile copy = new StatProfile(false);
        for (int i = 0; i < STAT_COUNT; i++) {
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
            float multVal = multiplicative.getFloat(id);
            float bonusVal = bonus.getFloat(id);
            float finalVal = get(stat);
            sb.append(String.format("  %s -> base=%.2f, additive=%.2f, multiplicative=%.2f, bonus=%.2f, final=%.2f%n",
                    stat.name(), baseVal, addVal, multVal, bonusVal, finalVal));
        }
        sb.append("}");
        return sb.toString();
    }
}