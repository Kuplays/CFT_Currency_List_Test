package com.cft.kuplays.currency;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "currency.db";
    private static final String CREATE_TABLE =
            "CREATE TABLE " + DatabaseContract.CurrencyEntry.TABLE_NAME
            + " ("
            + DatabaseContract.CurrencyEntry._ID + " INTEGER PRIMARY KEY,"
            + DatabaseContract.CurrencyEntry.JSON_STRING_FIELD + " TEXT)";

    private static final String DROP_TABLE = "DROP TABLE IF EXISTS " + DatabaseContract.CurrencyEntry.TABLE_NAME;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE);
        onCreate(db);
    }
}
