package me.dufek.securitydrones.utilities;

import java.awt.Color;

/**
 * Conversion between colors.
 *
 * @author Jan Dufek
 */
public class ColorConverter {

    public static Color getRGBColor(int red, int green, int blue) {
        float[] colorHSBArray = Color.RGBtoHSB(red, green, blue, null);
        Color colorHSB = Color.getHSBColor(colorHSBArray[0], colorHSBArray[1], colorHSBArray[2]);

        return colorHSB;
    }

    public static Color getRGBColor(int[] rgb) {
        return getRGBColor(rgb[0], rgb[1], rgb[2]);
    }
}
