package me.dufek.securitydrones.algorithm.spanningtreecoverage.single.mastergraph;

import java.util.Comparator;

/**
 * Edge comparator compares edges of master graph according to their weight.
 *
 * @author Jan Dufek
 */
public class EdgeWeightComparator implements Comparator<MasterGraphEdge> {

    @Override
    public int compare(MasterGraphEdge edge1, MasterGraphEdge edge2) {
        return Double.compare(edge1.getWeight(), edge2.getWeight());
    }
}
