package me.dufek.securitydrones.uav.objective.flyto;

import cz.cuni.amis.pogamut.base3d.worldview.object.Location;
import cz.cuni.amis.pogamut.base3d.worldview.object.Rotation;
import cz.cuni.amis.pogamut.usar2004.communication.messages.datatypes.SensorMount;
import cz.cuni.amis.pogamut.usar2004.communication.messages.usarcommands.DriveAerial;
import cz.cuni.amis.pogamut.usar2004.samples.AirScanner.RiskLevel;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import me.dufek.securitydrones.conversion.MapUAVConversion;
import me.dufek.securitydrones.conversion.NumberConversion;
import me.dufek.securitydrones.logger.Logger;
import me.dufek.securitydrones.main.Global;
import me.dufek.securitydrones.preferences.UserPreferences;
import me.dufek.securitydrones.uav.UAV;
import me.dufek.securitydrones.uav.UAVs;
import me.dufek.securitydrones.uav.destination.Destinations;
import me.dufek.securitydrones.uav.objective.Objective;
import me.dufek.securitydrones.uav.status.UAVsStatus;
import me.dufek.securitydrones.utilities.Angle;
import me.dufek.securitydrones.utilities.Distance;

/**
 * This objective is responsible for getting UAV safely from one location to
 * another. It also features collision avoidance.
 *
 * @author Jan Dufek
 */
public class FlyTo extends Objective {

    /**
     * The offset of the laser if UAV is tilted.
     */
    private int laserOffset = 0;

    /**
     * Should UAV continue with low risk of collision?
     */
    private boolean continueWithLowRisk = true;

    /**
     * Location of target in map coordinate system.
     */
    private Location targetLocationMapCoordinates = null;

    /**
     * Location of target in UAV coordinate system.
     */
    private Location targetLocationUAVCoordinates = null;

    /**
     * Initialize by giving information where to fly.
     *
     * @param locationMapCoordinates Location of target in map coordinate
     * system.
     * @param destinationDescription String description of destination used in
     * GUI.
     */
    public FlyTo(Location locationMapCoordinates, String destinationDescription) {
        super();

        this.targetLocationMapCoordinates = locationMapCoordinates;
        this.name = "Going to the " + destinationDescription + " at " + locationMapCoordinates.toString();
    }

    /**
     * Logic method is called periodically. It is responsible for movement of
     * the vehicle and keeping elevation.
     */
    @Override
    public void logic() {
        super.logic();

        if (targetLocationUAVCoordinates == null) {
            this.targetLocationUAVCoordinates = MapUAVConversion.mapToUAV(targetLocationMapCoordinates, super.uav.getStartLocation());
        }

        super.uav.nextLocation = targetLocationUAVCoordinates;

        move();

        if (Distance.LocationEquals2D(super.uav.nextLocation, super.uav.actualLocation, Preferences.DESTINATION_REACHED_THRESHOLD)) {
            Destinations.removeDestination(super.uav, MapUAVConversion.UAVToMap(super.uav.nextLocation, super.uav.getStartLocation()));
            Logger.log(uav.getName() + ": Destination reached at " + MapUAVConversion.UAVToMap(super.uav.actualLocation, super.uav.getStartLocation()).toString() + ".");
            UAVsStatus.setStatus(uav, "Destination reached at " + MapUAVConversion.UAVToMap(super.uav.actualLocation, super.uav.getStartLocation()).toString() + ".");

            if (Global.algorithm != null) {
                super.uav.getAct().act(new DriveAerial(0, 0, 0, 0, false));
            }

            super.satisfy();
        }
    }

    /**
     * Main control block that computes next direction based on altitude,
     * collision risk level and computes next goal if needed. Also it unstucks
     * the robot if necessary.
     */
    private void move() {
        checkAltitudeFromLaser();

        if (checkSonars() == RiskLevel.HIGHRISK) {
            return;
        }

        if (avoidOtherUAVs()) {
            return;
        }

        approachNext();

        if (hasReachedTarget(super.uav.actualLocation, super.uav.nextLocation)) {
            super.uav.flyingState = FlyToState.getNextState(super.uav.flyingState);
            computeNext(super.uav.flyingState);
        }

        carryOnIfStuck();
    }

