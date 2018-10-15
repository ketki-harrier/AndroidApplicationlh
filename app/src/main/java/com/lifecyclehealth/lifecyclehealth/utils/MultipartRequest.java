package com.lifecyclehealth.lifecyclehealth.utils;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.lifecyclehealth.lifecyclehealth.activities.LoginActivity;
import com.lifecyclehealth.lifecyclehealth.application.MyApplication;
import com.lifecyclehealth.lifecyclehealth.callbacks.VolleyCallback;
import com.lifecyclehealth.lifecyclehealth.db.LifecycleDatabase;
import com.lifecyclehealth.lifecyclehealth.multipart.VolleyMultipartRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by vaibhavi on 07-09-2017.
 */

public class MultipartRequest {
    private Context context;
    private String TAG;
    private static MultipartRequest mInstance;
    LifecycleDatabase lifecycleDatabase;


    public MultipartRequest(Context context) {
        this.context = context;
        lifecycleDatabase=new LifecycleDatabase(context);
        mInstance = this;
    }

    public MultipartRequest(Context context, String TAG) {
        this.context = context;
        mInstance = this;
        this.TAG = TAG;
    }

    public static synchronized MultipartRequest getInstance() {
        return mInstance;
    }

    public void postDataMultipartSecure(String url, final Map<String, String> params, final Map<String, VolleyMultipartRequest.DataPart> paramDataPart, final VolleyCallback callback) {

        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, url, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                String resultResponse = new String(response.data);
                try {
                    JSONObject result = new JSONObject(resultResponse);
                    printLog("Response: " + result);
                    callback.onSuccess(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                printLog("Error:" + error.getMessage());
                callback.onError(error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                return params;
            }
            @Override
            protected Map<String, DataPart> getByteData() {
                return paramDataPart;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                //headers.put("X-Access-Token", MyApplication.getInstance().getFromSharedPreference(AppConstants.USER_TOKEN));
                String token=lifecycleDatabase.retrieveToken();
                headers.put("X-Access-Token", token);
                //headers.put("X-Access-Token", LoginActivity.loginToken);
                //headers.put("Content-Type", "multipart/form-data");
                return headers;
            }

        };

        RetryPolicy policy = new DefaultRetryPolicy(AppConstants.REQUEST_TIMEOUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        multipartRequest.setRetryPolicy(policy);
        MyApplication.getInstance().addToRequestQueue(multipartRequest, "");
    }

    private void printLog(String message) {
        Log.e("NetworkUtil", message);
    }


    public String getServerError(VolleyError error) {
        NetworkResponse response = error.networkResponse;
        if (error instanceof ServerError && response != null) {
            try {
                String res = new String(response.data,
                        HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                printLog("ObjectError" + res);
                return res;
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }
        }
        return null;
    }

    public void knowAboutError(VolleyError error) {
// As of f605da3 the following should work
        NetworkResponse response = error.networkResponse;
        if (error instanceof ServerError && response != null) {
            try {
                String res = new String(response.data,
                        HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                printLog("ObjectError" + res);
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }
        } else {
            printLog("Error is null");
        }
    }


/*     new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            NetworkResponse networkResponse = error.networkResponse;
            String errorMessage = "Unknown error";
            if (networkResponse == null) {
                if (error.getClass().equals(TimeoutError.class)) {
                    errorMessage = "Request timeout";
                } else if (error.getClass().equals(NoConnectionError.class)) {
                    errorMessage = "Failed to connect server";
                }
            } else {
                String result = new String(networkResponse.data);
                try {
                    JSONObject response = new JSONObject(result);
                    String status = response.getString("status");
                    String message = response.getString("message");

                    Log.e("Error Status", status);
                    Log.e("Error Message", message);

                    if (networkResponse.statusCode == 404) {
                        errorMessage = "Resource not found";
                    } else if (networkResponse.statusCode == 401) {
                        errorMessage = message + " Please login again";
                    } else if (networkResponse.statusCode == 400) {
                        errorMessage = message + " Check your inputs";
                    } else if (networkResponse.statusCode == 500) {
                        errorMessage = message + " Something is getting wrong";
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            Log.i("Error", errorMessage);
            error.printStackTrace();
        }
    })*/

}
