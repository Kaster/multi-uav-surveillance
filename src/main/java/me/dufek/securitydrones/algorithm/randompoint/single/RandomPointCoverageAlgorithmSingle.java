package me.dufek.securitydrones.algorithm.randompoint.single;

import cz.cuni.amis.pogamut.base3d.worldview.object.Location;
import me.dufek.securitydrones.algorithm.Algorithm;
import me.dufek.securitydrones.algorithm.Algorithms;
import me.dufek.securitydrones.algorithm.AreaDivision;
import me.dufek.securitydrones.algorithm.Variability;
import me.dufek.securitydrones.area.Area;
import me.dufek.securitydrones.area.Areas;
import me.dufek.securitydrones.gui.Window;
import me.dufek.securitydrones.uav.destination.Destination;
import me.dufek.securitydrones.uav.destination.Destinations;
import me.dufek.securitydrones.uav.UAV;
import me.dufek.securitydrones.uav.UAVs;
import me.dufek.securitydrones.uav.objective.flyto.FlyTo;
import me.dufek.securitydrones.uav.objective.Takeoff;

/**
 * Random point coverage algorithm single. It selects random point of area's
 * perimeter and assign it to UAV.
 *
 * @author Jan Dufek
 */
public class RandomPointCoverageAlgorithmSingle extends Algorithm {

    public RandomPointCoverageAlgorithmSingle(Window mainWindow, Variability variability) {
        super(mainWindow, variability);
    }

    /**
     * Logic is used to select and assign targets.
     */
    @Override
    public void logic() {
        super.logic();
        for (UAV uav : UAVs.listOfUAVs) {

            if (!uav.isInAir() && !uav.isTakeoff()) {
                assignObjective(uav, Takeoff.class);
            }

            if (uav.hasObjective()) {

            } else {

                if (Areas.noAreas()) {
                    return;
                }

                Area area = Areas.selectArea(Areas.listOfAreas);

                if (area != null) {
                    Location randomPointOnAreaPerimeter = area.getRandomPointOnPerimeter();
                    uav.assignObjective(new FlyTo(randomPointOnAreaPerimeter, "checkpoint"));
                    Destinations.addDestination(new Destination(uav, randomPointOnAreaPerimeter));
                }
            }
        }
    }

    @Override
    public String getName() {
        return Algorithms.RANDOM_POINT_COVERAGE.toString() + " - " + AreaDivision.SINGLE.toString() + " - " + variability.toString();
    }

    @Override
    public void reportUAVAddition(UAV uav) {
        reset();
    }

    @Override
    public void reportUAVDeletion(UAV uav) {
        reset();
    }

    @Override
    public void reportAreaAddition(Area area) {
        reset();
    }

    @Override
    public void reportAreaDeletion(Area area) {
        reset();
    }

    @Override
    public void reportChargingCompleted() {
        if (this.variability == Variability.DYNAMIC) {
            reset();
        }
    }

    private void reset() {
        for (UAV uav : UAVs.listOfUAVs) {
            uav.cancelObjectives();
        }
    }
}
