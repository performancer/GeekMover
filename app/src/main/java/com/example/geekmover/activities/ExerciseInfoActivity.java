package com.example.geekmover.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.geekmover.R;
import com.example.geekmover.Schedule;
import com.example.geekmover.UserData;
import com.example.geekmover.data.Day;
import com.example.geekmover.data.Exercise;
import com.example.geekmover.data.IExercise;
import com.example.geekmover.data.Jog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Displays specific info on an exercise, gets the exercise from ScheduleActivity with an intent.
 */
public class ExerciseInfoActivity extends AppCompatActivity {

    IExercise exercise = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_info);

        final SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH);
        final Calendar calendar = Calendar.getInstance();

        final String dateString = getIntent().getStringExtra(ScheduleActivity.DATE);
        int EIndex = getIntent().getIntExtra(ScheduleActivity.EINDEX, 0);

        ArrayList<Day> days = UserData.getInstance().getSchedule().getDays();

        String name = "Could not find Name";
        double caloriesBurned = 0;
        boolean finished = false;

        for(Day day : days){
            if (fmt.format(day.getDate()).equals(dateString)) {
                exercise = day.getExercises()[EIndex];
                name = exercise.toString();
                caloriesBurned = exercise.getCaloriesBurned();
                finished = exercise.getFinished();
                break;
            }
        }
        final TextView nameView = findViewById(R.id.exerciseName);
        final TextView caloriesView = findViewById(R.id.exerciseCalories);
        final TextView finishedView = findViewById(R.id.exerciseFinished);

        if(finished){
            finishedView.setText("Finished");
        }else{
            finishedView.setText("Unfinished");
        }

        nameView.setText(name);
        caloriesView.setText(Math.round((caloriesBurned*100.0)/100.0) + " kcal");

        final Button finishButton = findViewById(R.id.completeExercise);

        if(finished || exercise.getClass() == Jog.class){
            finishButton.setVisibility(View.INVISIBLE);
        }
        finishButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                try {
                    if (calendar.getTime().getTime() >= (fmt.parse(dateString).getTime())) {
                        exercise.setFinished(true);
                        finishedView.setText("Finished");
                    }else{
                        //Kopioitu Emilin koodista
                        String toastMessage = "Cannot finish exercise in the future";
                        Toast message = Toast.makeText(ExerciseInfoActivity.this, toastMessage, Toast.LENGTH_SHORT);
                        message.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.TOP, 0, 150);
                        message.show();
                    }
                }catch(ParseException e){
                    e.printStackTrace();
                }
            }
        });
    }
}
