package com.example.geekmover;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Used to store latitude, longitude and the timestamp when the coordinate was created (Useful when calculating speed for example)
 */
public class Coordinates {

    private double latitude;
    private double longitude;
    private Date timestamp;

    /**
     *  Constructor for coordinates. Creates a timestamp
     * @param latitude
     * @param longitude
     */
    public Coordinates(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.timestamp = Calendar.getInstance().getTime();
    }

    /**
     * A method for calculating the distance in meters between two Coordinates. Assumes the world is a perfect ball.
     * @param to The other coordinate
     * @return Distance between two coordinates
     */
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

    /**
     * a getter method for latitude
     * @return latitude
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * a getter method for longitude
     * @return longitude
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * a getter method for timestamp
     * @return timestamp
     */
    public Date getTimestamp() {
        return timestamp;
    }

    /**
     * combines latitude and longitude into LatLng object
     * @return coordinates' latitude and longitude in LatLng form
     */
    public LatLng getLatLng(){
        LatLng latLng = new LatLng (latitude,longitude);

        return latLng;
    }
}
