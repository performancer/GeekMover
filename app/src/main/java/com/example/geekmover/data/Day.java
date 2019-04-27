package com.example.geekmover.data;

import java.util.Date;

public class Day {
    private Date date;
    private IExercise[] exercises;

    public Day(Date date){
        this.date = date;
        this.exercises = new IExercise[0];
    }

    public Day(Date date, IExercise[] exercises) {
        this.date = date;
        this.exercises = exercises;
    }

    public double getTotalCaloriesBurned() {

        int calories = 0;

        for(IExercise exercise : exercises){
            calories += exercise.getCaloriesBurned();
        }

        return calories;
    }

    public double getCurrentCaloriesBurned() {

        int calories = 0;

        for(IExercise exercise : exercises){
            if(exercise.getFinished())
                calories += exercise.getCaloriesBurned();
        }

        return calories;
    }

    public IExercise[] getExercises() {
        return exercises;
    }

    public Date getDate(){
        return date;
    }

    public Jog getJog(){

        for(IExercise exercise : exercises)
        {
            if(exercise instanceof Jog)
                return (Jog)exercise;
        }

        return null;
    }
}
