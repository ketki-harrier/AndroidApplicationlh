package com.lifecyclehealth.lifecyclehealth.application;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.multidex.MultiDex;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.lifecyclehealth.lifecyclehealth.dto.AuthenticateUserDTO;
import com.lifecyclehealth.lifecyclehealth.utils.AESHelper;
import com.lifecyclehealth.lifecyclehealth.utils.AppConstants;
import com.lifecyclehealth.lifecyclehealth.utils.LruBitmapCache;
import com.moxtra.sdk.ChatClient;
import com.segment.analytics.Analytics;

import java.util.ArrayList;
import java.util.Set;

import zemin.notification.NotificationDelegater;

import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.CONST_PATIENT;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.EMAIL_ID;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.IS_LOGOUT;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.IS_TIMEOUT;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.PREF_CURRENT_USER;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.PREF_IS_PATIENT;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.TOUCH_EMAIL_ID;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.seedValue;

/**
 * Created by satyam on 01/07/2016.
 */
public class MyApplication extends Application {
    public static final String TAG = MyApplication.class.getSimpleName();
    private RequestQueue mRequestQueue;
    private SharedPreferences sharedPreferences;
    //private HashMap<String, String> serviceNameHashMap;
    private static MyApplication mInstance;
    private boolean isRealiseModeOn = false;
    private Typeface helveticaNeue;
    private ImageLoader mImageLoader;
    public ArrayList<Integer> arrayListChecked;

    public ArrayList<Integer> getArrayListChecked() {
        return arrayListChecked;
    }

    public void setArrayListChecked(ArrayList<Integer> arrayListChecked) {
        this.arrayListChecked = arrayListChecked;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        initNotification();
        try {

            ChatClient.initialize(this);

            /******for test*********/
            //  MXAccountManager.createInstance(this, "iAQQ9UaAdrQ", "v7cn6OgKXVs", true);

            /******for production*********/
            //MXAccountManager.createInstance(this, "qfiugFWfGqg", "ous0w9vF1U0", true);

            Analytics analytics = new Analytics.Builder(this, "mMJN5zbvWjhUo4KiXFkT91HdlMkWzm4p").build();
            Analytics.setSingletonInstance(analytics);
            setSharedPreference();

        } catch (Exception invalidParameter) {
            Log.e(TAG, "Error when creating MXAccountManager instance.", invalidParameter);
        }

    }

    private void initNotification() {
        NotificationDelegater.initialize(
                this,
                NotificationDelegater.LOCAL |
                        NotificationDelegater.GLOBAL |
                        NotificationDelegater.REMOTE);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }


    private void setSharedPreference() {
        String fromSharedPreference = MyApplication.getInstance().getFromSharedPreference(AppConstants.EMAIL_ID);

        MyApplication.getInstance().addBooleanToSharedPreference(AppConstants.IS_USERNAME_EDITABLE, false);
        MyApplication.getInstance().addBooleanToSharedPreference(AppConstants.IS_LOGINACTIVITY_ALIVE, false);
        MyApplication.getInstance().addBooleanToSharedPreference(AppConstants.IS_MAINACTIVITY_ALIVE, false);
        MyApplication.getInstance().addBooleanToSharedPreference(AppConstants.IS_IN_MEET_FRAGMENT, false);
        if (fromSharedPreference == null) {
            MyApplication.getInstance().addBooleanToSharedPreference(AppConstants.LOCAL_DB_TOUCH, false);
            MyApplication.getInstance().addBooleanToSharedPreference(AppConstants.SERVER_DB_TOUCH, false);
            try {
                MyApplication.getInstance().addToSharedPreference(TOUCH_EMAIL_ID, AESHelper.encrypt(seedValue, ""));
                MyApplication.getInstance().addToSharedPreference(EMAIL_ID, AESHelper.encrypt(seedValue, ""));
                MyApplication.getInstance().addBooleanToSharedPreference(IS_LOGOUT, false);
                MyApplication.getInstance().addBooleanToSharedPreference(IS_TIMEOUT, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public static synchronized MyApplication getInstance() {
        return mInstance;
    }


    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(this.mRequestQueue,
                    new LruBitmapCache());
        }
        return this.mImageLoader;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }


    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }


    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }


