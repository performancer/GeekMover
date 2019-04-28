package com.example.geekmover;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.JsonWriter;
import android.util.Log;

import com.example.geekmover.data.Day;
import com.example.geekmover.data.Exercise;
import com.example.geekmover.data.IExercise;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class UserData {
    private static final UserData ourInstance = new UserData();

    private Schedule schedule;

    public static UserData getInstance() {
        return ourInstance;
    }

    public Schedule getSchedule(){
        return this.schedule;
    }

    private int level, height, weight;


    private UserData() {
        schedule = new Schedule();
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
