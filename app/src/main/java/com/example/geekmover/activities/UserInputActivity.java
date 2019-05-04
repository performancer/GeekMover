package com.example.geekmover.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.geekmover.R;
import com.example.geekmover.UserData;

import java.sql.SQLOutput;

import static java.lang.Math.round;

public class UserInputActivity extends AppCompatActivity {

    private UserData userData = UserData.getInstance( );

    private static final String PREFS = "PrefsFile";

    private SharedPreferences pref;

    //private Button validate;
    private TextView textViewHeight;
    private EditText inputHeight;

    private TextView textViewWeight;
    private EditText inputWeight;

    private TextView textViewBMI;

    private EditText inputLevel;

    //private TextView showBMI;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_input);

        /*
        prefs = getApplicationContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        userData.LoadData(prefs, getApplicationContext());
        */

        //loadData();


        //validate = findViewById(R.id.saveButton);

        inputHeight = findViewById(R.id.inputHeight);
        textViewHeight = findViewById(R.id.heightTextView);

        inputWeight = findViewById(R.id.inputWeight);
        textViewWeight = findViewById(R.id.weightTextView);

        textViewBMI = findViewById(R.id.showBMITextView);

        inputLevel = findViewById(R.id.inputLevel);

        textViewHeight.setText(Integer.toString(userData.getHeight()));
        inputHeight.setText(Integer.toString(userData.getHeight()));

        textViewWeight.setText(Integer.toString(userData.getWeight()));
        inputWeight.setText(Integer.toString(userData.getWeight()));

        textViewBMI.setText(Double.toString(userData.getBMI()));

        inputLevel.setText(Integer.toString(userData.getLevel()));

    }

    @Override
    protected void onStart(){
        super.onStart();

        /*
        prefs = getApplicationContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        userData.LoadData(prefs, getApplicationContext());
        */
        //loadData();

    }

    @Override
    protected void onResume(){
        super.onResume();

        /*
        prefs = getApplicationContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        userData.LoadData(prefs, getApplicationContext());
        */
        //loadData();

    }



    @Override
    protected void onPause(){
        super.onPause();
        //Log.d(TAG, "OnPause Called");

        //save here
        //saveData();
        /*
        prefs = getApplicationContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        userData.LoadData(prefs, getApplicationContext());
        */
        //loadData();
    }

    @Override
    protected void onRestart(){
        super.onRestart();

        //Log.d(TAG, "OnRestart Called");
        /*
        prefs = getApplicationContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        userData.LoadData(prefs, getApplicationContext());
        */

        //loadData();
    }

    @Override
    protected void onStop(){
        super.onStop();

        /*
        prefs = getApplicationContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        userData.LoadData(prefs, getApplicationContext());
        */

        //loadData();
        //Log.d(TAG, "OnStop Called");
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        /*
        prefs = getApplicationContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        userData.LoadData(prefs, getApplicationContext());
        */

        //loadData();
        //Log.d(TAG, "OnDestroy Called");
    }

    public void onSaveClick(View view){
        System.out.println(inputHeight.getText());
        System.out.println(inputWeight.getText());

            textViewHeight.setText(inputHeight.getText());

            textViewWeight.setText(inputWeight.getText());

        try {
            userData.setHeight( Integer.parseInt(inputHeight.getText().toString()) );
        }

        catch (NumberFormatException e) {
            System.out.print("Problem" + e);
            textViewHeight.setText("");
        }

        try {
            userData.setWeight( Integer.parseInt(inputWeight.getText().toString()) );
        }

        catch (NumberFormatException e) {
            System.out.print("Problem: " + e);
            textViewWeight.setText("");
        }

        try{
            userData.setLevel( Integer.parseInt(inputLevel.getText().toString()) );
            if(userData.getLevel() > 20){
                userData.setLevel(20);
                inputLevel.setText("" + 20);

            }

        } catch (NumberFormatException e) {
            System.out.println("Problem: " + e);
            inputLevel.setText("");
        }

        double bmi = userData.getBMI();

        //bmi = Math.round(bmi*10)/10.0d;

        System.out.println(bmi);

        textViewBMI.setText("" + bmi );

        //UserData.getInstance().SaveData(prefs, getApplicationContext());
        saveData();
    }


    public void onResetClick(View view){
        inputHeight.setText("");
        inputWeight.setText("");
        inputLevel.setText("");
        textViewHeight.setText("Height");
        textViewWeight.setText("Weight");
        textViewBMI.setText("BMI");

        //userData.setHeight(0);
        //userData.setWeight(0);
    }

    public void onLoadClick(View view){
        /*
        prefs = getApplicationContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        userData.LoadData(prefs, getApplicationContext());
        */
        //loadData();

        inputHeight.setText(Integer.toString(userData.getHeight()));
        inputWeight.setText(Integer.toString(userData.getWeight()));
        textViewHeight.setText(Integer.toString(userData.getHeight()));
        textViewWeight.setText(Integer.toString(userData.getWeight()));
        textViewBMI.setText(Double.toString(userData.getBMI()));

        inputLevel.setText(Integer.toString(userData.getLevel()));

    }

    private void saveData(){
        pref = getApplicationContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        UserData.getInstance().SaveData(pref, getApplicationContext());
    }

    private void loadData(){
        //prefs = getApplicationContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        //userData.LoadData(prefs, getApplicationContext());
    }


}
