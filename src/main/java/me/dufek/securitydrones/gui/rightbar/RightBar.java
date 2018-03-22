package me.dufek.securitydrones.gui.rightbar;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Jan Dufek
 */
public class RightBar {

    private final Container pane;
    private JPanel rightBarPanel;
    public PerformancePanel performancePanel;
    public SimulationPanel simulationPanel;
    public AlgorithmPanel algorithmPanel;
    public UAVsPanel uavsPanel;
    public ChargingStationsPanel chargingStationsPanel;
    public AreasPanel areasPanel;
//    public DetailsPanel detailPanel;

    public RightBar(Container pane) {
        this.pane = pane;
        
        rightBarPanel = new JPanel(new GridBagLayout());
        
        performancePanel = new PerformancePanel(rightBarPanel);
        simulationPanel = new SimulationPanel(rightBarPanel);
        algorithmPanel = new AlgorithmPanel(rightBarPanel);
        uavsPanel = new UAVsPanel(rightBarPanel);
        chargingStationsPanel = new ChargingStationsPanel(rightBarPanel);
        areasPanel = new AreasPanel(rightBarPanel);
//        detailPanel = new DetailsPanel(rightBarPanel);
    }

    public void createRightBar() {
        performancePanel.create();
        simulationPanel.create();
        algorithmPanel.create();
        uavsPanel.create();
        chargingStationsPanel.create();
        areasPanel.create();
//        detailPanel.create();
        
        GridBagConstraints constraints = new GridBagConstraints();
        
        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        constraints.gridheight = 2;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.weightx = 0.3;
        constraints.weighty = 0.3;
        constraints.insets = new Insets(0, 0, 0, 0);
        constraints.ipadx = 0;
        constraints.ipady = 0;
        
        pane.add(rightBarPanel, constraints);
    }
}
