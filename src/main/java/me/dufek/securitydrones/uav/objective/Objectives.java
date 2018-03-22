package me.dufek.securitydrones.uav.objective;

import me.dufek.securitydrones.uav.objective.Objective;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import me.dufek.securitydrones.uav.UAV;
import me.dufek.securitydrones.uav.objective.flyto.FlyTo;

/**
 * Data structure for keeping objectives. It works like a queue.
 *
 * @author Jan Dufek
 */
public class Objectives {

    /**
     * List of objectives.
     */
    private final List<Objective> objectives;

    /**
     * UAV reference.
     */
    private final UAV uav;

    /**
     * Initialization of data structures.
     *
     * @param uav
     */
    public Objectives(UAV uav) {
        objectives = Collections.synchronizedList(new ArrayList<Objective>());
        this.uav = uav;
    }

    /**
     * Assign a new objective.
     *
     * @param objective Objective.
     */
    public void assign(Objective objective) {
        objective.setUAV(uav);

        synchronized (objectives) {
            objectives.add(objective);
        }
    }

    /**
     * Assign a new high priority objective. High priority objective is going to
     * the beginning of the queue.
     *
     * @param objective High priority objective.
     */
    public void assignHighPriority(Objective objective) {
        objective.setUAV(uav);
        objective.setHightPriority();

        synchronized (objectives) {
            objectives.add(0, objective);
        }

        uav.refreshObjectives();
    }

    /**
     * Satisfy particular objective. It means that given objective was
     * completed.
     *
     * @param objective Objective.
     */
    public void satisfy(Objective objective) {
        synchronized (objectives) {
            objectives.remove(objective);
        }
    }

    /**
     * Satisfy the last objective.
     */
    public void satisfyLast() {
        synchronized (objectives) {
            objectives.remove(0);
        }
    }

    /**
     * Get next objective.
     * 
     * @return Next objective.
     */
    public Objective getNext() {
        return objectives.get(0);
    }

    public boolean isAssign() {
        return !objectives.isEmpty();
    }

    public boolean isAssign(Class objectiveClass) {
        boolean assigned = false;

        synchronized (objectives) {
            for (Objective objective : objectives) {
                if (objective.getClass() == objectiveClass) {
                    assigned = true;
                    break;
                }
            }
        }

        return assigned;
    }

    public int getNumberOfObjectives() {
        return objectives.size();
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<html>");

        synchronized (objectives) {
            for (Objective objective : objectives) {
                stringBuilder.append("<p>").append(objective.toString()).append("</p>");
            }
        }

        stringBuilder.append("</html>");
        return stringBuilder.toString();
    }

    public String toString(int numberOfObjectives) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<html>");

        int counter = 0;

        synchronized (objectives) {
            for (Objective objective : objectives) {
                if (counter == numberOfObjectives) {
                    break;
                }

                counter++;
                stringBuilder.append("<p>").append(objective.toString()).append("</p>");
            }
        }

        int remaining = objectives.size() - numberOfObjectives;
        if (remaining > 0) {
            stringBuilder.append("<p>").append(remaining).append(" more...").append("</p>");
        }

        stringBuilder.append("</html>");
        return stringBuilder.toString();
    }

    public void cancelObjectives() {
        ArrayList<Objective> objectivesToRemove = new ArrayList<Objective>();

        synchronized (objectives) {
            for (Objective objective : objectives) {
                if (objective instanceof FlyTo && !objective.isHightPriority()) {
                    objectivesToRemove.add(objective);
                }
            }

            objectives.removeAll(objectivesToRemove);
        }
    }
}
