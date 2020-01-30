package com.lifecyclehealth.lifecyclehealth.callbacks;

import com.android.volley.VolleyError;

import org.json.JSONObject;

public interface VolleyCallback1 {
   // void onSuccess(JSONObject response);
    void onSuccess1(String response);

    void onError(VolleyError error);
}