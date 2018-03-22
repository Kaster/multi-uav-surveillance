package me.dufek.securitydrones.changes;

/**
 * One change made on object. It consists of one object and one action. It is
 * used by GUI's undo and redo functions.
 *
 * @author Jan Dufek
 */
public class Change {

    /**
     * Object (UAV, charging station, area).
     */
    private Object object;

    /**
     * Action made with this object (addition, deletion).
     */
    private Action action;

    public Change(Object object, Action action) {
        this.object = object;
        this.action = action;
    }

    public Object getObject() {
        return this.object;
    }

    public boolean isAdd() {
        return this.action == Action.ADD;
    }

    public boolean isDelete() {
        return this.action == Action.DELETE;
    }

    public Action getAction() {
        return this.action;
    }

    @Override
    public String toString() {
        return object.toString() + " " + action.toString();
    }
}
