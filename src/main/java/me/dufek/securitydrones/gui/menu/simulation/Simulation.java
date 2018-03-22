package me.dufek.securitydrones.gui.menu.simulation;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import me.dufek.securitydrones.activeobject.ActiveObject;
import me.dufek.securitydrones.algorithm.Algorithm;
import me.dufek.securitydrones.area.Area;
import me.dufek.securitydrones.area.Areas;
import me.dufek.securitydrones.changes.Changes;
import me.dufek.securitydrones.chargingstation.ChargingStation;
import me.dufek.securitydrones.chargingstation.ChargingStations;
import me.dufek.securitydrones.gui.menu.Menu;
import me.dufek.securitydrones.gui.menu.file.ApplicationState;
import me.dufek.securitydrones.main.Global;
import me.dufek.securitydrones.server.Server;
import me.dufek.securitydrones.uav.UAV;
import me.dufek.securitydrones.uav.request.UAVRequest;
import me.dufek.securitydrones.uav.request.UAVRequests;

/**
 *
 * @author Jan Dufek
 */
public class Simulation implements Menu {

    private final JMenuBar menuBar;
    private final JFrame frame;
    private JMenu simulationMenu;
    private me.dufek.securitydrones.simulation.Simulation simulationThread;
    JMenuItem startMenuItem;
    JMenuItem stopMenuItem;

    private ApplicationState applicationState;

    public Simulation(JFrame frame, JMenuBar menuBar) {
        this.frame = frame;
        this.menuBar = menuBar;
    }

    @Override
    public void create() {
        simulationMenu = new JMenu("Simulation");

        createStartMenuItem();
        createStopMenuItem();

        menuBar.add(simulationMenu);
    }

    private void createStartMenuItem() {
        startMenuItem = new JMenuItem("Start");
        startMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startSimulation();
            }
        });
        simulationMenu.add(startMenuItem);
        startMenuItem.setEnabled(true);
    }

    private void createStopMenuItem() {
        stopMenuItem = new JMenuItem("Stop");
        stopMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stopSimulation();
            }
        });
        simulationMenu.add(stopMenuItem);
        stopMenuItem.setEnabled(false);
    }

    private void startSimulation() {
        SelectAlgorithmDialog selectAlgorithmDialog = new SelectAlgorithmDialog(frame);
        Algorithm algorithm = selectAlgorithmDialog.create();

        if (algorithm != null) {
            Global.window.rightBar.algorithmPanel.setAlgorithm(algorithm.getName());

            saveStateBeforeSimulation();

            simulationThread = new me.dufek.securitydrones.simulation.Simulation(this, algorithm);
            simulationThread.start();

            setOptionsForSimulationMode();

            Global.simulation = simulationThread;

//            if (Global.selectedObject instanceof UAVRequest) {
//                UAVRequest uavRequest = (UAVRequest) Global.selectedObject;
//                Global.selectedObject = UAVs.getUAVByUAVRequest(uavRequest);
//            }
        }
    }

//    private boolean checkServer() throws IllegalStateException, HeadlessException {
//        if (!Server.isRunning(Global.getMapName())) {
//            int startServerReturnValue = askIfStartServer();
//            switch (startServerReturnValue) {
//                case -1:
//                    return true;
//                case 0:
//                    if (Global.window.menu.server.startServerRequest(Global.getMapName())) {
//                        return true;
//                    }
//                    break;
//                case 1:
//                    return true;
//                default:
//                    throw new IllegalStateException("Unknown return value.");
//            }
//        }
//        return false;
//    }
//
//    private static int askIfStartServer() throws HeadlessException {
//        Object[] options = {"Yes", "Cancel"};
//
//        int choice = JOptionPane.showOptionDialog(Global.window.frame,
//                "No server using map " + Global.getMapName() + " is running. Would you like to start server now?",
//                "No Server",
//                JOptionPane.YES_NO_CANCEL_OPTION,
//                JOptionPane.QUESTION_MESSAGE,
//                null,
//                options,
//                options[0]);
//        return choice;
//    }
    private void setOptionsForSimulationMode() {
        startMenuItem.setEnabled(false);
        stopMenuItem.setEnabled(true);

        Global.window.menu.server.startMenuItem.setEnabled(false);

        Global.window.menu.map.changeMenuItem.setEnabled(false);
        Global.window.menu.map.refreshMenuItem.setEnabled(false);

        Global.window.menu.file.newMenuItem.setEnabled(false);
        Global.window.menu.file.openMenuItem.setEnabled(false);
        Global.window.menu.file.saveMenuItem.setEnabled(false);
        Global.window.menu.file.saveAsMenuItem.setEnabled(false);
        Global.window.menu.edit.clearMenuItem.setEnabled(false);
    }

    private void saveStateBeforeSimulation() {
        applicationState = new ApplicationState();
        applicationState.snapshot();
    }

    public void stopSimulation() {
        Global.window.rightBar.algorithmPanel.setAlgorithm(null);
        Global.window.rightBar.simulationPanel.setTime(null);
        Global.window.rightBar.algorithmPanel.setActualCoverage(null);
        
        Global.algorithm = null;
        
        simulationThread.stopSimulation();
        Global.window.mapPanel.map.refresh();
        applicationState.restore();
        restoreSelectedObject();

        Global.simulation.clearBottomBar();

        Changes.reset();

        setOptionsForNonSimulationMode();

        Global.simulation = null;
    }

    private void restoreSelectedObject() {
        if (ActiveObject.get() instanceof UAV) {
            UAVRequest uavRequest = ((UAV) ActiveObject.get()).getUAVRequest();

            if (UAVRequests.contains(uavRequest)) {
                ActiveObject.setSelectedObject(uavRequest);
            } else {
                ActiveObject.setSelectedObject(null);
            }
        } else if (ActiveObject.get() instanceof ChargingStation) {
            if (!ChargingStations.contains((ChargingStation) ActiveObject.get())) {
                ActiveObject.setSelectedObject(null);
            }
        } else if (ActiveObject.get() instanceof Area) {
            if (!Areas.contains((Area) ActiveObject.get())) {
                ActiveObject.setSelectedObject(null);
            }
        }
    }

    private void setOptionsForNonSimulationMode() {
        startMenuItem.setEnabled(true);
        stopMenuItem.setEnabled(false);

        if (!Server.isRunning()) {
            Global.window.menu.server.startMenuItem.setEnabled(true);
            Global.window.menu.map.changeMenuItem.setEnabled(true);
            Global.window.menu.map.refreshMenuItem.setEnabled(true);
        }

        Global.window.menu.file.newMenuItem.setEnabled(true);
        Global.window.menu.file.openMenuItem.setEnabled(true);
        Global.window.menu.file.saveMenuItem.setEnabled(true);
        Global.window.menu.file.saveAsMenuItem.setEnabled(true);
        Global.window.menu.edit.clearMenuItem.setEnabled(true);
    }
}
