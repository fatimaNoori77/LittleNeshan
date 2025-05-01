package ir.noori.littleneshan.data.local.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "tbl_address", indices = {@Index(value = {"address"}, unique = true)})

public class AddressEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String address;
    private String neighbourhood;
    private String region;
    private String type;
    private String category;

    @ColumnInfo(name = "latitude")
    private Double lat;

    @ColumnInfo(name = "longitude")
    private Double lng;  // Use `lng` instead of `long` to avoid conflict with Java's primitive type

    // Getters and Setters
    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public String getAddress() { return address; }

    public void setAddress(String address) { this.address = address; }

    public String getNeighbourhood() { return neighbourhood; }

    public void setNeighbourhood(String neighbourhood) { this.neighbourhood = neighbourhood; }

    public String getRegion() { return region; }

    public void setRegion(String region) { this.region = region; }

    public String getType() { return type; }

    public void setType(String type) { this.type = type; }

    public String getCategory() { return category; }

    public void setCategory(String category) { this.category = category; }

    public Double getLat() { return lat; }

    public void setLat(Double lat) { this.lat = lat; }

    public Double getLng() { return lng; }

    public void setLng(Double lng) { this.lng = lng; }
}
