package ir.noori.littleneshan;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DirectionViewModel extends ViewModel {

    private final DirectionRepository repository;
    public DirectionViewModel() {
        this.repository = new DirectionRepository();
    }

    private  MutableLiveData<RouteResponse> routResult = new MutableLiveData<>();
    public LiveData<RouteResponse> getRoutResult() {
        return routResult;
    }

    public void getRoute(RoutRequestInputs inputs, String apiKey) {
        routResult = repository.getRoute(inputs, apiKey);

    }

}