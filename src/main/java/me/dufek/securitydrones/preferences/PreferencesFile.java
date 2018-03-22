package me.dufek.securitydrones.preferences;

import java.io.Serializable;

/**
 * Preferences file represent a serializable object which holds all the user's
 * settings and can be serialized to the file.
 *
 * @author Jan Dufek
 */
public class PreferencesFile implements Serializable {

    // General
    public String UNREAL_TOURNAMENT_2004_HOME_FOLDER;
    public String UNREAL_TOURNAMENT_2004_SCREEN_SHOTS_FOLDER;
    public String DEFAULT_MAP_NAME;

    // Birdview
    public double UNREAL_TOURNAMENT_2004_HORIZONTAL_RESOLUTION;
    public double UNREAL_TOURNAMENT_2004_VERTICAL_RESOLUTION;
    public double UNREAL_TOURNAMENT_2004_HORIZONTAL_FIELD_OF_VIEW;
    public double FLIGHT_HEIGHT;

    // Map
    public double MAP_DEFAULT_SCALE;
    public double MAP_SCALE_IN_FACTOR;
    public double MAP_SCALE_OUT_FACTOR;
    public double MAP_MINIMAL_SCALE;
    public double MAP_MAXIMUM_SCALE;
    public double MAP_DRAG_INERTIA_SPEED;
    public double MAP_DRAG_INERTIA_COOLDOWN;
    public int MAP_TIMER_DELAY;

    // Server
    public String SERVER_ADDRESS;
    public int SERVER_PORT;

    // Simulation
    public double SIMULATION_LOGIC_INTERVAL;
    public int COVEREGE_ANALYSIS_FREQUENCY;

    // Algorithm
    public double COLLISION_DETECTION_ALTITUDE_DIFFERENCE_THRESHOLD;
    public double IS_NEAR_DESTINATION_DISTANCE_THRESHOLD;
    public double APPROACH_ANGLE_DIFFERENCE_THRESHOLD;

    // Heat Map
    public double CELL_WIDTH;
    public double CELL_HEIGHT;
    public double MAXIMAL_HEAT;

    // UAV
    public double UAV_RESERVE_FOR_GOING_BACK_TO_CHARGER;
    public double UAV_RESERVE_STARTING_CHARGING_PROCEDURE;
    public double UAV_RESERVE_LANDING_TO_CHARGER;
    public double UAV_RESERVE_RAISING_FROM_CHARGER;
    public double UAV_RESERVE_ENDING_CHARGING_PROCEDURE;
    public double MINIMUM_FLIGHT_LEVEL;
    public double MAXIMUM_FLIGHT_LEVEL;
    public double DISTANCE_BETWEEN_FLIGHT_LEVELS;
    public double CLICK_ACTIVE_AREA_SIZE;
    public double SPAWNING_HEIGHT_ABOVE_SPAWN_LEVEL;
    public double SPAWN_BREAK_ALTITUDE;
}
