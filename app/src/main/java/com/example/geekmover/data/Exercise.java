package com.example.geekmover.data;

import com.example.geekmover.UserData;

import java.io.Serializable;

public class Exercise implements IExercise, Serializable {

    private String name;
    private int amount;
    private boolean finished;

    public Exercise(String name, int amount){
        this.name = name;
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    @Override
    public int getAmount() {
        return amount;
    }

    @Override
    public boolean getFinished(){ return finished; }

    @Override
    public void setFinished(boolean finished){ this.finished = finished; }

    @Override
    public double getCaloriesBurned() {
        UserData data = UserData.getInstance();
        return 0;
    }

    @Override
    public String toString() {
        return amount + "x " + name;
    }
}
