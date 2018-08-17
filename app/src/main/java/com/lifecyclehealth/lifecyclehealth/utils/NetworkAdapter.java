package com.lifecyclehealth.lifecyclehealth.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.lifecyclehealth.lifecyclehealth.callbacks.VolleyCallback;
import com.lifecyclehealth.lifecyclehealth.designate.DesignateCallBack;
import com.lifecyclehealth.lifecyclehealth.designate.GlobalDesignateCallBack;
import com.lifecyclehealth.lifecyclehealth.model.CheckProviderResponse;
import com.lifecyclehealth.lifecyclehealth.model.GlobalCheckProviderResponse;

import org.json.JSONObject;

import java.util.HashMap;

import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.BASE_URL;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.STATUS_SUCCESS;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.URL_MEET_CHECK_IS_DESIGNATE_SELECT;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.URL_MEET_CHECK_IS_DESIGNATE_SELECT_GLOBAL;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.URL_MEET_CHECK_IS_DESIGNATE_UN_SELECT;

/**
 * Created by vaibhavi on 01-11-2017.
 */

public class NetworkAdapter {

    public boolean isConnectedToNetwork(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        // boolean isWiFi = activeNetwork.getType() == ConnectivityManager.TYPE_WIFI;
    }

    public void checkProviderList(Context context, NetworkRequestUtil networkRequestUtil, String id, final DesignateCallBack designateCallBack) {


        final HashMap<String, String> params = new HashMap<>();
        params.put("Provider_UserID", id);

        NetworkAdapter networkAdapter = new NetworkAdapter();
        boolean connectedToNetwork = networkAdapter.isConnectedToNetwork(context);
        if (connectedToNetwork) {
            try {
                JSONObject requestJson = new JSONObject(new Gson().toJson(params));
                networkRequestUtil.putDataSecure(BASE_URL + URL_MEET_CHECK_IS_DESIGNATE_SELECT, requestJson, new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {

                        Log.e("response check", response + "");
                        if (response != null) {
                            CheckProviderResponse checkProviderResponse = new Gson().fromJson(response.toString(), CheckProviderResponse.class);
                            if (checkProviderResponse != null) {
                                if (checkProviderResponse.getStatus().equalsIgnoreCase(STATUS_SUCCESS)) {
                                    designateCallBack.onSuccess(checkProviderResponse);

                                }
                            }
                        }
                    }

                    @Override
                    public void onError(VolleyError error) {

                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void checkProviderListGlobal(Context context, NetworkRequestUtil networkRequestUtil, String id, final GlobalDesignateCallBack designateCallBack) {


        final HashMap<String, String> params = new HashMap<>();
        params.put("Provider_UserID", id);

        NetworkAdapter networkAdapter = new NetworkAdapter();
        boolean connectedToNetwork = networkAdapter.isConnectedToNetwork(context);
        if (connectedToNetwork) {
            try {
                JSONObject requestJson = new JSONObject(new Gson().toJson(params));
                networkRequestUtil.getDataSecure(BASE_URL + URL_MEET_CHECK_IS_DESIGNATE_SELECT_GLOBAL+id, new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {

                        Log.e("response check", response + "");
                        if (response != null) {
                            GlobalCheckProviderResponse checkProviderResponse = new Gson().fromJson(response.toString(), GlobalCheckProviderResponse.class);
                            if (checkProviderResponse != null) {
                                if (checkProviderResponse.getStatus().equalsIgnoreCase(STATUS_SUCCESS)) {
                                    designateCallBack.onSuccess(checkProviderResponse);

                                }
                            }
                        }
                    }

                    @Override
                    public void onError(VolleyError error) {

                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    public void removeProviderList(Context context, NetworkRequestUtil networkRequestUtil, String id, final DesignateCallBack designateCallBack) {


        final HashMap<String, String> params = new HashMap<>();
        params.put("Designate_UserID", id);

        NetworkAdapter networkAdapter = new NetworkAdapter();
        boolean connectedToNetwork = networkAdapter.isConnectedToNetwork(context);
        if (connectedToNetwork) {
            try {
                JSONObject requestJson = new JSONObject(new Gson().toJson(params));
                networkRequestUtil.putDataSecure(BASE_URL + URL_MEET_CHECK_IS_DESIGNATE_UN_SELECT, requestJson, new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {

                        Log.e("response uncheck", response + "");
                        if (response != null) {
                            CheckProviderResponse checkProviderResponse = new Gson().fromJson(response.toString(), CheckProviderResponse.class);

                               /* if (checkProviderResponse.getStatus().equalsIgnoreCase(STATUS_SUCCESS)) {}*/
                                designateCallBack.onSuccess(checkProviderResponse);

                        }
                    }

                    @Override
                    public void onError(VolleyError error) {

                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

}
