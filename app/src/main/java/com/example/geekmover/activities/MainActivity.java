package com.example.geekmover.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.geekmover.R;
import com.example.geekmover.Schedule;
import com.example.geekmover.UserData;
import com.example.geekmover.data.Day;
import com.example.geekmover.data.Jog;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        UserData data = UserData.getInstance();

        pref = getApplicationContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        data.LoadData(pref, getApplicationContext());

        boolean planned = data.getSchedule().hasPlan();

        TextView textView = findViewById(R.id.textView);
        textView.setText(planned ? "Schedule is planned" : "Schedule is not planned");

        Button button = findViewById(R.id.button);
        button.setText(planned ? "Start a Jog" : "Plan a Schedule");
    }

    @Override
    protected void onStop(){
        super.onStop();
        UserData.getInstance().SaveData(pref, getApplicationContext());
    }

    public void OnClick(View view){

        Schedule schedule = UserData.getInstance().getSchedule();

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
            schedule.planWeek(UserData.getInstance().getLevel());

            TextView textView = findViewById(R.id.textView);
            textView.setText("Schedule is planned");

            Day day = schedule.getToday();
            Jog jog = day.getJog();

            Button button = findViewById(R.id.button);
            button.setText(jog != null ? "Start a Jog" : "No Jog for you!");
        }
    }
}
