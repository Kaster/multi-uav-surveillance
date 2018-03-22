package me.dufek.securitydrones.uav.charging;

import cz.cuni.amis.pogamut.base3d.worldview.object.Location;
import me.dufek.securitydrones.chargingstation.ChargingStation;
import me.dufek.securitydrones.chargingstation.schedule.Task;

/**
 * Charging task represents scheduled charging.
 *
 * @author Jan Dufek
 */
public class ChargingTask {

    /**
     * Time task containing start and end.
     */
    private Task task;

    /**
     * On which charging station it is scheduled.
     */
    private ChargingStation chargingStation;

    /**
     * Is this task in progress?
     */
    private boolean inProgress;

    public ChargingTask(Task task, ChargingStation chargingStation) {
        this.task = task;
        this.chargingStation = chargingStation;
        this.inProgress = false;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public Task getTask() {
        return this.task;
    }

    public void setStart(double start) {
        task.moveToNewStart(start);
    }

    public double getStart() {
        return task.getStart();
    }

    public double getEnd() {
        return task.getEnd();
    }

    public void setChargingStation(ChargingStation chargingStation) {
        this.chargingStation = chargingStation;
    }

    public Location getLocation() {
        return chargingStation.getLocation();
    }

    public boolean inProgress() {
        return inProgress;
    }

    public void setInProgress() {
        this.inProgress = true;
    }

    public ChargingStation getChargingStation() {
        return this.chargingStation;
    }

    public void cancel() {
        chargingStation.cancel(this);
    }
}
