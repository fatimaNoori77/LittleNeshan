package ir.noori.littleneshan;

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

    public void searchAddress(String apiKey, String term, double lat, double lng) {
        isLoading.setValue(true);
        repository.searchAddress(apiKey, term, lat, lng).observeForever(response -> {
            isLoading.setValue(false);
            if (response != null) {
                searchResult.setValue(response);
            } else {
                searchResult.setValue(null);
            }
        });
    }
}
