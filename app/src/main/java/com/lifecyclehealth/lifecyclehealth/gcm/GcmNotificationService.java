/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lifecyclehealth.lifecyclehealth.gcm;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

/*import com.google.android.gms.gcm.GcmListenerService;*/
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.lifecyclehealth.lifecyclehealth.R;
import com.lifecyclehealth.lifecyclehealth.activities.BaseActivity;
import com.lifecyclehealth.lifecyclehealth.activities.BaseActivityLogin;
import com.lifecyclehealth.lifecyclehealth.activities.LoginActivity;
import com.lifecyclehealth.lifecyclehealth.activities.MainActivity;
import com.lifecyclehealth.lifecyclehealth.activities.MeetEventActivity;
import com.lifecyclehealth.lifecyclehealth.application.MyApplication;
import com.lifecyclehealth.lifecyclehealth.fragments.MeetFragment;
import com.moxtra.binder.ui.app.ApplicationDelegate;
import com.moxtra.sdk.ChatClient;
import com.moxtra.sdk.chat.model.Chat;
import com.moxtra.sdk.common.ApiCallback;
import com.moxtra.sdk.meet.model.Meet;
import com.moxtra.sdk.notification.NotificationHelper;
import com.moxtra.sdk.notification.NotificationManager;

import java.util.List;
import java.util.Map;

import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.IS_IN_MEET_FRAGMENT;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.IS_LOGINACTIVITY_ALIVE;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.IS_MAINACTIVITY_ALIVE;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.IS_USERNAME_EDITABLE;

public class GcmNotificationService extends FirebaseMessagingService {
    private static final String TAG = "DEMO_GcmIntentService";
    public int NOTIFICATION_ID;
    private android.app.NotificationManager mNotificationManager;
    String title;

