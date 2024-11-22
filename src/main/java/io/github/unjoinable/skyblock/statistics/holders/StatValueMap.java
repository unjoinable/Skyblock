package io.github.unjoinable.skyblock.statistics.holders;

import io.github.unjoinable.skyblock.statistics.Statistic;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

/**
 * A map-like class that stores Double values for specific Statistics.
 * This class is designed to be more efficient than a traditional Map<Statistic, Double>
 * by using an array to store the Double values, where the index of the array
 * corresponds to the ordinal value of the Statistic enum.
 */
public class StatValueMap {

    /**
     * The array of Double values, where the index of the array corresponds to the
     * ordinal value of the Statistic enum.
     */
    private final double[] statValues;

    /**
     * Constructs a new StatValueMap instance.
     */
    public StatValueMap() {
        statValues = new double[Statistic.values().length];
    }

    /**
     * Puts a Double value into the StatValueMap for a specific Statistic.
     *
     * @param statistic the Statistic for which to put the Double value
     * @param value the Double value to put into the StatValueMap
     */
    public void put(@NotNull Statistic statistic, double value) {
        statValues[statistic.ordinal()] = value;
    }

    /**
     * Gets the Double value for a specific Statistic from the StatValueMap.
     *
     * @param statistic the Statistic for which to get the Double value
     * @return the Double value for the specified Statistic, or 0.0 if no value exists
     */
    public double get(@NotNull Statistic statistic) {
        double value = statValues[statistic.ordinal()];
        return value == 0.0d ? 0.0 : value;
    }

    /**
     * Gets the Double value for a specific Statistic from the StatValueMap.
     *
     * @param ordinal the ordinal of that Statistic in the enum
     * @return the Double value for the specified Statistic, or 0.0 if no value exists
     */
    public double get(int ordinal) {
        double value = statValues[ordinal];
        return value == 0.0d ? 0.0 : value;
    }

    /**
     * Checks if a Double value exists for a specific Statistic in the StatValueMap.
     *
     * @param statistic the Statistic for which to check if a Double value exists
     * @return true if a Double value exists for the specified Statistic, false otherwise
     */
    public boolean has(@NotNull Statistic statistic) {
        return statValues[statistic.ordinal()] != 0.0;
    }

    /**
     * Clears all values in the StatValueMap.
     */
    public void clear() {
        Arrays.fill(statValues, 0.0);
    }
}
