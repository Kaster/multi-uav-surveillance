package me.dufek.securitydrones.algorithm.spanningtree;

import cz.cuni.amis.pogamut.base3d.worldview.object.Location;
import java.util.ArrayList;
import me.dufek.securitydrones.algorithm.grid.Cell;
import me.dufek.securitydrones.algorithm.grid.Grid;
import me.dufek.securitydrones.algorithm.grid.Neighbour;
import me.dufek.securitydrones.uav.UAV;
import me.dufek.securitydrones.uav.UAVs;
import me.dufek.securitydrones.uav.request.UAVRequest;
import me.dufek.securitydrones.uav.request.UAVRequests;

/**
 * Data structure for keeping spanning trees.
 *
 * @author Jan Dufek
 */
public class SpanningTrees {

    /**
     * List of spanning trees.
     */
    private final ArrayList<SpanningTree> list;

    /**
     * Grid.
     */
    private final Grid grid;

    public SpanningTrees(Grid grid) {
        list = new ArrayList<SpanningTree>();

        this.grid = grid;

        initialize();
    }

    /**
     * Initialize and create spanning trees.
     */
    private void initialize() {
        initializeSpanningTrees();
        createSpanningTrees();
    }

    public void add(SpanningTree spanningTree) {
        list.add(spanningTree);
    }

    public ArrayList<SpanningTree> getList() {
        return this.list;
    }

    /**
     * Get list of all the spanning trees but one.
     *
     * @param spanningTree Unwanted spanning tree.
     * @return List of spanning tree without the unwanted one.
     */
    public ArrayList<SpanningTree> getListWithout(SpanningTree spanningTree) {
        ArrayList listWithout = new ArrayList<SpanningTree>(list);
        listWithout.remove(spanningTree);

        return listWithout;
    }

    /**
     * Are all spanning trees completed.
     *
     * @return True if they area all completed, false otherwise.
     */
    public boolean areCompleted() {
        for (SpanningTree spanningTree : list) {
            if (!spanningTree.isCompleted()) {
                return false;
            }
        }

        return true;
    }

    /**
     * Initialize spanning trees.
     */
    private void initializeSpanningTrees() {
        for (UAVRequest uavRequest : UAVRequests.getList()) {
            Location location = uavRequest.getStartLocation();
            SpanningTree spanningTree = initializeSpanningTree(location);
            uavRequest.setSpanningTree(spanningTree);
        }

        for (UAV uav : UAVs.listOfUAVs) {
            Location location = uav.getActualLocationMapCoordinates();

            if (location == null) {
                location = uav.getUAVRequest().getStartLocation();
            }

            SpanningTree spanningTree = initializeSpanningTree(location);
            uav.getUAVRequest().setSpanningTree(spanningTree);
        }
    }

    /**
     * Initialize one spanning tree.
     *
     * @param location Location of UAV.
     * @return Spanning tree.
     */
    private SpanningTree initializeSpanningTree(Location location) {
        Cell nearestCell = getNearestCellBigGrid(location);

        SpanningTree spanningTree = new SpanningTree(location);
        spanningTree.addStartVertex(new SpanningTreeVertex(nearestCell));

        add(spanningTree);

        return spanningTree;
    }

    /**
     * Create spanning tree.
     */
    private void createSpanningTrees() {
        boolean stop = false;

        while (!stop) {
            for (SpanningTree spanningTree : getList()) {
                if (spanningTree.isCompleted()) {
                    continue;
                }

                Neighbour bestNeighbour = getFreeNeighbourAtMaximalDistance(spanningTree);

                if (bestNeighbour != null) {
                    spanningTree.addNeighbour(bestNeighbour);
                } else {
                    spanningTree.setCompleted();
                }

                if (areCompleted()) {
                    stop = true;
                }
            }
        }
    }

    private Cell getNearestCellBigGrid(Location location) {
        Cell nearestCell = grid.getNearestCell(location);

        return nearestCell;
    }

    /**
     * Selects from all neighbors the one maximizing minimal distance to all
     * other spanning trees.
     *
     * @param actualSpanningTree Current spanning tree.
     * @return Fittest neighbor.
     */
    private Neighbour getFreeNeighbourAtMaximalDistance(SpanningTree actualSpanningTree) {
        ArrayList<Neighbour> freeNeighbours = actualSpanningTree.getFreeNeighbours();
        ArrayList<Neighbour> freeNeighboursAtMinimumCrossingDistance = getNeighboursAtMinimalCrossingDistance(freeNeighbours);

        double maximalDistance = -Double.MAX_VALUE;
        Neighbour maximalDistanceNeighbour = null;

        for (Neighbour neighbour : freeNeighboursAtMinimumCrossingDistance) {
            double distance = 0;

            for (SpanningTree spanningTree : getListWithout(actualSpanningTree)) {
                distance += spanningTree.getMinimalDistance(neighbour);
            }

            if (maximalDistance < distance) {
                maximalDistance = distance;
                maximalDistanceNeighbour = neighbour;
            }
        }

        return maximalDistanceNeighbour;
    }

    /**
     * Get all neighbors with minimal distance.
     *
     * @param neighbours List of neighbors.
     * @return List of neighbors at minimum distance.
     */
    private ArrayList<Neighbour> getNeighboursAtMinimalCrossingDistance(ArrayList<Neighbour> neighbours) {
        double minimalCrossingDistance = getMinimalCrossingDistance(neighbours);

        ArrayList<Neighbour> minimalCrossingDistanceNeighbours = new ArrayList<Neighbour>();

        for (Neighbour neighbour : neighbours) {
            if (neighbour.getDistance() == minimalCrossingDistance) {
                minimalCrossingDistanceNeighbours.add(neighbour);
            }
        }

        return minimalCrossingDistanceNeighbours;
    }

    /**
     * Get minimal distance to neighbors.
     *
     * @param neighbours List of neighbors.
     * @return Minimal distance.
     */
    private double getMinimalCrossingDistance(ArrayList<Neighbour> neighbours) {
        double minimalCrossingDistance = Double.MAX_VALUE;

        for (Neighbour neighbour : neighbours) {
            double crossingDistance = neighbour.getDistance();

            if (crossingDistance < minimalCrossingDistance) {
                minimalCrossingDistance = crossingDistance;
            }
        }

        return minimalCrossingDistance;
    }
}
