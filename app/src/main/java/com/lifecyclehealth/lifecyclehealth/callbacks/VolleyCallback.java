package com.lifecyclehealth.lifecyclehealth.callbacks;

import com.android.volley.VolleyError;

import org.json.JSONObject;

public interface VolleyCallback {
    void onSuccess(JSONObject response);
   // void onSuccess1(String response);

    void onError(VolleyError error);
}