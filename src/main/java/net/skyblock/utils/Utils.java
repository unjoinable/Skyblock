package net.skyblock.utils;

import net.skyblock.stats.Statistic;

public class Utils {

    private Utils() {} //Utils class must not be initialized

    /**
     * Formats a numeric value with proper precision and percentage symbol when needed.
     *
     * @param value The numeric value to format
     * @param stat The statistic to determine if percentage display is needed
     * @return Formatted string representation of the value
     */
    public static String formatStatValue(double value, Statistic stat) {
        StringBuilder result = new StringBuilder();
        result.append('+');

        if (value == (int) value) {
            result.append((int) value);
        } else {
            result.append(String.format("%.1f", value));
        }

        if (stat.getPercentage()) {
            result.append('%');
        }

        return result.toString();
    }
}