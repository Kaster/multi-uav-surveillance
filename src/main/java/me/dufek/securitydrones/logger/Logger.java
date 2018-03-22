package me.dufek.securitydrones.logger;

import me.dufek.securitydrones.main.Preferences;

/**
 * Logger is used to log information directly to the console.
 *
 * @author Jan Dufek
 */
public class Logger {

    public static void log(String message) {
        if (Preferences.LOGGING_ENABLED) {
            System.out.println(message);
        }
    }
}
