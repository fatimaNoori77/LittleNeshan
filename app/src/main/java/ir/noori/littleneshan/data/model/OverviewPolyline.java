package ir.noori.littleneshan.data.model;

import com.google.gson.annotations.SerializedName;

public class OverviewPolyline {
    @SerializedName("points")
    private String points;

    public String getPoints() {
        return points;
    }
}
