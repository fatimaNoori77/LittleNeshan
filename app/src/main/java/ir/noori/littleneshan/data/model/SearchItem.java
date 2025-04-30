package ir.noori.littleneshan.data.model;
// SearchItem.java

public class SearchItem {
    private String title;
    private String address;
    private String neighbourhood;
    private String region;
    private String type;
    private String category;
    private LocationModel location;

    public SearchItem (){

    }
    public SearchItem(String title, String address, String neighbourhood, String region,
                      String type, String category, LocationModel location) {
        this.title = title;
        this.address = address;
        this.neighbourhood = neighbourhood;
        this.region = region;
        this.type = type;
        this.category = category;
        this.location = location;
    }


    public String getTitle() { return title; }
    public String getAddress() { return address; }
    public String getNeighbourhood() { return neighbourhood; }
    public String getRegion() { return region; }
    public String getType() { return type; }
    public String getCategory() { return category; }
    public LocationModel getLocation() { return location; }
}
