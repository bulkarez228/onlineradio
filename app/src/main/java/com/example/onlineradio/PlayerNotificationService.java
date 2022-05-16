package com.example.onlineradio;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.annotation.Nullable;

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
        toast("Starting notification!1");
        Log.d(LOG_TAG, "onStartCommand: ");
        switch (intent.getAction()) {
            case Actions.START_SERVICE:
                showNotification();
                toast("Starting notification!");
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
        RemoteViews qsNotification = new RemoteViews(getPackageName(), R.layout.notification_player);
        RemoteViews expandedNotification = new RemoteViews(getPackageName(), R.layout.notification_player);

        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.setAction(Actions.MAIN);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingNotificationIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        Intent closeIntent = new Intent(this, PlayerNotificationService.class);
        closeIntent.setAction(Actions.STOP_SERVICE);
        PendingIntent pendingCloseIntent = PendingIntent.getService(this, 0, closeIntent, 0);

        Intent playPauseIntent = new Intent(this, PlayerNotificationService.class);
        playPauseIntent.setAction(Actions.PLAY);
        PendingIntent playPausePendingIntent = PendingIntent.getService(this, 0, playPauseIntent, 0);

        qsNotification.setOnClickPendingIntent(R.id.button_play_pause, playPausePendingIntent);
        expandedNotification.setOnClickPendingIntent(R.id.button_play_pause, playPausePendingIntent);

        notification = new Notification.Builder(this).build();
        notification.contentView = qsNotification;
        notification.bigContentView = expandedNotification;
        notification.flags = Notification.FLAG_ONGOING_EVENT;
        notification.icon = R.drawable.outline_radio_black_48dp;
        notification.contentIntent = pendingNotificationIntent;
        startForeground(Actions.NOTIFICATION_ID, notification);
    }

    private void toast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}
