package me.dufek.securitydrones.area.status;

import java.util.ArrayList;
import java.util.Collections;
import me.dufek.securitydrones.area.Area;
import me.dufek.securitydrones.main.Global;

/**
 * Statuses of areas to be used in the right bar.
 *
 * @author Jan Dufek
 */
public class AreasStatus {

    /**
     * List of statuses.
     */
    public static ArrayList<Status> statuses = new ArrayList<Status>();

    public static void setStatus(Area area, String statusMessage) {
        Status status = getStatusByArea(area);

        if (status == null) {
            createStatus(area, statusMessage);
        } else {
            status.setStatus(statusMessage);
        }
    }

    private static Status getStatusByArea(Area area) {
        for (Status status : statuses) {
            if (status.getArea() == area) {
                return status;
            }
        }

        return null;
    }

    private static void createStatus(Area area, String statusMessage) {
        Status status = new Status(area, statusMessage);
        statuses.add(status);
    }

    public static void showStatusesInAreasPanel() {
        String statusMessage = getStatusMessage();

        Global.window.rightBar.areasPanel.setText(statusMessage);
    }

    /**
     * Get all the statuses alphabetically sorted.
     *
     * @return Sorted statuses.
     */
    private static String getStatusMessage() {
        Collections.sort(statuses, new ChargingStationStatusAlphabethicalComparator());

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("<html>");

        for (Status status : statuses) {
            stringBuilder.append("<p>").append(status.getAreaName()).append(": ").append(status.getStatusMessage()).append("</p>");
        }

        stringBuilder.append("</body></html>");

        return stringBuilder.toString();
    }

    public static void clear() {
        statuses.clear();
    }

    public static void remove(Area area) {
        ArrayList<Status> toRemove = new ArrayList<Status>();

        for (Status status : statuses) {
            if (status.getArea() == area) {
                toRemove.add(status);
            }
        }

        statuses.removeAll(toRemove);
    }
}
