package me.dufek.securitydrones.uav.objective;

import cz.cuni.amis.pogamut.usar2004.communication.messages.usarcommands.DriveAerial;
import me.dufek.securitydrones.logger.Logger;
import me.dufek.securitydrones.main.Global;
import me.dufek.securitydrones.uav.Preferences;
import me.dufek.securitydrones.uav.State;
import me.dufek.securitydrones.uav.status.UAVsStatus;

/**
 * Takeoff objective can be used to successfully take-off.
 *
 * @author Jan Dufek
 */
public class Takeoff extends Objective {

    /**
     * Initialization.
     */
    public Takeoff() {
        super();

        this.name = "Takeoff";
    }

    /**
     * Used to set up take-off state.
     */
    @Override
    public void beforeLogic() {
        super.beforeLogic();

        super.uav.state = State.TAKE_OFF;
    }

    /**
     * Logic called periodically is responsible for increase elevation.
     */
    @Override
    public void logic() {
        super.logic();

        if (super.uav.flightAltitude == 0) {
            return;
        }

        super.uav.actualAltitude = super.uav.laser.getNMidAvg(Preferences.NUMBER_OF_LASER_RAYS);

        if (Global.algorithm != null) {
            super.uav.getAct().act(new DriveAerial(super.uav.maxAltitudeVelocity * super.uav.altitudeVelocity, 0, 0, 0, false));
        }

        if (super.uav.flightAltitude - super.uav.actualAltitude < 0) {
            Logger.log(uav.getName() + ": In air.");
            UAVsStatus.setStatus(uav, "In air.");
            super.uav.state = State.IN_AIR;

            if (Global.algorithm != null) {
                super.uav.getAct().act(new DriveAerial(0, 0, 0, 0, false));
            }

            super.satisfy();
        }
    }
}
