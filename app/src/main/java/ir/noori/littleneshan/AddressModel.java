package ir.noori.littleneshan;

public class AddressModel {
    private String id;
    private String title;
    private String address;
    private double latitude;
    private double longitude;

    // Constructor
    public AddressModel(String id, String title, String address, double latitude, double longitude) {
        this.id = id;
        this.title = title;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getName() {
        return title;
    }

    public String getAddress() {
        return address;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}