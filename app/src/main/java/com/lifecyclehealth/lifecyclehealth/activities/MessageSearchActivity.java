package com.lifecyclehealth.lifecyclehealth.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Toast;

import com.lifecyclehealth.lifecyclehealth.R;
import com.lifecyclehealth.lifecyclehealth.application.MyApplication;
import com.lifecyclehealth.lifecyclehealth.services.BackgroundTrackingService;
import com.lifecyclehealth.lifecyclehealth.services.Time_out_services;
import com.lifecyclehealth.lifecyclehealth.utils.AppConstants;
import com.moxtra.binder.ui.search.global.GlobalSearchFragment;
import com.moxtra.sdk.chat.model.Chat;
import com.moxtra.sdk.common.ActionListener;

import zemin.notification.NotificationDelegater;
import zemin.notification.NotificationLocal;
import zemin.notification.NotificationView;

import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.TIME_TO_WAIT;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.TOUCH_TIME;

public class MessageSearchActivity extends BaseActivity {

    private NotificationDelegater mDelegater;
    private NotificationLocal mLocal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_search);
        mDelegater = NotificationDelegater.getInstance();
        mLocal = mDelegater.local();
        mLocal.setView((NotificationView) findViewById(R.id.nv));
        MyApplication.getInstance().addBooleanToSharedPreference(AppConstants.IS_MAINACTIVITY_ALIVE, true);
        start();
        Fragment fragment = new GlobalSearchFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.search_frame, fragment).commit();

        ActionListener<Chat> actionListener = new ActionListener<Chat>() {
            @Override
            public void onAction(View view, Chat chat) {
                Toast.makeText(getApplicationContext(), chat.getTopic(), Toast.LENGTH_SHORT).show();
            }
        };


    }

    @Override
    String getTag() {
        return "MessageSearchActivity";
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
    protected void onResume() {
        super.onResume();
        startService(new Intent(this, Time_out_services.class));
    }

    @Override
    protected void onStop() {
        super.onStop();
        stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //BaseActivity.active=false;
        startService(new Intent(this, BackgroundTrackingService.class));
        stop();
    }

    Runnable myRunnable = new Runnable() {
        @Override
        public void run() {
            // your code here
            Intent i = new Intent(MessageSearchActivity.this, LoginActivity.class);
            startActivity(i);
        }
    };

    private Handler myHandler;

    private void start() {
        myHandler = new Handler();
        myHandler.postDelayed(myRunnable, TIME_TO_WAIT);
        stopService(new Intent(this, BackgroundTrackingService.class));
    }

    private void stop() {
        myHandler.removeCallbacks(myRunnable);
    }

    private void restart() {
        myHandler.removeCallbacks(myRunnable);
        myHandler.postDelayed(myRunnable, TIME_TO_WAIT);
    }


}
