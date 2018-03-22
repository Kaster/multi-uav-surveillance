package me.dufek.securitydrones.geometry;

import cz.cuni.amis.pogamut.base3d.worldview.object.Location;

/**
 *
 * @author Jan Dufek
 */
public class Utils {

    public static void rotatePolygon(PolygonDouble polygon, Location center, double angle) {
        for (int i = 0; i < polygon.npoints; i++) {
            Location point = new Location(polygon.xpoints[i], polygon.ypoints[i]);
            Location rotatedPoint = rotatePoint(point, center, angle);

            polygon.xpoints[i] = rotatedPoint.getX();
            polygon.ypoints[i] = rotatedPoint.getY();
        }
    }

    public static Location rotatePoint(Location point, Location center, double angle) {
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);

        double rotatedX = center.getX() + (point.getX() - center.getX()) * cos - (point.getY() - center.getY()) * sin;
        double rotatedY = center.getY() + (point.getX() - center.getX()) * sin + (point.getY() - center.getY()) * cos;

        Location rotatedPoint = new Location(rotatedX, rotatedY);

        return rotatedPoint;
    }
}
