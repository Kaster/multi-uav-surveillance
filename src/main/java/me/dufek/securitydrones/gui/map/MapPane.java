package me.dufek.securitydrones.gui.map;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

/**
 * The part of the GUI showing the bird view on the map and all other
 * information.
 *
 * @author Jan Dufek
 */
public class MapPane {
    private JFrame frame;
    /**
     * Container for the map element.
     */
    private Container pane;
    /**
     * Scrolling pane.
     */
    private JScrollPane scrollPane;
    /**
     * Bird view of the map.
     */
    public Map map;

    public MapPane(Container pane, JFrame frame) {
        this.frame = frame;
        this.pane = pane;
    }

    /**
     * Create the map element.
     */
    public void createMapPane() {

        // Load image
//        BufferedImage mapImage = loadMapImage();

        // Create map
        map = new Map();
        map.refreshImage();

        // Create scroll pane
        createScrollPane();

        // Register listeners for actions
        registerListeners();
    }

    /**
     * Register listeners for events with the map.
     */
    private void registerListeners() {
        Listener componentDragScrollListener = new Listener(map, frame);

        map.addMouseMotionListener(componentDragScrollListener);
        map.addMouseListener(componentDragScrollListener);
        map.addHierarchyListener(componentDragScrollListener);
    }

    /**
     * Creates the scroll pane for the map.
     */
    private void createScrollPane() {
        
        // Create new scroll pane
        scrollPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        // Add map to the scroll pane
        scrollPane.setViewportView(map);

        // Set constrains
        GridBagConstraints constrains = new GridBagConstraints();
        
        constrains.gridx = 0;
        constrains.gridy = 0;
        constrains.gridwidth = 1;
        constrains.gridheight = 1;
        constrains.fill = GridBagConstraints.BOTH;
        constrains.anchor = GridBagConstraints.CENTER;
        constrains.weightx = 1;
        constrains.weighty = 1;
        constrains.insets = new Insets(0, 0, 0, 0);
        constrains.ipadx = 0;
        constrains.ipady = 0;
        
        // Add scroll pane to the pane
        pane.add(scrollPane, constrains);
    }
}
