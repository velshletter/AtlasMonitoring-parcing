package com.example.parcing;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.MainThread;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

public class YourService extends Service {

    private Document doc;
    private Thread secThread;
    private Runnable runnable1;
    public TimeListItem timeListItem;
    private Handler handler;
    SharedPreferences sharedPreferences;
    private final String urlFstP = "https://atlasbus.by/Маршруты/",
            urlSecP = "?date=2023-";
    private Elements stTime, sits, time, info;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startForeground();
        return super.onStartCommand(intent, flags, startId);
    }

    private void startForeground() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Intent notificationIntent = new Intent(this, MainActivity.class);
            getApplicationContext().startForegroundService(notificationIntent);

            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                    notificationIntent, PendingIntent.FLAG_IMMUTABLE);

            //AutoStartPermissionHelper.Companion.getInstance().getAutoStartPermission(getApplicationContext(),true, false);

            Notification notification = new Notification.Builder(this,
                    "not_channel")
                    .setOngoing(true)
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setContentTitle(getString(R.string.app_name))
                    .setContentText("Service is running background")
                    .setContentIntent(pendingIntent)
                    .build();
            startForeground(1, notification);
        }

    }


}
