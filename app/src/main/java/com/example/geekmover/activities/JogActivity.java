package com.example.geekmover.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.geekmover.Coordinates;
import com.example.geekmover.JogProgram;
import com.example.geekmover.data.Jog;
import com.example.geekmover.R;

public class JogActivity extends AppCompatActivity {

    private JogProgram jogProgram;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jog);

        jogProgram = new JogProgram(this, new Jog(2500));
        jogProgram.start();
    }

    public void Update() {

        Coordinates coordinates = jogProgram.getLatestCoordinates();

        double latitude = coordinates.getLatitude();
        double longitude = coordinates.getLongitude();

        TextView view = findViewById(R.id.coordinatesView);
        view.setText("lat:" + latitude + " long:"+ longitude);

        //meters to kilometers rounded to one decimal
        double distance =  Math.round(jogProgram.getTotalDistance() / 100.0) / 10.0;
        double goal = Math.round(jogProgram.getGoal() / 100.0) / 10.0;

        TextView goalView = findViewById(R.id.goalView);
        goalView.setText(distance + "/" + goal + "km");

        int average = (int)jogProgram.getAverageSpeed();
        int current = (int)jogProgram.getCurrentSpeed();

        TextView averageSpeedView = findViewById(R.id.averageSpeedView);
        averageSpeedView.setText("Avg. " + average + " m/s");

        TextView currentSpeedView = findViewById(R.id.currentSpeedView);
        currentSpeedView.setText("Cur. " + current + " m/s");
    }
}