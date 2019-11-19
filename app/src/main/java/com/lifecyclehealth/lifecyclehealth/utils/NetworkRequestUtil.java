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
import com.android.volley.toolbox.JsonObjectRequest;
import com.lifecyclehealth.lifecyclehealth.activities.LoginActivity;
import com.lifecyclehealth.lifecyclehealth.application.MyApplication;
import com.lifecyclehealth.lifecyclehealth.callbacks.VolleyCallback;
import com.lifecyclehealth.lifecyclehealth.db.LifecycleDatabase;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by satyam on 27/09/2016.
 */
public class NetworkRequestUtil {
    private Context context;
    private String TAG;
    private static NetworkRequestUtil mInstance;
    LifecycleDatabase lifecycleDatabase;
    String token;
    // Boolean sign;

    public NetworkRequestUtil(Context context) {
        this.context = context;
        lifecycleDatabase = new LifecycleDatabase(context);
        mInstance = this;
        // sign=PreferenceUtils.getESignature(context);
    }

    public NetworkRequestUtil(Context context, String TAG) {
        this.context = context;
        mInstance = this;
        this.TAG = TAG;
    }

    public static synchronized NetworkRequestUtil getInstance() {
        return mInstance;
    }


    public void postData(String url, final JSONObject requestJson, final VolleyCallback callback) {
        try {
            //final JSONObject jsonBody = new JSONObject();
            //jsonBody.put("request", requestJson);

            final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    url,
                    requestJson,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            printLog("Response: " + response);
                            callback.onSuccess(response);

                        }
                    },

                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            printLog("Error:" + error.getMessage());
                            callback.onError(error);
                        }
                    }
            ) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    // headers.put("Content-Type", "application/json; charset=us-ascii");
                    // headers.put("Content-Type", "application/json; charset=UTF-8");
                    // headers.put("Content-Type", "application/json");
                    //headers.put("X-Access-Token", MyApplication.getInstance().getFromSharedPreference(AppConstants.USER_TOKEN));
                    headers.put("lh-user-agent", "LifecycleHealth Android");
                    return headers;
                }

//                @Override
//                public String getBodyContentType() {
//                    return "application/json; charset=utf-8";
//                }
            };
            RetryPolicy policy = new DefaultRetryPolicy(AppConstants.REQUEST_TIMEOUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            jsonObjReq.setRetryPolicy(policy);
            MyApplication.getInstance().addToRequestQueue(jsonObjReq, TAG);


        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            // Cancelling request
            MyApplication.getInstance().getRequestQueue().cancelAll(TAG);
        }
    }

    /* uses token base authentication*/
    public void postDataSecure(String url, final JSONObject requestJson, final VolleyCallback callback) {
        try {
            //final JSONObject jsonBody = new JSONObject();
            //jsonBody.put("request", requestJson);

            final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    url,
                    requestJson,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            printLog("Response: " + response);
                            callback.onSuccess(response);

                        }
                    },

                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            printLog("Error:" + error.getMessage());
                            printLog("error response:" + error.getMessage());
                            knowAboutError(error);
                            callback.onError(error);
                        }
                    }
            ) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    // headers.put("Content-Type", "application/json; charset=us-ascii");
                    // headers.put("Content-Type", "application/json; charset=UTF-8");
                    headers.put("Content-Type", "application/json");
                    headers.put("X-Access-Token", MyApplication.getInstance().getFromSharedPreference(AppConstants.USER_TOKEN));
                    return headers;
                }

