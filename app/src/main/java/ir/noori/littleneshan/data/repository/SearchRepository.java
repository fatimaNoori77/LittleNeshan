package ir.noori.littleneshan.data.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import ir.noori.littleneshan.BuildConfig;
import ir.noori.littleneshan.data.network.ApiClient;
import ir.noori.littleneshan.data.network.ApiService;
import ir.noori.littleneshan.data.model.SearchResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchRepository {
    private final ApiService apiService;

    public SearchRepository() {
        this.apiService = ApiClient.getClient().create(ApiService.class);
    }

    public LiveData<SearchResponse> searchAddress(String term, double lat, double lng) {
        MutableLiveData<SearchResponse> data = new MutableLiveData<>();

        apiService.searchAddress(BuildConfig.NESHAN_API_KEY, term, lat, lng).enqueue(new Callback<>() {
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

}
