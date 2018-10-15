package com.lifecyclehealth.lifecyclehealth.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.lifecyclehealth.lifecyclehealth.R;
import com.lifecyclehealth.lifecyclehealth.activities.MainActivity;

/**
 * Created by vaibhavi on 14-12-2017.
 */

public class GcmBroadcast extends BroadcastReceiver {
    public GcmBroadcast() {
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        // This method is called when this BroadcastReceiver receives an Intent broadcast.
        Toast.makeText(context, "Action: " + intent.getAction(), Toast.LENGTH_SHORT).show();
        MainActivity mainActivity=new MainActivity();
        mainActivity.meetPushNotification = true;
        mainActivity.bottomBar.selectTabWithId(R.id.tab_message);
        mainActivity.bottomBarCaregiver.selectTabWithId(R.id.tab_message);
    }
}