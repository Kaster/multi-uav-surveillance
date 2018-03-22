package me.dufek.securitydrones.algorithm.perimeterfollowing.multiple;

import java.util.ArrayList;
import me.dufek.securitydrones.algorithm.perimeterfollowing.Bridge;
import me.dufek.securitydrones.algorithm.perimeterfollowing.Cycle;
import me.dufek.securitydrones.area.Area;
import me.dufek.securitydrones.uav.UAV;
import me.dufek.securitydrones.uav.request.UAVRequest;

/**
 * Scope represents assignment between UAV and areas which are surveyed by this
 * UAV.
 *
 * @author Jan Dufek
 */
public class Scope {

    /**
     * UAV request.
     */
    private UAVRequest uavRequest;

    /**
     * UAV.
     */
    private UAV uav;

    /**
     * Assigned areas.
     */
    private ArrayList<Area> areas;

    /**
     * Bridges between those areas.
     */
    private ArrayList<Bridge> bridges;
    
    /**
     * Main cycle around this scope.
     */
    private Cycle mainCycle;
    
    /**
     * Cycle for this UAV.
     */
    private Cycle cycle;

//    public Scope(UAV uav) {
//        this.areas = new ArrayList<Area>();
//
//        this.uav = uav;
//    }
    public Scope(UAVRequest uavRequest) {
        this.areas = new ArrayList<Area>();

        this.uavRequest = uavRequest;
    }

    public void addArea(Area area) {
        areas.add(area);
    }

    public Area getArea(int number) {
        return this.areas.get(number);
    }

    public ArrayList<Area> getAreas() {
        return this.areas;
    }

    public int getNumberOfAreas() {
        return areas.size();
    }

    public void setBridges(ArrayList<Bridge> bridges) {
        this.bridges = bridges;
    }

    public ArrayList<Bridge> getBridges() {
        return this.bridges;
    }

    public void setUAV(UAV uav) {
        this.uav = uav;
    }

    public UAV getUAV() {
        return this.uav;
    }

    public UAVRequest getUAVRequest() {
        return this.uavRequest;
    }

    public void setMainCycle(Cycle mainCycle) {
        this.mainCycle = mainCycle;
    }

    public Cycle getMainCycle() {
        return this.mainCycle;
    }

    public void setCycle(Cycle cycle) {
        this.cycle = cycle;
    }

    public Cycle getCycle() {
        return this.cycle;
    }
}
