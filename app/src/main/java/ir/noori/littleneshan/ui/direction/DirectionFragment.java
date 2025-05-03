package ir.noori.littleneshan.ui.direction;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.carto.core.ScreenBounds;
import com.carto.core.ScreenPos;
import com.carto.graphics.Color;
import com.carto.styles.LineStyle;
import com.carto.styles.LineStyleBuilder;
import com.carto.styles.MarkerStyle;
import com.carto.styles.MarkerStyleBuilder;

import org.neshan.common.model.LatLng;
import org.neshan.common.model.LatLngBounds;
import org.neshan.mapsdk.MapView;
import org.neshan.mapsdk.internal.utils.BitmapUtils;
import org.neshan.mapsdk.model.Marker;
import org.neshan.mapsdk.model.Polyline;

import java.util.ArrayList;

import ir.noori.littleneshan.R;
import ir.noori.littleneshan.data.model.RoutRequestInputs;
import ir.noori.littleneshan.data.model.Step;
import ir.noori.littleneshan.data.service.LocationForegroundService;
import ir.noori.littleneshan.databinding.FragmentDirectionBinding;
import ir.noori.littleneshan.utils.LocationHelper;
import ir.noori.littleneshan.utils.PolylineDecoder;

public class DirectionFragment extends Fragment {
    private FragmentDirectionBinding binding;
    private DirectionViewModel viewModel;
    private final LatLng selectedDestination;
    Polyline overViewPolyline;
    private MapView map;
    LocationHelper locationHelper;

    public DirectionFragment(LatLng selectedDestination) {
        this.selectedDestination = selectedDestination;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(DirectionViewModel.class);
        locationHelper = LocationHelper.getInstance(requireContext());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDirectionBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        initMap();
        initViews();
        initObservers();
        fetchRoutFromApi();
    }

    void initViews(){
        binding.cardGPS.setOnClickListener(v -> {
            moveToCurrentLocation();
        });

        binding.btnEnd.setOnClickListener(v -> {
            stopLocationService();
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        binding.btnStart.setOnClickListener(v -> {
            startLocationService();
            startLocationUpdate();
            viewModel.nextStep();
            binding.viewFlipper.setDisplayedChild(1);
            map.setTilt(60.0f, 0f);
        });
    }

    private void moveToCurrentLocation() {
        locationHelper.getLastKnownLocation(location -> {
            map.moveCamera(new LatLng(location.getLatitude(), location.getLongitude()), 0);
            map.setZoom(17f, 0);
        });
    }

    private void initObservers() {
        viewModel.getRoutResult().observe(getViewLifecycleOwner(), result -> {
            map.moveToCameraBounds(
                    new LatLngBounds(
                            viewModel.getCurrentUserLocation(),
                            new LatLng(selectedDestination.getLatitude(), selectedDestination.getLongitude())),
                    new ScreenBounds(new ScreenPos(0, 0),
                            new ScreenPos(map.getWidth(), map.getHeight())
                    ),
                    true, 0.25f);
        });

        viewModel.getStepsLiveData().observe(getViewLifecycleOwner(), steps -> {
            if(steps == null){
                Toast.makeText(requireContext(), R.string.routing_error, Toast.LENGTH_SHORT).show();
                return;
            }
            if (overViewPolyline != null)
                map.removePolyline(overViewPolyline);
            for (Step step : steps) {
                ArrayList<LatLng> path = new PolylineDecoder().decodePoly(step.getPolyline());
                overViewPolyline = new Polyline(path, getLineStyle());
                map.addPolyline(overViewPolyline);
            }
        });

        viewModel.getInstructionLiveData().observe(getViewLifecycleOwner(), step -> {
            binding.txtInstructions.setText(step.getInstruction());
            // just for example
            if(step.getModifier().contains("right")){
                binding.imgDirection.setImageResource(R.drawable.direction_right);
            }else if(step.getModifier().contains("left")){
                binding.imgDirection.setImageResource(R.drawable.direction_left);
            }else{
                binding.imgDirection.setImageResource(R.drawable.direction_direct);
            }
        });
    }

    private void fetchRoutFromApi() {
        RoutRequestInputs inputs = new RoutRequestInputs(
                "car",
                viewModel.getCurrentUserLocation().getLatitude() + "," + viewModel.getCurrentUserLocation().getLongitude(),
                selectedDestination.getLatitude() + "," + selectedDestination.getLongitude()
        );
        inputs.setAvoidTrafficZone(true);
        inputs.setAlternative(false);
        viewModel.getRoute(inputs);
    }

    private void startLocationUpdate() {
        locationHelper.startLocationUpdates(location -> {
            double lat = location.getLatitude();
            double lng = location.getLongitude();
            map.moveCamera(new LatLng(lat, lng), 0);
            map.setZoom(17f, 0);
            Marker marker = createMarker(new LatLng(lat, lng));
            map.clearMarkers();
            map.addMarker(marker);
        });
    }

    private Marker createMarker(LatLng loc) {
        MarkerStyleBuilder markStCr = new MarkerStyleBuilder();
        markStCr.setSize(30f);
        markStCr.setBitmap(BitmapUtils.createBitmapFromAndroidBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_direction)));
        MarkerStyle markSt = markStCr.buildStyle();
        return new Marker(loc, markSt);
    }

    private void startLocationService() {
        Intent intent = new Intent(requireContext(), LocationForegroundService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            requireActivity().startForegroundService(intent);
        } else {
            requireActivity().startService(intent);
        }
    }

    private void initMap() {
        map = binding.map;
    }

    private LineStyle getLineStyle() {
        LineStyleBuilder lineStCr = new LineStyleBuilder();
        Color blue = new Color((short) 2, (short) 119, (short) 189, (short) 190);
        lineStCr.setColor(blue);
        lineStCr.setWidth(6f);
        lineStCr.setStretchFactor(100f);
        return lineStCr.buildStyle();
    }

    private void stopLocationService() {
        Intent intent = new Intent(requireContext(), LocationForegroundService.class);
        requireActivity().stopService(intent);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        locationHelper.stopLocationUpdates();
    }

    @Override
    public void onStop() {
        super.onStop();
        // when the user press home button or lock the phone
        locationHelper.stopLocationUpdates();
    }
}