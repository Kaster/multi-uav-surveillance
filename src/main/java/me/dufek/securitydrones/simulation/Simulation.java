package me.dufek.securitydrones.simulation;

import java.util.logging.Level;
import java.util.logging.Logger;
import me.dufek.securitydrones.algorithm.Algorithm;
import me.dufek.securitydrones.area.Area;
import me.dufek.securitydrones.area.Areas;
import me.dufek.securitydrones.area.status.AreasStatus;
import me.dufek.securitydrones.graph.analyser.Analyser;
import me.dufek.securitydrones.gui.bottombar.BottomBarPainter;
import me.dufek.securitydrones.main.Global;
import me.dufek.securitydrones.main.Main;
import me.dufek.securitydrones.time.Time;
import me.dufek.securitydrones.uav.destination.Destinations;
import me.dufek.securitydrones.uav.UAV;
import me.dufek.securitydrones.uav.request.UAVRequests;
import me.dufek.securitydrones.uav.UAVs;
import me.dufek.securitydrones.chargingstation.ChargingStation;
import me.dufek.securitydrones.chargingstation.ChargingStations;
import me.dufek.securitydrones.chargingstation.status.ChargingStationsStatus;
import me.dufek.securitydrones.preferences.UserPreferences;
import me.dufek.securitydrones.uav.status.UAVsStatus;

/**
 * This class is responsible for managing the simulation. It works as a HUB
 * which is calling other methods. It is responsible for executing algorithms,
 * executing the logic of charging stations and areas and setting parameters for
 * GUI.
 *
 * @author Jan Dufek
 */
public class Simulation extends Thread {

    /**
     * Indicates if the simulation should be run.
     */
    private boolean run = true;
    
    /**
     * The simulation menu from the GUI.
     */
    me.dufek.securitydrones.gui.menu.simulation.Simulation simulationMenu;
    
    /**
     * Counter for analyzing the coverage.
     */
    int coverageAnalysationCounter = 0;
    
    /**
     * The algorithm used in this simulation.
     */
    private final Algorithm algorithm;

    /**
     * Initialize simulation.
     * 
     * @param simulationMenu The GUI simulation menu.
     * @param algorithm Algorithm.
     */
    public Simulation(me.dufek.securitydrones.gui.menu.simulation.Simulation simulationMenu, Algorithm algorithm) {
        this.simulationMenu = simulationMenu;
        this.algorithm = algorithm;
    }

    /**
     * The main class used to run the simulation. It is executed periodically.
     */
    @Override
    public void run() {
//        RandomAlgorithm randomAlgorithm = new RandomAlgorithm(Global.mainWindow);

//        SpanningTreeCoverageAlgorithm spanningTreeCoverageAlgorithm = new SpanningTreeCoverageAlgorithm(Global.mainWindow);
        Global.algorithm = algorithm;
//        
//        Algorithm algorithm = spanningTreeCoverageAlgorithm;

        while (run) {
            UAVs.saveActualTime();
            Global.window.rightBar.simulationPanel.setTime(Time.getTime());
            algorithm.logic();

            for (ChargingStation chargingStation : ChargingStations.listOfChargingStations) {
                chargingStation.logic();
            }

            Areas.updateHeatMap();
            UAVs.cooldownVisibleArea();

            for (Area area : Areas.listOfAreas) {
                area.setStatus();
            }

            showRightBarStatus();

            BottomBarPainter.paint();
            Global.window.mapPanel.map.refresh();
            analyseActualCoverage();

            try {
                Thread.sleep(Preferences.getSimulationLogicIntervalMiliSeconds());
            } catch (InterruptedException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Shows statuses in the right bar.
     */
    private void showRightBarStatus() {
        UAVsStatus.showStatusesInUAVsPanel();
        ChargingStationsStatus.showStatusesInChargingStationsPanel();
        AreasStatus.showStatusesInAreasPanel();
    }

    /**
     * Analysis actual coverage.
     */
    private void analyseActualCoverage() {
        if (coverageAnalysationCounter == UserPreferences.preferences.COVEREGE_ANALYSIS_FREQUENCY) {
            double actualCoverage = Analyser.getActualCoverage();

            Global.window.rightBar.algorithmPanel.setActualCoverage(actualCoverage);

            Global.window.rightBar.performancePanel.addMeasurement(Time.getDate(), actualCoverage);
            coverageAnalysationCounter = 0;
//            System.gc();
        } else {
            coverageAnalysationCounter++;
        }
    }

    /**
     * Used to terminate the simulation.
     */
    public void stopSimulation() {
        this.run = false;

        terminateUAVs();
        clearLists();
        clearPanels();
//        clearBottomBar();
    }

    /**
     * Kill all UAVs.
     */
    private void terminateUAVs() {
        for (UAV uav : UAVs.listOfUAVs) {
            uav.terminate();
        }
    }

    /**
     * Clear the lists of all objects.
     */
    private void clearLists() {
        UAVs.clear();
        UAVRequests.clear();
        Destinations.clear();
        Areas.clear();
        ChargingStations.clear();
        UAVsStatus.clear();
        ChargingStationsStatus.clear();
        AreasStatus.clear();
//        FlightLevels.clear();
    }

    /**
     * Clears the right bar panel.
     */
    private void clearPanels() {
        Global.window.rightBar.uavsPanel.setText(null);
    }

    /**
     * Clears the bottom bar.
     */
    public void clearBottomBar() {
        BottomBarPainter.paint();
    }

    /**
     * Checks if the simulation is running.
     * 
     * @return True if the simulation is running and false otherwise.
     */
    public boolean isRunning() {
        return run;
    }
}
