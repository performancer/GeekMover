package com.example.geekmover.data;

public interface IExercise {
    int getAmount();
    double getCaloriesBurned();
    boolean getFinished();
    void setFinished(boolean finished);
}
