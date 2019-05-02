package com.example.geekmover.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.geekmover.R;

import static java.lang.Math.round;

public class UserInputActivity extends AppCompatActivity {

    private Button validate;
    private TextView textViewHeight;
    private EditText inputHeight;

    private TextView textViewWeight;
    private EditText inputWeight;

    private TextView textViewBMI;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_input);

        validate = findViewById(R.id.validateButton);

        inputHeight = findViewById(R.id.inputHeight);
        textViewHeight = findViewById(R.id.heightTextView);

        inputWeight = findViewById(R.id.inputWeight);
        textViewWeight = findViewById(R.id.weightTextView);

        textViewBMI = findViewById(R.id.bmiTextView);
    }

    public void onValidateClick(View view){
        System.out.println(inputHeight.getText());
        System.out.println(inputWeight.getText());

        textViewHeight.setText(inputHeight.getText());
        textViewWeight.setText(inputWeight.getText());
        //Intent intent = new Intent(this, DisplayMessageActivity.class);

        //String viesti = editText.getText().toString();

        double height = Double.parseDouble(inputHeight.getText().toString());
        double weight = Double.parseDouble(inputWeight.getText().toString());
        double bmi = weight / Math.pow(height / 100d, 2) ;

        bmi = Math.round(bmi*10)/10.0d;

        System.out.println(bmi);

        textViewBMI.setText("" + bmi );
    }


    public void onResetClick(View view){
        textViewHeight.setText("");
        textViewWeight.setText("");
    }

    public void onSaveClick(View view){

    }

}
