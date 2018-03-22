package me.dufek.securitydrones.gui;

import java.awt.Color;
import java.awt.Font;
import me.dufek.securitydrones.utilities.ColorConverter;

/**
 * Preferences of the GUI. All important preferences are stored in this class.
 * Some of these preferences could be set up from the main application.
 *
 * @author Jan Dufek
 */
public final class Preferences {

    /**
     * Width of the main window as the multiple of screen size.
     */
    public static final double WINDOW_WIDTH_AS_MULTIPLE_OF_SCREEN_WIDTH = 0.8;

    /**
     * Height of the main window as the multiple of screen size.
     */
    public static final double WINDOW_HEIGHT_AS_MULTIPLE_OF_SCREEN_HEIGHT = 0.8;

    /**
     * Icon of the application.
     */
    public static final String ICON_IMAGE_PATH = "icon.png";

    /**
     * Title of the main window.
     */
    public static final String WINDOW_TITLE = "Surveillance Simulator";

    /**
     * How much to magnify the map when zooming in as the multiple of current
     * magnification.
     */
    public static final double MAP_SCALE_IN_FACTOR = 1.2;

    /**
     * How much to decrease size of the map when zooming out in as the multiple
     * of current magnification.
     */
    public static final double MAP_SCALE_OUT_FACTOR = 0.8;

    /**
     * Minimum scale of the map as the multiple of the original map size.
     */
    public static final double MAP_MINIMAL_SCALE = 0.1;

    /**
     * Maximum scale of the map as the multiple of the original map size.
     */
    public static final double MAP_MAXIMUM_SCALE = 10;

    /**
     * Default scale of the map as the multiple of the original map size.
     */
    public static final double MAP_DEFAULT_SCALE = 0.3;

    /**
     * Speed of inertia movement of the map when dragged and dropped.
     */
    public static final double MAP_DRAG_INERTIA_SPEED = 2.5;

    /**
     * Factor of slowing down the inertia movement of the map when dragged and
     * dropped.
     */
    public static final double MAP_DRAG_INERTIA_COOLDOWN = 0.01;

    /**
     * Delay in milli seconds after which the inertia speed of map movement will
     * be slowed down.
     */
    public static final int MAP_TIMER_DELAY = 10;

    public static final int[] LIGHT_BLUE = {195, 220, 223};
    public static final int[] BLUE = {130, 189, 200};
    public static final int[] RED = {132, 45, 81};
    public static final int[] DARK_VIOLET = {36, 26, 47};
    public static final int[] LIGHT_VIOLET = {227, 203, 239};
    public static final int[] GREEN = {51, 146, 106};
    public static final int[] LIGHT_GREEN = {145, 208, 182};
    public static final int[] PINK = {209, 157, 180};
    public static final int[] VERY_LIGHT_BLUE = {191, 245, 255};
    public static final int[] YELLOW = {250, 237, 186};

