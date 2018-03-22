package me.dufek.securitydrones.gui.rightbar;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import me.dufek.securitydrones.conversion.NumberConversion;
import me.dufek.securitydrones.main.Global;

/**
 *
 * @author Jan Dufek
 */
public class SimulationPanel implements rightBarItem {

    private final Container pane;
    private JPanel panel;

    private JLabel mapTitle;
    private JLabel map;

    private JLabel timeTitle;
    private JLabel time;

    public SimulationPanel(Container pane) {
        this.pane = pane;
    }

    @Override
    public void create() {
        createPanel();

        createMapTitle();
        createMap();

        createTimeTitle();
        createTime();
    }

    private void createPanel() {
        panel = new JPanel(new GridBagLayout());

        panel.setBorder(BorderFactory.createTitledBorder("Simulation"));

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.weightx = 0.4;
        constraints.weighty = 0.0;
        constraints.insets = new Insets(0, 0, 0, 0);
        constraints.ipadx = 0;
        constraints.ipady = 0;

        pane.add(panel, constraints);
    }

    private void createMapTitle() {
        mapTitle = new JLabel("Map");

        mapTitle.setHorizontalAlignment(SwingConstants.LEFT);
        mapTitle.setVerticalAlignment(SwingConstants.CENTER);

        mapTitle.setFont(new Font(null, Preferences.NORMAL_FONT_STYLE, Preferences.NORMAL_FONT_SIZE));
        mapTitle.setForeground(Preferences.NORMAL_FONT_COLOR);

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.insets = new Insets(0, Preferences.NORMAL_SIDE_INSET, 0, 0);
        constraints.ipadx = 0;
        constraints.ipady = 0;

        panel.add(mapTitle, constraints);
    }

    private void createMap() {
        map = new JLabel(Global.getMapName());

        map.setHorizontalAlignment(SwingConstants.RIGHT);
        map.setVerticalAlignment(SwingConstants.CENTER);

        map.setFont(new Font(null, Preferences.NORMAL_FONT_STYLE, Preferences.NORMAL_FONT_SIZE));
        map.setForeground(Preferences.NORMAL_FONT_COLOR);

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.insets = new Insets(0, 0, 0, Preferences.NORMAL_SIDE_INSET);
        constraints.ipadx = 0;
        constraints.ipady = 0;

        panel.add(map, constraints);
    }

    private void createTimeTitle() {
        timeTitle = new JLabel("Time");

        timeTitle.setHorizontalAlignment(SwingConstants.LEFT);
        timeTitle.setVerticalAlignment(SwingConstants.CENTER);

        timeTitle.setFont(new Font(null, Preferences.NORMAL_FONT_STYLE, Preferences.NORMAL_FONT_SIZE));
        timeTitle.setForeground(Preferences.NORMAL_FONT_COLOR);

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.insets = new Insets(0, Preferences.NORMAL_SIDE_INSET, 0, 0);
        constraints.ipadx = 0;
        constraints.ipady = 0;

        panel.add(timeTitle, constraints);
    }

    private void createTime() {
        time = new JLabel();

        time.setHorizontalAlignment(SwingConstants.RIGHT);
        time.setVerticalAlignment(SwingConstants.CENTER);

        time.setFont(new Font(null, Preferences.NORMAL_FONT_STYLE, Preferences.NORMAL_FONT_SIZE));
        time.setForeground(Preferences.NORMAL_FONT_COLOR);

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.insets = new Insets(0, 0, 0, Preferences.NORMAL_SIDE_INSET);
        constraints.ipadx = 0;
        constraints.ipady = 0;

        panel.add(time, constraints);
    }

    @Override
    public void setText(String text) {
        throw new IllegalStateException("Can not set text on this component.");
    }

    public void setMap(String map) {
        this.map.setText(map);
    }

    public void setTime(Double time) {
        if (time == null) {
            this.time.setText(null);
        } else {
            this.time.setText(NumberConversion.toInteger(time) + " s");
        }
    }
}
