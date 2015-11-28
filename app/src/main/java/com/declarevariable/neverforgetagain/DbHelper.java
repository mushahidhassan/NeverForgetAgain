package com.declarevariable.neverforgetagain;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by mushahid on 11/26/2015.
 */
public class DbHelper extends SQLiteOpenHelper {

    //Constants for db name and version
    private static final String DATABASE_NAME = "tasks.db";
    private static final int DATABASE_VERSION = 1;

    //Constants for identifying table and columns
    public static final String TABLE_TASKS = "tasks";
    public static final String TASK_ID = "_id";
    public static final String TASK_TEXT = "text";
    public static final String TASK_CREATED = "created";

    public static final String[] ALL_COLUMNS = {TASK_ID, TASK_TEXT, TASK_CREATED};

    //SQL to create table
    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_TASKS + " (" +
                    TASK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    TASK_TEXT + " TEXT, " +
                    TASK_CREATED + " TEXT default CURRENT_TIMESTAMP" +
                    ")";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
            db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS );
        onCreate(db);
    }
}
