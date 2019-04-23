package com.example.geekmover;
import java.util.ArrayList;

public class Jog implements IExercise {

    private double goal;
    private ArrayList<Coordinate> coordinates;

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

    @Override
    public double getCaloriesBurned(){
        return 0;
    }

    @Override
    public String toString() {
        return "jog " + goal + "km";
    }
}
