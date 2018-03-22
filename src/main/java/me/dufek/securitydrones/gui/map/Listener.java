package me.dufek.securitydrones.gui.map;

import cz.cuni.amis.pogamut.base3d.worldview.object.Location;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import me.dufek.securitydrones.activeobject.ActiveObject;
import me.dufek.securitydrones.area.Area;
import me.dufek.securitydrones.area.Areas;
import me.dufek.securitydrones.conversion.BirdviewMapConversion;
import me.dufek.securitydrones.conversion.NumberConversion;
import me.dufek.securitydrones.gui.Preferences;
import me.dufek.securitydrones.gui.bottombar.BottomBarPainter;
import me.dufek.securitydrones.gui.map.contextmenu.AddAreaDialog;
import me.dufek.securitydrones.gui.map.contextmenu.ContextMenu;
import me.dufek.securitydrones.main.Global;
import me.dufek.securitydrones.chargingstation.ChargingStation;
import me.dufek.securitydrones.chargingstation.ChargingStations;
import me.dufek.securitydrones.preferences.UserPreferences;
import me.dufek.securitydrones.uav.UAV;
import me.dufek.securitydrones.uav.UAVs;
import me.dufek.securitydrones.uav.request.UAVRequest;
import me.dufek.securitydrones.uav.request.UAVRequests;

/**
 * Listeners for operations wit map bird view.
 *
 * @author Jan Dufek
 */
class Listener extends MouseAdapter implements HierarchyListener, ActionListener {

    private JFrame frame;
    /**
     * Map bird view.
     */
    private final Map map;
    /**
     * Scrolling timer.
     */
    private final Timer scroller;
    /**
     * Start point of the action.
     */
    private Point startPoint = new Point();
    /**
     * Difference from start point to the new position.
     */
    private Point difference = new Point();
    /**
     * Mouse cursor for default operations.
     */
    private final Cursor defaultCursor;
    /**
     * Mouse cursor for drag operation.
     */
    private final Cursor dragCursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);

    /**
     * Initialize listener class.
     *
     * @param component map component for which we want to register the
     * listeners
     */
    public Listener(Map component, JFrame frame) {
        timer = new Timer(clickInterval, this);
        this.frame = frame;
        // Set map
        this.map = component;
        // Set default curson
        this.defaultCursor = component.getCursor();
        // Screate new scroller
        this.scroller = new Timer(UserPreferences.preferences.MAP_TIMER_DELAY, new ActionListener() {
            /**
             * Scrolls map to the new position.
             */
            @Override
            public void actionPerformed(ActionEvent event) {
                Container container = map.getParent();
                if (container instanceof JViewport) {
                    JViewport viewPort = (JViewport) container;
                    Point viewPosition = viewPort.getViewPosition();
                    viewPosition.translate(difference.x, difference.y);
                    map.scrollRectToVisible(new Rectangle(viewPosition, viewPort.getSize()));
                    cooldown();
                }
            }

            /**
             * Cools down inertia of the scrolling movement.
             */
            private void cooldown() {
                if (difference.x > 0) {
                    difference.x -= UserPreferences.preferences.MAP_DRAG_INERTIA_COOLDOWN;
                } else {
                    difference.x += UserPreferences.preferences.MAP_DRAG_INERTIA_COOLDOWN;
                }

                if (difference.y > 0) {
                    difference.y -= UserPreferences.preferences.MAP_DRAG_INERTIA_COOLDOWN;
                } else {
                    difference.y += UserPreferences.preferences.MAP_DRAG_INERTIA_COOLDOWN;
                }
            }
        });
    }

    /**
     * Mouse click occurs on the map.
     */
    @Override
    public void mouseClicked(MouseEvent event) {
        mouseClickedPerformed(event);
    }

    /**
     * Hierarchy changed on the map.
     *
     * @param event
     */
    @Override
    public void hierarchyChanged(HierarchyEvent event) {
        hierarchyChangePerformed(event);
    }

    /**
     * Mouse drag event occurs on the map.
     *
     * @param event
     */
    @Override
    public void mouseDragged(MouseEvent event) {
        mouseDraggedPerformed(event);
    }

    /**
     * Mouse was pressed over the map.
     *
     * @param event
     */
    @Override
    public void mousePressed(MouseEvent event) {
        mousePressedPerformed(event);
    }

    /**
     * Mouse was released from press over the map.
     *
     * @param event
     */
    @Override
    public void mouseReleased(MouseEvent event) {
        mouseReleasedPerformed(event);
    }

    /**
     * Mouse exited the area of the map.
     *
     * @param event
     */
    @Override
    public void mouseExited(MouseEvent event) {
        mouseExitedPerformed(event);
    }

    private final static int clickInterval = (Integer) Toolkit.getDefaultToolkit().getDesktopProperty("awt.multiClickInterval");
    private MouseEvent lastEvent;
    private Timer timer;

    /**
     * Called if the mouse click was performed over the map. We do not know if
     * it is just single click or the the first click of double click. So it is
     * necessary to wait until time for double click expire and only after that
     * perform single click. If during this time another click come, it is
     * considered as double click.
     *
     * @param event
     */
    private void mouseClickedPerformed(MouseEvent event) {

        lastEvent = event;

        // Check if timer for second click in double click is running
        if (timer.isRunning()) {
            timer.stop();
            mouseDoubleClickPerformed(event);
        } else {
            timer.restart();
        }

    }

    @Override
    public void actionPerformed(ActionEvent event) {
        timer.stop();
        mouseSingleClickPerformed(lastEvent);
    }

    private void mouseDoubleClickPerformed(MouseEvent event) {
        int button = event.getButton();
        switch (button) {
            case 1: // Right mouse button
                if (Global.areaSelectionMode) {
                    AddAreaDialog addAreaDialog = new AddAreaDialog(frame);
                    boolean add = addAreaDialog.create(Areas.constructedArea);
                    if (add) {
                        Areas.saveConstructedArea();
                    } else {
                        Areas.cancelConstructedArea();
                    }
                    Global.areaSelectionMode = false;
                    map.refresh();
                } else {
                    scaleKeepingPointUnderCursor(new Point(event.getX(), event.getY()), UserPreferences.preferences.MAP_SCALE_IN_FACTOR);
                }
                break;
            case 3: // Left mouse button (number 2 is the middle mouse button, which is usually not present on contemporary mouses)
                scaleKeepingPointUnderCursor(new Point(event.getX(), event.getY()), UserPreferences.preferences.MAP_SCALE_OUT_FACTOR);
                break;
        }
    }

    private void mouseSingleClickPerformed(MouseEvent event) {
        int button = event.getButton();
        switch (button) {
            case 1: // Right mouse button
                if (Global.areaSelectionMode) {
                    areaSelection(event);
                } else {
                    selectObject(event);
                }
                break;
            case 3: // Left mouse button (number 2 is the middle mouse button, which is usually not present on contemporary mouses)
                mouseSingleLeftClickPerformed(event);
                break;
        }
    }

    private void selectObject(MouseEvent event) {
        Object selectedObject = ActiveObject.checkObjects(event);

        ActiveObject.setSelectedObject(selectedObject);
        BottomBarPainter.paint();
    }

