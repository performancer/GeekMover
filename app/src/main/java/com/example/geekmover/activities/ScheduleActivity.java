package com.example.geekmover.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.geekmover.R;
import com.example.geekmover.Schedule;
import com.example.geekmover.UserData;
import com.example.geekmover.data.Day;
import com.example.geekmover.data.Exercise;
import com.example.geekmover.data.IExercise;
import com.example.geekmover.data.Jog;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ScheduleActivity extends AppCompatActivity {

    final SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH);


    public static final String DATE = "DATE";
    public static final String EINDEX = "EINDEX";

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
        final String text = "Day has been planned\n" + calendar.getTime().toString();
        dateText.setText(text);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int dayOfMonth) {
                calendar.set(year, month, dayOfMonth);

                for (final Day day : days) {
                    if (fmt.format(day.getDate()).equals(fmt.format(calendar.getTime()))) {
                        dateText.setText("Day has been planned\n" + day.getDate().toString());
                        setupListView(day);
                        break;
                    }else{
                        dateText.setText("Day has not been planned");
                    }
                }
            }
        });
    }



    @Override
    protected void onStart() {
        super.onStart();

        final Day day = UserData.getInstance().getSchedule().getToday();
        setupListView(day);
    }
    private void setupListView(final Day day) {
        ListView lv = findViewById(R.id.listView);
        try{
            lv.setAdapter(new ArrayAdapter<>(
                    ScheduleActivity.this,
                    android.R.layout.simple_list_item_1,
                    day.getExercises()));
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                    Intent intent = new Intent(ScheduleActivity.this, ExerciseInfoActivity.class);

                    intent.putExtra(DATE, fmt.format(day.getDate()));
                    intent.putExtra(EINDEX, i);

                    startActivity(intent);
                }
            });
        }catch(Exception e){
            e.printStackTrace();
        }
        return;
    }
}