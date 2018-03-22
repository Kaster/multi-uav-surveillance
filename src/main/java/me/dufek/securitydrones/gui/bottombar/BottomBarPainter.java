package me.dufek.securitydrones.gui.bottombar;

import cz.cuni.amis.pogamut.base3d.worldview.object.Location;
import cz.cuni.amis.pogamut.base3d.worldview.object.Rotation;
import me.dufek.securitydrones.activeobject.ActiveObject;
import me.dufek.securitydrones.area.Area;
import me.dufek.securitydrones.main.Global;
import me.dufek.securitydrones.chargingstation.ChargingStation;
import me.dufek.securitydrones.chargingstation.SwapChargingStation;
import me.dufek.securitydrones.gui.bottombar.area.BottomBarArea;
import me.dufek.securitydrones.gui.bottombar.chargingstation.BottomBarChargingStation;
import me.dufek.securitydrones.gui.bottombar.uav.BottomBarUAV;
import me.dufek.securitydrones.uav.UAV;
import me.dufek.securitydrones.uav.request.UAVRequest;

/**
 *
 * @author Jan Dufek
 */
public class BottomBarPainter {

    public static void paint() {
        if (ActiveObject.isSelected()) {

            if (ActiveObject.get() instanceof ChargingStation) {
                paintChargingStationBar();
            } else if (ActiveObject.get() instanceof UAV) {
                paintUAVBar();
            } else if (ActiveObject.get() instanceof UAVRequest) {
                paintUAVRequestBar();
            } else if (ActiveObject.get() instanceof Area) {
                paintAreaBar();
            }

            Global.window.bottomBar.setVisible(true);
        } else {
            Global.window.bottomBar.setVisible(false);
        }
    }

    private static void paintChargingStationBar() {
        if (!(Global.window.bottomBar.actualComponent instanceof BottomBarChargingStation)) {
            Global.window.bottomBar.create();
        }

        SwapChargingStation chargingStation = (SwapChargingStation) ActiveObject.get();
        Global.window.bottomBar.bottomBarChargingStation.setTitle(chargingStation.getName());
        Global.window.bottomBar.bottomBarChargingStation.setGeneralInformation(chargingStation.isOccupied(), chargingStation.getLocation(), chargingStation.getChargingPerformance(), chargingStation.getSwapTimeMean(), chargingStation.getSwapTimeStandardDeviation());
        Global.window.bottomBar.bottomBarChargingStation.setSchedule(chargingStation.getSchedule());
        Global.window.bottomBar.bottomBarChargingStation.setBatteryDrum(chargingStation.getBatteryDrum());
    }

    private static void paintUAVBar() {
        if (!(Global.window.bottomBar.actualComponent instanceof BottomBarUAV)) {
            Global.window.bottomBar.create();
        }

        UAV uav = (UAV) ActiveObject.get();
        Global.window.bottomBar.bottomBarUAV.setTitle(uav.getName());
        Global.window.bottomBar.bottomBarUAV.setGeneralInformation(uav.getStartLocation(), uav.getStartTime(), uav.getUpTime(), uav.getFlightLevel(), uav.getState());
        Global.window.bottomBar.bottomBarUAV.setPosition(uav.getActualAltitude(), uav.getActualRotation(), uav.getActualLocationMapCoordinates(), uav.getRealLocationMapCoordinates());
        Global.window.bottomBar.bottomBarUAV.setVelocity(uav.getActualVelocity(), uav.getLinearVelocity(), uav.getAltitudeVelocity(), uav.getRotationalVelocity(), uav.getMaximumLinearVelocity(), uav.getMaximumAltitudeVelocity(), uav.getMaximumRotationalVelocity());
        Global.window.bottomBar.bottomBarUAV.setPrimarySensor(uav.getPrimarySensorHorizontalFieldOfView(), uav.getPrimarySensorVerticalFieldOfView(), uav.getVisibleAreaWidth(), uav.getVisibleAreaHeight());
        Global.window.bottomBar.bottomBarUAV.setObjectives(uav.getObjectivesStringList());
        Global.window.bottomBar.bottomBarUAV.setBattery(uav.getBattery(), uav.getBatteryEmptyTime(), uav.getBatteryRemainingTime(), uav.getNumberOfBatteryChanges(), uav.getAmperageMean());
        Global.window.bottomBar.bottomBarUAV.setCharging(uav.getChargingStart(), uav.getChargingEnd(), uav.getChargingStationName(), uav.getDistanceToChargingStation(), uav.getFlyingTimeToChargingStation());
        Global.window.bottomBar.bottomBarUAV.setSonars(uav.getSonars());
    }

    private static void paintUAVRequestBar() {
        if (!(Global.window.bottomBar.actualComponent instanceof BottomBarUAV)) {
            Global.window.bottomBar.create();
        }

        UAVRequest uavRequest = (UAVRequest) ActiveObject.get();
        Global.window.bottomBar.bottomBarUAV.setTitle(uavRequest.getName());
        Global.window.bottomBar.bottomBarUAV.setGeneralInformation(uavRequest.getStartLocation(), null, null, null, null);
        Global.window.bottomBar.bottomBarUAV.setPosition(null, uavRequest.getRotation(), uavRequest.getStartLocation(), null);
        Global.window.bottomBar.bottomBarUAV.setVelocity(null, uavRequest.getLinearVelocity(), uavRequest.getAltitudeVelocity(), uavRequest.getRotationalVelocity(), null, null, null);
        Global.window.bottomBar.bottomBarUAV.setPrimarySensor(uavRequest.getPrimarySensorHorizontalFieldOfView(), uavRequest.getPrimarySensorVerticalFieldOfView(), null, null);
        Global.window.bottomBar.bottomBarUAV.setObjectives(null);
        Global.window.bottomBar.bottomBarUAV.setBattery(uavRequest.getBattery(), null, uavRequest.getBatteryRemainingTime(), 0, uavRequest.getAmperageMean());
        Global.window.bottomBar.bottomBarUAV.setCharging(null, null, null, null, null);
        Global.window.bottomBar.bottomBarUAV.setSonars(null);
    }

    private static void paintAreaBar() {
        if (!(Global.window.bottomBar.actualComponent instanceof BottomBarArea)) {
            Global.window.bottomBar.create();
        }

        Area area = (Area) ActiveObject.get();
        Global.window.bottomBar.bottomBarArea.setTitle(area.getName());
        Global.window.bottomBar.bottomBarArea.setGeneralInformation(area.getTypeString(), area.getArea(), area.getActualCoverage());
        Global.window.bottomBar.bottomBarArea.setActualCoverage(area.getActualCoverage());
    }
}
