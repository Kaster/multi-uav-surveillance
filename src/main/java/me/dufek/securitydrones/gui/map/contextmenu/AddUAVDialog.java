package me.dufek.securitydrones.gui.map.contextmenu;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import me.dufek.securitydrones.gui.openedfile.OpenedFile;
import me.dufek.securitydrones.main.Global;
import me.dufek.securitydrones.uav.Preferences;
import me.dufek.securitydrones.uav.request.UAVRequest;
import me.dufek.securitydrones.uav.request.UAVRequests;

/**
 *
 * @author Jan Dufek
 */
public class AddUAVDialog {

    private final JFrame frame;

    public AddUAVDialog(JFrame frame) {
        this.frame = frame;
    }

    public UAVRequest create() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));

        JPanel labels = new JPanel(new GridLayout(0, 1, 2, 2));
        labels.add(new JLabel("Name", SwingConstants.RIGHT));
        labels.add(new JLabel("Battery Capacity [mAh]", SwingConstants.RIGHT));
        labels.add(new JLabel("Amperage Mean [A]", SwingConstants.RIGHT));
        labels.add(new JLabel("Linear Velocity [% of maximum]", SwingConstants.RIGHT));
        labels.add(new JLabel("Altitude Velocity [% of maximum]", SwingConstants.RIGHT));
        labels.add(new JLabel("Rotational Velocity [rad/s]", SwingConstants.RIGHT));
        labels.add(new JLabel("Detector Horizontal FOV [degrees]", SwingConstants.RIGHT));
        labels.add(new JLabel("Detector Vertical FOV [degrees]", SwingConstants.RIGHT));
        panel.add(labels, BorderLayout.WEST);

        JPanel fields = new JPanel(new GridLayout(0, 1, 2, 2));
        JTextField uavName = new JTextField(UAVRequests.getNextDefaultName(), 15);
        uavName.addAncestorListener(new RequestFocusListener(false));
        fields.add(uavName);
        JTextField uavBatteryCapacity = new JTextField(Double.toString(Preferences.DEFAULT_BATTERY_CAPACITY));
        fields.add(uavBatteryCapacity);
        JTextField uavAmperageMean = new JTextField(Double.toString(Preferences.DEFAULT_AMPERAGE_MEAN));
        fields.add(uavAmperageMean);
        JTextField uavLinearVelocity = new JTextField(Double.toString(Preferences.DEFAULT_LINEAR_VELOCITY));
        fields.add(uavLinearVelocity);
        JTextField uavAltitudeVelocity = new JTextField(Double.toString(Preferences.DEFAULT_ALTITUDE_VELOCITY));
        fields.add(uavAltitudeVelocity);
        JTextField uavRotationalVelocity = new JTextField(Double.toString(Preferences.DEFAULT_ROTATIONAL_VELOCITY));
        fields.add(uavRotationalVelocity);
        JTextField uavPrimarySensorHorizontalFieldOfView = new JTextField(Double.toString(Preferences.DEFAULT_PRIMARY_SENSOR_HORIZONTAL_FIELD_OF_VIEW));
        fields.add(uavPrimarySensorHorizontalFieldOfView);
        JTextField uavPrimarySensorVerticalFieldOfView = new JTextField(Double.toString(Preferences.DEFAULT_PRIMARY_SENSOR_VERTICAL_FIELD_OF_VIEW));
        fields.add(uavPrimarySensorVerticalFieldOfView);
        panel.add(fields, BorderLayout.CENTER);

        String[] buttons = {"Add", "Cancel"};
        int c = JOptionPane.showOptionDialog(
                frame,
                panel,
                "Add UAV",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                buttons,
                buttons[0]
        );

        if (c == 0) {
            OpenedFile.setUnsaved();
            return new UAVRequest(uavName.getText(), Double.parseDouble(uavBatteryCapacity.getText()), Double.parseDouble(uavAmperageMean.getText()), Double.parseDouble(uavLinearVelocity.getText()) / 100, Double.parseDouble(uavAltitudeVelocity.getText()) / 100, Double.parseDouble(uavRotationalVelocity.getText()), Double.parseDouble(uavPrimarySensorHorizontalFieldOfView.getText()), Double.parseDouble(uavPrimarySensorVerticalFieldOfView.getText()), null);
        } else {
            return null;
        }
    }
}
