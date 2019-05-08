package com.example.geekmover.data;

import com.example.geekmover.UserData;

/**
 * Extends the Exercise class which is used to store data. This class is specifically used for
 * jog exercises, which have different way of calculating calorie burn and cannot be finished
 * without a jog program.
 */
public class Jog extends Exercise {

    /**
     * Constructor for Jog
     *
     * @param amount Distance the user is supposed to jog (in meters)
     */
    public Jog(int amount) {
        super("jog", amount);
    }

    /**
     * Gets the amount of calories that are estimated to be burned with this jog. User's
     * BMI affects the estimated burn.
     * @return double value of calories burned
     */
    @Override
    public double getCaloriesBurned(){
        UserData data = UserData.getInstance();

        //the following might not be highly accurate

        double speed = 4; //what is usually our average speed? should we store data for this?
        double seconds = getAmount() / speed;
        double factor = (speed * (0.7 * speed + 56)) / 360;
        double bmi = 1.0 + (data.getBMI() - 20) / 20;

        return factor * bmi * seconds;
    }

    /**
     * Used to get the essential data from the instance to a string
     * @return string with the exercise name and the goal distance
     */
    @Override
    public String toString() {
        return getName() + " " + getAmount() + "m";
    }
}
