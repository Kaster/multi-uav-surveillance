package me.dufek.securitydrones.uav.visiblearea;

import me.dufek.securitydrones.geometry.PolygonDouble;
import me.dufek.securitydrones.heatmap.HeatCell;
import me.dufek.securitydrones.preferences.UserPreferences;

/**
 * Visible area represents a rectangular area which is visible by the main
 * sensor of UAV (used for surveillance).
 *
 * @author Jan Dufek
 */
public class VisibleArea extends PolygonDouble {

    /**
     * Initialization of visible area.
     * 
     * @param xPoints X points of a polygon.
     * @param yPoints Y points of a polygon.
     */
    public VisibleArea(double[] xPoints, double[] yPoints) {
        super(xPoints, yPoints, xPoints.length);
    }

    /**
     * Checks of the visible area contains given heat cell.
     * 
     * @param cell hear cell.
     * @return True if contains, false otherwise.
     */
    public boolean contains(HeatCell cell) {
        return contains(cell.getX(), cell.getY(), UserPreferences.preferences.CELL_HEIGHT, UserPreferences.preferences.CELL_WIDTH);
    }
}
