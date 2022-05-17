package com.example.onlineradio;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class PlayerNotificationService extends Service {

    public static interface  Actions {
        public static final String MAIN          = "MAIN";
        public static final String INIT          = "INIT";
        public static final String PLAY          = "PLAY";
        public static final String START_SERVICE = "START_SERVICE";
        public static final String STOP_SERVICE  = "STOP_SERVICE";
        public static final int NOTIFICATION_ID  = 101;
    }

    private static final String LOG_TAG = "NotificationService";

    private Notification notification;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(LOG_TAG, "onStartCommand: invoked!");
        switch (intent.getAction()) {
            case Actions.START_SERVICE:
                showNotification();
                Log.d(LOG_TAG, "onStartCommand: Starting notification and service!");
                break;
            case Actions.MAIN:
                break;
            case Actions.PLAY:
                break;
            case Actions.STOP_SERVICE:
                break;
        }
        return START_STICKY;
    }

    private void showNotification() {
        RemoteViews qsNotification = new RemoteViews(getPackageName(), R.layout.notification_player_qs);
        RemoteViews expandedNotification = new RemoteViews(getPackageName(), R.layout.notification_player_expanded);

        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.setAction(Actions.MAIN);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingNotificationIntent;

        Intent closeIntent = new Intent(this, PlayerNotificationService.class);
        closeIntent.setAction(Actions.STOP_SERVICE);
        PendingIntent pendingCloseIntent;

        Intent playPauseIntent = new Intent(this, PlayerNotificationService.class);
        playPauseIntent.setAction(Actions.PLAY);
        PendingIntent playPausePendingIntent;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            pendingNotificationIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);
            playPausePendingIntent = PendingIntent.getService(this, 0, playPauseIntent, PendingIntent.FLAG_IMMUTABLE);
            pendingCloseIntent = PendingIntent.getService(this, 0, closeIntent, PendingIntent.FLAG_IMMUTABLE);
        }
        else {
            pendingNotificationIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
            playPausePendingIntent = PendingIntent.getService(this, 0, playPauseIntent, 0);
            pendingCloseIntent = PendingIntent.getService(this, 0, closeIntent, 0);
        }

        qsNotification.setOnClickPendingIntent(R.id.play_pause, playPausePendingIntent);
        expandedNotification.setOnClickPendingIntent(R.id.play_pause, playPausePendingIntent);


        NotificationCompat.Builder notificationBuilder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("PLAYER_CHANNEL", "Radio Player motification channel", NotificationManager.IMPORTANCE_HIGH);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            manager.createNotificationChannel(channel);

            notificationBuilder = new NotificationCompat.Builder(this, "PLAYER_CHANNEL");
        }
        else notificationBuilder = new NotificationCompat.Builder(this);

        notificationBuilder.setCategory(NotificationCompat.CATEGORY_SERVICE)
                .setCustomBigContentView(expandedNotification)
                .setCustomContentView(qsNotification);
        notification = notificationBuilder.build();
        notification.flags = Notification.FLAG_FOREGROUND_SERVICE;
        notification.icon = R.drawable.outline_radio_black_48dp;
        notification.contentIntent = pendingNotificationIntent;
        startForeground(Actions.NOTIFICATION_ID, notification);
    }

    private void toast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}
