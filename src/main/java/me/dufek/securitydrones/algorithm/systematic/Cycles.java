package me.dufek.securitydrones.algorithm.systematic;

import java.util.ArrayList;

/**
 * Cycle represents a path. The way points are particular cells.
 *
 * @author Jan Dufek
 */
public class Cycles {

    /**
     * List of cells. These are considered to be way points.
     */
    private final ArrayList<Cycle> list;

    public Cycles() {
        list = new ArrayList<Cycle>();
    }

    public ArrayList<Cycle> getList() {
        return this.list;
    }

    public void add(Cycle cycle) {
        this.list.add(cycle);
    }

    public void remove(Cycle cycle) {
        this.list.remove(cycle);
    }

    public int size() {
        return this.list.size();
    }
}
