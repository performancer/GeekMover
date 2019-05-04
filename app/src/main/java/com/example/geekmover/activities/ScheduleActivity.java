package com.example.geekmover.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.TextView;

import com.example.geekmover.R;
import com.example.geekmover.Schedule;
import com.example.geekmover.UserData;
import com.example.geekmover.data.Day;
import com.example.geekmover.data.IExercise;
import com.example.geekmover.data.Jog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ScheduleActivity extends AppCompatActivity {

    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        final ArrayList<Day> days = UserData.getInstance().getSchedule().getDays();
        final Calendar calendar = Calendar.getInstance();

        CalendarView calendarView = findViewById(R.id.calendarView);

        calendarView.setMinDate(Calendar.getInstance().getTime().getTime());
        calendarView.setMaxDate(days.get(days.size()-1).getDate().getTime());

        final TextView dateText = findViewById(R.id.dateText);

        String text = "Exercises for the day:\n";
        for(IExercise exercise : days.get(0).getExercises()) {
            text += exercise.toString() + "\n";
        }
        dateText.setText(text);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int dayOfMonth) {

                calendar.set(year, month, dayOfMonth);

                for (Day day : days) {
                    SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");

                    if (fmt.format(day.getDate()).equals(fmt.format(calendar.getTime()))) {
                        dateText.setText("Day has been planned\n" + calendar.getTime().toString());

                        if(day.getExercises().length > 0) {
                            String text = "Exercises for the day:\n";
                            for(IExercise exercise : day.getExercises())
                                text += exercise.toString() + "\n";

                            dateText.setText(text);
                        }
                        else{
                            dateText.setText("No exercises for this day, rest well!");
                        }

                        break;
                    }else{
                        dateText.setText("Day has not been planned");
                    }
                }
            }
        });

        System.out.println("OnCreate called");
        loadData();
    }

    @Override
    protected void onResume(){
        super.onResume();

        System.out.println("OnResume called");
        loadData();
    }

    private void loadData(){
        UserData data = UserData.getInstance();

        pref = getApplicationContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        data.LoadData(pref, getApplicationContext());

        System.out.println("Level,height,weight " + data.getLevel() + data.getHeight() + data.getWeight());
    }

}