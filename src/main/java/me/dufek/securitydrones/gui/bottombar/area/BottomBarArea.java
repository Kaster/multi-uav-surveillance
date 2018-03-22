package me.dufek.securitydrones.gui.bottombar.area;

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
import me.dufek.securitydrones.conversion.NumberConversion;

/**
 *
 * @author Jan Dufek
 */
public class BottomBarArea implements BottomBarType {

    private final Container pane;

    public JPanel titlePanel;
    public ImageIcon titleIcon;
    public JLabel titleText;
    public JLabel generalInformationTitle;
    public JPanel generalInformationPanel;
    public JLabel typeTitle;
    public JLabel type;
    public JLabel areaTitle;
    public JLabel area;
    public JLabel actualCoverageTitle;
    public JLabel actualCoverage;
//    public JLabel actualCoverageTitle;
//    public JLabel actualCoverage;
//    public JLabel swapTimeStandardDeviationTitle;
//    public JLabel swapTimeStandardDeviation;
    public JLabel actualCoverageVisualizationTitle;
    public ActualCoverageVisualization actualCoverageVisualization;
//    public JLabel scheduleTitle;
//    public ScheduleVisualization schedule;

    public BottomBarArea(Container pane) {
        this.pane = pane;
    }

    @Override
    public void create() {
        createTitle();

        createGeneralInformationTitle();
        createGeneralInformation();
        createActualCoverageVisualizationTitle();
        createActualCoverageVisualization();
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
        titleIcon = new ImageIcon("graphics/area.png");

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

        typeTitle();
        createType();
        createAreaTitle();
        createArea();
        createActualCoverageTitle();
        createActualCoverage();

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

    private void typeTitle() {
        typeTitle = new JLabel();

        typeTitle.setText("Type");

        typeTitle.setHorizontalAlignment(SwingConstants.LEFT);
        typeTitle.setVerticalAlignment(SwingConstants.CENTER);

        typeTitle.setFont(new Font(null, Preferences.NORMAL_TITLE_FONT_STYLE, Preferences.NORMAL_TITLE_FONT_SIZE));
        typeTitle.setForeground(Preferences.NORMAL_TITLE_FONT_COLOR);

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

        generalInformationPanel.add(typeTitle, constraints);
    }

    private void createType() {
        type = new JLabel();

        type.setHorizontalAlignment(SwingConstants.LEFT);
        type.setVerticalAlignment(SwingConstants.CENTER);

        type.setFont(new Font(null, Preferences.NORMAL_FONT_STYLE, Preferences.NORMAL_FONT_SIZE));
        type.setForeground(Preferences.NORMAL_FONT_COLOR);

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

        generalInformationPanel.add(type, constraints);
    }

    private void createAreaTitle() {
        areaTitle = new JLabel();

        areaTitle.setText("Area");

        areaTitle.setHorizontalAlignment(SwingConstants.LEFT);
        areaTitle.setVerticalAlignment(SwingConstants.CENTER);

        areaTitle.setFont(new Font(null, Preferences.NORMAL_TITLE_FONT_STYLE, Preferences.NORMAL_TITLE_FONT_SIZE));
        areaTitle.setForeground(Preferences.NORMAL_TITLE_FONT_COLOR);

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

        generalInformationPanel.add(areaTitle, constraints);
    }

    private void createArea() {
        area = new JLabel();

        area.setHorizontalAlignment(SwingConstants.LEFT);
        area.setVerticalAlignment(SwingConstants.CENTER);

        area.setFont(new Font(null, Preferences.NORMAL_FONT_STYLE, Preferences.NORMAL_FONT_SIZE));
        area.setForeground(Preferences.NORMAL_FONT_COLOR);

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

        generalInformationPanel.add(area, constraints);
    }

    private void createActualCoverageTitle() {
        actualCoverageTitle = new JLabel();

        actualCoverageTitle.setText("Actual Coverage");

        actualCoverageTitle.setHorizontalAlignment(SwingConstants.LEFT);
        actualCoverageTitle.setVerticalAlignment(SwingConstants.CENTER);

        actualCoverageTitle.setFont(new Font(null, Preferences.NORMAL_TITLE_FONT_STYLE, Preferences.NORMAL_TITLE_FONT_SIZE));
        actualCoverageTitle.setForeground(Preferences.NORMAL_TITLE_FONT_COLOR);

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

        generalInformationPanel.add(actualCoverageTitle, constraints);
    }

    private void createActualCoverage() {
        actualCoverage = new JLabel();

        actualCoverage.setHorizontalAlignment(SwingConstants.LEFT);
        actualCoverage.setVerticalAlignment(SwingConstants.CENTER);

        actualCoverage.setFont(new Font(null, Preferences.NORMAL_FONT_STYLE, Preferences.NORMAL_FONT_SIZE));
        actualCoverage.setForeground(Preferences.NORMAL_FONT_COLOR);

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

        generalInformationPanel.add(actualCoverage, constraints);
    }

    private void createActualCoverageVisualizationTitle() {
        actualCoverageVisualizationTitle = new JLabel();
        actualCoverageVisualizationTitle.setText("Actual Coverage");

        actualCoverageVisualizationTitle.setHorizontalAlignment(SwingConstants.LEFT);
        actualCoverageVisualizationTitle.setVerticalAlignment(SwingConstants.CENTER);

        actualCoverageVisualizationTitle.setFont(new Font(null, Preferences.HEADING_1_FONT_STYLE, Preferences.HEADING_1_FONT_SIZE));
        actualCoverageVisualizationTitle.setForeground(Preferences.HEADING_1_FONT_COLOR);

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

        pane.add(actualCoverageVisualizationTitle, constraints);
    }

    private void createActualCoverageVisualization() {
        actualCoverageVisualization = new ActualCoverageVisualization(pane);

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

        pane.add(actualCoverageVisualization, constraints);
    }

//    private void createScheduleTitle() {
//        scheduleTitle = new JLabel();
//        scheduleTitle.setText("Schedule");
//
//        scheduleTitle.setHorizontalAlignment(SwingConstants.LEFT);
//        scheduleTitle.setVerticalAlignment(SwingConstants.CENTER);
//
//        scheduleTitle.setFont(new Font(null, Preferences.HEADING_1_FONT_STYLE, Preferences.HEADING_1_FONT_SIZE));
//        scheduleTitle.setForeground(Preferences.HEADING_1_FONT_COLOR);
//
//        GridBagConstraints constraints = new GridBagConstraints();
//
//        constraints.gridx = 0;
//        constraints.gridy = 3;
//        constraints.gridwidth = 2;
//        constraints.gridheight = 1;
//        constraints.fill = GridBagConstraints.BOTH;
//        constraints.anchor = GridBagConstraints.CENTER;
//        constraints.weightx = 1;
//        constraints.weighty = 0.1;
//        constraints.insets = new Insets(0, 0, 0, 0);
//        constraints.ipadx = 0;
//        constraints.ipady = 0;
//
//        pane.add(scheduleTitle, constraints);
//    }
//
//    private void createSchedule() {
//        schedule = new ScheduleVisualization(pane);
//
//        GridBagConstraints constraints = new GridBagConstraints();
//
//        constraints.gridx = 0;
//        constraints.gridy = 4;
//        constraints.gridwidth = 2;
//        constraints.gridheight = 1;
//        constraints.fill = GridBagConstraints.BOTH;
//        constraints.anchor = GridBagConstraints.CENTER;
//        constraints.weightx = 1;
//        constraints.weighty = 1;
//        constraints.insets = new Insets(0, 0, 0, 0);
//        constraints.ipadx = 0;
//        constraints.ipady = 0;
//
//        pane.add(schedule, constraints);
//    }
    public void setTitle(String name) {
        if (this.titleText == null) {
            return;
        }

        titleText.setText(name);
    }

    public void setGeneralInformation(String type, Double area, Double actualCoverage) {
        if (this.type == null || this.area == null || this.actualCoverage == null) {
            return;
        }
        
        this.type.setText(type);

        if (area != null) {
            this.area.setText(NumberConversion.roundToNDecimals(area, 2) + " sq m");
        }

        if (actualCoverage != null) {
            this.actualCoverage.setText(NumberConversion.roundToNDecimals(actualCoverage, 2) + " s");
        }
    }

    public void setActualCoverage(Double actualCoverage) {
        if (this.actualCoverageVisualization == null) {
            return;
        }
        
        this.actualCoverageVisualization.setActualCoverage(actualCoverage);
    }
}
