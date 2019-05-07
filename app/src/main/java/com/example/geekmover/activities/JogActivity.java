package com.example.geekmover.activities;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.geekmover.JogProgram;
import com.example.geekmover.LocationService;
import com.example.geekmover.Map;
import com.example.geekmover.UserData;
import com.example.geekmover.data.Jog;
import com.example.geekmover.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

public class JogActivity extends FragmentActivity implements OnMapReadyCallback {

    public static final String CHANNEL_ID = "mapServiceChannel";
    public static final String JOG_PROGRAM = "JOG_PROGRAM";
    private JogProgram jogProgram;
    private Map map = new Map();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jog);

        createNotificationChannel();

        UserData data = UserData.getInstance();
        Jog jog = data.getSchedule().getToday().getJog();

        if (jog == null) {
            finish();
            return;
        }

        jogProgram = new JogProgram(this, jog);
        jogProgram.start();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();

        stopService();

        if(jogProgram != null)
            jogProgram.end();
    }

    private void createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Map Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }

    public void startService(){
        Intent serviceIntent = new Intent(this, LocationService.class);
        startService(serviceIntent);
    }

    public void stopService(){
        Intent serviceIntent = new Intent(this, LocationService.class);
        stopService(serviceIntent);
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

        if(map.getPolyline() == null){
            map.createStartMarker(jogProgram.getLatestCoordinates());
        } else if(map.getPolyline() != null && map.getLocationMarker() == null) {
            map.createLocationMarker(jogProgram.getLatestCoordinates());
        } else {
            map.updateMarker(jogProgram.getLatestCoordinates());
        }

        map.drawPolyLine(jogProgram.getLatLngList());
        map.updateCamera(jogProgram.getLatestCoordinates());

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map.setMap(googleMap);
    }

    @Override
    protected void onStop() {
        super.onStop();
        startService();
    }
}