    public static final Color UAV_COLOR = ColorConverter.getRGBColor(BLUE);
    public static final Color UAV_COLOR_SELECTED = Color.WHITE;
    public static final Color UAV_COLOR_CRASHED = ColorConverter.getRGBColor(PINK);
    public static final Color UAV_COLOR_TAKEOFF = ColorConverter.getRGBColor(VERY_LIGHT_BLUE);
    public static final Color UAV_COLOR_LANDED = ColorConverter.getRGBColor(DARK_VIOLET);
    public static final int UAV_DIAMETER = 10;
    public static final Color UAV_REQUEST_COLOR = ColorConverter.getRGBColor(DARK_VIOLET);
    public static final Color UAV_REQUEST_COLOR_SELECTED = Color.WHITE;
    public static final int UAV_REQUEST_DIAMETER = 10;
    public static final Color UAV_VISIBLE_AREA_COLOR = ColorConverter.getRGBColor(BLUE);
    public static final Color UAV_VISIBLE_AREA_COLOR_SELECTED = UAV_COLOR_SELECTED;
    public static final Color UAV_VISIBLE_AREA_COLOR_CRASHED = UAV_COLOR_CRASHED;
    public static final Color UAV_VISIBLE_AREA_COLOR_TAKEOFF = UAV_COLOR_TAKEOFF;
    public static final Color UAV_VISIBLE_AREA_COLOR_LANDED = UAV_COLOR_LANDED;
    public static final Color UAV_DESTINATION_COLOR = ColorConverter.getRGBColor(VERY_LIGHT_BLUE);
    public static final int UAV_DESTINATION_DIAMETER = 7;
    public static final Color CHARGING_STATION_AREA_COLOR = ColorConverter.getRGBColor(LIGHT_VIOLET);
    public static final Color CHARGING_STATION_AREA_COLOR_SELECTED = Color.WHITE;
    public static final Color CHARGING_STATION_AREA_COLOR_OCCUPIED = ColorConverter.getRGBColor(PINK);
    public static final float CHARGING_STATION_AREA_ALPHA = 0.30F;
    public static final int CHARGING_STATION_DIAMETER = 20;
    public static final Color CHARGING_STATION_PERIMETER_COLOR = ColorConverter.getRGBColor(LIGHT_VIOLET);//Color.WHITE;
    public static final Color CHARGING_STATION_PERIMETER_COLOR_SELECTED = Color.WHITE;
    public static final Color CHARGING_STATION_PERIMETER_COLOR_OCCUPIED = ColorConverter.getRGBColor(PINK);
    public static final float CHARGING_STATION_PERIMETER_ALPHA = 1.00F;
    public static final Color CONSTRUCTED_AREA_COLOR = ColorConverter.getRGBColor(LIGHT_BLUE);
    public static final float CONSTRUCTED_AREA_ALPHA = 0.20F;
    public static final Color CONSTRUCTED_AREA_PERIMETER_COLOR = ColorConverter.getRGBColor(LIGHT_BLUE);
    public static final float CONSTRUCTED_AREA_PERIMETER_ALPHA = 1.00F;
    public static final Color CONSTRUCTED_AREA_END_POINT_COLOR = ColorConverter.getRGBColor(LIGHT_BLUE);
    public static final float CONSTRUCTED_AREA_END_POINT_ALPHA = 1.00F;
    public static final int CONSTRUCTED_AREA_END_POINT_DIAMETER = 10;
    public static final Color AREA_COLOR = ColorConverter.getRGBColor(LIGHT_BLUE);
    public static final float AREA_ALPHA = 0.30F;
    public static final Color AREA_PERIMETER_COLOR = ColorConverter.getRGBColor(GREEN);
    public static final Color AREA_PERIMETER_COLOR_SELECTED = Color.WHITE;
    public static final float AREA_PERIMETER_ALPHA = 1.00F;

    public static final Color SCHEDULE_COLOR_1 = ColorConverter.getRGBColor(BLUE);
    public static final Color SCHEDULE_COLOR_2 = ColorConverter.getRGBColor(PINK);
    public static final Color SCHEDULE_CAPTION_COLOR = Color.BLACK;
    public static final int SCHEDULE_HEIGHT = 50;
    public static final int SCHEDULE_TOP_PADDING = 0;

    public static final int BATTERY_DRUM_NUMBER_OF_COLUMNS = 3;
    public static final int BATTERY_CELLS_VERTICAL_SPACING = 5;
    public static final int BATTERY_CELLS_HORIZONTAL_SPACING = 5;
    public static final Color BATTERY_CELL_COLOR = Color.BLACK;
    public static final int BATTERY_MARKED_EMPTY_PERCENTAGE_LEVEL = 20;
    public static final Color BATTERY_FULL_COLOR = ColorConverter.getRGBColor(GREEN);
    public static final Color BATTERY_EMPTY_COLOR = ColorConverter.getRGBColor(RED);
    public static final int BATTERY_CAPTION_FONT_SIZE = 11;
    public static final int BATTERY_CAPTION_FONT_STYLE = Font.PLAIN;

    public static final int[] SONAR_COOL_COLOR = me.dufek.securitydrones.gui.Preferences.GREEN;
    public static final int[] SONAR_HOT_COLOR = me.dufek.securitydrones.gui.Preferences.RED;
    public static final int SONAR_CAPTION_FONT_SIZE = 11;
    public static final int SONAR_CAPTION_FONT_STYLE = Font.PLAIN;
    public static final int SONAR_PANEL_PREFERED_WIDTH = 300;
    public static final int SONAR_CELLS_HORIZONTAL_SPACING = 5;

    public static final int[] COVERAGE_COOL_COLOR = me.dufek.securitydrones.gui.Preferences.GREEN;
    public static final int[] COVERAGE_HOT_COLOR = me.dufek.securitydrones.gui.Preferences.RED;
    public static final int COVERAGE_CAPTION_FONT_SIZE = 11;
    public static final int COVERAGE_CAPTION_FONT_STYLE = Font.PLAIN;
    public static final int COVERAGE_CELL_VERTICAL_SPACING = 5;
    public static final int COVERAGE_CELL_HORIZONTAL_SPACING = 5;
    public static final Color COVERAGE_CELL_COLOR = Color.BLACK;

    public static final int GRID_REFRESH_FREQUENCY = 10;
    
    public static final String HELP_FILE = "help.pdf";
}
