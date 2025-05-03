package ir.noori.littleneshan.ui.main;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.splashscreen.SplashScreen;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.carto.styles.MarkerStyle;
import com.carto.styles.MarkerStyleBuilder;

import org.neshan.common.model.LatLng;
import org.neshan.mapsdk.MapView;
import org.neshan.mapsdk.internal.utils.BitmapUtils;
import org.neshan.mapsdk.model.Marker;

import ir.noori.littleneshan.R;
import ir.noori.littleneshan.data.model.SearchItem;
import ir.noori.littleneshan.databinding.ActivityMainBinding;
import ir.noori.littleneshan.ui.direction.DirectionFragment;
import ir.noori.littleneshan.ui.search.SearchAddressFragment;
import ir.noori.littleneshan.utils.CheckInternetStatus;
import ir.noori.littleneshan.utils.CheckLocationEnable;
import ir.noori.littleneshan.utils.Constants;
import ir.noori.littleneshan.utils.LocationHelper;

public class MainActivity extends AppCompatActivity
        implements SearchAddressFragment.DestinationSelectionListener {
    private ActivityMainBinding binding;
    private MapView map;
    private Location currentLocation;
    private LatLng selectedDestination;
    LocationHelper locationHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SplashScreen.installSplashScreen(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setNavigationBarColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
        getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
        setContentView(binding.getRoot());
        locationHelper = LocationHelper.getInstance(getApplicationContext());
    }

    @Override
    protected void onStart() {
        super.onStart();
        initMap();
//        here I must check if the user is in navigating mode, open the navigation page but I don't have enough time!
//        if(SharedPreferencesRepository.getInstance().isLocationServiceRunning()){
//            navigateToDirectionFragment();
//        }
        initViews();
        checkLocationStatus();
        checkInternetStatus();
    }

    private void checkInternetStatus() {
        if (!CheckInternetStatus.isInternetAvailable(getApplicationContext())) {
            Toast.makeText(this, R.string.internet_warning, Toast.LENGTH_SHORT).show();
        }
    }

    private void checkLocationStatus() {
        if (!CheckLocationEnable.isLocationEnabled(getApplicationContext())) {
            // show dialog is better that Toast
            Toast.makeText(this, R.string.turn_on_gps, Toast.LENGTH_LONG).show();
        }
    }

    private void initMap() {
        map = binding.map;
    }

    private void initViews() {
        moveToCurrentLocation();

        binding.cardGPS.setOnClickListener(v -> {
            checkLocationStatus();
            moveToCurrentLocation();
        });

        binding.cardClose.setOnClickListener(v -> {
            moveToCurrentLocation();
            map.clearMarkers();
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
            requestPostNotificationPermission();
        });

        binding.chipSave.setOnClickListener(v -> Toast.makeText(this,R.string.copy_address, Toast.LENGTH_SHORT).show());

        binding.chipShare.setOnClickListener(v -> Toast.makeText(this, R.string.share_address, Toast.LENGTH_SHORT).show());
    }

    private void showDestinationSearchBottomSheet() {
        binding.edtDestination.clearFocus();
        final String TAG = SearchAddressFragment.TAG;
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment existingFragment = fragmentManager.findFragmentByTag(TAG);
        if (existingFragment == null) {
            SearchAddressFragment bottomSheet = new SearchAddressFragment();
            bottomSheet.setDestinationSelectionListener(this);
            bottomSheet.show(fragmentManager, TAG);
        }
    }

    @Override
    public void onDestinationSelected(SearchItem destination) {
        binding.edtDestination.setText(destination.getAddress());
        selectedDestination = new LatLng(destination.getLocation().getY(), destination.getLocation().getX());
        map.moveCamera(selectedDestination, 0);
        Marker marker = createMarker(selectedDestination);
        map.clearMarkers();
        map.addMarker(marker);
        map.setZoom(17f, 0f);
        binding.llMoreOptions.setVisibility(View.VISIBLE);
        binding.cardClose.setVisibility(View.VISIBLE);
    }

    public void navigateToDirectionFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new DirectionFragment(selectedDestination))
                .addToBackStack(null)
                .commit();
        map.clearMarkers();
        binding.cardClose.performClick();
    }

    private Marker createMarker(LatLng loc) {
        MarkerStyleBuilder markStCr = new MarkerStyleBuilder();
        markStCr.setSize(30f);
        markStCr.setBitmap(BitmapUtils.createBitmapFromAndroidBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_pin)));
        MarkerStyle markSt = markStCr.buildStyle();
        return new Marker(loc, markSt);
    }

    private void requestPostNotificationPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this,R.string.give_post_notification_permission, Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, Constants.POST_NOTIFICATION_PERMISSION);
            }else{
                navigateToDirectionFragment();
            }
        }else{
            navigateToDirectionFragment();
        }
    }

    private void moveToCurrentLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Constants.LOCATION_PERMISSION);
            return;
        }

        locationHelper.getLastKnownLocation(location -> {
            currentLocation = location;
            map.setMyLocationEnabled(true);
            map.moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), 0);
            map.setZoom(17f, 0);
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length == 0) return; // No permission result to process
        boolean isGranted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
        switch (requestCode) {
            case Constants.LOCATION_PERMISSION:
                if (isGranted) {
                    moveToCurrentLocation();
                } else {
                    Toast.makeText(this, R.string.location_permission_rejected, Toast.LENGTH_SHORT).show();
                }
                break;

            case Constants.POST_NOTIFICATION_PERMISSION:
                if (isGranted) {
                    navigateToDirectionFragment();
                } else {
                    Toast.makeText(this, R.string.post_notification_permission_rejected, Toast.LENGTH_SHORT).show();
                }
                break;

            default:
                Log.w("Permission", "Unhandled permission request code: " + requestCode);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}