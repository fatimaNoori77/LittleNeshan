package ir.noori.littleneshan;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Step {
    @SerializedName("name")
    private String name;

    @SerializedName("instruction")
    private String instruction;

    @SerializedName("bearing_after")
    private int bearingAfter;

    @SerializedName("type")
    private String type;

    @SerializedName("modifier")
    private String modifier;

    @SerializedName("distance")
    private Distance distance;

    @SerializedName("duration")
    private Duration duration;

    @SerializedName("polyline")
    private String polyline;

    @SerializedName("start_location")
    private List<Double> startLocation;

    @SerializedName("exit")
    private Integer exit;

    public String getName() {
        return name;
    }

    public String getInstruction() {
        return instruction;
    }

    public int getBearingAfter() {
        return bearingAfter;
    }

    public String getType() {
        return type;
    }

    public String getModifier() {
        return modifier;
    }

    public Distance getDistance() {
        return distance;
    }

    public Duration getDuration() {
        return duration;
    }

    public String getPolyline() {
        return polyline;
    }

    public List<Double> getStartLocation() {
        return startLocation;
    }

    public Integer getExit() {
        return exit;
    }
}
