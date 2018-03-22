package me.dufek.securitydrones.algorithm;

/**
 * This class represents variants of algorithms with respect to their
 * variability.
 *
 * @author Jan Dufek
 */
public enum Variability {

    /**
     * Static version.
     */
    STATIC("Static"),
    
    /**
     * Dynamic version.
     */
    DYNAMIC("Dynamic");

    /**
     * The name of the variant.
     */
    private final String name;

    /**
     * Initialize variability.
     *
     * @param name Name.
     */
    private Variability(final String name) {
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
