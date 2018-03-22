package me.dufek.securitydrones.main;

import java.awt.HeadlessException;
import javax.swing.JOptionPane;
import me.dufek.securitydrones.algorithm.Algorithm;
import me.dufek.securitydrones.gui.Window;
import me.dufek.securitydrones.gui.menu.file.SaveDialog;
import me.dufek.securitydrones.gui.openedfile.OpenedFile;
import me.dufek.securitydrones.preferences.UserPreferences;
import me.dufek.securitydrones.simulation.Simulation;

/**
 * This class contains global data structures shared by various parts of the
 * program.
 *
 * @author Jan Dufek
 */
public class Global {

    /**
     * Main GUI window.
     */
    public static Window window;

    /**
     * Is are selection mode active?
     */
    public static boolean areaSelectionMode;

    /**
     * The name of currently used map.
     */
    private static String mapName;

    /**
     * The reference to the running simulation.
     */
    public static Simulation simulation;

    /**
     * The reference to the running algorithm.
     */
    public static Algorithm algorithm;

    /**
     * Get the name of actual map.
     *
     * @return Map name.
     */
    public static String getMapName() {
        if (mapName != null) {
            return mapName;
        } else {
            return UserPreferences.preferences.DEFAULT_MAP_NAME;
        }
    }

    /**
     * Sets the name of actual map.
     *
     * @param mapName Map name.
     */
    public static void setMapName(String mapName) {
        Global.mapName = mapName;

        if (Global.window != null && Global.window.rightBar != null && Global.window.rightBar.simulationPanel != null) {
            Global.window.rightBar.simulationPanel.setMap(mapName);
        }
    }

    /**
     * Performs safe exit. It will check if there are any unsaved changes.
     *
     * @throws HeadlessException Headless exception.
     */
    public static void saveExit() throws HeadlessException {
        if (OpenedFile.isSaved()) {
            System.exit(0);
        } else {
            //Custom button text
            Object[] options = {"Save",
                "Discart",
                "Cancel"};
            int n = JOptionPane.showOptionDialog(Global.window.frame,
                    "You have unsaved changes.",
                    "Unsaved Changes",
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]);

            switch (n) {
                case 0:
                    (new SaveDialog(Global.window.frame)).actionPerformed(null);
                    System.exit(0);
                    break;
                case 1:
                    System.exit(0);
                    break;
                case 2:
                    break;
            }

        }
    }
}
