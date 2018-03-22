package me.dufek.securitydrones.gui.menu;

import me.dufek.securitydrones.gui.menu.edit.Edit;
import me.dufek.securitydrones.gui.menu.preferences.Preferences;
import me.dufek.securitydrones.gui.menu.help.Help;
import me.dufek.securitydrones.gui.menu.simulation.Simulation;
import me.dufek.securitydrones.gui.menu.server.Server;
import me.dufek.securitydrones.gui.menu.file.File;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import me.dufek.securitydrones.gui.menu.birdview.Map;

/**
 *
 * @author Jan Dufek
 */
public class MenuBar {

    private JFrame frame;

    public File file;
    public Edit edit;
    public Map map;
    public Server server;
    public Simulation simulation;
    public Preferences preferences;
    public Help help;

    public MenuBar(JFrame frame) {
        this.frame = frame;
    }

    /**
     * Upper menu bar. Upper menu bar gives user control over application. It
     * provide access to some program features.
     */
    public void createMenu() {
        JMenuBar menuBar = new JMenuBar();

        file = new File(frame, menuBar);
        file.create();

        edit = new Edit(menuBar);
        edit.create();
        
        map = new Map(frame, menuBar);
        map.create();

        server = new Server(frame, menuBar);
        server.create();

        simulation = new Simulation(frame, menuBar);
        simulation.create();

        preferences = new Preferences(frame, menuBar);
        preferences.create();

        help = new Help(frame, menuBar);
        help.create();

        frame.setJMenuBar(menuBar);
    }
}
