package com.example.geekmover;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Coordinates {

    private double latitude;
    private double longitude;
    private Date timestamp;

    /**
     * 
     * @param latitude
     * @param longitude
     */
    public Coordinates(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.timestamp = Calendar.getInstance().getTime();
    }

    public double getDistanceTo(Coordinates to){
        double radius = 6371000;
        double dLat = Math.toRadians(to.getLatitude() - this.getLatitude());
        double dLon = Math.toRadians(to.getLongitude()- this.getLongitude());

        double lastLatInRadians = Math.toRadians(this.getLatitude());
        double currentLatInRadians = Math.toRadians(to.getLatitude());

        double a = Math.pow(Math.sin(dLat/2), 2) + Math.pow(Math.sin(dLon/2), 2) * Math.cos(lastLatInRadians) * Math.cos(currentLatInRadians);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

        return radius * c;
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

    public LatLng getLatLng(){
        LatLng latLng = new LatLng (latitude,longitude);

        return latLng;
    }

}
