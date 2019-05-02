package com.example.geekmover.activities;

import android.content.Intent;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        CalendarView calendarView = findViewById(R.id.calendarView);
        final TextView dateText = findViewById(R.id.dateText);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int dayOfMonth) {

                String dateS = dayOfMonth+"/"+month+"/"+year;
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, dayOfMonth);
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);

                ArrayList<Day> days = UserData.getInstance().getSchedule().getDays();

                for (Day day : days) {
                    SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd", Locale.US);

                    if (fmt.format(day.getDate()).equals(fmt.format(calendar.getTime()))) {
                        dateText.setText("Day has been planned\n" + calendar.getTime().toString());
                        String stringSum = "Exercises for the day:\n";

                        for(int i = 0; i < day.getExercises().length - 1; i++){
                                stringSum += day.getExercises()[i].toString()+"\n";
                        }
                        dateText.setText(stringSum);
                        break;
                    }else{
                        dateText.setText("Day has not been planned");
                    }

                }

            }
        });

    }
}