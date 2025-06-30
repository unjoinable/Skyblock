package net.unjoinable.skyblock.utils;

public class NumberUtils {
    
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
}
