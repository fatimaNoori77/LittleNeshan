package ir.noori.littleneshan.data.repository;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.neshan.common.model.LatLng;

import java.util.List;
import java.util.concurrent.Executors;

import ir.noori.littleneshan.BuildConfig;
import ir.noori.littleneshan.data.local.AddressDao;
import ir.noori.littleneshan.data.local.AppDatabase;
import ir.noori.littleneshan.data.local.SharedPreferencesRepository;
import ir.noori.littleneshan.data.local.entity.AddressEntity;
import ir.noori.littleneshan.data.model.SearchResponse;
import ir.noori.littleneshan.data.network.ApiClient;
import ir.noori.littleneshan.data.network.ApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchRepository {
    private final ApiService apiService;
    private final AddressDao addressDao;

    public SearchRepository(Context context) {
        this.apiService = ApiClient.getClient().create(ApiService.class);
        AppDatabase db = AppDatabase.getInstance(context);
        addressDao = db.addressDao();
    }

    public void insertAddress(AddressEntity address) {
        Executors.newSingleThreadExecutor().execute(() -> addressDao.insertAddress(address));
    }

    public LiveData<List<AddressEntity>> getAllAddresses() {
        return addressDao.getAllAddresses();
    }

    public void clearHistory() {
        Executors.newSingleThreadExecutor().execute(addressDao::clearHistory);
    }

    public LiveData<SearchResponse> searchAddress(String term, LatLng latLng) {
        MutableLiveData<SearchResponse> data = new MutableLiveData<>();

        apiService.searchAddress(BuildConfig.NESHAN_API_KEY, term, latLng.getLatitude(), latLng.getLongitude()).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<SearchResponse> call, @NonNull Response<SearchResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    data.setValue(response.body());
                } else {
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<SearchResponse> call, @NonNull Throwable t) {
                data.setValue(null);
            }
        });

        return data;
    }

    public LatLng getCurrentUserLocation(){
        return new LatLng(SharedPreferencesRepository.getInstance().getLatitude(),SharedPreferencesRepository.getInstance().getLongitude());
    }

}
