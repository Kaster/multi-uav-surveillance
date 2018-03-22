package me.dufek.securitydrones.chargingstation.status;

import java.util.Comparator;

/**
 *
 * @author Jan Dufek
 */
public class ChargingStationStatusAlphabethicalComparator implements Comparator<Status> {

    @Override
    public int compare(Status status1, Status status2) {
        return status1.getChargingStationName().compareTo(status2.getChargingStationName());
    }
}
