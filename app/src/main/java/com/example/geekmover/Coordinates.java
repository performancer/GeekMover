package com.example.geekmover;
import java.util.Date;

public class Coordinates {

    private double longitude;
    private double latitude;
    private Date timestamp;

    public Coordinates(double longitude, double latitude, Date timestamp) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.timestamp = timestamp;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public Date getTimestamp() {
        return timestamp;
    }
}