    public void addIntToSharedPreference(String key, int value) {
        sharedPreferences = getSharedPreferences(AppConstants.SHARED_PREFERENCE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value).commit();
    }

    public int getIntFromSharedPreference(String key) {
        sharedPreferences = getSharedPreferences(AppConstants.SHARED_PREFERENCE_NAME, MODE_PRIVATE);
        return sharedPreferences.getInt(key, -1);
    }

    public void addLongToSharedPreference(String key, Long value) {
        sharedPreferences = getSharedPreferences(AppConstants.SHARED_PREFERENCE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(key, value).commit();
    }

    public Long getLongFromSharedPreference(String key) {
        sharedPreferences = getSharedPreferences(AppConstants.SHARED_PREFERENCE_NAME, MODE_PRIVATE);
        return sharedPreferences.getLong(key, -1);
    }

    public void addToSharedPreference(String key, String value) {
        sharedPreferences = getSharedPreferences(AppConstants.SHARED_PREFERENCE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value).commit();
    }

    public String getFromSharedPreference(String key) {
        sharedPreferences = getSharedPreferences(AppConstants.SHARED_PREFERENCE_NAME, MODE_PRIVATE);
        return sharedPreferences.getString(key, null);
    }

    public void addBooleanToSharedPreference(String key, boolean value) {
        sharedPreferences = getSharedPreferences(AppConstants.SHARED_PREFERENCE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value).commit();
    }

    public boolean getBooleanFromSharedPreference(String key) {
        sharedPreferences = getSharedPreferences(AppConstants.SHARED_PREFERENCE_NAME, MODE_PRIVATE);
        sharedPreferences.getBoolean(key, false);
        return sharedPreferences.getBoolean(key, false);
    }

    public void addListToSharedPreference(String key, Set<String> value) {
        sharedPreferences = getSharedPreferences(AppConstants.SHARED_PREFERENCE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet(key, value).commit();
    }


   /* public void setCheckedToSharedPreference(String key, ArrayList<Integer> value) {
        sharedPreferences = getSharedPreferences(AppConstants.SHARED_PREFERENCE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key,  value).commit();
    }

    public int getCheckedFromSharedPreference(String key) {
        sharedPreferences = getSharedPreferences(AppConstants.SHARED_PREFERENCE_NAME, MODE_PRIVATE);
        sharedPreferences.getInt(key,0);
        return sharedPreferences.getInt(key ,0);
    }*/


    public Set<String> getListFromSharedPreference(String key) {
        sharedPreferences = getSharedPreferences(AppConstants.SHARED_PREFERENCE_NAME, MODE_PRIVATE);
        return sharedPreferences.getStringSet(key, null);
    }

    public boolean isConnectedToNetwork(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        // boolean isWiFi = activeNetwork.getType() == ConnectivityManager.TYPE_WIFI;
    }

    public void clearPreferences() {
        /*sharedPreferences = getSharedPreferences(AppConstants.SHARED_PREFERENCE_NAME, MODE_PRIVATE);
        sharedPreferences.edit().clear().commit();*/
    }

    public void updateCurrentUser(AuthenticateUserDTO userDTO) {
        if (userDTO.getUser().getRole().get(0).equalsIgnoreCase(CONST_PATIENT)) {
            addBooleanToSharedPreference(PREF_IS_PATIENT, true);
        } else addBooleanToSharedPreference(PREF_IS_PATIENT, false);
        addToSharedPreference(PREF_CURRENT_USER, new Gson().toJson(userDTO));
    }

    public AuthenticateUserDTO getCurrentUser() {
        return new Gson().fromJson(getFromSharedPreference(PREF_CURRENT_USER), AuthenticateUserDTO.class);
    }

    /*
        // for checking internet conection when activity created for firsttime
         without broadcast recievr
     */
    public boolean isNetworkAvailable() {
        // At activity startup we manually check the internet status and change
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }


}
