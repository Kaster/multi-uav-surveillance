package me.dufek.securitydrones.algorithm.systematic;

import java.util.ArrayList;
import me.dufek.securitydrones.algorithm.grid.Cell;
import me.dufek.securitydrones.uav.request.UAVRequest;

/**
 * Cycle represents a path. The way points are particular cells.
 *
 * @author Jan Dufek
 */
public class Cycle {

    /**
     * List of cells. These are considered to be way points.
     */
    private final ArrayList<Cell> list;

    /**
     * UAV reference to which this cycle belongs.
     */
    private UAVRequest uavRequest;

    public Cycle() {
        this.list = new ArrayList<Cell>();
    }

    public ArrayList<Cell> getList() {
        return this.list;
    }

    /**
     * Add cell to the cycle.
     *
     * @param cell Cell.
     */
    public void add(Cell cell) {
        this.list.add(cell);
    }

    public int getCellIndex(Cell cell) {
        int index = 0;

        for (Cell cycleCell : list) {
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

    public Cell get(int index) {
        return list.get(index);
    }

    public void setUAVRequest(UAVRequest uavRequest) {
        this.uavRequest = uavRequest;
    }

    public UAVRequest getUAVRequest() {
        return this.uavRequest;
    }
}
