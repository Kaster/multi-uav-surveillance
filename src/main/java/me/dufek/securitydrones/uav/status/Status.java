package me.dufek.securitydrones.uav.status;

import me.dufek.securitydrones.uav.UAV;

/**
 * Status of UAV which is used in the right bar.
 *
 * @author Jan Dufek
 */
public class Status {

    /**
     * UAV.
     */
    private final UAV uav;

    /**
     * Corresponding string status.
     */
    private String status;

    public Status(UAV uav, String status) {
        this.uav = uav;
        this.status = status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusMessage() {
        return this.status;
    }

    public UAV getUAV() {
        return this.uav;
    }

    public String getUAVName() {
        return uav.getName();
    }
}
