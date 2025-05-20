package ir.noori.littleneshan;

import android.app.Application;

import dagger.hilt.android.HiltAndroidApp;
import ir.noori.littleneshan.data.local.SharedPreferencesRepository;

@HiltAndroidApp
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SharedPreferencesRepository.getInstance().init(getApplicationContext());
    }
}
