package me.dufek.securitydrones.algorithm;

/**
 * Preferences for algorithms.
 *
 * @author Jan Dufek
 */
public class Preferences {

    /**
     * Threshold for vertical distance between UAVs to consider them in similar
     * altitude (higher risk of collision).
     */
    public final static double COLLISION_DETECTION_ALTITUDE_DIFFERENCE_THRESHOLD = 2;

    /**
     * Threshold for the distance of UAV from its destination. If the distance
     * is less an UAV is considered to be near its destination. The movement
     * characteristics will change if the UAV is near its destination to allow
     * exact maneuvering.
     */
    public final static double APPROACH_ANGLE_DIFFERENCE_THRESHOLD = 10;

    /**
     * the threshold for angle to destination. If an UAV is near its destination
     * the angle difference must be lower than this threshold. If not an UAV must
     * stop linear movement and rotate to its destination. This prevents cycling
     * around a destination.
     */
    public final static double IS_NEAR_DESTINATION_DISTANCE_THRESHOLD = 10;
}
