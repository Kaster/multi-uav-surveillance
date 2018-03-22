package me.dufek.securitydrones.activeobject;

import java.awt.event.MouseEvent;
import me.dufek.securitydrones.area.Area;
import me.dufek.securitydrones.area.Areas;
import me.dufek.securitydrones.chargingstation.ChargingStation;
import me.dufek.securitydrones.chargingstation.ChargingStations;
import me.dufek.securitydrones.main.Global;
import me.dufek.securitydrones.uav.UAV;
import me.dufek.securitydrones.uav.UAVs;
import me.dufek.securitydrones.uav.request.UAVRequest;
import me.dufek.securitydrones.uav.request.UAVRequests;

/**
 * This class represent object selected in GUI's bird view.
 *
 * @author Jan Dufek
 */
public class ActiveObject {

    /**
     * Selected object.
     */
    private static Object object;

    /**
     * Set selected object.
     *
     * @param object Object.
     */
    public static void setSelectedObject(Object object) {
        ActiveObject.object = object;

        if (ActiveObject.object == null) {
            Global.window.menu.edit.copyMenuItem.setEnabled(false);
            Global.window.menu.edit.cutMenuItem.setEnabled(false);
            Global.window.menu.edit.deleteMenuItem.setEnabled(false);
        } else {
            Global.window.menu.edit.copyMenuItem.setEnabled(true);
            Global.window.menu.edit.cutMenuItem.setEnabled(true);
            Global.window.menu.edit.deleteMenuItem.setEnabled(true);
        }

        Global.window.mapPanel.map.refresh();
    }

    /**
     * Get selected object.
     *
     * @return Object.
     */
    public static Object get() {
        return object;
    }

    /**
     * Is any object selected?
     *
     * @return True if object is selected, false otherwise.
     */
    public static boolean isSelected() {
        return object != null;
    }

    /**
     * Checks all the objects if the click was performed over it.
     *
     * @param event Click.
     * @return Object on which was clicked.
     */
    public static Object checkObjects(MouseEvent event) {
        Object object = null;

        if (object == null) {
            object = checkUAVs(event);
        }

        if (object == null) {
            object = checkUAVRequests(event);
        }

        if (object == null) {
            object = checkChargingStations(event);
        }

        if (object == null) {
            object = checkAreas(event);
        }

        return object;
    }

    /**
     * Checks UAVs for click.
     *
     * @param event Click.
     * @return UAV.
     */
    private static UAV checkUAVs(MouseEvent event) {
        for (UAV uav : UAVs.listOfUAVs) {
            if (uav.clicked(event)) {
                return uav;
            }
        }

        return null;
    }

    /**
     * Checks UAV requests for click.
     *
     * @param event Click.
     * @return UAVRequest UAV request.
     */
    private static UAVRequest checkUAVRequests(MouseEvent event) {
        for (UAVRequest uavRequest : UAVRequests.getList()) {
            if (uavRequest.clicked(event)) {
                return uavRequest;
            }
        }

        return null;
    }

    /**
     * Checks charging stations for click.
     *
     * @param event Click.
     * @return ChargingStation Charging station.
     */
    private static ChargingStation checkChargingStations(MouseEvent event) {
        for (ChargingStation chargingStation : ChargingStations.listOfChargingStations) {
            if (chargingStation.clicked(event)) {
                return chargingStation;
            }
        }

        return null;
    }
    
    /**
     * Checks areas for click.
     *
     * @param event Click.
     * @return Area Area.
     */
    private static Area checkAreas(MouseEvent event) {
        for (Area area : Areas.listOfAreas) {
            if (area.clicked(event)) {
                return area;
            }
        }

        return null;
    }
}
