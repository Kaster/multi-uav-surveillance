package me.dufek.securitydrones.algorithm.spanningtreecoverage.multiple;

import me.dufek.securitydrones.algorithm.spanningtreecoverage.cycle.Cycle;
import me.dufek.securitydrones.algorithm.spanningtreecoverage.cycle.Cycles;
import cz.cuni.amis.pogamut.base3d.worldview.object.Location;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
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
 * Spanning tree coverage algorithm multiple version. It uses spanning trees to
 * create a cyclic non-overlapping paths.
 *
 * @author Jan Dufek
 */
public class SpanningTreeCoverageAlgorithmMultiple extends Algorithm {

    /**
     * Grid.
     */
    private Grid grid;

    /**
     * Spanning trees for individual UAVs.
     */
    private SpanningTrees spanningTrees;

    /**
     * Cycles of individual UAVs.
     */
    private Cycles cycles;

    public SpanningTreeCoverageAlgorithmMultiple(Window mainWindow, Variability variability) {
        super(mainWindow, variability);
    }

    /**
     * Executed once before logic.
     */
    @Override
    public void beforeLogic() {
        super.beforeLogic();
    }

    /**
     * Initialize algorithm.
     */
    private void initialize() {
        if (Areas.noAreas()) {
            grid = null;
            spanningTrees = null;
            cycles = null;
            return;
        }

        if (UAVs.noUAVs() && UAVRequests.noRequest()) {
            grid = null;
            spanningTrees = null;
            cycles = null;
            return;
        }

        createGrids();

        createSpanningTrees();

        this.cycles = new Cycles();

        for (SpanningTree spanningTree : spanningTrees.getList()) {
            Cycle cycle = createCycle(spanningTree);
            cycles.add(cycle);
        }
    }

    /**
     * Creates cycle from spanning tree by circumnavigating it.
     *
     * @param spanningTree Spanning tree.
     * @return Cycle.
     */
    public Cycle createCycle(SpanningTree spanningTree) {
        Cycle cycle = new Cycle();

        SubCell startSubCell = spanningTree.getStartSubCell();

        if (Areas.intersect(startSubCell)) {
            cycle.add(startSubCell);
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
     * Create grid for each UAV.
     */
    private void createGrids() {
        double minimumVisibleAreaWidth = VisibleArea.getMinimumVisibleAreaWidth();

        grid = new Grid(2 * minimumVisibleAreaWidth);
    }
    
    /**
     * Create spanning tree for each UAV.
     */
    private void createSpanningTrees() {
        spanningTrees = new SpanningTrees(grid);
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

        return uav.getUAVRequest().getSpanningTree().getCycle();
    }

    @Override
    public void paint(Graphics2D graphics, BufferedImage mapImage, double scale) {
        super.paint(graphics, mapImage, scale);

        drawGrid();
        drawSpanningTrees();
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

    private void drawSpanningTrees() {
        if (spanningTrees == null) {
            return;
        }

        for (SpanningTree spanningTree : spanningTrees.getList()) {
            drawSpanningTree(spanningTree);
        }
    }

    private void drawSpanningTree(SpanningTree spanningTree) {
        for (SpanningTreeVertex vertex : spanningTree.getVertices()) {
            drawVertex(vertex);
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
        graphics.fillOval(NumberConversion.toInteger(locationBirdviewCoordinates.getX() - 10 / 2), NumberConversion.toInteger(locationBirdviewCoordinates.getY() - 10 / 2), 10, 10);
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
            drawCycle(cycle);
        }
    }

    private void drawCycle(Cycle cycle) {
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
        return Algorithms.SPANNING_TREE_COVERAGE.toString() + " - " + AreaDivision.MULTIPLE.toString() + " - " + variability.toString();
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
