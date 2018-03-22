package me.dufek.securitydrones.gui.birdview;

import cz.cuni.amis.pogamut.base.communication.worldview.object.WorldObjectId;
import cz.cuni.amis.pogamut.ut2004.communication.messages.gbinfomessages.NavPoint;
import java.io.Serializable;
import java.util.Map;

/**
 * Represents boundaries of the Unreal Tournament 2004 map in 3D space. It uses
 * Unreal Units as the units of distance.
 *
 * @author Jan Dufek
 */
public class MapBoundaries implements Serializable {

    /**
     * X axis of the map.
     */
    public Axis x;
    /**
     * Y axis of the map.
     */
    public Axis y;
    /**
     * Z axis of the map.
     */
    public Axis z;
    /**
     * Average elevation of the ground.
     */
    public double averageElevation;

    // Width of one fragment of bird view
    public double fragmentWidth = 0;
    // Height of one fragment of bird view
    public double fragmentHeight = 0;
    // Number of columns of fragments of bird view
    public int numberOfColumnsOfFragments = 0;
    // Number of rows of fragments of bird view
    public int numberOfRowsOfFragments = 0;

    public MapBoundaries(ControlConnection controlConnection) {
        initialize();
        getMapBoundaries(controlConnection);
    }

    /**
     * Initialize boundaries to maximum and minimum values.
     */
    private void initialize() {
        // Start with unlimitted boundaries
        x = new Axis(Double.MAX_VALUE, - Double.MAX_VALUE);
        y = new Axis(Double.MAX_VALUE, - Double.MAX_VALUE);
        z = new Axis(Double.MAX_VALUE, - Double.MAX_VALUE);

        averageElevation = 0;
    }

    /**
     * Finds out boundaries of the current map. It uses all navigation points in
     * the map to update those boundaries.
     */
    private void getMapBoundaries(ControlConnection controlConnection) {
        Map<WorldObjectId, NavPoint> navigationPoints = controlConnection.getWorldView().getAll(NavPoint.class);

        for (NavPoint navigationPoint : navigationPoints.values()) {
            double x = navigationPoint.getLocation().x;
            this.x.updateBoundaries(x);

            double y = navigationPoint.getLocation().y;
            this.y.updateBoundaries(y);

            double z = navigationPoint.getLocation().z;
            this.z.updateBoundaries(z);
            
            averageElevation += z;
        }

        averageElevation /= navigationPoints.size();
    }
}
