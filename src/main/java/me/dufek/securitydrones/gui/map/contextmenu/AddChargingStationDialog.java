package me.dufek.securitydrones.gui.map.contextmenu;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import me.dufek.securitydrones.chargingstation.Preferences;
import me.dufek.securitydrones.chargingstation.ChargingStation;
import me.dufek.securitydrones.chargingstation.ChargingStations;
import me.dufek.securitydrones.chargingstation.SwapChargingStation;
import me.dufek.securitydrones.gui.openedfile.OpenedFile;
import me.dufek.securitydrones.main.Global;

/**
 *
 * @author Jan Dufek
 */
public class AddChargingStationDialog {

    private JFrame frame;

    public AddChargingStationDialog(JFrame frame) {
        this.frame = frame;
    }

    public ChargingStation create() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));

        JPanel labels = new JPanel(new GridLayout(0, 1, 2, 2));
        labels.add(new JLabel("Name", SwingConstants.RIGHT));
        labels.add(new JLabel("Number of Batteries", SwingConstants.RIGHT));
        labels.add(new JLabel("Capacity of Batteries [mAh]", SwingConstants.RIGHT));
        labels.add(new JLabel("Charging Performance [mAh]", SwingConstants.RIGHT));
        labels.add(new JLabel("Swap Time Mean [s]", SwingConstants.RIGHT));
        labels.add(new JLabel("Swap Time Standard Deviation [s]", SwingConstants.RIGHT));
        panel.add(labels, BorderLayout.WEST);

        JPanel fields = new JPanel(new GridLayout(0, 1, 2, 2));
        JTextField chargingStationName = new JTextField(ChargingStations.getNextDefaultName());
        chargingStationName.addAncestorListener(new RequestFocusListener(false));
        fields.add(chargingStationName);
        JTextField chargingStationNumberOfBatterries = new JTextField(Integer.toString(Preferences.DEFAULT_CHARGING_STATION_NUMBER_OF_BATTERIES));
        fields.add(chargingStationNumberOfBatterries);
        JTextField chargingStationCapacityOfBatterries = new JTextField(Integer.toString(Preferences.DEFAULT_CHARGING_STATION_CAPACITY_OF_BATTERIES));
        fields.add(chargingStationCapacityOfBatterries);
        JTextField chargingStationChargingPerformance = new JTextField(Double.toString(Preferences.DEFAULT_CHARGING_STATION_CHARGING_PERFORMANCE));
        fields.add(chargingStationChargingPerformance);
        JTextField chargingStationSwapTimeMean = new JTextField(Double.toString(Preferences.DEFAULT_CHARGING_STATION_SWAP_TIME_MEAN));
        fields.add(chargingStationSwapTimeMean);
        JTextField chargingStationSwapTimeStandardDeviation = new JTextField(Double.toString(Preferences.DEFAULT_CHARGING_STATION_SWAP_TIME_STANDARD_DEVIATION));
        fields.add(chargingStationSwapTimeStandardDeviation);
        panel.add(fields, BorderLayout.CENTER);

        String[] buttons = {"Add", "Cancel"};
        int c = JOptionPane.showOptionDialog(
                frame,
                panel,
                "Add Charging Station",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                buttons,
                buttons[0]
        );

        if (c == 0) {
            OpenedFile.setUnsaved();
            return new SwapChargingStation(null, chargingStationName.getText(), Integer.parseInt(chargingStationNumberOfBatterries.getText()), Double.parseDouble(chargingStationCapacityOfBatterries.getText()), Double.parseDouble(chargingStationChargingPerformance.getText()), Double.parseDouble(chargingStationSwapTimeMean.getText()), Double.parseDouble(chargingStationSwapTimeStandardDeviation.getText()));
        } else {
            return null;
        }
    }
}
