package com.example.parcing;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
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
import java.util.Collection;
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
    private Thread thread2, thread3;
    private Runnable runnable1, runnable2;
    private final String urlFstP = "https://atlasbus.by/Маршруты/",
            urlSecP = "?date=2023-";
    private Elements stTime, sits, time, info;


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
        //parcTimeDepForList();

        setContentView(R.layout.activity_chose_time_and_start_find);

        EditText editText = findViewById(R.id.editTime);
        findButton = this.<Button>findViewById(R.id.find_button);
        selectedTime = new boolean[timeArrayLength];
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ChoseTimeAndStartActivity.this);
                //builder.setCancelable(false);
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
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String ans = "";
                        for (int j = 0; j < timeList.size(); j++) {
                            ans= ans + timeArray[timeList.get(j)] + " ";
                        }
                        editText.setText(ans);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
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
                //createNotificationChannel();
                //startService(new Intent(getApplicationContext(), YourService.class));
                //init();
            }
        });
    }


/*
    public void init() {
        handler = new Handler();
        runnable1 = () -> getWeb();
        thread2 = new Thread(runnable1);
        thread2.start();
    }

    private void getWeb() {
        try {
            String url = urlFstP + from + "/" + to + urlSecP + date;
            Document doc = Jsoup.connect(url).get();
            //doc = Jsoup.connect(urlFstP + "Ивье/Минск" + urlSecP + "07-26").get();

            info = doc.select("div.MuiGrid-grid-md-auto.MuiGrid-item.MuiGrid-root:nth-of-type(3)");
            time = doc.select("div.MuiGrid-grid-md-3.MuiGrid-item.MuiGrid-root:nth-of-type(1)");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0, j = 1; i < info.size(); i++, j += 2) {
                        stTime = time.get(j).getAllElements();
                        if (timeListItem.getChosedTime().substring(0, 5).equals(stTime.get(3).text())) {
                            sits = info.get(i).getAllElements();
                            break;
                        }
                    }
                    if (sits.size() > 1) {
                        sendNot();
                        Log.d("MyLog", "123123123");
                        //textView.setText(sits.get(sits.size() - 1).text());
                    } //textView.setText("");
                }
            });


        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            handler.postDelayed(runnable1, 5000);
        }
    }

    private void sendNot() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "not_channel")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Найдено место на время " + timeListItem.getChosedTime());

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

 */
}
