package me.dufek.securitydrones.gui.birdview;

import cz.cuni.amis.pogamut.base.agent.impl.AgentId;
import cz.cuni.amis.pogamut.base.agent.state.impl.AgentStateFailed;
import cz.cuni.amis.pogamut.base.communication.connection.impl.socket.SocketConnectionAddress;
import cz.cuni.amis.pogamut.base.component.exception.ComponentCantStartException;
import cz.cuni.amis.pogamut.ut2004.agent.params.UT2004AgentParameters;
import cz.cuni.amis.pogamut.ut2004.communication.messages.gbinfomessages.Player;
import cz.cuni.amis.pogamut.ut2004.factory.guice.remoteagent.UT2004ServerFactory;
import cz.cuni.amis.utils.collections.ObservableCollection;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import me.dufek.securitydrones.main.Global;
import me.dufek.securitydrones.preferences.UserPreferences;

/**
 * Manage creation of bird view from Unreal Tournament 2004 map. Runs the Unreal
 * Tournament 2004 server using the given map and uses spectator player to take
 * screen shots. Then it joins those screen shot into one bird view.
 *
 * @author Jan Dufek
 */
public class Birdview {

    // Map name
//    private final String mapName = "DroneVille";
    private final String mapName;

    // Host server
    public static final String SERVER_ADDRESS = "localhost";

    // Port
    public static final int SERVER_PORT = 3001;

    // Rotation of the spectator during taking screen shots (look down)
    public static final String OBSERVER_ROTATION = "49152,0,0";

    // Control connection to the server
    private ControlConnection controlConnection;
    // Boundaries of the give map
    private MapBoundaries mapBoundaries;
    // Flight level in absolut coordinates
    private double flightLevel = 0;
    // Width of one fragment of bird view
    private double fragmentWidth = 0;
    // Height of one fragment of bird view
    private double fragmentHeight = 0;
    // Process in which Unreal TOurnament 2004 is running
    private Process birdviewServerProcess;
    // Spectator which is taking screen shots
    private Player spectator;
    // List of taken images
    private ArrayList<File> images;
    // Number of columns of fragments of bird view
    private int numberOfColumnsOfFragments = 0;
    // Number of rows of fragments of bird view
    private int numberOfRowsOfFragments = 0;

    public Birdview(String mapName) {
        this.mapName = mapName;
    }

    /**
     * Main class which is controlling all the process of creating bird view.
     */
    public void start() {
        startPogamutDeathmatchServer();
        createControlConnection();
        String spectatorId = getSpectatorId();
        mapBoundaries = new MapBoundaries(controlConnection);
        getFlightLevel();
        getSectionSize();
        checkScreenShotsDirectory();
        setUpForShooting(spectatorId);
        startShooting(spectatorId);
        endServer();
        concatenatePictures();
        deletePictures();
        saveMapInformationToFile();
        refreshMap();
    }

