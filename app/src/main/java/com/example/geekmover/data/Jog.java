package com.example.geekmover.data;

import com.example.geekmover.UserData;

public class Jog implements IExercise {

    private boolean finished;
    private int amount;

    public Jog(int amount)
    {
        this.amount = amount;
    }

    @Override
    public int getAmount(){return amount;}

    @Override
    public double getCaloriesBurned(){
        UserData data = UserData.getInstance();
        return 0;
    }

    @Override
    public boolean getFinished(){
        return finished;
    }

    @Override
    public void setFinished(boolean finished){
        this.finished = finished;
    }

    @Override
    public String toString() {
        return "jog " + amount + "m";
    }
}
