package ir.noori.littleneshan;

import retrofit2.Call;

public class SearchRepository {
    private final ApiService apiService;

    public SearchRepository() {
        this.apiService = ApiClient.getClient().create(ApiService.class);
    }

    public Call<SearchResponse> searchAddress(String apiKey, String term, double lat, double lng) {
        return apiService.searchAddress(apiKey, term, lat, lng);
    }

}
