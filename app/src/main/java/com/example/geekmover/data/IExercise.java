package com.example.geekmover.data;
import java.io.Serializable;

public interface IExercise {
    int getAmount();
    double getCaloriesBurned();
    boolean getFinished();
    void setFinished(boolean finished);
}
