package me.dufek.securitydrones.algorithm.grid;

/**
 * This class represents dimensions.
 *
 * @author Jan Dufek
 */
public class Dimension {

    private final double width;
    private final double height;

    public Dimension(double width, double height) {
        this.height = height;
        this.width = width;
    }

    public double getWidth() {
        return this.width;
    }

    public double getHeight() {
        return this.height;
    }
}
