package me.dufek.securitydrones.uav.request;

import cz.cuni.amis.pogamut.base3d.worldview.object.Location;
import cz.cuni.amis.pogamut.base3d.worldview.object.Rotation;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import me.dufek.securitydrones.algorithm.grid.Dimension;
import me.dufek.securitydrones.algorithm.grid.Grid;
import me.dufek.securitydrones.algorithm.spanningtree.SpanningTree;
import me.dufek.securitydrones.conversion.BirdviewMapConversion;
import me.dufek.securitydrones.gui.map.MapInformation;
import me.dufek.securitydrones.uav.Preferences;
import me.dufek.securitydrones.battery.Battery;
import me.dufek.securitydrones.preferences.UserPreferences;
import me.dufek.securitydrones.uav.flightlevel.FlightLevels;

/**
 * UAV request is used to store information about particular UAV before it is
 * spawned to the virtual environment.
 *
 * @author Jan Dufek
 */
public class UAVRequest implements Serializable {

    private String name;
    private Battery battery;
    private double amperageMean;
    private Location startLocation;
    private Integer flightLevel;
    private transient SpanningTree spanningTree;
    private final double linearVelocity;
    private final double altitudeVelocity;
    private final double rotationalVelocity;
    private final double primarySensorHorizontalFieldOfView;
    private final double primarySensorVerticalFieldOfView;

    /**
     * Visual representation in GUI.
     */
    private Shape visualObject;

    public UAVRequest(String name, double batteryCapacity, double amperageMean, double linearVelocity, double altitudeVelocity, double rotationalVelocity, double primarySensorHorizontalFieldOfView, double primarySensorVerticalFieldOfView, Location startLocation) {
        this.name = name;
        this.battery = new Battery(batteryCapacity);
        this.amperageMean = amperageMean;
        this.startLocation = startLocation;
        this.linearVelocity = linearVelocity;
        this.altitudeVelocity = altitudeVelocity;
        this.rotationalVelocity = rotationalVelocity;
        this.primarySensorHorizontalFieldOfView = primarySensorHorizontalFieldOfView;
        this.primarySensorVerticalFieldOfView = primarySensorVerticalFieldOfView;
    }

    public UAVRequest(String name, double batteryCapacity, double amperageMean, double linearVelocity, double altitudeVelocity, double rotationalVelocity, double primarySensorHorizontalFieldOfView, double primarySensorVerticalFieldOfView, int startLocationX, int startLocationY) {
        this.name = name;
        this.battery = new Battery(batteryCapacity);
        this.amperageMean = amperageMean;
        this.linearVelocity = linearVelocity;
        this.altitudeVelocity = altitudeVelocity;
        this.rotationalVelocity = rotationalVelocity;
        this.primarySensorHorizontalFieldOfView = primarySensorHorizontalFieldOfView;
        this.primarySensorVerticalFieldOfView = primarySensorVerticalFieldOfView;
        this.startLocation = new Location(startLocationX, startLocationY, 0);
    }

    public void setStartLocationBirdviewCoordinates(int x, int y, BufferedImage birdview, double scale) {
        Location birdviewLocation = new Location(x, y, 0);
        Location mapLocation = BirdviewMapConversion.birdviewToMap(birdviewLocation, birdview, scale);

//        // Taking z location as maximum z coordinate of all navigation points. Do not work because if UAV falls from too high level, it will break down.
//        Location mapLocationWithZ = new Location(mapLocation.x, mapLocation.y, - MapInformation.mapBoundaries.z.max / 250);
        // Take z location as average z coordinate of all navigation points. Works better, but some places on map are higher and UAV will not spawn on them.
        // Solution is to spawn in hight altitude and slowly land.
        Location mapLocationWithZ = new Location(mapLocation.x, mapLocation.y, -(MapInformation.mapBoundaries.averageElevation / 250) - UserPreferences.preferences.SPAWNING_HEIGHT_ABOVE_SPAWN_LEVEL);
        this.startLocation = mapLocationWithZ;
    }

