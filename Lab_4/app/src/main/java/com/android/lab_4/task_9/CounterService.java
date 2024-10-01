package com.android.lab_4.task_9;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RemoteViews;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.android.lab_4.R;

public class CounterService extends Service {

    public static final String CHANNEL_ID = "CounterServiceChannel";
    private static final int NOTIFICATION_ID = 1;
    private int counter = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        startForegroundService();
    }

    private void startForegroundService() {
        createNotificationChannel();

        RemoteViews notificationLayout = new RemoteViews(getPackageName(), R.layout.notification_layout);

        // Обработка кнопки "Добавить" в уведомлении
        Intent addIntent = new Intent(this, CounterService.class);
        addIntent.setAction("ADD");
        PendingIntent addPendingIntent = PendingIntent.getService(this, 0, addIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        notificationLayout.setOnClickPendingIntent(R.id.btnNotificationAdd, addPendingIntent);

        // Обработка кнопки "Сброс" в уведомлении
        Intent resetIntent = new Intent(this, CounterService.class);
        resetIntent.setAction("RESET");
        PendingIntent resetPendingIntent = PendingIntent.getService(this, 1, resetIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        notificationLayout.setOnClickPendingIntent(R.id.btnNotificationReset, resetPendingIntent);

        // Настройка уведомления
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_background)  // Добавьте иконку
                .setCustomContentView(notificationLayout)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .build();

        startForeground(NOTIFICATION_ID, notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && intent.getAction() != null) {
            if (intent.getAction().equals("ADD")) {
                counter++;
            } else if (intent.getAction().equals("RESET")) {
                counter = 0;
            }
            updateNotification();
        }
        return START_NOT_STICKY;
    }

    private void updateNotification() {
        RemoteViews notificationLayout = new RemoteViews(getPackageName(), R.layout.notification_layout);
        notificationLayout.setTextViewText(R.id.tvNotificationCounter, "Счёт: " + counter);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_background)  // Добавьте иконку
                .setCustomContentView(notificationLayout)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, notification);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(serviceChannel);
            }
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}