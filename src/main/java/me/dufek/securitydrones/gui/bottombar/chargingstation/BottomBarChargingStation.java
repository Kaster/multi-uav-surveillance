package me.dufek.securitydrones.gui.bottombar.chargingstation;

import cz.cuni.amis.pogamut.base3d.worldview.object.Location;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import me.dufek.securitydrones.gui.bottombar.BottomBarType;
import me.dufek.securitydrones.gui.bottombar.Preferences;
import me.dufek.securitydrones.battery.BatteryDrum;
import me.dufek.securitydrones.chargingstation.schedule.Schedule;

/**
 *
 * @author Jan Dufek
 */
public class BottomBarChargingStation implements BottomBarType {

    private final Container pane;

    public JPanel titlePanel;
    public ImageIcon titleIcon;
    public JLabel titleText;
    public JLabel generalInformationTitle;
    public JPanel generalInformationPanel;
    public JLabel occupiedTitle;
    public JLabel occupied;
    public JLabel locationTitle;
    public JLabel location;
    public JLabel chargingPerformanceTitle;
    public JLabel chargingPerformance;
    public JLabel swapTimeMeanTitle;
    public JLabel swapTimeMean;
    public JLabel swapTimeStandardDeviationTitle;
    public JLabel swapTimeStandardDeviation;
    public JLabel batteryDrumTitle;
    public BatteryDrumVisualization batteryDrum;
    public JLabel scheduleTitle;
    public ScheduleVisualization schedule;

    public BottomBarChargingStation(Container pane) {
        this.pane = pane;
    }

    @Override
    public void create() {
        createTitle();

        createGeneralInformationTitle();
        createGeneralInformation();
        createBatteryDrumTitle();
        createBatteryDrum();
        createScheduleTitle();
        createSchedule();
    }

