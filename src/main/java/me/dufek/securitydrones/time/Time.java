package me.dufek.securitydrones.time;

import java.util.Calendar;
import java.util.Date;
import me.dufek.securitydrones.conversion.NumberConversion;

/**
 * Time is used to store time and provide it to the various parts of
 * application.
 *
 * @author Jan Dufek
 */
public class Time {

    /**
     * Current time.
     */
    private static double time;

    public static void updateTime(double time) {
        Time.time = time;
    }

    public static double getTime() {
        return Time.time;
    }

    /**
     * Get time in date format for coverage-time graph.
     *
     * @return Time in Data format.
     */
    public static Date getDate() {
        Date date = new Date(Calendar.getInstance().get(Calendar.YEAR), 0, 0, 0, 0, NumberConversion.toInteger(time));

        return date;
    }
}
