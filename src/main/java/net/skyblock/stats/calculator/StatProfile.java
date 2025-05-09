package net.skyblock.stats.calculator;

import it.unimi.dsi.fastutil.booleans.BooleanArrayList;
import it.unimi.dsi.fastutil.doubles.DoubleArrayList;
import net.skyblock.stats.definition.StatValueType;
import net.skyblock.stats.definition.Statistic;

import java.util.Map;

/**
 * Ultimate optimized stat system
 */
public final class StatProfile {
    private static final int STAT_COUNT = Statistic.values().length;
    private static final double[] STAT_CAPS = new double[STAT_COUNT];

    static {
        for (Statistic stat : Statistic.values()) {
            if (stat.isCapped()) {
                STAT_CAPS[stat.ordinal()] = stat.getCapValue();
            }
        }
    }

    private final DoubleArrayList base;
    private final DoubleArrayList additive;
    private final DoubleArrayList multiplicative;
    private final DoubleArrayList bonus;
    private final DoubleArrayList cachedValues;
    private final BooleanArrayList isDirty;

    /**
     * Creates a new StatProfile instance with empty stats (all base values set to 0).
     */
    public StatProfile() {
        this.base = new DoubleArrayList(STAT_COUNT);
        this.additive = new DoubleArrayList(STAT_COUNT);
        this.multiplicative = new DoubleArrayList(STAT_COUNT);
        this.bonus = new DoubleArrayList(STAT_COUNT);
        this.cachedValues = new DoubleArrayList(STAT_COUNT);
        this.isDirty = new BooleanArrayList(STAT_COUNT);

        // Initialize all arrays
        for (int i = 0; i < STAT_COUNT; i++) {
            base.add(0.0);
            additive.add(0.0);
            multiplicative.add(1.0); // Multiplicative identity
            bonus.add(0.0);
            cachedValues.add(0.0);
            isDirty.add(true);
        }
    }

    /**
     * Creates a new StatProfile instance with base stats initialized from the Statistic enum.
     *
     * @return a new StatProfile with default base stats
     */
    public static StatProfile fromBase() {
        StatProfile profile = new StatProfile();
        for (Statistic stat : Statistic.values()) {
            profile.base.set(stat.ordinal(), stat.getBaseValue());
        }
        profile.invalidateAll();
        return profile;
    }

    /**
     * Creates a new StatProfile from a Map of Statistics and their values for a specific StatValueType.
     *
     * @param statMap Map containing the statistics and their values
     * @param type The StatValueType to apply these values to
     * @return a new StatProfile with the given values
     */
    public static StatProfile fromMap(Map<Statistic, Double> statMap, StatValueType type) {
        StatProfile profile = new StatProfile();
        for (Map.Entry<Statistic, Double> entry : statMap.entrySet()) {
            Statistic stat = entry.getKey();
            Double value = entry.getValue();
            profile.addStat(stat, type, value);
        }
        return profile;
    }

    /**
     * Adds all values from a Map of Statistics to this profile with the specified StatValueType.
     *
     * @param statMap Map containing the statistics and their values
     * @param type The StatValueType to apply these values to
     */
    public void applyFromMap(Map<Statistic, Double> statMap, StatValueType type) {
        for (Map.Entry<Statistic, Double> entry : statMap.entrySet()) {
            Statistic stat = entry.getKey();
            Double value = entry.getValue();
            addStat(stat, type, value);
        }
    }

    /**
     * Gets the current value of a stat with all modifiers applied.
     * Formula: (base * (1 + additive) * multiplicative) + bonus
     */
    public double get(Statistic stat) {
        int id = stat.ordinal();
        if (!isDirty.getBoolean(id)) {
            return cachedValues.getDouble(id);
        }

        double value = calculateStatValue(id);
        cachedValues.set(id, value);
        isDirty.set(id, false);
        return value;
    }

    /**
     * Adds a stat modifier of specified type.
     * @param type Stat modifier type (BASE/ADDITIVE/MULTIPLICATIVE/BONUS)
     * @param amount Value to add (positive or negative)
     */
    public void addStat(Statistic stat, StatValueType type, double amount) {
        int id = stat.ordinal();
        switch (type) {
            case BASE -> base.set(id, base.getDouble(id) + amount);
            case ADDITIVE -> additive.set(id, additive.getDouble(id) + amount);
            case MULTIPLICATIVE -> multiplicative.set(id, multiplicative.getDouble(id) * (1 + amount));
            case BONUS -> bonus.set(id, bonus.getDouble(id) + amount);
        }
        isDirty.set(id, true);
    }

    /**
     * Removes a stat modifier of specified type.
     * @param type Stat modifier type (BASE/ADDITIVE/MULTIPLICATIVE/BONUS)
     * @param amount Value to remove (positive or negative)
     */
    public void removeStat(Statistic stat, StatValueType type, double amount) {
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
            base.set(i, base.getDouble(i) + other.base.getDouble(i));
            additive.set(i, additive.getDouble(i) + other.additive.getDouble(i));
            multiplicative.set(i, multiplicative.getDouble(i) * other.multiplicative.getDouble(i));
            bonus.set(i, bonus.getDouble(i) + other.bonus.getDouble(i));
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
        StatProfile copy = new StatProfile();
        for (Statistic stat : Statistic.values()) {
            int i = stat.ordinal();
            copy.base.set(i, base.getDouble(i));
            copy.additive.set(i, additive.getDouble(i));
            copy.multiplicative.set(i, multiplicative.getDouble(i));
            copy.bonus.set(i, bonus.getDouble(i));
            copy.isDirty.set(i, true); // New copies should recalculate
        }
        return copy;
    }

    private double calculateStatValue(int statId) {
        double value = (base.getDouble(statId) * (1 + additive.getDouble(statId)))
                * multiplicative.getDouble(statId)
                + bonus.getDouble(statId);

        Statistic stat = Statistic.values()[statId];
        return stat.isCapped() ? Math.min(value, STAT_CAPS[statId]) : value;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("StatProfile {\n");
        for (Statistic stat : Statistic.values()) {
            int id = stat.ordinal();
            double baseVal = base.getDouble(id);
            double addVal = additive.getDouble(id);
            double multiVal = multiplicative.getDouble(id);
            double bonusVal = bonus.getDouble(id);
            double finalVal = get(stat);
            sb.append(String.format("  %s -> base=%.2f, additive=%.2f, multiplicative=%.2f, bonus=%.2f, final=%.2f%n",
                    stat.name(), baseVal, addVal, multiVal, bonusVal, finalVal));
        }
        sb.append("}");
        return sb.toString();
    }
}