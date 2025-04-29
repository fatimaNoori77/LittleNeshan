package ir.noori.littleneshan;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchRepository {
    private final ApiService apiService;

    public SearchRepository() {
        this.apiService = ApiClient.getClient().create(ApiService.class);
    }

    public LiveData<SearchResponse> searchAddress(String apiKey, String term, double lat, double lng) {
        MutableLiveData<SearchResponse> data = new MutableLiveData<>();

        apiService.searchAddress(apiKey, term, lat, lng).enqueue(new Callback<SearchResponse>() {
            @Override
            public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    data.setValue(response.body());
                } else {
                    data.setValue(null);
                }
            }
            @Override
            public void onFailure(Call<SearchResponse> call, Throwable t) {
                data.setValue(null);
            }
        });

        return data;
    }

}
