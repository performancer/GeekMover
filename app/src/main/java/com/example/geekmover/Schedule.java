package com.example.geekmover;

import android.util.Log;

import com.example.geekmover.data.Day;
import com.example.geekmover.data.Exercise;
import com.example.geekmover.data.IExercise;
import com.example.geekmover.data.Jog;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Schedule implements Serializable {

    private final int millisecondsInDay = 86400000;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

    private ArrayList<Day> days;

    public Schedule(){
        days = new ArrayList<>();
    }

    public void plan() {

        UserData data = UserData.getInstance();
        Day today = getToday();

        int todayIndex = days.indexOf(today);
        int daysPlanned = days.size() - todayIndex;

        for (int i = 0; i < 30 - daysPlanned; i++) {
            int level = data.getLevel();

            planDay(level);

            if(level < 20 && days.size() % data.getPhase() == 0)
                data.setLevel(level + 1);
        }
    }

    private void planDay(int level) {

        Log.d("Debug", "Planning a day...");

        if(days.size() > 0 && days.size() % 3 == 0)
            level = 0; //rest day
        else if(days.size() > 0 && (days.size() - 1) % 3 == 0)
            level--; //easier day

        Date date = getNextDate();
        IExercise[] exercises = new IExercise[0];

        if (level > 0) {
            exercises = new IExercise[]
                    {
                            new Jog(level * 300),
                            new Exercise("push-ups", level * 3),
                            new Exercise("sit-ups", level * 5),
                    };
        }

        days.add(new Day(date, exercises));
    }

    public ArrayList<Day> getDays() {
        return days;
    }

    private Date getNextDate() {
        if (days != null && days.size() > 0)
            return new Date(days.get(days.size() - 1).getDate().getTime() + millisecondsInDay);
        else
            return Calendar.getInstance().getTime();
    }

    public boolean hasPlan(){
        return days != null && days.size() > 0;
    }

    public Day getToday() {

        Date now = Calendar.getInstance().getTime();

        for (Day day : days) {
            if (dateFormat.format(day.getDate()).equals(dateFormat.format(now)))
                return day;
        }

        return null;
    }

    public void replan(){
        days = new ArrayList<>();
        plan();
    }
}
