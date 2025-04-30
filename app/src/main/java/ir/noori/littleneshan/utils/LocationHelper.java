package ir.noori.littleneshan.utils;

import android.content.Context;
import android.location.Location;
import android.os.Looper;

import androidx.annotation.NonNull;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

public class LocationHelper {

    private static volatile LocationHelper instance;
    private final FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private LocationUpdateListener listener;

    public interface LocationUpdateListener {
        void onLocationUpdated(Location location);
    }

    private LocationHelper(Context context) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context.getApplicationContext());
    }

    public static LocationHelper getInstance(Context context) {
        if (instance == null) {
            synchronized (LocationHelper.class) {
                if (instance == null) {
                    instance = new LocationHelper(context);
                }
            }
        }
        return instance;
    }

    public void startLocationUpdates(LocationUpdateListener listener) {
        this.listener = listener;
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                if (listener != null) {
                    listener.onLocationUpdated(locationResult.getLastLocation());
                }
            }
        };
        try {
            fusedLocationClient.requestLocationUpdates(locationRequest,
                    locationCallback,
                    Looper.getMainLooper());
        } catch (SecurityException e) {
            e.printStackTrace();
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
                            callback.onLocationUpdated(task.getResult());
                        }
                    });
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }
}