    public GcmNotificationService() {
        super();
        Log.d(TAG, "GcmNotificationService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
        mNotificationManager = (android.app.NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
    }


    @Override
    //   public void onMessageReceived(String s, Bundle bundle) {
    public void onMessageReceived(RemoteMessage message) {
        /* super.onMessageReceived(s, bundle);*/
        super.onMessageReceived(message);

        String from = message.getFrom();
        // Map data = message.getData();
        Log.i("GcmNotificationService", "message received ");
        Intent intent = new Intent();
        // intent.putExtras((Intent) data);
        Map<String, String> datanew = message.getData();
        for (String key : datanew.keySet()) {
            intent.putExtra(key, datanew.get(key));
        }

        if (message.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + message.getData());
        }


        try {
            if (datanew != null && !datanew.isEmpty()) {
            /*    NotificationManager notificationManager = ChatClient.getClientDelegate().getNotificationManager();
                if (notificationManager.isValidRemoteNotification(intent)) {
                    int type = notificationManager.getValidNotificationType(intent);
                    String title = notificationManager.getNotificationMessageText(this, intent);*/

                NotificationManager notificationManager = ChatClient.getClientDelegate().getNotificationManager();
                NotificationHelper notificationHelper = new NotificationHelper();

                if (notificationHelper.isValidRemoteNotification(intent)) {
                    int type = notificationHelper.getValidNotificationType(intent);
                    //final String title = notificationHelper.getNotificationMessageText(this, intent);

                   /* final  String title = message.getNotification().getTitle();
                    String body = message.getNotification().getBody();*/

                    boolean handled = NotificationHelper.isValidRemoteNotification(intent);
                    if (handled) {
                        // This is a chat sdk message and it will be handled by chat sdk
                        title = NotificationHelper.getNotificationMessageText(this, intent);
                        sendChatSDKNotification(title, null, intent);
                    } else {
                        // Not a chat sdk message and app should handle it.
                        Log.i(TAG, "App should handle it.");
                    }
                    Log.i(TAG, "Received: " + intent.getExtras().toString());


                    Log.i(TAG, "Here comes a notification: type=" + type + ", title=" + title);


                    notificationManager.fetchChatFromChatNotification(intent, new ApiCallback<Chat>() {
                        @Override
                        public void onCompleted(Chat chat) {
                            sendNotification(title);
                        }

                        @Override
                        public void onError(int i, String s) {
                            Log.w(TAG, "Ignore invalid remote notification.");
                        }
                    });

                    notificationManager.fetchMeetFromMeetNotification(intent, new ApiCallback<Meet>() {
                        @Override
                        public void onCompleted(Meet meet) {
                            sendNotificationMeet(title);
                        }

                        @Override
                        public void onError(int i, String s) {
                            Log.w(TAG, "Ignore invalid remote notification.");
                        }
                    });
                }

               /* NotificationHelper notificationHelper = new NotificationHelper();
                if (notificationHelper.isValidRemoteNotification(intent)) {
                    int type = notificationHelper.getValidNotificationType(intent);
                    String title = notificationHelper.getNotificationMessageText(this, intent);
                    Log.i(TAG, "Here comes a notification: type=" + type + ", title=" + title);
                    switch (type) {
                        case 100:
                            sendNotification(title);
                            break;

                        case 200:
                            sendNotificationMeet(title);
                            break;

                        default:
                            sendNotification(title);
                            break;
                    }
                    //sendNotification(title);
                } else {
                    Log.w(TAG, "Ignore invalid remote notification.");
                    //sendNotification("message receive");
                }*/
            } else {
                Log.w(TAG, "Ignore notification without any extended data.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isActivityRunning(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;

    }


    private void sendNotification(String msg) {

        if (isActivityRunning(getApplicationContext())) {
            callLoginActivityNotification(msg);

        } else {
            if (MyApplication.getInstance().getBooleanFromSharedPreference(IS_MAINACTIVITY_ALIVE)) {
                BaseActivity.getInstance().showLocalNotification(msg);
            } else if (MyApplication.getInstance().getBooleanFromSharedPreference(IS_LOGINACTIVITY_ALIVE)) {
                BaseActivityLogin.getInstance().showLocalNotification(msg);
            } else {
                callMainActivityNotification(msg);
            }
        }


    }

    private void sendChatSDKNotification(String msg, Uri uri, Intent intent) {
        Log.d(TAG, "Got notification: msg = " + msg + ", uri = " + uri);
        // NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Intent notificationIntent = new Intent(this, LoginActivity.class);
        if (intent != null) {
            notificationIntent.putExtras(intent);
        }
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this, ApplicationDelegate.getInstance().getNotificationChannelID())
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(getString(getApplicationInfo().labelRes))
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                        .setContentText(msg)
                        .setAutoCancel(true)
                        .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE);

        if (uri != null) {
            builder.setSound(uri);
        }

        builder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, builder.build());
    }


    private void callMainActivityNotification(String msg) {
        Intent notificationIntent;
        notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        notificationIntent.putExtra("from_notification", "1");
        NOTIFICATION_ID = (int) System.currentTimeMillis();
        PendingIntent contentIntent = PendingIntent.getActivity(this, NOTIFICATION_ID, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        String channelId = getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(getString(getApplicationInfo().labelRes))
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                        .setContentText(msg)
                        .setAutoCancel(true)
                        .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE);

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

    private void callLoginActivityNotification(String msg) {
        Intent notificationIntent;
        notificationIntent = new Intent(this, LoginActivity.class);
        notificationIntent.putExtra("from_notification", "0");
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

        NOTIFICATION_ID = (int) System.currentTimeMillis();
        PendingIntent contentIntent = PendingIntent.getActivity(this, NOTIFICATION_ID, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        String channelId = getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(getString(getApplicationInfo().labelRes))
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                        .setContentText(msg)
                        .setAutoCancel(true)
                        .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE);

        mBuilder.setContentIntent(contentIntent);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    mNotificationManager.IMPORTANCE_DEFAULT);
            mNotificationManager.createNotificationChannel(channel);
        }


        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

    private void sendNotificationMeet(String msg) {

        if (MyApplication.getInstance().getBooleanFromSharedPreference(IS_IN_MEET_FRAGMENT)) {
            if (MyApplication.getInstance().getBooleanFromSharedPreference(IS_MAINACTIVITY_ALIVE)) {
                MainActivity.getInstance().checkAliveFragment();
            }
        }

        if (isActivityRunning(getApplicationContext())) {
            callLoginActivityNotification(msg);
        } else {
            if (MyApplication.getInstance().getBooleanFromSharedPreference(IS_MAINACTIVITY_ALIVE)) {
                BaseActivity.getInstance().showLocalNotification(msg);
            } else if (MyApplication.getInstance().getBooleanFromSharedPreference(IS_LOGINACTIVITY_ALIVE)) {
                BaseActivityLogin.getInstance().showLocalNotification(msg);
            } else {
                callMainActivityNotification(msg);
            }
        }




      /*  Intent notificationIntent = new Intent();
        if (isActivityRunning(getApplicationContext())) {
            notificationIntent = new Intent(this, LoginActivity.class);
            notificationIntent.putExtra("from_notification", "0");
            notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        } else {
            if (MyApplication.getInstance().getBooleanFromSharedPreference(IS_MAINACTIVITY_ALIVE)) {
                notificationIntent = new Intent(this, MainActivity.class);
                notificationIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                notificationIntent.putExtra("from_notification", "1");
            }
        }
        NOTIFICATION_ID = (int) System.currentTimeMillis();
        PendingIntent contentIntent = PendingIntent.getActivity(this, NOTIFICATION_ID, notificationIntent,
                PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(getString(getApplicationInfo().labelRes))
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                        .setContentText(msg)
                        .setAutoCancel(true)
                        .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE);


        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());*/

    }


}
