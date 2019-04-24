package com.example.geekmover;

public class Exercise implements IExercise {

    private String name;
    private int amount;

    public Exercise(String name, int amount){
        this.name = name;
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public int getAmount() {
        return amount;
    }

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
