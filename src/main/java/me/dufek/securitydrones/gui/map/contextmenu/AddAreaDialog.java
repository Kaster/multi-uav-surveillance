package me.dufek.securitydrones.gui.map.contextmenu;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import me.dufek.securitydrones.area.Area;
import me.dufek.securitydrones.area.AreaType;
import me.dufek.securitydrones.area.Areas;
import me.dufek.securitydrones.gui.openedfile.OpenedFile;
import me.dufek.securitydrones.main.Global;
import me.dufek.securitydrones.uav.request.UAVRequest;

/**
 *
 * @author Jan Dufek
 */
public class AddAreaDialog {

    private JFrame frame;

    public AddAreaDialog(JFrame frame) {
        this.frame = frame;
    }

    public boolean create(Area area) {
        JPanel panel = new JPanel(new BorderLayout(5, 5));

        JPanel labels = new JPanel(new GridLayout(0, 1, 2, 2));
        labels.add(new JLabel("Name", SwingConstants.RIGHT));
        labels.add(new JLabel("Type", SwingConstants.RIGHT));
        panel.add(labels, BorderLayout.WEST);

        JPanel fields = new JPanel(new GridLayout(0, 1, 2, 2));
        JTextField areaName = new JTextField(Areas.getNextDefaultName());
        areaName.addAncestorListener(new RequestFocusListener(false));
        fields.add(areaName);
        JComboBox areaType = new JComboBox(AreaType.values());
        fields.add(areaType);
        panel.add(fields, BorderLayout.CENTER);

        String[] buttons = {"Add", "Cancel"};
        int c = JOptionPane.showOptionDialog(
                frame,
                panel,
                "Add Area",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                buttons,
                buttons[0]
        );

        if (c == 0) {
            area.setName(areaName.getText());
            area.setType((AreaType) areaType.getSelectedItem());
            
            OpenedFile.setUnsaved();
            
            return true;
        } else {
            return false;
        }
    }
}
