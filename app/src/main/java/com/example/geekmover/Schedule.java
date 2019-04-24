package com.example.geekmover;

import java.util.ArrayList;

public class Schedule {

    private ArrayList<Day> days;

    public ArrayList<Day> getDays() {
        return days;
    }

    public Schedule(){
    }

    public void planWeek(int level){

        int rest = 0;
        int slower = level - 1;

        days.add(new Day(slower));
        days.add(new Day(level));
        days.add(new Day(rest));
        days.add(new Day(level));
        days.add(new Day(slower));
        days.add(new Day(level));
        days.add(new Day(rest));
    }
}
