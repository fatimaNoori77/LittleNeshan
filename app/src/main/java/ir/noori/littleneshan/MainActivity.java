package ir.noori.littleneshan;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.splashscreen.SplashScreen;
import androidx.lifecycle.ViewModelProvider;

import com.carto.styles.MarkerStyle;
import com.carto.styles.MarkerStyleBuilder;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import org.neshan.common.model.LatLng;
import org.neshan.mapsdk.MapView;
import org.neshan.mapsdk.internal.utils.BitmapUtils;
import org.neshan.mapsdk.model.Marker;

import ir.noori.littleneshan.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity
        implements DestinationSearchBottomSheet.DestinationSelectionListener {
    private ActivityMainBinding binding;
    private MapView map;
    private Location currentLocation;
    private LatLng selectedDestination;
    private DirectionViewModel viewModel;
    SharedPreferencesUtility preferences ;
    FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SplashScreen.installSplashScreen(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setNavigationBarColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
        getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
        setContentView(binding.getRoot());
        viewModel = new ViewModelProvider(this).get(DirectionViewModel.class);
        preferences = SharedPreferencesUtility.getInstance(getApplicationContext());
    }

    @Override
    protected void onStart() {
        super.onStart();
        initMap();
        initViews();
    }

    private void initMap() {
        map = binding.map;
    }

    private void initViews() {
        moveToCurrentLocation();

        binding.cardGPS.setOnClickListener(v -> {
            moveToCurrentLocation();
        });

        binding.cardClose.setOnClickListener(v -> {
            moveToCurrentLocation();
            binding.llMoreOptions.setVisibility(View.GONE);
            binding.cardClose.setVisibility(View.GONE);
            binding.edtDestination.getText().clear();
        });

        binding.edtDestination.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                showDestinationSearchBottomSheet();
            }
        });

        binding.chipDriving.setOnClickListener(v -> {
            showDirectionFragment();
        });

        binding.chipSave.setOnClickListener(v -> {
            Toast.makeText(this, "مثلا آدرس در حافظه کپی شد ;)", Toast.LENGTH_SHORT).show();
        });

        binding.chipShare.setOnClickListener(v -> {
            Toast.makeText(this, "مثلا آدرس ارسال شد ;)", Toast.LENGTH_SHORT).show();
        });
    }

    private void showDestinationSearchBottomSheet() {
        binding.edtDestination.clearFocus();
        DestinationSearchBottomSheet bottomSheet = new DestinationSearchBottomSheet();
        bottomSheet.setDestinationSelectionListener(this);
        bottomSheet.show(getSupportFragmentManager(), bottomSheet.getTag());
    }

    @Override
    public void onDestinationSelected(SearchItem destination) {
        binding.edtDestination.setText(destination.getAddress());
        selectedDestination = new LatLng(destination.getLocation().getY(), destination.getLocation().getX());
        map.moveCamera(selectedDestination, 0);
        map.addMarker(createMarker(selectedDestination));
        map.setZoom(17f, 0f);
        binding.llMoreOptions.setVisibility(View.VISIBLE);
        binding.cardClose.setVisibility(View.VISIBLE);
    }

    public void showDirectionFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new DirectionFragment(selectedDestination))
                .addToBackStack(null)
                .commit();
    }

    private Marker createMarker(LatLng loc) {
        MarkerStyleBuilder markStCr = new MarkerStyleBuilder();
        markStCr.setSize(30f);
        markStCr.setBitmap(BitmapUtils.createBitmapFromAndroidBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_pin)));
        MarkerStyle markSt = markStCr.buildStyle();
        return new Marker(loc, markSt);
    }

    private void moveToCurrentLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Constants.LOCATION_PERMISSION);
            return;
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        currentLocation = location;
                        preferences.saveLocation(currentLocation.getLatitude(), currentLocation.getLongitude());
                        map.setMyLocationEnabled(true);
                        map.moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), 0);
                        map.setZoom(17f, 0);
                    } else {
                        Toast.makeText(this, "موقعیت فعلی در دسترس نیست", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constants.LOCATION_PERMISSION && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            moveToCurrentLocation();
        } else {
            Toast.makeText(this, "مجوز مکان رد شد", Toast.LENGTH_SHORT).show();
        }
    }
}