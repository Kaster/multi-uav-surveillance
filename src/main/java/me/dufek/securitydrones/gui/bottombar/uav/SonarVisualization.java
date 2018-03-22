package me.dufek.securitydrones.gui.bottombar.uav;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Map;
import javax.swing.JPanel;
import me.dufek.securitydrones.conversion.NumberConversion;
import me.dufek.securitydrones.conversion.Scale;
import me.dufek.securitydrones.gui.Preferences;
import me.dufek.securitydrones.utilities.ColorConverter;

/**
 *
 * @author Jan Dufek
 */
public class SonarVisualization extends JPanel {

    public Graphics2D graphics;

    private int panelWidth;
    private int panelHeight;

    private Map<String, Double> sonars;

    public SonarVisualization(Container pane) {
        this.setPreferredSize(new Dimension(Preferences.SONAR_PANEL_PREFERED_WIDTH, 0));
    }

    @Override
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        this.graphics = (Graphics2D) graphics;

        panelWidth = this.getWidth();
        panelHeight = this.getHeight() - 1;

        if (sonars != null) {
            paintSonars();
        }
    }

    private void paintSonars() {

        String[] sonarOrder = new String[]{
            "L4", "L3", "L2", "L1", "M0", "R1", "R2", "R3", "R4"
        };

        int numberOfSonars = sonars.size();

        int sonarCellWidth = panelWidth / numberOfSonars;

        for (int i = 0; i < numberOfSonars; i++) {
            paintSonar(i, sonarCellWidth, sonarOrder[i], sonars.get(sonarOrder[i]));
        }
    }

    private void paintSonar(int number, int width, String name, Double value) {
        paintValue(number, width, value);
        paintCell(number, width);
        paintCaption(number, width, name, value);
    }

    private void paintCell(int number, int width) {
        graphics.setColor(Color.BLACK);
        graphics.drawRect(number * width, 0, width - Preferences.SONAR_CELLS_HORIZONTAL_SPACING, panelHeight);
    }

    private void paintValue(int number, int width, Double value) {
        graphics.setColor(getSonarColor(value));
        graphics.fillRect(number * width, panelHeight - distanceToPixels(value, 5, panelHeight), width - Preferences.BATTERY_CELLS_VERTICAL_SPACING, distanceToPixels(value, 5, panelHeight));
    }

    private void paintCaption(int number, int width, String name, Double value) {
        graphics.setColor(Color.BLACK);
        graphics.setFont(new Font(null, Preferences.SONAR_CAPTION_FONT_STYLE, Preferences.SONAR_CAPTION_FONT_SIZE));

        String stringValue = NumberConversion.roundToNDecimals(value, 1) + " m";
        int stringValueWidth = graphics.getFontMetrics().stringWidth(stringValue);
        int stringValueHeight = 2 * getStringHeight();

        graphics.drawString(stringValue, number * width + (width - Preferences.SONAR_CELLS_HORIZONTAL_SPACING) / 2 - stringValueWidth / 2, panelHeight / 2 + stringValueHeight / 3);

        int nameWidth = graphics.getFontMetrics().stringWidth(name);
        int nameHeight = getStringHeight();

        graphics.drawString(name, number * width + (width - Preferences.SONAR_CELLS_HORIZONTAL_SPACING) / 2 - nameWidth / 2, panelHeight / 2 + stringValueHeight / 3 + nameHeight + 5);
    }

    public Color getSonarColor(double value) {
        double quotient = 1 - (value / 5);

        double redDifference = Preferences.SONAR_COOL_COLOR[0] - Preferences.SONAR_HOT_COLOR[0];
        double greenDifference = Preferences.SONAR_COOL_COLOR[1] - Preferences.SONAR_HOT_COLOR[1];
        double blueDifference = Preferences.SONAR_COOL_COLOR[2] - Preferences.SONAR_HOT_COLOR[2];

        double redAddition = Preferences.SONAR_COOL_COLOR[0] - redDifference * quotient;
        double greenAddition = Preferences.SONAR_COOL_COLOR[1] - greenDifference * quotient;
        double blueAddition = Preferences.SONAR_COOL_COLOR[2] - blueDifference * quotient;

        int[] colorRGB = new int[]{NumberConversion.toInteger(redAddition), NumberConversion.toInteger(greenAddition), NumberConversion.toInteger(blueAddition)};

        Color color = ColorConverter.getRGBColor(colorRGB);

        return color;
    }

    private int getStringHeight() {
        graphics.setFont(new Font(null, Preferences.BATTERY_CAPTION_FONT_STYLE, Preferences.BATTERY_CAPTION_FONT_SIZE));
        int stringHeight = graphics.getFontMetrics().getHeight();
        return stringHeight;
    }

    public void setSonar(Map<String, Double> sonars) {
        this.sonars = sonars;
        refresh();
    }

    private int distanceToPixels(double actualLevel, double maxValue, int cellHeight) {
        return NumberConversion.toInteger(Scale.minMaxScale(actualLevel, 0, maxValue, 0, cellHeight));
    }

    public void refresh() {
        revalidate();
        repaint();
    }
}
