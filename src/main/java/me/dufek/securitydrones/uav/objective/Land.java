package me.dufek.securitydrones.uav.objective;

import cz.cuni.amis.pogamut.usar2004.communication.messages.usarcommands.DriveAerial;
import me.dufek.securitydrones.logger.Logger;
import me.dufek.securitydrones.main.Global;
import me.dufek.securitydrones.uav.Preferences;
import me.dufek.securitydrones.uav.State;
import me.dufek.securitydrones.uav.status.UAVsStatus;

/**
 * Land objective can be used to successfully land.
 *
 * @author Jan Dufek
 */
public class Land extends Objective {

    /**
     * Initialization.
     */
    public Land() {
        super();

        this.name = "Landing";
    }

    /**
     * Logic called periodically is responsible for decreasing elevation.
     */
    @Override
    public void logic() {
        super.logic();

        super.uav.actualAltitude = super.uav.laser.getNMidAvg(Preferences.NUMBER_OF_LASER_RAYS);

        if (Global.algorithm != null) {
            super.uav.getAct().act(new DriveAerial(-super.uav.maxAltitudeVelocity * super.uav.altitudeVelocity, 0, 0, 0, false));
        }

        if (Math.abs(super.uav.actualAltitude) < 0.5) {
            Logger.log(uav.getName() + ": Landed.");
            UAVsStatus.setStatus(uav, "Landed.");
            super.uav.state = State.LANDED;

            if (Global.algorithm != null) {
                super.uav.getAct().act(new DriveAerial(0, 0, 0, 0, false));
            }

            super.satisfy();
        }
    }
}
