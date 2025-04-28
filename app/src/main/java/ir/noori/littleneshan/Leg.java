package ir.noori.littleneshan;

import com.google.gson.annotations.SerializedName;

import org.neshan.common.model.Distance;

import java.util.ArrayList;

public class Leg {
    @SerializedName("summary")
    private String summary;

    @SerializedName("distance")
    private Distance distance;

    @SerializedName("duration")
    private Duration duration;

    @SerializedName("steps")
    private ArrayList<Step> steps;

    public String getSummary() {
        return summary;
    }

    public Distance getDistance() {
        return distance;
    }

    public Duration getDuration() {
        return duration;
    }

    public ArrayList<Step> getSteps() {
        return steps;
    }
}
