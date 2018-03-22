package me.dufek.securitydrones.chargingstation.status;

import java.util.ArrayList;
import java.util.Collections;
import me.dufek.securitydrones.chargingstation.ChargingStation;
import me.dufek.securitydrones.main.Global;

/**
 * Statuses of charging station used by the right bar.
 *
 * @author Jan Dufek
 */
public class ChargingStationsStatus {

    public static ArrayList<Status> statuses = new ArrayList<Status>();

    public static void setStatus(ChargingStation chargingStation, String statusMessage) {
        Status status = getStatusByChargingStation(chargingStation);

        if (status == null) {
            createStatus(chargingStation, statusMessage);
        } else {
            status.setStatus(statusMessage);
        }
    }

    private static Status getStatusByChargingStation(ChargingStation chargingStation) {
        for (Status status : statuses) {
            if (status.getChargingStation() == chargingStation) {
                return status;
            }
        }

        return null;
    }

    private static void createStatus(ChargingStation chargingStation, String statusMessage) {
        Status status = new Status(chargingStation, statusMessage);
        statuses.add(status);
    }

    public static void showStatusesInChargingStationsPanel() {
        String statusMessage = getStatusMessage();

        Global.window.rightBar.chargingStationsPanel.setText(statusMessage);
    }

    private static String getStatusMessage() {
        Collections.sort(statuses, new ChargingStationStatusAlphabethicalComparator());

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("<html>");

        for (Status status : statuses) {
            stringBuilder.append("<p>").append(status.getChargingStationName()).append(": ").append(status.getStatusMessage()).append("</p>");
        }

        stringBuilder.append("</body></html>");

        return stringBuilder.toString();
    }

    public static void clear() {
        statuses.clear();
    }

    public static void remove(ChargingStation chargingStation) {
        ArrayList<Status> toRemove = new ArrayList<Status>();

        for (Status status : statuses) {
            if (status.getChargingStation() == chargingStation) {
                toRemove.add(status);
            }
        }

        statuses.removeAll(toRemove);
    }
}
