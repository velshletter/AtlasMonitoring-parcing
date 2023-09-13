package com.example.parcing;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.judemanutd.autostarter.AutoStartPermissionHelper;

public class YourService extends Service {

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
                    .setSmallIcon(R.drawable.search__2_)
                    .setContentTitle(getString(R.string.app_name))
                    .setContentText("Идет поиск...")
                    .setContentIntent(pendingIntent)
                    .build();
            startForeground(1, notification);
        }

    }


}