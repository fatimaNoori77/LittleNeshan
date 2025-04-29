package ir.noori.littleneshan;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesUtility {

    private static final String PREF_NAME = "little_neshan_prefs";
    private static final String KEY_LATITUDE = "key_latitude";
    private static final String KEY_LONGITUDE = "key_longitude";
    private static SharedPreferencesUtility instance;

    private final SharedPreferences sharedPreferences;
    private SharedPreferencesUtility(Context context) {
        sharedPreferences = context.getApplicationContext()
                .getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized SharedPreferencesUtility getInstance(Context context) {
        if (instance == null) {
            instance = new SharedPreferencesUtility(context);
        }
        return instance;
    }
    public void saveLocation(double latitude, double longitude) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(KEY_LATITUDE, (float) latitude);
        editor.putFloat(KEY_LONGITUDE, (float) longitude);
        editor.apply();
    }

    public double getLatitude() {
        return sharedPreferences.getFloat(KEY_LATITUDE, 0f);
    }

    public double getLongitude() {
        return sharedPreferences.getFloat(KEY_LONGITUDE, 0f);
    }
}