//    private Object checkObjects(MouseEvent event) {
//        Object object = null;
//
//        if (object == null) {
//            object = checkUAVs(event);
//        }
//
//        if (object == null) {
//            object = checkUAVRequests(event);
//        }
//
//        if (object == null) {
//            object = checkChargingStations(event);
//        }
//
//        if (object == null) {
//            object = checkAreas(event);
//        }
//        
//        return object;
//    }
//
//    private UAV checkUAVs(MouseEvent event) {
//        for (UAV uav : UAVs.listOfUAVs) {
//            if (uav.clicked(event)) {
//                return uav;
//            }
//        }
//
//        return null;
//    }
//
//    private UAVRequest checkUAVRequests(MouseEvent event) {
//        for (UAVRequest uavRequest : UAVRequests.getList()) {
//            if (uavRequest.clicked(event)) {
//                return uavRequest;
//            }
//        }
//
//        return null;
//    }
//
//    private ChargingStation checkChargingStations(MouseEvent event) {
//        for (ChargingStation chargingStation : ChargingStations.listOfChargingStations) {
//            if (chargingStation.clicked(event)) {
//                return chargingStation;
//            }
//        }
//
//        return null;
//    }
//
//    private Area checkAreas(MouseEvent event) {
//        for (Area area : Areas.listOfAreas) {
//            if (area.clicked(event)) {
//                return area;
//            }
//        }
//
//        return null;
//    }

    private void areaSelection(MouseEvent event) {
        if (Areas.constructedArea == null) {
            Areas.constructedArea = new Area();
        }

        Location eventBirdviewLocation = new Location(event.getX(), event.getY());
        Location eventMapLocation = BirdviewMapConversion.birdviewToMap(eventBirdviewLocation, Global.window.mapPanel.map.mapImage, Global.window.mapPanel.map.scale);

        Areas.constructedArea.lastPoint = eventMapLocation;
        Areas.constructedArea.addPointBirdview(event.getX(), event.getY());
        map.refresh();
    }

    private void mouseSingleLeftClickPerformed(MouseEvent event) {
        ContextMenu contextMenu = new ContextMenu(event, frame);
        contextMenu.show(event.getComponent(), event.getX(), event.getY());

    }

    /**
     * Scales the map while keeping the original point under the cursor.
     *
     * @param point
     * @param scaleFactor
     */
    public void scaleKeepingPointUnderCursor(Point point, double scaleFactor) {

        double actualScale = map.getScale();
        double newScale = actualScale * scaleFactor;

        if (newScale <= UserPreferences.preferences.MAP_MAXIMUM_SCALE && newScale >= UserPreferences.preferences.MAP_MINIMAL_SCALE) {
            map.setScale(newScale);
            keepPointUnderCursor(point, scaleFactor);
        }
    }

    /**
     * Called when mouse pressed event occurs over the map.
     *
     * @param event
     */
    private void mousePressedPerformed(MouseEvent event) {
        scroller.stop();
        difference.setLocation(0, 0);
        JComponent component = (JComponent) event.getSource();

        Container container = component.getParent();
        if (container instanceof JViewport) {
            JViewport viewPort = (JViewport) container;
            Point convertedPoint = SwingUtilities.convertPoint(component, event.getPoint(), viewPort);
            startPoint.setLocation(convertedPoint);
        }
    }

    /**
     * Called when mouse dragged event occurs over the map. Drags the map to the
     * new position.
     *
     * @param event
     */
    private void mouseDraggedPerformed(MouseEvent event) {
        scroller.stop();
        JComponent component = (JComponent) event.getSource();
        Container container = component.getParent();
        if (container instanceof JViewport) {
            JViewport viewPort = (JViewport) component.getParent();
            Point convertedPoint = SwingUtilities.convertPoint(component, event.getPoint(), viewPort);
            int differenceX = startPoint.x - convertedPoint.x;
            int differenceY = startPoint.y - convertedPoint.y;
            Point viewPosition = viewPort.getViewPosition();
            viewPosition.translate(differenceX, differenceY);
            component.scrollRectToVisible(new Rectangle(viewPosition, viewPort.getSize()));
            difference.setLocation(UserPreferences.preferences.MAP_DRAG_INERTIA_SPEED * differenceX, UserPreferences.preferences.MAP_DRAG_INERTIA_SPEED * differenceY);
            startPoint.setLocation(convertedPoint);
            component.setCursor(dragCursor);
        }
    }

    /**
     * Called when mouse released event occurs over the map.
     *
     * @param event
     */
    private void mouseReleasedPerformed(MouseEvent event) {
        ((JComponent) event.getSource()).setCursor(defaultCursor);
        scroller.start();
    }

    /**
     * Move map to translate the old point (which was originally under the
     * cursor) under cursor.
     *
     * @param point
     * @param scaleFactor
     */
    public void keepPointUnderCursor(Point point, double scaleFactor) {
        Rectangle visibleArea = map.getVisibleRect();

        int shiftX = NumberConversion.toInteger((point.x * scaleFactor) - point.x);
        int shiftY = NumberConversion.toInteger((point.y * scaleFactor) - point.y);

        final Rectangle newArea = new Rectangle(visibleArea.x + shiftX, visibleArea.y + shiftY, visibleArea.width, visibleArea.height);

        // It is important to use invokeLater here otherwise it will not scroll correctly
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                map.scrollRectToVisible(newArea);
            }
        });
    }

    /**
     * Called when mouse hierarchy change event occurs over the map.
     *
     * @param event
     */
    private void hierarchyChangePerformed(HierarchyEvent event) {
        JComponent component = (JComponent) event.getSource();
        if ((event.getChangeFlags() & HierarchyEvent.DISPLAYABILITY_CHANGED) != 0
                && !component.isDisplayable()) {
            scroller.stop();
        }
    }

    /**
     * Called when mouse exited event occurs over the map.
     *
     * @param event
     */
    private void mouseExitedPerformed(MouseEvent event) {
        ((JComponent) event.getSource()).setCursor(defaultCursor);
        difference.setLocation(0, 0);
        scroller.stop();
    }
}
