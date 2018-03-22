package me.dufek.securitydrones.simulation;

import me.dufek.securitydrones.conversion.NumberConversion;
import me.dufek.securitydrones.preferences.UserPreferences;

/**
 * The preference of the simulator.
 *
 * @author Jan Dufek
 */
public class Preferences {

    /**
     * How often the simulation logic should executed.
     */
    public static final double SIMULATION_LOGIC_INTERVAL = 0.2;

    /**
     * How often measured in number of logic cycles the area coverage should be
     * computed.
     */
    public static final int COVEREGE_ANALYSIS_FREQUENCY = 5;

    /**
     * Gets length of one simulation logic interval in milli seconds.
     * 
     * @return Logic interval in milli seconds.
     */
    public static int getSimulationLogicIntervalMiliSeconds() {
        return NumberConversion.toInteger(UserPreferences.preferences.SIMULATION_LOGIC_INTERVAL * 1000);
    }
    
    /**
     * Gets length of one simulation logic interval in seconds.
     * 
     * @return Logic interval in seconds.
     */
    public static double getSimulationLogicIntervalSeconds() {
        return UserPreferences.preferences.SIMULATION_LOGIC_INTERVAL;
    }
}
