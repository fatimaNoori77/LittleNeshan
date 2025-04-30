package ir.noori.littleneshan.data.model;

import com.google.gson.annotations.SerializedName;

public class Distance {
    @SerializedName("value")
    private double value;

    @SerializedName("text")
    private String text;

    public double getValue() {
        return value;
    }

    public String getText() {
        return text;
    }
}
