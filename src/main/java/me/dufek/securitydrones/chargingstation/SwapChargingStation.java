package me.dufek.securitydrones.chargingstation;

import cz.cuni.amis.pogamut.base3d.worldview.object.Location;
import java.util.ArrayList;
import java.util.Random;
import me.dufek.securitydrones.battery.Battery;
import me.dufek.securitydrones.battery.BatteryDrum;
import me.dufek.securitydrones.chargingstation.schedule.Schedule;
import me.dufek.securitydrones.chargingstation.schedule.Task;
import me.dufek.securitydrones.chargingstation.status.ChargingStationsStatus;
import me.dufek.securitydrones.conversion.NumberConversion;
import me.dufek.securitydrones.utilities.RandomNumber;

/**
 *
 * @author Jan Dufek
 */
public class SwapChargingStation extends ChargingStation {

    private final int numberOfBatteries;
    private BatteryDrum batteryDrum;
    private final double swapTimeMean;
    private final double swapTimeStandardDeviation;
    private final double capacityOfBatteries;

    public SwapChargingStation(Location location, String name, int numberOfBatteries, double capacityOfBatteries, double chargingPerformance, double swapTimeMean, double swapTimeStandardDeviation) {
        super(location, name, chargingPerformance);

        this.numberOfBatteries = numberOfBatteries;
        this.capacityOfBatteries = capacityOfBatteries;

        this.batteryDrum = new BatteryDrum(numberOfBatteries, capacityOfBatteries);

        this.swapTimeMean = swapTimeMean;
        this.swapTimeStandardDeviation = swapTimeStandardDeviation;
    }

    @Override
    public void logic() {
        super.logic();

        batteryDrum.charge(super.getChargingPerformance());
        
        setStatus();        
    }

    private void setStatus() {
        Task earliestTask = super.getEarliestTask();
        String occupied = super.isOccupied() ? "Occupied" : "Free";
        
        String chargingTaskInformation = "";
        
        if (earliestTask != null) {
            chargingTaskInformation = earliestTask.getUAVName() + ". " + NumberConversion.toInteger(earliestTask.getStart()) + " s. ";
        }
        
        String status = chargingTaskInformation + NumberConversion.toInteger(getFreeBatteryCapacity()) + " mAh. " + occupied + ". " + NumberConversion.toInteger(getActualCapacity()) + " mAh.";
        ChargingStationsStatus.setStatus(this, status);
    }

    @Override
    public double getMaximumChargingTime() {
        return swapTimeMean + 2 * swapTimeStandardDeviation;
    }

    @Override
    public double getChargingTime() {
        return RandomNumber.getNormal(swapTimeMean, swapTimeStandardDeviation);
    }

    public double getSwapTimeMean() {
        return this.swapTimeMean;
    }

    public double getSwapTimeStandardDeviation() {
        return this.swapTimeStandardDeviation;
    }

    public BatteryDrum getBatteryDrum() {
        return this.batteryDrum;
    }

    public Battery swapBatteries(Battery oldBattery) {
        Battery newBattery = batteryDrum.takeMostChargedBattery();

        batteryDrum.addBattery(oldBattery);

        return newBattery;
    }

    public void resetBatteryDrum() {
        this.batteryDrum = new BatteryDrum(numberOfBatteries, capacityOfBatteries);
    }

    public double getFreeBatteryCapacity() {
        ArrayList<Battery> batteriesByCapacity = batteryDrum.getBatteriesByCapacity();

        int numberOfTasks = super.getNumberOfIncompleteTasksInSchedule();

        if (numberOfTasks >= batteriesByCapacity.size()) {
            return 0;
        } else {
            return batteriesByCapacity.get(numberOfTasks).getActualLevel();
        }
    }

    public double getActualCapacity() {
        double capacity = 0;

        for (Battery battery : batteryDrum.getBatteriesByCapacity()) {
            capacity += battery.getActualLevel();
        }

        return capacity;
    }

    public SwapChargingStation getCopy() {
        SwapChargingStation chargingStation = new SwapChargingStation(null, ChargingStations.getNextDefaultName(), numberOfBatteries, capacityOfBatteries, super.getChargingPerformance(), swapTimeMean, swapTimeStandardDeviation);
        return chargingStation;
    }
}
