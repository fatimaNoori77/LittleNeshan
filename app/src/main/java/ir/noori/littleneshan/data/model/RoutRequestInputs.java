package ir.noori.littleneshan.data.model;

public class RoutRequestInputs {
    private String type;
    private String origin;
    private String destination;
    private String waypoints; // Optional
    private Boolean avoidTrafficZone; // Optional
    private Boolean avoidOddEvenZone; // Optional
    private Boolean alternative; // Optional
    private Integer bearing; // Optional

    public RoutRequestInputs(String type, String origin, String destination) {
        this.type = type;
        this.origin = origin;
        this.destination = destination;
    }

    // Getters
    public String getType() {
        return type;
    }

    public String getOrigin() {
        return origin;
    }

    public String getDestination() {
        return destination;
    }

    public String getWaypoints() {
        return waypoints;
    }

    public Boolean getAvoidTrafficZone() {
        return avoidTrafficZone;
    }

    public Boolean getAvoidOddEvenZone() {
        return avoidOddEvenZone;
    }

    public Boolean getAlternative() {
        return alternative;
    }

    public Integer getBearing() {
        return bearing;
    }

    public void setWaypoints(String waypoints) {
        this.waypoints = waypoints;
    }

    public void setAvoidTrafficZone(Boolean avoidTrafficZone) {
        this.avoidTrafficZone = avoidTrafficZone;
    }

    public void setAvoidOddEvenZone(Boolean avoidOddEvenZone) {
        this.avoidOddEvenZone = avoidOddEvenZone;
    }

    public void setAlternative(Boolean alternative) {
        this.alternative = alternative;
    }

    public void setBearing(Integer bearing) {
        this.bearing = bearing;
    }
}
