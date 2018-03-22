package me.dufek.securitydrones.uav.objective.flyto;

/**
 * This enum represent the state of fly to objective.
 *
 * @author Jan Dufek
 */
public enum FlyToState {

    /**
     * Default state.
     */
    DEFAULT,
    /**
     * In this state UAV is avoiding obstacles.
     */
    AVOIDING_OBSTACLE,
    /**
     * In this state UAV is trying to get away from obstacle.
     */
    GOING_AWAY_FROM_OBSTACLE;

    /**
     * Gets next state.
     * 
     * @param currentState Current state.
     * @return Next state.
     */
    public static FlyToState getNextState(FlyToState currentState) {
        switch (currentState) {
            case DEFAULT:
                return DEFAULT;
            case AVOIDING_OBSTACLE:
                return GOING_AWAY_FROM_OBSTACLE;
            default:
                return DEFAULT;
        }
    }
}
