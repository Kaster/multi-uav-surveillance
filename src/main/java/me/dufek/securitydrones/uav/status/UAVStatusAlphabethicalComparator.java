package me.dufek.securitydrones.uav.status;

import java.util.Comparator;

/**
 *
 * @author Jan Dufek
 */
public class UAVStatusAlphabethicalComparator implements Comparator<Status> {

    @Override
    public int compare(Status status1, Status status2) {
        return status1.getUAVName().compareTo(status2.getUAVName());
    }
}
