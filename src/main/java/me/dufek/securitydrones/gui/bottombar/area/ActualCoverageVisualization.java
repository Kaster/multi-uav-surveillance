package me.dufek.securitydrones.gui.bottombar.area;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import javax.swing.JPanel;
import me.dufek.securitydrones.conversion.NumberConversion;
import me.dufek.securitydrones.conversion.Scale;
import me.dufek.securitydrones.gui.Preferences;
import me.dufek.securitydrones.battery.Battery;
import me.dufek.securitydrones.battery.BatteryDrum;
import me.dufek.securitydrones.preferences.UserPreferences;
import me.dufek.securitydrones.utilities.ColorConverter;

/**
 *
 * @author Jan Dufek
 */
public class ActualCoverageVisualization extends JPanel {

    private final Container pane;
    public Graphics2D graphics;

    private int width;
    private int height;

    private Double actualCoverage;

    public ActualCoverageVisualization(Container pane) {
        this.pane = pane;
    }

    @Override
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        this.graphics = (Graphics2D) graphics;

        width = getColumnWidth();
        height = getColumnHeight();

        if (actualCoverage != null) {
            paintActualCoverage();
        }
    }

    public void paintActualCoverage() {
        paintActualCoverageLevel();
        paintActualCoverageCell();
        paintActualCoverageCaption();

    }

    private void paintActualCoverageCaption() {
        graphics.setFont(new Font(null, Preferences.COVERAGE_CAPTION_FONT_STYLE, Preferences.COVERAGE_CAPTION_FONT_SIZE));

        String caption = Integer.toString(NumberConversion.toInteger(actualCoverage)) + " s";
        int captionWidth = graphics.getFontMetrics().stringWidth(caption);
        int captionHeight = getStringHeight();

        graphics.drawString(caption, (width - Preferences.COVERAGE_CELL_HORIZONTAL_SPACING) / 2 - captionWidth / 2, (height - Preferences.COVERAGE_CELL_VERTICAL_SPACING) / 2 + captionHeight / 3);
    }

    private void paintActualCoverageCell() {
        graphics.setColor(Preferences.COVERAGE_CELL_COLOR);
        graphics.drawRect(0, 0, width - Preferences.COVERAGE_CELL_HORIZONTAL_SPACING, height - Preferences.COVERAGE_CELL_VERTICAL_SPACING);
    }

    private void paintActualCoverageLevel() {
        Color color = determineColorIndicatingQualityOfCoverage();

        graphics.setColor(color);
        
        double actualCoverageLimited = actualCoverage < UserPreferences.preferences.MAXIMAL_HEAT ? actualCoverage : UserPreferences.preferences.MAXIMAL_HEAT;

        graphics.fillRect(0, 0, coverageToPixels(actualCoverageLimited, UserPreferences.preferences.MAXIMAL_HEAT, width - Preferences.COVERAGE_CELL_HORIZONTAL_SPACING), height - Preferences.COVERAGE_CELL_VERTICAL_SPACING);
    }

    private Color determineColorIndicatingQualityOfCoverage() {
        double actualCoverageLimited = actualCoverage < UserPreferences.preferences.MAXIMAL_HEAT ? actualCoverage : UserPreferences.preferences.MAXIMAL_HEAT;
        
        double quotient = actualCoverageLimited / UserPreferences.preferences.MAXIMAL_HEAT;

        double redDifference = Preferences.COVERAGE_COOL_COLOR[0] - Preferences.COVERAGE_HOT_COLOR[0];
        double greenDifference = Preferences.COVERAGE_COOL_COLOR[1] - Preferences.COVERAGE_HOT_COLOR[1];
        double blueDifference = Preferences.COVERAGE_COOL_COLOR[2] - Preferences.COVERAGE_HOT_COLOR[2];

        double redAddition = Preferences.COVERAGE_COOL_COLOR[0] - redDifference * quotient;
        double greenAddition = Preferences.COVERAGE_COOL_COLOR[1] - greenDifference * quotient;
        double blueAddition = Preferences.COVERAGE_COOL_COLOR[2] - blueDifference * quotient;

        int[] colorRGB = new int[]{NumberConversion.toInteger(redAddition), NumberConversion.toInteger(greenAddition), NumberConversion.toInteger(blueAddition)};

        Color color = ColorConverter.getRGBColor(colorRGB);

        return color;
    }

    private int getColumnWidth() {
        return this.getWidth();
    }

    private int getColumnHeight() {
        return this.getHeight();
    }

    private int getStringHeight() {
        graphics.setFont(new Font(null, Preferences.COVERAGE_CAPTION_FONT_STYLE, Preferences.COVERAGE_CAPTION_FONT_SIZE));
        int stringHeight = graphics.getFontMetrics().getHeight();
        return stringHeight;
    }

    public void setActualCoverage(Double actualCoverage) {
        this.actualCoverage = actualCoverage;
        refresh();
    }

    private int coverageToPixels(double actualCoverage, double worstCoverage, int cellWidth) {
        return NumberConversion.toInteger(Scale.minMaxScale(actualCoverage, 0, worstCoverage, 0, cellWidth));
    }

    public void refresh() {
        revalidate();
        repaint();
    }
}
