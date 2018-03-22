package me.dufek.securitydrones.main;

import javax.swing.SwingUtilities;
import me.dufek.securitydrones.gui.Window;
import me.dufek.securitydrones.preferences.UserPreferences;

/**
 * The main class used to launch the application.
 *
 * @author Jan Dufek
 */
public class Main {

    /**
     * Main method. It loads user preferences and launched the GUI.
     *
     * @param args String arguments.
     */
    public static void main(String[] args) {
        loadUserPreferences();
        startGUI();
    }

    /**
     * Load user preferences.
     */
    private static void loadUserPreferences() {
        UserPreferences.load();
    }

    /**
     * Start graphical user interface.
     */
    private static void startGUI() {
        Window mainWindow = new Window();
        Global.window = mainWindow;
        SwingUtilities.invokeLater(mainWindow);
    }

}
