package com.lifecyclehealth.lifecyclehealth.activities;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.lifecyclehealth.lifecyclehealth.R;
import com.lifecyclehealth.lifecyclehealth.services.Time_out_services;

import zemin.notification.NotificationDelegater;
import zemin.notification.NotificationLocal;
import zemin.notification.NotificationView;

import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.TIME_TO_WAIT;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.TOUCH_TIME;

public class InCompleteProfileActivity extends BaseActivity {

    private NotificationDelegater mDelegater;
    private NotificationLocal mLocal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_complete_profile);
        start();
        initView();
    }

    private void initView() {

        Button btnOk = (Button) findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mDelegater = NotificationDelegater.getInstance();
        mLocal = mDelegater.local();
        mLocal.setView((NotificationView) findViewById(R.id.nv));

    }

    @Override
    String getTag() {
        return "InCompleteProfileActivity";
    }

    @Override
    protected void onResume() {
        super.onResume();
        startService(new Intent(this, Time_out_services.class));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        printLog("touch onUserInteraction");
        TOUCH_TIME = System.currentTimeMillis();
        stop();
        restart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stop();
    }

    Runnable myRunnable = new Runnable() {
        @Override
        public void run() {
            // your code here
            Intent i = new Intent(InCompleteProfileActivity.this, LoginActivity.class);
            startActivity(i);
        }
    };

    private Handler myHandler;

    private void start() {
        myHandler = new Handler();
        myHandler.postDelayed(myRunnable, TIME_TO_WAIT);
    }

    private void stop() {
        myHandler.removeCallbacks(myRunnable);
    }

    private void restart() {
        myHandler.removeCallbacks(myRunnable);
        myHandler.postDelayed(myRunnable, TIME_TO_WAIT);
    }


}
