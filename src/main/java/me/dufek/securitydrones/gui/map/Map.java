package me.dufek.securitydrones.gui.map;

import cz.cuni.amis.pogamut.base3d.worldview.object.Location;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import me.dufek.securitydrones.activeobject.ActiveObject;
import me.dufek.securitydrones.area.Area;
import me.dufek.securitydrones.area.Areas;
import me.dufek.securitydrones.conversion.BirdviewMapConversion;
import me.dufek.securitydrones.conversion.NumberConversion;
import me.dufek.securitydrones.heatmap.HeatCell;
import me.dufek.securitydrones.heatmap.HeatGrid;
import me.dufek.securitydrones.gui.Preferences;
import me.dufek.securitydrones.main.Global;
import me.dufek.securitydrones.uav.destination.Destination;
import me.dufek.securitydrones.uav.destination.Destinations;
import me.dufek.securitydrones.uav.UAV;
import me.dufek.securitydrones.uav.request.UAVRequest;
import me.dufek.securitydrones.uav.request.UAVRequests;
import me.dufek.securitydrones.uav.UAVs;
import me.dufek.securitydrones.chargingstation.ChargingStation;
import me.dufek.securitydrones.chargingstation.ChargingStations;
import me.dufek.securitydrones.preferences.UserPreferences;
import me.dufek.securitydrones.uav.visiblearea.VisibleArea;

/**
 * Bird view of the Unreal Tournament 2004 map.
 *
 * @author Jan Dufek
 */
public class Map extends JPanel {

    /**
     * Bird view image.
     */
    public BufferedImage mapImage;
    /**
     * Scale of the map.
     */
    public double scale;
    /**
     * Graphics for drawing the map.
     */
    public Graphics2D graphics;

    public double minimalScale;

    public Map() {
//        this.mapImage = mapImage;
        // Set default scale
        scale = UserPreferences.preferences.MAP_DEFAULT_SCALE;
        setBackground(Color.black);
    }

    private void checkMinimumScale() {
        if (mapImage == null) {
            return;
        }

        int windowWidth = this.getWidth();
        int windowHeight = this.getHeight();
        double imageWidth = mapImage.getWidth();
        double imageHeight = mapImage.getHeight();

        minimalScale = Math.max(windowWidth / imageWidth, windowHeight / imageHeight);

        double correctedScale = Math.max(minimalScale, scale);

        scale = correctedScale;
    }

    /**
     * Paints the map bird view component.
     *
     * @param graphics
     */
    @Override
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        this.graphics = (Graphics2D) graphics;

        checkMinimumScale();

        centerComponent();
        scaleComponent();

        if (Global.areaSelectionMode) {
            drawConstructedArea();
        }

        drawAreas();

        drawAlgorithm();

        drawChargingStations();
        drawDestinations();
        drawUAVRequests();
        drawUAVsVisibleArea();
        drawUAVs();

        this.graphics.dispose();
        this.graphics = null;

        graphics.dispose();
        graphics = null;

