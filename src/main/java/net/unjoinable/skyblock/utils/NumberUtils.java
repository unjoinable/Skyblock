package net.unjoinable.skyblock.utils;

public class NumberUtils {
    private static final String[] SUFFIXES = {"", "k", "M", "B", "T", "Q"};
    private static final double[] THRESHOLDS = {1, 1_000, 1_000_000, 1_000_000_000, 1_000_000_000_000L, 1_000_000_000_000_000L};
    
    private NumberUtils() {
        throw new AssertionError("Utility class cannot be instantiated");
    }
    
    /**
     * Formats a number as dated string.
     * 
     * @param number the number to format
     * @return the dated string (e.g., "1st", "2nd", "3rd", "4th")
     */
    public static String formatDate(int number) {
        if (number % 100 >= 11 && number % 100 <= 13) {
            return number + "th";
        }
        return switch (number % 10) {
            case 1 -> number + "st";
            case 2 -> number + "nd";
            case 3 -> number + "rd";
            default -> number + "th";
        };
    }

    /**
     * Formats a double into an abbreviated string representation.
     * Examples:
     * - 500 -> "500"
     * - 1000 -> "1k"
     * - 1200 -> "1.2k"
     * - 4100000 -> "4.1M"
     * - 1200000000 -> "1.2B"
     *
     * @param value the number to format
     * @return the formatted string
     */
    public static String format(double value) {
        if (value < 0) {
            return "-" + format(-value);
        }

        if (value < 1000) {
            if (value == (long) value) {
                return String.valueOf((long) value);
            }
            return String.valueOf(value);
        }

        int suffixIndex = 0;
        for (int i = THRESHOLDS.length - 1; i >= 0; i--) {
            if (value >= THRESHOLDS[i]) {
                suffixIndex = i;
                break;
            }
        }

        double scaledValue = value / THRESHOLDS[suffixIndex];
        return String.format("%.1f%s", scaledValue, SUFFIXES[suffixIndex]);
    }

    /**
     * Alternative format method that removes unnecessary decimal places.
     * This version will show "1k" instead of "1.0k" when the decimal is zero.
     */
    public static String formatClean(double value) {
        String formatted = format(value);

        // Remove .0 from the end if present
        if (formatted.matches(".*\\.0[a-zA-Z]*$")) {
            formatted = formatted.replaceFirst("\\.0([a-zA-Z]*)$", "$1");
        }

        return formatted;
    }
}
