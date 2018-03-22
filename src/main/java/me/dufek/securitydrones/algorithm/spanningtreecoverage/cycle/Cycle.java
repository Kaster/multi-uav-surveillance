package me.dufek.securitydrones.algorithm.spanningtreecoverage.cycle;

import java.util.ArrayList;
import me.dufek.securitydrones.algorithm.grid.SubCell;
import me.dufek.securitydrones.uav.UAV;

/**
 * Cycle represents a path. The way points are particular sub-cells.
 *
 * @author Jan Dufek
 */
public class Cycle {

    /**
     * List of sub-cells. These are considered to be way points.
     */
    private final ArrayList<SubCell> list;

    /**
     * UAV reference to which this cycle belongs.
     */
    private UAV uav;

    public Cycle() {
        this.list = new ArrayList<SubCell>();
    }

    public ArrayList<SubCell> getList() {
        return this.list;
    }

    /**
     * Add sub-cell to the cycle.
     *
     * @param subCell Sub-cell.
     */
    public void add(SubCell subCell) {
        this.list.add(subCell);
    }

    public int getCellIndex(SubCell cell) {
        int index = 0;

        for (SubCell cycleCell : list) {
            if (cycleCell == cell) {
                return index;
            }

            index++;
        }

        throw new IllegalStateException("Index not found.");
    }

    public int size() {
        return list.size();
    }

    public SubCell get(int index) {
        return list.get(index);
    }

    public void setUAV(UAV uav) {
        this.uav = uav;
    }

    public UAV getUAV() {
        return this.uav;
    }
}
