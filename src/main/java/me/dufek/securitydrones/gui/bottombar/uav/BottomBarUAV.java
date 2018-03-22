package me.dufek.securitydrones.gui.bottombar.uav;

import cz.cuni.amis.pogamut.base3d.worldview.object.Location;
import cz.cuni.amis.pogamut.base3d.worldview.object.Rotation;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import me.dufek.securitydrones.battery.Battery;
import me.dufek.securitydrones.gui.bottombar.BottomBarType;
import me.dufek.securitydrones.gui.bottombar.Preferences;
import me.dufek.securitydrones.conversion.NumberConversion;

/**
 *
 * @author Jan Dufek
 */
public class BottomBarUAV implements BottomBarType {

    private final Container pane;

    public JPanel titlePanel;
    public ImageIcon titleIcon;
    public JLabel titleText;

    public JPanel informationPanel;
//    public JPanel secondColumnPanel;
//    public JPanel thirdColumnPanel;

    public JLabel generalInformationTitle;
//    public JPanel generalInformationPanel;
    public JLabel startLocationTitle;
    public JLabel startLocation;
    public JLabel startTimeTitle;
    public JLabel startTime;
    public JLabel upTimeTitle;
    public JLabel upTime;
    public JLabel flightLevelTitle;
    public JLabel flightLevel;
    public JLabel swapTimeStandardDeviationTitle;
    public JLabel stateTitle;
    public JLabel state;

    public JLabel positionTitle;
    public JLabel actualAltitudeTitle;
    public JLabel actualAltitude;
    public JLabel actualRotationTitle;
    public JLabel actualRotation;
    public JLabel actualLocationTitle;
    public JLabel actualLocation;
    public JLabel realLocationTitle;
    public JLabel realLocation;
    public JLabel deviationFromRealLocationTitle;
    public JLabel deviationFromRealLocation;

    public JLabel velocityTitle;
    public JLabel actualVelocityTitle;
    public JLabel actualVelocity;
    public JLabel linearVelocityTitle;
    public JLabel linearVelocity;
    public JLabel altitudeVelocityTitle;
    public JLabel altitudeVelocity;
    public JLabel rotationalVelocityTitle;
    public JLabel rotationalVelocity;

    public JLabel batteryTitle;
//    public JPanel batteryPanel;
    public BatteryVisualization battery;
    public JLabel emptyTimeTitle;
    public JLabel emptyTime;
    public JLabel remainingTimeTitle;
    public JLabel remainingTime;
    public JLabel numberOfChangesTitle;
    public JLabel numberOfChanges;
    public JLabel amperageMeanTitle;
    public JLabel amperageMean;

    public JLabel chargingTitle;
    public JLabel chargingTimeTitle;
    public JLabel chargingTime;
    public JLabel chargingStationTitle;
    public JLabel chargingStation;
    public JLabel distanceToStationTitle;
    public JLabel distanceToStation;
    public JLabel flyingTimeToStationTitle;
    public JLabel flyingTimeToStation;

    public JLabel primarySensorTitle;
//    public JPanel primarySensorPanel;
    public JLabel primarySensorHorizontalFieldOfViewTitle;
    public JLabel primarySensorHorizontalFieldOfView;
    public JLabel primarySensorVerticalFieldOfViewTitle;
    public JLabel primarySensorVerticalFieldOfView;
    public JLabel visibleAreaWidthTitle;
    public JLabel visibleAreaWidth;
    public JLabel visibleAreaHeightTitle;
    public JLabel visibleAreaHeight;

    public JLabel objectivesTitle;
    public JLabel objectives;

    public JLabel sonarsTitle;
    public SonarVisualization sonars;

    public BottomBarUAV(Container pane) {
        this.pane = pane;
    }

    @Override
    public void create() {
        createTitle();

        createFirstColumn();
//        createSecondColumn();
//        createThirdColumn();

        createGeneralInformationTitle();
        createGeneralInformation();
        createPositionTitle();
        createPosition();
        createVelocityTitle();
        createVelocity();
        createBatteryTitle();
        createBattery();
        createPrimarySensorTitle();
        createPrimarySensor();
        createChargingTitle();
        createCharging();
        createObjectivesTitle();
        createObjectives();
        createSonarsTitle();
        createSonars();
    }

    private void createTitle() {
        titlePanel = new JPanel(new GridBagLayout());

        createTitleIcon();
        createTitleText();

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.weightx = 1;
        constraints.weighty = 0.1;
        constraints.insets = new Insets(0, 0, 0, 0);
        constraints.ipadx = 0;
        constraints.ipady = 0;

        pane.add(titlePanel, constraints);
    }

    private void createTitleIcon() {
        titleIcon = new ImageIcon("graphics/uav.png");

        JLabel iconLabel = new JLabel(titleIcon);

        iconLabel.setHorizontalAlignment(SwingConstants.LEFT);
        iconLabel.setVerticalAlignment(SwingConstants.CENTER);

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.weightx = 0.01;
        constraints.weighty = 1;
        constraints.insets = new Insets(0, 0, 0, 0);
        constraints.ipadx = 0;
        constraints.ipady = 0;

        titlePanel.add(iconLabel, constraints);
    }

    private void createTitleText() {
        titleText = new JLabel();

        titleText.setHorizontalAlignment(SwingConstants.LEFT);
        titleText.setVerticalAlignment(SwingConstants.CENTER);

        titleText.setFont(new Font(null, Preferences.TITLE_FONT_STYLE, Preferences.TITLE_FONT_SIZE));
        titleText.setForeground(Preferences.TITLE_FONT_COLOR);

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.weightx = 0.9;
        constraints.weighty = 1;
        constraints.insets = new Insets(0, 0, 0, 0);
        constraints.ipadx = 0;
        constraints.ipady = 0;

        titlePanel.add(titleText, constraints);
    }

    private void createFirstColumn() {
        informationPanel = new JPanel(new GridBagLayout());

        createGeneralInformationTitle();
        createGeneralInformation();
        createPosition();
        createPositionTitle();
        createVelocity();
        createVelocityTitle();

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.insets = new Insets(0, 0, 0, 0);
        constraints.ipadx = 0;
        constraints.ipady = 0;

        pane.add(informationPanel, constraints);
    }

//    private void createSecondColumn() {
//        secondColumnPanel = new JPanel(new GridBagLayout());
//
//        createBattery();
//        createBatteryTitle();
////        createSchedule();
////        createScheduleTitle();
////        createPrimarySensor();
////        createPrimarySensorTitle();
//
//        GridBagConstraints constraints = new GridBagConstraints();
//
//        constraints.gridx = 1;
//        constraints.gridy = 1;
//        constraints.gridwidth = 1;
//        constraints.gridheight = 1;
//        constraints.fill = GridBagConstraints.BOTH;
//        constraints.anchor = GridBagConstraints.CENTER;
//        constraints.weightx = 1;
//        constraints.weighty = 1;
//        constraints.insets = new Insets(0, 0, 0, 0);
//        constraints.ipadx = 0;
//        constraints.ipady = 0;
//
//        pane.add(secondColumnPanel, constraints);
//    }
//
//    private void createThirdColumn() {
//        thirdColumnPanel = new JPanel(new GridBagLayout());
//
////        createObjectives();
////        createObjectivesTitle();
////        createSonar();
////        createSonarTitle();
//        GridBagConstraints constraints = new GridBagConstraints();
//
//        constraints.gridx = 2;
//        constraints.gridy = 1;
//        constraints.gridwidth = 1;
//        constraints.gridheight = 1;
//        constraints.fill = GridBagConstraints.BOTH;
//        constraints.anchor = GridBagConstraints.CENTER;
//        constraints.weightx = 1;
//        constraints.weighty = 1;
//        constraints.insets = new Insets(0, 0, 0, 0);
//        constraints.ipadx = 0;
//        constraints.ipady = 0;
//
//        pane.add(thirdColumnPanel, constraints);
//    }
    private void createGeneralInformationTitle() {
        generalInformationTitle = new JLabel();
        generalInformationTitle.setText("General Information");

        generalInformationTitle.setHorizontalAlignment(SwingConstants.LEFT);
        generalInformationTitle.setVerticalAlignment(SwingConstants.CENTER);

        generalInformationTitle.setFont(new Font(null, Preferences.HEADING_1_FONT_STYLE, Preferences.HEADING_1_FONT_SIZE));
        generalInformationTitle.setForeground(Preferences.HEADING_1_FONT_COLOR);

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.insets = new Insets(0, 0, 0, 0);
        constraints.ipadx = 0;
        constraints.ipady = 0;

        informationPanel.add(generalInformationTitle, constraints);
    }

