package me.dufek.securitydrones.gui.openedfile;

import me.dufek.securitydrones.main.Global;

/**
 *
 * @author Jan Dufek
 */
public class OpenedFile {

    public static String openedFile;
    public static String openedDirectory;

    private static boolean saved = false;

    public static void setSaved() {
        saved = true;
        Global.window.setFileTitle(openedFile);
    }

    public static void setUnsaved() {
        saved = false;
        Global.window.setFileTitle(openedFile);
    }

    public static boolean isSaved() {
        return saved;
    }
}
