package me.dufek.securitydrones.algorithm.spanningtree;

import cz.cuni.amis.pogamut.base3d.worldview.object.Location;
import me.dufek.securitydrones.algorithm.grid.Cell;

/**
 * Edge of spanning tree. It consists from two spanning tree vertices.
 *
 * @author Jan Dufek
 */
public class SpanningTreeEdge {

    /**
     * Start vertex.
     */
    private final SpanningTreeVertex startVertex;
    
    /**
     * End vertex.
     */
    private final SpanningTreeVertex endVertex;

    public SpanningTreeEdge(SpanningTreeVertex startVertex, SpanningTreeVertex endVertex) {
        this.startVertex = startVertex;
        this.endVertex = endVertex;
    }

    public Location getStartLocation() {
        return startVertex.getCell().getCenterLocation();
    }

    public Location getEndLocation() {
        return endVertex.getCell().getCenterLocation();
    }

    public SpanningTreeVertex getStartVertex() {
        return startVertex;
    }

    public SpanningTreeVertex getEndVertex() {
        return endVertex;
    }
}