    private void createGeneralInformation() {
        createStartLocationTitle();
        createStartLocation();
        createStartTimeTitle();
        createStartTime();
        createUpTimeTitle();
        createUpTime();
        createFlightLevelTitle();
        createFlightLevel();
        createStateTitle();
        createState();
    }

    private void createStartLocationTitle() {
        startLocationTitle = new JLabel();

        startLocationTitle.setText("Start Location");

        startLocationTitle.setHorizontalAlignment(SwingConstants.LEFT);
        startLocationTitle.setVerticalAlignment(SwingConstants.CENTER);

        startLocationTitle.setFont(new Font(null, Preferences.NORMAL_TITLE_FONT_STYLE, Preferences.NORMAL_TITLE_FONT_SIZE));
        startLocationTitle.setForeground(Preferences.NORMAL_TITLE_FONT_COLOR);

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.insets = new Insets(0, 0, 0, 0);
        constraints.ipadx = 0;
        constraints.ipady = 0;

        informationPanel.add(startLocationTitle, constraints);
    }

    private void createStartLocation() {
        startLocation = new JLabel();

        startLocation.setHorizontalAlignment(SwingConstants.LEFT);
        startLocation.setVerticalAlignment(SwingConstants.CENTER);

        startLocation.setFont(new Font(null, Preferences.NORMAL_FONT_STYLE, Preferences.NORMAL_FONT_SIZE));
        startLocation.setForeground(Preferences.NORMAL_FONT_COLOR);

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.insets = new Insets(0, 0, 0, 0);
        constraints.ipadx = 0;
        constraints.ipady = 0;

        informationPanel.add(startLocation, constraints);
    }

    private void createStartTimeTitle() {
        startTimeTitle = new JLabel();

        startTimeTitle.setText("Start Time");

        startTimeTitle.setHorizontalAlignment(SwingConstants.LEFT);
        startTimeTitle.setVerticalAlignment(SwingConstants.CENTER);

        startTimeTitle.setFont(new Font(null, Preferences.NORMAL_TITLE_FONT_STYLE, Preferences.NORMAL_TITLE_FONT_SIZE));
        startTimeTitle.setForeground(Preferences.NORMAL_TITLE_FONT_COLOR);

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.insets = new Insets(0, 0, 0, 0);
        constraints.ipadx = 0;
        constraints.ipady = 0;

        informationPanel.add(startTimeTitle, constraints);
    }

    private void createStartTime() {
        startTime = new JLabel();

        startTime.setHorizontalAlignment(SwingConstants.LEFT);
        startTime.setVerticalAlignment(SwingConstants.CENTER);

        startTime.setFont(new Font(null, Preferences.NORMAL_FONT_STYLE, Preferences.NORMAL_FONT_SIZE));
        startTime.setForeground(Preferences.NORMAL_FONT_COLOR);

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 1;
        constraints.gridy = 2;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.insets = new Insets(0, 0, 0, 0);
        constraints.ipadx = 0;
        constraints.ipady = 0;

        informationPanel.add(startTime, constraints);
    }

    private void createUpTimeTitle() {
        upTimeTitle = new JLabel();

        upTimeTitle.setText("Up Time");

        upTimeTitle.setHorizontalAlignment(SwingConstants.LEFT);
        upTimeTitle.setVerticalAlignment(SwingConstants.CENTER);

        upTimeTitle.setFont(new Font(null, Preferences.NORMAL_TITLE_FONT_STYLE, Preferences.NORMAL_TITLE_FONT_SIZE));
        upTimeTitle.setForeground(Preferences.NORMAL_TITLE_FONT_COLOR);

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.insets = new Insets(0, 0, 0, 0);
        constraints.ipadx = 0;
        constraints.ipady = 0;

        informationPanel.add(upTimeTitle, constraints);
    }

    private void createUpTime() {
        upTime = new JLabel();

        upTime.setHorizontalAlignment(SwingConstants.LEFT);
        upTime.setVerticalAlignment(SwingConstants.CENTER);

        upTime.setFont(new Font(null, Preferences.NORMAL_FONT_STYLE, Preferences.NORMAL_FONT_SIZE));
        upTime.setForeground(Preferences.NORMAL_FONT_COLOR);

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 1;
        constraints.gridy = 3;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.insets = new Insets(0, 0, 0, 0);
        constraints.ipadx = 0;
        constraints.ipady = 0;

        informationPanel.add(upTime, constraints);
    }

    private void createFlightLevelTitle() {
        flightLevelTitle = new JLabel();

        flightLevelTitle.setText("Flight Level");

        flightLevelTitle.setHorizontalAlignment(SwingConstants.LEFT);
        flightLevelTitle.setVerticalAlignment(SwingConstants.CENTER);

        flightLevelTitle.setFont(new Font(null, Preferences.NORMAL_TITLE_FONT_STYLE, Preferences.NORMAL_TITLE_FONT_SIZE));
        flightLevelTitle.setForeground(Preferences.NORMAL_TITLE_FONT_COLOR);

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 0;
        constraints.gridy = 4;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.insets = new Insets(0, 0, 0, 0);
        constraints.ipadx = 0;
        constraints.ipady = 0;

        informationPanel.add(flightLevelTitle, constraints);
    }

    private void createFlightLevel() {
        flightLevel = new JLabel();

        flightLevel.setHorizontalAlignment(SwingConstants.LEFT);
        flightLevel.setVerticalAlignment(SwingConstants.CENTER);

        flightLevel.setFont(new Font(null, Preferences.NORMAL_FONT_STYLE, Preferences.NORMAL_FONT_SIZE));
        flightLevel.setForeground(Preferences.NORMAL_FONT_COLOR);

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 1;
        constraints.gridy = 4;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.insets = new Insets(0, 0, 0, 0);
        constraints.ipadx = 0;
        constraints.ipady = 0;

        informationPanel.add(flightLevel, constraints);
    }

    private void createStateTitle() {
        stateTitle = new JLabel();

        stateTitle.setText("State");

        stateTitle.setHorizontalAlignment(SwingConstants.LEFT);
        stateTitle.setVerticalAlignment(SwingConstants.CENTER);

        stateTitle.setFont(new Font(null, Preferences.NORMAL_TITLE_FONT_STYLE, Preferences.NORMAL_TITLE_FONT_SIZE));
        stateTitle.setForeground(Preferences.NORMAL_TITLE_FONT_COLOR);

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 0;
        constraints.gridy = 5;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.insets = new Insets(0, 0, 0, 0);
        constraints.ipadx = 0;
        constraints.ipady = 0;

        informationPanel.add(stateTitle, constraints);
    }

    private void createState() {
        state = new JLabel();

        state.setHorizontalAlignment(SwingConstants.LEFT);
        state.setVerticalAlignment(SwingConstants.CENTER);

        state.setFont(new Font(null, Preferences.NORMAL_FONT_STYLE, Preferences.NORMAL_FONT_SIZE));
        state.setForeground(Preferences.NORMAL_FONT_COLOR);

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 1;
        constraints.gridy = 5;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.insets = new Insets(0, 0, 0, 0);
        constraints.ipadx = 0;
        constraints.ipady = 0;

        informationPanel.add(state, constraints);
    }

    private void createPositionTitle() {
        positionTitle = new JLabel();
        positionTitle.setText("Position");

        positionTitle.setHorizontalAlignment(SwingConstants.LEFT);
        positionTitle.setVerticalAlignment(SwingConstants.CENTER);

        positionTitle.setFont(new Font(null, Preferences.HEADING_1_FONT_STYLE, Preferences.HEADING_1_FONT_SIZE));
        positionTitle.setForeground(Preferences.HEADING_1_FONT_COLOR);

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 0;
        constraints.gridy = 6;
        constraints.gridwidth = 2;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.insets = new Insets(0, 0, 0, 0);
        constraints.ipadx = 0;
        constraints.ipady = 0;

        informationPanel.add(positionTitle, constraints);
    }

