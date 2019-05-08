package com.example.geekmover.data;

import com.example.geekmover.UserData;
import java.io.Serializable;

/**
 * Exercise implements the interface IExercise. Is used to store data of an exercise that the user
 * is supposed to complete during the given day. Serializable.
 */
public class Exercise implements IExercise, Serializable {

    private String name;
    private int amount;
    private boolean finished;

    /**
     * Constructor for Exercise
     *
     * @param name name of the exercise
     * @param amount amount of repeats for this exercise
     */
    public Exercise(String name, int amount){
        this.name = name;
        this.amount = amount;
    }

    /**
     Gets the name of the exercise
     @return name of the exercise
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the desired amount of repeats of the exercise
     * @return exercise amount
     */
    @Override
    public int getAmount() {
        return amount;
    }

    /**
     * Returns the boolean value of the exercise state, whether it is finished or not
     * @return is the exercise finished
     */
    @Override
    public boolean getFinished(){ return finished; }

    /**
     * Sets the exercise's state to desired value that describes if the exercise is finished or not
     * @param finished value that will be assigned
     */
    @Override
    public void setFinished(boolean finished){ this.finished = finished; }

    /**
     * Gets the amount of calories that are estimated to be burned with this exercise. User's
     * BMI affects the estimated burn.
     * @return double value of calories burned
     */
    @Override
    public double getCaloriesBurned() {
        UserData data = UserData.getInstance();

        //might not be highly accurate
        double bmi = 1.0 + (data.getBMI() - 20) / 20;
        return 0.57 * amount * bmi;
    }

    /**
     * Used to get the essential data from the instance to a string
     * @return string with amount of exercise and the name of the exercise
     */
    @Override
    public String toString() {
        return amount + "x " + name;
    }
}
