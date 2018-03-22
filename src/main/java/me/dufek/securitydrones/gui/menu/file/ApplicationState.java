package me.dufek.securitydrones.gui.menu.file;

import java.io.Serializable;
import java.util.ArrayList;
import me.dufek.securitydrones.area.Area;
import me.dufek.securitydrones.area.Areas;
import me.dufek.securitydrones.main.Global;
import me.dufek.securitydrones.uav.UAV;
import me.dufek.securitydrones.uav.UAVs;
import me.dufek.securitydrones.chargingstation.ChargingStation;
import me.dufek.securitydrones.chargingstation.ChargingStations;
import me.dufek.securitydrones.chargingstation.SwapChargingStation;
import me.dufek.securitydrones.uav.request.UAVRequest;
import me.dufek.securitydrones.uav.request.UAVRequests;

/**
 *
 * @author Jan Dufek
 */
public class ApplicationState implements Serializable {

    private ArrayList<UAVRequest> uavRequests;
    private ArrayList<ChargingStation> chargingStations;
    private ArrayList<Area> areas;

    public void snapshot() {
        uavRequests = new ArrayList<UAVRequest>(UAVRequests.getList());
        chargingStations = new ArrayList<ChargingStation>(ChargingStations.listOfChargingStations);
        areas = new ArrayList<Area>(Areas.listOfAreas);
    }

    public void restore() {
        UAVRequests.setList(new ArrayList<UAVRequest>(uavRequests));
        
        for (UAVRequest uavRequest : uavRequests) {
            uavRequest.resetBattery();
        }
        
        ChargingStations.listOfChargingStations = new ArrayList<ChargingStation>(chargingStations);

        for (ChargingStation chargingStation : ChargingStations.listOfChargingStations) {
            if (!(chargingStation instanceof SwapChargingStation)) {
                throw new UnsupportedOperationException("The only possible type of charging station is swap charging station. Other types of charging station are not supported yet.");
            }
            
            // We need to restart schedule, otherwise it will remembet its old schedule
            chargingStation.resetSchedule();
            chargingStation.setOccupied(false);

            // We need to restart battery drum
            ((SwapChargingStation) chargingStation).resetBatteryDrum();

        }

        Areas.listOfAreas = new ArrayList<Area>(areas);
        
        for (Area area : Areas.listOfAreas) {
            area.resetGrid();
        }

        Global.window.mapPanel.map.refresh();
    }
}
