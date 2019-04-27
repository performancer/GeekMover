package com.example.geekmover.data;

public class Day {
    private IExercise[] exercises;

    public Day(){
        this.exercises = new IExercise[0];
    }

    public Day(IExercise[] exercises) {
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

    public Jog getJog(){

        for(IExercise exercise : exercises)
        {
            if(exercise instanceof Jog)
                return (Jog)exercise;
        }

        return null;
    }
}
