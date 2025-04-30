package ir.noori.littleneshan.data.model;

import java.util.List;

public class SearchResponse {
    private int count;
    private List<SearchItem> items;

    public int getCount() { return count; }
    public List<SearchItem> getItems() { return items; }
}

