package ir.noori.littleneshan;

import android.app.Application;

import ir.noori.littleneshan.data.local.SharedPreferencesRepository;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SharedPreferencesRepository.getInstance().init(getApplicationContext());
    }
}
