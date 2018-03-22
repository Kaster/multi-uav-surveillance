package me.dufek.securitydrones.preferences;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * From here user's preferences are managed. They can be loaded from file or
 * restored to default values.
 *
 * @author Jan Dufek
 */
public class UserPreferences {

    private static final String DEFAULT_PREFERENCES_FILE = "Preferences.preferences";

    /**
     * Here all the preferences are stored.
     */
    public static PreferencesFile preferences;

    /**
     * Loads preferences from the file. If there is no file it will load the
     * default preferences.
     */
    public static void load() {
        File file = new File(DEFAULT_PREFERENCES_FILE);

        if (!file.exists()) {
            setDefault();
            save();
        } else {
            loadFromFile(file);
        }
    }

    /**
     * Set preferences to their default value.
     */
    public static void setDefault() {
        preferences = new PreferencesFile();

        // General
        preferences.UNREAL_TOURNAMENT_2004_HOME_FOLDER = me.dufek.securitydrones.main.Preferences.UNREAL_TOURNAMENT_2004_HOME_FOLDER;
        preferences.UNREAL_TOURNAMENT_2004_SCREEN_SHOTS_FOLDER = me.dufek.securitydrones.main.Preferences.UNREAL_TOURNAMENT_2004_SCREEN_SHOTS_FOLDER;
        preferences.DEFAULT_MAP_NAME = me.dufek.securitydrones.main.Preferences.DEFAULT_MAP_NAME;

        // Birdview
        preferences.UNREAL_TOURNAMENT_2004_HORIZONTAL_RESOLUTION = me.dufek.securitydrones.gui.birdview.Preferences.UNREAL_TOURNAMENT_2004_HORIZONTAL_RESOLUTION;
        preferences.UNREAL_TOURNAMENT_2004_VERTICAL_RESOLUTION = me.dufek.securitydrones.gui.birdview.Preferences.UNREAL_TOURNAMENT_2004_VERTICAL_RESOLUTION;
        preferences.UNREAL_TOURNAMENT_2004_HORIZONTAL_FIELD_OF_VIEW = me.dufek.securitydrones.gui.birdview.Preferences.UNREAL_TOURNAMENT_2004_HORIZONTAL_FIELD_OF_VIEW;
        preferences.FLIGHT_HEIGHT = me.dufek.securitydrones.gui.birdview.Preferences.FLIGHT_HEIGHT;

        // Map
        preferences.MAP_DEFAULT_SCALE = me.dufek.securitydrones.gui.Preferences.MAP_DEFAULT_SCALE;
        preferences.MAP_SCALE_IN_FACTOR = me.dufek.securitydrones.gui.Preferences.MAP_SCALE_IN_FACTOR;
        preferences.MAP_SCALE_OUT_FACTOR = me.dufek.securitydrones.gui.Preferences.MAP_SCALE_OUT_FACTOR;
        preferences.MAP_MINIMAL_SCALE = me.dufek.securitydrones.gui.Preferences.MAP_MINIMAL_SCALE;
        preferences.MAP_MAXIMUM_SCALE = me.dufek.securitydrones.gui.Preferences.MAP_MAXIMUM_SCALE;
        preferences.MAP_DRAG_INERTIA_SPEED = me.dufek.securitydrones.gui.Preferences.MAP_DRAG_INERTIA_SPEED;
        preferences.MAP_DRAG_INERTIA_COOLDOWN = me.dufek.securitydrones.gui.Preferences.MAP_DRAG_INERTIA_COOLDOWN;
        preferences.MAP_TIMER_DELAY = me.dufek.securitydrones.gui.Preferences.MAP_TIMER_DELAY;

        // Server
        preferences.SERVER_ADDRESS = me.dufek.securitydrones.server.Preferences.SERVER_ADDRESS;
        preferences.SERVER_PORT = me.dufek.securitydrones.server.Preferences.SERVER_PORT;

        // Simulation
        preferences.SIMULATION_LOGIC_INTERVAL = me.dufek.securitydrones.simulation.Preferences.SIMULATION_LOGIC_INTERVAL;
        preferences.COVEREGE_ANALYSIS_FREQUENCY = me.dufek.securitydrones.simulation.Preferences.COVEREGE_ANALYSIS_FREQUENCY;

        // Algorithm
        preferences.COLLISION_DETECTION_ALTITUDE_DIFFERENCE_THRESHOLD = me.dufek.securitydrones.algorithm.Preferences.COLLISION_DETECTION_ALTITUDE_DIFFERENCE_THRESHOLD;
        preferences.IS_NEAR_DESTINATION_DISTANCE_THRESHOLD = me.dufek.securitydrones.algorithm.Preferences.IS_NEAR_DESTINATION_DISTANCE_THRESHOLD;
        preferences.APPROACH_ANGLE_DIFFERENCE_THRESHOLD = me.dufek.securitydrones.algorithm.Preferences.APPROACH_ANGLE_DIFFERENCE_THRESHOLD;

        // Heat Map
        preferences.CELL_WIDTH = me.dufek.securitydrones.heatmap.Preferences.CELL_WIDTH;
        preferences.CELL_HEIGHT = me.dufek.securitydrones.heatmap.Preferences.CELL_HEIGHT;
        preferences.MAXIMAL_HEAT = me.dufek.securitydrones.heatmap.Preferences.MAXIMAL_HEAT;

        // UAV
        preferences.UAV_RESERVE_FOR_GOING_BACK_TO_CHARGER = me.dufek.securitydrones.uav.Preferences.UAV_RESERVE_FOR_GOING_BACK_TO_CHARGER;
        preferences.UAV_RESERVE_STARTING_CHARGING_PROCEDURE = me.dufek.securitydrones.uav.Preferences.UAV_RESERVE_STARTING_CHARGING_PROCEDURE;
        preferences.UAV_RESERVE_LANDING_TO_CHARGER = me.dufek.securitydrones.uav.Preferences.UAV_RESERVE_LANDING_TO_CHARGER;
        preferences.UAV_RESERVE_RAISING_FROM_CHARGER = me.dufek.securitydrones.uav.Preferences.UAV_RESERVE_RAISING_FROM_CHARGER;
        preferences.UAV_RESERVE_ENDING_CHARGING_PROCEDURE = me.dufek.securitydrones.uav.Preferences.UAV_RESERVE_ENDING_CHARGING_PROCEDURE;
        preferences.MINIMUM_FLIGHT_LEVEL = me.dufek.securitydrones.uav.Preferences.MINIMUM_FLIGHT_LEVEL;
        preferences.MAXIMUM_FLIGHT_LEVEL = me.dufek.securitydrones.uav.Preferences.MAXIMUM_FLIGHT_LEVEL;
        preferences.DISTANCE_BETWEEN_FLIGHT_LEVELS = me.dufek.securitydrones.uav.Preferences.DISTANCE_BETWEEN_FLIGHT_LEVELS;
        preferences.CLICK_ACTIVE_AREA_SIZE = me.dufek.securitydrones.uav.Preferences.CLICK_ACTIVE_AREA_SIZE;
        preferences.SPAWNING_HEIGHT_ABOVE_SPAWN_LEVEL = me.dufek.securitydrones.uav.Preferences.SPAWNING_HEIGHT_ABOVE_SPAWN_LEVEL;
        preferences.SPAWN_BREAK_ALTITUDE = me.dufek.securitydrones.uav.Preferences.SPAWN_BREAK_ALTITUDE;
    }

    private static void loadFromFile(File file) {
        PreferencesFile preferencesFile = null;

        try {
            FileInputStream fileInputStream = new FileInputStream(file.getName());
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            preferencesFile = (PreferencesFile) objectInputStream.readObject();
            objectInputStream.close();
            fileInputStream.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        } catch (ClassNotFoundException exception) {
            exception.printStackTrace();
        }

        UserPreferences.preferences = preferencesFile;
    }

    /**
     * Save preferences to the file.
     */
    public static void save() {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(DEFAULT_PREFERENCES_FILE);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(preferences);
            objectOutputStream.close();
            fileOutputStream.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
