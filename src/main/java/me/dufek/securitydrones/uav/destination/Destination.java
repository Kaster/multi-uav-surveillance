package me.dufek.securitydrones.uav.destination;

import cz.cuni.amis.pogamut.base3d.worldview.object.Location;
import me.dufek.securitydrones.uav.UAV;

/**
 * Represents destination of UAV to be represented in GUI.
 *
 * @author Jan Dufek
 */
public class Destination {

    /**
     * Corresponding UAV.
     */
    private UAV uav;
    
    /**
     * Destination.
     */
    private Location location;

    public Destination(UAV uav, Location location) {
        this.uav = uav;
        this.location = location;
    }

    public UAV getUAV() {
        return this.uav;
    }

    public Location getLocation() {
        return this.location;
    }

    @Override
    public String toString() {
        return uav.getName() + " destination: " + location.toString();
    }
}
