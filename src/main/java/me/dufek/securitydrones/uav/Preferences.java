package me.dufek.securitydrones.uav;

/**
 * Preferences for UAV. Here all the important settings regarding UAV can be
 * set.
 *
 * @author Jan Dufek
 */
public class Preferences {

    /**
     * Default battery capacity in milli Ampere hours (mAh)
     */
    public static final double DEFAULT_BATTERY_CAPACITY = 1350;
    /**
     * Default amperage of UAV in Amperes (A).
     */
    public static final double DEFAULT_AMPERAGE_MEAN = 11;

    /**
     * Factor by which is the time computed to be needed to return to charging
     * station multiplied. It is used because of unpredictable factors as
     * collision avoidance, sensor drift and so on.
     */
    public static final double UAV_RESERVE_FOR_GOING_BACK_TO_CHARGER = 1.2;

    /**
     * Additive constant in second giving the period in which an UAV should
     * arrive above a charging station. An UAV is allowed to arrive anytime
     * within this interval.
     */
    public static final double UAV_RESERVE_STARTING_CHARGING_PROCEDURE = 30;

    /**
     * Factor by which the computed times for landing is multiplied.
     */
    public static final double UAV_RESERVE_LANDING_TO_CHARGER = 1.3;

    /**
     * Factor by which the computed times for take-off is multiplied.
     */
    public static final double UAV_RESERVE_RAISING_FROM_CHARGER = 1.3;

    /**
     * Additive constant in second giving the time added to the end of charing.
     */
    public static final double UAV_RESERVE_ENDING_CHARGING_PROCEDURE = 10;

    /**
     * Maximum altitude above terrain in which UAVs are allowed to ﬂight in
     * order to maintain the quality of surveillance.
     */
    public static final double MINIMUM_FLIGHT_LEVEL = 18;

    /**
     * Minimum altitude where UAVs can flight.
     */
    public static final double MAXIMUM_FLIGHT_LEVEL = 30;

    /**
     * Distance between two flight levels which are next to each other
     */
    public static final double DISTANCE_BETWEEN_FLIGHT_LEVELS = 2;

    /**
     * Default horizontal field of view of the primary sensor.
     */
    public static final double DEFAULT_PRIMARY_SENSOR_HORIZONTAL_FIELD_OF_VIEW = 45;

    /**
     * Default vertical field of view of the primary sensor.
     */
    public static final double DEFAULT_PRIMARY_SENSOR_VERTICAL_FIELD_OF_VIEW = 34;

    /**
     * Linear velocity as a percentage of maximum linear velocity.
     */
    public static final double DEFAULT_LINEAR_VELOCITY = 40;

    /**
     * Altitude velocity as a percentage of maximum altitude velocity.
     */
    public static final double DEFAULT_ALTITUDE_VELOCITY = 25;

    /**
     * Rotational velocity as in radians per second.
     */
    public static final double DEFAULT_ROTATIONAL_VELOCITY = 1.5708 / 2;

    /**
     * Circular area around UAV’s visual representation given in pixels where if
     * a click is performed it is considered as click on the UAV.
     */
    public static final double CLICK_ACTIVE_AREA_SIZE = 50;

    /**
     * Altitude above average terrain from which UAVs are spawned.
     */
    public static final double SPAWNING_HEIGHT_ABOVE_SPAWN_LEVEL = 30;

    /**
     * Altitude above terrain in which an UAV is considered to be spawned, slows
     * down and is woken up.
     */
    public static final double SPAWN_BREAK_ALTITUDE = 5;

    /**
     * Number of laser rays on the bottom of UAV.
     */
    public static final int NUMBER_OF_LASER_RAYS = 10;
}
