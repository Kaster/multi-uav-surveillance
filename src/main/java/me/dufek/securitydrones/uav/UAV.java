package me.dufek.securitydrones.uav;

import me.dufek.securitydrones.uav.charging.ChargingTask;
import me.dufek.securitydrones.uav.objective.Objectives;
import me.dufek.securitydrones.uav.objective.Objective;
import cz.cuni.amis.pogamut.base.utils.guice.AgentScoped;
import cz.cuni.amis.pogamut.base3d.worldview.object.Location;
import cz.cuni.amis.pogamut.base3d.worldview.object.Rotation;
import cz.cuni.amis.pogamut.usar2004.agent.*;
import cz.cuni.amis.pogamut.usar2004.agent.module.configuration.*;
import cz.cuni.amis.pogamut.usar2004.agent.module.datatypes.*;
import cz.cuni.amis.pogamut.usar2004.agent.module.geometry.GeoSensorEffecter;
import cz.cuni.amis.pogamut.usar2004.agent.module.logic.USAR2004BotLogicController;
import cz.cuni.amis.pogamut.usar2004.agent.module.sensor.*;
import cz.cuni.amis.pogamut.usar2004.communication.messages.datatypes.SensorMount;
import cz.cuni.amis.pogamut.usar2004.communication.messages.usarcommands.*;
import cz.cuni.amis.pogamut.usar2004.communication.messages.usarinfomessages.NfoMessage;
import cz.cuni.amis.pogamut.usar2004.samples.AirScanner.*;
import cz.cuni.amis.utils.exception.PogamutException;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.dufek.securitydrones.area.Area;
import me.dufek.securitydrones.area.Areas;
import me.dufek.securitydrones.conversion.MapUAVConversion;
import me.dufek.securitydrones.geometry.Utils;
import me.dufek.securitydrones.heatmap.HeatCell;
import me.dufek.securitydrones.battery.Battery;
import me.dufek.securitydrones.chargingstation.ChargingStation;
import me.dufek.securitydrones.main.Global;
import me.dufek.securitydrones.preferences.UserPreferences;
import me.dufek.securitydrones.uav.destination.Destination;
import me.dufek.securitydrones.uav.destination.Destinations;
import me.dufek.securitydrones.uav.modules.ConfigMasterModule;
import me.dufek.securitydrones.uav.modules.GeometryMasterModule;
import me.dufek.securitydrones.uav.modules.ResponseModule;
import me.dufek.securitydrones.uav.modules.SensorMasterModuleQueued;
import me.dufek.securitydrones.uav.modules.StateMasterModule;
import me.dufek.securitydrones.uav.objective.Charge;
import me.dufek.securitydrones.uav.objective.flyto.FlyTo;
import me.dufek.securitydrones.uav.objective.flyto.FlyToState;
import me.dufek.securitydrones.uav.objective.Land;
import me.dufek.securitydrones.uav.objective.Takeoff;
import me.dufek.securitydrones.uav.request.UAVRequest;
import me.dufek.securitydrones.uav.status.UAVsStatus;
import me.dufek.securitydrones.uav.visiblearea.VisibleArea;
import me.dufek.securitydrones.utilities.Angle;

/**
 * This is the main class used for UAV control.
 *
 * @author Jan Dufek
 */
@AgentScoped
public class UAV extends USAR2004BotLogicController<USAR2004Bot> {

    /**
     * The name of UAV.
     */
    public String name;

    /**
     * The battery of the UAV.
     */
    public Battery battery;

    /**
     * The average amperage of the UAV.
     */
    private double amperageMean;

    /**
     * The linear velocity of the UAV given as percentage of maximum linear
     * velocity.
     */
    public double linearVelocity;

    /**
     * The altitude velocity of the UAV given as percentage of maximum linear
     * velocity.
     */
    public double altitudeVelocity;

    /**
     * The rotational velocity of the UAV given as percentage of maximum linear
     * velocity.
     */
    public double rotationalVelocity;

    /**
     * The horizontal field of view of the main sensor used for surveillance.
     */
    public double primarySensorHorizontalFieldOfView;

    /**
     * The vertical field of view of the main sensor used for surveillance.
     */
    public double primarySensorVerticalFieldOfView;

    /**
     * UAV request from which this UAV was created.
     */
    private UAVRequest uavRequest;

    /**
     * Objectives of UAV.
     */
    private final Objectives objectives = new Objectives(this);

    /**
     * Current objective of UAV.
     */
    private Objective objective = null;

    /**
     * The start location in map coordinate system.
     */
    public Location startLocation;

    /**
     * UAV's charging task.
     */
    public ChargingTask chargingTask = null;

    /**
     * The visual representation of the UAV in the bird view.
     */
    private Shape visualObject;

    /**
     * How many times was the battery changed.
     */
    private int numberOfBatteryChanges = 0;

    /**
     * Method triggered after Game is initialized and STARTPOSES obtained. It
     * will spawn the robot into the environment.
     *
     * @param nfom NfoMessge containing STARTPOSES
     */
    @Override
    public void robotInitialized(NfoMessage nfom) {
        super.robotInitialized(nfom);

        // Register this UAV
        UAVs.addUAV(this);

        logicInitialize(logicModule);

        if (Global.algorithm != null) {
            getAct().act(new Initialize("USARBot.AirRobot", getName(), getStartLocation(), null));
        }
    }

    /**
     * Determines if no charging task is assigned.
     *
     * @return True if no charging task is assigned and false if some charging
     * task is assigned
     */
    public boolean hasNoChargingTask() {
        return chargingTask == null;
    }

    /**
     * It cancels actual charging task.
     */
    public void resetChargingTask() {
        if (chargingTask != null) {
            chargingTask.cancel();
            chargingTask = null;
        }
    }

    /**
     * This method is used to notify UAV that schedule was changed.
     *
     * @param start The new start of the charging task.
     */
    public void notifyAboutScheduleChange(double start) {
        if (chargingTask == null) {
            throw new IllegalStateException("Changed task does not exist.");
        }

        chargingTask.setStart(start);
    }

    /**
     * Used to assign a new charging task.
     *
     * @param chargingTask The assigned charging task.
     */
    public void assignChargingTask(ChargingTask chargingTask) {
        this.chargingTask = chargingTask;
    }

