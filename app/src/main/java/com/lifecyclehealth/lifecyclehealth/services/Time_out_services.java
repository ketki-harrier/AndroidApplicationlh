package com.lifecyclehealth.lifecyclehealth.services;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.app.Notification;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager.WakeLock;
import android.os.PowerManager;
import android.os.Process;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.lifecyclehealth.lifecyclehealth.activities.LoginActivity;
import com.lifecyclehealth.lifecyclehealth.activities.MainActivity;

import java.util.concurrent.TimeUnit;

import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.TIME_TO_WAIT;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.TOUCH_TIME;

/**
 * Created by vaibhavi on 22-11-2017.
 */

public class Time_out_services extends Service implements SensorEventListener {
    public static final String TAG = Time_out_services.class.getName();

    private SensorManager mSensorManager = null;
    private WakeLock mWakeLock = null;

    /*
     * Register this as a sensor event listener.
     */
    private void registerListener() {
        mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    /*
     * Un-register this as a sensor event listener.
     */
    private void unregisterListener() {
        mSensorManager.unregisterListener(this);
    }

    public BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {


            if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
                Log.i(TAG, " off onReceive(" + intent + ")");
                start();
                return;
            } else if (!intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
                Log.i(TAG, " on onReceive(" + intent + ")");
                if (myHandler != null) {
                    stop();
                    myHandler.removeCallbacks(null);
                    myHandler = null;
                }
                return;
            }

           /* Runnable runnable = new Runnable() {
                public void run() {
                    //Log.i(TAG, "Runnable executing.");
                    unregisterListener();
                    registerListener();
                }
            };

            new Handler().postDelayed(runnable, SCREEN_OFF_RECEIVER_DELAY);*/
        }
    };

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Log.i(TAG, "onAccuracyChanged().");
    }

    public void onSensorChanged(SensorEvent event) {
        //Log.i(TAG, "onSensorChanged().");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        PowerManager manager =
                (PowerManager) getSystemService(Context.POWER_SERVICE);
        mWakeLock = manager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, TAG);
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_SCREEN_ON);
        registerReceiver(mReceiver, filter);
    }

    @Override
    public void onDestroy() {
        try {
            unregisterReceiver(mReceiver);
            unregisterListener();
            mWakeLock.release();
            stopForeground(true);
        }catch (Exception e){e.printStackTrace();}

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        //startForeground(Process.myPid(), new Notification());
        registerListener();
        mWakeLock.acquire();
        return START_STICKY;
    }


    Runnable myRunnable = new Runnable() {
        @Override
        public void run() {
            // your code here
            Log.e("log", "out");
            try {
                Intent i = new Intent(Time_out_services.this, LoginActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                stopSelf();
                //stopService(i);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private Handler myHandler;

    private void start() {
        long time = (System.currentTimeMillis() - TOUCH_TIME);
        int l = (int) ((time / 1000) / 60);
        Log.e("time touched", l + "");
        myHandler = new Handler();

        int totaltime;
        if (l != 0) {
            totaltime = TIME_TO_WAIT - (l * 60 * 1000);
        } else {
            totaltime = TIME_TO_WAIT;
        }
        Log.e("total time", totaltime + "");

        myHandler.postDelayed(myRunnable, totaltime);
    }

    private void stop() {
        myHandler.removeCallbacks(myRunnable);
    }


}

