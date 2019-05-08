package com.example.geekmover;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.example.geekmover.activities.JogActivity;

/**
 * LocationService is a background activity which runs even if the app is in background
 */
public class LocationService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    /**
     * Builds the notification for when the app is running in the background.
     * Wasn't mandatory to run a background service before but became so with an android update
     * @param intent
     * @param flags
     * @param startId
     * @return
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        Intent backToJogIntent = new Intent(this, JogActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, backToJogIntent, 0);

        Notification notification = new NotificationCompat.Builder(this, getString(R.string.channel_id))
                .setContentTitle("Location Service")
                .setContentText("Location is being tracked, don't worry")
                .setSmallIcon(R.drawable.ic_android)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1, notification);

        return START_NOT_STICKY;
    }

    /**
     * When a LocationService is stopped, stopSelf() is called which completely ends the service
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        stopSelf();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
