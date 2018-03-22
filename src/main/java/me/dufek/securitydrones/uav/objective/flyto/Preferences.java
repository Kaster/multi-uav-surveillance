package me.dufek.securitydrones.uav.objective.flyto;

import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Jan Dufek
 */
public class Preferences {

    /**
     * Everything beyond the threshold is considered to be in infinite distance.
     */
    public static final double SONAR_INFINITY_DISTANCE_THRESHOLD = 4.99d;

    /**
     * Name, linear and lateral velocity coefficient.
     */
    public static final Map<String, Point2D> HIGH_RISK_ACTION = new HashMap<String, Point2D>() {
        {
            put("L4", new math.geom2d.Point2D(-0.95, 0.31));
            put("R4", new math.geom2d.Point2D(-0.95, -0.31));
            put("L3", new math.geom2d.Point2D(-0.81, 0.59));
            put("R3", new math.geom2d.Point2D(-0.81, -0.59));
            put("L2", new math.geom2d.Point2D(-0.59, 0.81));
            put("R2", new math.geom2d.Point2D(-0.59, -0.81));
            put("L1", new math.geom2d.Point2D(-0.31, 0.95));
            put("R1", new math.geom2d.Point2D(-0.31, -0.95));
            put("M0", new math.geom2d.Point2D(-1, 0.31)); // Latereral velocity is needed otherwise it will stuck in front of a obstacle
        }
    };

    /**
     * Name, linear and lateral velocity coefficient.
     */
    public static final Map<CollisionSector, Point2D> UAV_COLLISION_ACTION = new HashMap<CollisionSector, Point2D>() {
        {
            put(CollisionSector.LEFT_4, new math.geom2d.Point2D(-0.95, 0.31));
            put(CollisionSector.RIGHT_4, new math.geom2d.Point2D(-0.95, -0.31));
            put(CollisionSector.LEFT_3, new math.geom2d.Point2D(-0.81, 0.59));
            put(CollisionSector.RIGHT_3, new math.geom2d.Point2D(-0.81, -0.59));
            put(CollisionSector.LEFT_2, new math.geom2d.Point2D(-0.59, 0.81));
            put(CollisionSector.RIGHT_2, new math.geom2d.Point2D(-0.59, -0.81));
            put(CollisionSector.LEFT_1, new math.geom2d.Point2D(-0.31, 0.95));
            put(CollisionSector.RIGHT_1, new math.geom2d.Point2D(-0.31, -0.95));

            put(CollisionSector.FRONT, new math.geom2d.Point2D(-1, 0.31)); // Latereral velocity is needed otherwise two UAVs can get stuck in front of each other

            put(CollisionSector.LEFT_5, new math.geom2d.Point2D(0, -0.21));
            put(CollisionSector.RIGHT_5, new math.geom2d.Point2D(0, 0.21));

            put(CollisionSector.LEFT_6, new math.geom2d.Point2D(0.95, 0.31));
            put(CollisionSector.RIGHT_6, new math.geom2d.Point2D(0.95, -0.31));
            put(CollisionSector.LEFT_7, new math.geom2d.Point2D(0.81, 0.59));
            put(CollisionSector.RIGHT_7, new math.geom2d.Point2D(0.81, -0.59));
            put(CollisionSector.LEFT_8, new math.geom2d.Point2D(0.59, 0.81));
            put(CollisionSector.RIGHT_8, new math.geom2d.Point2D(0.59, -0.81));
            put(CollisionSector.LEFT_9, new math.geom2d.Point2D(0.31, 0.95));
            put(CollisionSector.RIGHT_9, new math.geom2d.Point2D(0.31, -0.95));

            put(CollisionSector.BACK, new math.geom2d.Point2D(1, 0));
        }
    };

    /**
     * Distance thresholds for particular sectors used in dynamic collision
     * avoidance.
     */
    public static final Map<CollisionSector, Double> UAV_COLLISION_DANGER_DISTANCE_THRESHOLD = new HashMap<CollisionSector, Double>() {
        {
            put(CollisionSector.LEFT_4, 12.00);
            put(CollisionSector.RIGHT_4, 12.00);
            put(CollisionSector.LEFT_3, 14.00);
            put(CollisionSector.RIGHT_3, 14.00);
            put(CollisionSector.LEFT_2, 16.00);
            put(CollisionSector.RIGHT_2, 16.00);
            put(CollisionSector.LEFT_1, 18.00);
            put(CollisionSector.RIGHT_1, 18.00);

            put(CollisionSector.FRONT, 20.00);

            put(CollisionSector.LEFT_5, 10.00);
            put(CollisionSector.RIGHT_5, 10.00);

            put(CollisionSector.LEFT_6, 12.00);
            put(CollisionSector.RIGHT_6, 12.00);
            put(CollisionSector.LEFT_7, 14.00);
            put(CollisionSector.RIGHT_7, 14.00);
            put(CollisionSector.LEFT_8, 16.00);
            put(CollisionSector.RIGHT_8, 16.00);
            put(CollisionSector.LEFT_9, 18.00);
            put(CollisionSector.RIGHT_9, 18.00);

            put(CollisionSector.BACK, 20.00);
        }
    };

    /**
     * The distance measured by sonar which is considered to be a high risk.
     */
    public static final Map<String, Double> HIGHT_RISK_DISTANCE = new HashMap<String, Double>() {
        {
            put("L4", 4.00d);
            put("R4", 4.00d);
            put("L3", 4.20d);
            put("R3", 4.20d);
            put("L2", 4.40d);
            put("R2", 4.40d);
            put("L1", 4.60d);
            put("R1", 4.60d);
            put("M0", 4.90d);
        }
    };

    /**
     * The distance measured by sonar which is considered to be a low risk.
     */
    public static final Map<String, Double> LOW_RISK_DISTANCE = new HashMap<String, Double>() {
        {
            put("L4", 4.20d);
            put("R4", 4.20d);
            put("L3", 4.40d);
            put("R3", 4.40d);
            put("L2", 4.60d);
            put("R2", 4.60d);
            put("L1", 4.80d);
            put("R1", 4.80d);
            put("M0", 4.98d);
        }
    };

    /**
     * List of sonars names.
     */
    public static final String[] SONAR_ORDER = new String[]{
        "L4", "L3", "L2", "L1", "M0", "R1", "R2", "R3", "R4"
    };

    /**
     * Threshold to be considered stuck.
     */
    public static final int STUCK_THRESHOLD = 40;

    /**
     * Distance in which the destination is considered reached.
     */
    public static final double DESTINATION_REACHED_THRESHOLD = 2;

    /**
     * After how many no risk counts get back to the original direction after
     * avoiding obstacles.
     */
    public static final double RELEASE_DIVERSION_NO_RISK_COUNT = 6;
    
    /**
     * After how many counts cancel risk.
     */
    public static final double CANCEL_RISK_COUNT_THRESHOLD = 15;

    /**
     * The velocity during avoiding obstacles.
     */
    public static final double OBSTACLE_AVOIDANCE_VELOCITY_COEFICIENT = 40;
    
    public static final double SONAR_MINIMUM_TARGET_DIVERSION = 7;
}
