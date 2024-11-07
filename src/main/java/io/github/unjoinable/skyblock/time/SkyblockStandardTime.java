package io.github.unjoinable.skyblock.time;

import io.github.unjoinable.skyblock.util.StringUtils;
import net.minestom.server.MinecraftServer;
import net.minestom.server.timer.TaskSchedule;
import org.jetbrains.annotations.NotNull;

/**
 * Represents the standard time system for Skyblock.
 * This class manages and updates the in-game time, including year, month, day, hours, and minutes.
 */
public class SkyblockStandardTime {
    private static final long SKYBLOCK_START = 1560275700000L; //Constant Time actual starting of Hypixel Skyblock
    private Season season;
    private int year;
    private byte month;
    private byte day;
    private byte hours;
    private byte minutes;

    /**
     * Constructs a new SkyblockStandardTime instance.
     * Initializes a scheduler task to update the time every 8300 milliseconds.
     * 8300 is 8.3 seconds which is equivalent of 10 minutes of Skyblock time.
     */
    public SkyblockStandardTime() {
        MinecraftServer.getSchedulerManager().scheduleTask(() -> {
            update();
        }, TaskSchedule.immediate(), TaskSchedule.millis(8300));
    }

    /**
     * Updates the current Skyblock time based on the real-world time elapsed since the Skyblock start.
     * This method calculates and sets the current year, month, day, hours, minutes, and season.
     */
    private void update() {
        long skyBlockMillis = (System.currentTimeMillis() - SKYBLOCK_START) * 72;

        long seconds = skyBlockMillis / 1000;
        long minutes = seconds / 60;
        int hours = (int) (minutes / 60);
        int days = hours / 24;
        int months = days / 31;

        //updating values
        this.year = months / 12 + 1; //Since it starts from Year 1 not Year 0
        this.month = (byte) (months % 12);
        this.day = (byte) (days % 31 + 1);
        this.hours = (byte) (hours % 24);
        this.minutes = (byte) (((int) ((minutes % 60) / 10) ) * 10);  //rounding off to nearest 10s
        this.season = Season.byIndex(this.month);
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
    public @NotNull String getTime() {
        if (hours > 12) {
            return (hours - 12) + ":" + StringUtils.toDoubleDigit(minutes) + " PM";
        }
        return hours + ":" + StringUtils.toDoubleDigit(minutes);
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
        return !isDayTime();
    }
}
