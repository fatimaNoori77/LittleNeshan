package ir.noori.littleneshan;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.splashscreen.SplashScreen;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import org.neshan.common.model.LatLng;
import org.neshan.mapsdk.MapView;

import ir.noori.littleneshan.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity
        implements DestinationSearchBottomSheet.DestinationSelectionListener {
    private ActivityMainBinding binding;
    private MapView map;
    private Location currentLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SplashScreen.installSplashScreen(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setNavigationBarColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
        getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
        setContentView(binding.getRoot());
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
        binding.cardGPS.setOnClickListener(v -> {
            moveToCurrentLocation();
        });
        binding.edtDestination.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                showDestinationSearchBottomSheet();
            }
        });
    }

    private void showDestinationSearchBottomSheet() {
        binding.edtDestination.clearFocus();
        DestinationSearchBottomSheet bottomSheet = new DestinationSearchBottomSheet();
        bottomSheet.setDestinationSelectionListener(this);
        bottomSheet.show(getSupportFragmentManager(), bottomSheet.getTag());
    }

    @Override
    public void onDestinationSelected(AddressModel destination) {
        binding.edtDestination.setText(destination.getAddress());
        map.moveCamera(new LatLng(destination.getLatitude(), destination.getLongitude()), 0);
        map.setBearing(90.0f, 0f);
        map.setTilt(60.0f, 0f);
        map.setZoom(14f,0f);
    }

    private void moveToCurrentLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Constants.LOCATION_PERMISSION);
            return;
        }

        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        currentLocation = location;
                        map.moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), 0);
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