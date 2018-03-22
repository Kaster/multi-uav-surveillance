package me.dufek.securitydrones.server;

/**
 * This class is used to keep reference to the server's process.
 *
 * @author Jan Dufek
 */
public class Server {

    /**
     * The process in which the server is running.
     */
    private static Process server;

    /**
     * The map used by the server.
     */
    private static String serverMap;

    /**
     * Set up local server.
     *
     * @param server Server process.
     * @param map Sever map.
     */
    public static void setLocal(Process server, String map) {
        Server.server = server;
        Server.serverMap = map;
    }

    /**
     * Get server process.
     *
     * @return Server process.
     */
    public static Process get() {
        return server;
    }

    /**
     * Terminate the server.
     */
    public static void terminate() {
        server.destroy();
        server = null;
        serverMap = null;
    }

    /**
     * Checks if the server is running on particular map.
     *
     * @param map Map.
     * @return True if it is running on particular map and false otherwise.
     */
    public static boolean isRunning(String map) {
        return (map.equals(serverMap) && server != null);
    }

    /**
     * Checks if the server is running.
     *
     * @return True if it is running and false otherwise.
     */
    public static boolean isRunning() {
        return server != null;
    }
}
