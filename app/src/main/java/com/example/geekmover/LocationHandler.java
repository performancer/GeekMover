package com.example.geekmover;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

public class LocationHandler implements LocationListener {

    private double latitude;
    private double longitude;

    public LocationHandler(){ }

    public double getCurrentLatitude(){
        return this.latitude;
    }

    public double getCurrentLongitude(){
        return this.longitude;
    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = (location.getLatitude());
        longitude =  (location.getLongitude());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }


}