    private void createPosition() {
        createActualAltitudeTitle();
        createActualAltitude();
        createActualRotationTitle();
        createActualRotation();
        createActualLocationTitle();
        createActualLocation();
        createRealLocationTitle();
        createRealLocation();
        createDeviationFromRealLocationTitle();
        createDeviationFromRealLocation();
    }

    private void createActualAltitudeTitle() {
        actualAltitudeTitle = new JLabel();

        actualAltitudeTitle.setText("Actual Altitude");

        actualAltitudeTitle.setHorizontalAlignment(SwingConstants.LEFT);
        actualAltitudeTitle.setVerticalAlignment(SwingConstants.CENTER);

        actualAltitudeTitle.setFont(new Font(null, Preferences.NORMAL_TITLE_FONT_STYLE, Preferences.NORMAL_TITLE_FONT_SIZE));
        actualAltitudeTitle.setForeground(Preferences.NORMAL_TITLE_FONT_COLOR);

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 0;
        constraints.gridy = 7;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.insets = new Insets(0, 0, 0, 0);
        constraints.ipadx = 0;
        constraints.ipady = 0;

        informationPanel.add(actualAltitudeTitle, constraints);
    }

    private void createActualAltitude() {
        actualAltitude = new JLabel();

        actualAltitude.setHorizontalAlignment(SwingConstants.LEFT);
        actualAltitude.setVerticalAlignment(SwingConstants.CENTER);

        actualAltitude.setFont(new Font(null, Preferences.NORMAL_FONT_STYLE, Preferences.NORMAL_FONT_SIZE));
        actualAltitude.setForeground(Preferences.NORMAL_FONT_COLOR);

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 1;
        constraints.gridy = 7;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.insets = new Insets(0, 0, 0, 0);
        constraints.ipadx = 0;
        constraints.ipady = 0;

        informationPanel.add(actualAltitude, constraints);
    }

    private void createActualRotationTitle() {
        actualRotationTitle = new JLabel();

        actualRotationTitle.setText("Actual Rotation");

        actualRotationTitle.setHorizontalAlignment(SwingConstants.LEFT);
        actualRotationTitle.setVerticalAlignment(SwingConstants.CENTER);

        actualRotationTitle.setFont(new Font(null, Preferences.NORMAL_TITLE_FONT_STYLE, Preferences.NORMAL_TITLE_FONT_SIZE));
        actualRotationTitle.setForeground(Preferences.NORMAL_TITLE_FONT_COLOR);

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 0;
        constraints.gridy = 8;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.insets = new Insets(0, 0, 0, 0);
        constraints.ipadx = 0;
        constraints.ipady = 0;

        informationPanel.add(actualRotationTitle, constraints);
    }

    private void createActualRotation() {
        actualRotation = new JLabel();

        actualRotation.setHorizontalAlignment(SwingConstants.LEFT);
        actualRotation.setVerticalAlignment(SwingConstants.CENTER);

        actualRotation.setFont(new Font(null, Preferences.NORMAL_FONT_STYLE, Preferences.NORMAL_FONT_SIZE));
        actualRotation.setForeground(Preferences.NORMAL_FONT_COLOR);

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 1;
        constraints.gridy = 8;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.insets = new Insets(0, 0, 0, 0);
        constraints.ipadx = 0;
        constraints.ipady = 0;

        informationPanel.add(actualRotation, constraints);
    }

    private void createActualLocationTitle() {
        actualLocationTitle = new JLabel();

        actualLocationTitle.setText("Actual Location");

        actualLocationTitle.setHorizontalAlignment(SwingConstants.LEFT);
        actualLocationTitle.setVerticalAlignment(SwingConstants.CENTER);

        actualLocationTitle.setFont(new Font(null, Preferences.NORMAL_TITLE_FONT_STYLE, Preferences.NORMAL_TITLE_FONT_SIZE));
        actualLocationTitle.setForeground(Preferences.NORMAL_TITLE_FONT_COLOR);

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 0;
        constraints.gridy = 9;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.insets = new Insets(0, 0, 0, 0);
        constraints.ipadx = 0;
        constraints.ipady = 0;

        informationPanel.add(actualLocationTitle, constraints);
    }

    private void createActualLocation() {
        if (actualLocation != null) { // TODO testing
            actualLocation.setText(null);
            actualLocation = null;
        }
        actualLocation = new JLabel();

        actualLocation.setHorizontalAlignment(SwingConstants.LEFT);
        actualLocation.setVerticalAlignment(SwingConstants.CENTER);

        actualLocation.setFont(new Font(null, Preferences.NORMAL_FONT_STYLE, Preferences.NORMAL_FONT_SIZE));
        actualLocation.setForeground(Preferences.NORMAL_FONT_COLOR);

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 1;
        constraints.gridy = 9;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.insets = new Insets(0, 0, 0, 0);
        constraints.ipadx = 0;
        constraints.ipady = 0;

        informationPanel.add(actualLocation, constraints);
    }

    private void createRealLocationTitle() {
        realLocationTitle = new JLabel();

        realLocationTitle.setText("Real Location");

        realLocationTitle.setHorizontalAlignment(SwingConstants.LEFT);
        realLocationTitle.setVerticalAlignment(SwingConstants.CENTER);

        realLocationTitle.setFont(new Font(null, Preferences.NORMAL_TITLE_FONT_STYLE, Preferences.NORMAL_TITLE_FONT_SIZE));
        realLocationTitle.setForeground(Preferences.NORMAL_TITLE_FONT_COLOR);

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 0;
        constraints.gridy = 10;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.insets = new Insets(0, 0, 0, 0);
        constraints.ipadx = 0;
        constraints.ipady = 0;

        informationPanel.add(realLocationTitle, constraints);
    }

    private void createRealLocation() {
        realLocation = new JLabel();

        realLocation.setHorizontalAlignment(SwingConstants.LEFT);
        realLocation.setVerticalAlignment(SwingConstants.CENTER);

        realLocation.setFont(new Font(null, Preferences.NORMAL_FONT_STYLE, Preferences.NORMAL_FONT_SIZE));
        realLocation.setForeground(Preferences.NORMAL_FONT_COLOR);

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 1;
        constraints.gridy = 10;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.insets = new Insets(0, 0, 0, 0);
        constraints.ipadx = 0;
        constraints.ipady = 0;

        informationPanel.add(realLocation, constraints);
    }

    private void createDeviationFromRealLocationTitle() {
        deviationFromRealLocationTitle = new JLabel();

        deviationFromRealLocationTitle.setText("Deviation from Real Location");

        deviationFromRealLocationTitle.setHorizontalAlignment(SwingConstants.LEFT);
        deviationFromRealLocationTitle.setVerticalAlignment(SwingConstants.CENTER);

        deviationFromRealLocationTitle.setFont(new Font(null, Preferences.NORMAL_TITLE_FONT_STYLE, Preferences.NORMAL_TITLE_FONT_SIZE));
        deviationFromRealLocationTitle.setForeground(Preferences.NORMAL_TITLE_FONT_COLOR);

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 0;
        constraints.gridy = 11;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.insets = new Insets(0, 0, 0, 0);
        constraints.ipadx = 0;
        constraints.ipady = 0;

        informationPanel.add(deviationFromRealLocationTitle, constraints);
    }

    private void createDeviationFromRealLocation() {
        deviationFromRealLocation = new JLabel();

        deviationFromRealLocation.setHorizontalAlignment(SwingConstants.LEFT);
        deviationFromRealLocation.setVerticalAlignment(SwingConstants.CENTER);

        deviationFromRealLocation.setFont(new Font(null, Preferences.NORMAL_FONT_STYLE, Preferences.NORMAL_FONT_SIZE));
        deviationFromRealLocation.setForeground(Preferences.NORMAL_FONT_COLOR);

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 1;
        constraints.gridy = 11;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.insets = new Insets(0, 0, 0, 0);
        constraints.ipadx = 0;
        constraints.ipady = 0;

        informationPanel.add(deviationFromRealLocation, constraints);
    }

