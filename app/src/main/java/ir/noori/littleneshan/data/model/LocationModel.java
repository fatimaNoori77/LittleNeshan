package ir.noori.littleneshan.data.model;

public class LocationModel {
    private double x;

    public void setY(double y) {
        this.y = y;
    }

    public void setX(double x) {
        this.x = x;
    }

    private double y;
    public LocationModel(){

    }
    public LocationModel(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() { return x; }
    public double getY() { return y; }
}
