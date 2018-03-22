package me.dufek.securitydrones.algorithm.heatgradient;

import cz.cuni.amis.pogamut.base3d.worldview.object.Location;
import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import me.dufek.securitydrones.activeobject.ActiveObject;
import me.dufek.securitydrones.algorithm.Algorithm;
import me.dufek.securitydrones.algorithm.Algorithms;
import me.dufek.securitydrones.algorithm.AreaDivision;
import me.dufek.securitydrones.algorithm.Variability;
import me.dufek.securitydrones.algorithm.grid.Cell;
import me.dufek.securitydrones.algorithm.grid.Grid;
import me.dufek.securitydrones.algorithm.grid.Neighbour;
import me.dufek.securitydrones.algorithm.grid.Preferences;
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
 *
 * @author Jan Dufek
 */
public class HeatGradientCoverageAlgorithmSingle extends Algorithm {

    /**
     * Grids of particular UAVs.
     */
    private ArrayList<Grid> grids;

    public HeatGradientCoverageAlgorithmSingle(Window mainWindow, Variability variability) {
        super(mainWindow, variability);
    }

    /**
     * Initialization before the logic method.
     */
    @Override
    public void beforeLogic() {
        super.beforeLogic();

        initialize();
    }

    /**
     * Initialize UAV requests and UAVs.
     */
    private void initialize() {
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
     * Initialize UAV request. It will create a grid.
     *
     * @param uavRequest UAV request.
     */
    private void inicializeUAVRequest(UAVRequest uavRequest) {
        Grid grid = createGrid(uavRequest);
        grid.setUAVRequest(uavRequest);
        grids.add(grid);
    }

    /**
     * Initialize UAV. It will create a grid.
     *
     * @param uavRequest UAV.
     */
    private void initializeUAV(UAV uav) {
        Grid grid = createGrid(uav.getUAVRequest());
        grid.setUAVRequest(uav.getUAVRequest());
        grids.add(grid);
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
     * Get start cell on the grid from location and the grid. The start cell is
     * the nearest one.
     *
     * @param location Location.
     * @param grid Grid.
     * @return Start cell.
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
     * Assign the next target to each UAV.
     */
    @Override
    public void logic() {
        super.logic();

        for (UAV uav : UAVs.listOfUAVs) {

            if (!uav.isInAir() && !uav.isTakeoff()) {
                assignObjective(uav, Takeoff.class);
            }

            if (!uav.hasObjective()) {
                Cell nextCell = getNextCell(uav);

                if (nextCell == null) {
                    return;
                }

                Location location = nextCell.getLocation();
                uav.assignObjective(new FlyTo(location, "checkpoint"));
                Destinations.addDestination(new Destination(uav, location));
            }
        }
    }

    /**
     * Get next cell for UAV.
     *
     * @param uav UAV
     * @return Next cell.
     */
    private Cell getNextCell(UAV uav) {
        if (Areas.noAreas()) {
            return null;
        }

        Location uavLocation = getLocation(uav);
        Grid uavGrid = getGrid(uav);
        Cell uavCell = getStartCell(uavLocation, uavGrid);

        double highestHeat = -Double.MAX_VALUE;

        for (Neighbour neighbour : uavCell.getNeighbours().getList()) {
            if (neighbour.getCell() == null) {
                continue;
            }

            double heat = neighbour.getHeat();

            if (highestHeat < heat) {
                highestHeat = heat;
            }
        }

        ArrayList<Neighbour> highestHeatNeighbours = new ArrayList<Neighbour>();

        for (Neighbour neighbour : uavCell.getNeighbours().getList()) {
            if (neighbour.getCell() == null) {
                continue;
            }

            double heat = neighbour.getHeat();

            if (highestHeat == heat) {
                highestHeatNeighbours.add(neighbour);
            }
        }

        Collections.shuffle(highestHeatNeighbours);

        Neighbour highestHeatNeighbour = highestHeatNeighbours.get(0);

        Cell nextCell = highestHeatNeighbour.getCell();

        return nextCell;
    }

    private Location getLocation(UAV uav) {
        Location location = uav.getActualLocationMapCoordinates();

        if (location == null) {
            location = uav.getUAVRequest().getStartLocation();
        }

        return location;
    }

    private Grid getGrid(UAV uav) {
        for (Grid grid : grids) {
            if (grid.getUAVRequest() == uav.getUAVRequest()) {
                return grid;
            }
        }

        throw new IllegalStateException("Grid not found.");
    }

    @Override
    public void paint(Graphics2D graphics, BufferedImage mapImage, double scale) {
        super.paint(graphics, mapImage, scale);

        drawGrids();
    }

    private void drawGrids() {
        if (grids == null) {
            return;
        }

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
            graphics.drawString(Integer.toString(NumberConversion.toInteger(cell.getHeat())), NumberConversion.toInteger(birdviewLocation.getX()) + NumberConversion.toInteger(birdviewDimensions.getX()) / 2, NumberConversion.toInteger(birdviewLocation.getY()) + NumberConversion.toInteger(birdviewDimensions.getY()) / 2);
        }
    }

    @Override
    public String getName() {
        return Algorithms.HEAT_GRADIENT_COVERAGE.toString() + " - " + AreaDivision.SINGLE.toString() + " - " + variability.toString();
    }

    @Override
    public void reportUAVAddition(UAV uav) {
        reset();
    }

    @Override
    public void reportUAVDeletion(UAV uav) {
        deleteGridByUAV(uav);
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
        initialize();

        for (UAV uav : UAVs.listOfUAVs) {
            uav.cancelObjectives();
        }
    }
}