    private void createTitle() {
        titlePanel = new JPanel(new GridBagLayout());

        createTitleIcon();
        createTitleText();

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
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
        titleIcon = new ImageIcon("graphics/chargingStation.png");

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
        constraints.gridwidth = 1;
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

    private void createGeneralInformationTitle() {
        generalInformationTitle = new JLabel();
        generalInformationTitle.setText("General Information");

        generalInformationTitle.setHorizontalAlignment(SwingConstants.LEFT);
        generalInformationTitle.setVerticalAlignment(SwingConstants.CENTER);

        generalInformationTitle.setFont(new Font(null, Preferences.HEADING_1_FONT_STYLE, Preferences.HEADING_1_FONT_SIZE));
        generalInformationTitle.setForeground(Preferences.HEADING_1_FONT_COLOR);

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.weightx = 0.5;
        constraints.weighty = 0.1;
        constraints.insets = new Insets(0, 0, 0, 0);
        constraints.ipadx = 0;
        constraints.ipady = 0;

        pane.add(generalInformationTitle, constraints);
    }

    private void createGeneralInformation() {
        generalInformationPanel = new JPanel(new GridBagLayout());

        createOccupiedTitle();
        createOccupied();
        createLoactionTitle();
        createLocation();
        createChargingPerformanceTitle();
        createChargingPerformance();
        createSwapTimeMeanTitle();
        createSwapTimeMean();
        createSwapTimeStandardDeviationTitle();
        createSwapTimeStandardDeviation();

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.weightx = 0.5;
        constraints.weighty = 0.1;
        constraints.insets = new Insets(0, 0, 0, 0);
        constraints.ipadx = 0;
        constraints.ipady = 0;

        pane.add(generalInformationPanel, constraints);
    }

    private void createOccupiedTitle() {
        occupiedTitle = new JLabel();

        occupiedTitle.setText("Occupied");

        occupiedTitle.setHorizontalAlignment(SwingConstants.LEFT);
        occupiedTitle.setVerticalAlignment(SwingConstants.CENTER);

        occupiedTitle.setFont(new Font(null, Preferences.NORMAL_TITLE_FONT_STYLE, Preferences.NORMAL_TITLE_FONT_SIZE));
        occupiedTitle.setForeground(Preferences.NORMAL_TITLE_FONT_COLOR);

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.insets = new Insets(0, 0, 0, 0);
        constraints.ipadx = 0;
        constraints.ipady = 0;

        generalInformationPanel.add(occupiedTitle, constraints);
    }

    private void createOccupied() {
        occupied = new JLabel();

        occupied.setHorizontalAlignment(SwingConstants.LEFT);
        occupied.setVerticalAlignment(SwingConstants.CENTER);

        occupied.setFont(new Font(null, Preferences.NORMAL_FONT_STYLE, Preferences.NORMAL_FONT_SIZE));
        occupied.setForeground(Preferences.NORMAL_FONT_COLOR);

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.insets = new Insets(0, 0, 0, 0);
        constraints.ipadx = 0;
        constraints.ipady = 0;

        generalInformationPanel.add(occupied, constraints);
    }

    private void createLoactionTitle() {
        locationTitle = new JLabel();

        locationTitle.setText("Location");

        locationTitle.setHorizontalAlignment(SwingConstants.LEFT);
        locationTitle.setVerticalAlignment(SwingConstants.CENTER);

        locationTitle.setFont(new Font(null, Preferences.NORMAL_TITLE_FONT_STYLE, Preferences.NORMAL_TITLE_FONT_SIZE));
        locationTitle.setForeground(Preferences.NORMAL_TITLE_FONT_COLOR);

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

        generalInformationPanel.add(locationTitle, constraints);
    }

    private void createLocation() {
        location = new JLabel();

        location.setHorizontalAlignment(SwingConstants.LEFT);
        location.setVerticalAlignment(SwingConstants.CENTER);

        location.setFont(new Font(null, Preferences.NORMAL_FONT_STYLE, Preferences.NORMAL_FONT_SIZE));
        location.setForeground(Preferences.NORMAL_FONT_COLOR);

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

        generalInformationPanel.add(location, constraints);
    }

    private void createChargingPerformanceTitle() {
        chargingPerformanceTitle = new JLabel();

        chargingPerformanceTitle.setText("Charging Performance");

        chargingPerformanceTitle.setHorizontalAlignment(SwingConstants.LEFT);
        chargingPerformanceTitle.setVerticalAlignment(SwingConstants.CENTER);

        chargingPerformanceTitle.setFont(new Font(null, Preferences.NORMAL_TITLE_FONT_STYLE, Preferences.NORMAL_TITLE_FONT_SIZE));
        chargingPerformanceTitle.setForeground(Preferences.NORMAL_TITLE_FONT_COLOR);

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

        generalInformationPanel.add(chargingPerformanceTitle, constraints);
    }

    private void createChargingPerformance() {
        chargingPerformance = new JLabel();

        chargingPerformance.setHorizontalAlignment(SwingConstants.LEFT);
        chargingPerformance.setVerticalAlignment(SwingConstants.CENTER);

        chargingPerformance.setFont(new Font(null, Preferences.NORMAL_FONT_STYLE, Preferences.NORMAL_FONT_SIZE));
        chargingPerformance.setForeground(Preferences.NORMAL_FONT_COLOR);

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

        generalInformationPanel.add(chargingPerformance, constraints);
    }

    private void createSwapTimeMeanTitle() {
        swapTimeMeanTitle = new JLabel();

        swapTimeMeanTitle.setText("Swap Time Mean");

        swapTimeMeanTitle.setHorizontalAlignment(SwingConstants.LEFT);
        swapTimeMeanTitle.setVerticalAlignment(SwingConstants.CENTER);

        swapTimeMeanTitle.setFont(new Font(null, Preferences.NORMAL_TITLE_FONT_STYLE, Preferences.NORMAL_TITLE_FONT_SIZE));
        swapTimeMeanTitle.setForeground(Preferences.NORMAL_TITLE_FONT_COLOR);

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

        generalInformationPanel.add(swapTimeMeanTitle, constraints);
    }

    private void createSwapTimeMean() {
        swapTimeMean = new JLabel();

        swapTimeMean.setHorizontalAlignment(SwingConstants.LEFT);
        swapTimeMean.setVerticalAlignment(SwingConstants.CENTER);

        swapTimeMean.setFont(new Font(null, Preferences.NORMAL_FONT_STYLE, Preferences.NORMAL_FONT_SIZE));
        swapTimeMean.setForeground(Preferences.NORMAL_FONT_COLOR);

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

        generalInformationPanel.add(swapTimeMean, constraints);
    }

    private void createSwapTimeStandardDeviationTitle() {
        swapTimeStandardDeviation = new JLabel();

        swapTimeStandardDeviation.setText("Swap Time Standard Deviation");

        swapTimeStandardDeviation.setHorizontalAlignment(SwingConstants.LEFT);
        swapTimeStandardDeviation.setVerticalAlignment(SwingConstants.CENTER);

        swapTimeStandardDeviation.setFont(new Font(null, Preferences.NORMAL_TITLE_FONT_STYLE, Preferences.NORMAL_TITLE_FONT_SIZE));
        swapTimeStandardDeviation.setForeground(Preferences.NORMAL_TITLE_FONT_COLOR);

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

        generalInformationPanel.add(swapTimeStandardDeviation, constraints);
    }

    private void createSwapTimeStandardDeviation() {
        swapTimeStandardDeviation = new JLabel();

        swapTimeStandardDeviation.setHorizontalAlignment(SwingConstants.LEFT);
        swapTimeStandardDeviation.setVerticalAlignment(SwingConstants.CENTER);

        swapTimeStandardDeviation.setFont(new Font(null, Preferences.NORMAL_FONT_STYLE, Preferences.NORMAL_FONT_SIZE));
        swapTimeStandardDeviation.setForeground(Preferences.NORMAL_FONT_COLOR);

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

        generalInformationPanel.add(swapTimeStandardDeviation, constraints);
    }

    private void createBatteryDrumTitle() {
        batteryDrumTitle = new JLabel();
        batteryDrumTitle.setText("Battery Drum");

        batteryDrumTitle.setHorizontalAlignment(SwingConstants.LEFT);
        batteryDrumTitle.setVerticalAlignment(SwingConstants.CENTER);

        batteryDrumTitle.setFont(new Font(null, Preferences.HEADING_1_FONT_STYLE, Preferences.HEADING_1_FONT_SIZE));
        batteryDrumTitle.setForeground(Preferences.HEADING_1_FONT_COLOR);

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.weightx = 0.5;
        constraints.weighty = 0.1;
        constraints.insets = new Insets(0, 0, 0, 0);
        constraints.ipadx = 0;
        constraints.ipady = 0;

        pane.add(batteryDrumTitle, constraints);
    }

    private void createBatteryDrum() {
        batteryDrum = new BatteryDrumVisualization(pane);

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 1;
        constraints.gridy = 2;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.weightx = 0.5;
        constraints.weighty = 0.1;
        constraints.insets = new Insets(0, 0, 0, 0);
        constraints.ipadx = 0;
        constraints.ipady = 0;

        pane.add(batteryDrum, constraints);
    }

    private void createScheduleTitle() {
        scheduleTitle = new JLabel();
        scheduleTitle.setText("Schedule");

        scheduleTitle.setHorizontalAlignment(SwingConstants.LEFT);
        scheduleTitle.setVerticalAlignment(SwingConstants.CENTER);

        scheduleTitle.setFont(new Font(null, Preferences.HEADING_1_FONT_STYLE, Preferences.HEADING_1_FONT_SIZE));
        scheduleTitle.setForeground(Preferences.HEADING_1_FONT_COLOR);

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.gridwidth = 2;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.weightx = 1;
        constraints.weighty = 0.1;
        constraints.insets = new Insets(0, 0, 0, 0);
        constraints.ipadx = 0;
        constraints.ipady = 0;

        pane.add(scheduleTitle, constraints);
    }

    private void createSchedule() {
        schedule = new ScheduleVisualization(pane);

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 0;
        constraints.gridy = 4;
        constraints.gridwidth = 2;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.insets = new Insets(0, 0, 0, 0);
        constraints.ipadx = 0;
        constraints.ipady = 0;

        pane.add(schedule, constraints);
    }

    public void setTitle(String name) {
        if (this.titleText == null) {
            return;
        }

        titleText.setText(name);
    }

    public void setGeneralInformation(boolean occupied, Location location, double chargingPerformance, double swapTimeMean, double swapTimeStandardDeviation) {
        if (this.occupied == null || this.location == null || this.chargingPerformance == null || this.swapTimeMean == null || this.swapTimeStandardDeviation == null) {
            return;
        }
        
        this.occupied.setText(occupied ? "YES" : "NO");
        this.location.setText(location.toString());
        this.chargingPerformance.setText(chargingPerformance + " mA");
        this.swapTimeMean.setText(swapTimeMean + " s");
        this.swapTimeStandardDeviation.setText(swapTimeStandardDeviation + " s");
    }

    public void setSchedule(Schedule schedule) {
        if (this.schedule == null) {
            return;
        }
        
        this.schedule.setSchedule(schedule);
    }

    public void setBatteryDrum(BatteryDrum batteryDrum) {
        if (this.batteryDrum == null) {
            return;
        }
        
        this.batteryDrum.setBatteryDrum(batteryDrum);
    }
}
