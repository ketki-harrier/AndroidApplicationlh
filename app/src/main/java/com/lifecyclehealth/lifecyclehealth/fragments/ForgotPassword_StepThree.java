package com.lifecyclehealth.lifecyclehealth.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.lifecyclehealth.lifecyclehealth.R;
import com.lifecyclehealth.lifecyclehealth.activities.ForgotPasswordHolderActivity;
import com.lifecyclehealth.lifecyclehealth.application.MyApplication;
import com.lifecyclehealth.lifecyclehealth.callbacks.VolleyCallback;
import com.lifecyclehealth.lifecyclehealth.dto.NormalResponse;
import com.segment.analytics.Analytics;
import com.segment.analytics.Properties;

import org.json.JSONObject;

import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.BASE_URL;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.CONST_STEP_THREE;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.PREF_FORGOT_PASS_CURRENT_STEP;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.REQUEST_KEY_AUTH_ID;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.REQUEST_KEY_AUTH_OTP;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.REQUEST_KEY_PASSWORD;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.STATUS_SUCCESS;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.URL_FORGOT_PASSWORD_UPDATE_PASS;

/**
 * Created by satyam on 24/04/2017.
 */

public class ForgotPassword_StepThree extends BaseFragmentWithOptions {
    public static final String EXTRA_FRAGMENT_THREE = "step_three";
    public static final String EXTRA_FRAGMENT_THREE_ID = "step_three_ID";
    private ForgotPasswordHolderActivity holderActivity;
    EditText editTextPassword, editTextConfermPassword;
    private TextView TextViewErrorPassword, TextViewErrorConfermPassword;
    private Button buttonSavePass;
    ImageView confirmPasswordImage;

    int password = 0, confirmPasword = 0;

