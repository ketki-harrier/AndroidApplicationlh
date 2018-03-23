package com.lifecyclehealth.lifecyclehealth.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.view.View;
import android.widget.Button;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.lifecyclehealth.lifecyclehealth.R;
import com.lifecyclehealth.lifecyclehealth.application.MyApplication;
import com.lifecyclehealth.lifecyclehealth.callbacks.VolleyCallback;
import com.lifecyclehealth.lifecyclehealth.fingerprint.FingerPrintCallback;
import com.lifecyclehealth.lifecyclehealth.fingerprint.FingerPrintUtil;
import com.lifecyclehealth.lifecyclehealth.model.SurveySubmitResponse;
import com.lifecyclehealth.lifecyclehealth.utils.AESHelper;
import com.lifecyclehealth.lifecyclehealth.utils.AppConstants;
import com.lifecyclehealth.lifecyclehealth.utils.NetworkRequestUtil;

import org.json.JSONObject;

import zemin.notification.NotificationDelegater;
import zemin.notification.NotificationLocal;
import zemin.notification.NotificationView;

import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.BASE_URL;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.PREF_IS_PATIENT;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.STATUS_SUCCESS;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.TOUCH_EMAIL_ID;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.URL_CHANGE_TOUCH_STATE;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.seedValue;

public class TouchIDActivity extends BaseActivity implements View.OnClickListener, FingerPrintCallback, FingerPrintUtil.NextActivityCallback {

    private NetworkRequestUtil networkRequestUtil;
    private NotificationDelegater mDelegater;
    private NotificationLocal mLocal;
    private FingerPrintUtil fingerPrintUtil;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_touch_id);
        networkRequestUtil = new NetworkRequestUtil(this);
        initialize();
    }

    private void initialize() {
        fingerPrintUtil = new FingerPrintUtil(getApplicationContext(), this, this);
        Button btnAccept = (Button) findViewById(R.id.btnAccept);
        Button btnDecline = (Button) findViewById(R.id.btnDecline);

        mDelegater = NotificationDelegater.getInstance();
        mLocal = mDelegater.local();
        mLocal.setView((NotificationView) findViewById(R.id.nv));

        btnAccept.setOnClickListener(this);
        btnDecline.setOnClickListener(this);

    }

    @Override
    String getTag() {
        return "TouchIDActivity";
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAccept: {
                //changeTouchEnable("true");
                if (fingerPrintUtil.isUserAllowToUseFingerPrint()) {
                    fingerPrintStart();
                } else {
                    showDialogWithOkButton("This device does not have a TouchID sensor.");
                }
                break;
            }
            case R.id.btnDecline: {
                //changeTouchEnable("false");
                startActivity(new Intent(TouchIDActivity.this, LoginActivity.class));
                finish();
                break;
            }
        }
    }


    private void fingerPrintStart() {
        fingerPrintLogic();
    }
    private void fingerPrintLogic() {

        fingerPrintUtil.updateAllFlagsForUser();
        if (fingerPrintUtil.isUserAllowToUseFingerPrint()) {
            if (!fingerPrintUtil.isFingerPrintDontAskMeEnabled()) {
                if (!fingerPrintUtil.isFingerPrintLoginEnabled()) {
                    // touch not enable in device and
                    if (fingerPrintUtil.isTouchEnableInService()) {
                        // but still touch enable in service
                    }
                    fingerPrintUtil.showConfigurationDialog(this, this.getFragmentManager(), FingerPrintUtil.FLAG_REGISTER);
                } else {
                    /* this will only called if fingerprint login configure but skip using that and do traditional login*/
                    if (fingerPrintUtil.isUserWantTraditionalLogin()) {
                        // reset
                        fingerPrintUtil.updateUserWantTraditionalLogin(false);
                    } else if (!fingerPrintUtil.isTouchEnableInService()) {
                        fingerPrintUtil.showConfigurationDialog(this, this.getFragmentManager(), FingerPrintUtil.FLAG_REGISTER);
                    } else {
                        // onSuccess(isEmailVerified, userBaseKey, response);
                    }
                }
            } else {
                onSuccess();
            }
        } else {
            //printLog("User not allow to use fingerprint");
            onSuccess();
        }
    }

    private void onSuccess() {

    }

    private void changeTouchEnable(String touch) {
        showProgressDialog(true);
        if (isConnectedToNetwork(this)) {
            networkRequestUtil.putDataSecure(BASE_URL + URL_CHANGE_TOUCH_STATE + touch, null, new VolleyCallback() {
                @Override
                public void onSuccess(JSONObject response) {
                    showProgressDialog(false);
                    printLog("ResponseTouch state response" + response);
                    SurveySubmitResponse response1 = new Gson().fromJson(response.toString(), SurveySubmitResponse.class);
                    if (response1 != null) {
                        if (response1.getStatus().equalsIgnoreCase(STATUS_SUCCESS)) {
                            MyApplication.getInstance().addBooleanToSharedPreference(AppConstants.LOCAL_DB_TOUCH, true);
                            try {
                                String emailId = AESHelper.decrypt(seedValue, MyApplication.getInstance().getFromSharedPreference(AppConstants.EMAIL_ID));
                                MyApplication.getInstance().addToSharedPreference(TOUCH_EMAIL_ID, AESHelper.encrypt(seedValue, emailId));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                     /*       startActivity(new Intent(TouchIDActivity.this, LoginActivity.class));
                            finish();*/
                            if (MyApplication.getInstance().getBooleanFromSharedPreference(PREF_IS_PATIENT)) {
                                divertToHomeScreen();
                            }else {
                                divertToCareGiverScreen();
                            }

                        } else showDialogWithOkButton(response1.getMessage());

                    } else {
                        showDialogWithOkButton(getString(R.string.error_someting_went_wrong));
                    }
                }

                @Override
                public void onError(VolleyError error) {
                    showProgressDialog(false);
                }
            });
        } else {
            showProgressDialog(false);
            showDialogWithOkButton(getString(R.string.error_no_network));
        }
    }

    /* For diverting to homeScreen*/
    private void divertToHomeScreen() {
        Intent intent = new Intent(TouchIDActivity.this, MainActivity.class);
        intent.putExtra("from_notification", "0");
        startActivity(intent);
        //startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }

    private void divertToCareGiverScreen() {
        startActivity(new Intent(TouchIDActivity.this, CareGiverActivity.class));
        finish();
    }

    @Override
    public void onSuccess(FingerprintManagerCompat.AuthenticationResult result) {
        changeTouchEnable("true");
    }

    @Override
    public void onError(String error) {

    }

    @Override
    public void onMaxAttemptReach() {

    }

    @Override
    public void onCancelDialog() {

    }

    @Override
    public void onUserResponse(int flag) {

    }
}
