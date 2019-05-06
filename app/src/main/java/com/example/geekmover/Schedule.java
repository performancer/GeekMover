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

    /**
     * Schedule manages days and plans exercises for the user.
     */
    public Schedule(){
        days = new ArrayList<>();
    }

    /**
     * Plans days for at least 30 days ahead with the current level
     */
    public void plan() {

        UserData data = UserData.getInstance();
        Day today = getToday();

        int todayIndex = days.indexOf(today);
        int daysPlanned = days.size() - todayIndex;

        for (int i = 0; i < 30 - daysPlanned; i++) {
            planDay(data.getLevel());
        }
    }

    /**
     * Plans a day with the given level and adds it to the list. Every day contains currently
     * three different kind of exercises unless if it is a rest day.
     *
     * @param level level that is used to estimate the amount of exercise for this day
     */
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

    /**
     * Returns an ArrayList of the planned days that the schedule holds.
     *
     * @return The planned days.
     * @see ArrayList<Day>
     */
    public ArrayList<Day> getDays() {
        return days;
    }

    /**
     * Gets the date of the day after the last one on the planned day list. If the list is empty
     * returns current date.
     *
     * @return Date for a new day
     * @see Date
     */
    private Date getNextDate() {
        if (days != null && days.size() > 0)
            return new Date(days.get(days.size() - 1).getDate().getTime() + millisecondsInDay);
        else
            return Calendar.getInstance().getTime();
    }

    /**
     * Checks if there are days planned.
     *
     * @return true if there are days planned, otherwise false
     */
    public boolean hasPlan(){
        return days != null && days.size() > 0;
    }

    /**
     * Returns the day that matches the current date.
     *
     * @return today
     * @see Day
     */
    public Day getToday() {

        Date now = Calendar.getInstance().getTime();

        for (Day day : days) {
            if (dateFormat.format(day.getDate()).equals(dateFormat.format(now)))
                return day;
        }

        return null;
    }

    /**
     * Removes current plans and plans the schedule again.
     */
    public void replan(){
        days = new ArrayList<>();
        plan();
    }
}
