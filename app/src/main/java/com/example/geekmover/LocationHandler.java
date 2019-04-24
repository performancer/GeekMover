package com.example.geekmover;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import java.util.Calendar;
import java.util.Date;

public class LocationHandler implements LocationListener {

    private Jog jog;
    private LocationManager locationManager;

    public LocationHandler(Jog jog) {
        this.jog = jog;
    }

    public boolean start(Activity activity) {
        if(locationManager == null)
            locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);

        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_CALENDAR)
                == PackageManager.PERMISSION_GRANTED) {

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
                    0, this);
            return true;
        }
        return false;
    }

    public void end(){
        if(locationManager != null)
            locationManager.removeUpdates(this);
    }

    @Override
    public void onLocationChanged(Location location) {

        if(jog == null){
            end();
            return;
        }

        double latitude = (location.getLatitude());
        double longitude =  (location.getLongitude());
        Date current =  Calendar.getInstance().getTime();

        jog.addCoordinate(new Coordinate(latitude, longitude, current));
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
