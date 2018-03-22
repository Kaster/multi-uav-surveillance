package me.dufek.securitydrones.area.status;

import java.util.Comparator;

/**
 *
 * @author Jan Dufek
 */
public class ChargingStationStatusAlphabethicalComparator implements Comparator<Status> {

    @Override
    public int compare(Status status1, Status status2) {
        return status1.getAreaName().compareTo(status2.getAreaName());
    }
}
