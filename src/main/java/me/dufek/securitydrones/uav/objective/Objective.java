package me.dufek.securitydrones.uav.objective;

import me.dufek.securitydrones.logger.Logger;
import me.dufek.securitydrones.uav.UAV;
import me.dufek.securitydrones.uav.status.UAVsStatus;

/**
 * The objective superclass is the main class form which all objectives must
 * inherit. It provides some basic functions which can be implemented in
 * descendants.
 *
 * @author Jan Dufek
 */
public class Objective {

    /**
     * Name of the objective.
     */
    public String name;

    /**
     * Is it high priority objective?
     */
    private boolean hightPriority = false;

    /**
     * UAV reference.
     */
    public UAV uav = null;

    /**
     * Is it satisfied?
     */
    boolean satisfied = false;

    /**
     * Was before logic method completed.
     */
    private boolean beforeLogicCompleted = false;

    /**
     * This method is called only once in the beginning.
     */
    public void beforeLogic() {
        if (name != null) {
            Logger.log(uav.name + ": " + name);
            UAVsStatus.setStatus(uav, name + ".");
        }

        beforeLogicCompleted = true;
    }

    /**
     * This method is called periodically from the UAV.
     */
    public void logic() {
        if (uav == null) {
            throw new IllegalStateException("No UAV is asign to the objective.");
        }

        if (!beforeLogicCompleted) {
            beforeLogic();
        }
    }

    /**
     * Is this objective satisfied?
     * 
     * @return True if satisfied, false otherwise.
     */
    public boolean isSatisfied() {
        return satisfied;
    }

    /**
     * Satisfy this objective.
     */
    public void satisfy() {
        satisfied = true;
    }

    /**
     * Set UAV for this objective.
     * 
     * @param uav UAV
     */
    public void setUAV(UAV uav) {
        this.uav = uav;
    }

    /**
     * Mark if before logic method was completed or not.
     * 
     * @param completed Was it completed?
     */
    public void setBeforeLogicCompleted(boolean completed) {
        this.beforeLogicCompleted = completed;
    }

    /**
     * Print string representation of the objective.
     * @return 
     */
    @Override
    public String toString() {
        return name;
    }

    /**
     * Set this objective as being high priority objective.
     */
    public void setHightPriority() {
        this.hightPriority = true;
    }

    /**
     * Checks if this objective is a high priority one.
     * 
     * @return True if it is high priority and false otherwise.
     */
    public boolean isHightPriority() {
        return this.hightPriority;
    }
}
