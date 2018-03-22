package me.dufek.securitydrones.heatmap;

import java.io.Serializable;
import java.util.ArrayList;
import me.dufek.securitydrones.area.Area;
import me.dufek.securitydrones.geometry.Bounds;
import me.dufek.securitydrones.preferences.UserPreferences;

/**
 * Heat grid is the main component for surveillance quality analyzes. It
 * consists of heat cells which covers whole area. Then heat of each cell is
 * either increased or set to zero if it is visible from any UAV.
 *
 * @author Jan Dufek
 */
public class HeatGrid implements Serializable {

    /**
     * Heat cells.
     */
    private ArrayList<HeatCell> cells;

    /**
     * Initialization of a heat gird. It creates the grid automatically.
     *
     * @param area Area for which the grid should be created.
     */
    public HeatGrid(Area area) {
        cells = new ArrayList<HeatCell>();
        createGrid(area);
//        System.out.println("Heat grid created!");
    }

    /**
     * Method used to crate a heat grid for the area.
     * 
     * @param area Area.
     */
    private void createGrid(Area area) {
//        Rectangle2D bounds = area.getBounds2D();
        Bounds bounds = area.getBoundsDouble();

        double xMin = bounds.getMinX();
        double xMax = bounds.getMaxX();
        double yMin = bounds.getMinY();
        double yMax = bounds.getMaxY();

        int numberOfVerticalCells = (int) Math.ceil((xMax - xMin) / UserPreferences.preferences.CELL_HEIGHT);
        int numberOfHorizontalCells = (int) Math.ceil((yMax - yMin) / UserPreferences.preferences.CELL_WIDTH);

        for (int i = 0; i < numberOfVerticalCells; i++) {
            for (int j = 0; j < numberOfHorizontalCells; j++) {
                double x = xMax - i * UserPreferences.preferences.CELL_HEIGHT;
                double y = yMin + j * UserPreferences.preferences.CELL_WIDTH;
                HeatCell cell = new HeatCell(x, y);

                if (area.contains(cell)) {
                    cells.add(cell);
                }
            }
        }
    }

    /**
     * Get list of all cells.
     * 
     * @return List of cells.
     */
    public ArrayList<HeatCell> getCells() {
        return this.cells;
    }

    /**
     * Heat up whole heat grid.
     */
    public void heatUp() {
        for (HeatCell cell : cells) {
            cell.heatUp();
        }
    }
}
