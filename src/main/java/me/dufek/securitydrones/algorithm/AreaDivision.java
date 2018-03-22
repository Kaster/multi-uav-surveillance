package me.dufek.securitydrones.algorithm;

/**
 * This class represent variants of algorithms regarding area division.
 *
 * @author Jan Dufek
 */
public enum AreaDivision {

    /**
     * Single version of algorithm.
     */
    SINGLE("Single"),
    /**
     * Multiple version of algorithm.
     */
    MULTIPLE("Multiple");

    /**
     * The name of the variant.
     */
    private final String name;

    /**
     * Initialize variant.
     *
     * @param name Name.
     */
    private AreaDivision(final String name) {
        this.name = name;
    }

    /**
     * Get string name.
     *
     * @return Name.
     */
    @Override
    public String toString() {
        return name;
    }
}
