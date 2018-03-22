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
public class AlgorithmPanel implements rightBarItem {

    private final Container pane;
    private JPanel panel;

    private JLabel algorithmTitle;
    private JLabel algorithm;

    private JLabel actualCoverageTitle;
    private JLabel actualCoverage;

    public AlgorithmPanel(Container pane) {
        this.pane = pane;
    }

    @Override
    public void create() {
        createPanel();

        createAlgorithmTitle();
        createAlgorithm();

        createActualCoverageTitle();
        createActualCoverage();
    }

    private void createPanel() {
        panel = new JPanel(new GridBagLayout());

        panel.setBorder(BorderFactory.createTitledBorder("Algorithm"));

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 0;
        constraints.gridy = 2;
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

    private void createAlgorithmTitle() {
        algorithmTitle = new JLabel("Algorithm");

        algorithmTitle.setHorizontalAlignment(SwingConstants.LEFT);
        algorithmTitle.setVerticalAlignment(SwingConstants.CENTER);

        algorithmTitle.setFont(new Font(null, Preferences.NORMAL_FONT_STYLE, Preferences.NORMAL_FONT_SIZE));
        algorithmTitle.setForeground(Preferences.NORMAL_FONT_COLOR);

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

        panel.add(algorithmTitle, constraints);
    }

    private void createAlgorithm() {
        algorithm = new JLabel();

        algorithm.setHorizontalAlignment(SwingConstants.RIGHT);
        algorithm.setVerticalAlignment(SwingConstants.CENTER);

        algorithm.setFont(new Font(null, Preferences.NORMAL_FONT_STYLE, Preferences.NORMAL_FONT_SIZE));
        algorithm.setForeground(Preferences.NORMAL_FONT_COLOR);

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

        panel.add(algorithm, constraints);
    }

    private void createActualCoverageTitle() {
        actualCoverageTitle = new JLabel("Actual Coverage");

        actualCoverageTitle.setHorizontalAlignment(SwingConstants.LEFT);
        actualCoverageTitle.setVerticalAlignment(SwingConstants.CENTER);

        actualCoverageTitle.setFont(new Font(null, Preferences.NORMAL_FONT_STYLE, Preferences.NORMAL_FONT_SIZE));
        actualCoverageTitle.setForeground(Preferences.NORMAL_FONT_COLOR);

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

        panel.add(actualCoverageTitle, constraints);
    }

    private void createActualCoverage() {
        actualCoverage = new JLabel();

        actualCoverage.setHorizontalAlignment(SwingConstants.RIGHT);
        actualCoverage.setVerticalAlignment(SwingConstants.CENTER);

        actualCoverage.setFont(new Font(null, Preferences.NORMAL_FONT_STYLE, Preferences.NORMAL_FONT_SIZE));
        actualCoverage.setForeground(Preferences.NORMAL_FONT_COLOR);

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

        panel.add(actualCoverage, constraints);
    }

    @Override
    public void setText(String text) {
        throw new IllegalStateException("Can not set text on this component.");
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm.setText(algorithm);
    }

    public void setActualCoverage(Double actualCoverage) {
        if (actualCoverage == null) {
            this.actualCoverage.setText(null);
        } else {
            this.actualCoverage.setText(NumberConversion.roundToNDecimals(actualCoverage, Preferences.NUMBER_OF_DECIMAL_PLACES) + " s");
        }
    }
}
