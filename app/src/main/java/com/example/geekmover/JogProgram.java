package com.example.geekmover;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
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
    private double totalDistance;

    public JogProgram(JogActivity activity, Jog jog)
    {
        this.activity = activity;
        this.jog = jog;

        coordinatesArrayList = new ArrayList<>();
        totalDistance = 0;
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

    public double getDistance() {

        if(coordinatesArrayList.size() > 2) {
                totalDistance += measureDistanceBetweenCoordinates(getLatestCoordinates(), coordinatesArrayList.get(coordinatesArrayList.size() - 2))  * 1000;
        }
            return totalDistance;
    }

    public int getGoal(){
        if(jog != null)
            return jog.getAmount();

        return 0;
    }

    public double getAverageSpeed() {
        return 0;
    }

    public double getCurrentSpeed() {
        return 0;
    }

    public boolean isFinished(){
        return getDistance() >= getGoal();
    }

    public double getCaloriesBurned(){
        UserData data = UserData.getInstance();
        return 0;
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
        Date current =  Calendar.getInstance().getTime();

        addCoordinate(new Coordinates(latitude, longitude, current));

        if(isFinished())
            jog.setFinished(true);

        activity.Update();
    }

    public double measureDistanceBetweenCoordinates(Coordinates coordOne, Coordinates coordTwo){
        if(coordOne.getTimestamp() != coordTwo.getTimestamp()) {
            double earthRadius = 6371000; //meters
            double dLat = Math.toRadians(coordTwo.getLatitude()-coordOne.getLatitude());
            double dLng = Math.toRadians(coordTwo.getLongitude()-coordOne.getLongitude());
            double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                    Math.cos(Math.toRadians(coordOne.getLatitude())) * Math.cos(Math.toRadians(coordTwo.getLatitude())) * Math.sin(dLng/2) * Math.sin(dLng/2);
            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
            double dist = (float) (earthRadius * c);

            return dist;
        //    return Math.sqrt(Math.pow(coordOne.getLatitude() - coordTwo.getLatitude(), 2) + Math.pow(coordOne.getLongitude() - coordTwo.getLongitude(), 2))/2;
        }else{
            return 0;
        }
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
