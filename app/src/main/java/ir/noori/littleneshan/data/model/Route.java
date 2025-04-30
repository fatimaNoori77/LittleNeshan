package ir.noori.littleneshan.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Route {
    @SerializedName("overview_polyline")
    private OverviewPolyline overviewPolyline;

    @SerializedName("legs")
    private List<Leg> legs;

    public OverviewPolyline getOverviewPolyline() {
        return overviewPolyline;
    }

    public List<Leg> getLegs() {
        return legs;
    }
}
