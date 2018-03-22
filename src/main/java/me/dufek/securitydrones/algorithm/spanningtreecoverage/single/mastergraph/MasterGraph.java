package me.dufek.securitydrones.algorithm.spanningtreecoverage.single.mastergraph;

import cz.cuni.amis.pogamut.base3d.worldview.object.Location;
import java.util.ArrayList;
import java.util.Collections;
import me.dufek.securitydrones.algorithm.grid.Cell;
import me.dufek.securitydrones.algorithm.grid.Grid;
import me.dufek.securitydrones.algorithm.grid.Neighbour;
import me.dufek.securitydrones.algorithm.grid.SubCell;
import me.dufek.securitydrones.algorithm.grid.SubCellType;
import me.dufek.securitydrones.algorithm.grid.Transition;
import me.dufek.securitydrones.algorithm.spanningtreecoverage.cycle.Cycle;
import me.dufek.securitydrones.algorithm.spanningtree.SpanningTree;
import me.dufek.securitydrones.algorithm.spanningtree.SpanningTreeEdge;
import me.dufek.securitydrones.algorithm.spanningtree.SpanningTreeVertex;

/**
 * Master graph is a data structure used by spanning tree algorithm single
 * version. It is a multigraph whose vertices are spanning trees and edges are
 * connections between them.
 *
 * @author Jan Dufek
 */
public class MasterGraph {

    /**
     * Vertices contains spanning trees.
     */
    private final ArrayList<MasterGraphVertex> vertices;

    /**
     * Edges are connections between spanning trees.
     */
    private final ArrayList<MasterGraphEdge> edges;

    /**
     * Grid.
     */
    private final Grid grid;

    /**
     * List of spanning trees of individual UAVs.
     */
    private final ArrayList<SpanningTree> spanningTrees;

    /**
     * Overall distance circumnavigations all the spanning trees.
     */
    private double overallTravelDistance;

    /**
     * Number of UAVs used.
     */
    private double numberOfUAVs;

    /**
     * Initialize master graph. It is build during initialization.
     *
     * @param grid Grid.
     * @param spanningTrees Spanning trees of individual UAVs.
     */
    public MasterGraph(Grid grid, ArrayList<SpanningTree> spanningTrees) {
        this.vertices = new ArrayList<MasterGraphVertex>();
        this.edges = new ArrayList<MasterGraphEdge>();

        this.grid = grid;
        this.spanningTrees = spanningTrees;

        build();
    }

    /**
     * Build a master graph.
     */
    private void build() {
        getOverallTravelDistance();
        getNumberOfUAVs();

        for (SpanningTree spanningTree : spanningTrees) {
            addSpanningTree(spanningTree);
        }
    }

    /**
     * Gets overall travel distance circumnavigating all the spanning trees.
     */
    public void getOverallTravelDistance() {
        this.overallTravelDistance = grid.getCells().size() * 4;
    }

    public void getNumberOfUAVs() {
        this.numberOfUAVs = spanningTrees.size();
    }

    /**
     * Adds spanning tree to multigraph.
     *
     * @param spanningTree Spanning tree.
     */
    private void addSpanningTree(SpanningTree spanningTree) {
        addVertex(spanningTree);
        addEdges(spanningTree);
    }

    /**
     * Add vertex to multi graph.
     *
     * @param spanningTree Spanning tree.
     */
    private void addVertex(SpanningTree spanningTree) {
        MasterGraphVertex vertex = new MasterGraphVertex(spanningTree);
        vertices.add(vertex);
    }

    /**
     * Add edges to multi graph.
     *
     * @param spanningTree Spanning tree.
     */
    private void addEdges(SpanningTree spanningTree) {
        for (SpanningTreeVertex vertex : spanningTree.getVertices()) {
            addEdge(spanningTree, vertex);
        }
    }

