package me.dufek.securitydrones.changes;

import java.util.ArrayList;
import me.dufek.securitydrones.main.Global;
import me.dufek.securitydrones.objectmanager.ObjectManager;

/**
 * Data structure for holding all changes made with objects. It is used by the
 * GUI's undo and redo operations.
 *
 * @author Jan Dufek
 */
public class Changes {

    /**
     * List of changes.
     */
    private static ArrayList<Change> changes = new ArrayList<Change>();

    /**
     * Index of actual change. We can move around changes data structure.
     */
    private static int actualChangeIndex = -1;

    public static void reportAddition(Object object) {
        changes.add(actualChangeIndex + 1, new Change(object, Action.ADD));

        if (changes.size() > actualChangeIndex + 1 + 1) {
            ArrayList<Change> changesToRemove = new ArrayList<Change>();

            for (int i = actualChangeIndex + 1 + 1; i < Changes.changes.size(); i++) {
                changesToRemove.add(changes.get(i));
            }

            changes.removeAll(changesToRemove);
        }

        actualChangeIndex++;

        Global.window.menu.edit.undoMenuItem.setEnabled(true);
        Global.window.menu.edit.redoMenuItem.setEnabled(false);
    }

    public static void reportDeletion(Object object) {
        changes.add(actualChangeIndex + 1, new Change(object, Action.DELETE));

        if (changes.size() > actualChangeIndex + 1 + 1) {
            ArrayList<Change> changesToRemove = new ArrayList<Change>();

            for (int i = actualChangeIndex + 1 + 1; i < Changes.changes.size(); i++) {
                changesToRemove.add(changes.get(i));
            }

            changes.removeAll(changesToRemove);
        }

        actualChangeIndex++;

        Global.window.menu.edit.undoMenuItem.setEnabled(true);
        Global.window.menu.edit.redoMenuItem.setEnabled(false);
    }

    public static void undo() {
        Change change = changes.get(actualChangeIndex);

        switch (change.getAction()) {
            case ADD:
                ObjectManager.DeleteObjectForUndo(change.getObject());
                break;
            case DELETE:
                ObjectManager.AddObjectForUndo(change.getObject());
                break;
            default:
                throw new IllegalStateException("Unknown action.");
        }

        actualChangeIndex--;

        if (actualChangeIndex == -1) {
            Global.window.menu.edit.undoMenuItem.setEnabled(false);
        }

        Global.window.menu.edit.redoMenuItem.setEnabled(true);

        for (Change change1 : changes) {
            System.out.println(change1.toString());
        }
    }

    public static void redo() {
        Change change = changes.get(actualChangeIndex + 1);

        switch (change.getAction()) {
            case ADD:
                ObjectManager.AddObjectForUndo(change.getObject());
                break;
            case DELETE:
                ObjectManager.DeleteObjectForUndo(change.getObject());
                break;
            default:
                throw new IllegalStateException("Unknown action.");
        }

        actualChangeIndex++;

        if (actualChangeIndex + 1 >= changes.size()) {
            Global.window.menu.edit.redoMenuItem.setEnabled(false);
        }

        Global.window.menu.edit.undoMenuItem.setEnabled(true);
    }

    public static void reset() {
        changes.clear();
        actualChangeIndex = -1;
        Global.window.menu.edit.undoMenuItem.setEnabled(false);
        Global.window.menu.edit.redoMenuItem.setEnabled(false);
    }
}
