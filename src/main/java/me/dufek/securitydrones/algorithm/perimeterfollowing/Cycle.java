package me.dufek.securitydrones.algorithm.perimeterfollowing;

import cz.cuni.amis.pogamut.base3d.worldview.object.Location;
import java.util.ArrayList;
import me.dufek.securitydrones.algorithm.grid.SubCell;
import me.dufek.securitydrones.uav.UAV;

/**
 * Cycle represents a path. The way points are particular locations.
 *
 * @author Jan Dufek
 */
public class Cycle {

    /**
     * List of locations. These are considered to be way points.
     */
    private final ArrayList<Location> list;
    
    /**
     * UAV reference to which this cycle belongs.
     */
    private UAV uav;

    public Cycle() {
        this.list = new ArrayList<Location>();
    }

    public ArrayList<Location> getList() {
        return this.list;
    }

    /**
     * Add location to the cycle.
     * 
     * @param location Location.
     */
    public void add(Location location) {
        this.list.add(location);
    }

    public int getLocationIndex(Location location) {
        int index = 0;

        for (Location cycleLocation : list) {
            if (Location.equal(location, cycleLocation, 1)) {
                return index;
            }

            index++;
        }

        throw new IllegalStateException("Index not found.");
    }

    public int size() {
        return list.size();
    }

    public Location get(int index) {
        return list.get(index);
    }

    public void setUAV(UAV uav) {
        this.uav = uav;
    }

    public UAV getUAV() {
        return this.uav;
    }

    public void removeLast() {
        this.list.remove(this.list.size() - 1);
    }
}
