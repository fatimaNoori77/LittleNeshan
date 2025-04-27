package ir.noori.littleneshan;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RouteResponse {
    @SerializedName("routes")
    private List<Route> routes;

    public List<Route> getRoutes() {
        return routes;
    }
}
