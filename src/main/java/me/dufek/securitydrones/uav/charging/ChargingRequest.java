package me.dufek.securitydrones.uav.charging;

import me.dufek.securitydrones.uav.UAV;

/**
 * Charging request is used to request charging.
 *
 * @author Jan Dufek
 */
public class ChargingRequest {

    /**
     * UAV which is requesting.
     */
    private UAV uav;

    /**
     * Requested time.
     */
    private double requestedTime;

    public ChargingRequest(UAV uav, double requestedTime) {
        this.uav = uav;
        this.requestedTime = requestedTime;
    }

    public void setUAV(UAV uav) {
        this.uav = uav;
    }

    public UAV getUAV() {
        return this.uav;
    }

    public void setRequetedTime(double requestedTime) {
        this.requestedTime = requestedTime;
    }

    public double getRequestedTime() {
        return this.requestedTime;
    }
}
