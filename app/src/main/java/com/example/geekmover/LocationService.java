package com.example.geekmover;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.example.geekmover.activities.JogActivity;
import com.example.geekmover.activities.MainActivity;

import java.util.ArrayList;

import static com.example.geekmover.activities.JogActivity.CHANNEL_ID;
import static com.example.geekmover.activities.JogActivity.JOG_PROGRAM;

public class LocationService extends Service {

    private static JogProgram jogProgram;

    public static JogProgram getJogProgram(){
        return jogProgram;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        //jogProgram = (JogProgram) intent.getSerializableExtra(JOG_PROGRAM);

        Intent backToJogIntent = new Intent(this, JogActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, backToJogIntent, 0);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Location Service")
                .setContentText("Location is being tracked, don't worry")
                .setSmallIcon(R.drawable.ic_android)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1, notification);

        return START_NOT_STICKY;
    }

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