    /**
     * Add one edge to the multi graph.
     *
     * @param spanningTree Spanning tree.
     * @param vertex Vertex.
     */
    private void addEdge(SpanningTree spanningTree, SpanningTreeVertex vertex) {
        Cell cell = vertex.getCell();

        for (Neighbour neighbour : cell.getNeighbours().getList()) {
            Cell neighbourCell = neighbour.getCell();

            if (neighbourCell == null) {
                continue;
            }

            if (!insideSpanningTree(spanningTree, neighbourCell)) {
                MasterGraphVertex startMasterGraph = new MasterGraphVertex(spanningTree);
                SpanningTreeVertex startSpanningTree = new SpanningTreeVertex(cell);
                MasterGraphVertex endMasterGraph = new MasterGraphVertex(getSpanningTree(neighbourCell));
                SpanningTreeVertex endSpanningTree = new SpanningTreeVertex(neighbourCell);
                MasterGraphEdge masterGraphEdge = new MasterGraphEdge(startMasterGraph, startSpanningTree, endMasterGraph, endSpanningTree);

                double weight = getEdgeWeight(masterGraphEdge, neighbour);
                masterGraphEdge.setWeight(weight);

                edges.add(masterGraphEdge);
            }
        }
    }

    /**
     * Get spanning tree for the cell.
     *
     * @param cell Cell.
     * @return Spanning tree.
     */
    private SpanningTree getSpanningTree(Cell cell) {
        for (SpanningTree spanningTree : spanningTrees) {
            for (SpanningTreeVertex vertex : spanningTree.getVertices()) {
                Cell spanningTreeCell = vertex.getCell();

                if (spanningTreeCell == cell) {
                    return spanningTree;
                }
            }
        }

        throw new IllegalStateException("Spanning tree not found.");
    }

    /**
     * Checks if cell is inside spanning tree.
     *
     * @param spanningTree Spanning tree.
     * @param cell Cell.
     * @return True if the cell is inside and false if not.
     */
    private boolean insideSpanningTree(SpanningTree spanningTree, Cell cell) {
        for (SpanningTreeVertex spanningTreeVertex : spanningTree.getVertices()) {
            if (spanningTreeVertex.getCell() == cell) {
                return true;
            }
        }

        return false;
    }

    /**
     * Get weight of one particular edge.
     *
     * @param edge Edge.
     * @param neighbour Neighbor.
     * @return Weight.
     */
    private double getEdgeWeight(MasterGraphEdge edge, Neighbour neighbour) {
        SpanningTree spanningTree1 = edge.getStartMasterGraph().getSpanningTree();
        SpanningTree spanningTree2 = edge.getEndMasterGraph().getSpanningTree();

        Cell spanningTree1Cell = edge.getStartSpanningTree().getCell();
        Cell spanningTree2Cell = edge.getEndSpanningTree().getCell();

        SpanningTree joinedSpanningTree = joinSpanningTrees(spanningTree1, spanningTree2, neighbour);

        Cycle cycle = createCycle(joinedSpanningTree);

        // We need to remove added edge to put spanning trees back to original states
        joinedSpanningTree.removeEdge(neighbour);

        SubCell uav1SubCell = spanningTree1.getStartSubCell();
        SubCell uav2SubCell = spanningTree2.getStartSubCell();

        int cellDistance = getCellDistance(cycle, uav1SubCell, uav2SubCell);

        int spanningTree1TravelDistance = getSpanningTreeLengt(spanningTree1);
        int spanningTree2TravelDistance = getSpanningTreeLengt(spanningTree2);

        int spanningTree1TravelDeviation = spanningTree1TravelDistance - (int) Math.round(overallTravelDistance / numberOfUAVs);
        int spanningTree2TravelDeviation = spanningTree2TravelDistance - (int) Math.round(overallTravelDistance / numberOfUAVs);

        double weight = cellDistance - (overallTravelDistance / numberOfUAVs) - ((spanningTree1TravelDeviation + spanningTree2TravelDeviation) / 2);

        return Math.abs(weight);
    }

