package com.example.parcing.db;

import android.content.Context;

public class DbManager {
    private Context context;
    private DbHelper dbHelper;

    public DbManager(Context context) {
        this.context = context;
        dbHelper = new DbHelper(context);
    }
}
