package net.unjoinable.skyblock.time;

import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.timer.TaskSchedule;
import org.jspecify.annotations.Nullable;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.Component.textOfChildren;
import static net.kyori.adventure.text.format.NamedTextColor.*;

/**
 * Represents the standard time system for Skyblock.
 * This class manages and updates the in-game time, including year, month, day, hours, and minutes.
 */
public class SkyblockStandardTime {
    private static final char DAY = '☀';
    private static final char NIGHT = '☽';
    private static final long SKYBLOCK_START = 1560275700000L;
    private static final int SKYBLOCK_TIME_MULTIPLIER = 72;

    private Season season;
    private int year;
    private byte month;
    private byte day;
    private byte hours;
    private byte minutes;

    private @Nullable String cachedToString;
    private @Nullable Component cachedTimeComponent;

    public SkyblockStandardTime() {
        update();
        // Schedule update every 8.3 seconds (8300ms) to match original timing
        MinecraftServer.getSchedulerManager().scheduleTask(this::update,
                TaskSchedule.immediate(), TaskSchedule.millis(8300));
    }

    /**
     * Updates the current Skyblock time based on the real-world time elapsed since the Skyblock start.
     * This method calculates and sets the current year, month, day, hours, minutes, and season.
     */
    private void update() {
        long currentTime = System.currentTimeMillis();
        long elapsedRealMillis = currentTime - SKYBLOCK_START;

        // Convert to Skyblock time: 1 real second = 72 Skyblock seconds
        long skyBlockMillis = elapsedRealMillis * SKYBLOCK_TIME_MULTIPLIER;

        // Calculate time units
        long skyBlockSeconds = skyBlockMillis / 1000;
        long skyBlockMinutes = skyBlockSeconds / 60;
        long skyBlockHours = skyBlockMinutes / 60;
        long skyBlockDays = skyBlockHours / 24;
        int skyBlockMonths = (int) (skyBlockDays / 31);

        // Set time values
        this.year = (skyBlockMonths / 12) + 1; // Start from year 1
        this.month = (byte) (skyBlockMonths % 12);
        this.day = (byte) ((skyBlockDays % 31) + 1); // Days 1-31
        this.hours = (byte) (skyBlockHours % 24);
        this.minutes = (byte) (((skyBlockMinutes % 60) / 10) * 10); // Round to nearest 10

        this.season = Season.fromOrdinal(this.month);

        // Invalidate cached strings and components
        this.cachedToString = null;
        this.cachedTimeComponent = null;
    }

    /**
     * Gets the current season in Skyblock.
     *
     * @return The current Season.
     */
    public Season getSeason() {
        return season;
    }

    /**
     * Gets the current year in Skyblock.
     *
     * @return The current year as an integer.
     */
    public int getYear() {
        return year;
    }

    /**
     * Gets the current month in Skyblock.
     *
     * @return The current month as a byte (0-11).
     */
    public byte getMonth() {
        return month;
    }

    /**
     * Gets the current day of the month in Skyblock.
     *
     * @return The current day as a byte (1-31).
     */
    public byte getDay() {
        return day;
    }

    /**
     * Gets the current hour in Skyblock.
     *
     * @return The current hour as a byte (0-23).
     */
    public byte getHours() {
        return hours;
    }

    /**
     * Gets the current minute in Skyblock.
     *
     * @return The current minute as a byte (0-59), rounded to the nearest 10.
     */
    public byte getMinutes() {
        return minutes;
    }

    /**
     * Gets the current time in Skyblock as a formatted string.
     *
     * @return A string representation of the current time in "HH:MM" format, with AM/PM indicator.
     */
    public String getTime() {
        int displayHour = hours % 12;
        if (displayHour == 0) displayHour = 12; // 0 hours = 12 AM/PM

        String period = (hours >= 12) ? "PM" : "AM";
        return displayHour + ":" + String.format("%02d", minutes) + " " + period;
    }

    /**
     * Gets the current time in Skyblock as a formatted Adventure Component.
     * Returns a colored component with the time and a day/night symbol.
     * Daytime (6 AM - 6:59 PM) shows a yellow sun symbol, nighttime shows an aqua moon symbol.
     * The component is cached to avoid recreating expensive Component objects on repeated calls.
     *
     * @return A Component with the current time in gray text and a colored day/night symbol.
     */
    public Component getTimeComponent() {
        if (cachedTimeComponent == null) {
            if (isDayTime()) {
                cachedTimeComponent = textOfChildren(text(" " + getTime(), GRAY), text(" " + DAY, YELLOW));
            } else {
                cachedTimeComponent = textOfChildren(text(" " + getTime(), GRAY), text(" " + NIGHT, AQUA));
            }
        }
        return cachedTimeComponent;
    }

    /**
     * Checks if it's currently daytime in Skyblock.
     *
     * @return true if it's daytime (6:00 AM to 6:59 PM), false otherwise.
     */
    public boolean isDayTime() {
        return hours >= 6 && hours < 19;
    }

    /**
     * Checks if it's currently nighttime in Skyblock.
     *
     * @return true if it's nighttime (7:00 PM to 5:59 AM), false otherwise.
     */
    public boolean isNightTime() {
        return hours < 6 || hours >= 19;
    }

    @Override
    public String toString() {
        if (cachedToString == null) {
            cachedToString = getTime() + " " + getDay() + " of " + getSeason() + ", year " + getYear();
        }
        return cachedToString;
    }
}