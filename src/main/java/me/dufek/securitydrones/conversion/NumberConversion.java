package me.dufek.securitydrones.conversion;

/**
 * Number conversion is used to convert numbers.
 *
 * @author Jan Dufek
 */
public class NumberConversion {

    public static int toInteger(double number) {
        return (int) Math.round(number);
    }

    public static double roundToNDecimals(double number, int n) {
        if (n < 0) {
            throw new IllegalStateException("Number of decimal places must be positive.");
        } else {
            double quocient = 1;

            for (int i = 0; i < n; i++) {
                quocient *= 10;
            }

            return (double) Math.round(number * quocient) / quocient;
        }
    }
}
