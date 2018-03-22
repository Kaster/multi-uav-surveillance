package me.dufek.securitydrones.gui;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import me.dufek.securitydrones.conversion.NumberConversion;
import me.dufek.securitydrones.gui.birdview.check.BirdviewChecker;
import me.dufek.securitydrones.gui.bottombar.BottomBar;
import me.dufek.securitydrones.gui.map.MapPane;
import me.dufek.securitydrones.gui.menu.MenuBar;
import me.dufek.securitydrones.gui.openedfile.OpenedFile;
import me.dufek.securitydrones.gui.rightbar.RightBar;
import me.dufek.securitydrones.main.Global;

/**
 * Application main window. All program features can be controlled from here.
 *
 * @author Jan Dufek
 */
public class Window implements Runnable {

    /**
     * Window. It contains all other components.
     */
    public JFrame frame;
    public MenuBar menu;
    public MapPane mapPanel;
    public RightBar rightBar;
    public BottomBar bottomBar;

    @Override
    public void run() {
        this.createAndShowGUI();
    }

    /**
     * Creates and show MainWindow. Show all necessary elements. User can
     * operate program from here.
     */
    public void createAndShowGUI() {

        // Do not use default look and feel
        JFrame.setDefaultLookAndFeelDecorated(false);

        // Make window and set caption
        frame = new JFrame();//Preferences.WINDOW_TITLE + " - " + "New File");
        
        OpenedFile.setSaved();
        setFileTitle(OpenedFile.openedFile);

        // Set default close operation
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                Global.saveExit();
            }
        });

        // Set application icon
        frame.setIconImage(Toolkit.getDefaultToolkit().getImage(Preferences.ICON_IMAGE_PATH));

        // Set window size
        setWindowSize();

        // Set window location
        setWindowLocation();

        // Maximalize window
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        // Get content pane
        Container pane = frame.getContentPane();

        // Set up layout
        pane.setLayout(new GridBagLayout());

        // Check birdview
        checkBirdview();

        // Create menu
        menu = new MenuBar(frame);
        menu.createMenu();

        // Create map
        mapPanel = new MapPane(pane, frame);
        mapPanel.createMapPane();

        // Create right bar
        rightBar = new RightBar(pane);
        rightBar.createRightBar();

        // Create botoom bar
        bottomBar = new BottomBar(pane);

        // Show frame
        frame.setVisible(true);
    }

    private void checkBirdview() throws HeadlessException {
        int birdviewCheckerReturnValue = BirdviewChecker.check(Global.getMapName());

        switch (birdviewCheckerReturnValue) {
            case -1:
                Global.saveExit();
            case 1: // No
//                Global.saveExit();
                break;
            case 3:
                Global.saveExit();
            default:
                break;
        }
    }

    /**
     * Sets window size according to preferences. Window size is set in
     * preferences as percentage of screen dimensions.
     */
    private void setWindowSize() throws HeadlessException {

        // Get dimension of the screen
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        // Height
        int screenHeight = screenSize.height;
        double windowHeight = screenHeight * Preferences.WINDOW_HEIGHT_AS_MULTIPLE_OF_SCREEN_HEIGHT;

        // Width
        int screenWidth = screenSize.width;
        double windowWidth = screenWidth * Preferences.WINDOW_WIDTH_AS_MULTIPLE_OF_SCREEN_WIDTH;

        frame.setSize(NumberConversion.toInteger(windowWidth), NumberConversion.toInteger(windowHeight));
    }

    /**
     * Sets window location to the middle of the screen.
     */
    private void setWindowLocation() {
        frame.setLocationRelativeTo(null);
    }

//    public void createNoUAVRequestWarning() {
//        JOptionPane.showMessageDialog(frame,
//                "There are no UAVs to simulate. At first add at least one UAV using right mouse click on the desired location and selecting Add UAV from the context menu.",
//                "No UAVs Error",
//                JOptionPane.ERROR_MESSAGE);
//    }

    public void setFileTitle(String fileTitle) {
        String saved;

        if (OpenedFile.isSaved()) {
            saved = "";
        } else {
            saved = "*";
        }

        if (fileTitle != null) {
            frame.setTitle(Preferences.WINDOW_TITLE + " - " + fileTitle + saved);
        } else {
            frame.setTitle(Preferences.WINDOW_TITLE + " - " + "New File" + saved);
        }
    }
}
