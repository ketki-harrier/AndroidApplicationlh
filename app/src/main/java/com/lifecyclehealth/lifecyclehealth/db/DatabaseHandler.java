package com.lifecyclehealth.lifecyclehealth.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * Created by vaibhavi on 12-01-2018.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "LifecycleHealthDatabase.db";
    public static final String TABLE_PROFILE = "User";

    private static DatabaseHandler mInstance;
    // UserToken Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_TOKEN = "LoginToken";



    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mInstance = this;
    }

    public static synchronized DatabaseHandler getInstance() {
        return mInstance;
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PROFILE_TABLE = "CREATE TABLE " + TABLE_PROFILE + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TOKEN + " TEXT" + ")";

        db.execSQL(CREATE_PROFILE_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROFILE);
        // Create tables again
        onCreate(db);
    }

}
