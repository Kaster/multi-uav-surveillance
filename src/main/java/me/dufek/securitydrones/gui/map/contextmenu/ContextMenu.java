package me.dufek.securitydrones.gui.map.contextmenu;

import cz.cuni.amis.pogamut.base3d.worldview.object.Location;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import me.dufek.securitydrones.activeobject.ActiveObject;
import me.dufek.securitydrones.area.Area;
import me.dufek.securitydrones.area.Areas;
import me.dufek.securitydrones.changes.Changes;
import me.dufek.securitydrones.main.Global;
import me.dufek.securitydrones.chargingstation.ChargingStation;
import me.dufek.securitydrones.chargingstation.ChargingStations;
import me.dufek.securitydrones.chargingstation.SwapChargingStation;
import me.dufek.securitydrones.clipboard.Clipboard;
import me.dufek.securitydrones.conversion.BirdviewMapConversion;
import me.dufek.securitydrones.gui.openedfile.OpenedFile;
import me.dufek.securitydrones.objectmanager.ObjectManager;
import me.dufek.securitydrones.uav.UAV;
import me.dufek.securitydrones.uav.UAVs;
import me.dufek.securitydrones.uav.request.UAVRequest;
import me.dufek.securitydrones.uav.request.UAVRequests;

/**
 *
 * @author Jan Dufek
 */
public class ContextMenu extends JPopupMenu {

    private JFrame frame;
    private JMenuItem addUAVMenuItem;
    private JMenuItem addChargingStationMenuItem;
    private JMenuItem addAreaMenuItem;
    private JMenuItem cutMenuItem;
    private JMenuItem copyMenuItem;
    private JMenuItem deleteMenuItem;
    private JMenuItem pasteMenuItem;
    private MouseEvent event;
    
    private Object selectedObject;

    public ContextMenu(MouseEvent event, JFrame frame) {
        this.frame = frame;
        this.event = event;

        createContextMenu();
    }

    private void createContextMenu() {
        createAddUAVMenuItem();
        createAddChargingStationMenuItem();
        createAddAreaMenuItem();
        this.add(new JSeparator());

        selectedObject = ActiveObject.checkObjects(event);

        if (selectedObject != null) {
            createCutMenuItem();
            createCopyMenuItem();
            createDeleteMenuItem();
        }

        createPasteMenuItem();
    }

    private void createAddUAVMenuItem() {
        addUAVMenuItem = new JMenuItem("Add UAV");
        addUAVMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddUAVDialog addUAVDialog = new AddUAVDialog(frame);
                UAVRequest uavRequest = addUAVDialog.create();
                if (uavRequest != null) {
                    uavRequest.setStartLocationBirdviewCoordinates(event.getX(), event.getY(), Global.window.mapPanel.map.getImage(), Global.window.mapPanel.map.scale);
                    UAVRequests.addUAV(uavRequest);
                    Changes.reportAddition(uavRequest);
                    Global.window.mapPanel.map.refresh();
                }
            }
        });
        add(addUAVMenuItem);
    }

    private void createAddChargingStationMenuItem() {
        addChargingStationMenuItem = new JMenuItem("Add Charging Station");
        addChargingStationMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddChargingStationDialog addChargingStationDialog = new AddChargingStationDialog(frame);
                ChargingStation chargingStation = addChargingStationDialog.create();
                if (chargingStation != null) {
                    chargingStation.setLocationBirdviewCoordinates(event.getX(), event.getY(), Global.window.mapPanel.map.getImage(), Global.window.mapPanel.map.scale);
                    ChargingStations.addChargingStation(chargingStation);
                    Changes.reportAddition(chargingStation);
                    Global.window.mapPanel.map.refresh();
                }
            }
        });
        add(addChargingStationMenuItem);
    }

    private void createAddAreaMenuItem() {
        addAreaMenuItem = new JMenuItem("Add Area");
        addAreaMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Global.areaSelectionMode = true;
            }
        });
        add(addAreaMenuItem);
    }

    private void createCutMenuItem() {
        cutMenuItem = new JMenuItem("Cut");
        cutMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ObjectManager.cutObject(selectedObject);
            }
        });
        add(cutMenuItem);

//        cutMenuItem.setEnabled(false);
    }

    private void createCopyMenuItem() {
        copyMenuItem = new JMenuItem("Copy");
        copyMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ObjectManager.copyObject(selectedObject);
            }
        });
        add(copyMenuItem);

//        copyMenuItem.setEnabled(false);
    }

    private void createDeleteMenuItem() {
        deleteMenuItem = new JMenuItem("Delete");
        deleteMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ObjectManager.deleteObject(selectedObject);
            }
        });
        add(deleteMenuItem);

//        deleteMenuItem.setEnabled(false);
    }

    private void createPasteMenuItem() {
        pasteMenuItem = new JMenuItem("Paste");
        pasteMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Location locationBirdviewCoordinates = new Location(event.getX(), event.getY(), 0);
                Location locationMapCoordinates = BirdviewMapConversion.birdviewToMap(locationBirdviewCoordinates, Global.window.mapPanel.map.mapImage, Global.window.mapPanel.map.scale);

                if (Clipboard.get() instanceof UAVRequest) {
                    UAVRequest uavRequest = ((UAVRequest) Clipboard.get()).getCopy();
                    uavRequest.setStartLocationBirdviewCoordinates(event.getX(), event.getY(), Global.window.mapPanel.map.mapImage, Global.window.mapPanel.map.scale);
                    UAVRequests.addUAV(uavRequest);
                    Changes.reportAddition(uavRequest);
                    Global.window.mapPanel.map.refresh();
                } else if (Clipboard.get() instanceof ChargingStation) {
                    ChargingStation chargingStation = ((SwapChargingStation) Clipboard.get()).getCopy();
                    chargingStation.setLocationBirdviewCoordinates(event.getX(), event.getY(), Global.window.mapPanel.map.mapImage, Global.window.mapPanel.map.scale);
                    ChargingStations.addChargingStation(chargingStation);
                    Changes.reportAddition(chargingStation);
                    Global.window.mapPanel.map.refresh();
                } else if (Clipboard.get() instanceof Area) {
                    Area area = ((Area) Clipboard.get()).getCopy();
                    area.moveTo(locationMapCoordinates.getX(), locationMapCoordinates.getY());
                    area.createGrid();
                    Areas.addArea(area);
                    Changes.reportAddition(area);
                    Global.window.mapPanel.map.refresh();
                }

                OpenedFile.setUnsaved();
            }
        });
        add(pasteMenuItem);

        if (Clipboard.isFull()) {
            pasteMenuItem.setEnabled(true);
        } else {
            pasteMenuItem.setEnabled(false);
        }
    }
}
