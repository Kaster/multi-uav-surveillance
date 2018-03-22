package me.dufek.securitydrones.algorithm.spanningtreecoverage.single;

import me.dufek.securitydrones.algorithm.spanningtreecoverage.cycle.Cycle;
import me.dufek.securitydrones.algorithm.spanningtreecoverage.cycle.Cycles;
import cz.cuni.amis.pogamut.base3d.worldview.object.Location;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import me.dufek.securitydrones.activeobject.ActiveObject;
import me.dufek.securitydrones.algorithm.Algorithm;
import me.dufek.securitydrones.algorithm.Algorithms;
import me.dufek.securitydrones.algorithm.AreaDivision;
import me.dufek.securitydrones.algorithm.Variability;
import me.dufek.securitydrones.algorithm.grid.Cell;
import me.dufek.securitydrones.algorithm.grid.Grid;
import me.dufek.securitydrones.algorithm.grid.Preferences;
import me.dufek.securitydrones.algorithm.grid.SubCell;
import me.dufek.securitydrones.algorithm.grid.SubCellType;
import me.dufek.securitydrones.algorithm.grid.Transition;
import me.dufek.securitydrones.algorithm.spanningtreecoverage.single.mastergraph.MasterGraph;
import me.dufek.securitydrones.algorithm.spanningtreecoverage.single.mastergraph.MasterGraphEdge;
import me.dufek.securitydrones.algorithm.spanningtree.SpanningTreeEdge;
import me.dufek.securitydrones.algorithm.spanningtree.SpanningTree;
import me.dufek.securitydrones.algorithm.spanningtree.SpanningTrees;
import me.dufek.securitydrones.algorithm.spanningtree.SpanningTreeVertex;
import me.dufek.securitydrones.area.Area;
import me.dufek.securitydrones.area.Areas;
import me.dufek.securitydrones.conversion.BirdviewMapConversion;
import me.dufek.securitydrones.conversion.NumberConversion;
import me.dufek.securitydrones.gui.Window;
import me.dufek.securitydrones.uav.UAV;
import me.dufek.securitydrones.uav.UAVs;
import me.dufek.securitydrones.uav.destination.Destination;
import me.dufek.securitydrones.uav.destination.Destinations;
import me.dufek.securitydrones.uav.objective.flyto.FlyTo;
import me.dufek.securitydrones.uav.objective.Takeoff;
import me.dufek.securitydrones.uav.request.UAVRequests;
import me.dufek.securitydrones.utilities.VisibleArea;

/**
 * Spanning tree coverage algorithm single. It connects all the spanning trees
 * built for individual UAVs together. The goal it to crate shared cyclic
 * non-overlapping path where UAV will be spaced equally.
 *
 * @author Jan Dufek
 */
public class SpanningTreeCoverageAlgorithmSingle extends Algorithm {

    /**
     * Grid.
     */
    private Grid grid;

    /**
     * Spanning trees for individual UAVs.
     */
    private SpanningTrees uavSpanningTrees;

    /**
     * Single spanning tree.
     */
    private SpanningTree spanningTree;

    /**
     * Cycles for individual UAVs.
     */
    private Cycles cycles;

    /**
     * The main cycle.
     */
    private Cycle mainCycle;

    public SpanningTreeCoverageAlgorithmSingle(Window mainWindow, Variability variability) {
        super(mainWindow, variability);
    }

    /**
     * Called before logic only once.
     */
    @Override
    public void beforeLogic() {
        super.beforeLogic();
    }

    /**
     * Initialize the algorithm.
     */
    private void initialize() {
        grid = null;
        uavSpanningTrees = null;
        spanningTree = null;
        cycles = null;
        mainCycle = null;

        if (Areas.noAreas()) {
            return;
        }

        if (UAVs.noUAVs() && UAVRequests.noRequest()) {
            return;
        }

        createGrids();
        createSpanningTrees();

        createSpanningTree();

        createMainCycle();
    }

    /**
     * Cerate grids for individual UAVs.
     */
    private void createGrids() {
        double minimumVisibleAreaWidth = VisibleArea.getMinimumVisibleAreaWidth();

        grid = new Grid(2 * minimumVisibleAreaWidth);
    }

    /**
     * Creates spanning trees for individual UAVs.
     */
    private void createSpanningTrees() {
        uavSpanningTrees = new SpanningTrees(grid);
    }

    /**
     * Create single spanning tree.
     */
    private void createSpanningTree() {
        spanningTree = new SpanningTree(Location.NONE);

        for (SpanningTree uavSpanningTree : uavSpanningTrees.getList()) {
            spanningTree.add(uavSpanningTree);
        }

        MasterGraph masterGraph = new MasterGraph(grid, uavSpanningTrees.getList());

        ArrayList<MasterGraphEdge> masterSpanningTree = masterGraph.getMinimalSpanningTree();

        for (MasterGraphEdge edge : masterSpanningTree) {
            spanningTree.addEdge(edge);
        }
    }

