package com.example.geekmover.activities;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.geekmover.Coordinates;
import com.example.geekmover.JogProgram;
import com.example.geekmover.data.Jog;
import com.example.geekmover.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.Serializable;

//public class JogActivity extends AppCompatActivity {
public class JogActivity extends FragmentActivity implements OnMapReadyCallback {

    private JogProgram jogProgram;

    GoogleMap map;

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

        Coordinates coordinates = jogProgram.getLatestCoordinates();

        double latitude = coordinates.getLatitude();
        double longitude = coordinates.getLongitude();

        TextView view = findViewById(R.id.coordinatesView);
        view.setText("lat:" + latitude + " long:"+ longitude);

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
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        LatLng Vihti = new LatLng (60.417335, 24.324265);
        map.addMarker(new MarkerOptions().position(Vihti).title("Taistelu-Jaskan Kotimaa"));
        map.moveCamera(CameraUpdateFactory.newLatLng(Vihti));
    }
}