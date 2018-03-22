package me.dufek.securitydrones.area.status;

import me.dufek.securitydrones.area.Area;

/**
 * Status of area. It represents current status which is then used in the bottom
 * bar.
 *
 * @author Jan Dufek
 */
public class Status {

    private final Area area;
    private String status;

    public Status(Area area, String status) {
        this.area = area;
        this.status = status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusMessage() {
        return this.status;
    }

    public Area getArea() {
        return this.area;
    }

    public String getAreaName() {
        return area.getName();
    }
}
