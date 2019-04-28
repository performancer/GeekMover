package com.example.geekmover;

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

    private ArrayList<Day> days;

    public ArrayList<Day> getDays() {
        return days;
    }

    public Schedule(){
        days = new ArrayList<>();
    }

    public void planWeek(int level){

        int rest = 0;
        int slower = level - 1;

        planDay(slower);
        planDay(level);
        planDay(rest);
        planDay(level);
        planDay(slower);
        planDay(level);
        planDay(rest);
    }

    private void planDay(int level) {

        if(days == null)
            days = new ArrayList<>();

        Date date = getNextDate();
        Day day;

        if (level > 0) {
            IExercise[] exercises = new IExercise[]
                    {
                            new Jog(level * 750),
                            new Exercise("push-ups", level * 3),
                            new Exercise("sit-ups", level * 5),
                    };

            day = new Day(date, exercises);
        }
        else {
            day = new Day(date);
        }

        days.add(day);
    }

    private Date getNextDate() {
        if (days != null && days.size() > 0)
            return new Date(days.get(days.size() - 1).getDate().getTime() + 86400000);
        else
            return Calendar.getInstance().getTime();
    }

    public boolean hasPlan(){
        return days != null && days.size() > 0;
    }

    public Day getToday() {

        Date now = Calendar.getInstance().getTime();

        for (Day day : days) {
            SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");

            if (fmt.format(day.getDate()).equals(fmt.format(now)))
                return day;
        }

        return null;
    }
}
