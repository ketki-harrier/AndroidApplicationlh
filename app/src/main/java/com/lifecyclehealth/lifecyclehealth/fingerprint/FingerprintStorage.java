package com.lifecyclehealth.lifecyclehealth.fingerprint;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by satyam on 25/11/2016.
 */

public class FingerprintStorage extends SQLiteOpenHelper implements FingerPrintActionsCallback {
    private static final String TAG = FingerprintStorage.class.getSimpleName();

    /*DB information*/
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "PaytonicFingerprint.db";
    private static final String TABLE_NAME = "P_Finegrprint_table";
    /* Filed which will be used to store info of fingerprint*/
    private static final String MOBILE_NUMBER = "mobile_";
    private static final String TOUCH_STATUS_ON_DEVICE = "touch_status_on_device";
    private static final String TOUCH_STATUS_ON_SERVER = "touch_status_on_server";
    private static final String TOUCH_SALT_VALUE = "touch_public_key";
    private static final String MOBILE_PIN = "mobile_pin";
    private static final String DATE_OF_ADDED = "date_of_adding";
    private static final String ID = "db_id";
    private static final String IS_USER_ACTIVE = "is_user_active";
    private static final String IS_DONT_ASK_ON = "is_dont_ask_on";
    private static final String FLAG_TRUE = "true";
    private static final String FLAG_FALSE = "false";
    /**************
     * QUERY FOR TABLE OPERATIONS STARTS
     *****************/
    /* create query */
    String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "" +
            " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + DATE_OF_ADDED + " TEXT ,"
            + MOBILE_NUMBER + " TEXT ,"
            + TOUCH_STATUS_ON_DEVICE + " TEXT ,"
            + TOUCH_STATUS_ON_SERVER + " TEXT ,"
            + TOUCH_SALT_VALUE + " TEXT ,"
            + IS_USER_ACTIVE + " TEXT ,"
            + IS_DONT_ASK_ON + " TEXT ,"
            + MOBILE_PIN + " TEXT)";
    /* drop table*/
    String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    /**************
     * QUERY FOR TABLE OPERATIONS ENDS
     *****************/


    public FingerprintStorage(Context context) {
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


    /********************************************
     * MODEL CLASS ENDS
     * **********************************/

    /*************************
     * Implementation of Interface Starts
     *******************************/
    @Override
    public void addUser(TouchModel model) {
        SQLiteDatabase database = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(MOBILE_NUMBER, model.getMobileNumber());
            values.put(MOBILE_PIN, model.getMobileNumber());
            values.put(DATE_OF_ADDED, model.getDateOfAdded());
            values.put(TOUCH_STATUS_ON_DEVICE, getStringValue(model.isTouchStatusOnDevice()));
            values.put(TOUCH_STATUS_ON_SERVER, getStringValue(model.isTouchStatusOnServer()));
            values.put(TOUCH_SALT_VALUE, model.getTouchSaltValue());
            values.put(IS_USER_ACTIVE, getStringValue(model.isUserActive()));
            values.put(IS_DONT_ASK_ON, getStringValue(model.isDontAskMeAgainOn()));
            database.insert(TABLE_NAME, null, values);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            database.close();
        }
    }

    @Override
    public ArrayList<TouchModel> getAllRegisterUser() {
        SQLiteDatabase database = this.getReadableDatabase();
        ArrayList<TouchModel> usersList = new ArrayList<>();
        String QUERY = "SELECT * FROM " + TABLE_NAME;
        Cursor cursor = database.rawQuery(QUERY, null);
        try {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    TouchModel touchModel = new TouchModel();
                    touchModel.set_Id(cursor.getString(cursor.getColumnIndex(ID)));
                    touchModel.setTouchSaltValue(cursor.getString(cursor.getColumnIndex(TOUCH_SALT_VALUE)));
                    touchModel.setMobileNumber(cursor.getString(cursor.getColumnIndex(MOBILE_NUMBER)));
                    touchModel.setMobilePin(cursor.getString(cursor.getColumnIndex(MOBILE_PIN)));
                    touchModel.setDateOfAdded(cursor.getString(cursor.getColumnIndex(DATE_OF_ADDED)));
                    touchModel.setUserActive(getBooleanValue(cursor.getString(cursor.getColumnIndex(IS_USER_ACTIVE))));
                    touchModel.setTouchStatusOnServer(getBooleanValue(cursor.getString(cursor.getColumnIndex(TOUCH_STATUS_ON_SERVER))));
                    touchModel.setTouchStatusOnDevice(getBooleanValue(cursor.getString(cursor.getColumnIndex(TOUCH_STATUS_ON_DEVICE))));
                    touchModel.setDontAskMeAgainOn(getBooleanValue(cursor.getString(cursor.getColumnIndex(IS_DONT_ASK_ON))));
                    usersList.add(touchModel);
                    //printLog("Id while adding" + touchModel.get_Id());
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
            database.close();
        }
        return usersList;
    }