        System.gc();
    }

    public void drawAlgorithm() {
        if (Global.algorithm != null) {
            Global.algorithm.paint(this.graphics, mapImage, scale);
        }
    }

    public void drawUAVs() {
        for (UAV uav : UAVs.listOfUAVs) {

            drawUAV(uav);
        }
    }

    public void drawUAV(UAV uav) {
        Location UAVMapLocation = uav.getActualLocationMapCoordinates();
        Location UAVRealLocation = uav.getRealLocationMapCoordinates();
        if (graphics != null && UAVMapLocation != null) {
            if (ActiveObject.get() == uav) {
                graphics.setColor(Preferences.UAV_COLOR_SELECTED);
            } else if (uav.isCrashed()) {
                graphics.setColor(Preferences.UAV_COLOR_CRASHED);
            } else if (uav.isRising()) {
                graphics.setColor(Preferences.UAV_COLOR_TAKEOFF);
            } else if (uav.isLanded()) {
                graphics.setColor(Preferences.UAV_COLOR_LANDED);
            } else {
                graphics.setColor(Preferences.UAV_COLOR);
            }

            Location birdviewLocation = BirdviewMapConversion.mapToBirdview(UAVMapLocation, mapImage, scale);
            Location birdviewRealLocation = BirdviewMapConversion.mapToBirdview(UAVRealLocation, mapImage, scale);

            // Register uav visual object
            Ellipse2D uavVisualObject = new Ellipse2D.Double(NumberConversion.toInteger(birdviewLocation.x) - Preferences.UAV_DIAMETER / 2, NumberConversion.toInteger(birdviewLocation.y) - Preferences.UAV_DIAMETER / 2, Preferences.UAV_DIAMETER, Preferences.UAV_DIAMETER);
            uav.setVisualObject(uavVisualObject);

            graphics.fill(uavVisualObject);

            graphics.drawOval(NumberConversion.toInteger(birdviewRealLocation.x) - Preferences.UAV_DIAMETER / 2, NumberConversion.toInteger(birdviewRealLocation.y) - Preferences.UAV_DIAMETER / 2, Preferences.UAV_DIAMETER, Preferences.UAV_DIAMETER);
            graphics.drawString(uav.getName() + " (" + uav.getBatteryPercentage() + "%)", Math.min(NumberConversion.toInteger(birdviewLocation.x) - Preferences.UAV_DIAMETER / 2, NumberConversion.toInteger(birdviewRealLocation.x) - Preferences.UAV_DIAMETER / 2), Math.min(NumberConversion.toInteger(birdviewLocation.y) - Preferences.UAV_DIAMETER / 2, NumberConversion.toInteger(birdviewRealLocation.y) - Preferences.UAV_DIAMETER / 2) - 7);
        }
    }

    public void drawUAVRequests() {
        for (UAVRequest uavRequest : UAVRequests.getList()) {

            drawUAVRequest(uavRequest);
        }
    }

    public void drawUAVRequest(UAVRequest uavRequest) {
        Location UAVRequestMapLocation = uavRequest.getStartLocation();
        if (graphics != null && UAVRequestMapLocation != null) {

            if (ActiveObject.get() == uavRequest) {
                graphics.setColor(Preferences.UAV_REQUEST_COLOR_SELECTED);
            } else {
                graphics.setColor(Preferences.UAV_REQUEST_COLOR);
            }

            Location birdviewLocation = BirdviewMapConversion.mapToBirdview(UAVRequestMapLocation, mapImage, scale);

            // Register uav visual object
            Ellipse2D uavRequestVisualObject = new Ellipse2D.Double(NumberConversion.toInteger(birdviewLocation.x) - Preferences.UAV_REQUEST_DIAMETER / 2, NumberConversion.toInteger(birdviewLocation.y) - Preferences.UAV_REQUEST_DIAMETER / 2, Preferences.UAV_REQUEST_DIAMETER, Preferences.UAV_REQUEST_DIAMETER);
            uavRequest.setVisualObject(uavRequestVisualObject);

            graphics.fill(uavRequestVisualObject);
            graphics.drawString(uavRequest.getName(), NumberConversion.toInteger(birdviewLocation.x) - Preferences.UAV_REQUEST_DIAMETER / 2, NumberConversion.toInteger(birdviewLocation.y) - Preferences.UAV_REQUEST_DIAMETER / 2 - 7);
        }
    }

    private void drawConstructedArea() {
        if (Areas.constructedArea != null) {
            Area birdviewConstructedArea = BirdviewMapConversion.mapToBirdview(Areas.constructedArea, mapImage, scale);

//            Location mapLastPointLocation = new Location(Areas.constructedArea.lastPoint.getX(), Areas.constructedArea.lastPoint.getY());
            Location birdviewLastPointLocation = BirdviewMapConversion.mapToBirdview(Areas.constructedArea.lastPoint, mapImage, scale);

            graphics.setColor(Preferences.CONSTRUCTED_AREA_COLOR);
            setAlpha(Preferences.CONSTRUCTED_AREA_ALPHA);
            graphics.fill(birdviewConstructedArea);
            graphics.setColor(Preferences.CONSTRUCTED_AREA_PERIMETER_COLOR);
            setAlpha(Preferences.CONSTRUCTED_AREA_PERIMETER_ALPHA);
            graphics.draw(birdviewConstructedArea);
//            if (birdviewLastPoint != null) {
            if (birdviewLastPointLocation != null) {
                graphics.setColor(Preferences.CONSTRUCTED_AREA_END_POINT_COLOR);
                setAlpha(Preferences.CONSTRUCTED_AREA_END_POINT_ALPHA);
//                graphics.fillRect(birdviewLastPoint.x - 5, birdviewLastPoint.y - 5, 10, 10);
                graphics.fillRect((NumberConversion.toInteger(birdviewLastPointLocation.getX())) - Preferences.CONSTRUCTED_AREA_END_POINT_DIAMETER / 2, (NumberConversion.toInteger(birdviewLastPointLocation.getY())) - Preferences.CONSTRUCTED_AREA_END_POINT_DIAMETER / 2, Preferences.CONSTRUCTED_AREA_END_POINT_DIAMETER, Preferences.CONSTRUCTED_AREA_END_POINT_DIAMETER);
            }
        }
    }

    private void setAlpha(float alpha) {
        int type = AlphaComposite.SRC_OVER;
        AlphaComposite composite = AlphaComposite.getInstance(type, alpha);
        graphics.setComposite(composite);
    }

    public void drawAreas() {

        for (Area area : Areas.listOfAreas) {
            drawArea(area);
        }
    }

    public void drawArea(Area area) {
        Area birdviewArea = BirdviewMapConversion.mapToBirdview(area, mapImage, scale);

        drawGrid(area.getGrid());

        if (ActiveObject.get() == area) {
            graphics.setColor(Preferences.AREA_PERIMETER_COLOR_SELECTED);
        } else {
            graphics.setColor(Preferences.AREA_PERIMETER_COLOR);
        }

        setAlpha(Preferences.AREA_PERIMETER_ALPHA);
        graphics.draw(birdviewArea);
    }

    public void drawGrid(HeatGrid grid) {
        if (grid == null) {
            return;
        }

        setAlpha(0.5F);

        for (HeatCell cell : grid.getCells()) {
            Location birdviewLocation = BirdviewMapConversion.mapToBirdview(cell.getLocation(), mapImage, scale);
            Location birdviewDimensions = BirdviewMapConversion.mapToBirdviewLength(new Location(UserPreferences.preferences.CELL_HEIGHT, UserPreferences.preferences.CELL_WIDTH, 0), mapImage, scale);

            graphics.setColor(cell.getHeatColor());
            graphics.fillRect(NumberConversion.toInteger(birdviewLocation.getX()), NumberConversion.toInteger(birdviewLocation.getY()), NumberConversion.toInteger(birdviewDimensions.getX()), NumberConversion.toInteger(birdviewDimensions.getY()));
        }
    }

    public void drawDestinations() {
        synchronized (Destinations.getList()) {
            for (Destination destination : Destinations.getList()) {
                if (ActiveObject.get() instanceof UAV && ActiveObject.get() == destination.getUAV()) {
                    drawDestination(destination);
                }
            }
        }
    }

    public void drawDestination(Destination destination) {
        Location destinationMapLocation = destination.getLocation();
        if (graphics != null && destinationMapLocation != null) {
            Location destinationBirdviewLocation = BirdviewMapConversion.mapToBirdview(destinationMapLocation, mapImage, scale);

            graphics.setColor(Preferences.UAV_DESTINATION_COLOR);
            graphics.fillRect(NumberConversion.toInteger(destinationBirdviewLocation.x) - Preferences.UAV_DESTINATION_DIAMETER / 2, NumberConversion.toInteger(destinationBirdviewLocation.y) - Preferences.UAV_DESTINATION_DIAMETER / 2, Preferences.UAV_DESTINATION_DIAMETER, Preferences.UAV_DESTINATION_DIAMETER);
//            graphics.drawString(destination.getUAV().getName() + " Destination", NumberConversion.toInteger(destinationBirdviewLocation.x) - Preferences.UAV_DESTINATION_DIAMETER / 2, NumberConversion.toInteger(destinationBirdviewLocation.y) - Preferences.UAV_DESTINATION_DIAMETER / 2 - 7);
        }
    }

    public void drawChargingStations() {
        for (ChargingStation chargingStation : ChargingStations.listOfChargingStations) {

            drawChargingStation(chargingStation);
        }
    }

    public void drawChargingStation(ChargingStation chargingStation) {
        Location chargingStationMapLocation = chargingStation.getLocation();
        if (graphics != null && chargingStationMapLocation != null) {
            Location birdviewLocation = BirdviewMapConversion.mapToBirdview(chargingStationMapLocation, mapImage, scale);

            if (ActiveObject.get() == chargingStation) {
                graphics.setColor(Preferences.CHARGING_STATION_AREA_COLOR_SELECTED);
            } else if (chargingStation.isOccupied()) {
                graphics.setColor(Preferences.CHARGING_STATION_AREA_COLOR_OCCUPIED);
            } else {
                graphics.setColor(Preferences.CHARGING_STATION_AREA_COLOR);
            }

            setAlpha(Preferences.CHARGING_STATION_AREA_ALPHA);

            // Register charging station visual object
            Ellipse2D chargingStationVisualObject = new Ellipse2D.Double(NumberConversion.toInteger(birdviewLocation.x) - Preferences.CHARGING_STATION_DIAMETER / 2, NumberConversion.toInteger(birdviewLocation.y) - Preferences.CHARGING_STATION_DIAMETER / 2, Preferences.CHARGING_STATION_DIAMETER, Preferences.CHARGING_STATION_DIAMETER);
            chargingStation.setVisualObject(chargingStationVisualObject);
            graphics.fill(chargingStationVisualObject);

            if (ActiveObject.get() == chargingStation) {
                graphics.setColor(Preferences.CHARGING_STATION_PERIMETER_COLOR_SELECTED);
            } else if (chargingStation.isOccupied()) {
                graphics.setColor(Preferences.CHARGING_STATION_PERIMETER_COLOR_OCCUPIED);
            } else {
                graphics.setColor(Preferences.CHARGING_STATION_PERIMETER_COLOR);
            }

            setAlpha(Preferences.CHARGING_STATION_PERIMETER_ALPHA);

            graphics.drawOval(NumberConversion.toInteger(birdviewLocation.x) - Preferences.CHARGING_STATION_DIAMETER / 2, NumberConversion.toInteger(birdviewLocation.y) - Preferences.CHARGING_STATION_DIAMETER / 2, Preferences.CHARGING_STATION_DIAMETER, Preferences.CHARGING_STATION_DIAMETER);
            graphics.drawString(chargingStation.getName(), NumberConversion.toInteger(birdviewLocation.x) - Preferences.CHARGING_STATION_DIAMETER / 2, NumberConversion.toInteger(birdviewLocation.y) - Preferences.CHARGING_STATION_DIAMETER / 2 - 7);
        }
    }

    public void drawUAVsVisibleArea() {
        for (UAV uav : UAVs.listOfUAVs) {

            drawUAVVisibleArea(uav);
        }
    }

    public void drawUAVVisibleArea(UAV uav) {
        VisibleArea visibleAreaMapCoordinates = uav.getVisibleArea();

        if (graphics != null && visibleAreaMapCoordinates != null) {
            VisibleArea visibleAreaBirdviewCoordinates = BirdviewMapConversion.mapToBirdview(visibleAreaMapCoordinates, mapImage, scale);

            if (ActiveObject.get() == uav) {
                graphics.setColor(Preferences.UAV_VISIBLE_AREA_COLOR_SELECTED);
            } else if (uav.isCrashed()) {
                graphics.setColor(Preferences.UAV_VISIBLE_AREA_COLOR_CRASHED);
            } else if (uav.isRising()) {
                graphics.setColor(Preferences.UAV_VISIBLE_AREA_COLOR_TAKEOFF);
            } else if (uav.isLanded()) {
                graphics.setColor(Preferences.UAV_VISIBLE_AREA_COLOR_LANDED);
            } else {
                graphics.setColor(Preferences.UAV_VISIBLE_AREA_COLOR);
            }

            graphics.draw(visibleAreaBirdviewCoordinates);
        }
    }

    public void refresh() {
        revalidate();
        repaint();
    }

    /**
     * Gets preferred size of the map bird view.
     *
     * @return
     */
    @Override
    public Dimension getPreferredSize() {
        if (mapImage == null) {
            return new Dimension(0, 0);
        }

        int width = NumberConversion.toInteger(scale * mapImage.getWidth());
        int height = NumberConversion.toInteger(scale * mapImage.getHeight());
        return new Dimension(width, height);
    }

    /**
     * Sets map bird view scale.
     *
     * @param scale
     */
    public void setScale(double scale) {
        this.scale = scale;

        revalidate();
        repaint();
    }

    /**
     * Gets map bird view scale.
     *
     * @return
     */
    public double getScale() {
        return scale;
    }

    /**
     * Sets map bird view image.
     *
     * @param mapImage
     */
    public void setImage(BufferedImage mapImage) {
        this.mapImage = mapImage;
    }

    /**
     * Gets map bird view image.
     *
     * @return
     */
    public BufferedImage getImage() {
        return mapImage;
    }

    /**
     * Centers map bird in the component.
     *
     * @return
     */
    private void centerComponent() {
        if (mapImage == null) {
            return;
        }

        int windowWidth = this.getWidth();
        int windowHeight = this.getHeight();
        double imageWidth = mapImage.getWidth() * scale;
        double imageHeight = mapImage.getHeight() * scale;
        double shiftX = (windowWidth - imageWidth) / 2;
        double shiftY = (windowHeight - imageHeight) / 2;

        graphics.translate(NumberConversion.toInteger(shiftX), NumberConversion.toInteger(shiftY));
    }

    /**
     * Scales map bird.
     *
     * @return
     */
    private void scaleComponent() {
        AffineTransform affineTransform = new AffineTransform();
        affineTransform.scale(scale, scale);
        this.graphics.drawRenderedImage(mapImage, affineTransform);
    }

    public void refreshImage() {
        this.mapImage = loadMapImage();
        refresh();
    }

    /**
     * Loads bird view image from the file.
     */
    private BufferedImage loadMapImage() {
        BufferedImage mapImage = null;
        File mapFile = new File(Global.getMapName() + ".jpg");

        if (mapFile.exists()) {
            try {
                mapImage = ImageIO.read(mapFile);
            } catch (IOException exeption) {
                System.err.println("Error loading the map image " + Global.getMapName() + ".jpg" + ".");
                exeption.printStackTrace();
            }
        }

        return mapImage;
    }

    public void printComponent() {

        PrinterJob pj = PrinterJob.getPrinterJob();
        pj.setJobName("Print");

        pj.setPrintable(new Printable() {
            @Override
            public int print(Graphics grphcs, PageFormat pf, int i) throws PrinterException {
                if (i > 0) {
                    return Printable.NO_SUCH_PAGE;
                }

                // Get the preferred size ofthe component...
                Dimension compSize = Global.window.mapPanel.map.getPreferredSize();
                // Make sure we size to the preferred size
                Global.window.mapPanel.map.setSize(compSize);
                // Get the the print size
                Dimension printSize = new Dimension();
                printSize.setSize(pf.getImageableWidth(), pf.getImageableHeight());

                // Calculate the scale factor
                double scaleFactor = getScaleFactorToFit(compSize, printSize);
                // Don't want to scale up, only want to scale down
                if (scaleFactor > 1d) {
                    scaleFactor = 1d;
                }

                // Calcaulte the scaled size...
                double scaleWidth = compSize.width * scaleFactor;
                double scaleHeight = compSize.height * scaleFactor;

                // Create a clone of the graphics context.  This allows us to manipulate
                // the graphics context without begin worried about what effects
                // it might have once we're finished
                Graphics2D g2 = (Graphics2D) grphcs;
                // Calculate the x/y position of the component, this will center
                // the result on the page if it can
                double x = ((pf.getImageableWidth() - scaleWidth) / 2d) + pf.getImageableX();
                double y = ((pf.getImageableHeight() - scaleHeight) / 2d) + pf.getImageableY();
                // Create a new AffineTransformation
                AffineTransform at = new AffineTransform();
                // Translate the offset to out "center" of page
                at.translate(x, y);
                // Set the scaling
                at.scale(scaleFactor, scaleFactor);
                // Apply the transformation
                g2.transform(at);
                // Print the component
                Global.window.mapPanel.map.printAll(g2);

                Global.window.mapPanel.map.revalidate();

                // Dispose of the graphics context, freeing up memory and discarding our changes
                g2.dispose();

                return Printable.PAGE_EXISTS;
            }
        });

        if (pj.printDialog() == false) {
            return;
        }

        try {
            pj.print();
        } catch (PrinterException ex) {
            // handle exception
        }
    }

    public double getScaleFactorToFit(Dimension original, Dimension toFit) {

        double dScale = 1d;

        if (original != null && toFit != null) {

            double dScaleWidth = getScaleFactor(original.width, toFit.width);
            double dScaleHeight = getScaleFactor(original.height, toFit.height);

            dScale = Math.min(dScaleHeight, dScaleWidth);

        }

        return dScale;

    }

    public double getScaleFactor(int iMasterSize, int iTargetSize) {

        double dScale = 1;
        if (iMasterSize > iTargetSize) {

            dScale = (double) iTargetSize / (double) iMasterSize;

        } else {

            dScale = (double) iTargetSize / (double) iMasterSize;

        }

        return dScale;

    }

}
