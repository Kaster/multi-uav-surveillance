package me.dufek.securitydrones.gui.bottombar.chargingstation;

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
public class BatteryDrumVisualization extends JPanel {

    private final Container pane;
    public Graphics2D graphics;

    private int columnWidth;
    private int columnHeight;
    private int batteryCellHeight;

    private int maximalNumberOfBatteriesInColumn;

    private BatteryDrum batteryDrum;

    public BatteryDrumVisualization(Container pane) {
        this.pane = pane;
    }

    @Override
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        this.graphics = (Graphics2D) graphics;

        columnWidth = getColumnWidth();
        columnHeight = getColumnHeight();

        if (batteryDrum != null) {
            paintBatteryDrum();
        }
    }

    public void paintBatteryDrum() {
        ArrayList<Battery> batteries = batteryDrum.getBatteriesByCapacity();

//        // Check batteries order
//        System.out.println("Battery Drum:");
//        for (int i = 0; i < batteries.size() - 1; i++) {
//            System.out.print(batteries.get(i).getActualLevel() + " ");
//            if (batteries.get(i).getActualLevel() < batteries.get(i + 1).getActualLevel()) {
//                System.out.println(batteries.get(i + 1).getActualLevel());
//                throw new IllegalStateException("Batteries not in order!");
//            }
//        }
//        System.out.println(batteries.get(batteries.size() - 1).getActualLevel());

        int numberOfBatteries = batteries.size();

        int batteriesInColumnDiv = numberOfBatteries / Preferences.BATTERY_DRUM_NUMBER_OF_COLUMNS;
        int batteriesInColumnMod = numberOfBatteries % Preferences.BATTERY_DRUM_NUMBER_OF_COLUMNS;

        int oneRemainder = (batteriesInColumnMod != 0 ? 1 : 0);

        batteryCellHeight = columnHeight / (batteriesInColumnDiv + oneRemainder);

        maximalNumberOfBatteriesInColumn = batteriesInColumnDiv + oneRemainder;

        for (int i = 0; i < Preferences.BATTERY_DRUM_NUMBER_OF_COLUMNS; i++) {
            int numberOfBatteriesInCurrentColumn;

            if (numberOfBatteries - maximalNumberOfBatteriesInColumn < 0) {
                numberOfBatteriesInCurrentColumn = numberOfBatteries;
            } else {
                numberOfBatteriesInCurrentColumn = maximalNumberOfBatteriesInColumn;
            }

            numberOfBatteries -= numberOfBatteriesInCurrentColumn;

            paintColumns(i, numberOfBatteriesInCurrentColumn, batteries);
        }
    }

    private void paintColumns(int columnNumber, int numberOfBatteries, ArrayList<Battery> batteries) {

        for (int i = 0; i < numberOfBatteries; i++) {

            Battery battery = getCurrentBattery(batteries, columnNumber, i);

            paintBatteryActualLevel(battery, columnNumber, i);
            paintBatteryCell(columnNumber, i);
            paintBatteryCaption(battery, columnNumber, i);
        }
    }

    private void paintBatteryCaption(Battery battery, int columnNumber, int i) {
        graphics.setFont(new Font(null, Preferences.BATTERY_CAPTION_FONT_STYLE, Preferences.BATTERY_CAPTION_FONT_SIZE));

        String caption = Integer.toString(NumberConversion.toInteger(battery.getActualLevel())) + " / " + Integer.toString(NumberConversion.toInteger(battery.getCapacity())) + " mAh (" + battery.getBatteryPercentage() + "%)";
        int captionWidth = graphics.getFontMetrics().stringWidth(caption);
        int captionHeight = getStringHeight();

        graphics.drawString(caption, columnNumber * columnWidth + (columnWidth - Preferences.BATTERY_CELLS_HORIZONTAL_SPACING) / 2 - captionWidth / 2, i * batteryCellHeight + (batteryCellHeight - Preferences.BATTERY_CELLS_VERTICAL_SPACING) / 2 + captionHeight / 3);
    }

    private void paintBatteryCell(int columnNumber, int i) {
        graphics.setColor(Preferences.BATTERY_CELL_COLOR);
        graphics.drawRect(columnNumber * columnWidth, i * batteryCellHeight, columnWidth - Preferences.BATTERY_CELLS_HORIZONTAL_SPACING, batteryCellHeight - Preferences.BATTERY_CELLS_VERTICAL_SPACING);
    }

    private void paintBatteryActualLevel(Battery battery, int columnNumber, int i) {
        determineColorIndicatingFullness(battery);

        graphics.fillRect(columnNumber * columnWidth, i * batteryCellHeight, capacityToPixels(battery.getActualLevel(), battery.getCapacity(), columnWidth - Preferences.BATTERY_CELLS_HORIZONTAL_SPACING), batteryCellHeight - Preferences.BATTERY_CELLS_VERTICAL_SPACING);
    }

    private void determineColorIndicatingFullness(Battery battery) {
        if (battery.getBatteryPercentage() < Preferences.BATTERY_MARKED_EMPTY_PERCENTAGE_LEVEL) {
            graphics.setColor(Preferences.BATTERY_EMPTY_COLOR);
        } else {
            graphics.setColor(Preferences.BATTERY_FULL_COLOR);
        }
    }

    private Battery getCurrentBattery(ArrayList<Battery> batteries, int columnNumber, int i) {
        Battery battery = batteries.get(columnNumber * maximalNumberOfBatteriesInColumn + i);
        return battery;
    }

    private int getColumnWidth() {
        return this.getWidth() / Preferences.BATTERY_DRUM_NUMBER_OF_COLUMNS;
    }

    private int getColumnHeight() {
        return this.getHeight();
    }

    private int getStringHeight() {
        graphics.setFont(new Font(null, Preferences.BATTERY_CAPTION_FONT_STYLE, Preferences.BATTERY_CAPTION_FONT_SIZE));
        int stringHeight = graphics.getFontMetrics().getHeight();
        return stringHeight;
    }

    public void setBatteryDrum(BatteryDrum batteryDrum) {
        this.batteryDrum = batteryDrum;
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
