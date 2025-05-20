package ir.noori.littleneshan.ui.search;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.neshan.common.model.LatLng;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import ir.noori.littleneshan.data.local.entity.AddressEntity;
import ir.noori.littleneshan.data.model.SearchResponse;
import ir.noori.littleneshan.data.repository.SearchRepository;

@HiltViewModel
public class SearchAddressViewModel extends ViewModel {
    private final SearchRepository repository;
    private final MutableLiveData<SearchResponse> searchResult = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);

    @Inject
    public SearchAddressViewModel(SearchRepository searchRepository) {
        this.repository = searchRepository;
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
