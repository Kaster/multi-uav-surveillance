package me.dufek.securitydrones.conversion;

import cz.cuni.amis.pogamut.base3d.worldview.object.Location;

/**
 * This class is used to convert between map and UAV coordinates.
 *
 * @author Jan Dufek
 */
public class MapUAVConversion {

    public static Location mapToUAV(Location mapLocation, Location UAVStartLocation) {
        return Location.sub(UAVStartLocation, mapLocation);
    }

    public static Location UAVToMap(Location UAVLocation, Location UAVStartLocation) {
        Location UAVLocationTranslated = new Location(-UAVLocation.x, -UAVLocation.y, UAVLocation.z);
        return Location.add(UAVLocationTranslated, UAVStartLocation);
    }
}
