package me.dufek.securitydrones.algorithm.perimeterfollowing.single;

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
import me.dufek.securitydrones.algorithm.grid.Preferences;
import me.dufek.securitydrones.algorithm.perimeterfollowing.Bridge;
import me.dufek.securitydrones.algorithm.perimeterfollowing.Cycle;
import me.dufek.securitydrones.algorithm.perimeterfollowing.Cycles;
import me.dufek.securitydrones.area.Area;
import me.dufek.securitydrones.area.Areas;
import me.dufek.securitydrones.conversion.BirdviewMapConversion;
import me.dufek.securitydrones.conversion.NumberConversion;
import me.dufek.securitydrones.gui.Window;
import me.dufek.securitydrones.uav.UAV;
import me.dufek.securitydrones.uav.UAVs;
import me.dufek.securitydrones.uav.destination.Destination;
import me.dufek.securitydrones.uav.destination.Destinations;
import me.dufek.securitydrones.uav.objective.Takeoff;
import me.dufek.securitydrones.uav.objective.flyto.FlyTo;
import me.dufek.securitydrones.uav.request.UAVRequests;

/**
 * Perimeter following algorithm single version. It creates a cycle
 * circumnavigating all the areas. Every UAV then follows the same area.
 *
 * @author Jan Dufek
 */
public class PerimeterFollowingAlgorithmSingle extends Algorithm {

    /**
     * Bridges between areas.
     */
    private ArrayList<Bridge> bridges;

    /**
     * Main cycle around areas.
     */
    private Cycle mainCycle;

    /**
     * Cycles for particular UAVs.
     */
    private Cycles cycles;