    /**
     * Creates main cycle around spanning tree.
     */
    public void createMainCycle() {
        mainCycle = new Cycle();

        SubCell startSubCell = spanningTree.getStartSubCell();

        if (startSubCell == null) {
            startSubCell = spanningTree.getVertices().get(0).getCell().getSubCells().getBottomLeftSubCell();
        }

        if (Areas.intersect(startSubCell)) {
            mainCycle.add(startSubCell);
        }

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

            if (nextSubCell != startSubCell && Areas.intersect(nextSubCell)) {
                mainCycle.add(nextSubCell);
            }

        } while (nextSubCell != startSubCell);

        spanningTree.setCycle(mainCycle);
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
     * Assign paths to UAVs.
     */
    @Override
    public void logic() {
        super.logic();

        for (UAV uav : UAVs.listOfUAVs) {

            if (!uav.isInAir() && !uav.isTakeoff()) {
                assignObjective(uav, Takeoff.class);
            }

            if (!uav.hasObjective()) {

                Cycle cycle = getCycleByUAV(uav);

                if (cycle == null) {
                    return;
                }

                for (SubCell subCell : cycle.getList()) {
                    Location location = subCell.getLocation();
                    uav.assignObjective(new FlyTo(location, "checkpoint"));
                    Destinations.addDestination(new Destination(uav, location));
                }
            }
        }
    }

    /**
     * Get cycle for particular UAV.
     *
     * @param uav UAV.
     * @return Cycle.
     */
    private Cycle getCycleByUAV(UAV uav) {
        if (Areas.noAreas()) {
            return null;
        }

        if (cycles == null) {
            cycles = new Cycles();
        }

        for (Cycle cycle : cycles.getList()) {
            if (cycle.getUAV() == uav) {
                return cycle;
            }
        }

        // Create cycle if not exits
        createCycle(uav);

        return getCycleByUAV(uav);
    }

    /**
     * Creates cycle for particular UAV.
     *
     * @param uav UAV.
     * @return Cycle.
     */
    public Cycle createCycle(UAV uav) {
        Cycle cycle = new Cycle();

        SubCell startCell = getStartSubCell(uav);

        int cellIndex = mainCycle.getCellIndex(startCell);

        for (int i = cellIndex; i < mainCycle.size(); i++) {
            cycle.add(mainCycle.get(i));
        }

        for (int i = 0; i < cellIndex; i++) {
            cycle.add(mainCycle.get(i));
        }

        cycle.setUAV(uav);
        cycles.add(cycle);

        return cycle;
    }

    /**
     * Gets the nearest sub-cell to given UAV.
     * 
     * @param uav UAV
     * @return Sub-cell.
     */
    private SubCell getStartSubCell(UAV uav) {
        SubCell nearestSubCell = null;
        double minimalDistance = Double.MAX_VALUE;

        for (Cell cell : grid.getCells()) {
            for (SubCell subCell : cell.getSubCells()) {
                if (!Areas.intersect(subCell)) {
                    continue;
                }

                double distance = Location.getDistance2D(uav.getActualLocationMapCoordinates(), subCell.getLocation());

                if (distance < minimalDistance) {
                    minimalDistance = distance;
                    nearestSubCell = subCell;
                }
            }
        }

        if (nearestSubCell == null) {
            throw new IllegalStateException("Nearest sub cell not found.");
        }

        return nearestSubCell;
    }

    @Override
    public void paint(Graphics2D graphics, BufferedImage mapImage, double scale) {
        super.paint(graphics, mapImage, scale);

        drawGrid();
        drawSpanningTree();
        drawCycles();
    }

    private void drawGrid() {
        if (grid == null) {
            return;
        }

        setAlpha(1F);

        for (Cell cell : grid.getCells()) {
            Location birdviewLocation = BirdviewMapConversion.mapToBirdview(cell.getTopLeftCornerLocation(), mapImage, scale);
            Location birdviewDimensions = BirdviewMapConversion.mapToBirdviewLength(new Location(cell.getHeight(), cell.getWidth(), 0), mapImage, scale);

            graphics.setColor(Preferences.GRID_CELL_PERIMETER_COLOR);
            graphics.drawRect(NumberConversion.toInteger(birdviewLocation.getX()), NumberConversion.toInteger(birdviewLocation.getY()), NumberConversion.toInteger(birdviewDimensions.getX()), NumberConversion.toInteger(birdviewDimensions.getY()));
        }
    }

