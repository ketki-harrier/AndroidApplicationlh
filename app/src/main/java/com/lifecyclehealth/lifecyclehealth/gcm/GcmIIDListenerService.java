package com.lifecyclehealth.lifecyclehealth.gcm;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;


public class GcmIIDListenerService extends FirebaseInstanceIdService {
    private static final String TAG = "DEMO_IIDListenerService";
    String token;

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        token = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "onTokenRefresh");
    }
}
