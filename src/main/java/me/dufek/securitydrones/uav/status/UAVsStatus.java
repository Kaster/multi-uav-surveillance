package me.dufek.securitydrones.uav.status;

import java.util.ArrayList;
import java.util.Collections;
import me.dufek.securitydrones.main.Global;
import me.dufek.securitydrones.uav.UAV;

/**
 * The data structure for UAV statuses.
 *
 * @author Jan Dufek
 */
public class UAVsStatus {

    /**
     * List of UAV statuses.
     */
    public static ArrayList<Status> statuses = new ArrayList<Status>();

    public static void setStatus(UAV uav, String statusMessage) {
        Status status = getStatusByUAV(uav);

        if (status == null) {
            createStatus(uav, statusMessage);
        } else {
            status.setStatus(statusMessage);
        }
    }

    private static Status getStatusByUAV(UAV uav) {
        for (Status status : statuses) {
            if (status.getUAV() == uav) {
                return status;
            }
        }

        return null;
    }

    private static void createStatus(UAV uav, String statusMessage) {
        Status status = new Status(uav, statusMessage);
        statuses.add(status);
    }

    public static void showStatusesInUAVsPanel() {
        String statusMessage = getStatusMessage();

        Global.window.rightBar.uavsPanel.setText(statusMessage);
    }

    /**
     * Status message is alphabetically sorted and contains all the objects. It
     * is then printed to the right bar.
     *
     * @return Status message.
     */
    private static String getStatusMessage() {
        Collections.sort(statuses, new UAVStatusAlphabethicalComparator());

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("<html>");

        for (Status status : statuses) {
            stringBuilder.append("<p>").append(status.getUAVName()).append(": ").append(status.getStatusMessage()).append("</p>");
        }

        stringBuilder.append("</body></html>");

        return stringBuilder.toString();
    }

    public static void clear() {
        statuses.clear();
    }

    public static void remove(UAV uav) {
        ArrayList<Status> toRemove = new ArrayList<Status>();

        for (Status status : statuses) {
            if (status.getUAV() == uav) {
                toRemove.add(status);
            }
        }

        statuses.removeAll(toRemove);
    }
}