    @Override
    public String toString() {
        return this.name + " " + this.battery + " " + this.startLocation.toString();
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Battery getBattery() {
        return this.battery;
    }

    public void setBattery(Battery battery) {
        this.battery = battery;
    }

    public double getAmperageMean() {
        return this.amperageMean;
    }

    public void setAmperageMean(double amperageMean) {
        this.amperageMean = amperageMean;
    }

    public Location getStartLocation() {
        return this.startLocation;
    }

    public void setStartLocation(Location startLocation) {
        this.startLocation = startLocation;
    }

    public void setSpanningTree(SpanningTree spanningTree) {
        this.spanningTree = spanningTree;
    }

    public SpanningTree getSpanningTree() {
        return this.spanningTree;
    }

    public void resetBattery() {
        double batteryCapacity = battery.getCapacity();

        setBattery(new Battery(batteryCapacity));
    }

    public void setFlightLevel() {
        int newFlightLevel = FlightLevels.getNextFlightLevel(this);
        this.flightLevel = newFlightLevel;
    }

    public Integer getFlightLevel() {
        return this.flightLevel;
    }

    public boolean isFlightLevelSet() {
        return this.flightLevel != null;
    }

    public double getFlightAltitude() {
//        int usableFlightLevel;
//        
//        if (this.flightLevel == null) {
//            usableFlightLevel = 0;
//        } else {
//            usableFlightLevel = this.flightLevel;
//        }

        return UserPreferences.preferences.MAXIMUM_FLIGHT_LEVEL - this.flightLevel * UserPreferences.preferences.DISTANCE_BETWEEN_FLIGHT_LEVELS;
    }

    public double getLinearVelocity() {
        return this.linearVelocity;
    }

    public double getAltitudeVelocity() {
        return this.altitudeVelocity;
    }

    public double getRotationalVelocity() {
        return this.rotationalVelocity;
    }

    public double getPrimarySensorHorizontalFieldOfView() {
        return this.primarySensorHorizontalFieldOfView;
    }

    public double getPrimarySensorVerticalFieldOfView() {
        return this.primarySensorVerticalFieldOfView;
    }

    /**
     * Get dimension of visible area.
     *
     * @return Dimension of visible area.
     */
    public Dimension getVisibleAreaDimension() {
        double flightAltitude = getFlightAltitude();

        double width = Math.tan(Math.toRadians(getPrimarySensorHorizontalFieldOfView() / 2)) * flightAltitude * 2;
        double height = Math.tan(Math.toRadians(getPrimarySensorVerticalFieldOfView() / 2)) * flightAltitude * 2;

        Dimension dimension = new Dimension(width, height);

        return dimension;
    }

    public double getVisibleAreaWidth() {
        return getVisibleAreaDimension().getWidth();
    }

    public double getVisibleAreaHeight() {
        return getVisibleAreaDimension().getHeight();
    }

    /**
     * Returns remaining battery time in seconds.
     *
     * @return
     */
    public double getBatteryRemainingTime() {
        double remainingTime;

        remainingTime = (battery.getActualLevel() / (amperageMean * 1000)) * 60 * 60;

        return remainingTime;
    }

    public Rotation getRotation() {
        return new Rotation(0, 0, 0);
    }

    public void setVisualObject(Shape visualObject) {
        this.visualObject = visualObject;
    }

    public boolean clicked(MouseEvent event) {
        return visualObject.contains(event.getX(), event.getY());
    }

    public UAVRequest getCopy() {
        UAVRequest uavRequest = new UAVRequest(UAVRequests.getNextDefaultName(), battery.getCapacity(), amperageMean, linearVelocity, altitudeVelocity, rotationalVelocity, primarySensorHorizontalFieldOfView, primarySensorVerticalFieldOfView, null);

        return uavRequest;
    }
}
