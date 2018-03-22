package me.dufek.securitydrones.gui.birdview;

import java.io.Serializable;

/**
 * Represents one axis of 3D space.
 * 
 * @author Jan Dufek
 */
public class Axis implements Serializable {
    // Minimum of the axis
    public double min;
    
    // Maximum of the axis
    public double max;
    
    public Axis(double min, double max) {
        this.min = min;
        this.max = max;
    }
    
    /**
     * Update boundaries of the axis with new coordinates.
     * 
     * @param currentValue current coordinates
     */
    public void updateBoundaries(double currentValue) {
        if (currentValue > this.max) {
            this.max = currentValue;
        } else if (currentValue < this.min) {
            this.min = currentValue;
        }
    }
}
