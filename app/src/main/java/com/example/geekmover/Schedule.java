package com.example.geekmover;

import com.example.geekmover.data.Day;
import com.example.geekmover.data.Exercise;
import com.example.geekmover.data.IExercise;
import com.example.geekmover.data.Jog;

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

        days.add(planDay(slower));
        days.add(planDay(level));
        days.add(planDay(rest));
        days.add(planDay(level));
        days.add(planDay(slower));
        days.add(planDay(level));
        days.add(planDay(rest));
    }

    private Day planDay(int level){
        if(level > 0) {
            IExercise[] exercises = new IExercise[]
                    {
                            new Jog(level * 750),
                            new Exercise("push-ups", level * 3),
                            new Exercise("sit-ups", level * 5),
                    };

            return new Day(exercises);
        }

        return new Day();
    }
}
