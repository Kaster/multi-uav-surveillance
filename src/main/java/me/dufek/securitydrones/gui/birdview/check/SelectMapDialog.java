package me.dufek.securitydrones.gui.birdview.check;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import me.dufek.securitydrones.main.Global;

/**
 *
 * @author Jan Dufek
 */
public class SelectMapDialog {

    private final JFrame frame;

    public SelectMapDialog(JFrame frame) {
        this.frame = frame;
    }

    public String create() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));

        JPanel labels = new JPanel(new GridLayout(0, 1, 2, 2));
        labels.add(new JLabel("Map", SwingConstants.RIGHT));
        panel.add(labels, BorderLayout.WEST);

        JPanel fields = new JPanel(new GridLayout(0, 1, 2, 2));
        JTextField mapName = new JTextField(Global.getMapName());
        fields.add(mapName);
        panel.add(fields, BorderLayout.CENTER);

        String[] buttons = {"OK", "Cancel"};
     
        int returnValue = JOptionPane.showOptionDialog(
                frame,
                panel,
                "Select Map",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                buttons,
                buttons[0]
        );

        if (returnValue == 0) {
            return mapName.getText();
        } else {
            return null;
        }
    }
}
