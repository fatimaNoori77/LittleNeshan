package ir.noori.littleneshan;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchViewModel {
    private final SearchRepository repository;
    private final MutableLiveData<SearchResponse> searchResult = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);

    public SearchViewModel() {
        this.repository = new SearchRepository();
    }

    public LiveData<SearchResponse> getSearchResult() {
        return searchResult;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public void searchAddress(String apiKey, String term, double lat, double lng) {
        isLoading.setValue(true);
        repository.searchAddress(apiKey, term, lat, lng).enqueue(new Callback<SearchResponse>() {
            @Override
            public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                isLoading.setValue(false);
                if (response.isSuccessful() && response.body() != null) {
                    searchResult.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<SearchResponse> call, Throwable t) {
                isLoading.setValue(false);
                searchResult.setValue(null);
            }
        });
    }

}
