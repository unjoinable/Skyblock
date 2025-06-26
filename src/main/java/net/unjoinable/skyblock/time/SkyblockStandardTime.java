package net.unjoinable.skyblock.time;

import net.minestom.server.MinecraftServer;
import net.minestom.server.timer.TaskSchedule;
import org.jspecify.annotations.Nullable;

/**
 * Represents the standard time system for Skyblock.
 * This class manages and updates the in-game time, including year, month, day, hours, and minutes.
 */
public class SkyblockStandardTime {
    private static final long SKYBLOCK_START = 1560275700000L;
    private static final int SKYBLOCK_TIME_MULTIPLIER = 72;

    // Pre-calculated constants for better performance
    private static final long MILLIS_TO_GAME_MINUTES = SKYBLOCK_TIME_MULTIPLIER * 60_000L;
    private static final int MINUTES_IN_HOUR = 60;
    private static final int HOURS_IN_DAY = 24;
    private static final int DAYS_IN_MONTH = 31;
    private static final int MONTHS_IN_YEAR = 12;

    // Cache for formatted strings to avoid repeated allocations
    private static final String[] HOUR_CACHE = new String[12];
    private static final String[] MINUTE_CACHE = new String[6];

    static {
        // Pre-compute hour strings (1-12)
        for (int i = 0; i < 12; i++) {
            HOUR_CACHE[i] = String.valueOf(i == 0 ? 12 : i);
        }

        // Pre-compute minute strings (00, 10, 20, 30, 40, 50)
        for (int i = 0; i < 6; i++) {
            MINUTE_CACHE[i] = String.format("%02d", i * 10);
        }
    }

    private Season season;
    private int year;
    private byte month;
    private byte day;
    private byte hours;
    private byte minutes;

    private @Nullable String cachedToString;

    public SkyblockStandardTime() {
        update();
        MinecraftServer.getSchedulerManager().scheduleTask(this::update, TaskSchedule.immediate(), TaskSchedule.seconds(8));
    }

    /**
     * Updates the current Skyblock time based on the real-world time elapsed since the Skyblock start.
     * This method calculates and sets the current year, month, day, hours, minutes, and season.
     */
    private void update() {
        long currentTime = System.currentTimeMillis();

        long elapsedRealMillis = currentTime - SKYBLOCK_START;
        long elapsedGameMinutes = elapsedRealMillis / MILLIS_TO_GAME_MINUTES;

        // Use more efficient arithmetic
        long totalGameHours = elapsedGameMinutes / MINUTES_IN_HOUR;
        long totalGameDays = totalGameHours / HOURS_IN_DAY;
        int totalGameMonths = (int) (totalGameDays / DAYS_IN_MONTH);

        this.year = (totalGameMonths / MONTHS_IN_YEAR) + 1;
        this.month = (byte) (totalGameMonths % MONTHS_IN_YEAR);
        this.day = (byte) ((totalGameDays % DAYS_IN_MONTH) + 1);
        this.hours = (byte) (totalGameHours % HOURS_IN_DAY);
        this.minutes = (byte) ((elapsedGameMinutes % MINUTES_IN_HOUR) / 10 * 10);

        this.season = Season.fromOrdinal(this.month);

        // Invalidate cached string
        this.cachedToString = null;
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
     * @return A string representation of the current time in "HH:MM" format, with AM/PM indicator for afternoon times.
     */
    public String getTime() {
        int hourIndex = hours % 12;
        int minuteIndex = minutes / 10;
        String period = (hours >= 12) ? "PM" : "AM";

        // Use cached strings to avoid allocations
        return HOUR_CACHE[hourIndex] + ":" + MINUTE_CACHE[minuteIndex] + " " + period;
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
        return hours < 6 || hours >= 19; // More direct than !isDayTime()
    }

    @Override
    public String toString() {
        if (cachedToString == null) {
            cachedToString = getTime() + " " + getDay() + " of " + getSeason() + ", year " + getYear();
        }
        return cachedToString;
    }
}