    /**
     * This method is the main part of dynamic collision avoidance system. It is
     * responsible for checking other UAVs and avoid them if necessary.
     *
     * @return
     */
    private boolean avoidOtherUAVs() {
        Double avoidingUAVAngle = avoidingUAV();
        if (avoidingUAVAngle != null) {
            CollisionSector collisionSector = getCollisionSector(avoidingUAVAngle);
            Point2D velocity = Preferences.UAV_COLLISION_ACTION.get(collisionSector);

            double minimumDistance = Double.MAX_VALUE;

            for (UAV uav : UAVs.listOfUAVs) {
                double distance = Location.getDistance2D(super.uav.getActualLocationMapCoordinates(), uav.getActualLocationMapCoordinates());

                if (distance < minimumDistance) {
                    minimumDistance = distance;
                }
            }

            // If UAV is closer to target than the distance to nearest UAV, it is not necessary to avoid
            if (minimumDistance > Location.getDistance2D(super.uav.actualLocation, super.uav.nextLocation)) {
                return false;
            }

            abortAim(velocity);
            super.uav.noRiskCount = 0;
            super.uav.riskCount++;
            return true;
        }
        return false;
    }

    private Double avoidingUAV() {
        ArrayList<Double> angles = new ArrayList<Double>();

        for (UAV otherUAV : UAVs.listOfUAVs) {
            if (otherUAV == super.uav) {
                continue;
            }

            if (otherUAV == null) {
                continue;
            }

            Double angle = super.uav.getAngleTo(otherUAV.getLocationMapCoordinates());

            if (similarAltitude(otherUAV)) {
                if (near(otherUAV, angle)) {
                    angles.add(angle);
                }
            }
        }
        if (angles.isEmpty()) {
            return null;
        } else {
            return angleAverage(angles);
        }
    }

    /**
     * Average of list of angles.
     *
     * @param angles Angles.
     * @return Average of angles.
     */
    private double angleAverage(ArrayList<Double> angles) {
        double x = 0;
        double y = 0;

        for (Double angle : angles) {
            x += Math.cos(Math.toRadians(angle));
            y += Math.sin(Math.toRadians(angle));
        }

        x /= angles.size();
        y /= angles.size();

        double averageAngle = Math.toDegrees(Math.atan2(y, x));

        return averageAngle;
    }

    /**
     * Gets particular collision sector from the angle.
     *
     * @param angle Angle
     * @return Collision sector.
     */
    private CollisionSector getCollisionSector(double angle) {
        if (-180 <= angle && angle < -171) {
            return CollisionSector.BACK;
        } else if (-171 <= angle && angle < -153) {
            return CollisionSector.LEFT_9;
        } else if (-153 <= angle && angle < -135) {
            return CollisionSector.LEFT_8;
        } else if (-135 <= angle && angle < -117) {
            return CollisionSector.LEFT_7;
        } else if (-117 <= angle && angle < -99) {
            return CollisionSector.LEFT_6;
        } else if (-99 <= angle && angle < -81) {
            return CollisionSector.LEFT_5;
        } else if (-81 <= angle && angle < -63) {
            return CollisionSector.LEFT_4;
        } else if (-63 <= angle && angle < -45) {
            return CollisionSector.LEFT_3;
        } else if (-45 <= angle && angle < -27) {
            return CollisionSector.LEFT_2;
        } else if (-27 <= angle && angle < -9) {
            return CollisionSector.LEFT_1;
        } else if (-9 <= angle && angle < 9) {
            return CollisionSector.FRONT;
        } else if (9 <= angle && angle < 27) {
            return CollisionSector.RIGHT_1;
        } else if (27 <= angle && angle < 45) {
            return CollisionSector.RIGHT_2;
        } else if (45 <= angle && angle < 63) {
            return CollisionSector.RIGHT_3;
        } else if (63 <= angle && angle < 81) {
            return CollisionSector.RIGHT_4;
        } else if (81 <= angle && angle < 99) {
            return CollisionSector.RIGHT_5;
        } else if (99 <= angle && angle < 117) {
            return CollisionSector.RIGHT_6;
        } else if (117 <= angle && angle < 135) {
            return CollisionSector.RIGHT_7;
        } else if (135 <= angle && angle < 153) {
            return CollisionSector.RIGHT_8;
        } else if (153 <= angle && angle < 171) {
            return CollisionSector.RIGHT_9;
        } else if (171 <= angle && angle < 180) {
            return CollisionSector.BACK;
        } else {
            throw new IllegalStateException("No collision quadrant for given angle.");
        }
    }

