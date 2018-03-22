package me.dufek.securitydrones.chargingstation.schedule;

import java.util.ArrayList;
import java.util.Collections;
import me.dufek.securitydrones.uav.charging.ChargingRequest;
import me.dufek.securitydrones.uav.charging.ChargingTask;
import me.dufek.securitydrones.uav.UAV;
import me.dufek.securitydrones.chargingstation.ChargingStation;
import me.dufek.securitydrones.chargingstation.ChargingStations;

/**
 * Scheduler is responsible for scheduling charging stations.
 *
 * @author Jan Dufek
 */
public class Scheduler {

    /**
     * List of charging requests.
     */
    private ArrayList<ChargingRequest> chargingRequests = new ArrayList<ChargingRequest>();

    public void requestCharging(UAV uav) {
        double requestedTime = uav.getChargingProcedureRequestedTime();
        ChargingRequest chargingRequest = new ChargingRequest(uav, requestedTime);
        chargingRequests.add(chargingRequest);
    }

    /**
     * This methods is used to create a schedule.
     */
    public void makeSchedule() {
        Collections.sort(chargingRequests, new BatteryDeadComparator());
        for (ChargingRequest chargingRequest : chargingRequests) {
            ChargingTask chargingTask = meetRequest(chargingRequest);
            chargingRequest.getUAV().assignChargingTask(chargingTask);
        }

        chargingRequests.clear();
    }

    /**
     * Meet request is responsible for ensuring that every request is fulfilled.
     * It could be done either by registering the task or shifting schedule.
     *
     * @param chargingRequest
     * @return
     */
    private ChargingTask meetRequest(ChargingRequest chargingRequest) {

        // Get free charging station
        for (ChargingStation chargingStation : ChargingStations.getListSortedByPreference()) {
            Task task = chargingStation.requestCharging(chargingRequest);

            if (task != null) {
                ChargingTask chargingTask = new ChargingTask(task, chargingStation);
                return chargingTask;
            }
        }

        // No charging station is free. We need to shift schedule.
        ChargingStation chargingStationLatestSchedule = null;

        for (ChargingStation chargingStation : ChargingStations.listOfChargingStations) {
            if (chargingStationLatestSchedule == null || chargingStationLatestSchedule.getScheduleStart() < chargingStation.getScheduleStart()) {
                chargingStationLatestSchedule = chargingStation;
            }
        }

        if (chargingStationLatestSchedule == null) {
            throw new IllegalStateException("Some station must have the latest schedule.");
        }

        Task task = chargingStationLatestSchedule.enforceCharging(chargingRequest);

        ChargingTask chargingTask = new ChargingTask(task, chargingStationLatestSchedule);

        return chargingTask;
    }
}
