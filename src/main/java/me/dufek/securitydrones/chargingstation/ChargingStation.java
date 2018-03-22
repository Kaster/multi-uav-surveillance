package me.dufek.securitydrones.chargingstation;

import cz.cuni.amis.pogamut.base3d.worldview.object.Location;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import me.dufek.securitydrones.conversion.BirdviewMapConversion;
import me.dufek.securitydrones.uav.charging.ChargingRequest;
import me.dufek.securitydrones.chargingstation.schedule.Schedule;
import me.dufek.securitydrones.chargingstation.schedule.Task;
import me.dufek.securitydrones.preferences.UserPreferences;
import me.dufek.securitydrones.uav.charging.ChargingTask;

/**
 * Charging station is a superclass for charging station. It represents a place
 * where UAV can charge.
 *
 * @author Jan Dufek
 */
public abstract class ChargingStation implements Serializable {

    private Location location;
    private String name;
    private double chargingPerformance;
    private boolean occupied = false;
    private Schedule schedule;
    private Shape visualObject;

    public ChargingStation(Location location, String name, double chargingPerformance) {
        this.location = location;
        this.name = name;
        this.chargingPerformance = chargingPerformance;

        schedule = new Schedule();
    }

    /**
     * Logic is called from the Simulation class.
     */
    public void logic() {
        schedule.removeFinishedTasks();
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setLocationBirdviewCoordinates(int x, int y, BufferedImage birdview, double scale) {
        Location birdviewLocation = new Location(x, y, 0);
        Location mapLocation = BirdviewMapConversion.birdviewToMap(birdviewLocation, birdview, scale);

        this.location = mapLocation;
    }

    public Location getLocation() {
        return this.location;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setChargingPerformance(double chargingPerformance) {
        this.chargingPerformance = chargingPerformance;
    }

    public double getChargingPerformance() {
        return this.chargingPerformance;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public boolean isFree() {
        return !occupied;
    }

    @Override
    public String toString() {
        return name + " " + location.toString() + " " + chargingPerformance + " " + occupied;
    }

    public double getMaximumChargingTime() {
        throw new IllegalStateException("Should be implemented in descendants.");
    }

    public double getChargingTime() {
        throw new IllegalStateException("Should be implemented in descendants.");
    }

    /**
     * Request charging on this charging station.
     *
     * @param chargingRequest Charging request.
     * @return
     */
    public Task requestCharging(ChargingRequest chargingRequest) {
        Task chargingTask = new Task(chargingRequest.getRequestedTime(), getTaskEnd(chargingRequest), chargingRequest.getUAV());

        if (!schedule.scheduleConflict(chargingTask)) {
            schedule.registerTask(chargingTask);
            return chargingTask;
        } else {
            return null;
        }
    }

    /**
     * Get the time when the schedule on this charging station starts.
     *
     * @return Start of schedule.
     */
    public double getScheduleStart() {
        return schedule.getStart();
    }

    /**
     * Gets the earliest charging task.
     *
     * @return Task.
     */
    public Task getEarliestTask() {
        return schedule.getEarliestTask();
    }

    /**
     * Enforce charging will cause the schedule to shift.
     *
     * @param chargingRequest Charging request.
     * @return
     */
    public Task enforceCharging(ChargingRequest chargingRequest) {
        Task chargingTask = new Task(chargingRequest.getRequestedTime(), getTaskEnd(chargingRequest), chargingRequest.getUAV());

        schedule.forceRegisterTask(chargingTask);

        return chargingTask;
    }

    /**
     * Gets the end of charging task.
     * 
     * @param chargingRequest Charging request.
     * @return Charging task end.
     */
    private double getTaskEnd(ChargingRequest chargingRequest) {
        double taskStart = chargingRequest.getRequestedTime();
        double startingProcedureReserve = UserPreferences.preferences.UAV_RESERVE_STARTING_CHARGING_PROCEDURE;
        double landingTime = chargingRequest.getUAV().getLandingTime();
        double landingTimeReserve = UserPreferences.preferences.UAV_RESERVE_LANDING_TO_CHARGER;
        double maximumChargingTime = getMaximumChargingTime();
        double takeoffTime = chargingRequest.getUAV().getTakeoffTime();
        double takeoffTimeReserve = UserPreferences.preferences.UAV_RESERVE_RAISING_FROM_CHARGER;
        double endingProcedureReserve = UserPreferences.preferences.UAV_RESERVE_ENDING_CHARGING_PROCEDURE;

        double taskEnd = taskStart + startingProcedureReserve + landingTime * landingTimeReserve + maximumChargingTime + takeoffTime * takeoffTimeReserve + endingProcedureReserve;

        return taskEnd;
//        return (chargingRequest.getRequestedTime() + UserPreferences.preferences.UAV_RESERVE_STARTING_CHARGING_PROCEDURE + chargingRequest.getUAV().getLandingTime() * UserPreferences.preferences.UAV_RESERVE_LANDING_TO_CHARGER + getMaximumChargingTime() + chargingRequest.getUAV().getTakeoffTime() * UserPreferences.preferences.UAV_RESERVE_RAISING_FROM_CHARGER + UserPreferences.preferences.UAV_RESERVE_ENDING_CHARGING_PROCEDURE);
    }

    public void resetSchedule() {
        this.schedule = new Schedule();
    }

    public Schedule getSchedule() {
        return this.schedule;
    }

    public void setVisualObject(Shape visualObject) {
        this.visualObject = visualObject;
    }

    public boolean clicked(MouseEvent event) {
        return this.visualObject.contains(event.getX(), event.getY());
    }

    public int getNumberOfTasksInSchedule() {
        return schedule.getNumberOfTasks();
    }

    public int getNumberOfIncompleteTasksInSchedule() {
        return schedule.getNumberOfIncompleteTasks();
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }

    public void cancel(ChargingTask chargingTask) {
        this.schedule.cancel(chargingTask);
    }
}
