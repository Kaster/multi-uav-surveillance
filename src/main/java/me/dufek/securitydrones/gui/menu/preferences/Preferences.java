package me.dufek.securitydrones.gui.menu.preferences;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import me.dufek.securitydrones.gui.menu.Menu;

/**
 *
 * @author Jan Dufek
 */
public class Preferences implements Menu {
    private final JFrame frame;
    
    private final JMenuBar menuBar;
    private JMenu preferencesMenu;
    
    private JMenuItem viewPreferencesMenuItem;

    public Preferences(JFrame frame, JMenuBar menuBar) {
        this.frame = frame;
        this.menuBar = menuBar;
    }

    @Override
    public void create() {
        preferencesMenu = new JMenu("Preferences");

        createViewPreferencesMenu();

        menuBar.add(preferencesMenu);
    }

    private void createViewPreferencesMenu() {
        viewPreferencesMenuItem = new JMenuItem("View Preferences");
        viewPreferencesMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewPreferences();
            }
        });
        preferencesMenu.add(viewPreferencesMenuItem);
    }
    
    private void viewPreferences() {        
        PreferencesDialog preferencesDialog = new PreferencesDialog(frame);
        preferencesDialog.create();
    }
}
