package me.dufek.securitydrones.gui.menu.file;

import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import me.dufek.securitydrones.area.Areas;
import me.dufek.securitydrones.changes.Changes;
import me.dufek.securitydrones.chargingstation.ChargingStations;
import me.dufek.securitydrones.gui.menu.Menu;
import me.dufek.securitydrones.gui.openedfile.OpenedFile;
import me.dufek.securitydrones.main.Global;
import me.dufek.securitydrones.uav.destination.Destinations;
import me.dufek.securitydrones.uav.request.UAVRequests;

/**
 *
 * @author Jan Dufek
 */
public class File implements Menu {

    private final JFrame frame;
    private JMenuBar menuBar;
    private JMenu fileMenu;
    
    public JMenuItem newMenuItem;
    public JMenuItem openMenuItem;
    public JMenuItem saveMenuItem;
    public JMenuItem saveAsMenuItem;

    public File(JFrame frame, JMenuBar menuBar) {
        this.frame = frame;
        this.menuBar = menuBar;
    }

    @Override
    public void create() {
        fileMenu = new JMenu("File");

        createNewMenuItem();
        createOpenMenuItem();
        createSaveMenuItem();
        createSaveAsMenuItem();
        fileMenu.add(new JSeparator());
        createPrintMenuItem();
        fileMenu.add(new JSeparator());
        createExitMenuItem();

        menuBar.add(fileMenu);
    }

    private void createNewMenuItem() {
        newMenuItem = new JMenuItem("New");
        newMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UAVRequests.clear();
                Destinations.clear();
                Areas.clear();
                ChargingStations.clear();

                OpenedFile.openedFile = null;
                OpenedFile.openedDirectory = null;

                Global.window.setFileTitle(OpenedFile.openedFile);

                OpenedFile.setSaved();
                
                Changes.reset();

                Global.window.mapPanel.map.refresh();
            }
        });
        fileMenu.add(newMenuItem);
    }

    private void createOpenMenuItem() {
        openMenuItem = new JMenuItem("Open");
        openMenuItem.addActionListener(new OpenDialog(frame));
        fileMenu.add(openMenuItem);
    }

    private void createSaveMenuItem() {
        saveMenuItem = new JMenuItem("Save");
        saveMenuItem.addActionListener(new SaveDialog(frame));
        fileMenu.add(saveMenuItem);
    }

    private void createSaveAsMenuItem() {
        saveAsMenuItem = new JMenuItem("Save As");
        saveAsMenuItem.addActionListener(new SaveAsDialog(frame));
        fileMenu.add(saveAsMenuItem);
    }

    private void createPrintMenuItem() {
        JMenuItem menuItem = new JMenuItem("Print");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Global.window.mapPanel.map.printComponent();
            }
        });
        fileMenu.add(menuItem);
    }

    private void createExitMenuItem() {
        JMenuItem menuItem = new JMenuItem("Exit");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Global.saveExit();
            }
        });
        fileMenu.add(menuItem);
    }
}
