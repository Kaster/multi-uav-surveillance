package me.dufek.securitydrones.chargingstation.status;

import me.dufek.securitydrones.chargingstation.ChargingStation;

/**
 *
 * @author Jan Dufek
 */
public class Status {

    private final ChargingStation chargingStation;
    private String status;

    public Status(ChargingStation chargingStation, String status) {
        this.chargingStation = chargingStation;
        this.status = status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusMessage() {
        return this.status;
    }

    public ChargingStation getChargingStation() {
        return this.chargingStation;
    }

    public String getChargingStationName() {
        return chargingStation.getName();
    }
}