    /**
     * Gets the distance circumnavigating particular spanning tree.
     *
     * @param spanningTree Spanning tree.
     * @return Distance.
     */
    public int getSpanningTreeLengt(SpanningTree spanningTree) {
        return spanningTree.getVertices().size() * 4;
    }

    /**
     * Get distance between two cells following the cycle.
     *
     * @param cycle Cycle.
     * @param startCell Start cell.
     * @param endCell End cell.
     * @return Distance.
     */
    private int getCellDistance(Cycle cycle, SubCell startCell, SubCell endCell) {
        int counter = 0;

        boolean counting = false;

        for (SubCell cell : cycle.getList()) {
            if (cell == startCell) {
                counting = true;
            }

            if (cell == endCell) {
                counting = false;
            }

            if (counting) {
                counter++;
            }
        }

        if (counting) {
            for (SubCell cell : cycle.getList()) {
                if (cell == endCell) {
                    counting = false;
                }

                if (counting) {
                    counter++;
                }
            }
        }

        return counter;
    }

    /**
     * Join two spanning trees together.
     *
     * @param spanningTree1 Spanning tree 1.
     * @param spanningTree2 Spanning tree 2.
     * @param neighbour Neighbor used to connect trees.
     * @return Joined spanning tree.
     */
    private SpanningTree joinSpanningTrees(SpanningTree spanningTree1, SpanningTree spanningTree2, Neighbour neighbour) {
        SpanningTree joinedSpanningTree = new SpanningTree(Location.NONE);

        for (SpanningTreeVertex vertex : spanningTree1.getVertices()) {
            joinedSpanningTree.addVertex(vertex);
        }

        for (SpanningTreeEdge edge : spanningTree1.getEdges()) {
            joinedSpanningTree.addEdge(edge);
        }

        for (SpanningTreeVertex vertex : spanningTree2.getVertices()) {
            joinedSpanningTree.addVertex(vertex);
        }

        for (SpanningTreeEdge edge : spanningTree2.getEdges()) {
            joinedSpanningTree.addEdge(edge);
        }

        joinedSpanningTree.addEdge(neighbour);

        return joinedSpanningTree;
    }

    /**
     * Create cycle circumnavigating particular spanning tree.
     *
     * @param spanningTree Spanning tree.
     * @return Cycle.
     */
    public Cycle createCycle(SpanningTree spanningTree) {
        Cycle cycle = new Cycle();

        SubCell startSubCell = spanningTree.getStartSubCell();

        if (startSubCell == null) {
            startSubCell = spanningTree.getVertices().get(0).getCell().getSubCells().getBottomLeftSubCell();
        }

        cycle.add(startSubCell);

        SubCell nextSubCell = startSubCell;

        do {
            SubCell previousSubCell = nextSubCell;

            Transition transition = getTransition(previousSubCell);

            switch (transition) {
                case UP:
                    nextSubCell = getSubCellUp(previousSubCell);
                    break;
                case DOWN:
                    nextSubCell = getSubCellDown(previousSubCell);
                    break;
                case RIGHT:
                    nextSubCell = getSubCellRight(previousSubCell);
                    break;
                case LEFT:
                    nextSubCell = getSubCellLeft(previousSubCell);
                    break;
                default:
                    throw new IllegalStateException("Unknown transition.");
            }

            if (nextSubCell != startSubCell) {
                cycle.add(nextSubCell);
            }

        } while (nextSubCell != startSubCell);

        spanningTree.setCycle(cycle);

        return cycle;
    }

