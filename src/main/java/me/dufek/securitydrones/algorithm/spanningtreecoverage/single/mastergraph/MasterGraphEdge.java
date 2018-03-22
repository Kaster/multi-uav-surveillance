package me.dufek.securitydrones.algorithm.spanningtreecoverage.single.mastergraph;

import me.dufek.securitydrones.algorithm.spanningtree.SpanningTreeVertex;

/**
 * Edge of the master graph.
 *
 * @author Jan Dufek
 */
public class MasterGraphEdge {

    /**
     * Edge start spanning tree.
     */
    private final MasterGraphVertex startMasterGraph;

    /**
     * Edge start point on the start spanning tree.
     */
    private final SpanningTreeVertex startSpanningTree;

    /**
     * Edge end spanning tree.
     */
    private final MasterGraphVertex endMasterGraph;

    /**
     * Edge end point on the end spanning tree.
     */
    private final SpanningTreeVertex endSpanningTree;

    /**
     * Weight of the edge.
     */
    private double weight;

    public MasterGraphEdge(MasterGraphVertex startMasterGraph, SpanningTreeVertex startSpanningTree, MasterGraphVertex endMasterGraph, SpanningTreeVertex endSpanningTree) {
        this.startMasterGraph = startMasterGraph;
        this.startSpanningTree = startSpanningTree;
        this.endMasterGraph = endMasterGraph;
        this.endSpanningTree = endSpanningTree;
    }

    public MasterGraphVertex getStartMasterGraph() {
        return this.startMasterGraph;
    }

    public SpanningTreeVertex getStartSpanningTree() {
        return this.startSpanningTree;
    }

    public MasterGraphVertex getEndMasterGraph() {
        return this.endMasterGraph;
    }

    public SpanningTreeVertex getEndSpanningTree() {
        return this.endSpanningTree;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getWeight() {
        return this.weight;
    }
}
