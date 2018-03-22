package me.dufek.securitydrones.algorithm.spanningtree;

import cz.cuni.amis.pogamut.base3d.worldview.object.Location;
import java.io.Serializable;
import java.util.ArrayList;
import me.dufek.securitydrones.algorithm.spanningtreecoverage.cycle.Cycle;
import me.dufek.securitydrones.algorithm.grid.Cell;
import me.dufek.securitydrones.algorithm.grid.Grid;
import me.dufek.securitydrones.algorithm.grid.Neighbour;
import me.dufek.securitydrones.algorithm.grid.SubCell;
import me.dufek.securitydrones.algorithm.spanningtreecoverage.single.mastergraph.MasterGraphEdge;

/**
 * Spanning tree is a data structure representing a spanning tree. It contains
 * vertices and edges. Also it provides functions and methods for building and
 * manipulation with the spanning tree.
 *
 * @author Jan Dufek
 */
public class SpanningTree {

    /**
     * Vertices of the spanning tree.
     */
    private final ArrayList<SpanningTreeVertex> vertices;

    /**
     * Edges of the spanning tree.
     */
    private final ArrayList<SpanningTreeEdge> edges;

    /**
     * Start of growth.
     */
    private final Location growthStart;

    /**
     * Cycle around the spanning tree.
     */
    private Cycle cycle;

    /**
     * Start vertex.
     */
    private SpanningTreeVertex start;

    /**
     * Is spanning tree completely built?
     */
    private boolean completed = false;

    public SpanningTree(Location growthStart) {
        vertices = new ArrayList<SpanningTreeVertex>();
        edges = new ArrayList<SpanningTreeEdge>();
        this.growthStart = growthStart;
    }

    public ArrayList<SpanningTreeVertex> getVertices() {
        return this.vertices;
    }

    public ArrayList<SpanningTreeEdge> getEdges() {
        return this.edges;
    }

    public void addVertex(SpanningTreeVertex vertex) {
        vertex.getCell().setInSpanningTree();
        vertices.add(vertex);
    }

    public void addStartVertex(SpanningTreeVertex vertex) {
        this.start = vertex;
        this.addVertex(vertex);
    }

    public SpanningTreeVertex getStartVertex() {
        return this.start;
    }

    /**
     * Get the sub-cell of any cell which is the nearest to the growth start.
     *
     * @return Start sub-cell.
     */
    public SubCell getStartSubCell() {
        if (start == null) {
            return null;
        }

        double minimalDistance = Double.MAX_VALUE;
        SubCell startSubCell = null;
        for (SubCell subCell : start.getCell().getSubCells()) {
            Location subCellLocation = subCell.getLocation();

            double distance = Location.getDistance2D(growthStart, subCellLocation);

            if (distance < minimalDistance) {
                minimalDistance = distance;
                startSubCell = subCell;
            }
        }

        return startSubCell;
    }

    public void setCycle(Cycle cycle) {
        this.cycle = cycle;
    }

    public Cycle getCycle() {
        return this.cycle;
    }

    public void addEdge(Neighbour neighbour) {
        setBarriers(neighbour);

        edges.add(neighbour.getEdge());
    }

    public void removeEdge(Neighbour neighbour) {
        unSetBarriers(neighbour);

        edges.remove(neighbour.getEdge());
    }

    public void addEdge(SpanningTreeEdge edge) {
        edges.add(edge);
    }

    /**
     * Add edge from master graph to spanning tree.
     *
     * @param edge Master graph edge.
     */
    public void addEdge(MasterGraphEdge edge) {
        SpanningTreeVertex start = edge.getStartSpanningTree();

        Neighbour neighbour = null;

        for (Neighbour startNeighbour : start.getCell().getNeighbours().getList()) {
            if (edge.getEndSpanningTree().getCell() == startNeighbour.getVertex().getCell()) {
                neighbour = startNeighbour;
            }
        }

        if (neighbour == null) {
            throw new IllegalStateException("Neigbour not found.");
        }

        addEdge(neighbour);
    }

    /**
     * Join this spanning tree with another one.
     *
     * @param spanningTree Spanning tree.
     */
    public void add(SpanningTree spanningTree) {
        for (SpanningTreeEdge edge : spanningTree.getEdges()) {
            this.addVertex(edge.getStartVertex());
            this.addVertex(edge.getEndVertex());
            this.addEdge(edge);
        }

        if (spanningTree.getEdges().isEmpty()) {
            for (SpanningTreeVertex spanningTreeVertex : spanningTree.getVertices()) {
                this.addVertex(spanningTreeVertex);
            }
        }
    }

//    public void addEdge(Cell startCell, Cell endCell) {
//        edges.add(new SpanningTreeEdge(new SpanningTreeVertex(startCell), new SpanningTreeVertex(endCell)));
//    }
    /**
     * Set barriers which are created by this spanning tree.
     *
     * @param neighbour Neighbors.
     * @throws IllegalStateException
     */
    private void setBarriers(Neighbour neighbour) throws IllegalStateException {
        switch (neighbour.getType()) {
            case TOP:
                addTopBarrier(neighbour);
                break;
            case BOTTOM:
                addBottomBarrier(neighbour);
                break;
            case RIGHT:
                addRightBarrier(neighbour);
                break;
            case LEFT:
                addLeftBarrier(neighbour);
                break;
            default:
                throw new IllegalStateException("Unknown neighbour type.");
        }
    }

