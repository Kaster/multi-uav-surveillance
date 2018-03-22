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
import me.dufek.securitydrones.algorithm.grid.Preferences;
import me.dufek.securitydrones.algorithm.spanningtree.SpanningTree;
import me.dufek.securitydrones.algorithm.spanningtree.SpanningTrees;
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
import me.dufek.securitydrones.utilities.VisibleArea;

/**
 * Heat gradient coverage algorithm multiple version. It works by dividing the
 * area between UAVs and then each UAV travels to spots with higher heat.
 *
 * @author Jan Dufek
 */
public class HeatGradientCoverageAlgorithmMultiple extends Algorithm {

    /**
     * The main grid.
     */
    private Grid mainGrid;

    /**
     * Spanning trees of particular UAVs.
     */
    private SpanningTrees spanningTrees;

    /**
     * Grids of particular UAVs.
     */
    private ArrayList<Grid> grids;

    public HeatGradientCoverageAlgorithmMultiple(Window mainWindow, Variability variability) {
        super(mainWindow, variability);
    }

    /**
     * Initialize algorithms. It will initialize UAVs and UAV requests.
     */
    private void initialize() {
        grids = new ArrayList<Grid>();

        if (Areas.noAreas()) {
            mainGrid = null;
            spanningTrees = null;
            return;
        }

        if (UAVs.noUAVs() && UAVRequests.noRequest()) {
            mainGrid = null;
            spanningTrees = null;
            return;
        }

        createMainGrid();
        createSpanningTrees();

        for (UAVRequest uavRequest : UAVRequests.getList()) {
            inicializeUAVRequest(uavRequest);
        }

        for (UAV uav : UAVs.listOfUAVs) {
            initializeUAV(uav);
        }
    }

    /**
     * Used to create the main grid.
     */
    private void createMainGrid() {
        double minimumVisibleAreaWidth = VisibleArea.getMinimumVisibleAreaWidth();

        mainGrid = new Grid(2 * minimumVisibleAreaWidth);
    }

    /**
     * Used to create spanning trees.
     */
    private void createSpanningTrees() {
        spanningTrees = new SpanningTrees(mainGrid);
    }

    /**
     * Initialize UAV request. It will create both grid and spanning tree.
     *
     * @param uavRequest UAV request.
     */
    private void inicializeUAVRequest(UAVRequest uavRequest) {
        SpanningTree spanningTree = uavRequest.getSpanningTree();
        Grid uavGrid = spanningTree.getGrid();
        uavGrid.setUAVRequest(uavRequest);
        grids.add(uavGrid);
    }

    /**
     * Initialize UAV. It will create both grid and spanning tree.
     *
     * @param uavRequest UAV.
     */
    private void initializeUAV(UAV uav) {
        SpanningTree spanningTree = uav.getUAVRequest().getSpanningTree();
        Grid uavGrid = spanningTree.getGrid();
        uavGrid.setUAVRequest(uav.getUAVRequest());
        grids.add(uavGrid);
    }

    /**
     * Logic is used to assign the next location to UAVs.
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
     * Get the next cell for particular UAV.
     * 
     * @param uav UAV
     * @return Cell.
     */
    private Cell getNextCell(UAV uav) {
        if (Areas.noAreas()) {
            return null;
        }

        Location uavLocation = getLocation(uav);
        Grid uavGrid = uav.getUAVRequest().getSpanningTree().getGrid();
        Cell uavCell = uavGrid.getStartCell(uavLocation);

        double highestHeat = -Double.MAX_VALUE;

        // Top
        Cell topNeighbour = uavGrid.getCell(uavCell.getI() - 1, uavCell.getJ());

        if (topNeighbour != null) {

            double heat = topNeighbour.getHeat();

            if (highestHeat < heat) {
                highestHeat = heat;
            }
        }

        // Right
        Cell rightNeighbour = uavGrid.getCell(uavCell.getI(), uavCell.getJ() + 1);

        if (rightNeighbour != null) {

            double heat = rightNeighbour.getHeat();

            if (highestHeat < heat) {
                highestHeat = heat;
            }
        }

        // Left
        Cell leftNeighbour = uavGrid.getCell(uavCell.getI(), uavCell.getJ() - 1);

        if (leftNeighbour != null) {

            double heat = leftNeighbour.getHeat();

            if (highestHeat < heat) {
                highestHeat = heat;
            }
        }

        // Bottom
        Cell bottomNeigbbour = uavGrid.getCell(uavCell.getI() + 1, uavCell.getJ());

        if (bottomNeigbbour != null) {

            double heat = bottomNeigbbour.getHeat();

            if (highestHeat < heat) {
                highestHeat = heat;
            }
        }

        ArrayList<Cell> highestHeatNeighbours = new ArrayList<Cell>();

        // Top
        if (topNeighbour != null) {

            double heat = topNeighbour.getHeat();

            if (highestHeat == heat) {
                highestHeatNeighbours.add(topNeighbour);
            }
        }

        // Right
        if (rightNeighbour != null) {

            double heat = rightNeighbour.getHeat();

            if (highestHeat == heat) {
                highestHeatNeighbours.add(rightNeighbour);
            }
        }

        // Left
        if (leftNeighbour != null) {

            double heat = leftNeighbour.getHeat();

            if (highestHeat == heat) {
                highestHeatNeighbours.add(leftNeighbour);
            }
        }

        // Bottom
        if (bottomNeigbbour != null) {

            double heat = bottomNeigbbour.getHeat();

            if (highestHeat == heat) {
                highestHeatNeighbours.add(bottomNeigbbour);
            }
        }

        Collections.shuffle(highestHeatNeighbours);

        Cell highestHeatNeighbour = highestHeatNeighbours.get(0);

        Cell nextCell = highestHeatNeighbour;

        return nextCell;
    }

    private Location getLocation(UAV uav) {
        Location location = uav.getActualLocationMapCoordinates();

        if (location == null) {
            location = uav.getUAVRequest().getStartLocation();
        }

        return location;
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
        return Algorithms.HEAT_GRADIENT_COVERAGE.toString() + " - " + AreaDivision.MULTIPLE.toString() + " - " + variability.toString();
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
