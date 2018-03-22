package me.dufek.securitydrones.area;

import java.io.Serializable;

/**
 * Type of are.
 *
 * @author Jan Dufek
 */
public enum AreaType implements Serializable {

    /**
     * Guarded is a surveyed area.
     */
    GUARDED("Guarded");

    private final String name;

    private AreaType(final String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
