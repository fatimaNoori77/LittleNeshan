package ir.noori.littleneshan;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DirectionRepository {
    private final ApiService apiService;

    public DirectionRepository() {
        this.apiService = ApiClient.getClient().create(ApiService.class);
    }

    public LiveData<RouteResponse> getRoute(RoutRequestInputs inputs, String apiKey){
        MutableLiveData<RouteResponse> route = new MutableLiveData<>();
        apiService.getRoute(
                inputs.getType(),
                inputs.getOrigin(),
                inputs.getDestination(),
                inputs.getWaypoints(),
                inputs.getAvoidTrafficZone(),
                inputs.getAvoidOddEvenZone(),
                inputs.getAlternative(),
                inputs.getBearing(),
                apiKey).enqueue(new Callback<RouteResponse>() {
            @Override
            public void onResponse(Call<RouteResponse> call, Response<RouteResponse> response) {
                // handle this = {"status":"ERROR","code":200,"message":"No Route Found!"}
                if (response.isSuccessful()) {
                    route.setValue(response.body());
                } else {
                    route.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<RouteResponse> call, Throwable t) {
                route.setValue(null);
            }
        });
        return route;
    }

}
