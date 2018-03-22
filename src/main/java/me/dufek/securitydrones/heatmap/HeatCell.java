package me.dufek.securitydrones.heatmap;

import cz.cuni.amis.pogamut.base3d.worldview.object.Location;
import java.awt.Color;
import java.io.Serializable;
import me.dufek.securitydrones.conversion.NumberConversion;
import me.dufek.securitydrones.preferences.UserPreferences;
import me.dufek.securitydrones.utilities.ColorConverter;
import me.dufek.securitydrones.utilities.RandomNumber;

/**
 * Heat cell is the main part of the heat grid. It represents one small unit
 * which is keeping a heat.
 *
 * @author Jan Dufek
 */
public class HeatCell implements Serializable {

    private double x;
    private double y;
    private double heat;

    public HeatCell(double x, double y) {
        this.x = x;
        this.y = y;
        this.heat = 0;
//        this.heat = RandomNumber.getDouble(0, 120);
    }

    public HeatCell(double x, double y, double heat) {
        this.x = x;
        this.y = y;
        this.heat = heat;
    }

    public HeatCell(Location location) {
        this.x = location.getX();
        this.y = location.getY();
        this.heat = 0;
    }

    public HeatCell(Location location, double heat) {
        this.x = location.getX();
        this.y = location.getY();
        this.heat = heat;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public void setLocation(Location location) {
        this.x = location.getX();
        this.y = location.getY();
    }

    public Location getLocation() {
        return new Location(this.x, this.y, 0);
    }

    public void coolDown() {
        this.heat = 0;
    }

    public void heatUp() {
//        if (heat + me.dufek.securitydrones.simulation.Preferences.getSimulationLogicIntervalSeconds() <= UserPreferences.preferences.MAXIMAL_HEAT) {
//            this.heat += me.dufek.securitydrones.simulation.Preferences.getSimulationLogicIntervalSeconds();
//        } else {
//            this.heat = UserPreferences.preferences.MAXIMAL_HEAT;
//        }

        this.heat += me.dufek.securitydrones.simulation.Preferences.getSimulationLogicIntervalSeconds();
    }

    public void resetHeat() {
        this.heat = 0;
    }

    public Color getHeatColor() {
        double heatLimited = heat < UserPreferences.preferences.MAXIMAL_HEAT ? heat : UserPreferences.preferences.MAXIMAL_HEAT;

        double heatQuotient = heatLimited / UserPreferences.preferences.MAXIMAL_HEAT;

        double redDifference = Preferences.CELL_COOL_COLOR[0] - Preferences.CELL_HOT_COLOR[0];
        double greenDifference = Preferences.CELL_COOL_COLOR[1] - Preferences.CELL_HOT_COLOR[1];
        double blueDifference = Preferences.CELL_COOL_COLOR[2] - Preferences.CELL_HOT_COLOR[2];

        double redAddition = Preferences.CELL_COOL_COLOR[0] - redDifference * heatQuotient;
        double greenAddition = Preferences.CELL_COOL_COLOR[1] - greenDifference * heatQuotient;
        double blueAddition = Preferences.CELL_COOL_COLOR[2] - blueDifference * heatQuotient;

        int[] colorRGB = new int[]{NumberConversion.toInteger(redAddition), NumberConversion.toInteger(greenAddition), NumberConversion.toInteger(blueAddition)};

        Color color = ColorConverter.getRGBColor(colorRGB);

        return color;
    }

    public double getHeat() {
        return this.heat;
    }
}
