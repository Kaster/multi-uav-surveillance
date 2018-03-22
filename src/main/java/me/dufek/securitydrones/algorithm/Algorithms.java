package me.dufek.securitydrones.algorithm;

/**
 * This class represents all the names of algorithms.
 *
 * @author Jan Dufek
 */
public enum Algorithms {

//    // Random Point Coverage
//    RANDOM_POINT_COVERAGE_SINGLE_STATIC("Random Point Coverage - Single - Static"),
//    RANDOM_POINT_COVERAGE_SINGLE_DYNAMIC("Random Point Coverage - Single - Dynamic"),
//    RANDOM_POINT_COVERAGE_MULTIPLE_STATIC("Random Point Coverage - Multiple - Static"),
//    RANDOM_POINT_COVERAGE_MULTIPLE_DYNAMIC("Random Point Coverage - Multiple - Dynamic"),
//    // Spanning Tree Coverage
//    SPANNING_TREE_COVERAGE_SINGLE_STATIC("Spanning Tree Coverage - Single - Static"),
//    SPANNING_TREE_COVERAGE_SINGLE_DYNAMIC("Spanning Tree Coverage - Single - Dynamic"),
//    SPANNING_TREE_COVERAGE_MULTIPLE_STATIC("Spanning Tree Coverage - Multiple - Static"),
//    SPANNING_TREE_COVERAGE_MULTIPLE_DYNAMIC("Spanning Tree Coverage - Multiple - Dynamic"),
//    // Systematic Coverage Horizontal
//    SYSTEMATIC_COVERAGE_HORIZONTAL_SINGLE_STATIC("Systematic Coverage Horizontal - Single - Static"),
//    SYSTEMATIC_COVERAGE_HORIZONTAL_SINGLE_DYNAMIC("Systematic Coverage Horizontal - Single - Dynamic"),
//    SYSTEMATIC_COVERAGE_HORIZONTAL_MULTIPLE_STATIC("Systematic Coverage Horizontal - Multiple - Static"),
//    SYSTEMATIC_COVERAGE_HORIZONTAL_MULTIPLE_DYNAMIC("Systematic Coverage Horizontal - Multiple - Dynamic"),
//    // Systematic Coverage Vertical
//    SYSTEMATIC_COVERAGE_VERTICAL_SINGLE_STATIC("Systematic Coverage Vertical - Single - Static"),
//    SYSTEMATIC_COVERAGE_VERTICAL_SINGLE_DYNAMIC("Systematic Coverage Vertical - Single - Dynamic"),
//    SYSTEMATIC_COVERAGE_VERTICAL_MULTIPLE_STATIC("Systematic Coverage Vertical - Multiple - Static"),
//    SYSTEMATIC_COVERAGE_VERTICAL_MULTIPLE_DYNAMIC("Systematic Coverage Vertical - Multiple - Dynamic"),
//    // Systematic Coverage Spiral
//    SYSTEMATIC_COVERAGE_SPIRAL_SINGLE_STATIC("Systematic Coverage Spiral - Single - Static"),
//    SYSTEMATIC_COVERAGE_SPIRAL_SINGLE_DYNAMIC("Systematic Coverage Spiral - Single - Dynamic"),
//    SYSTEMATIC_COVERAGE_SPIRAL_MULTIPLE_STATIC("Systematic Coverage Spiral - Multiple - Static"),
//    SYSTEMATIC_COVERAGE_SPIRAL_MULTIPLE_DYNAMIC("Systematic Coverage Spiral - Multiple - Dynamic"),
//    // Perimeter Following
//    PERIMETER_FOLLOWING_SINGLE_STATIC("Perimeter Following - Single - Static"),
//    PERIMETER_FOLLOWING_SINGLE_DYNAMIC("Perimeter Following - Single - Dynamic"),
//    PERIMETER_FOLLOWING_MULTIPLE_STATIC("Perimeter Following - Multiple - Static"),
//    PERIMETER_FOLLOWING_MULTIPLE_DYNAMIC("Perimeter Following - Multiple - Dynamic"),
//    // Heat Gradient
//    HEAT_GRADIENT_SINGLE_STATIC("Heat Gradient - Single - Static"),
//    HEAT_GRADIENT_SINGLE_DYNAMIC("Heat Gradient - Single - Dynamic"),
//    HEAT_GRADIENT_MULTIPLE_STATIC("Heat Gradient - Multiple - Static"),
//    HEAT_GRADIENT_MULTIPLE_DYNAMIC("Heat Gradient - Multiple - Dynamic");
    /**
     * Heat Gradient.
     */
    HEAT_GRADIENT_COVERAGE("Heat Gradient"),
    /**
     * Perimeter Following.
     */
    PERIMETER_FOLLOWING("Perimeter Following"),
    /**
     * Random Point Coverage.
     */
    RANDOM_POINT_COVERAGE("Random Point Coverage"),
    /**
     * Spanning Tree Coverage.
     */
    SPANNING_TREE_COVERAGE("Spanning Tree Coverage"),
    /**
     * Systematic Coverage Horizontal.
     */
    SYSTEMATIC_COVERAGE_HORIZONTAL("Systematic Coverage Horizontal"),
    /**
     * Systematic Coverage Vertical.
     */
    SYSTEMATIC_COVERAGE_VERTICAL("Systematic Coverage Vertical"),
    /**
     * Systematic Coverage Spiral.
     */
    SYSTEMATIC_COVERAGE_SPIRAL("Systematic Coverage Spiral");

    /**
     * The name of the algorithm.
     */
    private final String name;

    /**
     * Initialize algorithm.
     *
     * @param name Name.
     */
    private Algorithms(final String name) {
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