    private void createVelocityTitle() {
        velocityTitle = new JLabel();
        velocityTitle.setText("Velocity");

        velocityTitle.setHorizontalAlignment(SwingConstants.LEFT);
        velocityTitle.setVerticalAlignment(SwingConstants.CENTER);

        velocityTitle.setFont(new Font(null, Preferences.HEADING_1_FONT_STYLE, Preferences.HEADING_1_FONT_SIZE));
        velocityTitle.setForeground(Preferences.HEADING_1_FONT_COLOR);

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 0;
        constraints.gridy = 12;
        constraints.gridwidth = 2;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.insets = new Insets(0, 0, 0, 0);
        constraints.ipadx = 0;
        constraints.ipady = 0;

        informationPanel.add(velocityTitle, constraints);
    }

    private void createVelocity() {
        createActualVelocityTitle();
        createActualVelocity();
        createLinearVelocityTitle();
        createLinearVelocity();
        createAltitudeVelocityTitle();
        createAltitudeVelocity();
        createRotationalVelocityTitle();
        createRotationalVelocity();
    }

    private void createActualVelocityTitle() {
        actualVelocityTitle = new JLabel();

        actualVelocityTitle.setText("Actual Velocity");

        actualVelocityTitle.setHorizontalAlignment(SwingConstants.LEFT);
        actualVelocityTitle.setVerticalAlignment(SwingConstants.CENTER);

        actualVelocityTitle.setFont(new Font(null, Preferences.NORMAL_TITLE_FONT_STYLE, Preferences.NORMAL_TITLE_FONT_SIZE));
        actualVelocityTitle.setForeground(Preferences.NORMAL_TITLE_FONT_COLOR);

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 0;
        constraints.gridy = 13;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.insets = new Insets(0, 0, 0, 0);
        constraints.ipadx = 0;
        constraints.ipady = 0;

        informationPanel.add(actualVelocityTitle, constraints);
    }

    private void createActualVelocity() {
        actualVelocity = new JLabel();

        actualVelocity.setHorizontalAlignment(SwingConstants.LEFT);
        actualVelocity.setVerticalAlignment(SwingConstants.CENTER);

        actualVelocity.setFont(new Font(null, Preferences.NORMAL_FONT_STYLE, Preferences.NORMAL_FONT_SIZE));
        actualVelocity.setForeground(Preferences.NORMAL_FONT_COLOR);

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 1;
        constraints.gridy = 13;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.insets = new Insets(0, 0, 0, 0);
        constraints.ipadx = 0;
        constraints.ipady = 0;

        informationPanel.add(actualVelocity, constraints);
    }

    private void createLinearVelocityTitle() {
        linearVelocityTitle = new JLabel();

        linearVelocityTitle.setText("Linear Velocity");

        linearVelocityTitle.setHorizontalAlignment(SwingConstants.LEFT);
        linearVelocityTitle.setVerticalAlignment(SwingConstants.CENTER);

        linearVelocityTitle.setFont(new Font(null, Preferences.NORMAL_TITLE_FONT_STYLE, Preferences.NORMAL_TITLE_FONT_SIZE));
        linearVelocityTitle.setForeground(Preferences.NORMAL_TITLE_FONT_COLOR);

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 0;
        constraints.gridy = 14;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.insets = new Insets(0, 0, 0, 0);
        constraints.ipadx = 0;
        constraints.ipady = 0;

        informationPanel.add(linearVelocityTitle, constraints);
    }

    private void createLinearVelocity() {
        linearVelocity = new JLabel();

        linearVelocity.setHorizontalAlignment(SwingConstants.LEFT);
        linearVelocity.setVerticalAlignment(SwingConstants.CENTER);

        linearVelocity.setFont(new Font(null, Preferences.NORMAL_FONT_STYLE, Preferences.NORMAL_FONT_SIZE));
        linearVelocity.setForeground(Preferences.NORMAL_FONT_COLOR);

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 1;
        constraints.gridy = 14;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.insets = new Insets(0, 0, 0, 0);
        constraints.ipadx = 0;
        constraints.ipady = 0;

        informationPanel.add(linearVelocity, constraints);
    }

    private void createAltitudeVelocityTitle() {
        altitudeVelocityTitle = new JLabel();

        altitudeVelocityTitle.setText("Altitude Velocity");

        altitudeVelocityTitle.setHorizontalAlignment(SwingConstants.LEFT);
        altitudeVelocityTitle.setVerticalAlignment(SwingConstants.CENTER);

        altitudeVelocityTitle.setFont(new Font(null, Preferences.NORMAL_TITLE_FONT_STYLE, Preferences.NORMAL_TITLE_FONT_SIZE));
        altitudeVelocityTitle.setForeground(Preferences.NORMAL_TITLE_FONT_COLOR);

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 0;
        constraints.gridy = 15;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.insets = new Insets(0, 0, 0, 0);
        constraints.ipadx = 0;
        constraints.ipady = 0;

        informationPanel.add(altitudeVelocityTitle, constraints);
    }

    private void createAltitudeVelocity() {
        altitudeVelocity = new JLabel();

        altitudeVelocity.setHorizontalAlignment(SwingConstants.LEFT);
        altitudeVelocity.setVerticalAlignment(SwingConstants.CENTER);

        altitudeVelocity.setFont(new Font(null, Preferences.NORMAL_FONT_STYLE, Preferences.NORMAL_FONT_SIZE));
        altitudeVelocity.setForeground(Preferences.NORMAL_FONT_COLOR);

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 1;
        constraints.gridy = 15;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.insets = new Insets(0, 0, 0, 0);
        constraints.ipadx = 0;
        constraints.ipady = 0;

        informationPanel.add(altitudeVelocity, constraints);
    }

    private void createRotationalVelocityTitle() {
        rotationalVelocityTitle = new JLabel();

        rotationalVelocityTitle.setText("Rotational Velocity");

        rotationalVelocityTitle.setHorizontalAlignment(SwingConstants.LEFT);
        rotationalVelocityTitle.setVerticalAlignment(SwingConstants.CENTER);

        rotationalVelocityTitle.setFont(new Font(null, Preferences.NORMAL_TITLE_FONT_STYLE, Preferences.NORMAL_TITLE_FONT_SIZE));
        rotationalVelocityTitle.setForeground(Preferences.NORMAL_TITLE_FONT_COLOR);

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 0;
        constraints.gridy = 16;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.insets = new Insets(0, 0, 0, 0);
        constraints.ipadx = 0;
        constraints.ipady = 0;

        informationPanel.add(rotationalVelocityTitle, constraints);
    }

    private void createRotationalVelocity() {
        rotationalVelocity = new JLabel();

        rotationalVelocity.setHorizontalAlignment(SwingConstants.LEFT);
        rotationalVelocity.setVerticalAlignment(SwingConstants.CENTER);

        rotationalVelocity.setFont(new Font(null, Preferences.NORMAL_FONT_STYLE, Preferences.NORMAL_FONT_SIZE));
        rotationalVelocity.setForeground(Preferences.NORMAL_FONT_COLOR);

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 1;
        constraints.gridy = 16;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.insets = new Insets(0, 0, 0, 0);
        constraints.ipadx = 0;
        constraints.ipady = 0;

        informationPanel.add(rotationalVelocity, constraints);
    }

    private void createBatteryTitle() {
        batteryTitle = new JLabel();
        batteryTitle.setText("Battery");

        batteryTitle.setHorizontalAlignment(SwingConstants.LEFT);
        batteryTitle.setVerticalAlignment(SwingConstants.CENTER);

        batteryTitle.setFont(new Font(null, Preferences.HEADING_1_FONT_STYLE, Preferences.HEADING_1_FONT_SIZE));
        batteryTitle.setForeground(Preferences.HEADING_1_FONT_COLOR);

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 2;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.insets = new Insets(0, 0, 0, 0);
        constraints.ipadx = 0;
        constraints.ipady = 0;

        informationPanel.add(batteryTitle, constraints);
    }

    private void createBattery() {
        createBatteryVisualization();
        createEmptyTimeTitle();
        createEmptyTime();
        createRemainingTimeTitle();
        createRemainingTime();
        createNumberOfChangesTitle();
        createNumberOfChanges();
        createAmperageMeanTitle();
        createAmperageMean();
    }

    private void createBatteryVisualization() {

        battery = new BatteryVisualization(informationPanel);

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 2;
        constraints.gridy = 1;
        constraints.gridwidth = 2;
        constraints.gridheight = 2;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.insets = new Insets(0, 0, 0, 0);
        constraints.ipadx = 0;
        constraints.ipady = 0;

        informationPanel.add(battery, constraints);
    }

