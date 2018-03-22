package me.dufek.securitydrones.uav.destination;

import me.dufek.securitydrones.uav.destination.Destination;
import cz.cuni.amis.pogamut.base3d.worldview.object.Location;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import me.dufek.securitydrones.conversion.NumberConversion;
import me.dufek.securitydrones.uav.UAV;

/**
 * Data structure intended to hold all the destinations.
 *
 * @author Jan Dufek
 */
public class Destinations {

    /**
     * List of destinations.
     */
    private static final List<Destination> listOfDestinations = Collections.synchronizedList(new ArrayList<Destination>());

    public static void addDestination(Destination destination) {
        listOfDestinations.add(destination);
    }

    public static void removeDestination(Destination destination) {
        listOfDestinations.remove(destination);
    }

    /**
     * Remove specific destination given by UAV and location.
     *
     * @param uav
     * @param location
     */
    public static void removeDestination(UAV uav, Location location) {
        Destination destinationToRemove = null;

        synchronized (listOfDestinations) {
            for (Destination destination : listOfDestinations) {
                if (destination.getUAV() == uav && NumberConversion.toInteger(destination.getLocation().x) == NumberConversion.toInteger(location.x) && NumberConversion.toInteger(destination.getLocation().y) == NumberConversion.toInteger(location.y)) {
                    destinationToRemove = destination;
                    break;
                }
            }

            if (destinationToRemove == null) {
                return;
//                throw new IllegalStateException("Reached destination was not found in the register of destinations.");
            }

            removeDestination(destinationToRemove);
        }
    }

    /**
     * Remove all the destinations of particular UAV.
     *
     * @param uav UAV.
     */
    public static void removeDestination(UAV uav) {
        ArrayList<Destination> destinationsToRemove = new ArrayList<Destination>();

        synchronized (listOfDestinations) {
            for (Destination destination : listOfDestinations) {
                if (destination.getUAV() == uav) {
                    destinationsToRemove.add(destination);
                }
            }

            listOfDestinations.removeAll(destinationsToRemove);
        }
    }

    public static void removeDestination(int indexOfDestination) {
        listOfDestinations.remove(indexOfDestination);
    }

    public static int getNumberOfDestinations() {
        return listOfDestinations.size();
    }

    public static Destination getDestination(int index) {
        return listOfDestinations.get(index);
    }

    public static void printListOfDestinations() {
        for (Destination destination : listOfDestinations) {
            System.out.println(destination.toString());
        }
    }

    public static int getDestinationIndex(Destination destination) {
        return listOfDestinations.indexOf(destination);
    }

    public static String getNextDefaultName() {
        return "Destination " + getNumberOfDestinations();
    }

    public static void clear() {
        listOfDestinations.clear();
    }

    public static boolean noDestinations() {
        return listOfDestinations.isEmpty();
    }

    public static List<Destination> getList() {
        return Destinations.listOfDestinations;
    }
}
