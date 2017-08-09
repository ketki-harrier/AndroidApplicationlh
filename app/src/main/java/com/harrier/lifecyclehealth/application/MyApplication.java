package com.harrier.lifecyclehealth.application;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.harrier.lifecyclehealth.dto.AuthenticateUserDTO;
import com.harrier.lifecyclehealth.utils.AppConstants;

import java.util.Set;

import static com.harrier.lifecyclehealth.utils.AppConstants.CONST_PATIENT;
import static com.harrier.lifecyclehealth.utils.AppConstants.PREF_CURRENT_USER;
import static com.harrier.lifecyclehealth.utils.AppConstants.PREF_IS_PATIENT;

/**
 * Created by satyam on 01/07/2016.
 */
public class MyApplication extends MultiDexApplication {
    public static final String TAG = MyApplication.class.getSimpleName();
    private RequestQueue mRequestQueue;
    private SharedPreferences sharedPreferences;
    //private HashMap<String, String> serviceNameHashMap;
    private static MyApplication mInstance;
    private boolean isRealiseModeOn = false;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        //initialize ORM
        //SugarContext.init(this);


    }

    public static synchronized MyApplication getInstance() {
        return mInstance;
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
        return sharedPreferences.getBoolean(key, false);
    }

    public void addListToSharedPreference(String key, Set<String> value) {
        sharedPreferences = getSharedPreferences(AppConstants.SHARED_PREFERENCE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet(key, value).commit();
    }

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
        sharedPreferences = getSharedPreferences(AppConstants.SHARED_PREFERENCE_NAME, MODE_PRIVATE);
        sharedPreferences.edit().clear().commit();
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
