package ir.noori.littleneshan.utils;

import android.content.Context;
import android.location.Location;
import android.os.Looper;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.hilt.android.qualifiers.ApplicationContext;
import ir.noori.littleneshan.data.local.SharedPreferencesRepository;

@Singleton
public class LocationHelper {

    private final FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private LocationUpdateListener listener;

    public interface LocationUpdateListener {
        void onLocationUpdated(Location location);
    }

    @Inject
    public LocationHelper(@ApplicationContext Context context) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context.getApplicationContext());
    }

    public void startLocationUpdates(LocationUpdateListener listener) {
        this.listener = listener;
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (listener != null && locationResult.getLastLocation() != null) {
                    listener.onLocationUpdated(locationResult.getLastLocation());
                }
            }
        };

        try {
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
        } catch (SecurityException e) {
            Log.e("LocationHelper", "startLocationUpdates: " + e.getMessage());
        }
    }

    public void stopLocationUpdates() {
        if (locationCallback != null) {
            fusedLocationClient.removeLocationUpdates(locationCallback)
                    .addOnCompleteListener(task -> locationCallback = null);
        }
    }

    public void getLastKnownLocation(LocationUpdateListener callback) {
        try {
            fusedLocationClient.getLastLocation()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful() && task.getResult() != null) {
                            SharedPreferencesRepository.getInstance().saveLocation(
                                    task.getResult().getLatitude(), task.getResult().getLongitude()
                            );
                            callback.onLocationUpdated(task.getResult());
                        }
                    });
        } catch (SecurityException e) {
            Log.e("LocationHelper", "getLastKnownLocation: " + e.getMessage());
        }
    }
}
