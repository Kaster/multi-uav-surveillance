package me.dufek.securitydrones.algorithm.systematic.single;

import cz.cuni.amis.pogamut.base3d.worldview.object.Location;
import java.awt.AlphaComposite;
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
import me.dufek.securitydrones.algorithm.grid.Transition;
import me.dufek.securitydrones.algorithm.systematic.Cycle;
import me.dufek.securitydrones.algorithm.systematic.Cycles;
import me.dufek.securitydrones.algorithm.systematic.Type;
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
import me.dufek.securitydrones.uav.request.UAVRequest;
import me.dufek.securitydrones.uav.request.UAVRequests;

/**
 * Systematic coverage algorithm single version. It creates grid over given
 * area. Then all the UAVs survey the same area using systematic paths. Those
 * paths are selected according to the type used.
 *
 * @author Jan Dufek
 */
public class SystematicCoverageAlgorithmSingle extends Algorithm {

    /**
     * Type of the systematic algorithm.
     */
    private final Type type;

    /**
     * Grids for individual UAVs.
     */
    private ArrayList<Grid> grids;

    /**
     * Cycles for individual UAVs.
     */
    private Cycles cycles;

    public SystematicCoverageAlgorithmSingle(Window mainWindow, Type type, Variability variability) {
        super(mainWindow, variability);

        this.type = type;
    }

    /**
     * Algorithm is initialized before logic.
     */
    @Override
    public void beforeLogic() {
        super.beforeLogic();

        initializeCycles();
    }

    /**
     * Initialize cycles.
     */
    private void initializeCycles() {
        cycles = new Cycles();
        grids = new ArrayList<Grid>();

        if (Areas.noAreas()) {
            return;
        }

        for (UAVRequest uavRequest : UAVRequests.getList()) {
            inicializeUAVRequest(uavRequest);
        }

        for (UAV uav : UAVs.listOfUAVs) {
            initializeUAV(uav);
        }
    }

    /**
     * Initialize UAV requests. Grid is created.
     *
     * @param uavRequest UAV request.
     */
    private void inicializeUAVRequest(UAVRequest uavRequest) {
        Grid grid = createGrid(uavRequest);
        grid.setUAVRequest(uavRequest);
        grids.add(grid);

        Cycle mainCycle = createMainCycle(grid);

        Location location = uavRequest.getStartLocation();
        Cycle cycle = createCycle(mainCycle, grid, location);

        cycle.setUAVRequest(uavRequest);

        cycles.add(cycle);
    }

    /**
     * Initialize UAV. Grid is created.
     *
     * @param uav UAV.
     */
    private void initializeUAV(UAV uav) {
        Grid grid = createGrid(uav.getUAVRequest());
        grid.setUAVRequest(uav.getUAVRequest());
        grids.add(grid);

        Cycle mainCycle = createMainCycle(grid);

        Location location = uav.getLocationMapCoordinates();

        if (location == null) {
            location = uav.getUAVRequest().getStartLocation();
        }

        Cycle cycle = createCycle(mainCycle, grid, location);

        cycle.setUAVRequest(uav.getUAVRequest());
        cycles.add(cycle);
    }

    /**
     * Create grid for UAV request.
     *
     * @param uavRequest UAV request.
     * @return Grid.
     */
    private Grid createGrid(UAVRequest uavRequest) {
        if (!uavRequest.isFlightLevelSet()) {
            uavRequest.setFlightLevel();
        }

        double visibleAreaWidth = uavRequest.getVisibleAreaWidth();

        return new Grid(visibleAreaWidth);
    }

    /**
     * Creates main cycle for the grid.
     *
     * @param grid Grid.
     * @return Main cycle.
     */
    public Cycle createMainCycle(Grid grid) {
        Cycle cycle;

        switch (type) {
            case HORIZONTAL:
                cycle = createMainCycleHorizontal(grid);
                break;
            case VERTICAL:
                cycle = createMainCycleVertical(grid);
                break;
            case SPIRAL:
                cycle = createMainCycleSpiral(grid);
                break;
            default:
                throw new IllegalStateException("Unknown type (" + type + ") of systematic algorithm.");
        }

        return cycle;
    }

    /**
     * Create cycle around grid using horizontal method.
     *
     * @param grid Grid
     * @return Cycle.
     */
    public Cycle createMainCycleHorizontal(Grid grid) {
        Cycle cycle = new Cycle();

        Transition transition = Transition.RIGHT;

        for (int row = 0; row < grid.getNumberOfVerticalCells(); row++) {

            if (transition == Transition.RIGHT) {
                for (int column = 0; column < grid.getNumberOfHorizontalCells(); column++) {
                    Cell cell = grid.getCell(row, column);

                    if (cell != null) {
                        cycle.add(cell);
                    }
                }

                transition = Transition.LEFT;
            } else {
                for (int column = grid.getNumberOfHorizontalCells() - 1; column >= 0; column--) {
                    Cell cell = grid.getCell(row, column);

                    if (cell != null) {
                        cycle.add(cell);
                    }
                }

                transition = Transition.RIGHT;
            }
        }

        return cycle;
    }

