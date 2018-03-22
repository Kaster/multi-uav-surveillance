package me.dufek.securitydrones.area;

import me.dufek.securitydrones.geometry.PolygonDouble;
import me.dufek.securitydrones.geometry.Bounds;
import cz.cuni.amis.pogamut.base3d.worldview.object.Location;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.Collections;
import me.dufek.securitydrones.algorithm.grid.Cell;
import me.dufek.securitydrones.algorithm.grid.SubCell;
import me.dufek.securitydrones.area.status.AreasStatus;
import me.dufek.securitydrones.conversion.BirdviewMapConversion;
import me.dufek.securitydrones.conversion.NumberConversion;
import me.dufek.securitydrones.heatmap.HeatCell;
import me.dufek.securitydrones.heatmap.HeatGrid;
import me.dufek.securitydrones.heatmap.Preferences;
import me.dufek.securitydrones.main.Global;
import me.dufek.securitydrones.preferences.UserPreferences;
import me.dufek.securitydrones.uav.UAV;
import me.dufek.securitydrones.uav.UAVs;
import me.dufek.securitydrones.uav.request.UAVRequest;
import me.dufek.securitydrones.uav.request.UAVRequests;
import me.dufek.securitydrones.utilities.RandomNumber;
import org.jfree.util.ArrayUtilities;

/**
 * Area represents area which is needed to be surveyed.
 *
 * @author Jan Dufek
 */
public class Area extends PolygonDouble {

    private AreaType type;
    private String name;

    /**
     * Last point is used during UAV addition.
     */
    public Location lastPoint;
    private HeatGrid grid;

    public Area() {
        super();
    }

//    public Area(double[] coordinatesX, double[] coordinatesY, int numberOfPoints, HeatGrid grid) {
//        super(coordinatesX, coordinatesY, numberOfPoints);
//        this.grid = grid;
//    }
    public Area(double[] coordinatesX, double[] coordinatesY, int numberOfPoints) {
        super(coordinatesX, coordinatesY, numberOfPoints);
    }

