package com.example.parcing;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class ChoseTimeAndStartActivity extends AppCompatActivity {

    String[] timeArray;
    ArrayList<Integer> timeList = new ArrayList<>();
    ArrayList<String> timeArrayList;
    int timeArrayLength = 0;
    private String from, to, date;
    private boolean[] selectedTime;

    Button findButton;
    private Handler handler;

    private Document doc;
    private Thread thread2;
    private Runnable runnable1;
    private final String urlFstP = "https://atlasbus.by/Маршруты/",
            urlSecP = "?date=2023-";
    private Elements sits, info;

    private Intent serviceIntent;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getIntent().getExtras();
        timeArrayList = (ArrayList<String>) arguments.get("timeArray");
        timeArray = new String[timeArrayList.size()];
        timeArray = timeArrayList.toArray(timeArray);
        timeArrayLength = (int) arguments.get("length");
        TimeListItem timeListItem;
        timeListItem = (TimeListItem) arguments.getSerializable(TimeListItem.class.getSimpleName());
        from = timeListItem.getFrom();
        to = timeListItem.getTo();
        date = timeListItem.getDate();

        setContentView(R.layout.activity_chose_time_and_start_find);

        EditText editText = findViewById(R.id.editTime);
        findButton = this.<Button>findViewById(R.id.find_button);
        selectedTime = new boolean[timeArrayLength];
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ChoseTimeAndStartActivity.this);
                builder.setCancelable(false);
                builder.setTitle("Выберите время отправления");
                builder.setMultiChoiceItems(timeArray, selectedTime , new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                        if (b) {
                            timeList.add(i);
                            Collections.sort(timeList);
                        } else {
                            timeList.remove(Integer.valueOf(i));
                        }
                    }
                });
                builder.setPositiveButton("Ок", new DialogInterface.OnClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        StringBuilder ans = new StringBuilder();
                        for (int j = 0; j < timeList.size(); j++) {
                            ans.append(timeArray[timeList.get(j)]).append(" ");
                        }
                        editText.setText(ans.toString());
                    }
                });
                builder.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.show();
            }
        });

        findButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNotificationChannel();
                serviceIntent = new Intent(getApplicationContext(), YourService.class);
                startService(serviceIntent);
                init();
            }
        });
    }
    public void init() {
        handler = new Handler();
        runnable1 = () -> getWeb();
        thread2 = new Thread(runnable1);
        thread2.start();
    }
    private void getWeb() {
        try {
            String url = urlFstP + from + "/" + to + urlSecP + date;
            doc = Jsoup.connect(url).get();
            //doc = Jsoup.connect(urlFstP + "Ивье/Минск" + urlSecP + "07-26").get();
            info = doc.select("div.MuiGrid-grid-md-auto.MuiGrid-item.MuiGrid-root:nth-of-type(3)");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < timeList.size(); i++) {
                        sits = info.get(timeList.get(i)).getAllElements();
                        if (sits.size() > 1) {
                           sendNot(timeArray[timeList.get(i)]);
                           stopService(serviceIntent);
                           return;
                        }
                    }
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            handler.postDelayed(runnable1, 60_000);
        }
    }

    private void sendNot(String foundedTime) {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, PendingIntent.FLAG_IMMUTABLE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "not_channel")
                .setSmallIcon(R.drawable.search__1_)
                .setContentTitle(getString(R.string.app_name))
                .setContentText("Найдено место на время " + foundedTime)
                .setContentIntent(pendingIntent);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
        createNotificationChannel();
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        notificationManagerCompat.notify(0, builder.build());
    }

    private void createNotificationChannel() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            //NotificationChannel notificationChannel = notificationManager.getNotificationChannel("not1");
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel("not_channel", "data", importance);
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }

    }

 
}