    public static ForgotPassword_StepThree newInstance(String otp, String id) {
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_FRAGMENT_THREE, otp);
        bundle.putString(EXTRA_FRAGMENT_THREE_ID, id);
        ForgotPassword_StepThree stepFour = new ForgotPassword_StepThree();
        stepFour.setArguments(bundle);
        return stepFour;
    }

    @Override
    String getFragmentTag() {
        return "ForgotPassword_StepThree";
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        holderActivity = (ForgotPasswordHolderActivity) context;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_forgot_pass_step_three, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupToolbarTitle(holderActivity.getToolbar(), getString(R.string.forgot_pass_title_step_three));
        hideKeyboard(view);
        setupView(view);
    }

    @Override
    public void onResume() {
        super.onResume();
        MyApplication.getInstance().addIntToSharedPreference(PREF_FORGOT_PASS_CURRENT_STEP, CONST_STEP_THREE);
    }

    private void setupView(View view) {
        Analytics.with(getContext()).screen("Reset Password");
        confirmPasswordImage = (ImageView) view.findViewById(R.id.confirmPasswordImage);
        TextViewErrorPassword = (TextView) view.findViewById(R.id.errorViewUserNameTv);
        TextViewErrorConfermPassword = (TextView) view.findViewById(R.id.errorViewPasswordTv);
        setErrorOf();
        editTextPassword = (EditText) view.findViewById(R.id.newPasswordEditText);
        editTextConfermPassword = (EditText) view.findViewById(R.id.confirmPasswordEditText);
        buttonSavePass = (Button) view.findViewById(R.id.savePasswordIn);
        buttonSavePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValidCode(editTextPassword) && isValidCode(editTextConfermPassword)) {
                    //printLog("Equal:" + editTextPassword.getText().toString().equals(editTextConfermPassword.getText().toString()));
                    if (editTextPassword.getText().toString().equals(editTextConfermPassword.getText().toString())) {
                        TextViewErrorConfermPassword.setVisibility(View.INVISIBLE);
                        if (password == 1 && confirmPasword == 1)
                            Analytics.with(getContext()).track("Save Password", new Properties().putValue("category", "Mobile"));
                        sendPassword();
                    } else {
                        setNotMatchError();
                    }
                } else {
                    setBlankErrorBoth();
                }
            }
        });

        editTextPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //printLog("Password:" + validPassword(charSequence.toString() + ""));
                if (validPassword(charSequence.toString())) {
                    password = 1;
                    TextViewErrorPassword.setVisibility(View.INVISIBLE);
                } else {
                    password = 0;
                    TextViewErrorPassword.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        editTextConfermPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                printLog(editTextPassword.getText().toString() + "||" + charSequence.toString());

                if (editTextPassword.getText().toString().equals(charSequence.toString())) {
                    confirmPasword = 1;
                    confirmPasswordImage.setVisibility(View.VISIBLE);
                    TextViewErrorConfermPassword.setVisibility(View.INVISIBLE);
                } else {
                    confirmPasword = 0;
                    confirmPasswordImage.setVisibility(View.GONE);
                    TextViewErrorConfermPassword.setVisibility(View.VISIBLE);
                    TextViewErrorConfermPassword.setText(getString(R.string.error_in_password_confirm_2));
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private boolean validPassword(String password) {
        printLog("PasswordToIdentify:" + password);
        boolean upCase = false;
        boolean loCase = false;
        boolean isDigit = false;
        if (password.length() > 6) {
            if (password.matches(".*[A-Z].*")) {
                upCase = true;
            }
            if (password.matches(".*[a-z].*")) {
                loCase = true;
            }
            if (password.matches(".*[1-9].*")) {
                isDigit = true;
            }
        }
        printLog("||Upcase:" + upCase + "||LoCase" + loCase + "||Isdigit" + isDigit);
        return (upCase && loCase && isDigit);
    }

    private boolean isValidCode(EditText editText) {
        return (editText.getText().toString().trim().length() > 0);
    }

    private void setErrorOf() {
        TextViewErrorConfermPassword.setVisibility(View.INVISIBLE);
        TextViewErrorPassword.setVisibility(View.INVISIBLE);
    }

    private void setBlankErrorBoth() {
        TextViewErrorPassword.setVisibility(View.VISIBLE);
        TextViewErrorConfermPassword.setVisibility(View.VISIBLE);

        TextViewErrorPassword.setText(getString(R.string.error_in_password_new));
        TextViewErrorConfermPassword.setText(getString(R.string.error_in_password_confirm));
    }

    private void setNotMatchError() {
        password = 0;
        confirmPasword = 0;
        TextViewErrorPassword.setVisibility(View.INVISIBLE);
        TextViewErrorConfermPassword.setVisibility(View.VISIBLE);
        TextViewErrorConfermPassword.setText(getString(R.string.error_in_password_confirm_2));
    }

    private void setNotMatchErrorWithInstruction() {
        TextViewErrorPassword.setVisibility(View.VISIBLE);
        TextViewErrorConfermPassword.setVisibility(View.VISIBLE);
        TextViewErrorConfermPassword.setText(getString(R.string.error_in_password_confirm_2));
    }

    private void sendPassword() {
        printLog("Started Call");
        showProgressDialog(true);
        if (isConnectedToNetwork(holderActivity)) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put(REQUEST_KEY_AUTH_ID, getArguments().getString(EXTRA_FRAGMENT_THREE_ID));
                jsonObject.put(REQUEST_KEY_AUTH_OTP, getArguments().getString(EXTRA_FRAGMENT_THREE));
                jsonObject.put(REQUEST_KEY_PASSWORD, editTextPassword.getText().toString().trim());
                printLog("Request:" + jsonObject.toString());
                holderActivity.networkRequestUtil.putData(BASE_URL + URL_FORGOT_PASSWORD_UPDATE_PASS, jsonObject, new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        showProgressDialog(false);
                        printLog("Response Of Send code:" + response);
                        NormalResponse normalResponse = new Gson().fromJson(response.toString(), NormalResponse.class);
                        if (normalResponse != null) {
                            if (normalResponse.getStatus().equalsIgnoreCase(STATUS_SUCCESS)) {
                                holderActivity.proceedToStepFour();
                            } else {
                                showDialogWithOkButton(normalResponse.getMessage());
                            }
                        } else {
                            showDialogWithOkButton(getString(R.string.error_someting_went_wrong));
                        }

                    }

                    @Override
                    public void onError(VolleyError error) {
                        printLog("Error:-" + error);
                        showProgressDialog(false);
                    }
                });
            } catch (Exception e) {
                printLog("Error:-" + e);
                showProgressDialog(false);
            }
        } else {
            showProgressDialog(false);
            showNoNetworkMessage();
        }
    }


}
