package me.dufek.securitydrones.conversion;

/**
 *
 * @author Jan Dufek
 */
public class Scale {

    public static double minMaxScale(double number, double oldMin, double oldMax, double newMin, double newMax) {
        // Scale of vector
        double scale = (newMax - newMin) / (oldMax - oldMin);

        // Shift of vector
        double shift = newMin - (oldMin * scale);

        // Transformation
        double scaledNumber = (number * scale) + shift;

        return scaledNumber;
    }
}
