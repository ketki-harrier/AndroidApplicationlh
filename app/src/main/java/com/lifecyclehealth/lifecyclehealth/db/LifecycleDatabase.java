package com.lifecyclehealth.lifecyclehealth.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by vaibhavi on 12-01-2018.
 */

public class LifecycleDatabase {


    public static final String TABLE_PROFILE = "User";
    // UserToken Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_TOKEN = "LoginToken";

    private Context context;
    private DatabaseHandler helper;
    private SQLiteDatabase database;

    public LifecycleDatabase(Context context) {
        this.context = context;
        helper = new DatabaseHandler(context);
        database = helper.getWritableDatabase();
    }

    public void addData(String token) {
        ContentValues values = new ContentValues();
        values.put(KEY_TOKEN, token);
        database.insert(TABLE_PROFILE, null, values);// Inserting Row
    }

    // Updating single
    public int updateData(String token) {
        ContentValues values = new ContentValues();
        values.put(KEY_TOKEN, token);
        return database.update(TABLE_PROFILE, values, KEY_ID + " = ?", new String[]{String.valueOf(token)});
    }

    // Retrieve single
    public String retrieveToken() {
        Cursor cursorEmployees = database.rawQuery("SELECT LoginToken FROM User", null);
        String token = null;
        //if the cursor has some data
        if (cursorEmployees.moveToFirst()) {
            do {
                token = cursorEmployees.getString(0);

            } while (cursorEmployees.moveToNext());
        }
        //closing the cursor
        Log.e("Token", token);
        cursorEmployees.close();
        return token;
    }


    // Deleting single
    public void deleteData() {
        database.delete(TABLE_PROFILE, null, null);
    }

}
