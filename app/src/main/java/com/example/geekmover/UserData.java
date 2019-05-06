package com.example.geekmover;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class UserData {
    private static final UserData ourInstance = new UserData();

    public static UserData getInstance() {
        return ourInstance;
    }

    private Schedule schedule;
    private final String path = "schedule";
    private int level, phase, height, weight;

    private UserData() {

    }

    public int getLevel(){
        return level;
    }

    public void setLevel(int level){
        this.level = level;
    }

    public void setPhase(int phase){
        this.phase = phase;
    }

    public int getPhase(){
        return phase;
    }

    public void setHeight(int h){
        this.height = h;
    }

    public void setWeight(int w){
        this.weight = w;
    }

    public int getHeight() {
        return height;
    }

    public int getWeight() {
        return weight;
    }

    public double getBMI(){
        if(weight <= 0 || height <= 0){
            return 0;
        } else {
            return (Math.round( ( (weight / Math.pow(height / 100d, 2)) *10))/10.0d);
        }
    }

    public Schedule getSchedule(){
        return schedule;
    }

    public void LoadData(SharedPreferences pref, Context context) {
        level = pref.getInt("level", 1);
        phase = pref.getInt("phase", 20);
        height = pref.getInt("height", 0);
        weight = pref.getInt("weight", 0);

        try {
            try (FileInputStream fis = context.openFileInput(path)) {
                try (ObjectInputStream is = new ObjectInputStream(fis)) {
                    schedule = (Schedule) is.readObject();
                }
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
        catch(ClassNotFoundException e){
            e.printStackTrace();
        }
        catch(NullPointerException e){
            e.printStackTrace();
        }

        if(schedule == null)
            schedule = new Schedule();
    }

    public void SaveData(SharedPreferences pref, Context context) {
        SharedPreferences.Editor editor = pref.edit();

        editor.putInt("level", level);
        editor.putInt("phase", phase);
        editor.putInt("height", height);
        editor.putInt("weight", weight);
        editor.apply();

        try {
            FileOutputStream fos = context.openFileOutput(path, Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(schedule);
            os.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
