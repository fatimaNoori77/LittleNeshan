package ir.noori.littleneshan.ui.direction;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.neshan.common.model.LatLng;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import ir.noori.littleneshan.data.model.RoutRequestInputs;
import ir.noori.littleneshan.data.model.RouteResponse;
import ir.noori.littleneshan.data.model.Step;
import ir.noori.littleneshan.data.repository.DirectionRepository;

@HiltViewModel
public class DirectionViewModel extends ViewModel {

    private final DirectionRepository repository;
    @Inject
    public DirectionViewModel() {
        this.repository = new DirectionRepository();
    }
    private final MutableLiveData<List<Step>> stepsLiveData = new MutableLiveData<>();
    private final MutableLiveData<Step> instructionLiveData = new MutableLiveData<>();
    private List<Step> currentSteps;
    private int currentStepIndex = 0;

    public LiveData<List<Step>> getStepsLiveData() {
        return stepsLiveData;
    }

    public LiveData<Step> getInstructionLiveData() {
        return instructionLiveData;
    }
    private final MutableLiveData<RouteResponse> routResult = new MutableLiveData<>();
    public LiveData<RouteResponse> getRoutResult() {
        return routResult;
    }

    public void getRoute(RoutRequestInputs inputs) {
        repository.getRoute(inputs).observeForever(response->{
            try {
                if (response != null) {
                    if(response.getRoutes() == null){
                        stepsLiveData.setValue(null);
                        return;
                    }
                    routResult.setValue(response);
                    List<Step> steps = response.getRoutes().get(0).getLegs().get(0).getSteps();
                    this.currentSteps = steps;
                    stepsLiveData.setValue(steps);

                    if (!steps.isEmpty()) {
                        currentStepIndex = 0;
                    }
                } else {
                    routResult.setValue(null);
                }
            }catch (Exception e){
                Log.e("TAG", "getRoute: "+ e.getMessage());
            }
        });
    }

    public void nextStep() {
        if (currentSteps != null && currentStepIndex < currentSteps.size() - 1) {
            currentStepIndex++;
            instructionLiveData.setValue(currentSteps.get(currentStepIndex));
        }
    }

    public Step getCurrentStep() {
        if (currentSteps != null && !currentSteps.isEmpty()) {
            return currentSteps.get(currentStepIndex);
        }
        return null;
    }

    public boolean isLastStep() {
        return currentSteps != null && currentStepIndex >= currentSteps.size() - 1;
    }

    public LatLng getCurrentUserLocation(){
        return repository.getCurrentUserLocation();
    }

}