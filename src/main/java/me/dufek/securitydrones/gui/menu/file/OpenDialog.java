package me.dufek.securitydrones.gui.menu.file;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import me.dufek.securitydrones.changes.Changes;
import me.dufek.securitydrones.gui.birdview.MapBoundaries;
import me.dufek.securitydrones.gui.openedfile.OpenedFile;
import me.dufek.securitydrones.main.Global;

/**
 *
 * @author Jan Dufek
 */
public class OpenDialog implements ActionListener {

    private final JFrame frame;

    public String fileName;
    public String directory;

    private JFileChooser fileChooser;

    public OpenDialog(JFrame frame) {
        this.frame = frame;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        fileChooser = new JFileChooser();

        int returnValue = fileChooser.showOpenDialog(frame);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            open();
        }

    }

    private void open() {
        fileName = fileChooser.getSelectedFile().getName();
        directory = fileChooser.getCurrentDirectory().toString();

        ApplicationState savedFile = null;

        try {
            FileInputStream fileInputStream = new FileInputStream(directory + System.getProperty("file.separator") + fileName);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            savedFile = (ApplicationState) objectInputStream.readObject();
            objectInputStream.close();
            fileInputStream.close();
        } catch (Exception exception) {
            JOptionPane.showMessageDialog(frame,
                    "Can not load given file. Make sure you are loading the right file which was previously stored by Security Drones application.",
                    "Loading file error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        OpenedFile.openedFile = fileName;
        OpenedFile.openedDirectory = directory;

        Global.window.setFileTitle(fileName);

        savedFile.restore();
        
        Changes.reset();
        
        OpenedFile.setSaved();
    }
}
