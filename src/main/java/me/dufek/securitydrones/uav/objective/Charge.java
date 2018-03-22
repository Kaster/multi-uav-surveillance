package me.dufek.securitydrones.uav.objective;

import me.dufek.securitydrones.uav.charging.ChargingTask;
import me.dufek.securitydrones.battery.Battery;
import me.dufek.securitydrones.chargingstation.SwapChargingStation;
import me.dufek.securitydrones.main.Global;
import me.dufek.securitydrones.uav.status.UAVsStatus;

/**
 * Charge objective is used for battery swap. It is executed while on the
 * charger.
 *
 * @author Jan Dufek
 */
public class Charge extends Objective {

    /**
     * Charging task.
     */
    private final ChargingTask chargingTask;

    /**
     * Charging station.
     */
    private final SwapChargingStation chargingStation;

    /**
     * Charging time.
     */
    private double chargingTime;

    /**
     * The time when charging finishes.
     */
    private double chargingFinishedTime;

    /**
     * Initialization of this objective.
     *
     * @param chargingTask Charging task.
     */
    public Charge(ChargingTask chargingTask) {
        super();

        this.name = "Charging";
        this.chargingTask = chargingTask;
        this.chargingStation = (SwapChargingStation) chargingTask.getChargingStation();

    }

    /**
     * Called once at the beginning. Responsible for determination of charging
     * finished time.
     */
    @Override
    public void beforeLogic() {
        super.beforeLogic();

        chargingStation.setOccupied(true);

        chargingTime = chargingStation.getChargingTime();
        chargingFinishedTime = uav.time + chargingTime;

        resetSensoryModule();
    }

    /**
     * Logic executed periodically. Here responsible for swapping the batteries.
     */
    @Override
    public void logic() {
        super.logic();

        if (chargingFinishedTime < uav.time) {
            UAVsStatus.setStatus(uav, "Charging completed.");

            Battery oldBattery = super.uav.battery.getCopy();
            super.uav.battery = null;
            Battery newBattery = chargingStation.swapBatteries(oldBattery);
            super.uav.chargingTask.getTask().setCompleted();

            super.uav.battery = newBattery;

            super.uav.increaseNumberOfBatteryChanges();

            uav.chargingTask = null;

            super.satisfy();

            chargingStation.setOccupied(false);

            Global.algorithm.reportChargingCompleted();
        }
    }

    /**
     * This methods resets localization by correcting its location since the
     * location of charging station is known.
     */
    private void resetSensoryModule() {
        super.uav.resetINS();
    }
}