    /**
     * Create cycle around grid using vertical method.
     *
     * @param grid Grid
     * @return Cycle.
     */
    public Cycle createMainCycleVertical(Grid grid) {
        Cycle cycle = new Cycle();

        Transition transition = Transition.DOWN;

        for (int column = 0; column < grid.getNumberOfHorizontalCells(); column++) {

            if (transition == Transition.DOWN) {
                for (int row = 0; row < grid.getNumberOfVerticalCells(); row++) {
                    Cell cell = grid.getCell(row, column);

                    if (cell != null) {
                        cycle.add(cell);
                    }
                }

                transition = Transition.UP;
            } else {
                for (int row = grid.getNumberOfVerticalCells() - 1; row >= 0; row--) {
                    Cell cell = grid.getCell(row, column);

                    if (cell != null) {
                        cycle.add(cell);
                    }
                }

                transition = Transition.DOWN;
            }
        }

        return cycle;
    }

    /**
     * Create cycle around grid using spiral method.
     *
     * @param grid Grid
     * @return Cycle.
     */
    public Cycle createMainCycleSpiral(Grid grid) {
        Cycle cycle = new Cycle();

        int numberOfHorizontalCells = grid.getNumberOfHorizontalCells();
        int numberOfVerticalCells = grid.getNumberOfVerticalCells();

        createSpiralCycle(grid, numberOfHorizontalCells, numberOfVerticalCells, cycle);

        return cycle;
    }

    /**
     * Creates spiral from a grid.
     *
     * @param grid Grid.
     * @param numberOfColumns Number of columns.
     * @param numberOfRows Number of rows.
     * @param cycle Cycle.
     */
    private static void createSpiralCycle(Grid grid, int numberOfColumns, int numberOfRows, Cycle cycle) {
        createTopRight(grid, 0, 0, numberOfColumns - 1, numberOfRows - 1, cycle);
    }

    /**
     * Creates top right path.
     *
     * @param grid Grid.
     * @param leftBound Left bound of indices.
     * @param topBound Top bound of indices.
     * @param rightBound Right bound of indices.
     * @param bottomBound Bottom bound of indices.
     * @param cycle Cycle.
     */
    private static void createTopRight(Grid grid, int leftBound, int topBound, int rightBound, int bottomBound, Cycle cycle) {

        // Add row
        for (int i = leftBound; i <= rightBound; i++) {
            Cell cell = grid.getCell(topBound, i);

            if (cell != null) {
                cycle.add(cell);
            }
        }

        // Add column
        for (int j = topBound + 1; j <= bottomBound; j++) {
            Cell cell = grid.getCell(j, rightBound);

            if (cell != null) {
                cycle.add(cell);
            }
        }

        // Check if more is needed
        if (rightBound - leftBound > 0 && bottomBound - topBound > 0) {
            createBottomLeft(grid, leftBound, topBound + 1, rightBound - 1, bottomBound, cycle);
        }
    }

    /**
     * Creates bottom left path.
     *
     * @param grid Grid.
     * @param leftBound Left bound of indices.
     * @param topBound Top bound of indices.
     * @param rightBound Right bound of indices.
     * @param bottomBound Bottom bound of indices.
     * @param cycle Cycle.
     */
    private static void createBottomLeft(Grid grid, int leftBound, int topBound, int rightBound, int bottomBound, Cycle cycle) {

        // Add row in reverse order
        for (int i = rightBound; i >= leftBound; i--) {
            Cell cell = grid.getCell(bottomBound, i);

            if (cell != null) {
                cycle.add(cell);
            }
        }

        // Print column in reverse order
        for (int j = bottomBound - 1; j >= topBound; j--) {
            Cell cell = grid.getCell(j, leftBound);

            if (cell != null) {
                cycle.add(cell);
            }
        }

        // Check if more is needed
        if (rightBound - leftBound > 0 && bottomBound - topBound > 0) {
            createTopRight(grid, leftBound + 1, topBound, rightBound, bottomBound - 1, cycle);
        }
    }

    /**
     * Create cycle from main cycle and UAV location.
     *
     * @param mainCycle Main cycle.
     * @param grid Grid.
     * @param location Location.
     * @return Cycle.
     */
    public Cycle createCycle(Cycle mainCycle, Grid grid, Location location) {
        Cycle cycle = new Cycle();

        Cell startCell = getStartCell(location, grid);

        int cellIndex = mainCycle.getCellIndex(startCell);

        for (int i = cellIndex; i < mainCycle.size(); i++) {
            cycle.add(mainCycle.get(i));
        }

        for (int i = 0; i < cellIndex; i++) {
            cycle.add(mainCycle.get(i));
        }

        return cycle;
    }

