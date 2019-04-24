package com.example.geekmover;
import android.app.Activity;

import java.util.ArrayList;

public class Jog implements IExercise {

    private double goal;
    private ArrayList<Coordinate> coordinates;
    private LocationHandler locationHandler;

    public Jog(double goal)
    {
        this.goal = goal;
    }

    public void addCoordinate(Coordinate coordinate){
        coordinates.add(coordinate);
    }

    public ArrayList<Coordinate> getCoordinates() {
        return coordinates;
    }

    public double getDistance() {
        return 0;
    }

    public double getAverageSpeed() {
        return 0;
    }

    public double getCurrentSpeed() {
        return 0;
    }

    public boolean isFinished(){
        return getDistance() >= goal;
    }

    public boolean start(Activity activity){

        if(locationHandler != null){
            locationHandler.end();
        }

        locationHandler = new LocationHandler(this);
        return locationHandler.start(activity);
    }

    public void end(){
        if(locationHandler != null)
            locationHandler.end();
    }

    @Override
    public double getCaloriesBurned(){
        UserData data = UserData.getInstance();
        return 0;
    }

    @Override
    public String toString() {
        return "jog " + goal + "km";
    }
}
