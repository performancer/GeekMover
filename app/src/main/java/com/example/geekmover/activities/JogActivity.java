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
                    getString(R.string.channel_id),
                    getString(R.string.map_service_channel),
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
            text = getString(R.string.finished);
        }
        else {
            //meters to kilometers rounded to one decimal
            double distance = Math.round(jogProgram.getTotalDistance() / 100.0) / 10.0;
            double goal = Math.round(jogProgram.getGoal() / 100.0) / 10.0;

            text = getString(R.string.jog_distance, distance, goal);
        }

        TextView goalView = findViewById(R.id.goalView);
        goalView.setText(text);

        int average = (int)jogProgram.getAverageSpeed();
        int current = (int)jogProgram.getCurrentSpeed();

        TextView averageSpeedView = findViewById(R.id.averageSpeedView);
        averageSpeedView.setText(getString(R.string.average_speed, average));

        TextView currentSpeedView = findViewById(R.id.currentSpeedView);
        currentSpeedView.setText(getString(R.string.current_speed, current));

        int calories = jogProgram.getCaloriesBurned();

        TextView caloriesView = findViewById(R.id.caloriesView);
        caloriesView.setText(getString(R.string.calories, calories));

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

    @Override
    protected void onDestroy(){
        super.onDestroy();
        stopService();
    }
}