    /**
     * Add neighbor bounding from the top.
     *
     * @param neighbour Neighbor.
     */
    private void addTopBarrier(Neighbour neighbour) {
        SpanningTreeVertex actualVertex = neighbour.getEdge().getStartVertex();
        SpanningTreeVertex neighbourVertex = neighbour.getVertex();

        actualVertex.getCell().getSubCells().getTopRightSubCell().setLeftCrossed();
        actualVertex.getCell().getSubCells().getTopLeftSubCell().setRightCrossed();

        neighbourVertex.getCell().getSubCells().getBottomRightSubCell().setLeftCrossed();
        neighbourVertex.getCell().getSubCells().getBottomLeftSubCell().setRightCrossed();
    }

    /**
     * Add neighbor bounding from the bottom.
     *
     * @param neighbour Neighbor.
     */
    private void addBottomBarrier(Neighbour neighbour) {
        SpanningTreeVertex actualVertex = neighbour.getEdge().getStartVertex();
        SpanningTreeVertex neighbourVertex = neighbour.getVertex();

        actualVertex.getCell().getSubCells().getBottomRightSubCell().setLeftCrossed();
        actualVertex.getCell().getSubCells().getBottomLeftSubCell().setRightCrossed();

        neighbourVertex.getCell().getSubCells().getTopRightSubCell().setLeftCrossed();
        neighbourVertex.getCell().getSubCells().getTopLeftSubCell().setRightCrossed();
    }

    /**
     * Add neighbor bounding from the right.
     *
     * @param neighbour Neighbor.
     */
    private void addRightBarrier(Neighbour neighbour) {
        SpanningTreeVertex actualVertex = neighbour.getEdge().getStartVertex();
        SpanningTreeVertex neighbourVertex = neighbour.getVertex();

        actualVertex.getCell().getSubCells().getTopRightSubCell().setBottomCrossed();
        actualVertex.getCell().getSubCells().getBottomRightSubCell().setTopCrossed();

        neighbourVertex.getCell().getSubCells().getTopLeftSubCell().setBottomCrossed();
        neighbourVertex.getCell().getSubCells().getBottomLeftSubCell().setTopCrossed();
    }

    /**
     * Add neighbor bounding from the left.
     *
     * @param neighbour Neighbor.
     */
    private void addLeftBarrier(Neighbour neighbour) {
        SpanningTreeVertex actualVertex = neighbour.getEdge().getStartVertex();
        SpanningTreeVertex neighbourVertex = neighbour.getVertex();

        actualVertex.getCell().getSubCells().getTopLeftSubCell().setBottomCrossed();
        actualVertex.getCell().getSubCells().getBottomLeftSubCell().setTopCrossed();

        neighbourVertex.getCell().getSubCells().getTopRightSubCell().setBottomCrossed();
        neighbourVertex.getCell().getSubCells().getBottomRightSubCell().setTopCrossed();
    }

    /**
     * Remove all barriers from neighbor.
     *
     * @param neighbour Neighbor.
     * @throws IllegalStateException
     */
    private void unSetBarriers(Neighbour neighbour) throws IllegalStateException {
        switch (neighbour.getType()) {
            case TOP:
                removeTopBarrier(neighbour);
                break;
            case BOTTOM:
                removeBottomBarrier(neighbour);
                break;
            case RIGHT:
                removeRightBarrier(neighbour);
                break;
            case LEFT:
                removeLeftBarrier(neighbour);
                break;
            default:
                throw new IllegalStateException("Unknown neighbour type.");
        }
    }

    /**
     * Remove neighbor bounding from the top.
     *
     * @param neighbour Neighbor.
     */
    private void removeTopBarrier(Neighbour neighbour) {
        SpanningTreeVertex actualVertex = neighbour.getEdge().getStartVertex();
        SpanningTreeVertex neighbourVertex = neighbour.getVertex();

        actualVertex.getCell().getSubCells().getTopRightSubCell().setLeftUnCrossed();
        actualVertex.getCell().getSubCells().getTopLeftSubCell().setRightUnCrossed();

        neighbourVertex.getCell().getSubCells().getBottomRightSubCell().setLeftUnCrossed();
        neighbourVertex.getCell().getSubCells().getBottomLeftSubCell().setRightUnCrossed();
    }

