package me.dufek.securitydrones.chargingstation;

import java.util.Comparator;

/**
 *
 * @author Jan Dufek
 */
public class ChargingStationsComparator implements Comparator<ChargingStation> {

    @Override
    public int compare(ChargingStation chargingStation1, ChargingStation chargingStation2) {
        int result = Double.compare(((SwapChargingStation) chargingStation1).getFreeBatteryCapacity(), ((SwapChargingStation) chargingStation2).getFreeBatteryCapacity());
        
        if (result != 0) {
            return result;
        } else {
            return Double.compare(((SwapChargingStation) chargingStation1).getActualCapacity(), ((SwapChargingStation) chargingStation2).getActualCapacity());
        }

//        return Double.compare(((SwapChargingStation) chargingStation1).getFreeBatteryCapacity(), ((SwapChargingStation) chargingStation2).getFreeBatteryCapacity());
    }
}
