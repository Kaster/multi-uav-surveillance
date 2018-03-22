package me.dufek.securitydrones.clipboard;

/**
 * Clipboard is used to temporarily save objects during performing edit
 * operations.
 *
 * @author Jan Dufek
 */
public class Clipboard {

    private static Object clipboard;

    public static void put(Object object) {
        clipboard = object;
    }

    public static Object get() {
        return clipboard;
    }

    public static boolean isFull() {
        return clipboard != null;
    }
}
