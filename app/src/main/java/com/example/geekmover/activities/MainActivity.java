package com.example.geekmover.activities;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.geekmover.R;
import com.example.geekmover.Schedule;
import com.example.geekmover.UserData;
import com.example.geekmover.data.Day;
import com.example.geekmover.data.Jog;

import java.text.SimpleDateFormat;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            Toast.makeText(MainActivity.this, "You have already granted this permission", Toast.LENGTH_SHORT);
        }else{
            requestLocationPermission();
        }

        UserData data = UserData.getInstance();

        pref = getApplicationContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        data.LoadData(pref, getApplicationContext());

        Schedule schedule = data.getSchedule();
        schedule.plan();

        boolean planned = schedule.hasPlan();

        if(planned)
        {
            Day day = schedule.getToday();

            TextView view = findViewById(R.id.textView);
            view.setText("You are currently at level " + data.getLevel());

            Button button = findViewById(R.id.button);
            button.setText(day.getJog() != null ? "Start a Jog" : "No Jog for today!");

            String text;
            int length = day.getExercises().length;

            if(length > 0) {
                text = "Today " + SimpleDateFormat.getDateInstance().format(day.getDate()) + "\n";

                if (length > 1)
                    text += "There are " + length + " exercises for today\n";
                else
                    text += "There is one exercise for today\n";

                text += day.getCurrentCaloriesBurned() + "/" + day.getTotalCaloriesBurned() + "kcal";
            }
            else {
                text = "Today is a rest day! :)";
            }

            TextView todayText = findViewById(R.id.todayView);
            todayText.setText(text);
        }
    }

    private void requestLocationPermission() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)){
            new AlertDialog.Builder(this).setTitle("Permission needed").setMessage("Application needs to access your location ").setPositiveButton("ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                }
            }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).create().show();
        }else{
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 1){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Permission GRANTED", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onResume(){
        super.onResume();

        TextView view = findViewById(R.id.textView);
        view.setText("You are currently at level " + UserData.getInstance().getLevel());
    }

    @Override
    protected void onStop(){
        super.onStop();
        UserData.getInstance().SaveData(pref, getApplicationContext());
    }

    public void onCalendarClick(View view){
        Intent intent = new Intent(this, ScheduleActivity.class);
        startActivity(intent);
    }

    public void onUserInputClick(View view){
        Intent intent = new Intent(this, UserInputActivity.class);
        startActivity(intent);
    }

    public void OnClick(View view){

        Schedule schedule = UserData.getInstance().getSchedule();

        if(schedule.hasPlan()){

            Day day = schedule.getToday();
            Jog jog = day.getJog();

            if(jog != null)
            {
                Intent intent = new Intent(this, JogActivity.class);
                intent.putExtra("Jog", jog);
                startActivity(intent);
            }
        }
    }
}
