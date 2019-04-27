package com.example.geekmover.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.geekmover.R;
import com.example.geekmover.Schedule;
import com.example.geekmover.data.Day;
import com.example.geekmover.data.Jog;

public class MainActivity extends AppCompatActivity {

    private Schedule schedule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        schedule = new Schedule();

        boolean planned = schedule.hasPlan();

        TextView textView = findViewById(R.id.textView);
        textView.setText(planned ? "Schedule is planned" : "Schedule is not planned");

        Button button = findViewById(R.id.button);
        button.setText(planned ? "Start a Jog" : "Plan a Schedule");

    }

    public void OnClick(View view){
        boolean planned = schedule.hasPlan();

        if(planned){

            Day day = schedule.getDays().get(0);
            Jog jog = day.getJog();

            if(jog != null)
            {
                Intent intent = new Intent(this, JogActivity.class);
                intent.putExtra("Jog", jog);
                startActivity(intent);
            }
        }
        else {
            schedule.planWeek(2);

            TextView textView = findViewById(R.id.textView);
            textView.setText("Schedule is planned");

            Day day = schedule.getDays().get(0);
            Jog jog = day.getJog();

            Button button = findViewById(R.id.button);
            button.setText(jog != null ? "Start a Jog" : "No Jog for you!");
        }
    }
}
