package ir.noori.littleneshan;

import static ir.noori.littleneshan.Constants.NESHAN_API_KEY;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.carto.core.ScreenBounds;
import com.carto.core.ScreenPos;
import com.carto.graphics.Color;
import com.carto.styles.LineStyle;
import com.carto.styles.LineStyleBuilder;

import org.neshan.common.model.LatLng;
import org.neshan.common.model.LatLngBounds;
import org.neshan.mapsdk.MapView;
import org.neshan.mapsdk.model.Polyline;

import java.util.ArrayList;

import ir.noori.littleneshan.databinding.FragmentDirectionBinding;

public class DirectionFragment extends Fragment {
    private FragmentDirectionBinding binding;
    private DirectionViewModel viewModel;
    SharedPreferencesUtility preferences ;
    private LatLng selectedDestination;
    Polyline overViewPolyline;
    private MapView map;
    public DirectionFragment(LatLng selectedDestination) {
        this.selectedDestination = selectedDestination;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(DirectionViewModel.class);
        preferences = SharedPreferencesUtility.getInstance(requireContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDirectionBinding.inflate(inflater, container, false);


        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initMap();
        initViews();
        initObservers();

        RoutRequestInputs inputs = new RoutRequestInputs(
                "car",
                preferences.getLatitude() + "," + preferences.getLongitude(),
                selectedDestination.getLatitude() + "," + selectedDestination.getLongitude()
        );
        inputs.setAvoidTrafficZone(true);
        inputs.setAlternative(false);
        viewModel.getRoute(inputs, NESHAN_API_KEY);

    }

    private void initObservers() {
        viewModel.getRoutResult().observe(getViewLifecycleOwner(), result -> {
            map.moveToCameraBounds(
                    new LatLngBounds(
                            new LatLng(preferences.getLatitude(), preferences.getLongitude()),
                            new LatLng(selectedDestination.getLatitude(), selectedDestination.getLongitude())),
                    new ScreenBounds(new ScreenPos(0, 0),
                            new ScreenPos(map.getWidth(), map.getHeight())
                    ),
                    true, 0.25f);
            map.setTilt(60.0f, 0f);
        });

        viewModel.getStepsLiveData().observe(getViewLifecycleOwner(), steps -> {
            if (overViewPolyline != null)
                map.removePolyline(overViewPolyline);
            for (Step step : steps) {
                ArrayList<LatLng> path = new PolylineDecoder().decodePoly(step.getPolyline());
                overViewPolyline = new Polyline(path, getLineStyle());
                map.addPolyline(overViewPolyline);
            }
        });

        viewModel.getInstructionLiveData().observe(getViewLifecycleOwner(), instruction -> {
            Toast.makeText(requireContext(), instruction, Toast.LENGTH_SHORT).show();
        });
    }

    void initViews(){

    }

    private void initMap() {
        map = binding.map;
    }

    private LineStyle getLineStyle() {
        LineStyleBuilder lineStCr = new LineStyleBuilder();
        lineStCr.setColor(new Color((short) 2, (short) 119, (short) 189, (short) 190));
        lineStCr.setWidth(6f);
        lineStCr.setStretchFactor(100f);
        return lineStCr.buildStyle();
    }


}