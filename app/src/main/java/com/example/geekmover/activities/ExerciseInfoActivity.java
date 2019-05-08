package com.example.geekmover.activities;

import android.content.Context;
import android.content.SharedPreferences;
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
 * @author Lauri
 */
public class ExerciseInfoActivity extends AppCompatActivity {

    IExercise exercise = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_info);

        final SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH);
        final Calendar calendar = Calendar.getInstance();

        final String dateString = getIntent().getStringExtra(getString(R.string.DATE));
        int EIndex = getIntent().getIntExtra(getString(R.string.EINDEX), 0);

        final TextView nameView = findViewById(R.id.exerciseName);
        final TextView caloriesView = findViewById(R.id.exerciseCalories);
        final TextView finishedView = findViewById(R.id.exerciseFinished);

        ArrayList<Day> days = UserData.getInstance().getSchedule().getDays();

        String name = getString(R.string.name_unfound);
        double caloriesBurned = 0;
        boolean finished = false;
        //finds Day object where the formatted dates match, uses getters from found Day object to populate the screen with info
        for(Day day : days){
            if (fmt.format(day.getDate()).equals(dateString)) {
                exercise = day.getExercises()[EIndex];
                name = exercise.toString();
                caloriesBurned = exercise.getCaloriesBurned();
                finished = exercise.getFinished();
                break;
            }
        }

        if(finished)
            finishedView.setText(R.string.finished);
        else
            finishedView.setText(R.string.unfinished);

        nameView.setText(name);
        caloriesView.setText(Math.round((caloriesBurned*100.0)/100.0) + " kcal");

        final Button finishButton = findViewById(R.id.completeExercise);

        //if Exercise is a Jog or exercise is finished, hide button
        if(finished || exercise.getClass() == Jog.class)
            finishButton.setVisibility(View.INVISIBLE);

        finishButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                FinishExercise(calendar, fmt, dateString, finishedView);
            }
        });
    }
    /**
     * Finishes Exercise if able, saves data afterwards
     * Prevents click if viewing an exercise in the future or exercise already finished.
     * Displays a toast if button cannot ne used.
     * @throws ParseException
     * @param calendar
     * @param fmt
     * @param dateString
     * @param finishedView
     */
    private void FinishExercise(Calendar calendar, SimpleDateFormat fmt, String dateString, TextView finishedView) {
        try {
            if (calendar.getTime().getTime() >= (fmt.parse(dateString).getTime())) {
                exercise.setFinished(true);
                finishedView.setText(R.string.finished);

                SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
                UserData.getInstance().SaveData(pref, getApplicationContext());
            }else{
                String toastMessage = getString(R.string.cannot_finish);
                Toast message = Toast.makeText(ExerciseInfoActivity.this, toastMessage, Toast.LENGTH_SHORT);
                message.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.TOP, 0, 150);
                message.show();
            }
        }catch(ParseException e){
            e.printStackTrace();
        }
    }
}