    private void createEmptyTimeTitle() {
        emptyTimeTitle = new JLabel();

        emptyTimeTitle.setText("Empty Time");

        emptyTimeTitle.setHorizontalAlignment(SwingConstants.LEFT);
        emptyTimeTitle.setVerticalAlignment(SwingConstants.CENTER);

        emptyTimeTitle.setFont(new Font(null, Preferences.NORMAL_TITLE_FONT_STYLE, Preferences.NORMAL_TITLE_FONT_SIZE));
        emptyTimeTitle.setForeground(Preferences.NORMAL_TITLE_FONT_COLOR);

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 2;
        constraints.gridy = 3;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.insets = new Insets(0, 0, 0, 0);
        constraints.ipadx = 0;
        constraints.ipady = 0;

        informationPanel.add(emptyTimeTitle, constraints);
    }

    private void createEmptyTime() {
        emptyTime = new JLabel();

        emptyTime.setHorizontalAlignment(SwingConstants.LEFT);
        emptyTime.setVerticalAlignment(SwingConstants.CENTER);

        emptyTime.setFont(new Font(null, Preferences.NORMAL_FONT_STYLE, Preferences.NORMAL_FONT_SIZE));
        emptyTime.setForeground(Preferences.NORMAL_FONT_COLOR);

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 3;
        constraints.gridy = 3;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.insets = new Insets(0, 0, 0, 0);
        constraints.ipadx = 0;
        constraints.ipady = 0;

        informationPanel.add(emptyTime, constraints);
    }

    private void createRemainingTimeTitle() {
        remainingTimeTitle = new JLabel();

        remainingTimeTitle.setText("Remaining Time");

        remainingTimeTitle.setHorizontalAlignment(SwingConstants.LEFT);
        remainingTimeTitle.setVerticalAlignment(SwingConstants.CENTER);

        remainingTimeTitle.setFont(new Font(null, Preferences.NORMAL_TITLE_FONT_STYLE, Preferences.NORMAL_TITLE_FONT_SIZE));
        remainingTimeTitle.setForeground(Preferences.NORMAL_TITLE_FONT_COLOR);

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 2;
        constraints.gridy = 4;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.insets = new Insets(0, 0, 0, 0);
        constraints.ipadx = 0;
        constraints.ipady = 0;

        informationPanel.add(remainingTimeTitle, constraints);
    }

    private void createRemainingTime() {
        remainingTime = new JLabel();

        remainingTime.setHorizontalAlignment(SwingConstants.LEFT);
        remainingTime.setVerticalAlignment(SwingConstants.CENTER);

        remainingTime.setFont(new Font(null, Preferences.NORMAL_FONT_STYLE, Preferences.NORMAL_FONT_SIZE));
        remainingTime.setForeground(Preferences.NORMAL_FONT_COLOR);

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 3;
        constraints.gridy = 4;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.insets = new Insets(0, 0, 0, 0);
        constraints.ipadx = 0;
        constraints.ipady = 0;

        informationPanel.add(remainingTime, constraints);
    }

    private void createNumberOfChangesTitle() {
        numberOfChangesTitle = new JLabel();

        numberOfChangesTitle.setText("Number of Changes");

        numberOfChangesTitle.setHorizontalAlignment(SwingConstants.LEFT);
        numberOfChangesTitle.setVerticalAlignment(SwingConstants.CENTER);

        numberOfChangesTitle.setFont(new Font(null, Preferences.NORMAL_TITLE_FONT_STYLE, Preferences.NORMAL_TITLE_FONT_SIZE));
        numberOfChangesTitle.setForeground(Preferences.NORMAL_TITLE_FONT_COLOR);

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 2;
        constraints.gridy = 5;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.insets = new Insets(0, 0, 0, 0);
        constraints.ipadx = 0;
        constraints.ipady = 0;

        informationPanel.add(numberOfChangesTitle, constraints);
    }

    private void createNumberOfChanges() {
        numberOfChanges = new JLabel();

        numberOfChanges.setHorizontalAlignment(SwingConstants.LEFT);
        numberOfChanges.setVerticalAlignment(SwingConstants.CENTER);

        numberOfChanges.setFont(new Font(null, Preferences.NORMAL_FONT_STYLE, Preferences.NORMAL_FONT_SIZE));
        numberOfChanges.setForeground(Preferences.NORMAL_FONT_COLOR);

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 3;
        constraints.gridy = 5;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.insets = new Insets(0, 0, 0, 0);
        constraints.ipadx = 0;
        constraints.ipady = 0;

        informationPanel.add(numberOfChanges, constraints);
    }

    private void createAmperageMeanTitle() {
        amperageMeanTitle = new JLabel();

        amperageMeanTitle.setText("Amperage Mean");

        amperageMeanTitle.setHorizontalAlignment(SwingConstants.LEFT);
        amperageMeanTitle.setVerticalAlignment(SwingConstants.CENTER);

        amperageMeanTitle.setFont(new Font(null, Preferences.NORMAL_TITLE_FONT_STYLE, Preferences.NORMAL_TITLE_FONT_SIZE));
        amperageMeanTitle.setForeground(Preferences.NORMAL_TITLE_FONT_COLOR);

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 2;
        constraints.gridy = 6;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.insets = new Insets(0, 0, 0, 0);
        constraints.ipadx = 0;
        constraints.ipady = 0;

        informationPanel.add(amperageMeanTitle, constraints);
    }

    private void createAmperageMean() {
        amperageMean = new JLabel();

        amperageMean.setHorizontalAlignment(SwingConstants.LEFT);
        amperageMean.setVerticalAlignment(SwingConstants.CENTER);

        amperageMean.setFont(new Font(null, Preferences.NORMAL_FONT_STYLE, Preferences.NORMAL_FONT_SIZE));
        amperageMean.setForeground(Preferences.NORMAL_FONT_COLOR);

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 3;
        constraints.gridy = 6;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.insets = new Insets(0, 0, 0, 0);
        constraints.ipadx = 0;
        constraints.ipady = 0;

        informationPanel.add(amperageMean, constraints);
    }

    private void createChargingTitle() {
        chargingTitle = new JLabel();
        chargingTitle.setText("Charging");

        chargingTitle.setHorizontalAlignment(SwingConstants.LEFT);
        chargingTitle.setVerticalAlignment(SwingConstants.CENTER);

        chargingTitle.setFont(new Font(null, Preferences.HEADING_1_FONT_STYLE, Preferences.HEADING_1_FONT_SIZE));
        chargingTitle.setForeground(Preferences.HEADING_1_FONT_COLOR);

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 2;
        constraints.gridy = 7;
        constraints.gridwidth = 2;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.insets = new Insets(0, 0, 0, 0);
        constraints.ipadx = 0;
        constraints.ipady = 0;

        informationPanel.add(chargingTitle, constraints);
    }

    private void createCharging() {
        createChargingTimeTitle();
        createChargingTime();
        createChargingStationTitle();
        createChargingStation();
        createDistanceToStationTitle();
        createDistanceToStation();
        createFlyingTimeToStationTitle();
        createFlyingTimeToStation();
    }

    private void createChargingTimeTitle() {
        chargingTimeTitle = new JLabel();

        chargingTimeTitle.setText("Charging Time");

        chargingTimeTitle.setHorizontalAlignment(SwingConstants.LEFT);
        chargingTimeTitle.setVerticalAlignment(SwingConstants.CENTER);

        chargingTimeTitle.setFont(new Font(null, Preferences.NORMAL_TITLE_FONT_STYLE, Preferences.NORMAL_TITLE_FONT_SIZE));
        chargingTimeTitle.setForeground(Preferences.NORMAL_TITLE_FONT_COLOR);

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 2;
        constraints.gridy = 8;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.insets = new Insets(0, 0, 0, 0);
        constraints.ipadx = 0;
        constraints.ipady = 0;

        informationPanel.add(chargingTimeTitle, constraints);
    }

