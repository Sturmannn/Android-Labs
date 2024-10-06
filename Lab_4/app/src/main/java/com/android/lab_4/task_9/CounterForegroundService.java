package com.android.lab_4.task_9;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class CounterForegroundService extends Service {

    private static final String CHANNEL_ID = "counter_channel";
    private static final int NOTIFICATION_ID = 1;
    private int counter = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
        startForeground(NOTIFICATION_ID, createNotification());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String action = intent.getAction();
            if ("INCREMENT".equals(action)) {
                counter++;
            } else if ("RESET".equals(action)) {
                counter = 0;
            }
            Notification notification = createNotification();
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.notify(NOTIFICATION_ID, notification);
        }

        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private Notification createNotification() {
        // Intent для действия "Добавить"
        Intent incrementIntent = new Intent(this, CounterForegroundService.class);
        incrementIntent.setAction("INCREMENT");

        PendingIntent incrementPendingIntent = PendingIntent.getService(
                this,
                0,
                incrementIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        // Intent для действия "Сбросить"
        Intent resetIntent = new Intent(this, CounterForegroundService.class);
        resetIntent.setAction("RESET");

        PendingIntent resetPendingIntent = PendingIntent.getService(
                this,
                0,
                resetIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        // Создание уведомления
        return new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Счётчик")
                .setContentText("Текущий счёт: " + counter)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .addAction(0, "Добавить", incrementPendingIntent)
                .addAction(0, "Сбросить", resetPendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Counter Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(serviceChannel);
            }
        }
    }
}