    /**
     * Determines at what time the battery will be empty.
     *
     * @return Battery empty time.
     */
    public Double getBatteryEmptyTime() {
        double remainingTime = getRemainingBatteryTime();

        if (!Global.simulation.isRunning()) {
            return null;
        }

        // Wait for inicialization of state module
        while (staModule.getStatesByVehilceType(VehicleType.AERIAL_VEHICLE) == null) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                Logger.getLogger(UAV.class.getName()).log(Level.SEVERE, null, ex);
            }

            if (!Global.simulation.isRunning()) {
                return null;
            }

            if (staModule == null) {
                return null;
            }
        }

        getActualTime();

        double batteryEmptyTime = time + remainingTime;

        return batteryEmptyTime;
    }

    /**
     * Returns remaining battery time in seconds.
     *
     * @return Remaining battery time.
     */
    public double getRemainingBatteryTime() {
        double remainingTime;

        if (battery == null) {
            return 0;
        }
        remainingTime = (battery.getActualLevel() / (amperageMean * 1000)) * 60 * 60;

        return remainingTime;
    }

    /**
     * Kills the UAV.
     */
    public void terminate() {
        this.bot.stop();
    }

    /**
     * Sets UAV request of this UAV.
     *
     * @param uavRequest The UAV request which will be set.
     */
    public void setUAVRequest(UAVRequest uavRequest) {
        this.uavRequest = uavRequest;
    }

    /**
     * Gets UAV request.
     *
     * @return UAV request from which this particular UAV was created.
     */
    public UAVRequest getUAVRequest() {
        return this.uavRequest;
    }

    /**
     * Set UAV's name.
     *
     * @param name The name of the UAV.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets UAV's name. If the name is not initialized yet it will wait for it.
     *
     * @return
     */
    public String getName() {

        while (this.name == null) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                Logger.getLogger(UAV.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return name;
    }

    /**
     * Set the battery.
     *
     * @param battery The battery which is required to be set.
     */
    public void setBattery(Battery battery) {
        this.battery = battery;
    }

    /**
     * Get the actual capacity of the battery as a percentage of maximum
     * capacity.
     *
     * @return Percentage capacity.
     */
    public int getBatteryPercentage() {
        return battery.getBatteryPercentage();
    }

    /**
     * Sets amperage mean.
     *
     * @param amperageMean The amperage mean of the UAV.
     */
    public void setAmperageMean(double amperageMean) {
        this.amperageMean = amperageMean;
    }

    /**
     * Get the amperage mean of this UAV.
     *
     * @return UAV's amperage mean.
     */
    public double getAmperageMean() {
        return this.amperageMean;
    }

    /**
     * Sets linear velocity of the UAV as a percentage of the maximum possible
     * linear velocity.
     *
     * @param linearVelocity The linear velocity percentage.
     */
    public void setLinearVelocity(double linearVelocity) {
        this.linearVelocity = linearVelocity;
    }

    /**
     * Sets altitude velocity of the UAV as a percentage of the maximum possible
     * altitude velocity.
     *
     * @param altitudeVelocity The altitude velocity percentage.
     */
    public void setAltitudeVelocity(double altitudeVelocity) {
        this.altitudeVelocity = altitudeVelocity;
    }

    /**
     * Sets rotational velocity of the UAV as a percentage of the maximum
     * possible velocity.
     *
     * @param rotationalVelocity The rotational velocity percentage.
     */
    public void setRotationalVelocity(double rotationalVelocity) {
        this.rotationalVelocity = rotationalVelocity;
    }

    /**
     * Sets the horizontal field of view of the main sensor used for
     * surveillance.
     *
     * @param primarySensorHorizontalFieldOfView The horizontal field of view of
     * the main sensor.
     */
    public void setPrimarySensorHorizontalFieldOfView(double primarySensorHorizontalFieldOfView) {
        this.primarySensorHorizontalFieldOfView = primarySensorHorizontalFieldOfView;
    }

    /**
     * Sets the vertical field of view of the main sensor used for surveillance.
     *
     * @param primarySensorVerticalFieldOfView The vertical field of view of the
     * main sensor.
     */
    public void setPrimarySensorVerticalFieldOfView(double primarySensorVerticalFieldOfView) {
        this.primarySensorVerticalFieldOfView = primarySensorVerticalFieldOfView;
    }

    /**
     * Sets the start location of the UAV.
     *
     * @param startLocation UAV's start location.
     */
    public void setStartLocation(Location startLocation) {
        this.startLocation = startLocation;
    }

    /**
     * Gets the start location of UAV.
     *
     * @return Start location.
     */
    public Location getStartLocation() {
        while (this.startLocation == null) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                Logger.getLogger(UAV.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return this.startLocation;
    }

    /**
     * Gets the actual location of the UAV in map coordinate system.
     *
     * @return Actual location in map coordinates.
     */
    public Location getActualLocationMapCoordinates() {
        if (actualLocation == null) {
            return null;
        }
        return MapUAVConversion.UAVToMap(actualLocation, this.getStartLocation());
    }

    /**
     * Gets the real location of the UAV in map coordinate system.
     *
     * @return Real location in map coordinates.
     */
    public Location getRealLocationMapCoordinates() {
        if (truth == null || truth.getLocation() == null) {
            return null;
        }

        return truth.getLocation();
    }

    /**
     * Initialize controller of the UAV.
     *
     * @param bot The reference to the bot object.
     */
    @Override
    public void initializeController(USAR2004Bot bot) {
        super.initializeController(bot);
    }

    /**
     * The spawn location of the UAV. It is subtracted from UAV's location to
     * get relative location to the start point.
     */
    private final Location spawnLocation = new Location(0, 0, 0);

    /**
     * The current destination of the UAV.
     */
    public Location destination;

    /**
     * The desired flight altitude of the UAV.
     */
    public double flightAltitude;

    /**
     * The number of assigned flight level.
     */
    public double flightLevelNumber;

    /**
     * The actual altitude of the UAV.
     */
    public double actualAltitude = 0;

    /**
     * The actual location of the UAV in UAV's coordinate system.
     */
    public Location actualLocation;

    /**
     * The actual rotation of the UAV.
     */
    public Rotation actualRotation;

    /**
     * The next location to which UAV is heading.
     */
    public Location nextLocation;

    /**
     * The state of flight.
     */
    public FlyToState flyingState = FlyToState.DEFAULT;

    /**
     * The actual state of the UAV.
     */
    public State state;

    /**
     * Indicates if the parameters of the UAV were already obtained.
     */
    private boolean parametersObtained = false;

    /**
     * Maximum altitude velocity.
     */
    public double maxAltitudeVelocity = 0;

    /**
     * Used to assign an objective given as an object to this UAV.
     *
     * @param objective The objective as an object.
     */
    public void assignObjective(Objective objective) {

        if (objective == null) {
            throw new IllegalStateException("Objective object can not be null.");
        }

        objectives.assign(objective);
    }

    /**
     * Used to assign an objective given as a class to this UAV.
     *
     * @param objectiveClass The objective as a class.
     */
    public void assignObjective(Class objectiveClass) {
        Objective objectiveObject = null;

        try {
            objectiveObject = (Objective) objectiveClass.newInstance();

        } catch (InstantiationException ex) {
            Logger.getLogger(UAV.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(UAV.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (objectiveObject == null) {
            throw new IllegalStateException("Objective object can not be null.");
        }

        objectives.assign(objectiveObject);
    }

    /**
     * Gets the number of objectives in objective queue.
     *
     * @return The number of objectives.
     */
    public int getNumberOfObjectives() {
        return objectives.getNumberOfObjectives();
    }

    /**
     * Checks if UAV has any objectives assigned.
     *
     * @return True if there are some objectives and false otherwise.
     */
    public boolean hasObjective() {
        return getNumberOfObjectives() != 0;
    }

    /**
     * Checks if UAV is in take-off.
     *
     * @return True if UAV is the take-off right now and false otherwise.
     */
    public boolean isTakeoff() {
        return objectives.isAssign(Takeoff.class);

    }
    private boolean spawned = false;

    /**
     * This method is triggered by receipt of STA message. This is where all
     * behavior is controlled
     *
     * @throws PogamutException The Pogamut exception.
     */
    @Override
    public void logic() throws PogamutException {
        try {
            super.logic();

            savePreviousLocationAndTime();
            getActualTime();
            getDataFromResModule();

            if (obtainSensorMessages()) {
                clearModules();
                return;
            }

            // UAV could be spawned above ground, thus it is crutial to get it on ground before continuing in logic
            if (!spawned) {
                actualAltitude = laser.getNMidAvg(Preferences.NUMBER_OF_LASER_RAYS);

                if (Math.abs(actualAltitude) < UserPreferences.preferences.SPAWN_BREAK_ALTITUDE) {
                    if (Global.algorithm != null) {
                        getAct().act(new DriveAerial(0, 0, 0, 0, false));
                    }
                    spawned = true;
                } else {
                    if (Global.algorithm != null) {
                        getAct().act(new DriveAerial(-maxAltitudeVelocity, 0, 0, 0, false));
                    }
                    clearModules();
                    return;
                }
            }

            if (checkBattery()) {
                clearModules();
                return;
            }

            // Check if objective was satisfied
            if (objective != null && objective.isSatisfied()) {
                objectives.satisfyLast();
                objective = null;
            }

            if (chargingTask != null && !chargingTask.inProgress() && isInAir()) {

                if (chargingTask.getStart() - time < getTravelTimeToChargingStation()) {

                    if (objective != null) {
                        objective.setBeforeLogicCompleted(false);
                    }

                    Destinations.addDestination(new Destination(this, chargingTask.getLocation()));
                    me.dufek.securitydrones.logger.Logger.log(this.name + ": Going to charging station at " + chargingTask.getLocation());
                    UAVsStatus.setStatus(this, "Going to charging station at " + chargingTask.getLocation() + ".");
                    objectives.assignHighPriority(new Takeoff());
                    objectives.assignHighPriority(new Charge(chargingTask));
                    objectives.assignHighPriority(new Land());
                    objectives.assignHighPriority(new FlyTo(chargingTask.getLocation(), "charging station"));
                    chargingTask.setInProgress();
                }
            }

            // Get next objective
            if (objective == null && objectives.isAssign()) {
                objective = objectives.getNext();
            }

            // Execute objective
            if (objective != null) {
                objective.logic();
            }

            clearModules();
        } catch (Exception e) {
            clearModules();
            System.err.println(this.name + " logic exception.");
            e.printStackTrace();
        }

    }

    /**
     * Clears all the modules. It is necessary because otherwise data would pile
     * up in those data structures and it will consume too much memory.
     */
    private void clearModules() {
        senModule.getSensorsBySensorType(SensorType.ACCELERATION_SENSOR);
        senModule.getSensorsBySensorType(SensorType.ENCODER_SENSOR);
        senModule.getSensorsBySensorType(SensorType.GPS_SENSOR);
        senModule.getSensorsBySensorType(SensorType.GROUND_TRUTH);
        senModule.getSensorsBySensorType(SensorType.HELPER_SENSOR);
        senModule.getSensorsBySensorType(SensorType.HUMAN_MOTION_DETECTION);
        senModule.getSensorsBySensorType(SensorType.INS_SENSOR);
        senModule.getSensorsBySensorType(SensorType.LASER_SENSOR);
        senModule.getSensorsBySensorType(SensorType.ODOMETRY_SENSOR);
        senModule.getSensorsBySensorType(SensorType.RANGE_SENSOR);
        senModule.getSensorsBySensorType(SensorType.RFID_SENSOR);
        senModule.getSensorsBySensorType(SensorType.SOUND_SENSOR);
        senModule.getSensorsBySensorType(SensorType.TACHOMETER);
        senModule.getSensorsBySensorType(SensorType.TOUCH_SENSOR);
        senModule.getSensorsBySensorType(SensorType.UNKNOWN_SENSOR);
        senModule.getSensorsBySensorType(SensorType.VICTIM_SENSOR);

        geoModule.getGeometriesByGeometryType(GeometryType.AERIAL_VEHICLE);
        geoModule.getGeometriesByGeometryType(GeometryType.GROUND_VEHICLE);
        geoModule.getGeometriesByGeometryType(GeometryType.LEGGED_ROBOT);
        geoModule.getGeometriesByGeometryType(GeometryType.MISSION_PACKAGE);
        geoModule.getGeometriesByGeometryType(GeometryType.NAUTIC_VEHICLE);
        geoModule.getGeometriesByGeometryType(GeometryType.SENSOR_EFFECTER);
        geoModule.getGeometriesByType("Sonar");

        confModule.getConfigurationsByConfigType(ConfigType.AERIAL_VEHICLE);
        confModule.getConfigurationsByConfigType(ConfigType.EFFECTER);
        confModule.getConfigurationsByConfigType(ConfigType.GROUND_VEHICLE);
        confModule.getConfigurationsByConfigType(ConfigType.LEGGED_ROBOT);
        confModule.getConfigurationsByConfigType(ConfigType.MISSION_PACKAGE);
        confModule.getConfigurationsByConfigType(ConfigType.NAUTIC_VEHICLE);
        confModule.getConfigurationsByConfigType(ConfigType.SENSOR);
        confModule.queryConfigurationByType("Robot");

        for (int i = 0; i < resModule.size(); i++) {
            resModule.pull();
        }

        staModule.getStatesByVehilceType(VehicleType.AERIAL_VEHICLE);
        staModule.getStatesByVehilceType(VehicleType.GROUND_VEHICLE);
        staModule.getStatesByVehilceType(VehicleType.LEGGED_ROBOT);
        staModule.getStatesByVehilceType(VehicleType.NAUTIC_VEHICLE);
        staModule.getStatesByVehilceType(VehicleType.UNKNOWN);
    }

    /**
     * Used to receive sensor messages.
     *
     * @return True if the the message was received and false otherwise.
     */
    private boolean obtainSensorMessages() {
        if (!parametersObtained) {
            if (!acceptSensorMessage()) {
                return true;
            }
            getConfiguration();
            return true;
        } else {
            acceptSensorMessages();
        }
        return false;
    }

    /**
     * Removes data from the response module. Otherwise data will pile up.
     */
    private void getDataFromResModule() {
        while (resModule.size() > 0) {
            resModule.pull();
        }
    }

    /**
     * It will run battery logic and checks if there is any free capacity left.
     *
     * @return True if battery is empty and false otherwise.
     */
    private boolean checkBattery() {
        if (!(objective instanceof Charge)) {
            batteryLogic();
            if (battery.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Saves actual time from the server.
     */
    private void getActualTime() {
        time = staModule.getStatesByVehilceType(VehicleType.AERIAL_VEHICLE).getTime();
    }

    /**
     * Counter used for computing actual velocity.
     */
    private int velocityIntervalCounter = 0;

    /**
     * Previous location used for computing velocity.
     */
    private Location velocityPreviousLocation;

    /**
     * Previous time used for computing velocity.
     */
    private double velocityPreviousTime;

    /**
     * Actual location used for computing velocity.
     */
    private Location velocityActualLocation;

    /**
     * Actual time used for computing velocity.
     */
    private double velocityActualTime;

    /**
     * It saves the previous location and time in order to compute actual
     * velocity.
     */
    private void savePreviousLocationAndTime() {
        if (velocityIntervalCounter == 5) {
            velocityIntervalCounter = 0;

            if (velocityActualLocation != null) {
                velocityPreviousLocation = velocityActualLocation.clone();
                velocityPreviousTime = velocityActualTime;
            }

            velocityActualLocation = Location.sub(startLocation, ins.getLocation());
            velocityActualTime = staModule.getStatesByVehilceType(VehicleType.AERIAL_VEHICLE).getTime();
        } else {
            velocityIntervalCounter++;
        }
    }

    /**
     * It will set the actual objective to the first objective in the queue of
     * objectives.
     */
    public void refreshObjectives() {
        objective = objectives.getNext();
    }

    /**
     * Gets time needed for landing.
     *
     * @return Landing time.
     */
    public double getLandingTime() {
        double verticalDistance = flightAltitude;
        double verticalVelocity = getAverageVerticalVelocity();
        return getAverageTravelTime(verticalDistance, verticalVelocity);
    }

    /**
     * Get the time needed for takeoff.
     *
     * @return Takeoff time.
     */
    public double getTakeoffTime() {
        return getLandingTime();
    }

    /**
     * Computes the time when will the charging be required.
     *
     * @return Charging required time.
     */
    public double getChargingProcedureRequestedTime() {
        // Time when battery level will reach zero
        double batteryEmptyTime = getBatteryEmptyTime();

        // Intervel in which UAV must arrive above charging station
        double startingProcedureReserve = UserPreferences.preferences.UAV_RESERVE_STARTING_CHARGING_PROCEDURE;

        // Time needed for landing on charging station
        double landingTime = getLandingTime();

        // Reserve for landing
        double landingTimeReserve = UserPreferences.preferences.UAV_RESERVE_LANDING_TO_CHARGER;

        // Requested time is battery empty time minus half of interval in which UAV must arrive minus time needed for landing
        double requestedTime = batteryEmptyTime - startingProcedureReserve / 2 - landingTime * landingTimeReserve;

        return requestedTime;

//        return getBatteryEmptyTime() - getLandingTime() * UserPreferences.preferences.UAV_RESERVE_LANDING_TO_CHARGER - UserPreferences.preferences.UAV_RESERVE_STARTING_CHARGING_PROCEDURE;
    }

    /**
     * Gets the average vertical velocity.
     *
     * @return Average vertical velocity.
     */
    private double getAverageVerticalVelocity() {
        while (maxAltitudeVelocity == 0) {
            try {
                Thread.sleep(100);

            } catch (InterruptedException ex) {
                Logger.getLogger(UAV.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        }

        return maxAltitudeVelocity * altitudeVelocity;
    }

    /**
     * Gets average horizontal velocity.
     *
     * @return Average horizontal velocity.
     */
    private double getAverageHorizontalVelocity() {
        return maxLinearVelocity * linearVelocity;
    }

    /**
     * Gets average rotational velocity.
     *
     * @return Average rotational velocity.
     */
    private double getAverageRotationalVelocity() {

        double difference = getAngleToChargingStation();

        double averageRotationalVelocity = Math.min(Math.abs((double) (maxRotationalVelocity * (-difference / 90))), rotationalVelocity);
        return averageRotationalVelocity;
    }

    /**
     * Get the angle between UAV's current rotation and the charging station
     * where this UAV is scheduled.
     *
     * @return Angular difference from the charging station.
     */
    private double getAngleToChargingStation() {
        Location chargingStationUAVCoordinates = MapUAVConversion.mapToUAV(chargingTask.getLocation(), startLocation);

        double differenceX = chargingStationUAVCoordinates.getX() - actualLocation.getX();
        double differenceY = chargingStationUAVCoordinates.getY() - actualLocation.getY();

        double distance = Math.sqrt(differenceX * differenceX + differenceY * differenceY);

        double sin = differenceY / distance;
        double cos = differenceX / distance;

        double requiredAngle = (Angle.getAngle(sin, cos) + 180) % 360;
        double actualAngle = actualRotation.yaw * 180 / Math.PI;
        double angleToChargingStation = actualAngle - requiredAngle;
        if (angleToChargingStation > 180) {
            angleToChargingStation = angleToChargingStation - 360;
        } else if (angleToChargingStation < -180) {
            angleToChargingStation = angleToChargingStation + 360;
        }
        return angleToChargingStation;
    }

    /**
     * Gets angle to specific location.
     *
     * @param location The location to which we want to get the angle.
     * @return The angle to this location.
     */
    public Double getAngleTo(Location location) {

        if (actualRotation == null || actualLocation == null || location == null) {
            return null;
        }

        Location locationUAVCoordinates = MapUAVConversion.mapToUAV(location, startLocation);

        double differenceX = locationUAVCoordinates.getX() - actualLocation.getX();
        double differenceY = locationUAVCoordinates.getY() - actualLocation.getY();

        double distance = Math.sqrt(differenceX * differenceX + differenceY * differenceY);

        double sin = differenceY / distance;
        double cos = differenceX / distance;

        double requiredAngle = (Angle.getAngle(sin, cos) + 180) % 360;
        double actualAngle = actualRotation.yaw * 180 / Math.PI;
        double angle = actualAngle - requiredAngle;
        if (angle > 180) {
            angle = angle - 360;
        } else if (angle < -180) {
            angle = angle + 360;
        }
        return -angle;
    }

    /**
     * Gets the average time needed to travel specified distance.
     *
     * @param distance Distance.
     * @param velocity Velocity.
     * @return
     */
    private double getAverageTravelTime(double distance, double velocity) {
        return Math.abs(distance / velocity);
    }

    /**
     * Gets the time needed to travel to charging station.
     *
     * @return Travel time to charging station.
     */
    private double getTravelTimeToChargingStation() {
        Location uavMapLocation = MapUAVConversion.UAVToMap(actualLocation, startLocation);

        // We count only with horizontal distance. Vertical distance is included in allocated time as well as time reserve.
        double horizontalDistance = Location.getDistance2D(uavMapLocation, chargingTask.getLocation());

        double horizontalVelocity = getAverageHorizontalVelocity();

        double horizontalTime = getAverageTravelTime(horizontalDistance, horizontalVelocity);

        double rotationAngleDegrees = getAngleToChargingStation();
        double rotationAngleRadians = Math.toRadians(rotationAngleDegrees);
        double rotationVelocity = getAverageRotationalVelocity();
        double rotationTime = getAverageTravelTime(rotationAngleRadians, rotationVelocity);

        // Multiply trave time with time reserves (for rotating, avoiding, etc.) Then subtract half of the reserve interval.
        return rotationTime + horizontalTime * UserPreferences.preferences.UAV_RESERVE_FOR_GOING_BACK_TO_CHARGER - UserPreferences.preferences.UAV_RESERVE_STARTING_CHARGING_PROCEDURE / 2;
    }

    /**
     * The battery logic. It is responsible for draining battery capacity.
     */
    private void batteryLogic() {

        this.battery.drain(amperageMean);

        if (battery.isEmpty()) {
            crash();
        }
    }

    /**
     * It cause UAV to simulate a crash.
     */
    private void crash() {
        state = State.CRASHED;
        actualAltitude = laser.getNMidAvg(Preferences.NUMBER_OF_LASER_RAYS);

        if (Global.algorithm != null) {
            getAct().act(new DriveAerial(-30, 0, 0, 0, false));
        }
    }

    /**
     * Carefully tries to obtain all sensor messages. It is set at the
     * beginning.
     *
     * @return Returns false if it was not able to obtain values. True
     * otherwise.
     */
    private boolean acceptSensorMessage() {

        if (!senModule.isSensorReady(SensorType.LASER_SENSOR)
                || !senModule.isSensorReady(SensorType.INS_SENSOR)
                || !senModule.isSensorReady(SensorType.GROUND_TRUTH)
                || !senModule.isSensorReady(SensorType.RANGE_SENSOR)) {
            return false;
        }

        laser = (SensorLaser) senModule.getSensorsBySensorType(SensorType.LASER_SENSOR).get(0);
        ins = (SensorINS) senModule.getSensorsBySensorType(SensorType.INS_SENSOR).get(0);
        sonar = (SensorRange) senModule.getSensorsBySensorType(SensorType.RANGE_SENSOR).get(0);
        truth = (SensorGroundTruth) senModule.getSensorsBySensorType(SensorType.GROUND_TRUTH).get(0);

        return true;
    }

    /**
     * Gets all sensor messages queued from the last logic call.
     */
    private void acceptSensorMessages() {
        List<SuperSensor> lasers = senModule.getSensorsBySensorType(SensorType.LASER_SENSOR);
        List<SuperSensor> inses = senModule.getSensorsBySensorType(SensorType.INS_SENSOR);
        List<SuperSensor> sonars = senModule.getSensorsBySensorType(SensorType.RANGE_SENSOR);
        List<SuperSensor> truths = senModule.getSensorsBySensorType(SensorType.GROUND_TRUTH);
//        List<SuperSensor> gpss = senModule.getSensorsBySensorType(SensorType.GPS_SENSOR);

        int minimumIndex = ToolBox.getMin(lasers.size(), inses.size(), sonars.size(), truths.size());

        for (int i = 0; i < minimumIndex; i++) {
            laser = (SensorLaser) lasers.get(i);
            truth = (SensorGroundTruth) truths.get(i);
            ins = (SensorINS) inses.get(i);
            sonar = (SensorRange) sonars.get(i);
//            gps = (SensorGPS) gpss.get(i);

            refreshLocationAndRotation();
        }
    }

    /**
     * Sets actual rotation and orientation and sets important sensor data to
     * the preview JForm.
     */
    private void refreshLocationAndRotation() {
        actualLocation = Location.sub(startLocation, ins.getLocation());
        actualRotation = ins.getOrientation();
    }

    /**
     * Laser sensor.
     */
    public SensorLaser laser;

    /**
     * Ground truth sensor.
     */
    private SensorGroundTruth truth;

    /**
     * Inertia Navigational System.
     */
    private SensorINS ins;

    /**
     * Sonar.
     */
    public SensorRange sonar;

    /**
     * Actual time.
     */
    public double time = 0;

    /**
     * The time of start.
     */
    private double startTime = 0;

    /**
     * Maximum lateral velocity.
     */
    private double maxLateralVelocity = 0;

    /**
     * Maximum linear velocity.
     */
    public double maxLinearVelocity = 0;

    /**
     * Maximum rotational velocity.
     */
    public double maxRotationalVelocity = 0;

    /**
     * Gets sensor data from ground truth sensor and sets it into the INS sensor
     * which results in erasing the drift UAV gained.
     */
    public void resetINS() {
        Location truthLocation = truth.getLocation();
        Rotation truthRotation = truth.getOrientation();

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(truthLocation.x).append(',').append(truthLocation.y).append(',').append(actualLocation.z).append(','); // Here we need to set up actual location as z because real z is computed in respect to different point
        stringBuilder.append(truthRotation.roll).append(',').append(truthRotation.pitch).append(',').append(truthRotation.yaw);

        if (Global.algorithm != null) {
            bot.getAct().act(new SetSensorEffecter("INS", "INS", "POSE", stringBuilder.toString()));
        }
    }

    /**
     * Temporary location used during obstacle avoidance.
     */
    public Location temporaryNextLocation;

    /**
     * Temporary state during obstacle avoidance.
     */
    public FlyToState temporaryState;

    /**
     * Sonar positions.
     */
    public List<SensorMount> sonarGeo;

    /**
     * The sonar risk levels. Risk level a level of risk some obstacle will be
     * hit.
     */
    public Map.Entry<String, Double> greatestRiskSonar;

    /**
     * How many times no risk of collision was detected.
     */
    public int noRiskCount = 0;

    /**
     * How many times the risk of collision was detected.
     */
    public int riskCount = 0;

    /**
     * If the geometry or configuration modules are ready it issues a query
     * about the robots configuration and than a query about the sonar geometry.
     * Please note that this method works in cycles. First it issues a
     * configuration query, when received it issues a geometry query. When
     * obtained both it can proceed in filling and configurating variables.
     * complete configuration information. There is a reason why both queries
     * are issued at different calls. This method is considered to be called -
     * once at most - from the logic() which is in fact triggered every time the
     * state message from the server is received. Thus, each query in this
     * method is issued at different cycle. This fact gives the server necessary
     * time to process the queries. In other words: The experience with the
     * USARSim server so far was that it won't process more than one CONF/GEO
     * message at a time. Which is why we can't call more than one
     * GETCONF/GETGEO command message in one cycle. Note that there would be
     * another "else if" branch if we wanted to know any other conf./geo. data
     * about anything else. The if clause with another isReady() would not be
     * sufficient, we would have to ask for example about the number of messages
     * conf or geo module has in store.
     *
     */
    /**
     * Obtaining of configuration information in several steps. It is not
     * possible to obtain all data in one logic cycle.
     */
    private void getConfiguration() {
        if (!confModule.isReady()) {
            confModule.queryConfigurationByType("Robot");
        } else if (!geoModule.isReady()) {
            geoModule.queryGeometryByType("Sonar");
        } else if (senModule.isReady()) {
            ConfigAerial robotCfg = (ConfigAerial) confModule.getConfigurationsByConfigType(ConfigType.AERIAL_VEHICLE).get(0);

            // Get parameters
            maxLateralVelocity = Double.parseDouble(robotCfg.getFeatureValueBy("MaxLateralVelocity"));
            maxLinearVelocity = Double.parseDouble(robotCfg.getFeatureValueBy("MaxLinearVelocity"));
            maxAltitudeVelocity = Double.parseDouble(robotCfg.getFeatureValueBy("MaxAltitudeVelocity"));
            maxRotationalVelocity = Double.parseDouble(robotCfg.getFeatureValueBy("MaxRotationalVelocity"));

            // Prepare variables for the flight
            actualLocation = spawnLocation;

            startTime = time;

            // Save geometry gf sonars
            sonarGeo = ((GeoSensorEffecter) geoModule.getGeometriesByGeometryType(GeometryType.SENSOR_EFFECTER).get(0)).getSensorMountCollection();

            // Set flag
            parametersObtained = true;
        }
    }

    /**
     * Initialization of modules used within this robot.
     *
     * @param bot Necessary parameter for hooking listeners and for sending
     * commands
     */
    @Override
    public void prepareBot(USAR2004Bot bot) {
        super.prepareBot(bot);

        senModule = new SensorMasterModuleQueued(bot);
        geoModule = new GeometryMasterModule(bot);
        confModule = new ConfigMasterModule(bot);
        resModule = new ResponseModule(bot);
        staModule = new StateMasterModule(bot);
    }

    /**
     * Sensory module.
     */
    public SensorMasterModuleQueued senModule;

    /**
     * Geometry module.
     */
    private GeometryMasterModule geoModule;

    /**
     * Configuration module.
     */
    private ConfigMasterModule confModule;

    /**
     * Response module.
     */
    private ResponseModule resModule;

    /**
     * State module.
     */
    public StateMasterModule staModule;

    /**
     * Sets flight level.
     *
     * @param flightLevel Flight level.
     */
    public void setFlightLevel(int flightLevel) {
        this.flightAltitude = UserPreferences.preferences.MAXIMUM_FLIGHT_LEVEL - flightLevel * UserPreferences.preferences.DISTANCE_BETWEEN_FLIGHT_LEVELS;
        this.flightLevelNumber = flightLevel;
    }

    /**
     * Gets current time.
     *
     * @return Time.
     */
    public double getTime() {
        return this.time;
    }

    /**
     * Gets location in map coordinate system.
     *
     * @return Location.
     */
    public Location getLocationMapCoordinates() {
        if (actualLocation == null || startLocation == null) {
            return null;
        }

        return MapUAVConversion.UAVToMap(actualLocation, startLocation);
    }

    /**
     * Gets actual visible area of the UAV.
     *
     * @return A visible area representation.
     */
    public VisibleArea getVisibleArea() {

        if (actualLocation != null && actualRotation != null) {
            Location actualLocationMapCoordinates = getLocationMapCoordinates();

            double visibleAreaWidth = Math.tan(Math.toRadians(primarySensorHorizontalFieldOfView / 2)) * actualAltitude * 2;
            double visibleAreaHeight = Math.tan(Math.toRadians(primarySensorVerticalFieldOfView / 2)) * actualAltitude * 2;

            double topLeftX = actualLocationMapCoordinates.getX() + visibleAreaHeight / 2;
            double topLeftY = actualLocationMapCoordinates.getY() - visibleAreaWidth / 2;

            double topRightX = actualLocationMapCoordinates.getX() + visibleAreaHeight / 2;
            double topRightY = actualLocationMapCoordinates.getY() + visibleAreaWidth / 2;

            double bottomLeftX = actualLocationMapCoordinates.getX() - visibleAreaHeight / 2;
            double bottomLeftY = actualLocationMapCoordinates.getY() - visibleAreaWidth / 2;

            double bottomRightX = actualLocationMapCoordinates.getX() - visibleAreaHeight / 2;
            double bottomRightY = actualLocationMapCoordinates.getY() + visibleAreaWidth / 2;

            double[] xPoints = {topLeftX, topRightX, bottomRightX, bottomLeftX};
            double[] yPoints = {topLeftY, topRightY, bottomRightY, bottomLeftY};

            VisibleArea visibleArea = new VisibleArea(xPoints, yPoints);

            Utils.rotatePolygon(visibleArea, new Location(actualLocationMapCoordinates.getX(), actualLocationMapCoordinates.getY(), 0), actualRotation.getYaw());

            return visibleArea;
        } else {
            return null;
        }
    }

    /**
     * Cools down all the cells under UAV visible area.
     */
    public void cooldownVisibleArea() {
        for (Area area : Areas.listOfAreas) {
            if (area.getGrid() == null) {
                continue;
            }

            for (HeatCell cell : area.getGrid().getCells()) {
                if (isVisible(cell)) {
                    cell.coolDown();
                }
            }
        }
    }

    /**
     * Checks if given heat cell is visible from the UAV.
     *
     * @param cell Heat cell.
     * @return True if it is visible and false if not.
     */
    public boolean isVisible(HeatCell cell) {
        VisibleArea visibleArea = getVisibleArea();

        if (visibleArea != null) {
            return visibleArea.contains(cell);
        } else {
            return false;
        }
    }

    /**
     * Sets visual object corresponding to this UAV.
     *
     * @param visualObject The visual object.
     */
    public void setVisualObject(Shape visualObject) {
        this.visualObject = visualObject;
    }

    /**
     * Checks if click occurs over the visual representation of this UAV.
     *
     * @param event Click.
     * @return True if it was clicked on the UAV and false otherwise.
     */
    public boolean clicked(MouseEvent event) {
        Ellipse2D visualObjectEllipse = (Ellipse2D) visualObject;

        if (visualObjectEllipse == null) {
            return false;
        }

        Ellipse2D activeArea = new Ellipse2D.Double(visualObjectEllipse.getX() - UserPreferences.preferences.CLICK_ACTIVE_AREA_SIZE / 2, visualObjectEllipse.getY() - UserPreferences.preferences.CLICK_ACTIVE_AREA_SIZE / 2, visualObjectEllipse.getWidth() + UserPreferences.preferences.CLICK_ACTIVE_AREA_SIZE, visualObjectEllipse.getHeight() + UserPreferences.preferences.CLICK_ACTIVE_AREA_SIZE);

        return activeArea.contains(event.getX(), event.getY());
    }

    /**
     * Gets start time of the UAV.
     *
     * @return Start time.
     */
    public double getStartTime() {
        return this.startTime;
    }

    /**
     * Gets up time of the UAV. It is the time for which the UAV is running.
     *
     * @return Up time.
     */
    public double getUpTime() {
        double upTime = this.time - this.startTime;

        return upTime;
    }

    /**
     * Gets flight level of UAV.
     *
     * @return Flight level.
     */
    public double getFlightLevel() {
        return this.flightAltitude;
    }

    /**
     * Gets the number of the flight level.
     *
     * @return Number of the flight level.
     */
    public double getFlightLevelNumber() {
        return this.flightLevelNumber;
    }

    /**
     * Get state of the UAV.
     *
     * @return State.
     */
    public String getState() {
        if (isCrashed()) {
            return "Crashed";
        } else if (isInAir()) {
            return "In Air";
        } else if (isLanded()) {
            return "Landed";
        } else if (isRising()) {
            return "Take Off";
        } else {
            return null;
        }
    }

    /**
     * Checks if UAV is crashed.
     *
     * @return True if it is crashed and false otherwise.
     */
    public boolean isCrashed() {
        return state == State.CRASHED;
    }

    /**
     * Checks if UAV is in air.
     *
     * @return True if it is in air, false otherwise.
     */
    public boolean isInAir() {
        return state == State.IN_AIR;
    }

    /**
     * Checks if UAV has landed.
     *
     * @return True if landed, false otherwise.
     */
    public boolean isLanded() {
        return state == State.LANDED;
    }

    /**
     * Checks if UAV is rising.
     *
     * @return True if it is rising and false otherwise.
     */
    public boolean isRising() {
        return (this.objective instanceof Takeoff);
    }

    /**
     * Gets the actual altitude.
     *
     * @return Actual altitude.
     */
    public double getActualAltitude() {
        return actualAltitude;
    }

    /**
     * Gets actual rotation.
     *
     * @return Actual rotation.
     */
    public Rotation getActualRotation() {
        return this.actualRotation;
    }

    /**
     * Gets actual velocity of the UAV.
     *
     * @return Actual velocity.
     */
    public Double getActualVelocity() {
        if (velocityPreviousLocation != null && velocityActualLocation != null) {
            double distance = Location.getDistance(velocityPreviousLocation, velocityActualLocation);
            double timeDifference = velocityActualTime - velocityPreviousTime;

            double actualVelocity = distance / timeDifference;

            return actualVelocity;
        } else {
            return null;
        }
    }

    /**
     * Gets linear velocity.
     *
     * @return Linear velocity.
     */
    public double getLinearVelocity() {
        return this.linearVelocity;
    }

    /**
     * Gets altitude velocity.
     *
     * @return Altitude velocity.
     */
    public double getAltitudeVelocity() {
        return this.altitudeVelocity;
    }

    /**
     * Gets rotational velocity.
     *
     * @return Rotational velocity.
     */
    public double getRotationalVelocity() {
        return this.rotationalVelocity;
    }

    /**
     * Gets maximum linear velocity.
     *
     * @return Maximum linear velocity.
     */
    public double getMaximumLinearVelocity() {
        return this.maxLinearVelocity;
    }

    /**
     * Gets maximum altitude velocity.
     *
     * @return Maximum altitude velocity.
     */
    public double getMaximumAltitudeVelocity() {
        return this.maxAltitudeVelocity;
    }

    /**
     * Gets maximum rotational velocity.
     *
     * @return Maximum rotational velocity.
     */
    public double getMaximumRotationalVelocity() {
        return this.maxRotationalVelocity;
    }

    /**
     * Gets the horizontal field of view of the main sensor used for
     * surveillance.
     *
     * @return Horizontal field of view
     */
    public double getPrimarySensorHorizontalFieldOfView() {
        return this.primarySensorHorizontalFieldOfView;
    }

    /**
     * Gets the vertical field of view of the main sensor used for surveillance.
     *
     * @return Vertical field of view
     */
    public double getPrimarySensorVerticalFieldOfView() {
        return this.primarySensorVerticalFieldOfView;
    }

    /**
     * Gets the width of visible area.
     *
     * @return Visible area width.
     */
    public double getVisibleAreaWidth() {
        return Math.tan(Math.toRadians(getPrimarySensorHorizontalFieldOfView() / 2)) * actualAltitude * 2;
    }

    /**
     * Gets the height of visible area.
     *
     * @return Visible area height.
     */
    public double getVisibleAreaHeight() {
        return Math.tan(Math.toRadians(getPrimarySensorVerticalFieldOfView() / 2)) * actualAltitude * 2;
    }

    /**
     * Gets the list of objectives as a string.
     *
     * @return String list of objectives.
     */
    public synchronized String getObjectivesStringList() {
        return objectives.toString(me.dufek.securitydrones.gui.bottombar.Preferences.NUMBER_OF_OBJECTIVES_IN_LIST);
    }

    /**
     * Gets the battery.
     *
     * @return Battery
     */
    public Battery getBattery() {
        return this.battery;
    }

    /**
     * Gets the battery remaining time.
     *
     * @return Battery remaining time.
     */
    public Double getBatteryRemainingTime() {
        Double batteryEmptyTime = getBatteryEmptyTime();

        if (batteryEmptyTime != null) {
            return batteryEmptyTime - time;
        } else {
            return null;
        }
    }

    /**
     * Gets number of battery changes.
     *
     * @return Number of battery changes.
     */
    public int getNumberOfBatteryChanges() {
        return this.numberOfBatteryChanges;
    }

    /**
     * Increase number of battery changes.
     */
    public void increaseNumberOfBatteryChanges() {
        this.numberOfBatteryChanges++;
    }

    /**
     * Get the time the charging starts.
     * 
     * @return The start charging time. 
     */
    public Double getChargingStart() {
        if (chargingTask != null) {
            return this.chargingTask.getStart();
        } else {
            return null;
        }
    }

    /**
     * Get the time the charging ends.
     * 
     * @return The end charging time. 
     */
    public Double getChargingEnd() {
        if (chargingTask != null) {
            return this.chargingTask.getEnd();
        } else {
            return null;
        }
    }

    /**
     * Return the name of charging station where this UAV is scheduled.
     * 
     * @return Name of charging station.
     */
    public String getChargingStationName() {
        if (chargingTask != null) {
            return this.chargingTask.getChargingStation().getName();
        } else {
            return null;
        }
    }

    /**
     * Gets distance to charging station where the UAV is scheduled.
     * 
     * @return Distance to charging station.
     */
    public Double getDistanceToChargingStation() {
        if (chargingTask != null && this.getActualLocationMapCoordinates() != null && this.chargingTask.getLocation() != null) {
            return Location.getDistance2D(getActualLocationMapCoordinates(), chargingTask.getLocation());
        } else {
            return null;
        }
    }

    /**
     * Gets the time needed to fly to charging station.
     * 
     * @return Flight time to charging station.
     */
    public Double getFlyingTimeToChargingStation() {
        if (chargingTask != null && actualRotation != null) {
            return this.getTravelTimeToChargingStation();
        } else {
            return null;
        }
    }

    /**
     * Gets all the sonars.
     * 
     * @return Sonars.
     */
    public Map<String, Double> getSonars() {
        if (sonar != null) {
            return sonar.getRanges();
        } else {
            return null;
        }
    }

    /**
     * Gets actual objective of the UAV.
     * 
     * @return Objective.
     */
    public Object getObjective() {
        return this.objective;
    }

    /**
     * Get the charing station where the UAV is scheduled.
     * 
     * @return Charging station of this UAV.
     */
    public ChargingStation getChargingStation() {
        if (this.chargingTask == null) {
            return null;
        }

        return this.chargingTask.getChargingStation();
    }

    /**
     * Cancel all the objectives of this UAV.
     */
    public void cancelObjectives() {
        this.objectives.cancelObjectives();

        if (objective instanceof FlyTo && !objective.isHightPriority()) {
            this.objective = null;

            if (Global.algorithm != null) {
                getAct().act(new DriveAerial(0, 0, 0, 0, false));
            }
        }

        Destinations.removeDestination(this);
    }
}