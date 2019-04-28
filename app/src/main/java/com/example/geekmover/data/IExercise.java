package com.example.geekmover.data;

public interface IExercise {
    String getName();
    int getAmount();
    double getCaloriesBurned();
    boolean getFinished();
    void setFinished(boolean finished);
}
