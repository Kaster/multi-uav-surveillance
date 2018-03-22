package me.dufek.securitydrones.utilities;

import me.dufek.securitydrones.uav.UAV;
import me.dufek.securitydrones.uav.UAVs;
import me.dufek.securitydrones.uav.request.UAVRequest;
import me.dufek.securitydrones.uav.request.UAVRequests;

/**
 *
 * @author Jan Dufek
 */
public class VisibleArea {
        public static double getMinimumVisibleAreaWidth() {
        double minimumWidth = Double.MAX_VALUE;

        for (UAVRequest uavRequest : UAVRequests.getList()) {
            if (uavRequest.isFlightLevelSet()) {
                uavRequest.setFlightLevel();
            }

            if (uavRequest.getVisibleAreaDimension().getWidth() < minimumWidth) {
                minimumWidth = uavRequest.getVisibleAreaDimension().getWidth();
            }
        }

        for (UAV uav : UAVs.listOfUAVs) {
            if (uav.getUAVRequest().getVisibleAreaDimension().getWidth() < minimumWidth) {
                minimumWidth = uav.getUAVRequest().getVisibleAreaDimension().getWidth();
            }
        }

        return minimumWidth;
    }
}
