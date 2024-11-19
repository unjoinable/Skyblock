package io.github.unjoinable.skyblock.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jetbrains.annotations.NotNull;

import java.text.NumberFormat;
import java.util.Locale;

public class StringUtils {

    /**
     * Converts a string into a MiniMessage Component.
     * The input string is deserialized using MiniMessage's deserialize method,
     * "<!i>" added to disable italics.
     * @param string The input string to be converted.
     * @return The resulting MiniMessage Component.
     */
    public static Component toComponent(String string) {
        return MiniMessage.miniMessage().deserialize("<!i>" + string);
    }

    /**
     * Converts a formatted string into a MiniMessage Component.
     * This method first formats the message with the provided arguments,
     * then converts it to a Component.
     *
     * @param message The message to be formatted and converted.
     * @param args The arguments to be inserted into the message.
     * @return The resulting Component.
     */
    public static Component toComponent(String message, Object... args) {
        return toComponent(formatString(message, args));
    }

    /**
     * Formats a string by replacing placeholders with provided arguments.
     * Each occurrence of "{}" in the string is replaced with the string
     * representation of the corresponding argument.
     *
     * @param str The string to be formatted.
     * @param args The arguments to be inserted into the string.
     * @return The formatted string.
     */
    public static String formatString(String str, Object... args) {
        StringBuilder sb = new StringBuilder();
        int argIndex = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == '{' && i + 1 < str.length() && str.charAt(i + 1) == '}') {
                if (argIndex < args.length) {
                    sb.append(args[argIndex]); argIndex++;
                } i++;
        } else {
                sb.append(str.charAt(i));
            }
        }
        return sb.toString();
    }

    /**
     * Converts a byte value to a two-digit string representation.
     * If the value is less than 10, it will be padded with a leading zero.
     *
     * @param value The byte value to be converted to a two-digit string.
     *              Should be a single digit (0-9).
     * @return A string representation of the byte value, always two digits long.
     *         The returned string is guaranteed to be non-null.
     */
    public static @NotNull String toDoubleDigit(byte value) {
        if (value > 9) return String.valueOf(value);
        return String.format("%02d", value);
    }

     /**
     * Formats a given day number with its appropriate ordinal suffix (st, nd, rd, or th).
     *
     * @param day The day number to be formatted. Should be a positive byte.
     * @return A string representation of the day number with its ordinal suffix.
     *         For example, 1 becomes "1st", 2 becomes "2nd", 3 becomes "3rd",
     *         4 becomes "4th", 11 becomes "11th", etc.
     */
    public static String formatDayWithSuffix(byte day) {
        if (day >= 11 && day <= 13) {
            return day + "th"; }
        return switch (day % 10) {
            case 1 -> day + "st";
            case 2 -> day + "nd";
            case 3 -> day + "rd";
            default -> day + "th";
        };
    }

    public static String makeRepresentable(double number) {
        // Remove decimals by converting to long
         long roundedNumber = Math.round(number);
         NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
         return numberFormat.format(roundedNumber);
    }
}
