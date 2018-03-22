package me.dufek.securitydrones.gui.menu.simulation;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;
import me.dufek.securitydrones.algorithm.Algorithm;
import me.dufek.securitydrones.algorithm.Algorithms;
import me.dufek.securitydrones.algorithm.AreaDivision;
import me.dufek.securitydrones.algorithm.Variability;
import me.dufek.securitydrones.algorithm.heatgradient.HeatGradientCoverageAlgorithmSingle;
import me.dufek.securitydrones.algorithm.heatgradient.HeatGradientCoverageAlgorithmMultiple;
import me.dufek.securitydrones.algorithm.perimeterfollowing.multiple.PerimeterFollowingAlgorithmMultiple;
import me.dufek.securitydrones.algorithm.perimeterfollowing.single.PerimeterFollowingAlgorithmSingle;
import me.dufek.securitydrones.algorithm.randompoint.multiple.RandomPointCoverageAlgorithmMultiple;
import me.dufek.securitydrones.algorithm.randompoint.single.RandomPointCoverageAlgorithmSingle;
import me.dufek.securitydrones.algorithm.spanningtreecoverage.multiple.SpanningTreeCoverageAlgorithmMultiple;
import me.dufek.securitydrones.algorithm.spanningtreecoverage.single.SpanningTreeCoverageAlgorithmSingle;
import me.dufek.securitydrones.algorithm.systematic.single.SystematicCoverageAlgorithmSingle;
import me.dufek.securitydrones.algorithm.systematic.Type;
import me.dufek.securitydrones.algorithm.systematic.multiple.SystematicCoverageAlgorithmMultiple;
import me.dufek.securitydrones.main.Global;

/**
 *
 * @author Jan Dufek
 */
public class SelectAlgorithmDialog {

    private JFrame frame;

    public SelectAlgorithmDialog(JFrame frame) {
        this.frame = frame;
    }

