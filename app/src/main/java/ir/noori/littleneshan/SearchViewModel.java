package ir.noori.littleneshan;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SearchViewModel extends ViewModel {
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

    public LiveData<SearchResponse> searchAddress(String apiKey, String term, double lat, double lng) {
        LiveData<SearchResponse> data = repository.searchAddress(apiKey, term, lat, lng);
        Log.i("TAG", "searchAddress: " + data.getValue().getItems());
        return data;

    }
}
