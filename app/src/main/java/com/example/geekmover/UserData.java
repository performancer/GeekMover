package com.example.geekmover;

import android.content.SharedPreferences;
import android.util.JsonWriter;

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

    public void writeJsonStream(OutputStream out, List<Day> days) throws IOException{
        JsonWriter writer = new JsonWriter(new OutputStreamWriter(out, "UTF-8"));
        writer.setIndent("  ");
        writeDaysArray(writer, days);
        writer.close();
    }

    private void writeDaysArray(JsonWriter writer, List<Day> days) throws IOException {
        writer.beginArray();
        for(Day day : days){
            writeDay(writer, day);
        }
        writer.endArray();
    }

    private void writeDay(JsonWriter writer, Day day) throws IOException {
        writer.beginObject();
        writer.name("date").value(day.getDate().getTime());
        writeExercisesArray(writer, day.getExercises());
        writer.endObject();
    }

    private void writeExercisesArray(JsonWriter writer, IExercise[] exercises) throws IOException {
        writer.beginArray();
        for (IExercise exercise : exercises){
            writeExercise(writer, exercise);
        }
        writer.endArray();
    }

    private void writeExercise(JsonWriter writer, IExercise exercise) throws IOException {
        writer.beginObject();
        writer.name("name").value(exercise.getName());
        writer.name("amount").value(exercise.getAmount());
        writer.name("finished").value(exercise.getFinished());
        writer.endObject();
    }

    public void loadFromJSON(){

    }

}
