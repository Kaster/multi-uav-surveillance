package me.dufek.securitydrones.algorithm;

import cz.cuni.amis.pogamut.base3d.worldview.object.Location;
import cz.cuni.amis.pogamut.usar2004.utils.USAR2004BotRunner;
import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.dufek.securitydrones.activeobject.ActiveObject;
import me.dufek.securitydrones.area.Area;
import me.dufek.securitydrones.gui.Window;
import me.dufek.securitydrones.uav.UAV;
import me.dufek.securitydrones.uav.request.UAVRequest;
import me.dufek.securitydrones.uav.request.UAVRequests;
import me.dufek.securitydrones.uav.UAVs;
import me.dufek.securitydrones.chargingstation.ChargingStations;
import me.dufek.securitydrones.chargingstation.schedule.Scheduler;
import me.dufek.securitydrones.main.Global;
import me.dufek.securitydrones.preferences.UserPreferences;
import me.dufek.securitydrones.uav.flightlevel.FlightLevels;

/**
 * The main superclass for all algorithms. All other algorithms must extend from
 * this method. The descendants may extend certain methods. It is also
 * responsible for initializing UAVs and charging stations scheduling.
 *
 * @author Jan Dufek
 */
public abstract class Algorithm {

    /**
     * It determines if the algorithm should be used in static or dynamic
     * version.
     */
    protected final Variability variability;

    /**
     * The main window of the application.
     */
    protected Window mainWindow = null;

    /**
     * The scheduler used to schedule charging stations.
     */
    private final Scheduler scheduler;

    /**
     * Indicates if the before logic method was already executed.
     */
    private boolean beforeLogicCompleted = false;

    /**
     * The graphics which can be used to draw into the bird view.
     */
    protected Graphics2D graphics;

    /**
     * Map image is the background image of the bird view.
     */
    protected BufferedImage mapImage;

    /**
     * The scale of the bird view.
     */
    protected double scale;

    /**
     * Initialize algorithm.
     *
     * @param mainWindow The main window of the application.
     * @param variability The variability of the algorithm.
     */
    public Algorithm(Window mainWindow, Variability variability) {
        this.variability = variability;
        this.mainWindow = mainWindow;
//        initializeUAVs();
        scheduler = new Scheduler();
    }

    /**
     * This method is executed only once at the beginning before the logic cycle
     * is started.
     */
    public void beforeLogic() {
        beforeLogicCompleted = true;

//        if (UAVRequests.requestPending()) {
//            initializeUAVs();
//        }
    }

    /**
     * It is called if an UAV is added.
     *
     * @param uav UAV
     */
    public void reportUAVAddition(UAV uav) {

    }

    /**
     * It is called if an UAV is deleted.
     *
     * @param uav UAV
     */
    public void reportUAVDeletion(UAV uav) {

    }

    /**
     * It is called if an area is added.
     *
     * @param area Area
     */
    public void reportAreaAddition(Area area) {

    }

    /**
     * It is called if an area is deleted.
     *
     * @param area Area
     */
    public void reportAreaDeletion(Area area) {

    }

    /**
     * It is called when charing of any UAV is completed.
     */
    public void reportChargingCompleted() {

    }

    /**
     * The logic method is called in cycles and it is the brain of the
     * algorithm.
     */
    public void logic() {
        if (!beforeLogicCompleted) {
            beforeLogic();
        }

        paintUAVsLocations();

        if (UAVRequests.requestPending()) {
            initializeUAVs();
        }

        if (!ChargingStations.noChargingStations()) {
            for (UAV uav : UAVs.listOfUAVs) {
                if (uav.hasNoChargingTask()) {
                    scheduler.requestCharging(uav);
                }
            }

            scheduler.makeSchedule();
        }

//        checkPossibleCollisions();
    }

