package ir.noori.littleneshan.ui.search;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.neshan.common.model.LatLng;

import java.util.List;

import ir.noori.littleneshan.data.local.entity.AddressEntity;
import ir.noori.littleneshan.data.model.SearchResponse;
import ir.noori.littleneshan.data.repository.SearchRepository;

public class SearchAddressViewModel extends AndroidViewModel {
    private final SearchRepository repository;
    private final MutableLiveData<SearchResponse> searchResult = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);

    public SearchAddressViewModel(@NonNull Application application) {
        super(application);
        this.repository = new SearchRepository(application);
        allAddresses = repository.getAllAddresses();

    }

    public void insertAddress(AddressEntity address){
        repository.insertAddress(address);
    }

    private final LiveData<List<AddressEntity>> allAddresses;

    public LiveData<List<AddressEntity>> getAllAddressesFromDatabase() {
        return allAddresses;
    }

    public LiveData<SearchResponse> getSearchResult() {
        return searchResult;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public void searchAddress(String term, LatLng latLng) {
        isLoading.setValue(true);
        repository.searchAddress(term, latLng).observeForever(response -> {
            isLoading.setValue(false);
            if (response != null) {
                searchResult.setValue(response);
            } else {
                searchResult.setValue(null);
            }
        });
    }

    public LatLng getCurrentUserLocation(){
        return repository.getCurrentUserLocation();
    }
}
