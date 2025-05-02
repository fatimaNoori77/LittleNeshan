package ir.noori.littleneshan.data.local;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesRepository {

    private static final String PREF_NAME = "little_neshan_prefs";
    private static final String KEY_LATITUDE = "key_latitude";
    private static final String KEY_LONGITUDE = "key_longitude";
    private static SharedPreferencesRepository instance;
    SharedPreferences.Editor editor;
    private SharedPreferences sharedPreferences;

    public void init(Context context) {
        sharedPreferences = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public static synchronized SharedPreferencesRepository getInstance() {
        if (instance == null) {
            instance = new SharedPreferencesRepository();
        }
        return instance;
    }
    public void saveLocation(double latitude, double longitude) {
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
