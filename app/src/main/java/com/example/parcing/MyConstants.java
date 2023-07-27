package com.example.parcing;

public class MyConstants {
    public static final String TABLE_NAME = "fr_to_dat";
    public static final String _ID = "_id";
    public static final String FROM = "cFrom";
    public static final String TO = "cTo";
    public static final String DATE = "cDate";
    public static final String DB_NAME = "fr_to_dat_db.db";
    public static final int DB_VERSION = 1;
    public static final String TABLE_STRUCTURE = "CREATE TABLE IF NOT EXISTS " +
            TABLE_NAME + " (" + _ID + "INTEGER PRIMARY KEY, " + FROM + " TEXT," +
            TO + " TEXT," + DATE + " TEXT)";
    public static final String DROP_TABLE = "DROP TABLE IF NOT EXISTS " + TABLE_STRUCTURE;
}
