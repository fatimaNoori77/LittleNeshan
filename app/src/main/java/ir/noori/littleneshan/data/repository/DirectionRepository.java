package ir.noori.littleneshan.data.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.neshan.common.model.LatLng;

import javax.inject.Inject;

import ir.noori.littleneshan.BuildConfig;
import ir.noori.littleneshan.data.local.SharedPreferencesRepository;
import ir.noori.littleneshan.data.model.RoutRequestInputs;
import ir.noori.littleneshan.data.model.RouteResponse;
import ir.noori.littleneshan.data.network.ApiClient;
import ir.noori.littleneshan.data.network.ApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DirectionRepository {
    private final ApiService apiService;

    @Inject
    public DirectionRepository() {
        this.apiService = ApiClient.getClient().create(ApiService.class);
    }

    public LiveData<RouteResponse> getRoute(RoutRequestInputs inputs){
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
                BuildConfig.NESHAN_API_KEY).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<RouteResponse> call, @NonNull Response<RouteResponse> response) {
                // handle this = {"status":"ERROR","code":200,"message":"No Route Found!"}
                if (response.isSuccessful()) {
                    route.setValue(response.body());
                } else {
                    route.setValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<RouteResponse> call, @NonNull Throwable t) {
                route.setValue(null);
            }
        });
        return route;
    }

    public LatLng getCurrentUserLocation(){
        return new LatLng(SharedPreferencesRepository.getInstance().getLatitude(), SharedPreferencesRepository.getInstance().getLongitude());
    }

}