    private void createChargingTime() {
        chargingTime = new JLabel();

        chargingTime.setHorizontalAlignment(SwingConstants.LEFT);
        chargingTime.setVerticalAlignment(SwingConstants.CENTER);

        chargingTime.setFont(new Font(null, Preferences.NORMAL_FONT_STYLE, Preferences.NORMAL_FONT_SIZE));
        chargingTime.setForeground(Preferences.NORMAL_FONT_COLOR);

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 3;
        constraints.gridy = 8;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.insets = new Insets(0, 0, 0, 0);
        constraints.ipadx = 0;
        constraints.ipady = 0;

        informationPanel.add(chargingTime, constraints);
    }

    private void createChargingStationTitle() {
        chargingStationTitle = new JLabel();

        chargingStationTitle.setText("Charging Station");

        chargingStationTitle.setHorizontalAlignment(SwingConstants.LEFT);
        chargingStationTitle.setVerticalAlignment(SwingConstants.CENTER);

        chargingStationTitle.setFont(new Font(null, Preferences.NORMAL_TITLE_FONT_STYLE, Preferences.NORMAL_TITLE_FONT_SIZE));
        chargingStationTitle.setForeground(Preferences.NORMAL_TITLE_FONT_COLOR);

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 2;
        constraints.gridy = 9;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.insets = new Insets(0, 0, 0, 0);
        constraints.ipadx = 0;
        constraints.ipady = 0;

        informationPanel.add(chargingStationTitle, constraints);
    }

    private void createChargingStation() {
        chargingStation = new JLabel();

        chargingStation.setHorizontalAlignment(SwingConstants.LEFT);
        chargingStation.setVerticalAlignment(SwingConstants.CENTER);

        chargingStation.setFont(new Font(null, Preferences.NORMAL_FONT_STYLE, Preferences.NORMAL_FONT_SIZE));
        chargingStation.setForeground(Preferences.NORMAL_FONT_COLOR);

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 3;
        constraints.gridy = 9;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.insets = new Insets(0, 0, 0, 0);
        constraints.ipadx = 0;
        constraints.ipady = 0;

        informationPanel.add(chargingStation, constraints);
    }

    private void createDistanceToStationTitle() {
        distanceToStationTitle = new JLabel();

        distanceToStationTitle.setText("Distance to Station");

        distanceToStationTitle.setHorizontalAlignment(SwingConstants.LEFT);
        distanceToStationTitle.setVerticalAlignment(SwingConstants.CENTER);

        distanceToStationTitle.setFont(new Font(null, Preferences.NORMAL_TITLE_FONT_STYLE, Preferences.NORMAL_TITLE_FONT_SIZE));
        distanceToStationTitle.setForeground(Preferences.NORMAL_TITLE_FONT_COLOR);

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 2;
        constraints.gridy = 10;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.insets = new Insets(0, 0, 0, 0);
        constraints.ipadx = 0;
        constraints.ipady = 0;

        informationPanel.add(distanceToStationTitle, constraints);
    }

    private void createDistanceToStation() {
        distanceToStation = new JLabel();

        distanceToStation.setHorizontalAlignment(SwingConstants.LEFT);
        distanceToStation.setVerticalAlignment(SwingConstants.CENTER);

        distanceToStation.setFont(new Font(null, Preferences.NORMAL_FONT_STYLE, Preferences.NORMAL_FONT_SIZE));
        distanceToStation.setForeground(Preferences.NORMAL_FONT_COLOR);

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 3;
        constraints.gridy = 10;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.insets = new Insets(0, 0, 0, 0);
        constraints.ipadx = 0;
        constraints.ipady = 0;

        informationPanel.add(distanceToStation, constraints);
    }

    private void createFlyingTimeToStationTitle() {
        flyingTimeToStationTitle = new JLabel();

        flyingTimeToStationTitle.setText("Flying Time to Station");

        flyingTimeToStationTitle.setHorizontalAlignment(SwingConstants.LEFT);
        flyingTimeToStationTitle.setVerticalAlignment(SwingConstants.CENTER);

        flyingTimeToStationTitle.setFont(new Font(null, Preferences.NORMAL_TITLE_FONT_STYLE, Preferences.NORMAL_TITLE_FONT_SIZE));
        flyingTimeToStationTitle.setForeground(Preferences.NORMAL_TITLE_FONT_COLOR);

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 2;
        constraints.gridy = 11;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.insets = new Insets(0, 0, 0, 0);
        constraints.ipadx = 0;
        constraints.ipady = 0;

        informationPanel.add(flyingTimeToStationTitle, constraints);
    }

    private void createFlyingTimeToStation() {
        flyingTimeToStation = new JLabel();

        flyingTimeToStation.setHorizontalAlignment(SwingConstants.LEFT);
        flyingTimeToStation.setVerticalAlignment(SwingConstants.CENTER);

        flyingTimeToStation.setFont(new Font(null, Preferences.NORMAL_FONT_STYLE, Preferences.NORMAL_FONT_SIZE));
        flyingTimeToStation.setForeground(Preferences.NORMAL_FONT_COLOR);

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 3;
        constraints.gridy = 11;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.insets = new Insets(0, 0, 0, 0);
        constraints.ipadx = 0;
        constraints.ipady = 0;

        informationPanel.add(flyingTimeToStation, constraints);
    }

    private void createPrimarySensorTitle() {
        primarySensorTitle = new JLabel();
        primarySensorTitle.setText("Primary Sensor");

        primarySensorTitle.setHorizontalAlignment(SwingConstants.LEFT);
        primarySensorTitle.setVerticalAlignment(SwingConstants.CENTER);

        primarySensorTitle.setFont(new Font(null, Preferences.HEADING_1_FONT_STYLE, Preferences.HEADING_1_FONT_SIZE));
        primarySensorTitle.setForeground(Preferences.HEADING_1_FONT_COLOR);

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 2;
        constraints.gridy = 12;
        constraints.gridwidth = 2;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.insets = new Insets(0, 0, 0, 0);
        constraints.ipadx = 0;
        constraints.ipady = 0;

        informationPanel.add(primarySensorTitle, constraints);
    }

    private void createPrimarySensor() {
        createPrimarySensorHorizontalFieldOfViewTitle();
        createPrimarySensorHorizontalFieldOfView();
        createPrimarySensorVerticalFieldOfViewTitle();
        createPrimarySensorVerticalFieldOfView();
        createVisibleAreaWidthTitle();
        createVisibleAreaWidth();
        createVisibleAreaHeightTitle();
        createVisibleAreaHeight();
    }

    private void createPrimarySensorHorizontalFieldOfViewTitle() {
        primarySensorHorizontalFieldOfViewTitle = new JLabel();

        primarySensorHorizontalFieldOfViewTitle.setText("Horizontal FOV");

        primarySensorHorizontalFieldOfViewTitle.setHorizontalAlignment(SwingConstants.LEFT);
        primarySensorHorizontalFieldOfViewTitle.setVerticalAlignment(SwingConstants.CENTER);

        primarySensorHorizontalFieldOfViewTitle.setFont(new Font(null, Preferences.NORMAL_TITLE_FONT_STYLE, Preferences.NORMAL_TITLE_FONT_SIZE));
        primarySensorHorizontalFieldOfViewTitle.setForeground(Preferences.NORMAL_TITLE_FONT_COLOR);

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 2;
        constraints.gridy = 13;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.insets = new Insets(0, 0, 0, 0);
        constraints.ipadx = 0;
        constraints.ipady = 0;

        informationPanel.add(primarySensorHorizontalFieldOfViewTitle, constraints);
    }

    private void createPrimarySensorHorizontalFieldOfView() {
        primarySensorHorizontalFieldOfView = new JLabel();

        primarySensorHorizontalFieldOfView.setHorizontalAlignment(SwingConstants.LEFT);
        primarySensorHorizontalFieldOfView.setVerticalAlignment(SwingConstants.CENTER);

        primarySensorHorizontalFieldOfView.setFont(new Font(null, Preferences.NORMAL_FONT_STYLE, Preferences.NORMAL_FONT_SIZE));
        primarySensorHorizontalFieldOfView.setForeground(Preferences.NORMAL_FONT_COLOR);

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 3;
        constraints.gridy = 13;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.insets = new Insets(0, 0, 0, 0);
        constraints.ipadx = 0;
        constraints.ipady = 0;

        informationPanel.add(primarySensorHorizontalFieldOfView, constraints);
    }