    /**
     * Check if this UAV is near particular UAV. The threshold depends on the
     * angle.
     *
     * @param uav Other UAV
     * @param angle Angle to that UAV
     * @return
     */
    private boolean near(UAV uav, Double angle) {
        if (angle == null) {
            return false;
        }

//        Location thisLocation = super.uav.getActualLocationMapCoordinates();
//        Location uav2Location = uav.getActualLocationMapCoordinates();
        Location thisLocation = super.uav.getRealLocationMapCoordinates();
        Location uav2Location = uav.getRealLocationMapCoordinates();

        if (thisLocation == null || uav2Location == null) {
            return false;
        } else if (Location.getDistance2D(thisLocation, uav2Location) < Preferences.UAV_COLLISION_DANGER_DISTANCE_THRESHOLD.get(getCollisionSector(angle))) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Checks if this UAV has a similar altitude to other given UAV.
     * 
     * @param uav UAV
     * @return True if the altitude is similar, false otherwise.
     */
    private boolean similarAltitude(UAV uav) {
        double thisAltitude = super.uav.getLocationMapCoordinates().getZ();

        if (uav == null || uav.getLocationMapCoordinates() == null) {
            return false;
        }
        double uavAltitude = uav.getLocationMapCoordinates().getZ();

        double altitudeDifference = Math.abs(thisAltitude - uavAltitude);

        return altitudeDifference < UserPreferences.preferences.COLLISION_DETECTION_ALTITUDE_DIFFERENCE_THRESHOLD;
    }

    /**
     * Computes the average height using laser rays from range scanner that aim
     * directly below the robot.
     */
    private void checkAltitudeFromLaser() {
        laserOffset = NumberConversion.toInteger((super.uav.actualRotation.roll >= Math.PI) ? (360 - (super.uav.actualRotation.roll * 180 / Math.PI)) : (-super.uav.actualRotation.roll * 180 / Math.PI));
        super.uav.actualAltitude = super.uav.laser.getNMidAvg(me.dufek.securitydrones.uav.Preferences.NUMBER_OF_LASER_RAYS, laserOffset);
    }

    /**
     * Checks the sonar ring for possible collision threat and also issues stuck
     * count represents.
     *
     * @return returns LOW, HIGH or NO RISK based on Sonar sensor reading.
     */
    private RiskLevel checkSonars() {
        RiskLevel worstThread = getRiskLevel(super.uav.sonar.getRanges());

        // If it is closer to target than the worst risk level than return no risk because UAV is not in danger
        if (worstThread != RiskLevel.NORISK && super.uav.greatestRiskSonar.getValue() > Location.getDistance2D(super.uav.actualLocation, super.uav.nextLocation)) {
            return RiskLevel.NORISK;
        }

        switch (worstThread) {
            case NORISK:
                super.uav.noRiskCount++;
                if (super.uav.noRiskCount > Preferences.RELEASE_DIVERSION_NO_RISK_COUNT) {
                    tryReleaseDiversion();
                }
                if (super.uav.noRiskCount > Preferences.CANCEL_RISK_COUNT_THRESHOLD) {
                    super.uav.riskCount = 0;
                }
                break;
            case LOWRISK:
                if (continueWithLowRisk) {
                    computeDiversion(super.uav.sonar.getRanges());
                }
                super.uav.noRiskCount = 0;
                super.uav.riskCount++;
                break;
            case HIGHRISK:
                String key = super.uav.greatestRiskSonar.getKey();
                Point2D velocity = Preferences.HIGH_RISK_ACTION.get(key);
                abortAim(velocity);
                super.uav.noRiskCount = 0;
                super.uav.riskCount++;
                break;
        }
        return worstThread;
    }

    /**
     * System of setting lateral, linear, altitude and rotational velocities
     * takes place here. When it is close to goal it stops the forward movement
     * for better dexterity.
     */
    private void approachNext() {
        double rotationalVelocity;
        double altitudeVelocity;
        double linearVelocity = super.uav.maxLinearVelocity * super.uav.linearVelocity;

        double targetAngleDifference = getTargetAngle(false);

        rotationalVelocity = Math.signum(-targetAngleDifference) * Math.min(Math.abs((double) (super.uav.maxRotationalVelocity * (-targetAngleDifference / 90))), this.uav.rotationalVelocity);

//        if (super.uav.actualLocation.z < UserPreferences.preferences.SEA_LEVEL + 1 && super.uav.actualAltitude > super.uav.flightAltitude) {
//            altitudeVelocity = (double) (super.uav.maxAltitudeVelocity * (UserPreferences.preferences.SEA_LEVEL - super.uav.actualLocation.z) / 6);
        if (super.uav.actualAltitude > super.uav.flightAltitude) {
            altitudeVelocity = (double) (super.uav.maxAltitudeVelocity * (super.uav.flightAltitude - super.uav.actualAltitude) / 6);
        } else {
            altitudeVelocity = (double) (super.uav.maxAltitudeVelocity * (super.uav.flightAltitude - super.uav.actualAltitude) / 5);
        }

        if (super.uav.flyingState == FlyToState.AVOIDING_OBSTACLE || Location.getDistance2D(super.uav.actualLocation, super.uav.nextLocation) < 2.7) {
            linearVelocity = (Math.abs(rotationalVelocity) >= this.uav.rotationalVelocity) ? 0 : linearVelocity;
        } else {
            linearVelocity = (Math.abs(rotationalVelocity) >= this.uav.rotationalVelocity) ? linearVelocity * 3 / 5 : linearVelocity;
        }

        continueWithLowRisk = true;

        if (Math.abs(rotationalVelocity) >= this.uav.rotationalVelocity) {
            super.uav.noRiskCount = Math.max(0, super.uav.noRiskCount - 1);
            if (super.uav.flyingState == FlyToState.AVOIDING_OBSTACLE) {
                continueWithLowRisk = false;
            }
        }

        altitudeVelocity = (Math.abs(rotationalVelocity) >= this.uav.rotationalVelocity) ? 0 : altitudeVelocity;

        // If UAV is near to destination and the angular difference is higher than given threshold, UAV needs to stop linear movement and to rotate directly to the destination. This shold prevent circling around destination.
        if (isNear() && Math.abs(targetAngleDifference) > UserPreferences.preferences.APPROACH_ANGLE_DIFFERENCE_THRESHOLD) {
            linearVelocity = 0;
        }

        if (Global.algorithm != null) {
            super.uav.getAct().act(new DriveAerial(altitudeVelocity, linearVelocity, 0, rotationalVelocity, false));
        }
    }

    /**
     * Checks if UAV is near its next location.
     * 
     * @return True if it is near, false otherwise.
     */
    private boolean isNear() {
        double distance = Location.getDistance2D(super.uav.actualLocation, super.uav.nextLocation);

        return distance < UserPreferences.preferences.IS_NEAR_DESTINATION_DISTANCE_THRESHOLD;
    }

    /**
     * We use minimal deviation constant to allow robot to be just near its
     * goal. The INS noise would cause the robot to spin around when the
     * acceptable distance was too close.
     *
     * @param actual actual position of the robot
     * @param target goal position.
     * @return returns true if the robot is within acceptable distance from its
     * target.
     */
    private boolean hasReachedTarget(Location actual, Location target) {
        if (Location.equal(actual, target, Preferences.DESTINATION_REACHED_THRESHOLD)) {
            return false;
        }
        return true;
    }

    /**
     * If the Robot is avoiding an obstacle for a long time (edge of the world,
     * target location is in the middle of a tree/house etc.), there is probably
     * no point trying any further. Next Location to go after the one currently
     * pursued is computed and given to the robot to chase. The robot simply
     * leaves out its original goal after some time trying to avoid an obstacle.
     */
    private void carryOnIfStuck() {
        if (super.uav.riskCount > Preferences.STUCK_THRESHOLD) {
            if (super.uav.flyingState == FlyToState.AVOIDING_OBSTACLE) {
                tryReleaseDiversion();
            }

            super.uav.flyingState = FlyToState.getNextState(super.uav.flyingState);
            computeNext(super.uav.flyingState);
            super.uav.riskCount = 0;
        }
    }

    /**
     * Gets risk level from each sonar from the Map sonars and returns the
     * greatest risk.
     *
     * @param sonars map acquired from the sensor module
     * @return returns greatest thread to the robot
     */
    private RiskLevel getRiskLevel(Map<String, Double> sonars) {
        List<Double> sonarValues = new LinkedList<Double>();

        for (int i = 0; i < sonars.size(); i++) {
            sonarValues.add(sonars.get(Preferences.SONAR_ORDER[i]));
        }

        RiskLevel globalRisk = RiskLevel.NORISK;

        double riskValue = Preferences.SONAR_INFINITY_DISTANCE_THRESHOLD;

        // Order of entries does not matter at this time
        for (Map.Entry<String, Double> entry : sonars.entrySet()) {
            RiskLevel risk = getRiskLevel(entry);

            // Due to nonequal conditions for each sonar, we have to get the urgency first (greater urgency at one could be further than no urgency at other) and than ask the distance. We want to determine the greatest risk and than the nearest sonar at this risk level.
            if (risk.isGreaterRisk(globalRisk)) {
                globalRisk = risk;
                riskValue = entry.getValue();
                super.uav.greatestRiskSonar = entry;
            } else if (risk == globalRisk) {
                if (entry.getValue() < riskValue) {
                    riskValue = entry.getValue();
                    super.uav.greatestRiskSonar = entry;
                }
            }
        }

        return globalRisk;
    }

    /**
     * This computes next goal or chooses next action according to actual state
     * of the robot.
     *
     * @param state current state of the robot.
     */
    private void computeNext(FlyToState state) {
        switch (state) {
            case DEFAULT:
            case GOING_AWAY_FROM_OBSTACLE:
                tryReleaseDiversion();
                break;
            default:
                throw new IllegalStateException("Unexpected state.");
        }
    }

    /**
     * Triggered by robot being close to an obstacle. Computes the most
     * advantageous direction based on sonar ring. Find out where are the
     * obstacles and compute point next to obstacle. The robot should fit on
     * this spot and should not put itself into HighRisk condition. From the ray
     * which has the lowest value, check seonars to the right and to the left
     * and take weighted average. Choose first free point.
     */
    private void computeDiversion(Map<String, Double> sonars) {
        boolean leftSide = true;

        double leftWeightedMean = 0;
        double rightWeightedMean = 0;

        double leftFittest = 0;
        double rightFittest = 0;

        int leftIndex = 0;
        int rightIndex = 0;

        int greatestRiskIndex = -1;

        for (int i = 0; i < sonars.size(); i++) {
            Double entry = sonars.get(Preferences.SONAR_ORDER[i]);
            if (Preferences.SONAR_ORDER[i].equals(super.uav.greatestRiskSonar.getKey())) {
                greatestRiskIndex = i;
                leftSide = false;
                continue;
            }

            // Computes the odds left and right from the sonar with greatest risk
            if (leftSide) {
                // Get the mean from the second power. It will ensure, that one infinite ray is valued better than six finite.
                leftWeightedMean += entry * entry;

                // We want the closest open direction to the occupied as possible
                if (leftFittest < entry || entry > Preferences.SONAR_INFINITY_DISTANCE_THRESHOLD) {
                    leftFittest = entry;
                    leftIndex = i;
                }
            } else {
                rightWeightedMean += entry * entry;

                // We want the first open direction or the best possible
                if (rightFittest < entry && rightFittest < Preferences.SONAR_INFINITY_DISTANCE_THRESHOLD) {
                    rightFittest = entry;
                    rightIndex = i;
                }
            }
        }
        if (leftWeightedMean < rightWeightedMean) {
            if (rightIndex < 7 && sonars.get(Preferences.SONAR_ORDER[rightIndex - 1]) < 2.5d) {
                RiskLevel righterRisk = getRiskLevel(Preferences.SONAR_ORDER[rightIndex + 2], sonars.get(Preferences.SONAR_ORDER[rightIndex + 2]));
                if (righterRisk == RiskLevel.NORISK) {
                    rightIndex += 2;
                }
            } // If it is not on the side, it takes the second best ray
            else if (rightIndex < 8) { // We need to make sure, that ther is no obstacle at next direction
                RiskLevel righterRisk = getRiskLevel(Preferences.SONAR_ORDER[rightIndex + 1], sonars.get(Preferences.SONAR_ORDER[rightIndex + 1]));
                if (righterRisk == RiskLevel.NORISK) {
                    rightIndex++;
                }
            }
            int targetIndex = getSonarTargetIndex();
            if (targetIndex > rightIndex && targetIndex < 8) {
                RiskLevel targetRiskLevel = getRiskLevel(Preferences.SONAR_ORDER[targetIndex], sonars.get(Preferences.SONAR_ORDER[targetIndex]));
                if (targetRiskLevel == RiskLevel.NORISK) {
                    // No need to change direction
                    rightIndex = targetIndex;
                    tryReleaseDiversion();
                    return;
                }
            }
            setDiversionPoint(rightIndex, rightFittest);
        } else {
            if (leftIndex > 1 && sonars.get(Preferences.SONAR_ORDER[leftIndex + 1]) < 1.5d) {
                RiskLevel lefterRisk = getRiskLevel(Preferences.SONAR_ORDER[leftIndex - 2], sonars.get(Preferences.SONAR_ORDER[leftIndex - 2]));
                if (lefterRisk == RiskLevel.NORISK) {
                    leftIndex -= 2;
                }
            } else if (leftIndex > 0) {
                RiskLevel lefterRisk = getRiskLevel(Preferences.SONAR_ORDER[leftIndex - 1], sonars.get(Preferences.SONAR_ORDER[leftIndex - 1]));
                if (lefterRisk == RiskLevel.NORISK) {
                    leftIndex--;
                }
            }
            int targetIndex = getSonarTargetIndex();
            if (targetIndex < leftIndex && targetIndex > 0) {
                RiskLevel targetRiskLevel = getRiskLevel(Preferences.SONAR_ORDER[targetIndex], sonars.get(Preferences.SONAR_ORDER[targetIndex]));
                if (targetRiskLevel == RiskLevel.NORISK) {
                    leftIndex = targetIndex;
                    tryReleaseDiversion();
                    return;
                }
            }
            setDiversionPoint(leftIndex, leftFittest);
        }
    }

    /**
     * Sets a diversion point in a direction of the fittest sonar based on its
     * index.
     *
     * @param index index of the sonar sensor.
     * @param fittest fittest value obtained from sonar sensor.
     */
    private void setDiversionPoint(int index, double fittest) {
        Rotation sonarRotation = super.uav.sonarGeo.get(index).getOrientation();

        // If its left side sonar give it +0.3 rad to go further from an obstacle. if it is right lets give it -0.3 rad for the same reason
        double angle = Math.PI / 2 - (sonarRotation.yaw + super.uav.actualRotation.yaw) + ((super.uav.greatestRiskSonar.getKey().charAt(0) == 'L') ? -0.3 : (super.uav.greatestRiskSonar.getKey().charAt(0) == 'R') ? 0.3 : 0);
        Location actualDiversion = new Location(-Math.sin(angle) * fittest, -Math.cos(angle) * fittest);
        Location diversion = super.uav.actualLocation.add(actualDiversion);

        if (super.uav.flyingState != FlyToState.AVOIDING_OBSTACLE) {
            super.uav.temporaryState = super.uav.flyingState;
            super.uav.temporaryNextLocation = super.uav.nextLocation;
        }

        super.uav.flyingState = FlyToState.AVOIDING_OBSTACLE;
        super.uav.nextLocation = diversion;
    }

    /**
     * If the robot faces the goal this gets the index of sonar that points
     * closest to this direction.
     *
     * @return Returns a sonar index, that points to goal direction.
     */
    private int getSonarTargetIndex() {
        int sonarIndex = -1;
        int count = 0;

        double targetAngle = getTargetAngle(true) / 180 * Math.PI;

        double minDiversion = Preferences.SONAR_MINIMUM_TARGET_DIVERSION;

        for (SensorMount sensorMount : super.uav.sonarGeo) {
            if (minDiversion > Math.abs(targetAngle - sensorMount.getOrientation().yaw)) {
                minDiversion = Math.abs(targetAngle - sensorMount.getOrientation().yaw);
                sonarIndex = count;
            }
            count++;
        }
        return sonarIndex;
    }

    /**
     * Triggered by robot being too close to an obstacle. It dodges an obstacle
     * in a wince maner.
     */
    private void abortAim(Point2D velocity) {

        if (Global.algorithm != null) {
            super.uav.getAct().act(new DriveAerial(0, velocity.getX() * Preferences.OBSTACLE_AVOIDANCE_VELOCITY_COEFICIENT, velocity.getY() * Preferences.OBSTACLE_AVOIDANCE_VELOCITY_COEFICIENT, 0, true));
        }
    }

    /**
     * Decides the risk individually based on the name of the sonar and static
     * arrays with Low Risk and High Risk Thresholds. Side sonars have grater
     * tollerance than the front ones.
     *
     * @param sonar entry acquired from the sonar sensor Map.
     * @return returns the risk based on the name of the sonar.
     */
    private RiskLevel getRiskLevel(Map.Entry<String, Double> sonar) {
        return (sonar.getValue() < Preferences.LOW_RISK_DISTANCE.get(sonar.getKey())) ? (sonar.getValue() < Preferences.HIGHT_RISK_DISTANCE.get(sonar.getKey())) ? RiskLevel.HIGHRISK : RiskLevel.LOWRISK : RiskLevel.NORISK;
    }

    /**
     * Gets risk level from particular sonar.
     *
     * @param sonar name of the sonar
     * @param value value of the sonar sensor
     * @return returns risk level of input sonar.
     */
    private RiskLevel getRiskLevel(String sonar, double value) {
        return (value < Preferences.LOW_RISK_DISTANCE.get(sonar)) ? (value < Preferences.HIGHT_RISK_DISTANCE.get(sonar)) ? RiskLevel.HIGHRISK : RiskLevel.LOWRISK : RiskLevel.NORISK;
    }

    /**
     * If the robot is avoiding and is presumably stuck, this methods proceeds
     * to next goal.
     */
    private void tryReleaseDiversion() {
        if (super.uav.flyingState == FlyToState.AVOIDING_OBSTACLE || super.uav.flyingState == FlyToState.GOING_AWAY_FROM_OBSTACLE) {
            super.uav.nextLocation = super.uav.temporaryNextLocation;
            super.uav.temporaryNextLocation = null;
            super.uav.flyingState = super.uav.temporaryState;
        }
    }

    /**
     * *
     * Computes an angle in degrees of the aircraft relative to next Location
     * that the robot is heading to.
     *
     * @param actual Tells the method whether it should be considering temporary
     * next Location (true) or not (false). Temporary next Location is greater
     * goal that the robot is not pursuing when avoiding an obstacle.
     * @return Returns angle in degrees relative to next Location or temp. Next
     * Location based on <B>actual</B>.
     */
    private double getTargetAngle(boolean actual) {
        double differenceX;
        double differenceY;

        if (actual && super.uav.flyingState == FlyToState.AVOIDING_OBSTACLE && super.uav.temporaryNextLocation != null) {
            differenceX = super.uav.temporaryNextLocation.x - super.uav.actualLocation.x;
            differenceY = super.uav.temporaryNextLocation.y - super.uav.actualLocation.y;
        } else {
            differenceX = super.uav.nextLocation.x - super.uav.actualLocation.x;
            differenceY = super.uav.nextLocation.y - super.uav.actualLocation.y;
        }

        double distance = Math.sqrt(differenceX * differenceX + differenceY * differenceY);

        double sin = differenceY / distance;
        double cos = differenceX / distance;

        double requiredAngle = (Angle.getAngle(sin, cos) + 180) % 360;
        double actualAngle = super.uav.actualRotation.yaw * 180 / Math.PI;

        double difference = actualAngle - requiredAngle;

        if (difference > 180) {
            difference = difference - 360;
        } else if (difference < -180) {
            difference = difference + 360;
        }

        return difference;
    }
}
