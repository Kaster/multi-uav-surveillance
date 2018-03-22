package me.dufek.securitydrones.algorithm.spanningtreecoverage.single.mastergraph;

import me.dufek.securitydrones.algorithm.spanningtree.SpanningTree;

/**
 * Vertex of the master graph.
 *
 * @author Jan Dufek
 */
public class MasterGraphVertex {

    /**
     * Spanning tree.
     */
    private final SpanningTree spanningTree;

    public MasterGraphVertex(SpanningTree spanningTree) {
        this.spanningTree = spanningTree;
    }

    public SpanningTree getSpanningTree() {
        return this.spanningTree;
    }
}