    private void drawSpanningTree() {
        if (spanningTree == null) {
            return;
        }

        for (SpanningTreeVertex vertex : spanningTree.getVertices()) {
            drawVertex(vertex);
        }

        if (spanningTree == null) {
            return;
        }

        for (SpanningTreeEdge edge : spanningTree.getEdges()) {
            drawEdge(edge);
        }
    }

    private void drawVertex(SpanningTreeVertex vertex) {
        Location locationMapCoordinates = vertex.getCell().getCenterLocation();
        Location locationBirdviewCoordinates = BirdviewMapConversion.mapToBirdview(locationMapCoordinates, mapImage, scale);

        setAlpha(1);
        graphics.setColor(Preferences.SPANNING_TREE_VERTEX_COLOR);
        graphics.fillOval(NumberConversion.toInteger(locationBirdviewCoordinates.getX() - Preferences.SPANNING_TREE_VERTEX_DIAMETER / 2), NumberConversion.toInteger(locationBirdviewCoordinates.getY() - Preferences.SPANNING_TREE_VERTEX_DIAMETER / 2), Preferences.SPANNING_TREE_VERTEX_DIAMETER, Preferences.SPANNING_TREE_VERTEX_DIAMETER);
    }

    private void drawEdge(SpanningTreeEdge edge) {
        Location startLocationMapCoordinates = edge.getStartLocation();
        Location startLocationBirdviewCoordinates = BirdviewMapConversion.mapToBirdview(startLocationMapCoordinates, mapImage, scale);

        Location endLocationMapCoordinates = edge.getEndLocation();
        Location endLocationBirdviewCoordinates = BirdviewMapConversion.mapToBirdview(endLocationMapCoordinates, mapImage, scale);

        setAlpha(1);
        graphics.setColor(Preferences.EDGE_COLOR);
        graphics.drawLine(NumberConversion.toInteger(startLocationBirdviewCoordinates.getX()), NumberConversion.toInteger(startLocationBirdviewCoordinates.getY()), NumberConversion.toInteger(endLocationBirdviewCoordinates.getX()), NumberConversion.toInteger(endLocationBirdviewCoordinates.getY()));
    }

    private void drawCycles() {
        if (cycles == null) {
            return;
        }

        for (Cycle cycle : cycles.getList()) {
            if (ActiveObject.get() instanceof UAV && ActiveObject.get() == cycle.getUAV()) {
                drawCycle(cycle);
            }
        }
    }

    private void drawCycle(Cycle cycle) {
        if (cycle == null) {
            return;
        }

        int number = 0;

        for (SubCell subCell : cycle.getList()) {
            drawSubCell(subCell, number);
            number++;
        }
    }

    private void drawSubCell(SubCell subCell, int number) {
        Location subCellMapLocation = subCell.getLocation();
        Location subCellBirdviewLocation = BirdviewMapConversion.mapToBirdview(subCellMapLocation, mapImage, scale);

        graphics.setColor(Preferences.GRID_CELL_CENTER_COLOR);
        graphics.fillOval(NumberConversion.toInteger(subCellBirdviewLocation.getX() - Preferences.GRID_CELL_CENTER_DIAMETER / 2), NumberConversion.toInteger(subCellBirdviewLocation.getY() - Preferences.GRID_CELL_CENTER_DIAMETER / 2), Preferences.GRID_CELL_CENTER_DIAMETER, Preferences.GRID_CELL_CENTER_DIAMETER);
        graphics.drawString(Integer.toString(number), NumberConversion.toInteger(subCellBirdviewLocation.getX() - Preferences.GRID_CELL_CENTER_DIAMETER / 2), NumberConversion.toInteger(subCellBirdviewLocation.getY() - Preferences.GRID_CELL_CENTER_DIAMETER / 2 - Preferences.GRID_CELL_CENTER_DIAMETER));
    }

    @Override
    public String getName() {
        return Algorithms.SPANNING_TREE_COVERAGE.toString() + " - " + AreaDivision.SINGLE.toString() + " - " + variability.toString();
    }

    @Override
    public void reportUAVAddition(UAV uav) {
        reset();
    }

    @Override
    public void reportUAVDeletion(UAV uav) {
        reset();
    }

    @Override
    public void reportAreaAddition(Area area) {
        reset();
    }

    @Override
    public void reportAreaDeletion(Area area) {
        reset();
    }

    @Override
    public void reportChargingCompleted() {
        if (this.variability == Variability.DYNAMIC) {
            reset();
        }
    }

    private void reset() {
        initialize();

        for (UAV uav : UAVs.listOfUAVs) {
            uav.cancelObjectives();
        }
    }
}
