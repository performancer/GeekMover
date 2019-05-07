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

/**
 * Has TextFields, EditTexts and a Button named 'Save'
 * TextFields convey what kind of information the user should input and displays the calculated BMI based on height and weight input
 * EditTexts allows the user to input height, weight and the desired level of difficulty
 * Button named 'Save' allows the user to save the inputs to UserData class if there are no incorrect inputs and returns the user to MainActivity
 * @author Emil
 */
public class UserInputActivity extends AppCompatActivity {

    private UserData userData = UserData.getInstance();
    private SharedPreferences pref;

    private EditText inputHeight;
    private EditText inputWeight;
    private EditText inputLevel;

    private TextView textViewBMI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_input);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        pref = getApplicationContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);

        loadData();

        inputHeight = findViewById(R.id.inputHeight);
        inputWeight = findViewById(R.id.inputWeight);
        textViewBMI = findViewById(R.id.showBMITextView);
        inputLevel = findViewById(R.id.inputLevel);

        if(userData.getHeight() == 0){
            inputHeight.setText("");
        } else {
            inputHeight.setText(Integer.toString(userData.getHeight()));
        }

        if(userData.getWeight() == 0){
            inputHeight.setText("");
        } else {
            inputWeight.setText(Integer.toString(userData.getWeight()));
        }

        textViewBMI.setText(Double.toString(userData.getBMI()));
        inputLevel.setText(Integer.toString(userData.getLevel()));
    }

    /**
     * Saves the user inputs in the UserData class and SharedPreferences, if there are no incorrect inputs
     * If the level variable is changed, the Schedule is updated
     * @param view view for the button onClick event
     * @see Schedule
     */
    public void onSaveClick(View view){
        int levelBeforeSave = userData.getLevel();
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
            if (weightInput >= 20 && weightInput <= 400 ){
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

        double bmi = userData.getBMI();

        textViewBMI.setText("" + bmi );

        showToast(noErrors);

        if(noErrors){

            saveData();

            if(levelBeforeSave != userData.getLevel()){
                Schedule schedule = UserData.getInstance().getSchedule();
                schedule.replan();
            }

            finish();
        }
    }

    /**
     * Saves the UserData by SharedPreferences
     * @see UserData
     */
    private void saveData(){
        UserData.getInstance().SaveData(pref, getApplicationContext());
    }

    /**
     * Loads the UserData by SharedPreferences
     * @see UserData
     */
    private void loadData(){
       UserData.getInstance().LoadData(pref, getApplicationContext());
    }

    /**
     * Toast messages to display to the user whether data is saved or not based on input
     * @param noErrors
     */
    private void showToast(boolean noErrors){
        String toastMessage;
        if(noErrors){
            toastMessage = getString(R.string.data_saved);
        } else {
            toastMessage = getString(R.string.incorrect_input);
        }

        Toast saveMessage = Toast.makeText(UserInputActivity.this, toastMessage, Toast.LENGTH_SHORT);
        saveMessage.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.TOP, 0, 150);
        saveMessage.show();
    }

}
