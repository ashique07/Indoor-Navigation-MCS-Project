 package com.example.indoornavigation;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class IndoorNavigationDatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "IndoorNavigation";

    private static final int DB_VERSION = 1;

    IndoorNavigationDatabaseHelper(Context context)
    {
        //Create Database
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //Create Table
        db.execSQL("CREATE TABLE MAGNETIC_FIELD("
        +"_id INTEGER PRIMARY KEY AUTOINCREMENT, "
        +"ROOM_NUMBER TEXT, "
        +"X_VALUE REAL, "
        +"Y_VALUE REAL, "
        +"Z_VALUE REAL);" );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
