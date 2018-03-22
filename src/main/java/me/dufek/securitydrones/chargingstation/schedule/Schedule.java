package me.dufek.securitydrones.chargingstation.schedule;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import me.dufek.securitydrones.uav.charging.ChargingTask;

/**
 * Schedule is used by charging stations in order to schedule charging tasks.
 *
 * @author Jan Dufek
 */
public class Schedule implements Serializable {

    /**
     * Schedule is represented as a list of tasks.
     */
    private ArrayList<Task> schedule;

    public Schedule() {
        schedule = new ArrayList<Task>();
    }

    public void registerTask(Task task) {
//        System.out.println("Schedule Before:");
//        System.out.println(this.toString());
//        System.out.println("Task to Register: " + task.toString());
        if (scheduleConflict(task)) {
            throw new IllegalStateException("Can not register the task because it is in conflict with schedule.");
        }

        schedule.add(task);
//        System.out.println("Schedule After:");
//        System.out.println(this.toString());
    }

    /**
     * Find out if there is any conflict of potential task with tasks already in
     * the schedule.
     *
     * @param potentialTask Potential task
     * @return True if there is a conflict, false otherwise.
     */
    public boolean scheduleConflict(Task potentialTask) {
        for (Task task : schedule) {
            if (taskConflict(potentialTask, task)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Checks if two tasks are in conflict.
     *
     * @param task1 Task 1.
     * @param task2 Task 2.
     * @return True if there is a conflict and false otherwise.
     */
    private boolean taskConflict(Task task1, Task task2) {
        boolean conflict = true;

        if (task1.getEnd() <= task2.getStart()) {
            conflict = false;
        } else if (task2.getEnd() <= task1.getStart()) {
            conflict = false;
        }

        return conflict;
    }

    public double getStart() {
        Task earliestTask = Collections.min(schedule, new TaskStartComparator());

        return earliestTask.getStart();
    }

    public Task getEarliestTask() {
        if (schedule.isEmpty()) {
            return null;
        }

        Task earliestTask = Collections.min(schedule, new TaskStartComparator());

        return earliestTask;
    }

    /**
     * Forcing the task to register will require to shift the schedule.
     * 
     * @param chargingTask Charging Task
     */
    public void forceRegisterTask(Task chargingTask) {
        ArrayList<Task> conflictTasks = getConflictTasks(chargingTask);

        double requiredShift = getShift(chargingTask, conflictTasks);

        moveAllTasksBeforeConflict(conflictTasks, requiredShift);
        registerTask(chargingTask);
    }

    /**
     * Get all the tasks which are in conflict with given task.
     * 
     * @param chargingTask Charging task.
     * @return List of conflict tasks.
     */
    public ArrayList<Task> getConflictTasks(Task chargingTask) {
        ArrayList<Task> conflictTasks = new ArrayList<Task>();

        for (Task task : schedule) {
            if (taskConflict(chargingTask, task)) {
                conflictTasks.add(task);
            }
        }

        if (conflictTasks.isEmpty()) {
            throw new IllegalStateException("There is no conflict task!");
        }

        Collections.sort(conflictTasks, new TaskStartComparator());

        return conflictTasks;
    }

    /**
     * Find the time shift required to shift the schedule.
     * 
     * @param chargingTask Charging task.
     * @param conflictTasks Conflict charging tasks.
     * @return Shift.
     */
    public double getShift(Task chargingTask, ArrayList<Task> conflictTasks) {
        Task latestConflictTask = conflictTasks.get(conflictTasks.size() - 1);

        double shift = latestConflictTask.getEnd() - chargingTask.getStart();

        return shift;
    }

    /**
     * This methods moves all the conflict tasks using the shift.
     * 
     * @param conflictTasks List of conflict tasks.
     * @param requiredShift Shift.
     */
    private void moveAllTasksBeforeConflict(ArrayList<Task> conflictTasks, double requiredShift) {
        Collections.sort(schedule, new TaskStartComparator());

        Task latestTaskToBeMoved = conflictTasks.get(conflictTasks.size() - 1);

        int indexOfLatestTaskToBeMoved = schedule.indexOf(latestTaskToBeMoved);

        for (int i = 0; i <= indexOfLatestTaskToBeMoved; i++) {
            schedule.get(i).move(requiredShift);
        }
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        for (Task task : schedule) {
            stringBuilder.append(task.toString());
        }

        return stringBuilder.toString();
    }

    public ArrayList<Task> getChronologicalSchedule() {
        Collections.sort(schedule, new TaskStartComparator());
        return schedule;
    }

    public int getNumberOfTasks() {
        return schedule.size();
    }

    public int getNumberOfIncompleteTasks() {
        int numberOfIncompleteTasks = 0;

        for (Task task : schedule) {
            if (!task.isCompleted()) {
                numberOfIncompleteTasks++;
            }
        }
        return numberOfIncompleteTasks;
    }

    public void removeFinishedTasks() {
        ArrayList<Task> finishedTasks = new ArrayList<Task>();

        for (Task task : schedule) {
            if (task.isFinished()) {
                finishedTasks.add(task);
            }
        }

        schedule.removeAll(finishedTasks);
    }

    public void cancel(ChargingTask chargingTask) {
        this.schedule.remove(chargingTask.getTask());
    }
}
