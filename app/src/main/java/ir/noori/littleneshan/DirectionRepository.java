package ir.noori.littleneshan;

import androidx.lifecycle.MutableLiveData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DirectionRepository {
    private final ApiService apiService;

    public DirectionRepository() {
        this.apiService = ApiClient.getClient().create(ApiService.class);
    }

    public  MutableLiveData<ApiResult<RouteResponse>> getRoute(RoutRequestInputs inputs, String apiKey){
        MutableLiveData<ApiResult<RouteResponse>> route = new MutableLiveData<>();
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
                if (response.isSuccessful() && response.body() != null) {
                    route.setValue(new ApiResult.Success<>(response.body()));
                } else {
                    route.setValue(new ApiResult.Error<>(response.message()));
                }
            }

            @Override
            public void onFailure(Call<RouteResponse> call, Throwable t) {
                route.setValue(new ApiResult.Error<>(t.getMessage()));
            }
        });
        return route;
    }

}
