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

/**
 *
 * @author Jan Dufek
 */
public class ChargingStationsPanel implements rightBarItem {

    private final Container pane;
    private JPanel panel;
    private JLabel label;

    public ChargingStationsPanel(Container pane) {
        this.pane = pane;
    }

    @Override
    public void create() {
        createPanel();
        createLabel();
    }

    private void createPanel() {
        panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Charging Stations"));

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 0;
        constraints.gridy = 4;
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

    private void createLabel() {
        label = new JLabel();
        label.setHorizontalAlignment(SwingConstants.LEFT);
        label.setVerticalAlignment(SwingConstants.TOP);

        label.setFont(new Font(null, Preferences.NORMAL_FONT_STYLE, Preferences.NORMAL_FONT_SIZE));
        label.setForeground(Preferences.NORMAL_FONT_COLOR);

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        constraints.insets = new Insets(0, 5, 0, 5);
        constraints.ipadx = 0;
        constraints.ipady = 0;

        panel.add(label, constraints);
    }

    @Override
    public void setText(String text1) {
        label.setText(text1);
    }
}
