package com.example.geekmover;

import android.util.JsonWriter;

import com.example.geekmover.data.Day;
import com.example.geekmover.data.IExercise;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

public class DaySerialization {

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
