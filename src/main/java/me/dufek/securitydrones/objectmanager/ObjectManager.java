package me.dufek.securitydrones.objectmanager;

import me.dufek.securitydrones.activeobject.ActiveObject;
import me.dufek.securitydrones.area.Area;
import me.dufek.securitydrones.area.Areas;
import me.dufek.securitydrones.area.status.AreasStatus;
import me.dufek.securitydrones.changes.Changes;
import me.dufek.securitydrones.chargingstation.ChargingStation;
import me.dufek.securitydrones.chargingstation.ChargingStations;
import me.dufek.securitydrones.chargingstation.SwapChargingStation;
import me.dufek.securitydrones.chargingstation.status.ChargingStationsStatus;
import me.dufek.securitydrones.clipboard.Clipboard;
import me.dufek.securitydrones.gui.bottombar.BottomBarPainter;
import me.dufek.securitydrones.gui.openedfile.OpenedFile;
import me.dufek.securitydrones.main.Global;
import me.dufek.securitydrones.uav.UAV;
import me.dufek.securitydrones.uav.UAVs;
import me.dufek.securitydrones.uav.destination.Destinations;
import me.dufek.securitydrones.uav.objective.Charge;
import me.dufek.securitydrones.uav.request.UAVRequest;
import me.dufek.securitydrones.uav.request.UAVRequests;
import me.dufek.securitydrones.uav.status.UAVsStatus;

/**
 * Object manager is determined to work with objects. Those objects are UAVs,
 * areas, and charging stations. Those can be cut, copied, added or deleted.
 *
 * @author Jan Dufek
 */
public class ObjectManager {

    public static void cutObject(Object object) {
        if (object != null) {
            if (object instanceof UAV) {
                UAV uav = (UAV) object;
                Clipboard.put(uav.getUAVRequest().getCopy());
                deleteUAV(uav);
                Changes.reportDeletion(uav.getUAVRequest());
            } else if (object instanceof UAVRequest) {
                UAVRequest uavRequest = (UAVRequest) object;
                Clipboard.put(uavRequest.getCopy());
                deleteUAVRequest(uavRequest);
                Changes.reportDeletion(uavRequest);
            } else if (object instanceof ChargingStation) {
                ChargingStation chargingStation = (ChargingStation) object;
                Clipboard.put(((SwapChargingStation) chargingStation).getCopy());
                deleteChargingStation(chargingStation);
                Changes.reportDeletion(chargingStation);
            } else if (object instanceof Area) {
                Area area = (Area) object;
                Clipboard.put(area.getCopy());
                deleteArea(area);
                Changes.reportDeletion(area);
            }

            if (object == ActiveObject.get()) {
                ActiveObject.setSelectedObject(null);
                BottomBarPainter.paint();
            }

            Global.window.mapPanel.map.refresh();
            OpenedFile.setUnsaved();
        }
    }

    public static void copyObject(Object object) {
        if (object != null) {
            if (object instanceof UAV) {
                UAV uav = (UAV) object;
                Clipboard.put(uav.getUAVRequest().getCopy());
            } else if (object instanceof UAVRequest) {
                UAVRequest uavRequest = (UAVRequest) object;
                Clipboard.put(uavRequest.getCopy());
            } else if (object instanceof ChargingStation) {
                ChargingStation chargingStation = (ChargingStation) object;
                Clipboard.put(((SwapChargingStation) chargingStation).getCopy());
            } else if (object instanceof Area) {
                Area area = (Area) object;
                Clipboard.put(area.getCopy());
            }

//                    Global.setSelectedObject(null);
//                    BottomBarPainter.paint();
        }
    }

    public static void deleteObject(Object object) {
        if (object != null) {
            if (object instanceof UAV) {
                UAV uav = (UAV) object;
                deleteUAV(uav);
                Changes.reportDeletion(uav.getUAVRequest());
            } else if (object instanceof UAVRequest) {
                UAVRequest uavRequest = (UAVRequest) object;
                deleteUAVRequest(uavRequest);
                Changes.reportDeletion(uavRequest);
            } else if (object instanceof ChargingStation) {
                ChargingStation chargingStation = (ChargingStation) object;
                deleteChargingStation(chargingStation);
                Changes.reportDeletion(chargingStation);
            } else if (object instanceof Area) {
                Area area = (Area) object;
                deleteArea(area);
                Changes.reportDeletion(area);
            }

            if (object == ActiveObject.get()) {
                ActiveObject.setSelectedObject(null);
                BottomBarPainter.paint();
            }

            Global.window.mapPanel.map.refresh();
            OpenedFile.setUnsaved();
        }
    }

    public static void deleteArea(Area area) {
        AreasStatus.remove(area);
        Areas.removeArea(area);
    }

    public static void deleteChargingStation(ChargingStation chargingStation) {
        ChargingStationsStatus.remove(chargingStation);
        ChargingStations.removeChargingStation(chargingStation);
    }

    public static void deleteUAVRequest(UAVRequest uavRequest) {
        UAVRequests.removeUAV(uavRequest);
    }

    public static void deleteUAV(UAV uav) {
        Destinations.removeDestination(uav);
        UAVsStatus.remove(uav);
        UAVs.removeUAV(uav);

        if (uav.chargingTask != null) {
            uav.chargingTask.cancel();
        }

        if (uav.getObjective() instanceof Charge) {
            uav.getChargingStation().setOccupied(false);
        }

        uav.terminate();

        Global.algorithm.reportUAVDeletion(uav);
    }

    public static void AddObjectForUndo(Object object) {
        if (object != null) {
            if (object instanceof UAVRequest) {
                UAVRequest uavRequest = (UAVRequest) object;
                UAVRequests.addUAV(uavRequest);
            } else if (object instanceof ChargingStation) {
                ChargingStation chargingStation = (ChargingStation) object;
                ChargingStations.addChargingStation(chargingStation);
            } else if (object instanceof Area) {
                Area area = (Area) object;
                Areas.addArea(area);
            }

            if (object == ActiveObject.get()) {
                ActiveObject.setSelectedObject(null);
                BottomBarPainter.paint();
            }

            Global.window.mapPanel.map.refresh();
            OpenedFile.setUnsaved();
        }
    }

    public static void DeleteObjectForUndo(Object object) {
        if (object != null) {
            if (object instanceof UAV) {
                UAV uav = (UAV) object;
                deleteUAV(uav);
            } else if (object instanceof UAVRequest) {
                UAVRequest uavRequest = (UAVRequest) object;

                if (Global.simulation == null) {
                    deleteUAVRequest(uavRequest);
                } else {
                    UAV uav = UAVs.getUAVByUAVRequest(uavRequest);
                    deleteUAV(uav);
                }
            } else if (object instanceof ChargingStation) {
                ChargingStation chargingStation = (ChargingStation) object;
                deleteChargingStation(chargingStation);
            } else if (object instanceof Area) {
                Area area = (Area) object;
                deleteArea(area);
            }

            if (object == ActiveObject.get()) {
                ActiveObject.setSelectedObject(null);
                BottomBarPainter.paint();
            }

            Global.window.mapPanel.map.refresh();
            OpenedFile.setUnsaved();
        }
    }
}
