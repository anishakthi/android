package com.example.user.travel.db.config;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by User on 6/8/2017.
 */

public class TravelDbHelper extends SQLiteOpenHelper {

    public TravelDbHelper(Context context) {
        super(context, TravelDestination.DB_NAME, null, TravelDestination.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TravelDestination.TravelEntry.TABLE + " ( " +
                TravelDestination.TravelEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TravelDestination.TravelEntry.COL_TRAVEL_TITLE + " TEXT NOT NULL);";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TravelDestination.TravelEntry.TABLE);
        onCreate(db);
    }
}
