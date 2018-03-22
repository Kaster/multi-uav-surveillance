package me.dufek.securitydrones.gui.bottombar.uav;

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

/**
 *
 * @author Jan Dufek
 */
public class BatteryVisualization extends JPanel {

    private final Container pane;
    public Graphics2D graphics;

    private int panelWidth;
//    private int panelHeight;
    private int panelHeight;
    
    private Battery battery;

    public BatteryVisualization(Container pane) {
        this.pane = pane;
    }

    @Override
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        this.graphics = (Graphics2D) graphics;

        panelWidth = this.getWidth();
        panelHeight = this.getHeight();

        if (battery != null) {
            paintBattery();
        }
    }

    public void paintBattery() {
            paintBatteryActualLevel(battery);
            paintBatteryCell();
            paintBatteryCaption(battery);
    }

    private void paintBatteryCaption(Battery battery) {
        graphics.setFont(new Font(null, Preferences.BATTERY_CAPTION_FONT_STYLE, Preferences.BATTERY_CAPTION_FONT_SIZE));

        String caption = Integer.toString(NumberConversion.toInteger(battery.getActualLevel())) + " / " + Integer.toString(NumberConversion.toInteger(battery.getCapacity())) + " mAh (" + battery.getBatteryPercentage() + "%)";
        int captionWidth = graphics.getFontMetrics().stringWidth(caption);
        int captionHeight = getStringHeight();

        graphics.drawString(caption, (panelWidth - Preferences.BATTERY_CELLS_HORIZONTAL_SPACING) / 2 - captionWidth / 2, (panelHeight - Preferences.BATTERY_CELLS_VERTICAL_SPACING) / 2 + captionHeight / 3);
    }

    private void paintBatteryCell() {
        graphics.setColor(Preferences.BATTERY_CELL_COLOR);
        graphics.drawRect(0, 0, panelWidth - Preferences.BATTERY_CELLS_HORIZONTAL_SPACING, panelHeight - Preferences.BATTERY_CELLS_VERTICAL_SPACING);
    }

    private void paintBatteryActualLevel(Battery battery) {
        determineColorIndicatingFullness(battery);

        graphics.fillRect(0, 0, capacityToPixels(battery.getActualLevel(), battery.getCapacity(), panelWidth - Preferences.BATTERY_CELLS_HORIZONTAL_SPACING), panelHeight - Preferences.BATTERY_CELLS_VERTICAL_SPACING);
    }

    private void determineColorIndicatingFullness(Battery battery) {
        if (battery.getBatteryPercentage() < Preferences.BATTERY_MARKED_EMPTY_PERCENTAGE_LEVEL) {
            graphics.setColor(Preferences.BATTERY_EMPTY_COLOR);
        } else {
            graphics.setColor(Preferences.BATTERY_FULL_COLOR);
        }
    }

    private int getStringHeight() {
        graphics.setFont(new Font(null, Preferences.BATTERY_CAPTION_FONT_STYLE, Preferences.BATTERY_CAPTION_FONT_SIZE));
        int stringHeight = graphics.getFontMetrics().getHeight();
        return stringHeight;
    }

    public void setBattery(Battery battery) {
        this.battery = battery;
        refresh();
    }

    private int capacityToPixels(double actualLevel, double capacity, int cellWidth) {
        return NumberConversion.toInteger(Scale.minMaxScale(actualLevel, 0, capacity, 0, cellWidth));
    }

    public void refresh() {
        revalidate();
        repaint();
    }
}
