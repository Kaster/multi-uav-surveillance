package me.dufek.securitydrones.geometry;

/**
 * Bounds represents an extend of a shape.
 *
 * @author Jan Dufek
 */
public class Bounds {

    private double minX;
    private double maxX;
    private double minY;
    private double maxY;

    public Bounds(double minX, double maxX, double minY, double maxY) {
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
    }

    public void setMinX(double minX) {
        this.minX = minX;
    }

    public double getMinX() {
        return this.minX;
    }

    public void setMaxX(double maxX) {
        this.maxX = maxX;
    }

    public double getMaxX() {
        return this.maxX;
    }

    public void setMinY(double minY) {
        this.minY = minY;
    }

    public double getMinY() {
        return this.minY;
    }

    public void setMaxY(double maxY) {
        this.maxY = maxY;
    }

    public double getMaxY() {
        return this.maxY;
    }
}
