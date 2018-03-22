package me.dufek.securitydrones.uav;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import me.dufek.securitydrones.time.Time;
import me.dufek.securitydrones.uav.request.UAVRequest;

/**
 * The data structure for keeping references to all the UAVs.
 *
 * @author Jan Dufek
 */
public class UAVs {

    /**
     * List of UAVs.
     */
    public static List<UAV> listOfUAVs = Collections.synchronizedList(new ArrayList<UAV>());

    /**
     * Add new UAV.
     * 
     * @param uav UAV
     */
    public static void addUAV(UAV uav) {
        listOfUAVs.add(uav);
    }

    public static void removeUAV(UAV uav) {
        listOfUAVs.remove(uav);
    }

    public static void removeUAV(int indexOfUav) {
        listOfUAVs.remove(indexOfUav);
    }

    public static int getNumberOfUAVs() {
        return listOfUAVs.size();
    }

    public static UAV getUAV(int index) {
        return listOfUAVs.get(index);
    }

    public static void printListOfUAVs() {
        for (UAV uav : listOfUAVs) {
            System.out.println(uav.toString());
        }
    }

    public static int getUAVIndex(UAV uav) {
        return listOfUAVs.indexOf(uav);
    }

    public static String getNextDefaultName() {
        return "UAV " + getNumberOfUAVs();
    }

    public static void clear() {
        listOfUAVs.clear();
    }

    public static boolean noUAVs() {
        return listOfUAVs.isEmpty();
    }

    public static void cooldownVisibleArea() {
        for (UAV uav : listOfUAVs) {
            uav.cooldownVisibleArea();
        }
    }

    private static UAV getFirstUAV() {
        if (noUAVs()) {
            return null;
        } else {
            return listOfUAVs.get(0);
        }
    }

    public static void saveActualTime() {
        if (!noUAVs()) {
            Time.updateTime(getFirstUAV().getTime());
        }
    }

    public static UAV getUAVByUAVRequest(UAVRequest uavRequest) {
        for (UAV uav : listOfUAVs) {
//            System.out.println(uav.getUAVRequest().toString() + " " + uavRequest.toString());
            if (uav.getUAVRequest() == uavRequest) {
                return uav;
            }
        }

        throw new IllegalStateException("No UAV to given UAV request found.");
    }
}
