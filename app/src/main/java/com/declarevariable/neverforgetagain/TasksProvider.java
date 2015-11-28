package com.declarevariable.neverforgetagain;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by mushahid on 11/26/2015.
 */
public class TasksProvider extends ContentProvider {

    private static final String AUTHORITY = "com.declarevariable.neverforgetagain.tasksprovider";
    private static final String BASE_PATH = "tasks";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH );

    // Constant to identify the requested operation
    private static final int TASKS = 1;
    private static final int TASKS_ID = 2;

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(AUTHORITY ,BASE_PATH, TASKS);
        uriMatcher.addURI(AUTHORITY ,BASE_PATH + "/#", TASKS_ID);
    }

    private SQLiteDatabase database;

    @Override
    public boolean onCreate() {
        DbHelper helper = new DbHelper(getContext());
        database = helper.getWritableDatabase();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return database.query(DbHelper.TABLE_TASKS, DbHelper.ALL_COLUMNS, selection, null, null, null, DbHelper.TASK_CREATED + " DESC");
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Long id = database.insert(DbHelper.TABLE_TASKS, null, values);
        return Uri.parse(BASE_PATH + "/" + id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return database.delete(DbHelper.TABLE_TASKS, selection, selectionArgs);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return database.update(DbHelper.TABLE_TASKS, values, selection, selectionArgs);
    }
}
