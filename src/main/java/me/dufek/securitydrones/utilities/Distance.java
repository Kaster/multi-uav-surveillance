package me.dufek.securitydrones.utilities;

import cz.cuni.amis.pogamut.base3d.worldview.object.Location;

/**
 *
 * @author Jan Dufek
 */
public class Distance {
    public static boolean LocationEquals2D(Location location1, Location location2, double epsilon) {
        return Distance.equals(location1.x, location2.x, epsilon) && Distance.equals(location1.y, location2.y, epsilon);
    }
    
    private static boolean equals(double number1, double number2, double epsilon) {
        if (Math.abs(number1 - number2) <= epsilon) {
            return true;
        } else {
            return false;
        }
    }
}
