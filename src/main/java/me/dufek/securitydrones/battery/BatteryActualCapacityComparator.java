package me.dufek.securitydrones.battery;

import java.util.Comparator;

/**
 *
 * @author Jan Dufek
 */
public class BatteryActualCapacityComparator implements Comparator<Battery> {

    @Override
    public int compare(Battery battery1, Battery battery2) {
        return Double.compare(battery1.getActualLevel(), battery2.getActualLevel());
    }
}
