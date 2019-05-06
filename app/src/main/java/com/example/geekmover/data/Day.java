package com.example.geekmover.data;

import java.io.Serializable;
import java.util.Date;

public class Day implements Serializable {
    private Date date;
    private IExercise[] exercises;

    /**
     * Day is used to store exercises in and to have a date value of the specific date it
     * represents. Serializable.
     * @param date date for this day
     * @param exercises array of exercises for this day
     */
    public Day(Date date, IExercise[] exercises) {
        this.date = date;
        this.exercises = exercises;
    }

    /**
     * Gets the total calories that are supposed to be burned during this day. Sums the estimated
     * calorie burn of each exercise.
     *
     * @return the goal of calories burned (kcal)
     */
    public double getTotalCaloriesBurned() {

        int calories = 0;

        for(IExercise exercise : exercises){
            calories += exercise.getCaloriesBurned();
        }

        return calories;
    }

    /**
     * Sums the calories burned in every finished exercise of the day.
     *
     * @return current calories burned summed (kcal)
     */
    public double getCurrentCaloriesBurned() {

        int calories = 0;

        for(IExercise exercise : exercises){
            if(exercise.getFinished())
                calories += exercise.getCaloriesBurned();
        }

        return calories;
    }

    /**
     * Gets the array of IExercise interfaces that all the exercises implement. This can be used to
     * get the exercises of the day.
     *
     * @return exercises as an interface
     * @see IExercise
     */
    public IExercise[] getExercises() {
        return exercises;
    }

    /**
     * Gets the date of this day.
     * @return date
     * @see Date
     */
    public Date getDate(){
        return date;
    }

    /**
     * If the day has a jog exercise, this method returns it. Otherwise it will return null.
     * @return Jog for this day
     * @see Jog
     */
    public Jog getJog(){

        for(IExercise exercise : exercises)
        {
            if(exercise instanceof Jog)
                return (Jog)exercise;
        }

        return null;
    }
}