    public Algorithm create() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));

        JPanel labels = new JPanel(new GridLayout(0, 1, 2, 2));
        labels.add(new JLabel("Algorithm", SwingConstants.RIGHT));
        labels.add(new JLabel("Area Division", SwingConstants.RIGHT));
        labels.add(new JLabel("Variability", SwingConstants.RIGHT));
        panel.add(labels, BorderLayout.WEST);

        JPanel fields = new JPanel(new GridLayout(0, 1, 2, 2));
        JComboBox algorithmName = new JComboBox(Algorithms.values());
        fields.add(algorithmName);

        ButtonGroup areaDivisionButtonGroup = new ButtonGroup();

        JRadioButton single = new JRadioButton("Single");
        single.setMnemonic(KeyEvent.VK_S);
        single.setActionCommand("Single");
        single.setSelected(true);
        areaDivisionButtonGroup.add(single);

        JRadioButton multiple = new JRadioButton("Multiple");
        multiple.setMnemonic(KeyEvent.VK_M);
        multiple.setActionCommand("Multiple");
        multiple.setSelected(false);
        areaDivisionButtonGroup.add(multiple);

        JPanel areaDivisionPanel = new JPanel();
        areaDivisionPanel.add(single);
        areaDivisionPanel.add(multiple);

        fields.add(areaDivisionPanel);

        ButtonGroup variabilityButtonGroup = new ButtonGroup();

        JRadioButton staticVariability = new JRadioButton("Static");
        staticVariability.setMnemonic(KeyEvent.VK_T);
        staticVariability.setActionCommand("Static");
        staticVariability.setSelected(true);
        variabilityButtonGroup.add(staticVariability);

        JRadioButton dynamicVariability = new JRadioButton("Dynamic");
        dynamicVariability.setMnemonic(KeyEvent.VK_D);
        dynamicVariability.setActionCommand("Dynamic");
        dynamicVariability.setSelected(false);
        variabilityButtonGroup.add(dynamicVariability);

        JPanel variabilityPanel = new JPanel();
        variabilityPanel.add(staticVariability);
        variabilityPanel.add(dynamicVariability);

        fields.add(variabilityPanel);

        panel.add(fields, BorderLayout.CENTER);

        String[] buttons = {"Run", "Cancel"};
        int c = JOptionPane.showOptionDialog(
                frame,
                panel,
                "Select Algorithm",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                buttons,
                buttons[0]
        );

        if (c == 0) {
            AreaDivision areaDivision;

            if (single.isSelected()) {
                areaDivision = AreaDivision.SINGLE;
            } else {
                areaDivision = AreaDivision.MULTIPLE;
            }

            Variability variability;

            if (staticVariability.isSelected()) {
                variability = Variability.STATIC;
            } else {
                variability = Variability.DYNAMIC;
            }
            switch ((Algorithms) algorithmName.getSelectedItem()) {
                // Heat Gradient
                case HEAT_GRADIENT_COVERAGE:
                    switch (areaDivision) {
                        case SINGLE:
                            return new HeatGradientCoverageAlgorithmSingle(Global.window, variability);
                        case MULTIPLE:
                            return new HeatGradientCoverageAlgorithmMultiple(Global.window, variability);
                    }

                // Perimeter Following
                case PERIMETER_FOLLOWING:
                    switch (areaDivision) {
                        case SINGLE:
                            return new PerimeterFollowingAlgorithmSingle(Global.window, variability);
                        case MULTIPLE:
                            return new PerimeterFollowingAlgorithmMultiple(Global.window, variability);
                    }

                // Random Point Coverage
                case RANDOM_POINT_COVERAGE:
                    switch (areaDivision) {
                        case SINGLE:
                            return new RandomPointCoverageAlgorithmSingle(Global.window, variability);
                        case MULTIPLE:
                            return new RandomPointCoverageAlgorithmMultiple(Global.window, variability);
                    }

                // Spanning Tree Coverage
                case SPANNING_TREE_COVERAGE:
                    switch (areaDivision) {
                        case SINGLE:
                            return new SpanningTreeCoverageAlgorithmSingle(Global.window, variability);
                        case MULTIPLE:
                            return new SpanningTreeCoverageAlgorithmMultiple(Global.window, variability);
                    }

                // Systematic Coverage Horizontal
                case SYSTEMATIC_COVERAGE_HORIZONTAL:
                    switch (areaDivision) {
                        case SINGLE:
                            return new SystematicCoverageAlgorithmSingle(Global.window, Type.HORIZONTAL, variability);
                        case MULTIPLE:
                            return new SystematicCoverageAlgorithmMultiple(Global.window, Type.HORIZONTAL, variability);
                    }

                // Systematic Coverage Vertical
                case SYSTEMATIC_COVERAGE_VERTICAL:
                    switch (areaDivision) {
                        case SINGLE:
                            return new SystematicCoverageAlgorithmSingle(Global.window, Type.VERTICAL, variability);
                        case MULTIPLE:
                            return new SystematicCoverageAlgorithmMultiple(Global.window, Type.VERTICAL, variability);
                    }

                // Systematic Coverage Spiral
                case SYSTEMATIC_COVERAGE_SPIRAL:
                    switch (areaDivision) {
                        case SINGLE:
                            return new SystematicCoverageAlgorithmSingle(Global.window, Type.SPIRAL, variability);
                        case MULTIPLE:
                            return new SystematicCoverageAlgorithmMultiple(Global.window, Type.SPIRAL, variability);
                    }

                default:
                    throw new IllegalStateException("Unknown algorithm.");
            }
//            switch ((Algorithms) algorithmName.getSelectedItem()) {
//                // Random Point Coverage
//                case RANDOM_POINT_COVERAGE_SINGLE_STATIC:
//                    return new RandomPointAlgorithmSingle(Global.window, Variability.STATIC);
//                case RANDOM_POINT_COVERAGE_SINGLE_DYNAMIC:
//                    return new RandomPointAlgorithmSingle(Global.window, Variability.DYNAMIC);
//                case RANDOM_POINT_COVERAGE_MULTIPLE_STATIC:
//                    return new RandomPointAlgorithmMultiple(Global.window, Variability.STATIC);
//                case RANDOM_POINT_COVERAGE_MULTIPLE_DYNAMIC:
//                    return new RandomPointAlgorithmMultiple(Global.window, Variability.DYNAMIC);
//                // Spanning Tree Coverage
//                case SPANNING_TREE_COVERAGE_SINGLE_STATIC:
//                    return new SpanningTreeCoverageAlgorithmSingle(Global.window, Variability.STATIC);
//                case SPANNING_TREE_COVERAGE_SINGLE_DYNAMIC:
//                    return new SpanningTreeCoverageAlgorithmSingle(Global.window, Variability.DYNAMIC);
//                case SPANNING_TREE_COVERAGE_MULTIPLE_STATIC:
//                    return new SpanningTreeCoverageAlgorithmMultiple(Global.window, Variability.STATIC);
//                case SPANNING_TREE_COVERAGE_MULTIPLE_DYNAMIC:
//                    return new SpanningTreeCoverageAlgorithmMultiple(Global.window, Variability.DYNAMIC);
//                // Systematic Coverage Horizontal
//                case SYSTEMATIC_COVERAGE_HORIZONTAL_SINGLE_STATIC:
//                    return new SystematicAlgorithmSingle(Global.window, Type.HORIZONTAL, Variability.STATIC);
//                case SYSTEMATIC_COVERAGE_HORIZONTAL_SINGLE_DYNAMIC:
//                    return new SystematicAlgorithmSingle(Global.window, Type.HORIZONTAL, Variability.DYNAMIC);
//                case SYSTEMATIC_COVERAGE_HORIZONTAL_MULTIPLE_STATIC:
//                    return new SystematicAlgorithmMultiple(Global.window, Type.HORIZONTAL, Variability.STATIC);
//                case SYSTEMATIC_COVERAGE_HORIZONTAL_MULTIPLE_DYNAMIC:
//                    return new SystematicAlgorithmMultiple(Global.window, Type.HORIZONTAL, Variability.DYNAMIC);
//                // Systematic Coverage Vertical
//                case SYSTEMATIC_COVERAGE_VERTICAL_SINGLE_STATIC:
//                    return new SystematicAlgorithmSingle(Global.window, Type.VERTICAL, Variability.STATIC);
//                case SYSTEMATIC_COVERAGE_VERTICAL_SINGLE_DYNAMIC:
//                    return new SystematicAlgorithmSingle(Global.window, Type.VERTICAL, Variability.DYNAMIC);
//                case SYSTEMATIC_COVERAGE_VERTICAL_MULTIPLE_STATIC:
//                    return new SystematicAlgorithmMultiple(Global.window, Type.VERTICAL, Variability.STATIC);
//                case SYSTEMATIC_COVERAGE_VERTICAL_MULTIPLE_DYNAMIC:
//                    return new SystematicAlgorithmMultiple(Global.window, Type.VERTICAL, Variability.DYNAMIC);
//                // Systematic Coverage Spiral
//                case SYSTEMATIC_COVERAGE_SPIRAL_SINGLE_STATIC:
//                    return new SystematicAlgorithmSingle(Global.window, Type.SPIRAL, Variability.STATIC);
//                case SYSTEMATIC_COVERAGE_SPIRAL_SINGLE_DYNAMIC:
//                    return new SystematicAlgorithmSingle(Global.window, Type.SPIRAL, Variability.DYNAMIC);
//                case SYSTEMATIC_COVERAGE_SPIRAL_MULTIPLE_STATIC:
//                    return new SystematicAlgorithmMultiple(Global.window, Type.SPIRAL, Variability.STATIC);
//                case SYSTEMATIC_COVERAGE_SPIRAL_MULTIPLE_DYNAMIC:
//                    return new SystematicAlgorithmMultiple(Global.window, Type.SPIRAL, Variability.DYNAMIC);
//                // Perimeter Following
//                case PERIMETER_FOLLOWING_SINGLE_STATIC:
//                    return new PerimeterFollowingSingle(Global.window, Variability.STATIC);
//                case PERIMETER_FOLLOWING_SINGLE_DYNAMIC:
//                    return new PerimeterFollowingSingle(Global.window, Variability.DYNAMIC);
//                case PERIMETER_FOLLOWING_MULTIPLE_STATIC:
//                    return new PerimeterFollowingMultiple(Global.window, Variability.STATIC);
//                case PERIMETER_FOLLOWING_MULTIPLE_DYNAMIC:
//                    return new PerimeterFollowingMultiple(Global.window, Variability.DYNAMIC);
//                // Heat Gradient
//                case HEAT_GRADIENT_SINGLE_STATIC:
//                    return new HeatGradientSingle(Global.window, Variability.STATIC);
//                case HEAT_GRADIENT_SINGLE_DYNAMIC:
//                    return new HeatGradientSingle(Global.window, Variability.DYNAMIC);
//                case HEAT_GRADIENT_MULTIPLE_STATIC:
//                    return new HeatGradientMultiple(Global.window, Variability.STATIC);
//                case HEAT_GRADIENT_MULTIPLE_DYNAMIC:
//                    return new HeatGradientMultiple(Global.window, Variability.DYNAMIC);
//                default:
//                    throw new IllegalStateException("Unknown algorithm.");
//            }
        } else {
            return null;
        }
    }
}
