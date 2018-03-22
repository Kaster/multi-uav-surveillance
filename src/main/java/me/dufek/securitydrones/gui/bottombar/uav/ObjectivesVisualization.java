package me.dufek.securitydrones.gui.bottombar.uav;

import java.awt.Dimension;
import java.awt.Rectangle;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.Scrollable;

/**
 *
 * @author Jan Dufek
 */
public class ObjectivesVisualization extends JPanel implements Scrollable {

    ObjectivesVisualization(JLabel objectives, int VERTICAL_SCROLLBAR_AS_NEEDED, int HORIZONTAL_SCROLLBAR_NEVER) {
        super();
        this.add(objectives);
    }

    @Override
    public boolean getScrollableTracksViewportWidth() {
        return true;
    }    

    @Override
    public Dimension getPreferredScrollableViewportSize() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getScrollableUnitIncrement(Rectangle rctngl, int i, int i1) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getScrollableBlockIncrement(Rectangle rctngl, int i, int i1) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean getScrollableTracksViewportHeight() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
