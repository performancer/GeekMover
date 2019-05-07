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

    /**
     * When the activity is created, updates the text views etc accordingly. Checks the needed
     * permissions for the application and UserData singleton loads its data once the activity is
     * created. If user data has not been inserted, UserInputActivity shall be started
     * automatically.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            Toast.makeText(MainActivity.this, R.string.already_granted, Toast.LENGTH_SHORT);
        }else{
            requestLocationPermission();
        }

        UserData data = UserData.getInstance();

        pref = getApplicationContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        data.LoadData(pref, getApplicationContext());

        UpdateViews();

        if(data.getHeight() == 0 || data.getWeight() == 0){
            Intent intent = new Intent(this, UserInputActivity.class);
            startActivity(intent);
        }
    }

    /**
     * Checks if there is a plan and updates it and then updates the views in the activity
     * accordingly.
     */
    private void UpdateViews(){

        UserData data = UserData.getInstance();
        Schedule schedule = data.getSchedule();
        schedule.plan();

        boolean planned = schedule.hasPlan();

        if(planned)
        {
            Day day = schedule.getToday();

            TextView view = findViewById(R.id.textView);
            view.setText(getString(R.string.current_level,data.getLevel()));

            Button button = findViewById(R.id.button);
            button.setText(day.getJog() != null ? R.string.start_jog : R.string.no_jog);

            String text;
            int length = day.getExercises().length;

            if(length > 0) {
                text = getString(R.string.today, SimpleDateFormat.getDateInstance().format(day.getDate())) + "\n";

                if (length > 1)
                    text += getString(R.string.many_exercises, length) + "\n";
                else
                    text += getString(R.string.one_exercise) + "\n";

                text += day.getCurrentCaloriesBurned() + "/" + day.getTotalCaloriesBurned() + " kcal";
            }
            else {
                text = getString(R.string.no_exercises);
            }

            TextView todayText = findViewById(R.id.todayView);
            todayText.setText(text);
        }
    }

    private void requestLocationPermission() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)){
            new AlertDialog.Builder(this).setTitle(R.string.permission_needed).setMessage(R.string.access_location).setPositiveButton(R.string.agree, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                }
            }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
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
                Toast.makeText(this, R.string.permission_granted, Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, R.string.permission_denied, Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * When the activity resumes, views are updated in case for any changes.
     */
    @Override
    protected void onResume(){
        super.onResume();

        UpdateViews();
    }

    /**
     * When the activity stops, UserData is saved.
     */
    @Override
    protected void onStop(){
        super.onStop();
        UserData.getInstance().SaveData(pref, getApplicationContext());
    }

    /**
     * Starts ScheduleActivity when the button is clicked.
     *
     * @param view button that has been clicked
     */
    public void onCalendarClick(View view){
        Intent intent = new Intent(this, ScheduleActivity.class);
        startActivity(intent);
    }

    /**
     * Starts UserInputActivity when the button is clicked
     *
     * @param view button that has been clicked
     */
    public void onUserInputClick(View view){
        Intent intent = new Intent(this, UserInputActivity.class);
        startActivity(intent);
    }

    /**
     * Starts JogActivity when the button is clicked. Gets the jog from schedule in UserData
     * singleton and passes it to the activity as extra.
     *
     * @param view button that has been clicked
     */
    public void OnJogClick(View view){

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
