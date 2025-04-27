package ir.noori.littleneshan;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DirectionViewModel extends ViewModel {

    private final DirectionRepository repository;
    public DirectionViewModel() {
        this.repository = new DirectionRepository();
    }

    private  MutableLiveData<ApiResult<RouteResponse>> routResult = new MutableLiveData<>();
    public LiveData<ApiResult<RouteResponse>> getRoutResult() {
        return routResult;
    }

    public void getRoute(RoutRequestInputs inputs, String apiKey) {
        LiveData<ApiResult<RouteResponse>> repositoryLiveData = repository.getRoute(inputs, apiKey);
        routResult =(MutableLiveData<ApiResult<RouteResponse>>) repositoryLiveData;
    }

}