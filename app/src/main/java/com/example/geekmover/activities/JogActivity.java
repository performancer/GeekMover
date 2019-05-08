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

/**
 * JogActivity displays a Google map with current and average movement speed with calories burned.
 * When JogActivity stops, a background service is tarted which keeps updating the list of coordinates in JogProgram.
 */
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

        /* Gets a handle to the map fragment by calling FragmentManager.findFragmentById().
         * Then use getMapAsync() to register for the map callback:
         *
         * Source: https://developers.google.com/maps/documentation/android-sdk/map-with-marker */

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();

        stopService();

        if(jogProgram != null)
            jogProgram.end();
    }

    /**
     * Creates a notification channel through which notifications are possible
     */
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

    /**
     * Starts the background service
     */
    public void startService(){
        Intent serviceIntent = new Intent(this, LocationService.class);
        startService(serviceIntent);
    }

    /**
     * Stops background service
     */
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

        if(map.getPolyline() == null){ // if there is no polyline on the map, create the starting location marker
            map.createMarker(jogProgram.getLatestCoordinates(), getString(R.string.start_location), 0.5f, true);
        } else if(map.getPolyline() != null && map.getLocationMarker() == null) { // if there is a polyline on the map, create the current marker for current location
            map.createMarker(jogProgram.getLatestCoordinates(), getString(R.string.current_location), 1.0f, false);
        } else { // else update the current location's marker position
            map.updateMarker(jogProgram.getLatestCoordinates());
        }

        map.drawPolyLine(jogProgram.getLatLngList()); // draw polyline method. Polyline is created if there isn't one
        map.updateCamera(jogProgram.getLatestCoordinates()); // update the camera position
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map.setMap(googleMap);
    }

    /**
     * When JogActivity is stopped, LocationService starts which keeps the instance of JogProgram alive to still use gps in background
     */
    @Override
    protected void onStop() {
        super.onStop();
        startService();
    }
}