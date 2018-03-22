package me.dufek.securitydrones.chargingstation.schedule;

import java.util.Comparator;

/**
 *
 * @author Jan Dufek
 */
public class TaskStartComparator implements Comparator<Task> {

    @Override
    public int compare(Task task1, Task task2) {
        return Double.compare(task1.getStart(), task2.getStart());
    }
}
