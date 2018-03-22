package me.dufek.securitydrones.gui.menu.preferences;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import me.dufek.securitydrones.preferences.UserPreferences;

/**
 *
 * @author Jan Dufek
 */
public class PreferencesDialog {

    private final JFrame frame;

    public PreferencesDialog(JFrame frame) {
        this.frame = frame;
    }

    public void create() {
        JTabbedPane tabbedPane = new JTabbedPane();

        JPanel generalPanel = new JPanel(new BorderLayout(5, 5));

        JPanel generalLabels = new JPanel(new GridLayout(0, 1, 2, 2));
        generalLabels.add(new JLabel("UT2004 Home Folder", SwingConstants.RIGHT));
        generalLabels.add(new JLabel("UT2004 Screen Shots Folder", SwingConstants.RIGHT));
        generalLabels.add(new JLabel("UT2004 Default Map", SwingConstants.RIGHT));
        generalLabels.add(new JLabel("", SwingConstants.RIGHT));
        generalLabels.add(new JLabel("", SwingConstants.RIGHT));
        generalLabels.add(new JLabel("", SwingConstants.RIGHT));
        generalLabels.add(new JLabel("", SwingConstants.RIGHT));
        generalLabels.add(new JLabel("", SwingConstants.RIGHT));
        generalLabels.add(new JLabel("", SwingConstants.RIGHT));
        generalLabels.add(new JLabel("", SwingConstants.RIGHT));
        generalLabels.add(new JLabel("", SwingConstants.RIGHT));
        generalPanel.add(generalLabels, BorderLayout.WEST);

        JPanel generalFields = new JPanel(new GridLayout(0, 1, 2, 2));
        JTextField ut2004HomeFolder = new JTextField(UserPreferences.preferences.UNREAL_TOURNAMENT_2004_HOME_FOLDER);
        generalFields.add(ut2004HomeFolder);
        JTextField ut2004ScreenShotsFolder = new JTextField(UserPreferences.preferences.UNREAL_TOURNAMENT_2004_SCREEN_SHOTS_FOLDER);
        generalFields.add(ut2004ScreenShotsFolder);
        JTextField defaultMap = new JTextField(UserPreferences.preferences.DEFAULT_MAP_NAME);
        generalFields.add(defaultMap);
        generalFields.add(new JLabel("", SwingConstants.RIGHT));
        generalFields.add(new JLabel("", SwingConstants.RIGHT));
        generalFields.add(new JLabel("", SwingConstants.RIGHT));
        generalFields.add(new JLabel("", SwingConstants.RIGHT));
        generalFields.add(new JLabel("", SwingConstants.RIGHT));
        generalFields.add(new JLabel("", SwingConstants.RIGHT));
        generalFields.add(new JLabel("", SwingConstants.RIGHT));
        generalFields.add(new JLabel("", SwingConstants.RIGHT));
        generalPanel.add(generalFields, BorderLayout.CENTER);

        tabbedPane.addTab("General", null, generalPanel, null);

        JPanel birdviewPanel = new JPanel(new BorderLayout(5, 5));

        JPanel birdviewLabels = new JPanel(new GridLayout(0, 1, 2, 2));
        birdviewLabels.add(new JLabel("UT2004 Horizontal Resolution [px]", SwingConstants.RIGHT));
        birdviewLabels.add(new JLabel("UT2004 Vertical Resolution [px]", SwingConstants.RIGHT));
        birdviewLabels.add(new JLabel("UT2004 Horizontal Field of View [deg]", SwingConstants.RIGHT));
        birdviewLabels.add(new JLabel("Screen Shot Altitude [UU]", SwingConstants.RIGHT));
        birdviewLabels.add(new JLabel("", SwingConstants.RIGHT));
        birdviewLabels.add(new JLabel("", SwingConstants.RIGHT));
        birdviewLabels.add(new JLabel("", SwingConstants.RIGHT));
        birdviewLabels.add(new JLabel("", SwingConstants.RIGHT));
        birdviewLabels.add(new JLabel("", SwingConstants.RIGHT));
        birdviewLabels.add(new JLabel("", SwingConstants.RIGHT));
        birdviewLabels.add(new JLabel("", SwingConstants.RIGHT));
        birdviewPanel.add(birdviewLabels, BorderLayout.WEST);

        JPanel birdviewFields = new JPanel(new GridLayout(0, 1, 2, 2));
        JTextField unrealTournament2004HorizontalResolution = new JTextField(Double.toString(UserPreferences.preferences.UNREAL_TOURNAMENT_2004_HORIZONTAL_RESOLUTION));
        birdviewFields.add(unrealTournament2004HorizontalResolution);
        JTextField unrealTournament2004VerticalResolution = new JTextField(Double.toString(UserPreferences.preferences.UNREAL_TOURNAMENT_2004_VERTICAL_RESOLUTION));
        birdviewFields.add(unrealTournament2004VerticalResolution);
        JTextField unrealTournament2004HorizontalFieldOfView = new JTextField(Double.toString(UserPreferences.preferences.UNREAL_TOURNAMENT_2004_HORIZONTAL_FIELD_OF_VIEW));
        birdviewFields.add(unrealTournament2004HorizontalFieldOfView);
        JTextField flightHeight = new JTextField(Double.toString(UserPreferences.preferences.FLIGHT_HEIGHT));
        birdviewFields.add(flightHeight);
        birdviewFields.add(new JLabel("", SwingConstants.RIGHT));
        birdviewFields.add(new JLabel("", SwingConstants.RIGHT));
        birdviewFields.add(new JLabel("", SwingConstants.RIGHT));
        birdviewFields.add(new JLabel("", SwingConstants.RIGHT));
        birdviewFields.add(new JLabel("", SwingConstants.RIGHT));
        birdviewFields.add(new JLabel("", SwingConstants.RIGHT));
        birdviewFields.add(new JLabel("", SwingConstants.RIGHT));
        birdviewPanel.add(birdviewFields, BorderLayout.CENTER);

        tabbedPane.addTab("Birdview", null, birdviewPanel, null);

        JPanel mapPanel = new JPanel(new BorderLayout(5, 5));

        JPanel mapLabels = new JPanel(new GridLayout(0, 1, 2, 2));
        mapLabels.add(new JLabel("Default Scale [factor]", SwingConstants.RIGHT));
        mapLabels.add(new JLabel("Scale In Factor [factor]", SwingConstants.RIGHT));
        mapLabels.add(new JLabel("Scale Out Factor [factor]", SwingConstants.RIGHT));
        mapLabels.add(new JLabel("Minimal Scale [factor]", SwingConstants.RIGHT));
        mapLabels.add(new JLabel("Maximal Scale [factor]", SwingConstants.RIGHT));
        mapLabels.add(new JLabel("Drag Inertia Speed [factor]", SwingConstants.RIGHT));
        mapLabels.add(new JLabel("Drag Inertia Cooldown [factor]", SwingConstants.RIGHT));
        mapLabels.add(new JLabel("Drag Inertia Delay [ms]", SwingConstants.RIGHT));
        mapLabels.add(new JLabel("", SwingConstants.RIGHT));
        mapLabels.add(new JLabel("", SwingConstants.RIGHT));
        mapLabels.add(new JLabel("", SwingConstants.RIGHT));
        mapPanel.add(mapLabels, BorderLayout.WEST);

        JPanel mapFields = new JPanel(new GridLayout(0, 1, 2, 2));
        JTextField mapDefaultScale = new JTextField(Double.toString(UserPreferences.preferences.MAP_DEFAULT_SCALE));
        mapFields.add(mapDefaultScale);
        JTextField scaleInFactor = new JTextField(Double.toString(UserPreferences.preferences.MAP_SCALE_IN_FACTOR));
        mapFields.add(scaleInFactor);
        JTextField scaleOutFactor = new JTextField(Double.toString(UserPreferences.preferences.MAP_SCALE_OUT_FACTOR));
        mapFields.add(scaleOutFactor);
        JTextField minimalScale = new JTextField(Double.toString(UserPreferences.preferences.MAP_MINIMAL_SCALE));
        mapFields.add(minimalScale);
        JTextField maximalScale = new JTextField(Double.toString(UserPreferences.preferences.MAP_MAXIMUM_SCALE));
        mapFields.add(maximalScale);
        JTextField dragInertiaSpeed = new JTextField(Double.toString(UserPreferences.preferences.MAP_DRAG_INERTIA_SPEED));
        mapFields.add(dragInertiaSpeed);
        JTextField dragInertialCooldown = new JTextField(Double.toString(UserPreferences.preferences.MAP_DRAG_INERTIA_COOLDOWN));
        mapFields.add(dragInertialCooldown);
        JTextField dragInertialDelay = new JTextField(Integer.toString(UserPreferences.preferences.MAP_TIMER_DELAY));
        mapFields.add(dragInertialDelay);
        mapFields.add(new JLabel("", SwingConstants.RIGHT));
        mapFields.add(new JLabel("", SwingConstants.RIGHT));
        mapFields.add(new JLabel("", SwingConstants.RIGHT));
        mapPanel.add(mapFields, BorderLayout.CENTER);

        tabbedPane.addTab("Map", null, mapPanel, null);

        JPanel serverPanel = new JPanel(new BorderLayout(5, 5));

        JPanel serverLabels = new JPanel(new GridLayout(0, 1, 2, 2));
        serverLabels.add(new JLabel("Server Address", SwingConstants.RIGHT));
        serverLabels.add(new JLabel("Server Port", SwingConstants.RIGHT));
        serverLabels.add(new JLabel("", SwingConstants.RIGHT));
        serverLabels.add(new JLabel("", SwingConstants.RIGHT));
        serverLabels.add(new JLabel("", SwingConstants.RIGHT));
        serverLabels.add(new JLabel("", SwingConstants.RIGHT));
        serverLabels.add(new JLabel("", SwingConstants.RIGHT));
        serverLabels.add(new JLabel("", SwingConstants.RIGHT));
        serverLabels.add(new JLabel("", SwingConstants.RIGHT));
        serverLabels.add(new JLabel("", SwingConstants.RIGHT));
        serverLabels.add(new JLabel("", SwingConstants.RIGHT));
        serverPanel.add(serverLabels, BorderLayout.WEST);

        JPanel serverFields = new JPanel(new GridLayout(0, 1, 2, 2));
        JTextField serverAddress = new JTextField(UserPreferences.preferences.SERVER_ADDRESS);
        serverFields.add(serverAddress);
        JTextField serverPort = new JTextField(Integer.toString(UserPreferences.preferences.SERVER_PORT));
        serverFields.add(serverPort);
        serverFields.add(new JLabel("", SwingConstants.RIGHT));
        serverFields.add(new JLabel("", SwingConstants.RIGHT));
        serverFields.add(new JLabel("", SwingConstants.RIGHT));
        serverFields.add(new JLabel("", SwingConstants.RIGHT));
        serverFields.add(new JLabel("", SwingConstants.RIGHT));
        serverFields.add(new JLabel("", SwingConstants.RIGHT));
        serverFields.add(new JLabel("", SwingConstants.RIGHT));
        serverFields.add(new JLabel("", SwingConstants.RIGHT));
        serverFields.add(new JLabel("", SwingConstants.RIGHT));
        serverPanel.add(serverFields, BorderLayout.CENTER);

        tabbedPane.addTab("Server", null, serverPanel, null);

        JPanel simulationPanel = new JPanel(new BorderLayout(5, 5));

        JPanel simulationLabels = new JPanel(new GridLayout(0, 1, 2, 2));
        simulationLabels.add(new JLabel("Simulation Logic Interval [s]", SwingConstants.RIGHT));
        simulationLabels.add(new JLabel("Coverage Analysis Frequency [number of logics]", SwingConstants.RIGHT));
        simulationLabels.add(new JLabel("", SwingConstants.RIGHT));
        simulationLabels.add(new JLabel("", SwingConstants.RIGHT));
        simulationLabels.add(new JLabel("", SwingConstants.RIGHT));
        simulationLabels.add(new JLabel("", SwingConstants.RIGHT));
        simulationLabels.add(new JLabel("", SwingConstants.RIGHT));
        simulationLabels.add(new JLabel("", SwingConstants.RIGHT));
        simulationLabels.add(new JLabel("", SwingConstants.RIGHT));
        simulationLabels.add(new JLabel("", SwingConstants.RIGHT));
        simulationLabels.add(new JLabel("", SwingConstants.RIGHT));
        simulationPanel.add(simulationLabels, BorderLayout.WEST);

        JPanel simulationFields = new JPanel(new GridLayout(0, 1, 2, 2));
        JTextField simulationLogicInterval = new JTextField(Double.toString(UserPreferences.preferences.SIMULATION_LOGIC_INTERVAL));
        simulationFields.add(simulationLogicInterval);
        JTextField coverageAnalysisFrequency = new JTextField(Integer.toString(UserPreferences.preferences.COVEREGE_ANALYSIS_FREQUENCY));
        simulationFields.add(coverageAnalysisFrequency);
        simulationFields.add(new JLabel("", SwingConstants.RIGHT));
        simulationFields.add(new JLabel("", SwingConstants.RIGHT));
        simulationFields.add(new JLabel("", SwingConstants.RIGHT));
        simulationFields.add(new JLabel("", SwingConstants.RIGHT));
        simulationFields.add(new JLabel("", SwingConstants.RIGHT));
        simulationFields.add(new JLabel("", SwingConstants.RIGHT));
        simulationFields.add(new JLabel("", SwingConstants.RIGHT));
        simulationFields.add(new JLabel("", SwingConstants.RIGHT));
        simulationFields.add(new JLabel("", SwingConstants.RIGHT));
        simulationPanel.add(simulationFields, BorderLayout.CENTER);

        tabbedPane.addTab("Simulation", null, simulationPanel, null);

        JPanel algorithmPanel = new JPanel(new BorderLayout(5, 5));

        JPanel algorithmLabels = new JPanel(new GridLayout(0, 1, 2, 2));
        algorithmLabels.add(new JLabel("Collision Detection UAVs' Altitude Difference Threshold [m]", SwingConstants.RIGHT));
        algorithmLabels.add(new JLabel("Is Near Destination Distance Threshold [m]", SwingConstants.RIGHT));
        algorithmLabels.add(new JLabel("Approach Angle Difference Threshold [deg]", SwingConstants.RIGHT));
        algorithmLabels.add(new JLabel("", SwingConstants.RIGHT));
        algorithmLabels.add(new JLabel("", SwingConstants.RIGHT));
        algorithmLabels.add(new JLabel("", SwingConstants.RIGHT));
        algorithmLabels.add(new JLabel("", SwingConstants.RIGHT));
        algorithmLabels.add(new JLabel("", SwingConstants.RIGHT));
        algorithmLabels.add(new JLabel("", SwingConstants.RIGHT));
        algorithmLabels.add(new JLabel("", SwingConstants.RIGHT));
        algorithmLabels.add(new JLabel("", SwingConstants.RIGHT));
        algorithmPanel.add(algorithmLabels, BorderLayout.WEST);

        JPanel algorithmFields = new JPanel(new GridLayout(0, 1, 2, 2));
        JTextField collisionDetectionAltitudeDifferenceThreshold = new JTextField(Double.toString(UserPreferences.preferences.COLLISION_DETECTION_ALTITUDE_DIFFERENCE_THRESHOLD));
        algorithmFields.add(collisionDetectionAltitudeDifferenceThreshold);
        JTextField isNearDestinationDistanceThreshold = new JTextField(Double.toString(UserPreferences.preferences.IS_NEAR_DESTINATION_DISTANCE_THRESHOLD));
        algorithmFields.add(isNearDestinationDistanceThreshold);
        JTextField approachAngleDifferenceThreshold = new JTextField(Double.toString(UserPreferences.preferences.APPROACH_ANGLE_DIFFERENCE_THRESHOLD));
        algorithmFields.add(approachAngleDifferenceThreshold);
        algorithmFields.add(new JLabel("", SwingConstants.RIGHT));
        algorithmFields.add(new JLabel("", SwingConstants.RIGHT));
        algorithmFields.add(new JLabel("", SwingConstants.RIGHT));
        algorithmFields.add(new JLabel("", SwingConstants.RIGHT));
        algorithmFields.add(new JLabel("", SwingConstants.RIGHT));
        algorithmFields.add(new JLabel("", SwingConstants.RIGHT));
        algorithmFields.add(new JLabel("", SwingConstants.RIGHT));
        algorithmFields.add(new JLabel("", SwingConstants.RIGHT));
        algorithmPanel.add(algorithmFields, BorderLayout.CENTER);

        tabbedPane.addTab("Algorithm", null, algorithmPanel, null);

        JPanel heatMapPanel = new JPanel(new BorderLayout(5, 5));

        JPanel heatMapLabels = new JPanel(new GridLayout(0, 1, 2, 2));
        heatMapLabels.add(new JLabel("Cell Width [m]", SwingConstants.RIGHT));
        heatMapLabels.add(new JLabel("Cell Height [m]", SwingConstants.RIGHT));
        heatMapLabels.add(new JLabel("Maximal Heat [s]", SwingConstants.RIGHT));
        heatMapLabels.add(new JLabel("", SwingConstants.RIGHT));
        heatMapLabels.add(new JLabel("", SwingConstants.RIGHT));
        heatMapLabels.add(new JLabel("", SwingConstants.RIGHT));
        heatMapLabels.add(new JLabel("", SwingConstants.RIGHT));
        heatMapLabels.add(new JLabel("", SwingConstants.RIGHT));
        heatMapLabels.add(new JLabel("", SwingConstants.RIGHT));
        heatMapLabels.add(new JLabel("", SwingConstants.RIGHT));
        heatMapLabels.add(new JLabel("", SwingConstants.RIGHT));
        heatMapPanel.add(heatMapLabels, BorderLayout.WEST);

        JPanel heatMapFields = new JPanel(new GridLayout(0, 1, 2, 2));
        JTextField cellWidth = new JTextField(Double.toString(UserPreferences.preferences.CELL_WIDTH));
        heatMapFields.add(cellWidth);
        JTextField cellHeight = new JTextField(Double.toString(UserPreferences.preferences.CELL_HEIGHT));
        heatMapFields.add(cellHeight);
        JTextField maximalHeat = new JTextField(Double.toString(UserPreferences.preferences.MAXIMAL_HEAT));
        heatMapFields.add(maximalHeat);
        heatMapFields.add(new JLabel("", SwingConstants.RIGHT));
        heatMapFields.add(new JLabel("", SwingConstants.RIGHT));
        heatMapFields.add(new JLabel("", SwingConstants.RIGHT));
        heatMapFields.add(new JLabel("", SwingConstants.RIGHT));
        heatMapFields.add(new JLabel("", SwingConstants.RIGHT));
        heatMapFields.add(new JLabel("", SwingConstants.RIGHT));
        heatMapFields.add(new JLabel("", SwingConstants.RIGHT));
        heatMapFields.add(new JLabel("", SwingConstants.RIGHT));
        heatMapPanel.add(heatMapFields, BorderLayout.CENTER);

        tabbedPane.addTab("Heat Map", null, heatMapPanel, null);

        JPanel uavPanel = new JPanel(new BorderLayout(5, 5));

        JPanel uavLabels = new JPanel(new GridLayout(0, 1, 2, 2));
        uavLabels.add(new JLabel("Safety Margin for Return to Charger [factor]", SwingConstants.RIGHT));
        uavLabels.add(new JLabel("Safety Margin for Start of Charging [s]", SwingConstants.RIGHT));
        uavLabels.add(new JLabel("Safety Margin for Landing to Charger [factor]", SwingConstants.RIGHT));
        uavLabels.add(new JLabel("Safety Margin for Takeoff from Charger [factor]", SwingConstants.RIGHT));
        uavLabels.add(new JLabel("Safety Margin for End of Charging [s]", SwingConstants.RIGHT));
        uavLabels.add(new JLabel("Minimum Flight Level [m]", SwingConstants.RIGHT));
        uavLabels.add(new JLabel("Maximum Flight Level [m]", SwingConstants.RIGHT));
        uavLabels.add(new JLabel("Distance Between Flight Levels [m]", SwingConstants.RIGHT));
        uavLabels.add(new JLabel("Click Active Area Size [px]", SwingConstants.RIGHT));
        uavLabels.add(new JLabel("Spawning Height Above Spawn Level [m]", SwingConstants.RIGHT));
        uavLabels.add(new JLabel("Spawn Finished Altitude [m]", SwingConstants.RIGHT));
        uavPanel.add(uavLabels, BorderLayout.WEST);

        JPanel uavFields = new JPanel(new GridLayout(0, 1, 2, 2));
        JTextField uavReserveForGoingBackToCharger = new JTextField(Double.toString(UserPreferences.preferences.UAV_RESERVE_FOR_GOING_BACK_TO_CHARGER));
        uavFields.add(uavReserveForGoingBackToCharger);
        JTextField uavReserveStartingChargingProcedure = new JTextField(Double.toString(UserPreferences.preferences.UAV_RESERVE_STARTING_CHARGING_PROCEDURE));
        uavFields.add(uavReserveStartingChargingProcedure);
        JTextField uavReserveLandingToCharger = new JTextField(Double.toString(UserPreferences.preferences.UAV_RESERVE_LANDING_TO_CHARGER));
        uavFields.add(uavReserveLandingToCharger);
        JTextField uavReserveRaisingFromCharger = new JTextField(Double.toString(UserPreferences.preferences.UAV_RESERVE_RAISING_FROM_CHARGER));
        uavFields.add(uavReserveRaisingFromCharger);
        JTextField uavReserverEndingChargingProcedure = new JTextField(Double.toString(UserPreferences.preferences.UAV_RESERVE_ENDING_CHARGING_PROCEDURE));
        uavFields.add(uavReserverEndingChargingProcedure);
        JTextField minimumFlightLevel = new JTextField(Double.toString(UserPreferences.preferences.MINIMUM_FLIGHT_LEVEL));
        uavFields.add(minimumFlightLevel);
        JTextField maximumFlightLevel = new JTextField(Double.toString(UserPreferences.preferences.MAXIMUM_FLIGHT_LEVEL));
        uavFields.add(maximumFlightLevel);
        JTextField distanceBetweenFlightLevels = new JTextField(Double.toString(UserPreferences.preferences.DISTANCE_BETWEEN_FLIGHT_LEVELS));
        uavFields.add(distanceBetweenFlightLevels);
        JTextField clickActiveAreaSize = new JTextField(Double.toString(UserPreferences.preferences.CLICK_ACTIVE_AREA_SIZE));
        uavFields.add(clickActiveAreaSize);
        JTextField spawningHeightAboveSpawnLevel = new JTextField(Double.toString(UserPreferences.preferences.SPAWNING_HEIGHT_ABOVE_SPAWN_LEVEL));
        uavFields.add(spawningHeightAboveSpawnLevel);
        JTextField spawnBreakAltitude = new JTextField(Double.toString(UserPreferences.preferences.SPAWN_BREAK_ALTITUDE));
        uavFields.add(spawnBreakAltitude);
        uavPanel.add(uavFields, BorderLayout.CENTER);

        tabbedPane.addTab("UAV", null, uavPanel, null);

        String[] buttons = {"Save", "Default", "Cancel"};
        int returnValue = JOptionPane.showOptionDialog(
                frame,
                tabbedPane,
                "Preferences",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                buttons,
                buttons[0]
        );

        if (returnValue == 0) {

            // General
            UserPreferences.preferences.UNREAL_TOURNAMENT_2004_HOME_FOLDER = ut2004HomeFolder.getText();
            UserPreferences.preferences.UNREAL_TOURNAMENT_2004_SCREEN_SHOTS_FOLDER = ut2004ScreenShotsFolder.getText();
            UserPreferences.preferences.DEFAULT_MAP_NAME = defaultMap.getText();

            // Birdview
            UserPreferences.preferences.UNREAL_TOURNAMENT_2004_HORIZONTAL_RESOLUTION = Double.parseDouble(unrealTournament2004HorizontalResolution.getText());
            UserPreferences.preferences.UNREAL_TOURNAMENT_2004_VERTICAL_RESOLUTION = Double.parseDouble(unrealTournament2004VerticalResolution.getText());
            UserPreferences.preferences.UNREAL_TOURNAMENT_2004_HORIZONTAL_FIELD_OF_VIEW = Double.parseDouble(unrealTournament2004HorizontalFieldOfView.getText());
            UserPreferences.preferences.FLIGHT_HEIGHT = Double.parseDouble(flightHeight.getText());

            // Map
            UserPreferences.preferences.MAP_DEFAULT_SCALE = Double.parseDouble(mapDefaultScale.getText());
            UserPreferences.preferences.MAP_SCALE_IN_FACTOR = Double.parseDouble(scaleInFactor.getText());
            UserPreferences.preferences.MAP_SCALE_OUT_FACTOR = Double.parseDouble(scaleOutFactor.getText());
            UserPreferences.preferences.MAP_MINIMAL_SCALE = Double.parseDouble(minimalScale.getText());
            UserPreferences.preferences.MAP_MAXIMUM_SCALE = Double.parseDouble(maximalScale.getText());
            UserPreferences.preferences.MAP_DRAG_INERTIA_SPEED = Double.parseDouble(dragInertiaSpeed.getText());
            UserPreferences.preferences.MAP_DRAG_INERTIA_COOLDOWN = Double.parseDouble(dragInertialCooldown.getText());
            UserPreferences.preferences.MAP_TIMER_DELAY = Integer.parseInt(dragInertialDelay.getText());

            // Server
            UserPreferences.preferences.SERVER_ADDRESS = serverAddress.getText();
            UserPreferences.preferences.SERVER_PORT = Integer.parseInt(serverPort.getText());

            // Simulation
            UserPreferences.preferences.SIMULATION_LOGIC_INTERVAL = Double.parseDouble(simulationLogicInterval.getText());
            UserPreferences.preferences.COVEREGE_ANALYSIS_FREQUENCY = Integer.parseInt(coverageAnalysisFrequency.getText());

            // Algorithm
            UserPreferences.preferences.COLLISION_DETECTION_ALTITUDE_DIFFERENCE_THRESHOLD = Double.parseDouble(collisionDetectionAltitudeDifferenceThreshold.getText());
            UserPreferences.preferences.IS_NEAR_DESTINATION_DISTANCE_THRESHOLD = Double.parseDouble(isNearDestinationDistanceThreshold.getText());
            UserPreferences.preferences.APPROACH_ANGLE_DIFFERENCE_THRESHOLD = Double.parseDouble(approachAngleDifferenceThreshold.getText());

            // Heat Map
            UserPreferences.preferences.CELL_WIDTH = Double.parseDouble(cellWidth.getText());
            UserPreferences.preferences.CELL_HEIGHT = Double.parseDouble(cellHeight.getText());
            UserPreferences.preferences.MAXIMAL_HEAT = Double.parseDouble(maximalHeat.getText());

            // UAV
            UserPreferences.preferences.UAV_RESERVE_FOR_GOING_BACK_TO_CHARGER = Double.parseDouble(uavReserveForGoingBackToCharger.getText());
            UserPreferences.preferences.UAV_RESERVE_STARTING_CHARGING_PROCEDURE = Double.parseDouble(uavReserveStartingChargingProcedure.getText());
            UserPreferences.preferences.UAV_RESERVE_LANDING_TO_CHARGER = Double.parseDouble(uavReserveLandingToCharger.getText());
            UserPreferences.preferences.UAV_RESERVE_RAISING_FROM_CHARGER = Double.parseDouble(uavReserveRaisingFromCharger.getText());
            UserPreferences.preferences.UAV_RESERVE_ENDING_CHARGING_PROCEDURE = Double.parseDouble(uavReserverEndingChargingProcedure.getText());
            UserPreferences.preferences.MINIMUM_FLIGHT_LEVEL = Double.parseDouble(minimumFlightLevel.getText());
            UserPreferences.preferences.MAXIMUM_FLIGHT_LEVEL = Double.parseDouble(maximumFlightLevel.getText());
            UserPreferences.preferences.DISTANCE_BETWEEN_FLIGHT_LEVELS = Double.parseDouble(distanceBetweenFlightLevels.getText());
            UserPreferences.preferences.CLICK_ACTIVE_AREA_SIZE = Double.parseDouble(clickActiveAreaSize.getText());
            UserPreferences.preferences.SPAWNING_HEIGHT_ABOVE_SPAWN_LEVEL = Double.parseDouble(spawningHeightAboveSpawnLevel.getText());
            UserPreferences.preferences.SPAWN_BREAK_ALTITUDE = Double.parseDouble(spawnBreakAltitude.getText());

            UserPreferences.save();
        }

        if (returnValue == 1) {
            UserPreferences.setDefault();
            UserPreferences.save();

            this.create();
        }
    }
}
