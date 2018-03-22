package me.dufek.securitydrones.chargingstation.schedule;

import java.util.Comparator;
import me.dufek.securitydrones.uav.charging.ChargingRequest;

/**
 *
 * @author Jan Dufek
 */
public class BatteryDeadComparator implements Comparator<ChargingRequest> {

    @Override
    public int compare(ChargingRequest chargingRequest1, ChargingRequest chargingRequest2) {
        return Double.compare(chargingRequest1.getRequestedTime(), chargingRequest2.getRequestedTime());
    }
    
}