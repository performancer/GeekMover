package com.example.geekmover.activities;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.geekmover.JogProgram;
import com.example.geekmover.Map;
import com.example.geekmover.data.Jog;
import com.example.geekmover.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import java.io.Serializable;

public class JogActivity extends FragmentActivity implements OnMapReadyCallback {

    private JogProgram jogProgram;

    private Map map = new Map();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jog);

        Serializable serializable = getIntent().getSerializableExtra("Jog");

        if(serializable instanceof Jog) {
            jogProgram = new JogProgram(this, (Jog)serializable);
            jogProgram.start();
        }
        else{
            finish();
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    public void Update() {
        String text;

        if(jogProgram.isFinished()) {
            text = "Finished";
        }
        else {
            //meters to kilometers rounded to one decimal
            double distance = Math.round(jogProgram.getTotalDistance() / 100.0) / 10.0;
            double goal = Math.round(jogProgram.getGoal() / 100.0) / 10.0;

            text = distance + "/" + goal + "km";
        }

        TextView goalView = findViewById(R.id.goalView);
        goalView.setText(text);

        int average = (int)jogProgram.getAverageSpeed();
        int current = (int)jogProgram.getCurrentSpeed();

        TextView averageSpeedView = findViewById(R.id.averageSpeedView);
        averageSpeedView.setText("Avg. " + average + " m/s");

        TextView currentSpeedView = findViewById(R.id.currentSpeedView);
        currentSpeedView.setText("Cur. " + current + " m/s");

        int calories = jogProgram.getCaloriesBurned();

        TextView caloriesView = findViewById(R.id.caloriesView);
        caloriesView.setText(calories + " kcal");

        map.drawPolyLine(jogProgram.getLatLngList());
        map.updateCamera(jogProgram.getLatestCoordinates());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map.setMap(googleMap);
    }
}