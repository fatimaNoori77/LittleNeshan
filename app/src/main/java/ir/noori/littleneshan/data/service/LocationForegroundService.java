package ir.noori.littleneshan.data.service;

import static ir.noori.littleneshan.utils.Constants.NOTIFICATION_CHANNEL_ID;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import ir.noori.littleneshan.data.local.SharedPreferencesRepository;
import ir.noori.littleneshan.utils.LocationHelper;
import ir.noori.littleneshan.utils.NotificationUtils;

public class LocationForegroundService extends Service {
    private static final int NOTIFICATION_ID = 1;
    private LocationHelper locationHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        locationHelper = LocationHelper.getInstance(this);
        SharedPreferencesRepository.getInstance().setLocationServiceRunning(true);
        createNotificationChannel();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startForeground(NOTIFICATION_ID, NotificationUtils.getLocationNotification(this));

        locationHelper.startLocationUpdates(location -> SharedPreferencesRepository.getInstance().saveLocation(location.getLatitude(), location.getLongitude()));

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        SharedPreferencesRepository.getInstance().setLocationServiceRunning(false);
        locationHelper.stopLocationUpdates();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    NOTIFICATION_CHANNEL_ID,
                    "Location Tracking",
                    NotificationManager.IMPORTANCE_HIGH
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }
}