    @Override
    public void updateTouchOnServer(TouchModel model, boolean isEnable) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MOBILE_NUMBER, model.getMobileNumber());
        values.put(MOBILE_PIN, model.getMobileNumber());
        values.put(DATE_OF_ADDED, model.getDateOfAdded());
        values.put(TOUCH_STATUS_ON_DEVICE, getStringValue(model.isTouchStatusOnDevice()));
        values.put(TOUCH_STATUS_ON_SERVER, getStringValue(isEnable));
        values.put(TOUCH_SALT_VALUE, model.getTouchSaltValue());
        values.put(IS_USER_ACTIVE, getStringValue(model.isUserActive()));
        values.put(IS_DONT_ASK_ON, getStringValue(model.isDontAskMeAgainOn()));
        try {
            int returnValue = db.update(TABLE_NAME, values, ID + " = ?", new String[]{model.get_Id()});
        } catch (Exception e) {
        } finally {
            db.close();
        }
    }

    @Override
    public void updateTouchOnDevice(TouchModel model, boolean isEnable) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MOBILE_NUMBER, model.getMobileNumber());
        values.put(MOBILE_PIN, model.getMobileNumber());
        values.put(DATE_OF_ADDED, model.getDateOfAdded());
        values.put(TOUCH_STATUS_ON_DEVICE, getStringValue(isEnable));
        values.put(TOUCH_STATUS_ON_SERVER, getStringValue(model.isTouchStatusOnServer()));
        values.put(TOUCH_SALT_VALUE, model.getTouchSaltValue());
        values.put(IS_USER_ACTIVE, getStringValue(model.isUserActive()));
        values.put(IS_DONT_ASK_ON, getStringValue(model.isDontAskMeAgainOn()));
        try {
            int returnValue = db.update(TABLE_NAME, values, ID + " = ?", new String[]{model.get_Id()});
        } catch (Exception e) {
        } finally {
            db.close();
        }
    }

    @Override
    public void updateUser(TouchModel model) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MOBILE_NUMBER, model.getMobileNumber());
        values.put(MOBILE_PIN, model.getMobileNumber());
        values.put(DATE_OF_ADDED, model.getDateOfAdded());
        values.put(TOUCH_STATUS_ON_DEVICE, getStringValue(model.isTouchStatusOnDevice()));
        values.put(TOUCH_STATUS_ON_SERVER, getStringValue(model.isTouchStatusOnServer()));
        values.put(TOUCH_SALT_VALUE, model.getTouchSaltValue());
        values.put(IS_USER_ACTIVE, getStringValue(model.isUserActive()));
        values.put(IS_DONT_ASK_ON, getStringValue(model.isDontAskMeAgainOn()));
        try {
            int returnValue = db.update(TABLE_NAME, values, ID + " = ?", new String[]{model.get_Id()});
        } catch (Exception e) {
        } finally {
            db.close();
        }
    }

    @Override
    public int getDataBaseCount() {
        int num = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            String QUERY = "SELECT * FROM " + TABLE_NAME;
            Cursor cursor = db.rawQuery(QUERY, null);
            num = cursor.getCount();
            db.close();
            return num;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    private String getStringValue(boolean flag) {
        if (flag) return FLAG_TRUE;
        else return FLAG_FALSE;
    }

    private boolean getBooleanValue(String flag) {
        if (flag.equalsIgnoreCase(FLAG_TRUE)) return true;
        else return false;
    }

}
