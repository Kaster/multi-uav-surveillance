package me.dufek.securitydrones.algorithm.grid;

import me.dufek.securitydrones.algorithm.spanningtree.SpanningTreeEdge;
import me.dufek.securitydrones.algorithm.spanningtree.SpanningTreeVertex;

/**
 * Data structure used to store all the neighbors of particular call.
 *
 * @author Jan Dufek
 */
public class Neighbours {

    /**
     * Cell.
     */
    private final Cell cell;

    /**
     * Array of neighbors.
     */
    private final Neighbour[] neighbours = new Neighbour[4];

    /**
     * Initialize empty neighbors.
     *
     * @param cell Cell.
     */
    public Neighbours(Cell cell) {
        this.cell = cell;
    }

    public void setTopNeighbour(Neighbour topNeighbour) {
        this.neighbours[0] = topNeighbour;
    }

    public void setTopNeighbour(Cell cell, double distance) {
        SpanningTreeEdge edge = new SpanningTreeEdge(new SpanningTreeVertex(this.cell), new SpanningTreeVertex(cell));
        Neighbour neighbour = new Neighbour(cell, edge, distance, NeighbourType.TOP);
        this.neighbours[0] = neighbour;
    }

    public Neighbour getTopNeighbour() {
        return this.neighbours[0];
    }

    public void setBottomNeighbour(Neighbour bottomNeighbour) {
        this.neighbours[1] = bottomNeighbour;
    }

    public void setBottomNeighbour(Cell cell, double distance) {
        SpanningTreeEdge edge = new SpanningTreeEdge(new SpanningTreeVertex(this.cell), new SpanningTreeVertex(cell));
        Neighbour neighbour = new Neighbour(cell, edge, distance, NeighbourType.BOTTOM);
        this.neighbours[1] = neighbour;
    }

    public Neighbour getBottomNeighbour() {
        return this.neighbours[1];
    }

    public void setRightNeighbour(Neighbour rightNeighbour) {
        this.neighbours[2] = rightNeighbour;
    }

    public void setRightNeighbour(Cell cell, double distance) {
        SpanningTreeEdge edge = new SpanningTreeEdge(new SpanningTreeVertex(this.cell), new SpanningTreeVertex(cell));
        Neighbour neighbour = new Neighbour(cell, edge, distance, NeighbourType.RIGHT);
        this.neighbours[2] = neighbour;
    }

    public Neighbour getRightNeighbour() {
        return this.neighbours[2];
    }

    public void setLeftNeighbour(Neighbour leftNeighbour) {
        this.neighbours[3] = leftNeighbour;
    }

    public void setLeftNeighbour(Cell cell, double distance) {
        SpanningTreeEdge edge = new SpanningTreeEdge(new SpanningTreeVertex(this.cell), new SpanningTreeVertex(cell));
        Neighbour neighbour = new Neighbour(cell, edge, distance, NeighbourType.LEFT);
        this.neighbours[3] = neighbour;
    }

    public Neighbour getLeftNeighbour() {
        return this.neighbours[3];
    }

    public Neighbour[] getList() {
        return this.neighbours;
    }
}