    private void createPrimarySensorVerticalFieldOfViewTitle() {
        primarySensorVerticalFieldOfViewTitle = new JLabel();

        primarySensorVerticalFieldOfViewTitle.setText("Vertical FOV");

        primarySensorVerticalFieldOfViewTitle.setHorizontalAlignment(SwingConstants.LEFT);
        primarySensorVerticalFieldOfViewTitle.setVerticalAlignment(SwingConstants.CENTER);

        primarySensorVerticalFieldOfViewTitle.setFont(new Font(null, Preferences.NORMAL_TITLE_FONT_STYLE, Preferences.NORMAL_TITLE_FONT_SIZE));
        primarySensorVerticalFieldOfViewTitle.setForeground(Preferences.NORMAL_TITLE_FONT_COLOR);

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 2;
        constraints.gridy = 14;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.insets = new Insets(0, 0, 0, 0);
        constraints.ipadx = 0;
        constraints.ipady = 0;

        informationPanel.add(primarySensorVerticalFieldOfViewTitle, constraints);
    }

    private void createPrimarySensorVerticalFieldOfView() {
        primarySensorVerticalFieldOfView = new JLabel();

        primarySensorVerticalFieldOfView.setHorizontalAlignment(SwingConstants.LEFT);
        primarySensorVerticalFieldOfView.setVerticalAlignment(SwingConstants.CENTER);

        primarySensorVerticalFieldOfView.setFont(new Font(null, Preferences.NORMAL_FONT_STYLE, Preferences.NORMAL_FONT_SIZE));
        primarySensorVerticalFieldOfView.setForeground(Preferences.NORMAL_FONT_COLOR);

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 3;
        constraints.gridy = 14;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.insets = new Insets(0, 0, 0, 0);
        constraints.ipadx = 0;
        constraints.ipady = 0;

        informationPanel.add(primarySensorVerticalFieldOfView, constraints);
    }

    private void createVisibleAreaWidthTitle() {
        visibleAreaWidthTitle = new JLabel();

        visibleAreaWidthTitle.setText("Visible Area Width");

        visibleAreaWidthTitle.setHorizontalAlignment(SwingConstants.LEFT);
        visibleAreaWidthTitle.setVerticalAlignment(SwingConstants.CENTER);

        visibleAreaWidthTitle.setFont(new Font(null, Preferences.NORMAL_TITLE_FONT_STYLE, Preferences.NORMAL_TITLE_FONT_SIZE));
        visibleAreaWidthTitle.setForeground(Preferences.NORMAL_TITLE_FONT_COLOR);

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 2;
        constraints.gridy = 15;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.insets = new Insets(0, 0, 0, 0);
        constraints.ipadx = 0;
        constraints.ipady = 0;

        informationPanel.add(visibleAreaWidthTitle, constraints);
    }

    private void createVisibleAreaWidth() {
        visibleAreaWidth = new JLabel();

        visibleAreaWidth.setHorizontalAlignment(SwingConstants.LEFT);
        visibleAreaWidth.setVerticalAlignment(SwingConstants.CENTER);

        visibleAreaWidth.setFont(new Font(null, Preferences.NORMAL_FONT_STYLE, Preferences.NORMAL_FONT_SIZE));
        visibleAreaWidth.setForeground(Preferences.NORMAL_FONT_COLOR);

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 3;
        constraints.gridy = 15;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.insets = new Insets(0, 0, 0, 0);
        constraints.ipadx = 0;
        constraints.ipady = 0;

        informationPanel.add(visibleAreaWidth, constraints);
    }

    private void createVisibleAreaHeightTitle() {
        visibleAreaHeightTitle = new JLabel();

        visibleAreaHeightTitle.setText("Visible Area Height");

        visibleAreaHeightTitle.setHorizontalAlignment(SwingConstants.LEFT);
        visibleAreaHeightTitle.setVerticalAlignment(SwingConstants.CENTER);

        visibleAreaHeightTitle.setFont(new Font(null, Preferences.NORMAL_TITLE_FONT_STYLE, Preferences.NORMAL_TITLE_FONT_SIZE));
        visibleAreaHeightTitle.setForeground(Preferences.NORMAL_TITLE_FONT_COLOR);

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 2;
        constraints.gridy = 16;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.insets = new Insets(0, 0, 0, 0);
        constraints.ipadx = 0;
        constraints.ipady = 0;

        informationPanel.add(visibleAreaHeightTitle, constraints);
    }

    private void createVisibleAreaHeight() {
        visibleAreaHeight = new JLabel();

        visibleAreaHeight.setHorizontalAlignment(SwingConstants.LEFT);
        visibleAreaHeight.setVerticalAlignment(SwingConstants.CENTER);

        visibleAreaHeight.setFont(new Font(null, Preferences.NORMAL_FONT_STYLE, Preferences.NORMAL_FONT_SIZE));
        visibleAreaHeight.setForeground(Preferences.NORMAL_FONT_COLOR);

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 3;
        constraints.gridy = 16;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.insets = new Insets(0, 0, 0, 0);
        constraints.ipadx = 0;
        constraints.ipady = 0;

        informationPanel.add(visibleAreaHeight, constraints);
    }

    private void createObjectivesTitle() {
        objectivesTitle = new JLabel();
        objectivesTitle.setText("Objectives");

        objectivesTitle.setHorizontalAlignment(SwingConstants.LEFT);
        objectivesTitle.setVerticalAlignment(SwingConstants.CENTER);

        objectivesTitle.setFont(new Font(null, Preferences.HEADING_1_FONT_STYLE, Preferences.HEADING_1_FONT_SIZE));
        objectivesTitle.setForeground(Preferences.HEADING_1_FONT_COLOR);

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 4;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.insets = new Insets(0, 0, 0, 0);
        constraints.ipadx = 0;
        constraints.ipady = 0;

        informationPanel.add(objectivesTitle, constraints);
    }

    private void createObjectives() {
        objectives = new JLabel();
//        objectives.setText("Objectives list");

        objectives.setHorizontalAlignment(SwingConstants.LEFT);
        objectives.setVerticalAlignment(SwingConstants.TOP);

        objectives.setFont(new Font(null, Preferences.NORMAL_TITLE_FONT_STYLE, Preferences.NORMAL_TITLE_FONT_SIZE));
        objectives.setForeground(Preferences.NORMAL_TITLE_FONT_COLOR);

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 4;
        constraints.gridy = 1;
        constraints.gridwidth = 1;
        constraints.gridheight = 8;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.insets = new Insets(0, 0, 0, 0);
        constraints.ipadx = 0;
        constraints.ipady = 0;

//        JScrollPane objectivesScrollPane = new JScrollPane(objectives, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
//        ObjectivesVisualization objectivesScrollPane = new ObjectivesVisualization(objectives, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        informationPanel.add(objectives, constraints);
    }

    private void createSonarsTitle() {
        sonarsTitle = new JLabel();
        sonarsTitle.setText("Sonars");

        sonarsTitle.setHorizontalAlignment(SwingConstants.LEFT);
        sonarsTitle.setVerticalAlignment(SwingConstants.CENTER);

        sonarsTitle.setFont(new Font(null, Preferences.HEADING_1_FONT_STYLE, Preferences.HEADING_1_FONT_SIZE));
        sonarsTitle.setForeground(Preferences.HEADING_1_FONT_COLOR);

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 4;
        constraints.gridy = 9;
        constraints.gridwidth = 2;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.insets = new Insets(0, 0, 0, 0);
        constraints.ipadx = 0;
        constraints.ipady = 0;

        informationPanel.add(sonarsTitle, constraints);
    }

    private void createSonars() {
        sonars = new SonarVisualization(informationPanel);

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 4;
        constraints.gridy = 10;
        constraints.gridwidth = 1;
        constraints.gridheight = 7;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.insets = new Insets(0, 0, 0, 0);
        constraints.ipadx = 0;
        constraints.ipady = 0;

        informationPanel.add(sonars, constraints);
    }

    public void setTitle(String name) {
        if (this.titleText == null) {
            return;
        }

        titleText.setText(name);
    }