    /**
     * Get sub-cell to the up.
     *
     * @param actualSubCell Sub-cell.
     * @return Up sub-cell.
     */
    public SubCell getSubCellUp(SubCell actualSubCell) {
        SubCell nextSubCell;

        switch (actualSubCell.getType()) {
            case TOP_RIGHT:
                nextSubCell = actualSubCell.getCell().getTopNeighbour().getCell().getSubCells().getBottomRightSubCell();
                break;
            case TOP_LEFT:
                nextSubCell = actualSubCell.getCell().getTopNeighbour().getCell().getSubCells().getBottomLeftSubCell();
                break;
            case BOTTOM_RIGHT:
                nextSubCell = actualSubCell.getCell().getSubCells().getTopRightSubCell();
                break;
            case BOTTOM_LEFT:
                nextSubCell = actualSubCell.getCell().getSubCells().getTopLeftSubCell();
                break;
            default:
                throw new IllegalStateException("Unknown sub cell type.");
        }

        return nextSubCell;
    }

    /**
     * Get sub-cell to the down.
     *
     * @param actualSubCell Sub-cell.
     * @return Down sub-cell.
     */
    public SubCell getSubCellDown(SubCell actualSubCell) {
        SubCell nextSubCell;

        switch (actualSubCell.getType()) {
            case TOP_RIGHT:
                nextSubCell = actualSubCell.getCell().getSubCells().getBottomRightSubCell();
                break;
            case TOP_LEFT:
                nextSubCell = actualSubCell.getCell().getSubCells().getBottomLeftSubCell();
                break;
            case BOTTOM_RIGHT:
                nextSubCell = actualSubCell.getCell().getBottomNeighbour().getCell().getSubCells().getTopRightSubCell();
                break;
            case BOTTOM_LEFT:
                nextSubCell = actualSubCell.getCell().getBottomNeighbour().getCell().getSubCells().getTopLeftSubCell();
                break;
            default:
                throw new IllegalStateException("Unknown sub cell type.");
        }

        return nextSubCell;
    }

    /**
     * Get sub-cell to the right.
     *
     * @param actualSubCell Sub-cell.
     * @return Right sub-cell.
     */
    public SubCell getSubCellRight(SubCell actualSubCell) {
        SubCell nextSubCell;

        switch (actualSubCell.getType()) {
            case TOP_RIGHT:
                nextSubCell = actualSubCell.getCell().getRightNeighbour().getCell().getSubCells().getTopLeftSubCell();
                break;
            case TOP_LEFT:
                nextSubCell = actualSubCell.getCell().getSubCells().getTopRightSubCell();
                break;
            case BOTTOM_RIGHT:
                nextSubCell = actualSubCell.getCell().getRightNeighbour().getCell().getSubCells().getBottomLeftSubCell();
                break;
            case BOTTOM_LEFT:
                nextSubCell = actualSubCell.getCell().getSubCells().getBottomRightSubCell();
                break;
            default:
                throw new IllegalStateException("Unknown sub cell type.");
        }

        return nextSubCell;
    }

    /**
     * Get sub-cell to the left.
     *
     * @param actualSubCell Sub-cell.
     * @return Left sub-cell.
     */
    public SubCell getSubCellLeft(SubCell actualSubCell) {
        SubCell nextSubCell;

        switch (actualSubCell.getType()) {
            case TOP_RIGHT:
                nextSubCell = actualSubCell.getCell().getSubCells().getTopLeftSubCell();
                break;
            case TOP_LEFT:
                nextSubCell = actualSubCell.getCell().getLeftNeighbour().getCell().getSubCells().getTopRightSubCell();
                break;
            case BOTTOM_RIGHT:
                nextSubCell = actualSubCell.getCell().getSubCells().getBottomLeftSubCell();
                break;
            case BOTTOM_LEFT:
                nextSubCell = actualSubCell.getCell().getLeftNeighbour().getCell().getSubCells().getBottomRightSubCell();
                break;
            default:
                throw new IllegalStateException("Unknown sub cell type.");
        }

        return nextSubCell;
    }

