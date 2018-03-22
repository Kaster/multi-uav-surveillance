package me.dufek.securitydrones.gui.menu.server;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import me.dufek.securitydrones.gui.birdview.check.BirdviewChecker;
import me.dufek.securitydrones.gui.menu.Menu;
import me.dufek.securitydrones.main.Global;
import me.dufek.securitydrones.preferences.UserPreferences;

/**
 *
 * @author Jan Dufek
 */
public class Server implements Menu {

    private final JMenuBar menuBar;
    private final JFrame frame;

    private JMenu serverMenu;

    public JMenuItem startMenuItem;
    public JMenuItem stopMenuItem;

    public Server(JFrame frame, JMenuBar menuBar) {
        this.frame = frame;
        this.menuBar = menuBar;
    }

    @Override
    public void create() {
        serverMenu = new JMenu("Server");

        createStartMenuItem();
        createStopMenuItem();

        menuBar.add(serverMenu);
    }

    private void createStartMenuItem() {
        startMenuItem = new JMenuItem("Start");
        startMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startServerRequest();
            }
        }
        );
        serverMenu.add(startMenuItem);
    }

    public void startServerRequest() throws IllegalStateException {
        String mapName = BirdviewChecker.getMapName();

        if (mapName == null) {
            return;
        }

        int birdviewCheckerReturnValue = BirdviewChecker.check(mapName);

        switch (birdviewCheckerReturnValue) {
            case -1:
                return;
            case 0:
                break;
            case 1:
                break;
            case 3:
                return;
            case 4:
                break;
            default:
                throw new IllegalStateException("Unknown return value from birdview checker.");
        }
        startServer();
    }

    private void startServer() {
        if (Global.simulation != null && Global.simulation.isRunning()) {
            throw new IllegalStateException("Can not start server because simulation is running.");
        }

        try {
            Process serverProcess = new ProcessBuilder(UserPreferences.preferences.UNREAL_TOURNAMENT_2004_HOME_FOLDER + "System\\UT2004.exe", Global.getMapName() + "?game=USARBot.USARDeathMatch?TimeLimit=0?GameStats=False?NumBots=0?spectatoronly=1?AdminName=admin?AdminPassword=admin", "-ini=USARSim.ini", "-log=USARSim.log").start();
            me.dufek.securitydrones.server.Server.setLocal(serverProcess, Global.getMapName());
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }

        setOptionsForServerRunningMode();
    }

    private void setOptionsForServerRunningMode() {
        startMenuItem.setEnabled(false);
        stopMenuItem.setEnabled(true);

        Global.window.menu.map.changeMenuItem.setEnabled(false);
        Global.window.menu.map.refreshMenuItem.setEnabled(false);
    }

    private void createStopMenuItem() {
        stopMenuItem = new JMenuItem("Stop");
        stopMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stopServer();
            }
        }
        );
        serverMenu.add(stopMenuItem);

        stopMenuItem.setEnabled(false);
    }

    private void stopServer() {
        if (Global.simulation != null) {
            Global.window.menu.simulation.stopSimulation();
        }

        me.dufek.securitydrones.server.Server.terminate();

        setOptionsForServerStoppedMode();
    }

//    private void createConnectMenuItem() {
//        JMenuItem menuItem = new JMenuItem("Connect");
//        menuItem.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
////                String hostAddress = (String) JOptionPane.showInputDialog(frame, "Host address:", "Connect to a Server", JOptionPane.PLAIN_MESSAGE, null, null, null);
//                ConnectDialog connectDialog = new ConnectDialog(frame);
//                boolean returnValue = connectDialog.create();
//                
//                if (returnValue) {
//                    connectToRemoteServer();
//                }
//            }
//        });
//        serverMenu.add(menuItem);
//    }
//    private void createRefreshMapMenuItem() {
//        JMenuItem menuItem = new JMenuItem("Refresh birdview");
//        menuItem.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
////                Birdview birdview = new Birdview();
////                birdview.start();
//
//            }
//        });
//        serverMenu.add(menuItem);
//    }
    private void setOptionsForServerStoppedMode() {
        startMenuItem.setEnabled(true);
        stopMenuItem.setEnabled(false);

        Global.window.menu.map.changeMenuItem.setEnabled(true);
        Global.window.menu.map.refreshMenuItem.setEnabled(true);
    }
}
