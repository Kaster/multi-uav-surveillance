package me.dufek.securitydrones.uav.request;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import me.dufek.securitydrones.uav.UAV;
import me.dufek.securitydrones.uav.UAVs;
import static me.dufek.securitydrones.uav.UAVs.getNumberOfUAVs;

/**
 * UAV requests is used to store all the UAV requests.
 *
 * @author Jan Dufek
 */
public class UAVRequests {

    /**
     * List of UAV requests.
     */
    private static List<UAVRequest> listOfUAVs = Collections.synchronizedList(new ArrayList<UAVRequest>());

    public static void addUAV(UAVRequest uav) {
//        uav.setFlightLevel();
        listOfUAVs.add(uav);
    }

    public static void removeUAV(UAVRequest uav) {
        listOfUAVs.remove(uav);
    }

//    public static void removeUAV(int indexOfUav) {
//        listOfUAVs.remove(indexOfUav);
//    }
    public static int getNumberOfUAVs() {
        return listOfUAVs.size();
    }

    public static UAVRequest getUAV(int index) {
        return listOfUAVs.get(index);
    }

    public static void printListOfUAVs() {
        for (UAVRequest uav : listOfUAVs) {
            System.out.println(uav.toString());
        }
    }

    public static int getUAVIndex(UAVRequest uav) {
        return listOfUAVs.indexOf(uav);
    }

    public static boolean noRequest() {
        return listOfUAVs.isEmpty();
    }

    public static boolean requestPending() {
        return !noRequest();
    }

    public static void clear() {
        listOfUAVs.clear();
    }

    public static String getNextDefaultName() {
        for (int i = 0; i <= getNumberOfUAVs() + UAVs.getNumberOfUAVs(); i++) {
            String name = "UAV " + i;
            if (isNameFree(name)) {
                return name;
            }
        }
        throw new IllegalStateException("No name is free.");
//        return "UAV " + (getNumberOfUAVs() + UAVs.getNumberOfUAVs());
    }

    public static boolean isNameFree(String name) {
        for (UAVRequest uAVRequest : listOfUAVs) {
            if (name.equals(uAVRequest.getName())) {
                return false;
            }
        }

        for (UAV uav : UAVs.listOfUAVs) {
            if (name.equals(uav.getName())) {
                return false;
            }
        }

        return true;
    }

    public static void setList(ArrayList<UAVRequest> list) {
        UAVRequests.listOfUAVs = list;
    }

    public static List<UAVRequest> getList() {
        return UAVRequests.listOfUAVs;
    }

    public static boolean contains(UAVRequest uavRequest) {
        return UAVRequests.listOfUAVs.contains(uavRequest);
    }
}
