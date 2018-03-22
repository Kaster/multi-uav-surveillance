package me.dufek.securitydrones.uav.flightlevel;

import me.dufek.securitydrones.preferences.UserPreferences;
import me.dufek.securitydrones.uav.Preferences;
import me.dufek.securitydrones.uav.UAV;
import me.dufek.securitydrones.uav.UAVs;
import me.dufek.securitydrones.uav.request.UAVRequest;
import me.dufek.securitydrones.uav.request.UAVRequests;

/**
 * Flight levels manage all the flight levels used by UAVs. It is responsible
 * for assigning charging stations to UAV requests.
 *
 * @author Jan Dufek
 */
public class FlightLevels {

//    private static int actualFlightLevel = 0;
    /**
     * Get the fittest flight level for given UAV request.
     *
     * @param uavRequest UAV request.
     * @return Flight level.
     */
    public static int getNextFlightLevel(UAVRequest uavRequest) {

        int lowestFlightLevel = (int) Math.floor((UserPreferences.preferences.MAXIMUM_FLIGHT_LEVEL - UserPreferences.preferences.MINIMUM_FLIGHT_LEVEL) / UserPreferences.preferences.DISTANCE_BETWEEN_FLIGHT_LEVELS);

        int flightLevelWithminimumNumberOfUAVs = Integer.MAX_VALUE;
        int minimumNumberOfUAVs = Integer.MAX_VALUE;

        for (int i = 0; i <= lowestFlightLevel; i++) {
            int numberOfUAVs = getNumberOfUAVsOnLevel(i, uavRequest);
            if (numberOfUAVs < minimumNumberOfUAVs) {
                flightLevelWithminimumNumberOfUAVs = i;
                minimumNumberOfUAVs = numberOfUAVs;
            }
        }
//        System.out.println(flightLevelWithminimumNumberOfUAVs);
//        System.out.println(minimumNumberOfUAVs);
        return flightLevelWithminimumNumberOfUAVs;

//        // If none is free, get the one with less UAVs
//        if (UserPreferences.preferences.MAXIMUM_FLIGHT_LEVEL - actualFlightLevel * UserPreferences.preferences.DISTANCE_BETWEEN_FLIGHT_LEVELS < UserPreferences.preferences.MINIMUM_FLIGHT_LEVEL) {
//            actualFlightLevel = 0;
//        }
//
//        int flightLevel = actualFlightLevel;
//        actualFlightLevel++;
//
//        return flightLevel;
    }

    public static int getNumberOfUAVsOnLevel(int level, UAVRequest uavRequest) {
        int numberOfUAVs = 0;

        for (UAV uav : UAVs.listOfUAVs) {
            if (uav.getFlightLevelNumber() == level) {
                numberOfUAVs++;
            }
        }

        for (UAVRequest otherUAVRequest : UAVRequests.getList()) {
            if (otherUAVRequest == uavRequest) {
                continue;
            }

            Integer otherUAVRequestFlightLevel = otherUAVRequest.getFlightLevel();

            if (otherUAVRequestFlightLevel == null) {
                continue;
            }

            if (otherUAVRequest.getFlightLevel() == level) {
                numberOfUAVs++;
            }
        }

        return numberOfUAVs;
    }

//    public static boolean isFree(int number) {
//        for (UAV uav : UAVs.listOfUAVs) {
//            if (uav.getFlightLevel() == number) {
//                return false;
//            }
//        }
//
//        return true;
//    }
//    public static void clear() {
//        actualFlightLevel = 0;
//    }
//
//    public static int getNumberOfFlightLevels() {
//        return (int) Math.floor((UserPreferences.preferences.MAXIMUM_FLIGHT_LEVEL - UserPreferences.preferences.MINIMUM_FLIGHT_LEVEL) / UserPreferences.preferences.DISTANCE_BETWEEN_FLIGHT_LEVELS) + 1;
//    }
//
//    private double getFlightAltitude(int flightLevel) {
//        return Preferences.MAXIMUM_FLIGHT_LEVEL - flightLevel * Preferences.DISTANCE_BETWEEN_FLIGHT_LEVELS;
//    }
//
//    public double getMinimumVisibleAreaWidth(int numberOfUAVs) {
//        int numberOfFlightLevels = FlightLevels.getNumberOfFlightLevels();
//
//        int minimumFlightLevel;
//
//        if (numberOfFlightLevels < numberOfUAVs) {
//            minimumFlightLevel = numberOfFlightLevels;
//        } else {
//            minimumFlightLevel = numberOfUAVs % numberOfFlightLevels;
//        }
//        
//        
//        double flightAltitude = getFlightAltitude(minimumFlightLevel);
//        
//                double width = Math.tan(Math.toRadians(getPrimarySensorHorizontalFieldOfView() / 2)) * flightAltitude * 2;
//
//        Dimension dimension = new Dimension(width, height);
//    }
}
