package com.example.geekmover;

import android.content.SharedPreferences;

public class UserData {
    private static final UserData ourInstance = new UserData();

    public static UserData getInstance() {
        return ourInstance;
    }

    private int level, height, weight;


    private UserData() {
    }

    public int getLevel(){
        return level;
    }

    public void setLevel(int level){
        this.level = level;
    }

    public int getHeight() {
        return height;
    }

    public int getWeight() {
        return weight;
    }

    public double getBMI(){
        return weight / Math.pow(height / 100d, 2);
    }

    public void LoadData(SharedPreferences pref){
        level = pref.getInt("level", 5);
        height = pref.getInt("height", 0);
        weight = pref.getInt("weight", 0);
    }

    public void SaveData(SharedPreferences pref){
        SharedPreferences.Editor editor = pref.edit();

        editor.putInt("level", level);
        editor.putInt("height", height);
        editor.putInt("weight", weight);
        editor.apply();
    }
}
