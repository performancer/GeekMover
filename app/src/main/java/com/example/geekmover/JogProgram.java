package com.example.geekmover;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.example.geekmover.activities.JogActivity;
import com.example.geekmover.data.Jog;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class JogProgram implements LocationListener {

    private Jog jog;
    private JogActivity activity;
    private LocationManager locationManager;
    private ArrayList<Coordinates> coordinatesArrayList;

    public JogProgram(JogActivity activity, Jog jog)
    {
        this.activity = activity;
        this.jog = jog;

        coordinatesArrayList = new ArrayList<>();
    }

    void addCoordinate(Coordinates coordinates){
        this.coordinatesArrayList.add(coordinates);
    }

    public Coordinates getLatestCoordinates(){
        if(coordinatesArrayList.size() > 0)
            return coordinatesArrayList.get(coordinatesArrayList.size() - 1);

        return null;
    }

    public ArrayList<Coordinates> getCoordinatesArrayList() {
        return coordinatesArrayList;
    }

    public int getTotalDistance() {

        double distance = 0;

        if(coordinatesArrayList.size() >= 2)
        {
            for(int i = 1; i < coordinatesArrayList.size(); i++){

                Coordinates last = coordinatesArrayList.get(i - 1);
                Coordinates current = coordinatesArrayList.get(i);

                distance += last.getDistanceTo(current);
            }
        }

        Log.d("debug", " " + distance);

        return (int) distance;
    }

    //Obsolete, was replaced by Coordinates.getDistance(Coordinates to)
    /*private double getDistanceBetweenCoordinates(Coordinates one, Coordinates two){
        double radius = 6371000;
        double dLat = Math.toRadians(two.getLatitude() - one.getLatitude());
        double dLon = Math.toRadians(two.getLongitude()- one.getLongitude());

        double lastLatInRadians = Math.toRadians(one.getLatitude());
        double currentLatInRadians = Math.toRadians(two.getLatitude());

        double a = Math.pow(Math.sin(dLat/2), 2) + Math.pow(Math.sin(dLon/2), 2) * Math.cos(lastLatInRadians) * Math.cos(currentLatInRadians);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

        return radius * c;
    }*/

    public int getGoal(){
        if(jog != null)
            return jog.getAmount();

        return 0;
    }

    public double getAverageSpeed() {
        int size = coordinatesArrayList.size();

        if(size >= 2)
        {
            Coordinates latest = getLatestCoordinates();
            Coordinates first = coordinatesArrayList.get(0);

            long seconds = (latest.getTimestamp().getTime() - first.getTimestamp().getTime()) / 1000;

            return (double)getTotalDistance()/seconds;
        }

        return 0;
    }

    public double getCurrentSpeed() {
        int size = coordinatesArrayList.size();

        if(size >= 2)
        {
            Coordinates latest = getLatestCoordinates();
            Coordinates earlier = coordinatesArrayList.get(size - 2);

            long seconds = (latest.getTimestamp().getTime() - earlier.getTimestamp().getTime()) / 1000;

            return  earlier.getDistanceTo(latest) / seconds;
        }

        return 0;
    }

    public boolean isFinished(){
        return getTotalDistance() >= getGoal();
    }

    public int getCaloriesBurned() {
        UserData data = UserData.getInstance();

        double calories = 0;

        for (int i = 1; i < coordinatesArrayList.size(); i++) {

            Coordinates last = coordinatesArrayList.get(i - 1);
            Coordinates current = coordinatesArrayList.get(i);

            double seconds = (current.getTimestamp().getTime() - last.getTimestamp().getTime()) / 1000f;
            double speed = last.getDistanceTo(current) / seconds;
            double factor = (speed * (0.7 * speed + 56)) / 360;
            double bmi = 1.0 + (data.getBMI() - 20) / 20;

            calories += factor * bmi * seconds;
        }

        return (int) Math.round(calories);
    }

    public boolean start() {
        if(locationManager == null)
            locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);

        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

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

        if(activity == null){
            end();
            return;
        }

        double latitude = (location.getLatitude());
        double longitude =  (location.getLongitude());
        Date now =  Calendar.getInstance().getTime();

        Coordinates current = new Coordinates(latitude, longitude, now);
        Coordinates last = getLatestCoordinates();

        if(last != null) {
            double distance = last.getDistanceTo(current);

            if (distance < 10)
                return;
        }

        addCoordinate(current);

        if(isFinished())
            jog.setFinished(true);

        activity.Update();
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
