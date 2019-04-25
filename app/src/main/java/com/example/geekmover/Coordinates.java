package com.example.geekmover;
import java.util.Date;

public class Coordinates {

    private double latitude;
    private double longitude;
    private Date timestamp;

    public Coordinates(double latitude, double longitude, Date timestamp) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.timestamp = timestamp;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public Date getTimestamp() {
        return timestamp;
    }
}
