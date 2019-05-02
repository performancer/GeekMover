package com.example.geekmover.activities;

import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
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
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//public class JogActivity extends AppCompatActivity {
public class JogActivity extends FragmentActivity implements OnMapReadyCallback {

    private JogProgram jogProgram;

    GoogleMap map;

    //List<LatLng> points = new ArrayList<>();
    private Polyline polyline = null;
    private PolylineOptions polylineOptions = null;



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

        drawPolyLine();
        updateCamera();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
    }

    public void drawPolyLine(){
        if(polylineOptions == null){ // create a polyline if there is no polyline
            createPolyLine();
        }

        if (polylineOptions != null) { // if polyline exists, update it
            updatePolyLine();
        }
    }

    public void createPolyLine(){
        polylineOptions = new PolylineOptions().width(3).color(Color.RED).geodesic(true);
        polyline = map.addPolyline(polylineOptions);
    }

    public void updatePolyLine(){
        List<LatLng> latLngList = jogProgram.getLatLngList();

        polyline.setPoints(latLngList); //draw a polyline based on all points

        System.out.println(Arrays.toString(latLngList.toArray()));
    }

    public void updateCamera(){
        Coordinates coordinates = jogProgram.getLatestCoordinates();

        LatLng myLocation = coordinates.getLatLng();

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 15f));
    }

}