    public void addPointBirdview(int birdviewX, int birdviewY) {
        Location birdviewLocation = new Location(birdviewX, birdviewY, 0);
        Location mapLocation = BirdviewMapConversion.birdviewToMap(birdviewLocation, Global.window.mapPanel.map.getImage(), Global.window.mapPanel.map.scale);

        addPoint(mapLocation.x, mapLocation.y);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setType(String type) {
        this.type = AreaType.valueOf(type);
    }

    public void setType(AreaType type) {
        this.type = type;
    }

    public AreaType getType() {
        return this.type;
    }

    @Override
    public String toString() {
        return name + " " + type;
    }

    /**
     * Gets index of random side.
     *
     * @return Index of random side.
     */
    private int getRandomSideIndex() {

        double totalWeight = 0;

        for (int i = 0; i < npoints; i++) {
            totalWeight += getSideLength(i);
        }

        int index = -1;

        double random = Math.random() * totalWeight;

        for (int i = 0; i < npoints; ++i) {
            random -= getSideLength(i);

            if (random <= 0) {
                index = i;
                break;
            }
        }

        return index;
    }

    /**
     * Gets length of particular side.
     *
     * @param index Index of side.
     * @return Length.
     */
    private double getSideLength(int index) {
        int startIndex = index;

        int endIndex = index + 1;

        if (endIndex == npoints) {
            endIndex = 0;
        }

        Location sideStart = new Location(xpoints[startIndex], ypoints[startIndex]);
        Location sideEnd = new Location(xpoints[endIndex], ypoints[endIndex]);

        return Location.getDistance2D(sideStart, sideEnd);
    }

    /**
     * Gets random point on the perimeter of current area.
     *
     * @return Random point on perimeter.
     */
    public Location getRandomPointOnPerimeter() {
        int randomSideIndex = getRandomSideIndex();
        int startIndex = randomSideIndex;

        int endIndex;

        if (randomSideIndex + 1 == npoints) {
            endIndex = 0;
        } else {
            endIndex = randomSideIndex + 1;
        }

        double startX = xpoints[startIndex];
        double startY = ypoints[startIndex];
        double endX = xpoints[endIndex];
        double endY = ypoints[endIndex];

        double slope = 0;

        if (endX - startX == 0) {
            slope = -1;
        } else {
            slope = (endY - startY) / (double) (endX - startX);
        }

        double x = RandomNumber.getDouble(startX, endX);

        double y;

        if (slope == -1) { // Horizontal line
            y = RandomNumber.getDouble(startY, endY);
        } else {
            y = slope * (x - startX) + startY;
        }

        if (x == Double.NaN || y == Double.NaN) {
            throw new IllegalStateException("Random point on the area perimeter is NaN.");
        }

        return new Location(x, y, 0);
    }

    /**
     * Create heat grid.
     */
    public void createGrid() {
        this.grid = new HeatGrid(this);
    }

    public HeatGrid getGrid() {
        return this.grid;
    }

    public Bounds getBoundsDouble() {
        double minX = Double.MAX_VALUE;
        double maxX = -Double.MAX_VALUE;
        double minY = Double.MAX_VALUE;
        double maxY = -Double.MAX_VALUE;

        for (int i = 0; i < npoints; i++) {
            if (minX > xpoints[i]) {
                minX = xpoints[i];
            }
            if (maxX < xpoints[i]) {
                maxX = xpoints[i];
            }
            if (minY > ypoints[i]) {
                minY = ypoints[i];
            }
            if (maxY < ypoints[i]) {
                maxY = ypoints[i];
            }
        }

        return new Bounds(minX, maxX, minY, maxY);
    }

    public boolean contains(HeatCell cell) {
        if (contains(cell.getX(), cell.getY())) {
            return true;
        }

        if (contains(cell.getX() - UserPreferences.preferences.CELL_HEIGHT, cell.getY())) {
            return true;
        }

        if (contains(cell.getX(), cell.getY() + UserPreferences.preferences.CELL_WIDTH)) {
            return true;
        }

        if (contains(cell.getX() - UserPreferences.preferences.CELL_HEIGHT, cell.getY() + UserPreferences.preferences.CELL_WIDTH)) {
            return true;
        }

        return false;
    }

    @Override
    public boolean contains(double x, double y) {
        return super.contains(x, y);
    }

    /**
     * Heat up the heat grid of this area.
     */
    public void updateHeatMap() {
        if (grid == null) {
            return;
        }

        grid.heatUp();
    }

    public void resetGrid() {
        grid = new HeatGrid(this);
    }
    
    /**
     * Checks if the area intersects with given cell.
     *
     * @param cell Cell.
     * @return True if they intersect, false otherwise.
     */
    public boolean intersect(Cell cell) {
        Location upperLeftCorner = cell.getTopLeftCornerLocation();

        if (contains(upperLeftCorner.getX(), upperLeftCorner.getY())) {
            return true;
        }

        if (contains(upperLeftCorner.getX() - cell.getHeight(), upperLeftCorner.getY())) {
            return true;
        }

        if (contains(upperLeftCorner.getX(), upperLeftCorner.getY() + cell.getWidth())) {
            return true;
        }

        if (contains(upperLeftCorner.getX() - cell.getHeight(), upperLeftCorner.getY() + cell.getWidth())) {
            return true;
        }

        for (double i = upperLeftCorner.getX(); i >= upperLeftCorner.getX() - cell.getHeight(); i--) {
//            System.out.println(i + " " + upperLeftCorner.getY());
            if (contains(i, upperLeftCorner.getY())) {
                return true;
            }
        }

        for (double i = upperLeftCorner.getX(); i >= upperLeftCorner.getX() - cell.getHeight(); i--) {
            if (contains(i, upperLeftCorner.getY() + cell.getWidth())) {
                return true;
            }
        }

        for (double i = upperLeftCorner.getY(); i <= upperLeftCorner.getY() + cell.getWidth(); i++) {
            if (contains(upperLeftCorner.getX(), i)) {
                return true;
            }
        }

        for (double i = upperLeftCorner.getY(); i <= upperLeftCorner.getY() + cell.getWidth(); i++) {
            if (contains(upperLeftCorner.getX() - cell.getHeight(), i)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Checks if the area intersects with given sub-cell.
     *
     * @param subCell Sub-cell.
     * @return True if they intersect, false otherwise.
     */
    public boolean intersect(SubCell subCell) {
        Location upperLeftCorner = subCell.getCornerLocation();

        if (contains(upperLeftCorner.getX(), upperLeftCorner.getY())) {
            return true;
        }

        if (contains(upperLeftCorner.getX() - subCell.getHeight(), upperLeftCorner.getY())) {
            return true;
        }

        if (contains(upperLeftCorner.getX(), upperLeftCorner.getY() + subCell.getWidth())) {
            return true;
        }

        if (contains(upperLeftCorner.getX() - subCell.getHeight(), upperLeftCorner.getY() + subCell.getWidth())) {
            return true;
        }

        for (double i = upperLeftCorner.getX(); i >= upperLeftCorner.getX() - subCell.getHeight(); i--) {
            if (contains(i, upperLeftCorner.getY())) {
                return true;
            }
        }

        for (double i = upperLeftCorner.getX(); i >= upperLeftCorner.getX() - subCell.getHeight(); i--) {
            if (contains(i, upperLeftCorner.getY() + subCell.getWidth())) {
                return true;
            }
        }

        for (double i = upperLeftCorner.getY(); i <= upperLeftCorner.getY() + subCell.getWidth(); i++) {
            if (contains(upperLeftCorner.getX(), i)) {
                return true;
            }
        }

        for (double i = upperLeftCorner.getY(); i <= upperLeftCorner.getY() + subCell.getWidth(); i++) {
            if (contains(upperLeftCorner.getX() - subCell.getHeight(), i)) {
                return true;
            }
        }

        return false;
    }

    public boolean clicked(MouseEvent event) {
        Location eventLocationBirdviewCoordinates = new Location(event.getX(), event.getY(), 0);
        Location eventLocationMapCoordinates = BirdviewMapConversion.birdviewToMap(eventLocationBirdviewCoordinates, Global.window.mapPanel.map.mapImage, Global.window.mapPanel.map.scale);
        return this.contains(eventLocationMapCoordinates.getX(), eventLocationMapCoordinates.getY());
    }

    public String getTypeString() {
        switch (this.type) {
            case GUARDED:
                return "Guarded";
//            case RESTRICTED:
//                return "Restricted";
//            case LANDING:
//                return "Landing";
            default:
                throw new IllegalStateException("Unknown area type.");
        }
    }

    public Double getArea() {
        double area = 0;

        for (int i = 0; i < this.npoints; i++) {

            int j = (i + 1) % this.npoints;

            area += this.xpoints[i] * this.ypoints[j];
            area -= this.ypoints[i] * this.xpoints[j];
        }

        area /= 2;

        return (area < 0 ? -area : area);
    }

    public Double getActualCoverage() {
        if (grid == null) {
            return 0.0;
        }

        double heatSum = 0;

        for (HeatCell cell : this.grid.getCells()) {
            heatSum += cell.getHeat();
        }

        return heatSum / this.grid.getCells().size();
    }

    public void moveTo(double x, double y) {
//        double maxX = - Double.MAX_VALUE;
//        double minY = Double.MAX_VALUE;
//        
//        for (double currentX : xpoints) {
//            if (maxX < currentX) {
//                maxX = currentX;
//            }
//        }
//        
//        for (double currentY : ypoints) {
//            if (currentY < minY) {
//                minY = currentY;
//            }
//        }

        double centroidX = 0;
        double centroidY = 0;

        for (double currentX : xpoints) {
            centroidX += currentX;
        }

        centroidX /= npoints;

        for (double currentY : ypoints) {
            centroidY += currentY;
        }

        centroidY /= npoints;

        double differenceX = x - centroidX;
        double differenceY = y - centroidY;

        for (int i = 0; i < npoints; i++) {
            xpoints[i] += differenceX;
            ypoints[i] += differenceY;
        }
    }

    /**
     * Gets copy of this area.
     * 
     * @return Copy of area.
     */
    public Area getCopy() {
        double[] xPointsCopy = new double[npoints];
        System.arraycopy(xpoints, 0, xPointsCopy, 0, npoints);

        double[] yPointsCopy = new double[npoints];
        System.arraycopy(ypoints, 0, yPointsCopy, 0, npoints);

        Area area = new Area(xPointsCopy, yPointsCopy, npoints);

        area.setName(Areas.getNextDefaultName());
        area.setType(this.getType());

        return area;
    }

    public void setStatus() {
        String status = getTypeString() + ". " + NumberConversion.toInteger(getArea()) + " sq m. " + NumberConversion.toInteger(getActualCoverage()) + " s.";
        AreasStatus.setStatus(this, status);
    }

    public double getDistance(Location location) {
        double minimalDistance = Double.MAX_VALUE;

        for (int i = 0; i < npoints; i++) {
            Location pointLocation = new Location(xpoints[i], ypoints[i]);

            double distance = Location.getDistance2D(location, pointLocation);

            if (distance < minimalDistance) {
                minimalDistance = distance;
            }
        }

        if (Double.compare(minimalDistance, Double.MAX_VALUE) == 0) {
            throw new IllegalStateException("Distance can not be determined.");
        }

        return minimalDistance;
    }

    public UAVRequest getNearestUAV() {
        double minimumDistance = Double.MAX_VALUE;
        UAVRequest nearestUAV = null;

        for (UAVRequest uavRequest : UAVRequests.getList()) {
            Location uavLocation = uavRequest.getStartLocation();

            double distance = getDistance(uavLocation);

            if (distance < minimumDistance) {
                minimumDistance = distance;
                nearestUAV = uavRequest;
            }
        }

        for (UAV uav : UAVs.listOfUAVs) {
            Location uavLocation = uav.getActualLocationMapCoordinates();

            if (uavLocation == null) {
                uavLocation = uav.getUAVRequest().getStartLocation();
            }

            double distance = getDistance(uavLocation);

            if (distance < minimumDistance) {
                minimumDistance = distance;
                nearestUAV = uav.getUAVRequest();
            }
        }

        return nearestUAV;
    }
}