    public PerimeterFollowingAlgorithmSingle(Window mainWindow, Variability variability) {
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
     * Initialize the algorithm.
     */
    public void initialize() {

        bridges = null;
        mainCycle = null;
        cycles = null;

        if (Areas.noAreas()) {
            return;
        }

        if (UAVs.noUAVs() && UAVRequests.noRequest()) {
            return;
        }

        createBridges();

        createMainCycle();
    }

    /**
     * Assign paths for each UAV.
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

                for (Location location : cycle.getList()) {
                    uav.assignObjective(new FlyTo(location, "checkpoint"));
                    Destinations.addDestination(new Destination(uav, location));
                }
            }
        }
    }

    /**
     * Create bridges between areas.
     */
    public void createBridges() {
        bridges = new ArrayList<Bridge>();

        ArrayList<Area> usedAreas = new ArrayList<Area>(Areas.getNumberOfAreas());

        Area startArea = Areas.getArea(0);

        Area nextArea = startArea;

        while (usedAreas.size() < Areas.getNumberOfAreas() - 1) {
            usedAreas.add(nextArea);
            Bridge bridge = getShortestBridge(nextArea, usedAreas);
            bridges.add(bridge);
            nextArea = bridge.getEndArea();
        }
    }

    /**
     * Creates main cycle around the areas.
     */
    public void createMainCycle() {
        mainCycle = new Cycle();

        if (bridges.isEmpty()) {
            Area area = Areas.getArea(0);
            addPointsForArea(area, new Location(area.xpoints[0], area.ypoints[0]));
            mainCycle.removeLast();
            return;
        }

        addPointsForArea(bridges.get(0).getStartArea(), bridges.get(0).getStartLocation());

        for (int i = 0; i < bridges.size() - 1; i++) {
            Bridge incommingBridge = bridges.get(i);
            Bridge outcommingBridge = bridges.get(i + 1);

            Area area = incommingBridge.getEndArea();

            addPointsBetween(area, incommingBridge.getEndLocation(), outcommingBridge.getStartLocation());
        }

        addPointsForArea(bridges.get(bridges.size() - 1).getEndArea(), bridges.get(bridges.size() - 1).getEndLocation());

        for (int i = bridges.size() - 1; i > 0; i--) {
            Bridge outcommingBridge = bridges.get(i);
            Bridge incommingBridge = bridges.get(i - 1);

            Area area = outcommingBridge.getStartArea();

            addPointsBetween(area, outcommingBridge.getStartLocation(), incommingBridge.getEndLocation());
        }
    }

    /**
     * This method will add points between two locations to the cycle.
     *
     * @param area Area.
     * @param location1 First location.
     * @param location2 Second location.
     * @param cycle Cycle.
     */
    public void addPointsBetween(Area area, Location location1, Location location2) {
        boolean count = false;

        for (int i = 0; i < area.npoints; i++) {
            Location location = new Location(area.xpoints[i], area.ypoints[i]);

            if (Location.equal(location1, location, 1)) {
                count = true;
            }

            if (count == true) {
                if (Location.equal(location2, location, 1)) {
                    count = false;
                }

                mainCycle.add(location);
            }
        }

        if (count == true) {

            for (int i = 0; i < area.npoints; i++) {
                Location location = new Location(area.xpoints[i], area.ypoints[i]);

                if (Location.equal(location2, location, 1)) {
                    mainCycle.add(location);
                    break;
                }

                mainCycle.add(location);
            }
        }
    }

    /**
     * This function gets the shortest bridge between given area and the scope.
     *
     * @param area Area.
     * @param usedAreas List of used areas.
     * @param scope Scope.
     * @return Bridge.
     */
    private Bridge getShortestBridge(Area area, ArrayList<Area> usedAreas) {
        Bridge shortestBridge = null;
        double minimumLength = Double.MAX_VALUE;

        for (Area otherArea : Areas.listOfAreas) {
            if (usedAreas.contains(otherArea)) {
                continue;
            }

            Bridge bridge = getBridge(area, otherArea);

            if (bridge.getLenght() < minimumLength) {
                shortestBridge = bridge;
                minimumLength = bridge.getLenght();
            }
        }

        return shortestBridge;
    }

    /**
     * Gets the shortest bridge between areas.
     *
     * @param area1 First area.
     * @param area2 Second area.
     * @return Shortest bridge.
     */
    private Bridge getBridge(Area area1, Area area2) {
        double minimumDistance = Double.MAX_VALUE;
        Location bridgeStart = null;
        Location bridgeEnd = null;

        for (int i = 0; i < area1.npoints; i++) {
            Location area1Point = new Location(area1.xpoints[i], area1.ypoints[i]);

            for (int j = 0; j < area2.npoints; j++) {
                Location area2Point = new Location(area2.xpoints[j], area2.ypoints[j]);

                double distance = Location.getDistance2D(area1Point, area2Point);

                if (distance < minimumDistance) {
                    bridgeStart = area1Point;
                    bridgeEnd = area2Point;
                    minimumDistance = distance;
                }
            }
        }

        Bridge bridge = new Bridge(area1, bridgeStart, area2, bridgeEnd, minimumDistance);

        return bridge;
    }

    /**
     * Add all the points circumnavigating the area to the cycle.
     *
     * @param area Area.
     * @param startLocation Start location.
     * @param cycle Cycle.
     */
    private void addPointsForArea(Area area, Location startLocation) {
        boolean count = false;

        for (int i = 0; i < area.npoints; i++) {
            Location location = new Location(area.xpoints[i], area.ypoints[i]);

            if (Location.equal(startLocation, location, 1)) {
                count = true;
            }

            if (count == true) {
                mainCycle.add(location);
            }
        }

        for (int i = 0; i < area.npoints; i++) {
            Location location = new Location(area.xpoints[i], area.ypoints[i]);

            if (Location.equal(startLocation, location, 1)) {
                mainCycle.add(location);
                break;
            }

            if (count == true) {
                mainCycle.add(location);
            }
        }
    }

    /**
     * Get cycle for particular UAV.
     *
     * @param uav UAV.
     * @return Cycle for that UAV.
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
     * @return Cycle
     */
    public Cycle createCycle(UAV uav) {
        Cycle cycle = new Cycle();

        Location startLocation = getNearestPoint(uav);

        int cellIndex = mainCycle.getLocationIndex(startLocation);

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
     * Gets neatest point to the UAV.
     *
     * @param scope Scope.
     * @return
     */
    private Location getNearestPoint(UAV uav) {
        Location uavLocation = uav.getActualLocationMapCoordinates();

        if (uavLocation == null) {
            uavLocation = uav.getUAVRequest().getStartLocation();
        }

        Location nearestPoint = null;
        double minimalDistance = Double.MAX_VALUE;

        for (Area area : Areas.listOfAreas) {
            for (int i = 0; i < area.npoints; i++) {
                Location pointLocation = new Location(area.xpoints[i], area.ypoints[i]);

                double distance = Location.getDistance2D(uavLocation, pointLocation);

                if (distance < minimalDistance) {
                    minimalDistance = distance;
                    nearestPoint = pointLocation;
                }
            }
        }

        if (nearestPoint == null) {
            throw new IllegalStateException("Nearest point not found.");
        }

        return nearestPoint;
    }

    @Override
    public void paint(Graphics2D graphics, BufferedImage mapImage, double scale) {
        super.paint(graphics, mapImage, scale);

        drawCycles();
        drawBridges();
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

        for (Location location : cycle.getList()) {
            drawLocation(location, number);
            number++;
        }
    }

    private void drawBridges() {
        if (bridges == null) {
            return;
        }

        for (Bridge bridge : bridges) {
            drawBridge(bridge);
        }
    }

    private void drawBridge(Bridge bridge) {
        Location startLocationMapCoordinates = bridge.getStartLocation();
        Location startLocationBirdviewCoordinates = BirdviewMapConversion.mapToBirdview(startLocationMapCoordinates, mapImage, scale);

        Location endLocationMapCoordinates = bridge.getEndLocation();
        Location endLocationBirdviewCoordinates = BirdviewMapConversion.mapToBirdview(endLocationMapCoordinates, mapImage, scale);

        setAlpha(1);
        graphics.setColor(Preferences.EDGE_COLOR);
        graphics.drawLine(NumberConversion.toInteger(startLocationBirdviewCoordinates.getX()), NumberConversion.toInteger(startLocationBirdviewCoordinates.getY()), NumberConversion.toInteger(endLocationBirdviewCoordinates.getX()), NumberConversion.toInteger(endLocationBirdviewCoordinates.getY()));
    }

    private void drawLocation(Location location, int number) {
        Location birdviewLocation = BirdviewMapConversion.mapToBirdview(location, mapImage, scale);

        graphics.setColor(Preferences.GRID_CELL_CENTER_COLOR);
        graphics.fillOval(NumberConversion.toInteger(birdviewLocation.getX() - Preferences.GRID_CELL_CENTER_DIAMETER / 2), NumberConversion.toInteger(birdviewLocation.getY() - Preferences.GRID_CELL_CENTER_DIAMETER / 2), Preferences.GRID_CELL_CENTER_DIAMETER, Preferences.GRID_CELL_CENTER_DIAMETER);
        graphics.drawString(Integer.toString(number), NumberConversion.toInteger(birdviewLocation.getX() - Preferences.GRID_CELL_CENTER_DIAMETER / 2) + number * 3, NumberConversion.toInteger(birdviewLocation.getY() - Preferences.GRID_CELL_CENTER_DIAMETER / 2 - Preferences.GRID_CELL_CENTER_DIAMETER));
    }

    @Override
    public String getName() {
        return Algorithms.PERIMETER_FOLLOWING.toString() + " - " + AreaDivision.SINGLE.toString() + " - " + variability.toString();
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
