package me.dufek.securitydrones.chargingstation;

import java.util.ArrayList;
import java.util.Collections;
import me.dufek.securitydrones.changes.Changes;
import me.dufek.securitydrones.main.Global;
import me.dufek.securitydrones.uav.UAV;
import me.dufek.securitydrones.uav.UAVs;

/**
 * Charging stations is a data structure for keeping charging stations.
 *
 * @author Jan Dufek
 */
public class ChargingStations {

    /**
     * List of charging stations.
     */
    public static ArrayList<ChargingStation> listOfChargingStations = new ArrayList<ChargingStation>();

    public static void addChargingStation(ChargingStation chargingStation) {
        listOfChargingStations.add(chargingStation);

        if (Global.simulation != null) {
            for (UAV uav : UAVs.listOfUAVs) {
                uav.resetChargingTask();
            }
        }
    }

    public static void removeChargingStation(ChargingStation chargingStation) {
        listOfChargingStations.remove(chargingStation);

        if (Global.simulation != null) {
            for (UAV uav : UAVs.listOfUAVs) {
                uav.resetChargingTask();
            }
        }
    }

//    public static void removeChargingStation(int indexOfChargingStation) {
//        listOfChargingStations.remove(indexOfChargingStation);
//
//        if (Global.simulation != null) {
//            for (UAV uav : UAVs.listOfUAVs) {
//                uav.resetChargingTask();
//            }
//        }
//    }
    public static int getNumberOfChargingStations() {
        return listOfChargingStations.size();
    }

    public static ChargingStation getChargingStation(int index) {
        return listOfChargingStations.get(index);
    }

    public static void printListOfChargingStations() {
        for (ChargingStation chargingStation : listOfChargingStations) {
            System.out.println(chargingStation.toString());
        }
    }

    public static int getChargingStationIndex(ChargingStation chargingStation) {
        return listOfChargingStations.indexOf(chargingStation);
    }

    public static String getNextDefaultName() {
        for (int i = 0; i <= getNumberOfChargingStations(); i++) {
            String name = "Charging Station " + i;
            if (isNameFree(name)) {
                return name;
            }
        }
        throw new IllegalStateException("No name is free.");
    }

    public static boolean isNameFree(String name) {
        for (ChargingStation chargingStation : listOfChargingStations) {
            if (name.equals(chargingStation.getName())) {
                return false;
            }
        }

        return true;
    }

    public static void clear() {
        listOfChargingStations.clear();
    }

    public static boolean noChargingStations() {
        return listOfChargingStations.isEmpty();
    }

    public static boolean contains(ChargingStation chargingStation) {
        return ChargingStations.listOfChargingStations.contains(chargingStation);
    }

    public static ArrayList<ChargingStation> getListSortedByPreference() {
        Collections.sort(listOfChargingStations, new ChargingStationsComparator());
        Collections.reverse(listOfChargingStations);

        return listOfChargingStations;
    }
}
