package me.dufek.securitydrones.gui.bottombar.chargingstation;

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
import me.dufek.securitydrones.uav.UAVs;
import me.dufek.securitydrones.chargingstation.schedule.Schedule;
import me.dufek.securitydrones.chargingstation.schedule.Task;

/**
 *
 * @author Jan Dufek
 */
public class ScheduleVisualization extends JPanel {

    private final Container pane;
    public Graphics2D graphics;

    private int timelineLenghtPixels;
    private Schedule schedule;

    public ScheduleVisualization(Container pane) {
        this.pane = pane;

        this.timelineLenghtPixels = this.getWidth() - 5;
    }

    @Override
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        this.graphics = (Graphics2D) graphics;
        this.timelineLenghtPixels = this.getWidth() - 5;

        if (schedule != null) {
            paintSchedule();
        }

        paintTimeline(graphics);
    }

    private void paintTimeline(Graphics graphics) {
        graphics.setColor(Color.BLACK);
        graphics.drawRect(0, Preferences.SCHEDULE_TOP_PADDING, timelineLenghtPixels, Preferences.SCHEDULE_HEIGHT);
    }

    public void paintSchedule() {
        ArrayList<Task> taskList = schedule.getChronologicalSchedule();
        double actualTime = getActualTime();
        
        
        double latestTaskEnd;
        if (taskList.isEmpty()) {
            latestTaskEnd = actualTime;
        } else {
            latestTaskEnd = taskList.get(taskList.size() - 1).getEnd();
        }     

        paintTimelineStartTime(actualTime);
        paintTimelineEndTime(latestTaskEnd, actualTime);
        paintTasks(taskList, actualTime, latestTaskEnd);
    }

    private double getActualTime() {
        double actualTime;
        
        if (UAVs.noUAVs()) {
            actualTime = 0;
        } else {
            actualTime = UAVs.getUAV(0).time;
        }
        
        return actualTime;
    }

    private void paintTasks(ArrayList<Task> taskList, double actualTime, double latestTaskEnd) {
        graphics.setColor(Preferences.SCHEDULE_COLOR_1);

        for (Task task : taskList) {
            
            switchColors();

            int startInPixels = secondsToPixels(task.getStart(), actualTime, latestTaskEnd);
            int endInPixels = secondsToPixels(task.getEnd(), actualTime, latestTaskEnd);
            int widthInPixels = endInPixels - startInPixels;

            paintTaskBar(startInPixels,widthInPixels);

            Color originalColor = graphics.getColor();

            graphics.setColor(Preferences.SCHEDULE_CAPTION_COLOR);
            
            int stringHeight = getStringHeight();
            
            paintUAVName(task, startInPixels, widthInPixels, stringHeight);
            paintTaskStart(task, startInPixels, widthInPixels, stringHeight);
            paintTaskEnd(task, startInPixels, widthInPixels, stringHeight);

            graphics.setColor(originalColor);
        }
    }

    private int getStringHeight() {
        graphics.setFont(new Font(null, Font.PLAIN, 11));
        int stringHeight = graphics.getFontMetrics().getHeight();
        return stringHeight;
    }

    private void paintTaskEnd(Task task, int startInPixels, int widthInPixels, int stringHeight) {
        graphics.setFont(new Font(null, Font.PLAIN, 11));
        String taskEnd = Integer.toString(NumberConversion.toInteger(task.getEnd()));
        int taskEndStringLength = graphics.getFontMetrics().stringWidth(taskEnd);
        this.graphics.drawString(taskEnd, startInPixels + (widthInPixels / 2) - taskEndStringLength / 2, Preferences.SCHEDULE_TOP_PADDING + 3 * stringHeight);
    }

    private void paintTaskStart(Task task, int startInPixels, int widthInPixels, int stringHeight) {
        graphics.setFont(new Font(null, Font.PLAIN, 11));
        String taskStart = Integer.toString(NumberConversion.toInteger(task.getStart()));
        int taskStartStringLength = graphics.getFontMetrics().stringWidth(taskStart);
        this.graphics.drawString(taskStart, startInPixels + (widthInPixels / 2) - taskStartStringLength / 2, Preferences.SCHEDULE_TOP_PADDING + 2 * stringHeight);
    }

    private void paintUAVName(Task task, int startInPixels, int widthInPixels, int stringHeight) {
        graphics.setFont(new Font(null, Font.PLAIN, 11));
        String uavName = task.getUAVName();
        int uavNameStringLenght = graphics.getFontMetrics().stringWidth(uavName);
        this.graphics.drawString(uavName, startInPixels + (widthInPixels / 2) - uavNameStringLenght / 2, Preferences.SCHEDULE_TOP_PADDING + stringHeight);
    }

    private void paintTaskBar(int startInPixels, int widthInPixels) {
        this.graphics.fillRect(startInPixels, Preferences.SCHEDULE_TOP_PADDING, widthInPixels, Preferences.SCHEDULE_HEIGHT);
    }

    private void switchColors() {
        if (graphics.getColor() == Preferences.SCHEDULE_COLOR_1) {
            graphics.setColor(Preferences.SCHEDULE_COLOR_2);
        } else {
            graphics.setColor(Preferences.SCHEDULE_COLOR_1);
        }
    }

    private void paintTimelineEndTime(double latestTaskEnd, double actualTime) {
        graphics.setColor(Color.BLACK);
        graphics.setFont(new Font(null, Font.PLAIN, 11));
        String timelineEndString = Integer.toString(NumberConversion.toInteger(latestTaskEnd)) + " s";
        int timelineEndStringWidth = graphics.getFontMetrics().stringWidth(timelineEndString);
        int timelineEndStringHeight = graphics.getFontMetrics().getHeight();

        graphics.drawLine(secondsToPixels(latestTaskEnd, actualTime, latestTaskEnd), Preferences.SCHEDULE_TOP_PADDING + Preferences.SCHEDULE_HEIGHT, secondsToPixels(latestTaskEnd, actualTime, latestTaskEnd), Preferences.SCHEDULE_TOP_PADDING + Preferences.SCHEDULE_HEIGHT + timelineEndStringHeight);
        graphics.drawString(timelineEndString, secondsToPixels(latestTaskEnd, actualTime, latestTaskEnd) - timelineEndStringWidth - 5, Preferences.SCHEDULE_TOP_PADDING + Preferences.SCHEDULE_HEIGHT + timelineEndStringHeight);
    }

    private void paintTimelineStartTime(double actualTime) {
        graphics.setColor(Color.BLACK);
        graphics.setFont(new Font(null, Font.PLAIN, 11));
        String actualTimeString = Integer.toString(NumberConversion.toInteger(actualTime)) + " s";
        int actualTimeStringHeight = graphics.getFontMetrics().getHeight();

        graphics.drawLine(0, Preferences.SCHEDULE_TOP_PADDING + Preferences.SCHEDULE_HEIGHT, 0, Preferences.SCHEDULE_TOP_PADDING + Preferences.SCHEDULE_HEIGHT + actualTimeStringHeight);
        graphics.drawString(actualTimeString, 5, Preferences.SCHEDULE_TOP_PADDING + Preferences.SCHEDULE_HEIGHT + actualTimeStringHeight);
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
        refresh();
    }

    private int secondsToPixels(double seconds, double actualTime, double latestTaskEnd) {
        return NumberConversion.toInteger(Scale.minMaxScale(seconds, actualTime, latestTaskEnd, 0, timelineLenghtPixels));
    }

    public void refresh() {
        revalidate();
        repaint();
    }
}
