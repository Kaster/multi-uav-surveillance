package me.dufek.securitydrones.algorithm.grid;

import cz.cuni.amis.pogamut.base3d.worldview.object.Location;
import java.awt.Rectangle;
import java.io.Serializable;
import me.dufek.securitydrones.area.Area;
import me.dufek.securitydrones.area.Areas;
import me.dufek.securitydrones.heatmap.HeatCell;
import me.dufek.securitydrones.preferences.UserPreferences;

/**
 * One cell of the grid.
 *
 * @author Jan Dufek
 */
public class Cell implements Serializable {

    /**
     * X-coordinate.
     */
    private double x;

    /**
     * Y-coordinate.
     */
    private double y;

    /**
     * Width of cell.
     */
    private final double width;

    /**
     * Height of cell.
     */
    private final double height;

    /**
     * All the neighbors of the cell.
     */
    private final Neighbours neighbours = new Neighbours(this);

    /**
     * Particular sub-cells contained in this cell.
     */
    private final SubCells subCells;

    /**
     * Number of this cell.
     */
    private final int number;

    /**
     * Cell's row in the grid.
     */
    private final int i;

    /**
     * Cell's column in the grid.
     */
    private final int j;

    /**
     * Is this cell inside spanning tree?
     */
    private boolean inSpanningTree = false;

    /**
     * Initialization of the cell.
     *
     * @param x X-coordinate.
     * @param y Y-coordinate.
     * @param width Width.
     * @param height Height.
     * @param number Number.
     * @param i Row in grid.
     * @param j Column in grid.
     */
    public Cell(double x, double y, double width, double height, int number, int i, int j) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.number = number;
        this.i = i;
        this.j = j;

        this.subCells = new SubCells(x, y, width, height, this);
    }

    public int getNumber() {
        return this.number;
    }

    public int getI() {
        return this.i;
    }

    public int getJ() {
        return this.j;
    }
//    public Cell(Location location, double width, double height) {
//        this.x = location.getX();
//        this.y = location.getY();
//        this.width = width;
//        this.height = height;
//    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getWidth() {
        return this.width;
    }

    public double getHeight() {
        return this.height;
    }

    public void setLocation(Location location) {
        this.x = location.getX();
        this.y = location.getY();
    }

    public Location getLocation() {
        return new Location(this.x, this.y, 0);
    }

    public Location getCenterLocation() {
        return new Location(this.x, this.y, 0);
    }

    public Location getTopLeftCornerLocation() {
        return new Location(this.x + height / 2, this.y - width / 2, 0);
    }

    public Location getTopRightCornerLocation() {
        return new Location(this.x + height / 2, this.y + width / 2, 0);
    }

    public Location getBottomLeftCornerLocation() {
        return new Location(this.x - height / 2, this.y - width / 2, 0);
    }

    public Location getBottomRightCornerLocation() {
        return new Location(this.x - height / 2, this.y + width / 2, 0);
    }

    public void setTopNeighbour(Cell cell, double distance) {
        this.neighbours.setTopNeighbour(cell, distance);
    }

    public Neighbour getTopNeighbour() {
        return this.neighbours.getTopNeighbour();
    }

    public void setBottomNeighbour(Cell cell, double distance) {
        this.neighbours.setBottomNeighbour(cell, distance);
    }

    public Neighbour getBottomNeighbour() {
        return this.neighbours.getBottomNeighbour();
    }

    public void setRightNeighbour(Cell cell, double distance) {
        this.neighbours.setRightNeighbour(cell, distance);
    }

    public Neighbour getRightNeighbour() {
        return this.neighbours.getRightNeighbour();
    }

    public void setLeftNeighbour(Cell cell, double distance) {
        this.neighbours.setLeftNeighbour(cell, distance);
    }

    public Neighbour getLeftNeighbour() {
        return this.neighbours.getLeftNeighbour();
    }

    public void setInSpanningTree() {
        this.inSpanningTree = true;
    }

    public boolean isInSpanningTree() {
        return this.inSpanningTree;
    }

    public Neighbours getNeighbours() {
        return this.neighbours;
    }

    public SubCells getSubCells() {
        return this.subCells;
    }

    /**
     * Get heat of this cell. Heat is get as an average heat of all the heat
     * cells under.
     *
     * @return Heat.
     */
    public double getHeat() {
        double numberOfHeatCells = 0;
        double heatSum = 0;

        for (Area area : Areas.listOfAreas) {
            if (area.getGrid() == null) {
                continue;
            }

            for (HeatCell heatCell : area.getGrid().getCells()) {
                if (contains(heatCell)) {
                    numberOfHeatCells++;
                    heatSum += heatCell.getHeat();
                }
            }
        }

        return heatSum / numberOfHeatCells;
    }

    /**
     * Checks if the cell contains particular heat cell.
     *
     * @param heatCell Heat cell.
     * @return True if it contains and false if not.
     */
    public boolean contains(HeatCell heatCell) {
        Location cellCornerLocation = getTopLeftCornerLocation();

        if (cellCornerLocation.getX() > heatCell.getX() && heatCell.getX() - UserPreferences.preferences.CELL_HEIGHT > cellCornerLocation.getX() - height) {
            if (cellCornerLocation.getY() < heatCell.getY() && heatCell.getY() + UserPreferences.preferences.CELL_WIDTH < cellCornerLocation.getY() + width) {
                return true;
            }
        }

        return false;
    }
}
