package com.lifecyclehealth.lifecyclehealth.fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.lifecyclehealth.lifecyclehealth.R;
import com.lifecyclehealth.lifecyclehealth.activities.MainActivity;
import com.lifecyclehealth.lifecyclehealth.application.MyApplication;
import com.lifecyclehealth.lifecyclehealth.callbacks.VolleyCallback;
import com.lifecyclehealth.lifecyclehealth.fingerprint.FingerPrintCallback;
import com.lifecyclehealth.lifecyclehealth.fingerprint.FingerPrintUtil;
import com.lifecyclehealth.lifecyclehealth.model.SurveySubmitResponse;
import com.lifecyclehealth.lifecyclehealth.utils.AESHelper;
import com.lifecyclehealth.lifecyclehealth.utils.AppConstants;

import org.json.JSONObject;

import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.BASE_URL;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.EMAIL_ID;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.STATUS_SUCCESS;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.TOUCH_EMAIL_ID;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.URL_CHANGE_TOUCH_STATE;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.seedValue;

/**
 * A simple {@link Fragment} subclass.
 */
public class TouchIDFragment extends BaseFragmentWithOptions implements View.OnClickListener, FingerPrintCallback, FingerPrintUtil.NextActivityCallback {

    private MainActivity mainActivity;
    private String emailId;
    private FingerPrintUtil fingerPrintUtil;

    public TouchIDFragment() {
        // Required empty public constructor
    }

    @Override
    String getFragmentTag() {
        return "TouchIDFragment";
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file_show for the fragment
        return inflater.inflate(R.layout.activity_touch_id, parent, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    backPage();
                    return true;
                }
                return false;
            }
        });
    }


    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        initializeView(view);
    }

    private void initializeView(View view) {
        fingerPrintUtil = new FingerPrintUtil(getActivity(), this, this);
        Button btnAccept = (Button) view.findViewById(R.id.btnAccept);
        Button btnDecline = (Button) view.findViewById(R.id.btnDecline);

        try {
            emailId = AESHelper.decrypt(seedValue, MyApplication.getInstance().getFromSharedPreference(AppConstants.EMAIL_ID));
        } catch (Exception e) {
            e.printStackTrace();
        }


        btnAccept.setOnClickListener(this);
        btnDecline.setOnClickListener(this);
    }

    private void backPage() {
        getFragmentManager().popBackStack();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAccept: {
                if (fingerPrintUtil.isUserAllowToUseFingerPrint()) {
                    fingerPrintStart();
                } else {
                    showDialogWithOkButton("This device does not have a TouchID sensor.");
                }
                break;
            }
            case R.id.btnDecline: {
                backPage();
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
                    fingerPrintUtil.showConfigurationDialog(getActivity(), getActivity().getFragmentManager(), FingerPrintUtil.FLAG_REGISTER);
                } else {
                    /* this will only called if fingerprint login configure but skip using that and do traditional login*/
                    if (fingerPrintUtil.isUserWantTraditionalLogin()) {
                        // reset
                        fingerPrintUtil.updateUserWantTraditionalLogin(false);
                    } else if (!fingerPrintUtil.isTouchEnableInService()) {
                        fingerPrintUtil.showConfigurationDialog(getActivity(), getActivity().getFragmentManager(), FingerPrintUtil.FLAG_REGISTER);
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

        if (isConnectedToNetwork(mainActivity)) {
            mainActivity.networkRequestUtil.putDataSecure(BASE_URL + URL_CHANGE_TOUCH_STATE + touch, null, new VolleyCallback() {
                @Override
                public void onSuccess(JSONObject response) {
                    showProgressDialog(false);
                    printLog("ResponseTouch state response" + response);
                    SurveySubmitResponse response1 = new Gson().fromJson(response.toString(), SurveySubmitResponse.class);
                    if (response1 != null) {
                        if (response1.getStatus().equalsIgnoreCase(STATUS_SUCCESS)) {
                            try {
                                MyApplication.getInstance().addToSharedPreference(TOUCH_EMAIL_ID, AESHelper.encrypt(seedValue, emailId));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            MyApplication.getInstance().addBooleanToSharedPreference(AppConstants.LOCAL_DB_TOUCH, true);
                            backPage();
                            getFragmentManager().popBackStack();
                        } else {
                            showDialogWithOkButton(response1.getMessage());
                        }

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