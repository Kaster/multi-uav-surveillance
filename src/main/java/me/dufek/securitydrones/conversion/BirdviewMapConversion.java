package me.dufek.securitydrones.conversion;

import cz.cuni.amis.pogamut.base3d.worldview.object.Location;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import me.dufek.securitydrones.area.Area;
import me.dufek.securitydrones.gui.map.MapInformation;
import me.dufek.securitydrones.uav.visiblearea.VisibleArea;

/**
 * This class is responsible for converting between bird view and map
 * coordinates.
 *
 * @author Jan Dufek
 */
public class BirdviewMapConversion {

    public static Location birdviewToMap(Location birdviewLocation, BufferedImage birdview, double scale) {
        double xMinUSARSimUnits = MapInformation.mapBoundaries.x.min / 250;
        double xMaxUSARSimUnits = (MapInformation.mapBoundaries.x.min + MapInformation.mapBoundaries.numberOfRowsOfFragments * MapInformation.mapBoundaries.fragmentHeight) / 250;
        double yMinUSARSimUnits = MapInformation.mapBoundaries.y.min / 250;
        double yMaxUSARSimUnits = (MapInformation.mapBoundaries.y.min + MapInformation.mapBoundaries.numberOfColumnsOfFragments * MapInformation.mapBoundaries.fragmentWidth) / 250;

        double x = Scale.minMaxScale(birdviewLocation.y, birdview.getHeight() * scale, 0, xMinUSARSimUnits, xMaxUSARSimUnits);
        double y = Scale.minMaxScale(birdviewLocation.x, 0, birdview.getWidth() * scale, yMinUSARSimUnits, yMaxUSARSimUnits);

        double z = 0;

        return new Location(x, y, z);
    }

//    public static Area birdviewToMap(Area birdviewArea, BufferedImage birdview, double scale) {
//        double[] mapXPoints = new double[birdviewArea.npoints];
//        double[] mapYPoints = new double[birdviewArea.npoints];
//
//        // For each point of polygon
//        for (int i = 0; i < birdviewArea.npoints; i++) {
//            Location birdviewPointLocation = new Location(birdviewArea.xpoints[i], birdviewArea.ypoints[i], 0);
//            Location mapPointLocation = birdviewToMap(birdviewPointLocation, birdview, scale);
//            mapXPoints[i] = mapPointLocation.x;
//            mapYPoints[i] = mapPointLocation.y;
//        }
//
//        Area mapArea = new Area(mapXPoints, mapYPoints, birdviewArea.npoints);
//
//        return mapArea;
//    }
    public static VisibleArea birdviewToMap(VisibleArea birdviewVisibleArea, BufferedImage birdview, double scale) {
        double[] mapXPoints = new double[birdviewVisibleArea.npoints];
        double[] mapYPoints = new double[birdviewVisibleArea.npoints];

        // For each point of polygon
        for (int i = 0; i < birdviewVisibleArea.npoints; i++) {
            Location birdviewPointLocation = new Location(birdviewVisibleArea.xpoints[i], birdviewVisibleArea.ypoints[i], 0);
            Location mapPointLocation = birdviewToMap(birdviewPointLocation, birdview, scale);
            mapXPoints[i] = mapPointLocation.x;
            mapYPoints[i] = mapPointLocation.y;
        }

        VisibleArea mapVisibleArea = new VisibleArea(mapXPoints, mapYPoints);

        return mapVisibleArea;
    }

    public static Location mapToBirdview(Location mapLocation, BufferedImage birdview, double scale) {
        double xMinUSARSimUnits = MapInformation.mapBoundaries.x.min / 250;
        double xMaxUSARSimUnits = (MapInformation.mapBoundaries.x.min + MapInformation.mapBoundaries.numberOfRowsOfFragments * MapInformation.mapBoundaries.fragmentHeight) / 250;
        double yMinUSARSimUnits = MapInformation.mapBoundaries.y.min / 250;
        double yMaxUSARSimUnits = (MapInformation.mapBoundaries.y.min + MapInformation.mapBoundaries.numberOfColumnsOfFragments * MapInformation.mapBoundaries.fragmentWidth) / 250;

        double x = Scale.minMaxScale(mapLocation.y, yMinUSARSimUnits, yMaxUSARSimUnits, 0, birdview.getWidth() * scale);
        double y = Scale.minMaxScale(mapLocation.x, xMinUSARSimUnits, xMaxUSARSimUnits, birdview.getHeight() * scale, 0);

        double z = 0;

        return new Location(x, y, z);
    }

    public static Area mapToBirdview(Area mapArea, BufferedImage birdview, double scale) {
        double[] birdviewXPoints = new double[mapArea.npoints];
        double[] birdviewYPoints = new double[mapArea.npoints];

        // For each point of polygon
        for (int i = 0; i < mapArea.npoints; i++) {
            Location mapPointLocation = new Location(mapArea.xpoints[i], mapArea.ypoints[i], 0);
            Location birdviewPointLocation = mapToBirdview(mapPointLocation, birdview, scale);
            birdviewXPoints[i] = birdviewPointLocation.x;
            birdviewYPoints[i] = birdviewPointLocation.y;
        }

        Area birdviewArea = new Area(birdviewXPoints, birdviewYPoints, mapArea.npoints);

        return birdviewArea;
    }

    public static VisibleArea mapToBirdview(VisibleArea visibleArea, BufferedImage birdview, double scale) {
        double[] birdviewXPoints = new double[visibleArea.npoints];
        double[] birdviewYPoints = new double[visibleArea.npoints];

        // For each point of polygon
        for (int i = 0; i < visibleArea.npoints; i++) {
            Location mapPointLocation = new Location(visibleArea.xpoints[i], visibleArea.ypoints[i], 0);
            Location birdviewPointLocation = mapToBirdview(mapPointLocation, birdview, scale);
            birdviewXPoints[i] = birdviewPointLocation.x;
            birdviewYPoints[i] = birdviewPointLocation.y;
        }

        VisibleArea birdviewVisibleArea = new VisibleArea(birdviewXPoints, birdviewYPoints);

        return birdviewVisibleArea;
    }

    public static Rectangle2D.Double mapToBirdview(Rectangle2D.Double mapRectangle, BufferedImage birdview, double scale) {
        Location pointMapLocation = new Location(mapRectangle.getX(), mapRectangle.getY(), 0);
        Location pointBirdviewLocation = mapToBirdview(pointMapLocation, birdview, scale);

        Location dimensionsMapLocation = new Location(mapRectangle.getWidth(), mapRectangle.getHeight(), 0);
        Location dimensionsBirdviewLocation = mapToBirdviewLength(dimensionsMapLocation, birdview, scale);

        Rectangle2D.Double birdviewRectangle = new Rectangle2D.Double(pointBirdviewLocation.getX(), pointBirdviewLocation.getY(), dimensionsBirdviewLocation.getX(), dimensionsBirdviewLocation.getY());

        return birdviewRectangle;
    }

    public static Location mapToBirdviewLength(Location lengthLocation, BufferedImage birdview, double scale) {
        Location zeroLocation = new Location(0, 0, 0);

        Location birdviewLengthLocation = mapToBirdview(lengthLocation, birdview, scale);
        Location birdviewZeroLocation = mapToBirdview(zeroLocation, birdview, scale);

        double x = Math.abs(birdviewLengthLocation.getX() - birdviewZeroLocation.getX());
        double y = Math.abs(birdviewLengthLocation.getY() - birdviewZeroLocation.getY());

        Location birdviewLength = new Location(x, y, 0);

        return birdviewLength;
    }
}
