package me.dufek.securitydrones.chargingstation.schedule;

import java.io.Serializable;
import me.dufek.securitydrones.uav.UAV;

/**
 * Represent task in time.
 *
 * @author Jan Dufek
 */
public class Task implements Serializable {

    /**
     * Start of the task.
     */
    private double start;

    /**
     * End of the task.
     */
    private double end;
    private UAV uav;
    private boolean completed;

    public Task(double start, double end, UAV uav) {
        if (start > end) {
            throw new IllegalStateException("Task can not start after its end.");
        }

        this.start = start;
        this.end = end;
        this.uav = uav;
        this.completed = false;
    }

    public void setStart(double start) {
        this.start = start;
    }

    public double getStart() {
        return this.start;
    }

    public void setEnd(double end) {
        this.end = end;
    }

    public double getEnd() {
        return this.end;
    }

    public void move(double shift) {
        this.start -= shift;
        this.end -= shift;
    }

    /**
     * This method shifts the task to a new start.
     *
     * @param start New start.
     */
    public void moveToNewStart(double start) {
        double shift = this.getStart() - start;

        move(shift);
    }

    @Override
    public String toString() {
        return uav.name + ": " + start + " - " + end;
    }

    public String getUAVName() {
        return uav.getName();
    }

    public boolean isFinished() {
        return this.end < this.uav.getTime();
    }

    public void setCompleted() {
        this.completed = true;
    }

    public boolean isCompleted() {
        return this.completed;
    }
}
