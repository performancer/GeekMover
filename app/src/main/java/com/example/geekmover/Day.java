package com.example.geekmover;

public class Day {
    private IExercise[] exercises;

    public Day(int level) {

        if(level <= 0) {
            exercises = new IExercise[]{};
        }
        else {
            exercises = new IExercise[]
                    {
                            new Jog((double) level),
                            new Exercise("push-ups", level * 3),
                            new Exercise("sit-ups", level * 5),
                    };
        }
    }

    public double getCaloriesBurned() {
        return 0;
    }

    public IExercise[] getExercises() {
        return exercises;
    }
}
