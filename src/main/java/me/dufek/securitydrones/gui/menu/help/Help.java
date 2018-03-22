package me.dufek.securitydrones.gui.menu.help;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import me.dufek.securitydrones.gui.Preferences;
import me.dufek.securitydrones.gui.menu.Menu;

/**
 *
 * @author Jan Dufek
 */
public class Help implements Menu {

    private final JFrame frame;
    private final JMenuBar menuBar;
    private JMenu helpMenu;

    private JMenuItem viewHelpMenuItem;
    private JMenuItem aboutMenuItem;

    public Help(JFrame frame, JMenuBar menuBar) {
        this.frame = frame;
        this.menuBar = menuBar;
    }

    @Override
    public void create() {
        helpMenu = new JMenu("Help");

        createViewHelpMenuItem();
        helpMenu.add(new JSeparator());
        createAboutMenuItem();

        menuBar.add(helpMenu);
    }

    private void createViewHelpMenuItem() {
        viewHelpMenuItem = new JMenuItem("View Help");
        viewHelpMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                File helpFile = new File(Preferences.HELP_FILE);
                
                try {
                    Desktop.getDesktop().open(helpFile);
                } catch (IOException ex) {
                    Logger.getLogger(Help.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        helpMenu.add(viewHelpMenuItem);

        viewHelpMenuItem.setEnabled(true);
    }

    private void createAboutMenuItem() {
        aboutMenuItem = new JMenuItem("About");
        aboutMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                about();
            }
        });
        helpMenu.add(aboutMenuItem);
    }

    private void about() {
        AboutDialog aboutDialog = new AboutDialog(frame);
        aboutDialog.create();
    }
}
