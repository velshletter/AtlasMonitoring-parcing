package com.example.parcing;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    public EditText editFrom;
    public EditText editTo;
    public EditText editDate;

    private TimeListItem timeListItem;

    private Document doc;
    private final String urlFstP = "https://atlasbus.by/Маршруты/",
            urlSecP = "?date=2023-";
    private Elements stTime;
    private Elements time;
    private Elements info;


    private final ArrayList<String> timeArray = new ArrayList<>();
    private String from, to, date;
    private int g=0;

    public MainActivity() {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        //parcTimeDepForList();
        editFrom = findViewById(R.id.editFrom);
        editTo = findViewById(R.id.editTo);
        editDate = findViewById(R.id.editTextDate);
        Button buttonNext = this.findViewById(R.id.next_button);
        Button buttonStop = this.findViewById(R.id.close_button);

        buttonNext.setOnClickListener(view -> {
            from = String.valueOf(editFrom.getText());
            to = String.valueOf(editTo.getText());
            date = String.valueOf(editDate.getText());
            timeListItem = new TimeListItem(from, to, date);
                parcTimeDepForList();
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if(g!=0){
                        Intent intent = new Intent(getApplicationContext(), ChoseTimeAndStartActivity.class);
                        intent.putExtra(TimeListItem.class.getSimpleName(), timeListItem);
                        intent.putExtra("timeArray", timeArray);
                        intent.putExtra("length", g);
                        startActivity(intent);
                        cancel();
                        finish();
                    }

                }
            }, 0 , 800);

            //createNotificationChannel();
            //startService(new Intent(getApplicationContext(), YourService.class));
            //init();
        });

        buttonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent serviceIntent = new Intent(getApplicationContext(), YourService.class);
                stopService(serviceIntent);
            }
        });

    }
    public void parcTimeDepForList(){
        Runnable runnable2 = () -> {
            try {
                String url = urlFstP + from + "/" + to + urlSecP + date;
                doc = Jsoup.connect(url).get();
                //doc = Jsoup.connect(urlFstP + "Ивье/Минск" + urlSecP + "07-28").get();
                time = doc.select("div.MuiGrid-grid-md-3.MuiGrid-item.MuiGrid-root:nth-of-type(1)");
                runOnUiThread(() -> {
                    info = doc.select("div.MuiGrid-grid-md-auto.MuiGrid-item.MuiGrid-root:nth-of-type(3)");
                    for (int i = 0, j = 1; i < info.size(); i++, j += 2) {
                        stTime = time.get(j).getAllElements();
                        timeArray.add(stTime.get(3).text());
                        g++;
                    }
                });
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        };
        Thread thread2 = new Thread(runnable2);
        thread2.start();
    }

    /*
    public void init() {
        //handler = new Handler();
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



 */


}

/*

     Log.d("MyLog", " "+ans);

 */