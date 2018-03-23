package com.lifecyclehealth.lifecyclehealth.services;

import android.app.ActivityManager;
import android.app.IntentService;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.lifecyclehealth.lifecyclehealth.activities.LoginActivity;
import com.lifecyclehealth.lifecyclehealth.activities.MainActivity;
import com.lifecyclehealth.lifecyclehealth.application.MyApplication;
import com.lifecyclehealth.lifecyclehealth.utils.AppConstants;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.TIME_TO_WAIT;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.TOUCH_TIME;

/**
 * Created by vaibhavi on 07-02-2018.
 */

public class BackgroundTrackingService extends Service {
    public static final int notify = 3000;
    int totaltime;

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {

        Log.e("call", "background service");
        long time = (System.currentTimeMillis() - TOUCH_TIME);
        int l = (int) ((time / 1000) / 60);
        Log.e("time touched", l + "");

        if (l != 0) {
            totaltime = TIME_TO_WAIT - (l * 60 * 1000);
        } else {
            totaltime = TIME_TO_WAIT;
        }
        Log.e("total time", totaltime + "");
        start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MyApplication.getInstance().addBooleanToSharedPreference(AppConstants.IS_TIMEOUT, false);
        stop();
    }

    private Handler myHandler;

    private void start() {
        if (myHandler != null)
            myHandler.removeCallbacks(myRunnable);
        MyApplication.getInstance().addBooleanToSharedPreference(AppConstants.IS_TIMEOUT, true);
        myHandler = new Handler();
        myHandler.postDelayed(myRunnable, totaltime);
    }

    private void stop() {
        if (myHandler != null)
            myHandler.removeCallbacks(myRunnable);
    }


    Runnable myRunnable = new Runnable() {
        @Override
        public void run() {
            MyApplication.getInstance().addBooleanToSharedPreference(AppConstants.IS_TIMEOUT, false);
            // Toast.makeText(BackgroundTrackingService.this, "Service is out", Toast.LENGTH_SHORT).show();
        }
    };


    private boolean isAppOnForeground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null) {
            return false;
        }
        final String packageName = context.getPackageName();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND && appProcess.processName.equals(packageName)) {
                return true;
            }
        }
        return false;
    }

}