    /**
     * Remove neighbor bounding from the bottom.
     *
     * @param neighbour Neighbor.
     */
    private void removeBottomBarrier(Neighbour neighbour) {
        SpanningTreeVertex actualVertex = neighbour.getEdge().getStartVertex();
        SpanningTreeVertex neighbourVertex = neighbour.getVertex();

        actualVertex.getCell().getSubCells().getBottomRightSubCell().setLeftUnCrossed();
        actualVertex.getCell().getSubCells().getBottomLeftSubCell().setRightUnCrossed();

        neighbourVertex.getCell().getSubCells().getTopRightSubCell().setLeftUnCrossed();
        neighbourVertex.getCell().getSubCells().getTopLeftSubCell().setRightUnCrossed();
    }

    /**
     * Remove neighbor bounding from the right.
     *
     * @param neighbour Neighbor.
     */
    private void removeRightBarrier(Neighbour neighbour) {
        SpanningTreeVertex actualVertex = neighbour.getEdge().getStartVertex();
        SpanningTreeVertex neighbourVertex = neighbour.getVertex();

        actualVertex.getCell().getSubCells().getTopRightSubCell().setBottomUnCrossed();
        actualVertex.getCell().getSubCells().getBottomRightSubCell().setTopUnCrossed();

        neighbourVertex.getCell().getSubCells().getTopLeftSubCell().setBottomUnCrossed();
        neighbourVertex.getCell().getSubCells().getBottomLeftSubCell().setTopUnCrossed();
    }

    /**
     * Remove neighbor bounding from the left.
     *
     * @param neighbour Neighbor.
     */
    private void removeLeftBarrier(Neighbour neighbour) {
        SpanningTreeVertex actualVertex = neighbour.getEdge().getStartVertex();
        SpanningTreeVertex neighbourVertex = neighbour.getVertex();

        actualVertex.getCell().getSubCells().getTopLeftSubCell().setBottomUnCrossed();
        actualVertex.getCell().getSubCells().getBottomLeftSubCell().setTopUnCrossed();

        neighbourVertex.getCell().getSubCells().getTopRightSubCell().setBottomUnCrossed();
        neighbourVertex.getCell().getSubCells().getBottomRightSubCell().setTopUnCrossed();
    }

    /**
     * Get free neighbors which are not in any spanning tree.
     *
     * @return List of free neighbors.
     */
    public ArrayList<Neighbour> getFreeNeighbours() {
        ArrayList<Neighbour> freeNeighbours = new ArrayList<Neighbour>();

        for (SpanningTreeVertex vertex : vertices) {
            for (Neighbour neighbour : vertex.getCell().getNeighbours().getList()) {
                Cell neighbourCell = neighbour.getCell();

                if (neighbourCell != null && !neighbourCell.isInSpanningTree()) {
                    freeNeighbours.add(neighbour);
                }
            }
        }

        return freeNeighbours;
    }

    /**
     * Get minimal distance from this spanning tree to neighbor.
     *
     * @param neighbour Neighbor.
     * @return Minimal distance.
     */
    public double getMinimalDistance(Neighbour neighbour) {
        Location vertexLocation = neighbour.getCell().getCenterLocation();

        double minimalDistance = Double.MAX_VALUE;

        for (SpanningTreeVertex spanningTreeVertex : vertices) {
            Location spanningTreeVertexLocation = spanningTreeVertex.getCell().getCenterLocation();

            double distance = Location.getDistance2D(vertexLocation, spanningTreeVertexLocation);

            if (distance < minimalDistance) {
                minimalDistance = distance;
            }
        }

        return minimalDistance;
    }

    public void addNeighbour(Neighbour neighbour) {
        neighbour.getCell().setInSpanningTree();
        this.addVertex(neighbour.getVertex());
        this.addEdge(neighbour);
    }

    public boolean isCompleted() {
        return this.completed;
    }

    public void setCompleted() {
        this.completed = true;
    }

//    public Vertex getNearestVertex(Location cellLocation) {
//        double minimalDistance = Double.MAX_VALUE;
//        Vertex nearestVertex = null;
//        
//        for (Vertex vertex : vertices) {
//            Location vertexLocation = vertex.getCell().getCenterLocation();
//            double distance = Location.getDistance2D(vertexLocation, cellLocation);
//            
//            if (distance < minimalDistance) {
//                minimalDistance = distance;
//                nearestVertex = vertex;
//            }
//        }
//        
//        return nearestVertex;
//    }
//    public SpanningTree getCopy() {
//        ArrayList<SpanningTreeVertex> verticesCopy = new ArrayList<SpanningTreeVertex>(this.vertices.size());
//
//        for (SpanningTreeVertex vertex : this.vertices) {
//            SpanningTreeVertex vertexCopy = vertex.getCopy();
//            verticesCopy.add(vertexCopy);
//        }
//
//        ArrayList<SpanningTreeEdge> edgesCopy = new ArrayList<SpanningTreeEdge>(this.edges.size());
//    }
    public Grid getGrid() {
        ArrayList<Cell> cells = new ArrayList<Cell>(vertices.size());

        for (SpanningTreeVertex vertex : vertices) {
            cells.add(vertex.getCell());
        }

        Grid grid = new Grid(cells);

        return grid;
    }
}
