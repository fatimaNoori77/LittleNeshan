package ir.noori.littleneshan;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class SearchRepository {
    private final ApiService apiService;

    public SearchRepository() {
        this.apiService = ApiClient.getClient().create(ApiService.class);
    }

    public LiveData<SearchResponse> searchAddress(String apiKey, String term, double lat, double lng) {
        MutableLiveData<SearchResponse> data = new MutableLiveData<>();

        apiService.searchAddress(apiKey, term, lat, lng).enqueue(new retrofit2.Callback<SearchResponse>() {
            @Override
            public void onResponse(retrofit2.Call<SearchResponse> call, retrofit2.Response<SearchResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    data.postValue(response.body());
                } else {
                    data.postValue(null);
                }
            }

            @Override
            public void onFailure(retrofit2.Call<SearchResponse> call, Throwable t) {
                data.postValue(null);
            }
        });

        return data;
    }

}
