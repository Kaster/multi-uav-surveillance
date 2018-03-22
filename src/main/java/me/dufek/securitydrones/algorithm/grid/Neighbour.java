package me.dufek.securitydrones.algorithm.grid;

import me.dufek.securitydrones.algorithm.spanningtree.SpanningTreeEdge;
import me.dufek.securitydrones.algorithm.spanningtree.SpanningTreeVertex;

/**
 * Neighbor of particular cell.
 *
 * @author Jan Dufek
 */
public class Neighbour {

    /**
     * Cell.
     */
    private final Cell cell;

    /**
     * Edge of spanning tree from the cell to this neighbor.
     */
    private final SpanningTreeEdge edge;

    /**
     * Distance from the cell to this neighbor.
     */
    private final double distance;

    /**
     * Type of neighbor.
     */
    private final NeighbourType neighbourType;

    /**
     * Initialization of the neighbor.
     *
     * @param cell Cell.
     * @param edge Edge of spanning tree.
     * @param distance Distance.
     * @param neighbourType Type.
     */
    public Neighbour(Cell cell, SpanningTreeEdge edge, double distance, NeighbourType neighbourType) {
        this.cell = cell;
        this.edge = edge;
        this.distance = distance;
        this.neighbourType = neighbourType;
    }

    public Cell getCell() {
        return this.cell;
    }

    public SpanningTreeVertex getVertex() {
        return new SpanningTreeVertex(this.cell);
    }

    public SpanningTreeEdge getEdge() {
        return this.edge;
    }

    public double getDistance() {
        return this.distance;
    }

    public NeighbourType getType() {
        return this.neighbourType;
    }

    public double getHeat() {
        return this.cell.getHeat();
    }
}
