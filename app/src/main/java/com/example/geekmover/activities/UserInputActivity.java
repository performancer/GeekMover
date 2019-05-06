package com.example.geekmover.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.geekmover.R;
import com.example.geekmover.Schedule;
import com.example.geekmover.UserData;

public class UserInputActivity extends AppCompatActivity {

    private UserData userData = UserData.getInstance();
    private SharedPreferences pref;

    private EditText inputHeight;
    private EditText inputWeight;
    private EditText inputLevel;
    private EditText inputPhase;

    private TextView textViewBMI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_input);

        loadData();

        inputHeight = findViewById(R.id.inputHeight);
        inputWeight = findViewById(R.id.inputWeight);
        textViewBMI = findViewById(R.id.showBMITextView);
        inputLevel = findViewById(R.id.inputLevel);
        inputPhase = findViewById(R.id.inputPhase);

        inputHeight.setText(Integer.toString(userData.getHeight()));
        inputWeight.setText(Integer.toString(userData.getWeight()));
        textViewBMI.setText(Double.toString(userData.getBMI()));
        inputLevel.setText(Integer.toString(userData.getLevel()));
        inputPhase.setText(Integer.toString(userData.getPhase()));
    }

    public void onSaveClick(View view){
        int levelBeforeSave = userData.getLevel();
        int phaseBeforeSave = userData.getPhase();
        boolean noErrors = true;

        try {
            int heightInput = Integer.parseInt(inputHeight.getText().toString());
            if(heightInput >= 50 && heightInput <= 300){
                userData.setHeight(heightInput);
            } else {
                noErrors = false;
            }
        }

        catch (NumberFormatException e) {
            System.out.print("Problem" + e);
            noErrors = false;
        }

        try {
            int weightInput = Integer.parseInt(inputWeight.getText().toString());
            if (weightInput >= 20 || weightInput <= 400 ){
                userData.setWeight(weightInput);
            } else {
                noErrors = false;
            }
        }

        catch (NumberFormatException e) {
            System.out.print("Problem: " + e);
            noErrors = false;
        }

        try{
            userData.setLevel( Integer.parseInt(inputLevel.getText().toString()) );
            if(userData.getLevel() > 20){
                userData.setLevel(20);
                inputLevel.setText("" + 20);
            } else if (userData.getLevel() <= 0) {
                userData.setLevel(1);
                inputLevel.setText("" + 1);
            }

        } catch (NumberFormatException e) {
            inputLevel.setText("");
            noErrors = false;
        }

        try{
            userData.setPhase( Integer.parseInt(inputPhase.getText().toString()) );
            if(userData.getPhase() > 20){
                userData.setPhase(20);
                inputPhase.setText("" + 20);
            } else if (userData.getPhase() <= 0){
                userData.setPhase(1);
                inputPhase.setText("" + 1);
            }

        } catch (NumberFormatException e) {
            inputPhase.setText("");
            noErrors = false;
        }

        double bmi = userData.getBMI();

        textViewBMI.setText("" + bmi );

        showToast(noErrors);

        if(noErrors){

            saveData();

            if(levelBeforeSave != userData.getLevel() || phaseBeforeSave != userData.getPhase()){
                Schedule schedule = UserData.getInstance().getSchedule();
                schedule.replan();
            }

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    private void saveData(){
        pref = getApplicationContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        UserData.getInstance().SaveData(pref, getApplicationContext());
    }

    private void loadData(){
        UserData data = UserData.getInstance();

        pref = getApplicationContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        data.LoadData(pref, getApplicationContext());
    }

    private void showToast(boolean noErrors){
        String toastMessage;
        if(noErrors){
            toastMessage = "Data Saved";
        } else {
            toastMessage = "Incorrect Input";
        }

        Toast saveMessage = Toast.makeText(UserInputActivity.this, toastMessage, Toast.LENGTH_SHORT);
        saveMessage.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.TOP, 0, 150);
        saveMessage.show();
    }

}
