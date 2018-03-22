package me.dufek.securitydrones.gui.birdview.check;

import java.awt.HeadlessException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import javax.swing.JOptionPane;
import me.dufek.securitydrones.gui.birdview.Birdview;
import me.dufek.securitydrones.gui.birdview.MapBoundaries;
import me.dufek.securitydrones.gui.map.MapInformation;
import me.dufek.securitydrones.main.Global;

/**
 *
 * @author Jan Dufek
 */
public class BirdviewChecker {

    public static int check(String mapName) {
        if (!birdviewExists(mapName)) {

            int choice = askIfCreateBirdview(mapName);

            switch (choice) {
                case -1: // Close
                    return -1;
                case 0: // Yes
                    Global.setMapName(mapName);
                    Birdview birdview = new Birdview(mapName);
                    birdview.start();
                    loadMapInformation();
                    return 0;
                case 1: // No
                    Global.setMapName(mapName);
                    return 1;
                case 2: // Other Map
                    String newMapName = getMapName();
                    return check(newMapName);
                case 3: // Cancel
                    return 3;
                default:
                    throw new IllegalStateException("Unknown choice.");
            }
        } else { // Birdview exists
            Global.setMapName(mapName);
            refreshMap();
            return 4;
        }
    }

    private static boolean birdviewExists(String mapName) {
        File mapFile = new File(mapName + ".jpg");
        File informationFile = new File(mapName + ".information");
        return mapFile.exists() && informationFile.exists();
    }

    private static int askIfCreateBirdview(String mapName) throws HeadlessException {
        Object[] options = {"Yes", "No", "Other Map", "Cancel"};

        int choice = JOptionPane.showOptionDialog(Global.window.frame,
                "No birdview exists for map " + mapName + ". Would you like to create birdview now?",
                "No Birdview",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);
        return choice;
    }

    public static String getMapName() {
        SelectMapDialog selectMapDialog = new SelectMapDialog(Global.window.frame);
        String mapName = selectMapDialog.create();
        return mapName;
    }
    
    /**
     * Load map inforamation from the file.
     */
    private static void loadMapInformation() {

        MapBoundaries mapBoundaries = null;
        
        try {
            FileInputStream fileInputStream = new FileInputStream(Global.getMapName() + ".information");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            mapBoundaries = (MapBoundaries) objectInputStream.readObject();
            objectInputStream.close();
            fileInputStream.close();
        } catch (IOException exception) {
        } catch (ClassNotFoundException exception) {
        }

        MapInformation.mapBoundaries = mapBoundaries;
    }

    private static void refreshMap() {
        if (Global.window != null && Global.window.mapPanel != null && Global.window.mapPanel.map != null) {
            Global.window.mapPanel.map.refreshImage();
        }

        loadMapInformation();
    }
}
