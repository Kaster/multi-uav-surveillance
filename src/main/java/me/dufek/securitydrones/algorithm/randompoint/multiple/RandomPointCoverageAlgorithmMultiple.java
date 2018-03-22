package me.dufek.securitydrones.algorithm.randompoint.multiple;

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
import me.dufek.securitydrones.algorithm.spanningtree.SpanningTree;
import me.dufek.securitydrones.algorithm.spanningtree.SpanningTrees;
import me.dufek.securitydrones.algorithm.systematic.Cycles;
import me.dufek.securitydrones.area.Area;
import me.dufek.securitydrones.area.Areas;
import me.dufek.securitydrones.conversion.BirdviewMapConversion;
import me.dufek.securitydrones.conversion.NumberConversion;
import me.dufek.securitydrones.gui.Window;
import me.dufek.securitydrones.logger.Logger;
import me.dufek.securitydrones.uav.destination.Destination;
import me.dufek.securitydrones.uav.destination.Destinations;
import me.dufek.securitydrones.uav.UAV;
import me.dufek.securitydrones.uav.UAVs;
import me.dufek.securitydrones.uav.objective.flyto.FlyTo;
import me.dufek.securitydrones.uav.objective.Takeoff;
import me.dufek.securitydrones.uav.request.UAVRequest;
import me.dufek.securitydrones.uav.request.UAVRequests;
import me.dufek.securitydrones.uav.status.UAVsStatus;
import me.dufek.securitydrones.utilities.VisibleArea;

/**
 * Random point coverage algorithm multiple version. It divide area between UAV.
 * Each UAV is then surveying only its assigned sub-area. Surveillance is done
 * by selecting random points on the ares's perimeter.
 *
 * @author Jan Dufek
 */
public class RandomPointCoverageAlgorithmMultiple extends Algorithm {

    /**
     * Grid.
     */
    private Grid grid;

    /**
     * Spanning trees for individual UAVs.
     */
    private SpanningTrees spanningTrees;

    /**
     * Grids for individual UAVs.
     */
    private ArrayList<Grid> grids;

    /**
     * Scopes of individual UAVs.
     */
    private ArrayList<Scope> scopes;

    public RandomPointCoverageAlgorithmMultiple(Window mainWindow, Variability variability) {
        super(mainWindow, variability);
    }

    /**
     * Initialize before logic.
     */
    @Override
    public void beforeLogic() {
        super.beforeLogic();

        initialize();
    }

    /**
     * Initialize algorithm.
     */
    public void initialize() {

        grids = new ArrayList<Grid>();
        scopes = new ArrayList<Scope>();

        if (Areas.noAreas()) {
            grid = null;
            spanningTrees = null;
            return;
        }

        if (UAVs.noUAVs() && UAVRequests.noRequest()) {
            grid = null;
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
     * Main grid creation.
     */
    private void createMainGrid() {
//        double minimumVisibleAreaWidth = VisibleArea.getMinimumVisibleAreaWidth();
//
//        grid = new Grid(2 * minimumVisibleAreaWidth);
        grid = new Grid(20);
    }

    /**
     * Creation of spanning trees.
     */
    private void createSpanningTrees() {
        spanningTrees = new SpanningTrees(grid);
    }

    /**
     * Initialize UAV request. Spanning tree and grid is created.
     *
     * @param uavRequest UAV request.
     */
    private void inicializeUAVRequest(UAVRequest uavRequest) {
        SpanningTree spanningTree = uavRequest.getSpanningTree();
        Grid uavGrid = spanningTree.getGrid();
        uavGrid.setUAVRequest(uavRequest);
        grids.add(uavGrid);

        Scope scope = new Scope(uavRequest);

        ArrayList<Area> areas = uavGrid.getAreas();

        scope.setAreas(areas);

        scopes.add(scope);
    }

    /**
     * Initialize UAV. Spanning tree and grid is created.
     *
     * @param uav UAV.
     */
    private void initializeUAV(UAV uav) {
        SpanningTree spanningTree = uav.getUAVRequest().getSpanningTree();
        Grid uavGrid = spanningTree.getGrid();
        uavGrid.setUAVRequest(uav.getUAVRequest());
        grids.add(uavGrid);

        Scope scope = new Scope(uav.getUAVRequest());

        ArrayList<Area> areas = uavGrid.getAreas();

        scope.setAreas(areas);

        scopes.add(scope);
    }

    /**
     * Logic is used to assign targets to UAVs.
     */
    @Override
    public void logic() {
        super.logic();

        for (UAV uav : UAVs.listOfUAVs) {

            if (!uav.isInAir() && !uav.isTakeoff()) {
                assignObjective(uav, Takeoff.class);
            }

            if (!uav.hasObjective()) {

                if (Areas.noAreas()) {
                    return;
                }

                Scope scope = getScope(uav);

                if (scope == null) {
                    return;
                }

                Area area = Areas.selectArea(scope.getAreas());

                Location randomPointOnAreaPerimeter = area.getRandomPointOnPerimeter();
                uav.assignObjective(new FlyTo(randomPointOnAreaPerimeter, "checkpoint"));
                Destinations.addDestination(new Destination(uav, randomPointOnAreaPerimeter));
            }
        }
    }

    /**
     * Get scope for particular UAV.
     *
     * @param uav UAV.
     * @return Scope.
     */
    private Scope getScope(UAV uav) {
        if (Areas.noAreas()) {
            return null;
        }

        for (Scope scope : scopes) {
            if (scope.getUAVRequest() == uav.getUAVRequest()) {
                scope.setUAV(uav);
                return scope;
            }
        }

        throw new IllegalStateException("Scope not found.");
    }
    
    /**
     * Get scope for particular UAV request.
     *
     * @param uavRequest UAV request.
     * @return Scope.
     */
    private Scope getScope(UAVRequest uavRequest) {
        for (Scope scope : scopes) {
            if (scope.getUAVRequest() == uavRequest) {
                return scope;
            }
        }

        throw new IllegalStateException("Scope not found.");
    }

    @Override
    public void paint(Graphics2D graphics, BufferedImage mapImage, double scale) {
        super.paint(graphics, mapImage, scale);

        drawGrids();
        drawScopes();
    }

    private void drawGrids() {
        if (grids == null) {
            return;
        }

        for (Grid uavGrid : grids) {
            if (ActiveObject.get() instanceof UAV && ((UAV) ActiveObject.get()).getUAVRequest() == uavGrid.getUAVRequest()) {
                drawGrid(uavGrid);
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

    private void drawScopes() {
        if (scopes == null) {
            return;
        }

        for (Scope scope : scopes) {
            if (ActiveObject.get() instanceof UAV && ((UAV) ActiveObject.get()).getUAVRequest() == scope.getUAVRequest()) {
                drawScope(scope);
            }
        }
    }

    private void drawScope(Scope scope) {
        for (Area area : scope.getAreas()) {
            drawArea(area);
        }
    }

    private void drawArea(Area area) {
        Area areaBirdview = BirdviewMapConversion.mapToBirdview(area, mapImage, scale);

        setAlpha(1);
        graphics.setColor(Preferences.EDGE_COLOR);
        graphics.draw(areaBirdview);
    }

    @Override
    public String getName() {
        return Algorithms.RANDOM_POINT_COVERAGE.toString() + " - " + AreaDivision.MULTIPLE.toString() + " - " + variability.toString();
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
