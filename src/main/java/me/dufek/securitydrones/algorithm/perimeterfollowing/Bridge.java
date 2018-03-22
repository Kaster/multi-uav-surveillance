package me.dufek.securitydrones.algorithm.perimeterfollowing;

import cz.cuni.amis.pogamut.base3d.worldview.object.Location;
import me.dufek.securitydrones.area.Area;

/**
 * Bridge is a data structure connecting two areas in two particular points.
 *
 * @author Jan Dufek
 */
public class Bridge {

    /**
     * Start area of the bridge.
     */
    private final Area startArea;
    
    /**
     * Start location in the start area.
     */
    private final Location startLocation;
    
    /**
     * End area of the bridge.
     */
    private final Area endArea;
    
    /**
     * End location in the start area.
     */
    private final Location endLocation;
    
    /**
     * Length of the bridge.
     */
    private double length;

    public Bridge(Area startArea, Location startLocation, Area endArea, Location endLocation, double length) {
        this.startArea = startArea;
        this.startLocation = startLocation;
        this.endArea = endArea;
        this.endLocation = endLocation;
        this.length = length;
    }

    public Area getStartArea() {
        return this.startArea;
    }

    public Location getStartLocation() {
        return this.startLocation;
    }

    public Area getEndArea() {
        return this.endArea;
    }

    public Location getEndLocation() {
        return this.endLocation;
    }

    public double getLenght() {
        return this.length;
    }
}