    public void setGeneralInformation(Location startLocation, Double startTime, Double upTime, Double flightLevel, String state) {
        if (this.startLocation == null || this.startTime == null || this.upTime == null || this.flightLevel == null || this.state == null) {
            return;
        }

        this.startLocation.setText(startLocation.toString());

        if (startTime != null) {
            this.startTime.setText(NumberConversion.toInteger(startTime) + " s");
        } else {
            this.startTime.setText(null);
        }

        if (upTime != null) {
            this.upTime.setText(NumberConversion.toInteger(upTime) + " s");
        } else {
            this.upTime.setText(null);
        }

        if (flightLevel != null) {
            this.flightLevel.setText(NumberConversion.roundToNDecimals(flightLevel, Preferences.NUMBER_OF_DECIMAL_PLACES) + " m");
        } else {
            this.flightLevel.setText(null);
        }

        this.state.setText(state);
    }

    public void setPosition(Double actualAltitude, Rotation actualRotation, Location actualLocation, Location realLocation) {
        if (this.actualAltitude == null || this.actualRotation == null || this.actualLocation == null || this.realLocation == null || this.deviationFromRealLocation == null) {
            return;
        }

        if (actualAltitude != null) {
            this.actualAltitude.setText(NumberConversion.roundToNDecimals(actualAltitude, Preferences.NUMBER_OF_DECIMAL_PLACES) + " m");
        } else {
            this.actualAltitude.setText(null);
        }

        if (actualRotation != null) {
            this.actualRotation.setText(actualRotation.toString());
        } else {
            this.actualRotation.setText(null);
        }

        if (actualLocation != null) {
            this.actualLocation.setText(actualLocation.toString());
        } else {
            this.actualLocation.setText(null);
        }

        if (realLocation != null) {
            this.realLocation.setText(realLocation.toString());

            if (actualLocation != null) {
                this.deviationFromRealLocation.setText(NumberConversion.roundToNDecimals(Location.getDistance2D(actualLocation, realLocation), Preferences.NUMBER_OF_DECIMAL_PLACES) + " m");
            } else {
                this.deviationFromRealLocation.setText(null);
            }
        } else {
            this.realLocation.setText(null);
            this.deviationFromRealLocation.setText(null);
        }
    }

    public void setVelocity(Double actualVelocity, double linearVelocity, double altitudeVelocity, double rotationalVelocity, Double maximumLinearVelocity, Double maximumAltitudeVelocity, Double maximumRotationalVelocity) {
        if (this.actualVelocity == null || this.linearVelocity == null || this.altitudeVelocity == null || this.rotationalVelocity == null) {
            return;
        }

        if (actualVelocity != null) {
            this.actualVelocity.setText(NumberConversion.roundToNDecimals(actualVelocity, Preferences.NUMBER_OF_DECIMAL_PLACES) + " m/s");
        } else {
            this.actualVelocity.setText(null);
        }

        if (maximumLinearVelocity != null) {
            this.linearVelocity.setText(NumberConversion.roundToNDecimals(linearVelocity * 100, Preferences.NUMBER_OF_DECIMAL_PLACES) + "% from " + NumberConversion.roundToNDecimals(maximumLinearVelocity, Preferences.NUMBER_OF_DECIMAL_PLACES) + " m/s");
        } else {
            this.linearVelocity.setText(NumberConversion.roundToNDecimals(linearVelocity * 100, Preferences.NUMBER_OF_DECIMAL_PLACES) + "%");
        }

        if (maximumAltitudeVelocity != null) {
            this.altitudeVelocity.setText(NumberConversion.roundToNDecimals(altitudeVelocity * 100, Preferences.NUMBER_OF_DECIMAL_PLACES) + "% from " + NumberConversion.roundToNDecimals(maximumAltitudeVelocity, Preferences.NUMBER_OF_DECIMAL_PLACES) + " m/s");
        } else {
            this.altitudeVelocity.setText(NumberConversion.roundToNDecimals(altitudeVelocity * 100, Preferences.NUMBER_OF_DECIMAL_PLACES) + "%");
        }

        if (this.rotationalVelocity != null) {
            if (maximumRotationalVelocity != null) {
                this.rotationalVelocity.setText(NumberConversion.roundToNDecimals(rotationalVelocity, Preferences.NUMBER_OF_DECIMAL_PLACES) + " rad/s from " + NumberConversion.roundToNDecimals(maximumRotationalVelocity, Preferences.NUMBER_OF_DECIMAL_PLACES) + " rad/s");
            } else {
                this.rotationalVelocity.setText(NumberConversion.roundToNDecimals(rotationalVelocity, Preferences.NUMBER_OF_DECIMAL_PLACES) + " rad/s");
            }
        }
    }

    public void setPrimarySensor(double primarySensorHorizontalFieldOfView, double primarySensorVerticalFieldOfView, Double visibleAreaWidth, Double visibleAreaHeight) {
        if (this.primarySensorHorizontalFieldOfView == null || this.primarySensorVerticalFieldOfView == null || this.visibleAreaWidth == null || this.visibleAreaHeight == null) {
            return;
        }

        if (this.primarySensorHorizontalFieldOfView != null) {
            this.primarySensorHorizontalFieldOfView.setText(NumberConversion.roundToNDecimals(primarySensorHorizontalFieldOfView, Preferences.NUMBER_OF_DECIMAL_PLACES) + "°");
        }

        if (this.primarySensorVerticalFieldOfView != null) {
            this.primarySensorVerticalFieldOfView.setText(NumberConversion.roundToNDecimals(primarySensorVerticalFieldOfView, Preferences.NUMBER_OF_DECIMAL_PLACES) + "°");
        }

        if (visibleAreaWidth != null) {
            this.visibleAreaWidth.setText(NumberConversion.roundToNDecimals(visibleAreaWidth, Preferences.NUMBER_OF_DECIMAL_PLACES) + " m");
        } else {
            this.visibleAreaWidth.setText(null);
        }

        if (visibleAreaHeight != null) {
            this.visibleAreaHeight.setText(NumberConversion.roundToNDecimals(visibleAreaHeight, Preferences.NUMBER_OF_DECIMAL_PLACES) + " m");
        } else {
            this.visibleAreaHeight.setText(null);
        }
    }

    public void setObjectives(String listOfObjectives) {
        if (this.objectives == null) {
            return;
        }

        this.objectives.setText(listOfObjectives);
    }

    public void setBattery(Battery battery, Double emptyTime, Double remainingTime, int numberOfChanges, double amperageMean) {
        if (this.battery == null || this.emptyTime == null || this.remainingTime == null || this.numberOfChanges == null || this.amperageMean == null) {
            return;
        }

        this.battery.setBattery(battery);

        if (emptyTime != null) {
            this.emptyTime.setText(NumberConversion.toInteger(emptyTime) + " s");
        } else {
            this.emptyTime.setText(null);
        }

        if (remainingTime != null) {
            this.remainingTime.setText(NumberConversion.toInteger(remainingTime) + " s");
        } else {
            this.remainingTime.setText(null);
        }

        this.numberOfChanges.setText(Integer.toString(numberOfChanges));
        this.amperageMean.setText(NumberConversion.roundToNDecimals(amperageMean, Preferences.NUMBER_OF_DECIMAL_PLACES) + " A");
    }

    public void setCharging(Double chargingStart, Double chargingEnd, String chargingStationName, Double distanceToChargingStation, Double flyingTimeToChargingStation) {
        if (this.chargingTime == null || this.chargingStation == null || this.distanceToStation == null || this.flyingTimeToStation == null) {
            return;
        }
        
        if (chargingStart != null && chargingEnd != null) {
            this.chargingTime.setText(NumberConversion.toInteger(chargingStart) + " - " + NumberConversion.toInteger(chargingEnd) + " s");
        } else {
            this.chargingTime.setText(null);
        }

        if (chargingStationName != null) {
            this.chargingStation.setText(chargingStationName);
        } else {
            this.chargingStation.setText(null);
        }

        if (distanceToChargingStation != null) {
            this.distanceToStation.setText(NumberConversion.roundToNDecimals(distanceToChargingStation, Preferences.NUMBER_OF_DECIMAL_PLACES) + " m");
        } else {
            this.distanceToStation.setText(null);
        }

        if (flyingTimeToChargingStation != null) {
            this.flyingTimeToStation.setText(NumberConversion.toInteger(flyingTimeToChargingStation) + " s");
        } else {
            this.flyingTimeToStation.setText(null);
        }
    }

    public void setSonars(Map<String, Double> sonars) {
        if (this.sonars == null) {
            return;
        }
       
        this.sonars.setSonar(sonars);
    }
}
