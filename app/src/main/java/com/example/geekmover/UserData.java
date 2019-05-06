package com.example.geekmover;

import android.content.Context;
import android.content.SharedPreferences;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * UserData is used to store data to a singleton which can be called at anytime in the application.
 */
public class UserData {
    private static final UserData ourInstance = new UserData();

    public static UserData getInstance() {
        return ourInstance;
    }

    private Schedule schedule;
    private final String path = "schedule";
    private int level, phase, height, weight;

    /**
     * Constructor for UserData
     */
    private UserData() {

    }

    /**
     * getter method for level
     *
     * @return level
     */
    public int getLevel(){
        return level;
    }

    /**
     * setter method for level
     *
     * @param level desired value
     */
    public void setLevel(int level){
        this.level = level;
    }

    public void setPhase(int phase){
        this.phase = phase;
    }

    public int getPhase(){
        return phase;
    }

    /**
     * setter method for user height
     *
     * @param h height
     */
    public void setHeight(int h){
        this.height = h;
    }

    /**
     * setter method for user weight
     *
     * @param w weight
     */
    public void setWeight(int w){
        this.weight = w;
    }

    /**
     * getter method for user height
     *
     * @return height
     */
    public int getHeight() {
        return height;
    }

    /**
     * getter method for user weight
     *
     * @return weight
     */
    public int getWeight() {
        return weight;
    }

    /**
     * calculates BMI from user weight and height
     *
     * @return BMI
     */
    public double getBMI(){
        if(weight <= 0 || height <= 0){
            return 0;
        } else {
            return (Math.round( ( (weight / Math.pow(height / 100d, 2)) *10))/10.0d);
        }
    }

    /**
     * getter method for schedule
     *
     * @return schedule
     */
    public Schedule getSchedule(){
        return schedule;
    }

    /**
     * Loads user data from SharedPreferences and schedule data from a file.
     *
     * @param pref SharedPreference from the data is get
     * @param context Context to open file input
     */
    public void LoadData(SharedPreferences pref, Context context) {
        level = pref.getInt("level", 1);
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

    /**
     * Saves user data to SharedPreferences and schedule data to a file.
     *
     * @param pref SharedPreferences that the data is put in
     * @param context Context that is used to open file output
     */
    public void SaveData(SharedPreferences pref, Context context) {
        SharedPreferences.Editor editor = pref.edit();

        editor.putInt("level", level);
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
