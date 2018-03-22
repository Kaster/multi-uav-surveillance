package me.dufek.securitydrones.gui.menu.file;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import me.dufek.securitydrones.gui.openedfile.OpenedFile;
import me.dufek.securitydrones.main.Global;

/**
 *
 * @author Jan Dufek
 */
public class SaveAsDialog implements ActionListener {

    private final JFrame frame;

    public String fileName;
    public String directory;

    private JFileChooser fileChooser;

    public SaveAsDialog(JFrame frame) {
        this.frame = frame;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        fileChooser = new JFileChooser();

        if (Global.simulation != null && Global.simulation.isRunning()) {
            JOptionPane.showMessageDialog(frame,
                    "Can not save during simulation. Please stop simulation and try again.",
                    "Can not save error",
                    JOptionPane.ERROR_MESSAGE);

            return;
        } else {
            int returnValue = fileChooser.showSaveDialog(frame);

            if (returnValue == JFileChooser.APPROVE_OPTION) {
                save();
            }
        }
    }

    private void save() {
        fileName = fileChooser.getSelectedFile().getName();
        directory = fileChooser.getCurrentDirectory().toString();

        OpenedFile.openedFile = fileName;
        OpenedFile.openedDirectory = directory;

        Global.window.setFileTitle(fileName);

        ApplicationState applicationState = new ApplicationState();
        applicationState.snapshot();

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(directory + System.getProperty("file.separator") + fileName);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(applicationState);
            objectOutputStream.close();
            fileOutputStream.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        
        OpenedFile.setSaved();
    }
}
