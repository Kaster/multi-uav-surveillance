package me.dufek.securitydrones.gui.menu.birdview;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import me.dufek.securitydrones.gui.birdview.check.BirdviewChecker;
import me.dufek.securitydrones.gui.menu.Menu;
import me.dufek.securitydrones.main.Global;

/**
 *
 * @author Jan Dufek
 */
public class Map implements Menu {

    private final JMenuBar menuBar;
    private final JFrame frame;

    private JMenu mapMenu;

    public JMenuItem changeMenuItem;
    public JMenuItem refreshMenuItem;

    public Map(JFrame frame, JMenuBar menuBar) {
        this.frame = frame;
        this.menuBar = menuBar;
    }

    @Override
    public void create() {
        mapMenu = new JMenu("Map");

        createChangeMenuItem();
        createRefreshMenuItem();

        menuBar.add(mapMenu);
    }

    private void createChangeMenuItem() {
        changeMenuItem = new JMenuItem("Change");
        changeMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                change();
            }
        });
        mapMenu.add(changeMenuItem);
    }

    private void change() {
        String mapName = BirdviewChecker.getMapName();
        
        if (mapName == null) {
            return;
        }
        BirdviewChecker.check(mapName);
    }

    private void createRefreshMenuItem() {
        refreshMenuItem = new JMenuItem("Refresh");
        refreshMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refresh();
            }
        });
        mapMenu.add(refreshMenuItem);
    }

    private void refresh() {
        me.dufek.securitydrones.gui.birdview.Birdview birdview = new me.dufek.securitydrones.gui.birdview.Birdview(Global.getMapName());
        birdview.start();
    }
}
