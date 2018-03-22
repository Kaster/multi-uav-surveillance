package me.dufek.securitydrones.battery;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import me.dufek.securitydrones.chargingstation.ChargingStation;
import me.dufek.securitydrones.main.Global;

/**
 * Battery drum is a battery stack inside charging station. It can hold numerous
 * batteries and all are being charged simultaneously.
 *
 * @author Jan Dufek
 */
public class BatteryDrum implements Serializable {

    /**
     * List of batteries.
     */
    private ArrayList<Battery> batteries;

    public BatteryDrum() {
        batteries = new ArrayList<Battery>();
    }

    public BatteryDrum(int numberOfBatteries, double capacity) {
        batteries = new ArrayList<Battery>();

        for (int i = 0; i < numberOfBatteries; i++) {
            batteries.add(new Battery(capacity));
        }
    }

    /**
     * Get list of batteries sorted by their capacity.
     *
     * @return Sorted list of batteries.
     */
    public ArrayList<Battery> getBatteriesByCapacity() {
        sortByCapacityDescending();
        return batteries;
    }

    /**
     * Gets the battery with highest available capacity.
     * 
     * @return Battery with highest available capacity.
     */
    public Battery takeMostChargedBattery() {
        sortByCapacityDescending();

        Battery mostChargedBattery = batteries.get(0);

        batteries.remove(mostChargedBattery);

        if (Global.window.bottomBar.actualComponent instanceof ChargingStation) {
            Global.window.bottomBar.bottomBarChargingStation.batteryDrum.refresh();
        }

        return mostChargedBattery;
    }

    public void addBattery(Battery battery) {
        batteries.add(battery);

        sortByCapacityDescending();

        if (Global.window.bottomBar.actualComponent instanceof ChargingStation) {
            Global.window.bottomBar.bottomBarChargingStation.batteryDrum.refresh();
        }
    }

    private void sortByCapacityDescending() {
        Collections.sort(batteries, new BatteryActualCapacityComparator());
        Collections.reverse(batteries);
    }

    public void charge(double chargingPerformance) {
        for (Battery battery : batteries) {
            battery.charge(chargingPerformance);
        }
    }
}
