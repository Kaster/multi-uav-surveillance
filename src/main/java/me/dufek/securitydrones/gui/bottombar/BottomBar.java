package me.dufek.securitydrones.gui.bottombar;

import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JPanel;
import me.dufek.securitydrones.activeobject.ActiveObject;
import me.dufek.securitydrones.area.Area;
import me.dufek.securitydrones.chargingstation.ChargingStation;
import me.dufek.securitydrones.gui.bottombar.area.BottomBarArea;
import me.dufek.securitydrones.gui.bottombar.chargingstation.BottomBarChargingStation;
import me.dufek.securitydrones.gui.bottombar.uav.BottomBarUAV;
import me.dufek.securitydrones.main.Global;
import me.dufek.securitydrones.uav.UAV;
import me.dufek.securitydrones.uav.request.UAVRequest;

/**
 *
 * @author Jan Dufek
 */
public class BottomBar {

    private final Container pane;
    private JPanel bottomBarPanel;

    public BottomBarType actualComponent;
    
    public BottomBarUAV bottomBarUAV;
    public BottomBarChargingStation bottomBarChargingStation;
    public BottomBarArea bottomBarArea;

    public BottomBar(Container pane) {
        this.pane = pane;
    }

    public void create() {
        if (bottomBarPanel != null) {
            pane.remove(bottomBarPanel);
        }

        bottomBarPanel = new JPanel(new GridBagLayout());

        if (ActiveObject.get() instanceof ChargingStation) {
            createChargingStationBar();
        } else if (ActiveObject.get() instanceof UAV) {
            createUAVBar();
        } else if (ActiveObject.get() instanceof UAVRequest) {
            createUAVBar();
        } else if (ActiveObject.get() instanceof Area) {
            createAreaBar();
        }

    }

    private void createChargingStationBar() {
        bottomBarChargingStation = new BottomBarChargingStation(bottomBarPanel);
        bottomBarChargingStation.create();

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.weightx = 0.2;
        constraints.weighty = 0.2;
        constraints.insets = new Insets(5, 5, 5, 5);
        constraints.ipadx = 0;
        constraints.ipady = 0;

        pane.add(bottomBarPanel, constraints);
        actualComponent = bottomBarChargingStation;

        setVisible(false);
    }

    private void createUAVBar() {
        bottomBarUAV = new BottomBarUAV(bottomBarPanel);
        bottomBarUAV.create();

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.weightx = 0.2;
        constraints.weighty = 0.2;
        constraints.insets = new Insets(5, 5, 5, 5);
        constraints.ipadx = 0;
        constraints.ipady = 0;

        pane.add(bottomBarPanel, constraints);
        actualComponent = bottomBarUAV;

        setVisible(false);
    }

    private void createAreaBar() {
        bottomBarArea = new BottomBarArea(bottomBarPanel);
        bottomBarArea.create();

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.weightx = 0.2;
        constraints.weighty = 0.2;
        constraints.insets = new Insets(5, 5, 5, 5);
        constraints.ipadx = 0;
        constraints.ipady = 0;

        pane.add(bottomBarPanel, constraints);
        actualComponent = bottomBarArea;

        setVisible(false);
    }

    public void setVisible(boolean visible) {
        if (bottomBarPanel != null) {
            this.bottomBarPanel.setVisible(visible);
        } else {
            Global.window.bottomBar.create();
            this.bottomBarPanel.setVisible(visible);
        }
    }
}
