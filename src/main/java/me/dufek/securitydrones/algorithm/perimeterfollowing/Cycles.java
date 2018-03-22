package me.dufek.securitydrones.algorithm.perimeterfollowing;

import java.util.ArrayList;

/**
 * Cycles is a data structure used to store individual cycles.
 *
 * @author Jan Dufek
 */
public class Cycles {

    /**
     * List of cycles.
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
}
