package me.dufek.securitydrones.gui.rightbar;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

/**
 *
 * @author Jan Dufek
 */
public class DetailsPanel implements rightBarItem {
    
    private final Container pane;
    private JLabel label;

    public DetailsPanel(Container pane) {
        this.pane = pane;
    }
    
    @Override
    public void create() {
        GridBagConstraints constraints = new GridBagConstraints();

        label = new JLabel();
                label.setMinimumSize(new Dimension(0, Integer.MIN_VALUE));
        label.setMaximumSize(new Dimension(0, Integer.MAX_VALUE));
        label.setHorizontalAlignment(SwingConstants.LEFT);
        label.setVerticalAlignment(SwingConstants.TOP);
        
        label.setBorder(BorderFactory.createTitledBorder("Details"));

        constraints.gridx = 0;
        constraints.gridy = 6;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.weightx = 0.4;
        constraints.weighty = 0.4;
        constraints.insets = new Insets(0, 0, 0, 0);
        constraints.ipadx = 0;
        constraints.ipady = 0;
        
        pane.add(label, constraints);
    }
    
    @Override
    public void setText(String text) {
        label.setText(text);
    }
}
