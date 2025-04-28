package ir.noori.littleneshan;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface ApiService {

    @GET("v4/direction")
    Call<RouteResponse> getRoute(
            @Query("type") String type,
            @Query("origin") String origin,
            @Query("destination") String destination,
            @Query("waypoints") String waypoints,
            @Query("avoidTrafficZone") Boolean avoidTrafficZone,
            @Query("avoidOddEvenZone") Boolean avoidOddEvenZone,
            @Query("alternative") Boolean alternative,
            @Query("bearing") Integer bearing,
            @Header("Api-Key") String apiKey
    );

    @GET("v1/search")
    Call<SearchResponse> searchAddress(
            @Header("Api-Key") String apiKey,
            @Query("term") String term,
            @Query("lat") double lat,
            @Query("lng") double lng
    );

}