    /**
     * This method is used to initialize UAV which are waiting for
     * initialization as UAV requests.
     */
    public final void initializeUAVs() {

        for (UAVRequest uavRequest : UAVRequests.getList()) {
            uavRequest.setFlightLevel();
            addUAV(uavRequest);

            if (ActiveObject.get() == uavRequest) {
                ActiveObject.setSelectedObject(UAVs.getUAVByUAVRequest(uavRequest));
            }
        }

        UAVRequests.clear();

        reportUAVAddition(null);
    }

    /**
     * This method is used to add UAV from an UAV request.
     *
     * @param uavRequest UAV request.
     */
    private void addUAV(final UAVRequest uavRequest) {
        final int numberOfUAVsBefore = UAVs.getNumberOfUAVs();

        Thread thread = new Thread() {
            @Override
            public void run() {
                new USAR2004BotRunner(UAV.class, uavRequest.getName(), UserPreferences.preferences.SERVER_ADDRESS, UserPreferences.preferences.SERVER_PORT).setLogLevel(Level.OFF).startAgent();
            }
        };
        thread.start();

        waitForUAV(numberOfUAVsBefore + 1);

        UAV newUAV = UAVs.getUAV(numberOfUAVsBefore);

        newUAV.setName(uavRequest.getName());

        newUAV.setBattery(uavRequest.getBattery());
        newUAV.setAmperageMean(uavRequest.getAmperageMean());

        newUAV.setLinearVelocity(uavRequest.getLinearVelocity());
        newUAV.setAltitudeVelocity(uavRequest.getAltitudeVelocity());
        newUAV.setRotationalVelocity(uavRequest.getRotationalVelocity());

        newUAV.setPrimarySensorHorizontalFieldOfView(uavRequest.getPrimarySensorHorizontalFieldOfView());
        newUAV.setPrimarySensorVerticalFieldOfView(uavRequest.getPrimarySensorVerticalFieldOfView());

        newUAV.setStartLocation(uavRequest.getStartLocation());
        newUAV.setFlightLevel(uavRequest.getFlightLevel());
//        newUAV.setFlightLevel(FlightLevels.getNextFlightLevel());
        newUAV.setUAVRequest(uavRequest);
    }

    /**
     * This method is used to wait for UAV to be spawned to the environment.
     *
     * @param desiredNumberOfUAVs The number of UAV.
     */
    private void waitForUAV(int desiredNumberOfUAVs) {
        while (UAVs.getNumberOfUAVs() != desiredNumberOfUAVs) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                Logger.getLogger(Algorithm.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    /**
     * Assign objective to particular UAV.
     *
     * @param uav UAV
     * @param objective Objective
     */
    public void assignObjective(UAV uav, Class objective) {
        uav.assignObjective(objective);
    }

    /**
     * Paint is called in every logic cycle and can be used to paint anything to
     * the bird view.
     *
     * @param graphics Graphics of the bird view.
     * @param mapImage Bird view background image.
     * @param scale The bird view scale.
     */
    public void paint(Graphics2D graphics, BufferedImage mapImage, double scale) {
        this.graphics = graphics;
        this.mapImage = mapImage;
        this.scale = scale;
    }

    /**
     * Used to paint locations of UAV to the bird view.
     */
    private void paintUAVsLocations() {

        if (mainWindow != null && mainWindow.mapPanel != null && mainWindow.mapPanel.map != null) {

            for (UAV uav : UAVs.listOfUAVs) {
                Location UAVLocation = uav.getActualLocationMapCoordinates();
                if (UAVLocation != null) {
                    mainWindow.mapPanel.map.refresh();
                }
            }
        }
    }

    /**
     * Get the name of the algorithm.
     *
     * @return Name.
     */
    public String getName() {
        throw new IllegalStateException("Should be implemented in ancestors.");
    }

    /**
     * Set alpha chanel of the graphics.
     *
     * @param alpha
     */
    protected void setAlpha(float alpha) {
        int type = AlphaComposite.SRC_OVER;
        AlphaComposite composite = AlphaComposite.getInstance(type, alpha);
        graphics.setComposite(composite);
    }
}
