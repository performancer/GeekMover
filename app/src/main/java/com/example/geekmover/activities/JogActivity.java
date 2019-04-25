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

        //meters to kilometers
        double distance = jogProgram.getDistance() / 1000.0;
        double goal = jogProgram.getGoal() / 1000.0;

        TextView goalView = findViewById(R.id.goalView);
        goalView.setText(distance + "km /" + goal + "km");
    }
}