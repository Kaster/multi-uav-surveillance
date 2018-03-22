package me.dufek.securitydrones.algorithm.spanningtree;

import me.dufek.securitydrones.algorithm.grid.Cell;

/**
 * Vertex of spanning tree.
 *
 * @author Jan Dufek
 */
public class SpanningTreeVertex {

    /**
     * Cell.
     */
    private final Cell cell;

    public SpanningTreeVertex(Cell cell) {
        this.cell = cell;
    }

    public Cell getCell() {
        return this.cell;
    }
}
