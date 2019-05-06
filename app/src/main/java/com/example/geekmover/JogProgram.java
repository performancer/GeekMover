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
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class JogProgram implements LocationListener {

    private Jog jog;
    private JogActivity activity;
    private LocationManager locationManager;
    private ArrayList<Coordinates> coordinatesArrayList;

    /**
     * JogProgram holds data about user's jogging and keeps a list of locations that the user has
     * been at. JogProgram holds methods for calculating various things about the current jog.
     * Implements LocationListener to receive GPS-coordinates, which it assigns to a ArrayList of
     * Coordinates.
     *
     * @param activity JogActivity that the jog program informs about location changes
     * @param jog Reference to the exercise that the user is completing.
     */
    public JogProgram(JogActivity activity, Jog jog)
    {
        this.activity = activity;
        this.jog = jog;

        coordinatesArrayList = new ArrayList<>();
    }

    /**
     * Adds coordinates to the coordinatesArrayList of the instance.
     *
     * @param coordinates the coordinates that are to be added to the list
     */
    void addCoordinate(Coordinates coordinates){
        this.coordinatesArrayList.add(coordinates);
    }

    /**
     * Returns the last index of the coordinatesArrayList if possible. Otherwise returns null.
     *
     * @return Coordinates that are the last in the coordinatesArrayList
     * @see Coordinates
     */
    public Coordinates getLatestCoordinates(){
        if(coordinatesArrayList.size() > 0)
            return coordinatesArrayList.get(coordinatesArrayList.size() - 1);

        return null;
    }

    /**
     * Returns the complete ArrayList of coordinates that are collected this far.
     *
     * @return ArrayList of Coordinates
     * @see ArrayList<Coordinates>
     */
    public ArrayList<Coordinates> getCoordinatesArrayList() {
        return coordinatesArrayList;
    }

    /**
     * Calculates the total distance between all coordinates and returns the total jogged distance
     *
     * @return total distance (meters)
     */
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
        return (int) distance;
    }

    /**
     * Gets the goal distance of the jog that is supposed to be completed as an integer.
     *
     * @return goal distance (meters)
     */
    public int getGoal(){
        if(jog != null)
            return jog.getAmount();

        return 0;
    }

    /**
     * Calculates the average speed between all coordinates by comparing their distance and time.
     * If there are not enough coordinates listed, returns 0.
     *
     * @return average speed (meters per second)
     */
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

    /**
     * Calculates the current speed between this and coordinates before this. If there are not
     * enough coordinates listed, returns 0.
     *
     * @return current speed (meters per second)
     */

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

    /**
     * Compares if the goal distance is reached and thus if the jog program is completed.
     *
     * @return is the jog program finished
     */
    public boolean isFinished(){
        return getTotalDistance() >= getGoal();
    }

    /**
     * Calculates the estimated calories burned with this jog program by comparing user data,
     * distance and speed between coordinates. The result is rounded.
     *
     * @return integer of the calories that are burned
     */
    public int getCaloriesBurned() {
        UserData data = UserData.getInstance();

        double calories = 0;

        //this might or might not be very accurate

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

    /**
     * Starts the jog program, checks the permission for location and assigns a LocationManager. For
     * map drawing.
     *
     * @return if permission is granted returns true otherwise false
     */
    public boolean start() {
        if(locationManager == null)
            locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);

        //do we need permission checks here for safety?

        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
                    0, this);
            return true;
        }

        //maybe send a toast message about not having a permission?
        return false;
    }

    /**
     * Closes the LocationManager if it is assigned.
     */
    public void end(){
        if(locationManager != null)
            locationManager.removeUpdates(this);
    }

    /**
     * When location changes and the change is greater than 10m, the coordinates are assigned to the
     * coordinatesArrayList with a timestamp. If the jog program is completed after assigning the new
     * coordinates, we change pass it on to the jog-object.
     *
     * @param location current location
     */
    @Override
    public void onLocationChanged(Location location) {

        if(activity == null){
            end(); // should not run without JogActivity
            return;
        }

        double latitude = (location.getLatitude());
        double longitude =  (location.getLongitude());

        Coordinates current = new Coordinates(latitude, longitude);
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

    /**
     * Gets a list of LatLng from the coordinates in coordinatesArrayList.
     *
     * @return list of LatLng
     * @see List<LatLng>
     */
    public List<LatLng> getLatLngList(){
        List<LatLng> latLngList = new ArrayList<>();

        for(int i = 0; i < coordinatesArrayList.size(); i++){
            latLngList.add(coordinatesArrayList.get(i).getLatLng());
        }

        return latLngList;
    }
}