    /**
     * Get start cell on the grid for given location.
     *
     * @param location Location.
     * @param grid Grid.
     * @return Cell.
     */
    private Cell getStartCell(Location location, Grid grid) {
        double minimalDistance = Double.MAX_VALUE;
        Cell startCell = null;

        for (Cell cell : grid.getCells()) {
            Location cellLocation = cell.getLocation();

            double distance = Location.getDistance2D(location, cellLocation);

            if (distance < minimalDistance) {
                minimalDistance = distance;
                startCell = cell;
            }
        }

        return startCell;
    }

    /**
     * Logic assign paths.
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

                for (Cell cell : cycle.getList()) {
                    Location location = cell.getLocation();
                    uav.assignObjective(new FlyTo(location, "checkpoint"));
                    Destinations.addDestination(new Destination(uav, location));
                }
            }
        }
    }

    /**
     * Gets cycle for given UAV.
     *
     * @param uav UAV
     * @return Cycle.
     */
    private Cycle getCycleByUAV(UAV uav) {
        if (Areas.noAreas()) {
            return null;
        }

        for (Cycle cycle : cycles.getList()) {
            if (cycle.getUAVRequest() == uav.getUAVRequest()) {
                return cycle;
            }
        }

        // Create cycle if not exits
        initializeUAV(uav);

        return getCycleByUAV(uav);
    }

    @Override
    public void paint(Graphics2D graphics, BufferedImage mapImage, double scale) {
        super.paint(graphics, mapImage, scale);

        drawGrids();
        drawCycles();
    }

    private void drawGrids() {
        for (Grid grid : grids) {
            if (ActiveObject.get() instanceof UAV && ((UAV) ActiveObject.get()).getUAVRequest() == grid.getUAVRequest()) {
                drawGrid(grid);
            }
        }
    }

    private void drawGrid(Grid grid) {
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

    private void drawCycles() {
        for (Cycle cycle : cycles.getList()) {
            if (ActiveObject.get() instanceof UAV && ((UAV) ActiveObject.get()).getUAVRequest() == cycle.getUAVRequest()) {
                drawCycle(cycle);
            }
        }
    }

    private void drawCycle(Cycle cycle) {
        if (cycle == null) {
            return;
        }

        int number = 0;

        for (Cell cell : cycle.getList()) {
            drawCell(cell, number);
            number++;
        }
    }

    private void drawCell(Cell cell, int number) {
        Location subCellMapLocation = cell.getLocation();
        Location subCellBirdviewLocation = BirdviewMapConversion.mapToBirdview(subCellMapLocation, mapImage, scale);

        graphics.setColor(Preferences.GRID_CELL_CENTER_COLOR);
        graphics.fillOval(NumberConversion.toInteger(subCellBirdviewLocation.getX() - Preferences.GRID_CELL_CENTER_DIAMETER / 2), NumberConversion.toInteger(subCellBirdviewLocation.getY() - Preferences.GRID_CELL_CENTER_DIAMETER / 2), Preferences.GRID_CELL_CENTER_DIAMETER, Preferences.GRID_CELL_CENTER_DIAMETER);
        graphics.drawString(Integer.toString(number), NumberConversion.toInteger(subCellBirdviewLocation.getX() - Preferences.GRID_CELL_CENTER_DIAMETER / 2), NumberConversion.toInteger(subCellBirdviewLocation.getY() - Preferences.GRID_CELL_CENTER_DIAMETER / 2 - Preferences.GRID_CELL_CENTER_DIAMETER));
    }

    @Override
    public String getName() {

        switch (type) {
            case HORIZONTAL:
                return Algorithms.SYSTEMATIC_COVERAGE_HORIZONTAL.toString() + " - " + AreaDivision.SINGLE.toString() + " - " + variability.toString();
            case VERTICAL:
                return Algorithms.SYSTEMATIC_COVERAGE_VERTICAL.toString() + " - " + AreaDivision.SINGLE.toString() + " - " + variability.toString();
            case SPIRAL:
                return Algorithms.SYSTEMATIC_COVERAGE_SPIRAL.toString() + " - " + AreaDivision.SINGLE.toString() + " - " + variability.toString();
        }

        throw new IllegalStateException("Name not found.");
    }

    @Override
    public void reportUAVDeletion(UAV uav) {
        deleteCycleByUAV(uav);
        deleteGridByUAV(uav);
    }

    private void deleteCycleByUAV(UAV uav) {
        Cycle cycleToDelete = null;

        for (Cycle cycle : cycles.getList()) {
            if (cycle.getUAVRequest() == uav.getUAVRequest()) {
                cycleToDelete = cycle;
            }
        }

        cycles.remove(cycleToDelete);
    }

    private void deleteGridByUAV(UAV uav) {
        Grid gridToRemove = null;

        for (Grid grid : grids) {
            if (grid.getUAVRequest() == uav.getUAVRequest()) {
                gridToRemove = grid;
            }
        }

        grids.remove(gridToRemove);
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
        initializeCycles();

        for (UAV uav : UAVs.listOfUAVs) {
            uav.cancelObjectives();
        }
    }
}
