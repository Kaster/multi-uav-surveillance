package me.dufek.securitydrones.utilities;

/**
 * Angle utilities.
 *
 * @author Jan Dufek
 */
public class Angle {

    /**
     * Computes an angle of the robot in degrees.
     *
     * @param sx sinus
     * @param cx cosinus
     * @return returns angle in degrees.
     */
    public static double getAngle(double sx, double cx) {
        if (sx > 0 && cx > 0) {
            return Math.acos(Math.abs(cx)) * 180 / Math.PI;
        } else if (sx > 0 && cx <= 0) {
            return 180 - Math.acos(Math.abs(cx)) * 180 / Math.PI;
        } else if (sx <= 0 && cx <= 0) {
            return 180 + Math.acos(Math.abs(cx)) * 180 / Math.PI;
        } else {
            return 360 - Math.acos(Math.abs(cx)) * 180 / Math.PI;
        }
    }
}
