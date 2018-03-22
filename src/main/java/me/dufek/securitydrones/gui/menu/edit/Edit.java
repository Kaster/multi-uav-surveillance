package me.dufek.securitydrones.gui.menu.edit;

import cz.cuni.amis.pogamut.base3d.worldview.object.Location;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import me.dufek.securitydrones.activeobject.ActiveObject;
import me.dufek.securitydrones.area.Area;
import me.dufek.securitydrones.area.Areas;
import me.dufek.securitydrones.changes.Changes;
import me.dufek.securitydrones.chargingstation.ChargingStation;
import me.dufek.securitydrones.main.Global;
import me.dufek.securitydrones.uav.UAVs;
import me.dufek.securitydrones.chargingstation.ChargingStations;
import me.dufek.securitydrones.chargingstation.SwapChargingStation;
import me.dufek.securitydrones.conversion.MapUAVConversion;
import me.dufek.securitydrones.gui.bottombar.BottomBarPainter;
import me.dufek.securitydrones.gui.menu.Menu;
import me.dufek.securitydrones.gui.openedfile.OpenedFile;
import me.dufek.securitydrones.objectmanager.ObjectManager;
import me.dufek.securitydrones.uav.UAV;
import me.dufek.securitydrones.uav.destination.Destinations;
import me.dufek.securitydrones.uav.objective.Charge;
import me.dufek.securitydrones.uav.request.UAVRequest;
import me.dufek.securitydrones.uav.request.UAVRequests;
import me.dufek.securitydrones.uav.status.UAVsStatus;

/**
 *
 * @author Jan Dufek
 */
public class Edit implements Menu {

    private final JMenuBar menuBar;
    private JMenu editMenu;

    public JMenuItem clearMenuItem;
    
    public JMenuItem redoMenuItem;
    public JMenuItem undoMenuItem;
    
    public JMenuItem cutMenuItem;
    public JMenuItem copyMenuItem;
    public JMenuItem deleteMenuItem;

    public Edit(JMenuBar menuBar) {
        this.menuBar = menuBar;
    }

    @Override
    public void create() {
        editMenu = new JMenu("Edit");

        createClearMenuItem();
        editMenu.add(new JSeparator());
        createUndoMenuItem();
        createRedoMenuItem();
        editMenu.add(new JSeparator());
        createCutMenuItem();
        createCopyMenuItem();
        createDeleteMenuItem();

        menuBar.add(editMenu);
    }

    private void createClearMenuItem() {
        clearMenuItem = new JMenuItem("Clear");
        clearMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                UAVRequests.clear();
                Destinations.clear();
                Areas.clear();
                ChargingStations.clear();

                Global.window.mapPanel.map.refresh();

                ActiveObject.setSelectedObject(null);
                BottomBarPainter.paint();
                OpenedFile.setUnsaved();
                Changes.reset();
            }
        });

        if (Global.simulation != null && Global.simulation.isRunning()) {
            clearMenuItem.setEnabled(false);
        }

        editMenu.add(clearMenuItem);
    }

    private void createUndoMenuItem() {
        undoMenuItem = new JMenuItem("Undo");
        undoMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Changes.undo();
            }
        });
        editMenu.add(undoMenuItem);
        
        undoMenuItem.setEnabled(false);
    }

    private void createRedoMenuItem() {
        redoMenuItem = new JMenuItem("Redo");
        redoMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Changes.redo();
            }
        });
        editMenu.add(redoMenuItem);
        
        redoMenuItem.setEnabled(false);
    }

    private void createCutMenuItem() {
        cutMenuItem = new JMenuItem("Cut");
        cutMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ObjectManager.cutObject(ActiveObject.get());
            }
        });
        editMenu.add(cutMenuItem);

        cutMenuItem.setEnabled(false);
    }

    private void createCopyMenuItem() {
        copyMenuItem = new JMenuItem("Copy");
        copyMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ObjectManager.copyObject(ActiveObject.get());
            }
        });
        editMenu.add(copyMenuItem);

        copyMenuItem.setEnabled(false);
    }

    private void createDeleteMenuItem() {
        deleteMenuItem = new JMenuItem("Delete");
        deleteMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ObjectManager.deleteObject(ActiveObject.get());
            }
        });
        editMenu.add(deleteMenuItem);

        deleteMenuItem.setEnabled(false);
    }
}