//                @Override
//                public String getBodyContentType() {
//                    return "application/json; charset=utf-8";
//                }
            };
            RetryPolicy policy = new DefaultRetryPolicy(AppConstants.REQUEST_TIMEOUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            jsonObjReq.setRetryPolicy(policy);
            MyApplication.getInstance().addToRequestQueue(jsonObjReq, TAG);


        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            // Cancelling request
            MyApplication.getInstance().getRequestQueue().cancelAll(TAG);
        }
    }

    public void getData(String url, final VolleyCallback callback) {
        try {

            final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                    url,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            printLog("Response: " + response);
                            callback.onSuccess(response);

                        }
                    },

                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            printLog("Error:" + error.getMessage());
                            callback.onError(error);
                        }
                    }
            ) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json");
                    return headers;
                }
            };
            RetryPolicy policy = new DefaultRetryPolicy(AppConstants.REQUEST_TIMEOUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            jsonObjReq.setRetryPolicy(policy);
            MyApplication.getInstance().addToRequestQueue(jsonObjReq, TAG);

        } catch (Exception e) {
            Log.e(TAG, e.getMessage());

            // Cancelling request
            MyApplication.getInstance().getRequestQueue().cancelAll(TAG);
        }
    }

    /* for secure connection where it uses user token*/
    public void getDataSecure(String url, final VolleyCallback callback) {
        try {

            final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                    url,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            printLog("Response: " + response);
                            callback.onSuccess(response);

                        }
                    },

                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            printLog("Error:" + error.getMessage());
                            callback.onError(error);
                        }
                    }
            ) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json");
                    //headers.put("X-Access-Token", MyApplication.getInstance().getFromSharedPreference(AppConstants.USER_TOKEN));
                    //headers.put("X-Access-Token", LoginActivity.loginToken);
                    token = lifecycleDatabase.retrieveToken();
                    headers.put("X-Access-Token", token);
                    return headers;
                }
            };
            RetryPolicy policy = new DefaultRetryPolicy(AppConstants.REQUEST_TIMEOUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            jsonObjReq.setRetryPolicy(policy);
            MyApplication.getInstance().addToRequestQueue(jsonObjReq, TAG);


        } catch (Exception e) {
            Log.e(TAG, e.getMessage());

            // Cancelling request
            MyApplication.getInstance().getRequestQueue().cancelAll(TAG);
        }
    }

    public void putData(String url, JSONObject requestJson, final VolleyCallback callback) {
        try {

            final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
                    url, requestJson,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            printLog("Response: " + response);
                            callback.onSuccess(response);

                        }
                    },

                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            printLog("Error:" + error.getMessage());
                            callback.onError(error);
                        }
                    }
            ) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json");
                    return headers;
                }
            };
            RetryPolicy policy = new DefaultRetryPolicy(AppConstants.REQUEST_TIMEOUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            jsonObjReq.setRetryPolicy(policy);
            MyApplication.getInstance().addToRequestQueue(jsonObjReq, TAG);


        } catch (Exception e) {
            Log.e(TAG, e.getMessage());

            // Cancelling request
            MyApplication.getInstance().getRequestQueue().cancelAll(TAG);
        }
    }

    public void putDataSecure(String url, JSONObject requestJson, final VolleyCallback callback) {
        try {
            final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
                    url, requestJson,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            printLog("Response: " + response);
                            callback.onSuccess(response);

                        }
                    },

                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            printLog("Error:" + error.getMessage());
                            callback.onError(error);
                        }
                    }
            ) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Accept", "application/json");
                    /*headers.put("Content-Type","application/json");*/
                    headers.put("Content-Type", "application/json");
                    headers.put("X-Access-Token", MyApplication.getInstance().getFromSharedPreference(AppConstants.USER_TOKEN));
                    //  headers.put("Requier_E_Signature",sign);
                    return headers;
                }
            };
            RetryPolicy policy = new DefaultRetryPolicy(AppConstants.REQUEST_TIMEOUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            jsonObjReq.setRetryPolicy(policy);
            MyApplication.getInstance().addToRequestQueue(jsonObjReq, TAG);


        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            // Cancelling request
            MyApplication.getInstance().getRequestQueue().cancelAll(TAG);
        }
    }

    public void knowAboutError(VolleyError error) {
        // As of f605da3 the following should work
        NetworkResponse response = error.networkResponse;
        if (error instanceof ServerError && response != null) {
            try {
                String res = new String(response.data,
                        HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                // Now you can use any deserializer to make sense of data
//                JSONObject obj = new JSONObject(res);
                printLog("ObjectError know" + res);
            } catch (UnsupportedEncodingException e1) {
                // Couldn't properly decode data to string
                e1.printStackTrace();
            }
//            } catch (JSONException e2) {
//                // returned data is not JSONObject?
//                e2.printStackTrace();
//            }
        }
    }

    private void printLog(String message) {
        Log.e("NetworkUtil", message);
    }

}
