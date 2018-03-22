package me.dufek.securitydrones.area;

import cz.cuni.amis.pogamut.base3d.worldview.object.Location;
import java.util.ArrayList;
import me.dufek.securitydrones.algorithm.grid.Cell;
import me.dufek.securitydrones.algorithm.grid.SubCell;
import me.dufek.securitydrones.changes.Changes;
import me.dufek.securitydrones.geometry.Bounds;
import me.dufek.securitydrones.main.Global;

/**
 * Data structure to keep all the areas.
 *
 * @author Jan Dufek
 */
public class Areas {

    /**
     * List of areas.
     */
    public static ArrayList<Area> listOfAreas = new ArrayList<Area>();

    /**
     * The area which is being constructed using the GUI.
     */
    public static Area constructedArea;

    /**
     * Saves constructed area from the GUI.
     */
    public static void saveConstructedArea() {
        constructedArea.createGrid();
        addArea(constructedArea);
        Changes.reportAddition(constructedArea);
        constructedArea = null;
    }

    public static void cancelConstructedArea() {
        constructedArea = null;
    }

    public static void addArea(Area area) {
        listOfAreas.add(area);

        if (Global.algorithm != null) {
            Global.algorithm.reportAreaAddition(area);
        }
    }

    public static void removeArea(Area area) {
        listOfAreas.remove(area);

        if (Global.algorithm != null) {
            Global.algorithm.reportAreaDeletion(area);
        }
    }

//    public static void removeArea(int indexOfArea) {
//        listOfAreas.remove(indexOfArea);
//    }
    public static int getNumberOfAreas() {
        return listOfAreas.size();
    }

    public static Area getArea(int index) {
        return listOfAreas.get(index);
    }

    public static void printListOfAreas() {
        for (Area area : listOfAreas) {
            System.out.println(area.toString());
        }
    }

    public static int getAreaIndex(Area area) {
        return listOfAreas.indexOf(area);
    }

    /**
     * Gets available default name for the area.
     *
     * @return Default name.
     */
    public static String getNextDefaultName() {
        for (int i = 0; i <= getNumberOfAreas(); i++) {
            String name = "Area " + i;
            if (isNameFree(name)) {
                return name;
            }
        }
        throw new IllegalStateException("No name is free.");
    }

    public static boolean isNameFree(String name) {
        for (Area area : listOfAreas) {
            if (name.equals(area.getName())) {
                return false;
            }
        }

        return true;
    }

    public static void clear() {
        listOfAreas.clear();
    }

    public static boolean noAreas() {
        return listOfAreas.isEmpty();
    }

    /**
     * Returns first entered area to guard.
     *
     * @return
     */
    public static Area getGuardedArea() {
        for (Area area : listOfAreas) {
            if (area.getType() == AreaType.GUARDED) {
                return area;
            }
        }

        return null;
    }

    /**
     * Heat up the heat grid for each area.
     */
    public static void updateHeatMap() {
        for (Area area : listOfAreas) {
            area.updateHeatMap();
        }
    }

    /**
     * Get bounds of the area. Bounds are minimum respectively maximum values on
     * aces.
     *
     * @return Bounds.
     */
    public static Bounds getBounds() {
        double minX = Double.MAX_VALUE;
        double maxX = -Double.MAX_VALUE;
        double minY = Double.MAX_VALUE;
        double maxY = -Double.MAX_VALUE;

        for (Area area : listOfAreas) {
            for (int i = 0; i < area.npoints; i++) {
                if (minX > area.xpoints[i]) {
                    minX = area.xpoints[i];
                }
                if (maxX < area.xpoints[i]) {
                    maxX = area.xpoints[i];
                }
                if (minY > area.ypoints[i]) {
                    minY = area.ypoints[i];
                }
                if (maxY < area.ypoints[i]) {
                    maxY = area.ypoints[i];
                }
            }
        }

        return new Bounds(minX, maxX, minY, maxY);
    }

    public static boolean intersect(Cell cell) {
        for (Area area : listOfAreas) {
            if (area.intersect(cell)) {
                return true;
            }
        }

        return false;
    }

    public static boolean intersect(SubCell subCell) {
        for (Area area : listOfAreas) {
            if (area.intersect(subCell)) {
                return true;
            }
        }

        return false;
    }

    public static boolean contains(Area area) {
        return Areas.listOfAreas.contains(area);
    }

    /**
     * Gets nearest area to given location.
     *
     * @param location Location.
     * @return Area.
     */
    public static Area getNearestArea(Location location) {
        double minimumDistance = Double.MAX_VALUE;
        Area nearestArea = null;

        for (Area area : listOfAreas) {
            double distance = area.getDistance(location);

            if (distance < minimumDistance) {
                minimumDistance = distance;
                nearestArea = area;
            }
        }

        if (nearestArea == null) {
            throw new IllegalStateException("Nearest area not found.");
        }

        return nearestArea;
    }

    /**
     * Gets the nearest area to the location which is not listed in the given
     * list of occupied areas.
     *
     * @param location Location.
     * @param occupiedAreas List of occupied areas.
     * @return
     */
    public static Area getNearestFreeArea(Location location, ArrayList<Area> occupiedAreas) {
        double minimumDistance = Double.MAX_VALUE;
        Area nearestArea = null;

        for (Area area : listOfAreas) {
            if (occupiedAreas.contains(area)) {
                continue;
            }

            double distance = area.getDistance(location);

            if (distance < minimumDistance) {
                minimumDistance = distance;
                nearestArea = area;
            }
        }

        return nearestArea;
    }

    /**
     * Get random area weighted by its size.
     * 
     * @param areas List of areas.
     * @return Random area.
     */
    public static Area selectArea(ArrayList<Area> areas) {

        double totalWeight = 0;

        for (Area area : areas) {
            totalWeight += area.getArea();
        }

        int index = -1;

        double random = Math.random() * totalWeight;

        for (int i = 0; i < areas.size(); ++i) {
            random -= areas.get(i).getArea();

            if (random <= 0) {
                index = i;
                break;
            }
        }

        return areas.get(index);
    }
}
