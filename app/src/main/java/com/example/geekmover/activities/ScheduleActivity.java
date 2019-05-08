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

import com.example.geekmover.R;
import com.example.geekmover.UserData;
import com.example.geekmover.data.Day;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * Has a CalendarView and a ListView.
 * CalendarView shows a calendar with non-planned days grayed out(non clickable).
 * ListView displays Exercises for the chosen day.
 */
public class ScheduleActivity extends AppCompatActivity {

    final SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH);

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

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int dayOfMonth) {
                calendar.set(year, month, dayOfMonth);
                //finds clicked Day in the Schedule
                for (final Day day : days) {
                    if (fmt.format(day.getDate()).equals(fmt.format(calendar.getTime()))) {
                        setupListView(day);
                        break;
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

    /**
     * Sets up the ListView which shows all the exercises for the day.
     * Creates an ArrayAdapter which puts all of the Exercises into the ListView
     *
     * @param day Needs a Day object to display Exercises.
     */
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
                    intent.putExtra(getString(R.string.DATE), fmt.format(day.getDate()));
                    intent.putExtra(getString(R.string.EINDEX), i);
                    startActivity(intent);
                }
            });
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}