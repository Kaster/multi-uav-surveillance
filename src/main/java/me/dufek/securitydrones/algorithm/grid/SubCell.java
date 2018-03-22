package me.dufek.securitydrones.algorithm.grid;

import cz.cuni.amis.pogamut.base3d.worldview.object.Location;

/**
 * Sub cell represent small cells contained inside the bigger one.
 *
 * @author Jan Dufek
 */
public class SubCell {

    /**
     * X-coordinate.
     */
    private final double x;

    /**
     * Y-coordinate.
     */
    private final double y;

    /**
     * Width.
     */
    private final double width;

    /**
     * Height.
     */
    private final double height;

    /**
     * Is bounded by spanning tree from top?
     */
    private boolean topCrossed;

    /**
     * Is bounded by spanning tree from bottom?
     */
    private boolean bottomCrossed;

    /**
     * Is bounded by spanning tree from right?
     */
    private boolean rightCrossed;

    /**
     * Is bounded by spanning tree from left?
     */
    private boolean leftCrossed;

    /**
     * Type of sub cell.
     */
    private SubCellType type;

    /**
     * Parent cell.
     */
    private final Cell cell;

    public SubCell(double x, double y, double width, double height, SubCellType type, Cell cell) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.type = type;
        this.cell = cell;
    }

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

    public Location getLocation() {
        return new Location(x, y, 0);
    }

    public Location getCornerLocation() {
        return new Location(this.x + height / 2, this.y - width / 2, 0);
    }

    public void setTopCrossed() {
        this.topCrossed = true;
    }

    public void setTopUnCrossed() {
        this.topCrossed = false;
    }

    public boolean isTopCrossed() {
        return this.topCrossed;
    }

    public void setBottomCrossed() {
        this.bottomCrossed = true;
    }

    public void setBottomUnCrossed() {
        this.bottomCrossed = false;
    }

    public boolean isBottomCrossed() {
        return this.bottomCrossed;
    }

    public void setRightCrossed() {
        this.rightCrossed = true;
    }

    public void setRightUnCrossed() {
        this.rightCrossed = false;
    }

    public boolean isRightCrossed() {
        return this.rightCrossed;
    }

    public void setLeftCrossed() {
        this.leftCrossed = true;
    }

    public void setLeftUnCrossed() {
        this.leftCrossed = false;
    }

    public boolean isLeftCrossed() {
        return this.leftCrossed;
    }

    public SubCellType getType() {
        return this.type;
    }

    public Cell getCell() {
        return this.cell;
    }
}