    /**
     * Get the next direction from current sub-cell given the fact, that we can
     * not cross the spanning tree.
     *
     * @param subCell Current sub-cell
     * @return Transition
     */
    private Transition getTransition(SubCell subCell) {
        if ((!subCell.isTopCrossed() && !subCell.isBottomCrossed() && !subCell.isRightCrossed() && subCell.isLeftCrossed()) || (subCell.isTopCrossed() && !subCell.isBottomCrossed() && !subCell.isRightCrossed() && subCell.isLeftCrossed())) {
            return Transition.DOWN;
        } else if ((subCell.isTopCrossed() && !subCell.isBottomCrossed() && !subCell.isRightCrossed() && !subCell.isLeftCrossed()) || (subCell.isTopCrossed() && !subCell.isBottomCrossed() && subCell.isRightCrossed() && !subCell.isLeftCrossed())) {
            return Transition.LEFT;
        } else if ((!subCell.isTopCrossed() && !subCell.isBottomCrossed() && subCell.isRightCrossed() && !subCell.isLeftCrossed()) || (!subCell.isTopCrossed() && subCell.isBottomCrossed() && subCell.isRightCrossed() && !subCell.isLeftCrossed())) {
            return Transition.UP;
        } else if ((!subCell.isTopCrossed() && subCell.isBottomCrossed() && !subCell.isRightCrossed() && !subCell.isLeftCrossed()) || (!subCell.isTopCrossed() && subCell.isBottomCrossed() && !subCell.isRightCrossed() && subCell.isLeftCrossed())) {
            return Transition.RIGHT;
        } else if (subCell.getType() == SubCellType.TOP_RIGHT) {
            return Transition.DOWN;
        } else if (subCell.getType() == SubCellType.BOTTOM_RIGHT) {
            return Transition.LEFT;
        } else if (subCell.getType() == SubCellType.BOTTOM_LEFT) {
            return Transition.UP;
        } else if (subCell.getType() == SubCellType.TOP_LEFT) {
            return Transition.RIGHT;
        } else {
            throw new IllegalStateException("Sub cell is not restricted at all.");
        }
    }

    /**
     * Gets minimum spanning tree on the master graph using Kruskal's algorithm.
     *
     * @return Minimal spanning tree.
     */
    public ArrayList<MasterGraphEdge> getMinimalSpanningTree() {
        ArrayList<MasterGraphEdge> minimalSpanningTree = new ArrayList<MasterGraphEdge>();

        ArrayList<ArrayList<MasterGraphVertex>> sets = new ArrayList<ArrayList<MasterGraphVertex>>();

        for (MasterGraphVertex vertex : vertices) {
            makeSet(vertex, sets);
        }

        Collections.sort(edges, new EdgeWeightComparator());

        for (MasterGraphEdge edge : edges) {
            ArrayList<MasterGraphVertex> setStart = findSet(edge.getStartMasterGraph(), sets);
            ArrayList<MasterGraphVertex> setEnd = findSet(edge.getEndMasterGraph(), sets);

            if (setStart != setEnd) {
                minimalSpanningTree.add(edge);
                union(setStart, setEnd, sets);
            }
        }

        return minimalSpanningTree;
    }

    private void makeSet(MasterGraphVertex vertex, ArrayList<ArrayList<MasterGraphVertex>> sets) {
        ArrayList<MasterGraphVertex> set = new ArrayList<MasterGraphVertex>();
        set.add(vertex);

        sets.add(set);
    }

    private ArrayList<MasterGraphVertex> findSet(MasterGraphVertex vertex, ArrayList<ArrayList<MasterGraphVertex>> sets) {
        for (ArrayList<MasterGraphVertex> set : sets) {
            for (MasterGraphVertex masterGraphVertex : set) {
                if (vertex.getSpanningTree() == masterGraphVertex.getSpanningTree()) {
                    return set;
                }
            }
        }

        return null;
    }

    private void union(ArrayList<MasterGraphVertex> set1, ArrayList<MasterGraphVertex> set2, ArrayList<ArrayList<MasterGraphVertex>> sets) {
        set1.addAll(set2);
        sets.remove(set2);
    }
}
