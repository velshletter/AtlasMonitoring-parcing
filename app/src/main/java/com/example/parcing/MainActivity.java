package com.example.parcing;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    public EditText editTime, editFrom, editTo, editDate;
    private Button button;
    private final TimeListItem timeListItem = new TimeListItem();
    public TimeDialogFragment timeDialogFragment;
    private Handler handler;

    private Document doc;
    private Thread thread2, thread3;
    private Runnable runnable1, runnable2;
    private final String urlFstP = "https://atlasbus.by/Маршруты/",
            urlSecP = "?date=2023-";
    private Elements stTime, sits, time, info;
    private String from, to, date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
        textView = findViewById(R.id.textView1);
        editFrom = findViewById(R.id.editFrom);
        editTo = findViewById(R.id.editTo);
        editDate = findViewById(R.id.editTextDate);
        button = findViewById(R.id.find_button);


        editTime = findViewById(R.id.editTime);
        editTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeDialogFragment = new TimeDialogFragment();
                timeDialogFragment.setListItem(timeListItem);
                timeDialogFragment.show(getSupportFragmentManager(), "fr");
                timeDialogFragment.setListItem(editTime);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                from = String.valueOf(editFrom.getText());
                to = String.valueOf(editTo.getText());
                date = String.valueOf(editDate.getText());
                createNotificationChannel();
                startService(new Intent(getApplicationContext(), YourService.class));
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
            String url = urlFstP + from + "/" + to + urlSecP + date; doc = Jsoup.connect(url).get();
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
                        textView.setText(sits.get(sits.size() - 1).text());
                    } else textView.setText("");
                }
            });


        } catch (IOException e) {
            throw new RuntimeException(e);
        }   finally {
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

    private void createNotificationChannel(){
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

/*

     Log.d("MyLog", " "+ans);

 */