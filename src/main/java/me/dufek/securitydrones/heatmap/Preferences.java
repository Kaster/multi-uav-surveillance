package me.dufek.securitydrones.heatmap;

/**
 *
 * @author Jan Dufek
 */
public class Preferences {

    /**
     * Width of one heat map cell.
     */
    public static final double CELL_WIDTH = 3;

    /**
     * Height of one heat map cell.
     */
    public static final double CELL_HEIGHT = 3;
    
    /**
     * Color of coll cell.
     */
    public static final int[] CELL_COOL_COLOR = me.dufek.securitydrones.gui.Preferences.GREEN;
    
    /**
     * Color of hot cell.
     */
    public static final int[] CELL_HOT_COLOR = me.dufek.securitydrones.gui.Preferences.RED;
    
    /**
     * Threshold for actual heat. Above this threshold the heat is considered to
     * be hot. Then it is indicated in maximum red and this color is not changing
     * anymore. However, the actual heat may still rise.
     */
    public static final double MAXIMAL_HEAT = 1 * 60 * 3;
}
