package com.lifecyclehealth.lifecyclehealth.activities;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.lifecyclehealth.lifecyclehealth.R;
import com.lifecyclehealth.lifecyclehealth.application.MyApplication;
import com.lifecyclehealth.lifecyclehealth.callbacks.OnOkClick;
import com.lifecyclehealth.lifecyclehealth.services.BackgroundTrackingService;
import com.lifecyclehealth.lifecyclehealth.utils.AppConstants;
import com.moxtra.sdk.common.ContextWrapper;

import zemin.notification.NotificationBuilder;
import zemin.notification.NotificationDelegater;
import zemin.notification.NotificationLocal;
import zemin.notification.NotificationView;


/**
 * Created by satyam on 19/12/2016.
 */

public abstract class BaseActivity extends AppCompatActivity {
    private ProgressDialog progressDialog;
    private static BaseActivity mInstance;
    public NotificationDelegater mDelegater;
    public NotificationLocal mLocal;
    private static boolean changeView = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_activity);
        mInstance = this;
        if (MyApplication.getInstance().getFromSharedPreference(AppConstants.Moxtra_ORG_ID) != null) {
            //start();
            //timer();
        }
        setProgressDialog();
        MyApplication.getInstance().addBooleanToSharedPreference(AppConstants.IS_TIMEOUT, false);
        stopService(new Intent(this, BackgroundTrackingService.class));
        mDelegater = NotificationDelegater.getInstance();
        mLocal = mDelegater.local();
        mLocal.setView((NotificationView) findViewById(R.id.nv));
    }

    public static synchronized BaseActivity getInstance() {
        return mInstance;
    }


    public void showLocalNotification(String text) {
        try {
            NotificationBuilder.V1 builder = NotificationBuilder.local()
                    .setBackgroundColor(getResources().getColor(R.color.colorPrimary))
                    .setLayoutId(getNextLayout())
                    .setTitle(text)
                    .setDelay(10)
                    .setIsRefresh(changeView)
                    .setNohistory(true);
            changeView = false;
            mDelegater.send(builder.getNotification());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int mLayoutIdx;
    // if layout is set to 0, the default will be used.
    private static final int[] mLayoutSet = new int[]{
            zemin.notification.R.layout.notification_simple_2,
    };

    private int getNextLayout() {
        if (mLayoutIdx == mLayoutSet.length) {
            mLayoutIdx = 0;
        }
        return mLayoutSet[mLayoutIdx++];
    }


    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ContextWrapper.wrap(newBase, this));
    }


    @Override
    public void onUserInteraction() {
        // TODO Auto-generated method stub
        super.onUserInteraction();

        //printLog("touch onUserInteraction");
        /*myHandler = new Handler();
        stop();
        restart();*/


    }

  /*  CountDownTimer waitTimer;

    private void timer() {

        waitTimer = new CountDownTimer(120000, 300) {
            //120000
            public void onTick(long millisUntilFinished) {
                //called every 300 milliseconds, which could be used to
                //send messages or some other action
            }

            public void onFinish() {
                //After 60000 milliseconds (60 sec) finish current
                //if you would like to execute something when time finishes
                Intent i = new Intent(BaseActivity.this, LoginActivity.class);
                startActivity(i);
            }
        };
        waitTimer.start();
    }*/

  /*  Runnable myRunnable = new Runnable() {
        @Override
        public void run() {
            // your code here
            Intent i = new Intent(BaseActivity.this, LoginActivity.class);
            startActivity(i);
        }
    };

    public Handler myHandler ;
    private static final int TIME_TO_WAIT = 2 * 60 * 1000;

    public void start() {
        myHandler = new Handler();
        myHandler.postDelayed(myRunnable, TIME_TO_WAIT);
    }

    public void stop() {
        myHandler.removeCallbacks(myRunnable);
    }

    public void restart() {
        myHandler.removeCallbacks(myRunnable);
        myHandler.postDelayed(myRunnable, TIME_TO_WAIT);
    }*/

    @Override
    protected void onResume() {
        super.onResume();
        changeView = true;
        mDelegater = NotificationDelegater.getInstance();
        mLocal = mDelegater.local();
        mLocal.setView((NotificationView) findViewById(R.id.nv));
        MyApplication.getInstance().addBooleanToSharedPreference(AppConstants.IS_TIMEOUT, false);
        stopService(new Intent(this, BackgroundTrackingService.class));
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //active = false;
        MyApplication.getInstance().clearPreferences();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
 /* *********************************************** Validation************************************** */

    public boolean isValid(String value) {
        return (value != null) && !value.isEmpty();
    }

    public boolean isValid(View view) {
        return (view != null);
    }

    /**************************
     * NETWORK AVAILABILITY
     *****************************/

    public boolean isConnectedToNetwork(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        // boolean isWiFi = activeNetwork.getType() == ConnectivityManager.TYPE_WIFI;
    }


    /* for printing Log*/
    public void printLog(String message) {
        Log.e(getTag(), message);
    }

    abstract String getTag();

    public void showErrorDialog(Context context, String message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setTitle(context.getString(R.string.error_title));
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setPositiveButton(context.getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int arg1) {
                dialog.dismiss();
            }
        });
        alertDialogBuilder.show();
    }

    private void setProgressDialog() {
        progressDialog = ProgressDialog.show(this, null, null);
        progressDialog.setContentView(R.layout.layout_progress_bar);
        if (progressDialog.getWindow() != null)
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.setIndeterminateDrawable(ContextCompat.getDrawable(this, R.drawable.progress_indeterminate));
        progressDialog.setProgressDrawable(ContextCompat.getDrawable(this, R.drawable.progress_indeterminate));
        progressDialog.setCancelable(false);
        if (progressDialog.isShowing()) progressDialog.dismiss();
    }

    public ProgressDialog getProgressDialog() {
        return progressDialog;
    }

    public void showProgressDialog(boolean flag) {
        if (flag) {
            if (!progressDialog.isShowing()) {
                progressDialog.show();
            }
        } else {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
    }

    public void showDialogWithOkButton(String message, final OnOkClick onOkClick) {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        LayoutInflater layoutInflater = this.getLayoutInflater();
        View dialogView = layoutInflater.inflate(R.layout.layout_dialog_ok, null);
        builder.setView(dialogView);
        final android.support.v7.app.AlertDialog alertDialog = builder.create();
        TextView TextViewMessage = (TextView) dialogView.findViewById(R.id.messageTv);
        TextViewMessage.setText(message);
        Button buttonOk = (Button) dialogView.findViewById(R.id.ok_btn);
        buttonOk.setText(getString(R.string.ok));
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                onOkClick.OnOkClicked();
            }
        });
        Window window = alertDialog.getWindow();
        WindowManager.LayoutParams wmlp = window.getAttributes();
        wmlp.windowAnimations = R.style.dialog_animation;
        window.setAttributes(wmlp);
        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog.show();
    }

    public void showDialogWithOkButton(String message) {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        LayoutInflater layoutInflater = this.getLayoutInflater();
        View dialogView = layoutInflater.inflate(R.layout.layout_dialog_ok, null);
        builder.setView(dialogView);
        final android.support.v7.app.AlertDialog alertDialog = builder.create();
        TextView TextViewMessage = (TextView) dialogView.findViewById(R.id.messageTv);
        TextViewMessage.setText(message);
        Button buttonOk = (Button) dialogView.findViewById(R.id.ok_btn);
        buttonOk.setText(getString(R.string.ok));
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        Window window = alertDialog.getWindow();
        if (window != null) {
            WindowManager.LayoutParams wmlp = window.getAttributes();
            wmlp.windowAnimations = R.style.dialog_animation;
            window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            window.setAttributes(wmlp);
        }
        alertDialog.show();
    }


    public void showDialogWithOkCancelButton(String message, final OnOkClick onOkClick) {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        LayoutInflater layoutInflater = this.getLayoutInflater();
        View dialogView = layoutInflater.inflate(R.layout.layout_dialog_ok_cancel, null);
        builder.setView(dialogView);
        final android.support.v7.app.AlertDialog alertDialog = builder.create();
        TextView TextViewMessage = (TextView) dialogView.findViewById(R.id.messageTv);
        TextViewMessage.setText(message);
        Button yes_btn = (Button) dialogView.findViewById(R.id.yes_btn);
        Button cancel_btn = (Button) dialogView.findViewById(R.id.cancel_btn);
        yes_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onOkClick.OnOkClicked();
                alertDialog.dismiss();
            }
        });
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        Window window = alertDialog.getWindow();
        if (window != null) {
            WindowManager.LayoutParams wmlp = window.getAttributes();
            wmlp.windowAnimations = R.style.dialog_animation;
            window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            window.setAttributes(wmlp);
        }
        alertDialog.show();
    }

    public void showNoNetworkMessage() {
        showDialogWithOkButton(getString(R.string.error_no_network));
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