    /**
     * Starts up Pogamut death match server.
     */
    private void startPogamutDeathmatchServer() {
        try {
            birdviewServerProcess = new ProcessBuilder(UserPreferences.preferences.UNREAL_TOURNAMENT_2004_HOME_FOLDER + "System\\UT2004.exe", mapName + "?game=GameBots2004.BotDeathMatch?TimeLimit=0?GameStats=False?NumBots=0?spectatoronly=1?AdminName=admin?AdminPassword=admin", "-log=BirdviewServer.log").start();
        } catch (IOException ex) {
            Logger.getLogger(Birdview.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Creates one control connection to the Pogamut server.
     */
    private void createControlConnection() {
        // Creating agent parameters - setting module name and connection adress
        UT2004AgentParameters controlConnectionParameters = new UT2004AgentParameters();
        controlConnectionParameters.setAgentId(new AgentId("ControlConnection"));
        controlConnectionParameters.setWorldAddress(new SocketConnectionAddress(SERVER_ADDRESS, SERVER_PORT));

        // Creating module that tells guice it should instantiate OUR (this) class
        ControlConnectionModule controlConnectionModule = new ControlConnectionModule();

        // Creating pogamut factory
        UT2004ServerFactory serverFactory = new UT2004ServerFactory(controlConnectionModule);

        // Creating control connection
        controlConnection = (ControlConnection) serverFactory.newAgent(controlConnectionParameters);

        // Starting the connection - connecting to the server
        boolean tryToConnect = true;

        // Try connectiong to the server till it is available unless there is some other error
        while (tryToConnect) {
            tryToConnect = false;

            try {
                controlConnection.start();
            } catch (ComponentCantStartException exception) {
                if (controlConnection.getState().getFlag().isState(AgentStateFailed.class)) {
                    tryToConnect = true;
                } else {
                    throw exception;
                }
            }
        }

        // Initializing the connection
        controlConnection.initialize();
    }

    /**
     * Gets Unreal ID of the spectator responsible for taking screen shots.
     */
    private String getSpectatorId() {

        // All players on the server
        ObservableCollection<Player> players = controlConnection.getPlayers();
        String spectatorId = null;

        // Get ID of the first human player found
        for (Iterator<Player> iterator = players.iterator(); iterator.hasNext();) {
            Player player = iterator.next();
            String playerId = player.getId().getStringId();

            if (playerId.contains("Player")) {
                spectatorId = playerId;
                spectator = player;
                break;
            }
        }

        if (spectatorId != null) {
            return spectatorId;
        } else {
            throw new AssertionError("No spectator human player found on the GameBots2004 server for taking birdview.", null);
        }
    }

    /**
     * Gets flight level in absolute coordinates from relative flight level
     * above ground. Ground level is taken as average elevation of all
     * navigation point in the map.
     */
    private void getFlightLevel() {
        flightLevel = mapBoundaries.averageElevation + UserPreferences.preferences.FLIGHT_HEIGHT;
    }

    /**
     * Gets size of one fragment of the bird view.
     */
    private void getSectionSize() {
        fragmentWidth = Math.tan(Math.toRadians(UserPreferences.preferences.UNREAL_TOURNAMENT_2004_HORIZONTAL_FIELD_OF_VIEW / 2)) * UserPreferences.preferences.FLIGHT_HEIGHT * 2;
        fragmentHeight = (UserPreferences.preferences.UNREAL_TOURNAMENT_2004_VERTICAL_RESOLUTION / UserPreferences.preferences.UNREAL_TOURNAMENT_2004_HORIZONTAL_RESOLUTION) * fragmentWidth;
    }

    /**
     * Checks if screen shot directory is empty.
     */
    private void checkScreenShotsDirectory() {
        images = listImages();
        if (!images.isEmpty()) {
            throw new AssertionError("ScreenShots folder must be empty in order to create a birdview.");
        }
    }

    /**
     * Lists all images in the screen shot folder.
     */
    private ArrayList<File> listImages() {
        ArrayList<File> files = new ArrayList<File>();
        String fileName;
        File folder = new File(UserPreferences.preferences.UNREAL_TOURNAMENT_2004_SCREEN_SHOTS_FOLDER);

        // Folder does not exist. So list is empty.
        if (!folder.exists()) {
            return files;
        }

        File[] listOfFiles = folder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                File file = listOfFiles[i];
                fileName = file.getName();

                if (fileName.endsWith(".bmp") || fileName.endsWith(".BMP")) {
                    files.add(file);
                }
            }
        }

        return files;
    }

    /**
     * Prepare spectator for shooting by setting up proper screen mode. It will
     * turn off all additional data showed on the screen for the player.
     */
    private void setUpForShooting(String spectatorId) {
        controlConnection.getAct().act(new MyCommandMessage("CONSOLE {Id " + spectatorId + "} {Command TOGGLESCREENSHOTMODE}"));
    }

    /**
     * Takes screen shot from bird view of the whole map.
     */
    private void startShooting(String spectatorId) {
        double currentSpectatorX = mapBoundaries.x.min + fragmentHeight / 2;
        double currentSpectatorY = mapBoundaries.y.min + fragmentWidth / 2;

        // Shift on the y axis
        while (currentSpectatorY <= mapBoundaries.y.max + fragmentWidth / 2) {
            numberOfColumnsOfFragments++;

            // Shift on the x axis
            while (currentSpectatorX <= mapBoundaries.x.max + fragmentHeight / 2) {
                if (numberOfColumnsOfFragments == 1) {
                    numberOfRowsOfFragments++;
                }

                // Determine new location
                String newLocation = currentSpectatorX + "," + currentSpectatorY + "," + flightLevel;

                // Send respawn command to respawn on the new location
                respawn(spectatorId, newLocation, OBSERVER_ROTATION);

                double actualX = Math.floor(spectator.getLocation().x);
                double requiredX = Math.floor(currentSpectatorX);

                double actualY = Math.floor(spectator.getLocation().y);
                double requiredY = Math.floor(currentSpectatorY);

                // Wait for player to be respawned
                while ((requiredX != actualX) || (requiredY != actualY)) {
                    actualX = Math.floor(spectator.getLocation().x);
                    actualY = Math.floor(spectator.getLocation().y);
                }

                // Take and save the picture
                takeAPicture(spectatorId);

                // Shift on the x axis
                currentSpectatorX += fragmentHeight;
            }

            // Shift on the y axis
            currentSpectatorY += fragmentWidth;

            // Get back to the beginning on the x axis
            currentSpectatorX = mapBoundaries.x.min + fragmentHeight / 2;
        }

        // Wait for creation of all screen shot files
        images = listImages();
        while (images.size() != numberOfColumnsOfFragments * numberOfRowsOfFragments) {
            images = listImages();
        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(Birdview.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Wait for saving all image data
        while (images.get((numberOfColumnsOfFragments * numberOfRowsOfFragments) - 1).length() == 0) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException ex) {
                Logger.getLogger(Birdview.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Respawn player to the new coordinated..
     */
    private void respawn(String spectatorId, String location, String rotation) {
        controlConnection.getAct().act(new MyCommandMessage("RESPAWN {Id " + spectatorId + "} {StartLocation " + location + "} {StartRotation " + rotation + "}"));
    }

    /**
     * Takes a screen shot.
     */
    private void takeAPicture(String spectatorId) {
        controlConnection.getAct().act(new MyCommandMessage("CONSOLE {Id " + spectatorId + "} {Command SHOT}"));
    }

    /**
     * Terminates Pogamut server.
     */
    private void endServer() {
        birdviewServerProcess.destroy();
    }

    /**
     * Join screen shots to create a bird view.
     */
    private void concatenatePictures() {
        int numberOfFrangments = numberOfRowsOfFragments * numberOfColumnsOfFragments;
        int fragmentResolutionWidth;
        int fragmentResolutionHeight;
        int type;

        // Get array of images
        File[] imagesArray = new File[images.size()];
        images.toArray(imagesArray);
        if (imagesArray.length != numberOfFrangments) {
            throw new AssertionError("Different number of BMP images in ScreenShots and given number of fragments.", null);
        }

        // Create buffered image array from image files
        BufferedImage[] bufferedImages = new BufferedImage[numberOfFrangments];
        for (int i = 0; i < numberOfFrangments; i++) {
            try {
                bufferedImages[i] = ImageIO.read(imagesArray[i]);
            } catch (IOException ex) {
                Logger.getLogger(Birdview.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        type = bufferedImages[0].getType();
        fragmentResolutionWidth = bufferedImages[0].getWidth();
        fragmentResolutionHeight = bufferedImages[0].getHeight();

        // Initialize final image
        BufferedImage finalImage = new BufferedImage(fragmentResolutionWidth * numberOfColumnsOfFragments, fragmentResolutionHeight * numberOfRowsOfFragments, type);

        // Drawing final image
        int counter = 0;
        for (int j = 0; j < numberOfColumnsOfFragments; j++) {
            for (int i = 0; i < numberOfRowsOfFragments; i++) {
                finalImage.createGraphics().drawImage(bufferedImages[counter], fragmentResolutionWidth * j, fragmentResolutionHeight * (numberOfRowsOfFragments - 1 - i), null);
                counter++;
            }
        }
        try {
            ImageIO.write(finalImage, "jpeg", new File(mapName + ".jpg"));
        } catch (IOException ex) {
            Logger.getLogger(Birdview.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Delete all taken screen shots from the screen shot directory.
     */
    private void deletePictures() {
        for (Iterator<File> iterator = images.iterator(); iterator.hasNext();) {
            File file = iterator.next();
            file.delete();
        }
    }

    /**
     * Prints size of one fragment.
     */
    private void printFragmentSize() {
        System.out.println(fragmentWidth + " x " + fragmentHeight);
    }

    /**
     * Prints map boundaries. Boundaries are maximum and minimum coordinates of
     * the map.
     */
    private void printMapBoundaries() {
        System.out.println("X: " + mapBoundaries.x.min + " - " + mapBoundaries.x.max);
        System.out.println("Y: " + mapBoundaries.y.min + " - " + mapBoundaries.y.max);
        System.out.println("Z: " + mapBoundaries.z.min + " - " + mapBoundaries.z.max);
        System.out.println("Average elevation: " + mapBoundaries.averageElevation);
    }

    private void saveMapInformationToFile() {
        mapBoundaries.fragmentHeight = this.fragmentHeight;
        mapBoundaries.fragmentWidth = this.fragmentWidth;
        mapBoundaries.numberOfColumnsOfFragments = this.numberOfColumnsOfFragments;
        mapBoundaries.numberOfRowsOfFragments = this.numberOfRowsOfFragments;

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(mapName + ".information");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(mapBoundaries);
            objectOutputStream.close();
            fileOutputStream.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    private void refreshMap() {
        if (Global.window != null && Global.window.mapPanel != null && Global.window.mapPanel.map != null) {
            Global.window.mapPanel.map.refreshImage();
        }
    }
}
