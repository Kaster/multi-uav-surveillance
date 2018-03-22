package me.dufek.securitydrones.battery;

import java.io.Serializable;
import me.dufek.securitydrones.conversion.NumberConversion;

/**
 * Battery is representation of the real battery. It can be drained, charged and
 * it has its maximum capacity.
 *
 * @author Jan Dufek
 */
public class Battery implements Serializable {

    /**
     * Capacity of the battery in milli Ampere hours (mAh).
     */
    private double capacity;
    /**
     * Actual capacity of the battery in milli Ampere hours (mAh).
     */
    private double actualLevel;

    public Battery(double capacity) {
        this.capacity = capacity;
        actualLevel = capacity;
    }

    /**
     * Used to drain battery capacity.
     *
     * @param amperageMean Average amperage.
     */
    public void drain(double amperageMean) {
        double amperageMeanOneLogicMiliAmpers = getAmperageMeanOneLogicMiliAmpers(amperageMean);

        if (actualLevel - amperageMeanOneLogicMiliAmpers <= 0) {
            actualLevel = 0;
        } else {
            actualLevel -= amperageMeanOneLogicMiliAmpers;
        }
    }

    private double getAmperageMeanOneLogicMiliAmpers(double amperageMean) {
        return amperageMean * 10 / 6.0 / 6.0 / 5.0;
    }

    /**
     * Used to charge the battery.
     *
     * @param chargingPerformance Charging performance of the charger.
     */
    public void charge(double chargingPerformance) {
        double chargingPerformanceOneLogicMiliAmpers = getChargingPerformanceOneSimulationLogicMiliAmpers(chargingPerformance);

        if (actualLevel + chargingPerformanceOneLogicMiliAmpers >= capacity) {
            actualLevel = capacity;
        } else {
            actualLevel += chargingPerformanceOneLogicMiliAmpers;
        }
    }

    private double getChargingPerformanceOneSimulationLogicMiliAmpers(double chargingPerformance) {
        return chargingPerformance / 60.0 / 60.0 / 2.0;
    }

    public boolean isEmpty() {
        return actualLevel == 0;
    }

    public boolean isFull() {
        return actualLevel == capacity;
    }

    public double getActualLevel() {
        return actualLevel;
    }

    public int getBatteryPercentage() {
        return NumberConversion.toInteger(actualLevel * 100 / capacity);
    }

    public double getCapacity() {
        return this.capacity;
    }

    public Battery getCopy() {
        Battery batteryCopy = new Battery(this.capacity);
        batteryCopy.actualLevel = this.actualLevel;

        return batteryCopy;
    }
}
