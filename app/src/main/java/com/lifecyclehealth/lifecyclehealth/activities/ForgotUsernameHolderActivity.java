package com.lifecyclehealth.lifecyclehealth.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.lifecyclehealth.lifecyclehealth.R;
import com.lifecyclehealth.lifecyclehealth.application.MyApplication;
import com.lifecyclehealth.lifecyclehealth.callbacks.VolleyCallback;
import com.lifecyclehealth.lifecyclehealth.model.SurveySubmitResponse;
import com.lifecyclehealth.lifecyclehealth.utils.KeyBoardHandler;
import com.lifecyclehealth.lifecyclehealth.utils.NetworkRequestUtil;
import com.segment.analytics.Analytics;
import com.segment.analytics.Properties;

import org.json.JSONObject;

import zemin.notification.NotificationDelegater;
import zemin.notification.NotificationLocal;
import zemin.notification.NotificationView;

import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.BASE_URL;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.REQUEST_KEY_EMAIL;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.REQUEST_KEY_USERNAME;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.STATUS_SUCCESS;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.URL_CHECK_USERNAME;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.URL_FORGOT_USERNAME;

public class ForgotUsernameHolderActivity extends BaseActivity {
    private Toolbar toolbar;
    public NetworkRequestUtil networkRequestUtil;
    public String userId;
    private KeyBoardHandler keyBoardHandler;
    private RelativeLayout rootLayout;
    private NotificationDelegater mDelegater;
    private NotificationLocal mLocal;
    private EditText editTextUserName;
    private Button buttonCancel, buttonSendCode, btnSignIn;
    private TextView errorViewUserNameTv;
    LinearLayout linear_signin, linear_username;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z0-9]+\\.+[a-z]+";
    //String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    String getTag() {
        return "ForgotPasswordHolderActivity";
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_username_holder);
        networkRequestUtil = new NetworkRequestUtil(this);
        mDelegater = NotificationDelegater.getInstance();
        mLocal = mDelegater.local();
        mLocal.setView((NotificationView) findViewById(R.id.nv));
        initialization();

    }

    private void initialization() {
        errorViewUserNameTv = (TextView) findViewById(R.id.errorViewUserNameTv);
        editTextUserName = (EditText) findViewById(R.id.userNameEditText);
        buttonSendCode = (Button) findViewById(R.id.btnSendCode);
        btnSignIn = (Button) findViewById(R.id.btnSignIn);
        buttonCancel = (Button) findViewById(R.id.btnCancel);
        rootLayout = (RelativeLayout) findViewById(R.id.rootLayout);
        keyBoardHandler = new KeyBoardHandler(this, rootLayout);
        buttonCancel.setOnClickListener(onClickListener);
        buttonSendCode.setOnClickListener(onClickListener);
        btnSignIn.setOnClickListener(onClickListener);
        linear_signin = (LinearLayout) findViewById(R.id.linear_signin);

        linear_username = (LinearLayout) findViewById(R.id.linear_username);
        linear_signin.setVisibility(View.GONE);
        showError(false);
        final String email = editTextUserName.getText().toString().trim();
        editTextUserName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() <= 0 && !email.matches(emailPattern)) {
                    showError(true);
                } else showError(false);
            }
        });
    }


    private void showError(boolean show) {
        if (show) {
            errorViewUserNameTv.setVisibility(View.VISIBLE);
        } else errorViewUserNameTv.setVisibility(View.GONE);
    }


    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            switch (view.getId()) {
                case R.id.btnCancel:
                    finish();
                    break;
                case R.id.btnSendCode: {
                    keyBoardHandler.hideKeyboard(true);
                    if (editTextUserName.getText().toString().trim().equals("")) {
                        showError(true);
                        return;
                    } else if (!editTextUserName.getText().toString().matches(emailPattern)) {
                        showError(true);
                        return;
                    }
                    checkUserName();
                    break;
                }
                case R.id.btnSignIn: {
                    keyBoardHandler.hideKeyboard(true);
                    Intent intent = new Intent(ForgotUsernameHolderActivity.this, LoginActivity.class);
                    startActivity(intent);
                    break;
                }
            }
        }
    };


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void checkUserName() {
        showProgressDialog(true);
        try {
            if (isConnectedToNetwork(this)) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put(REQUEST_KEY_USERNAME, editTextUserName.getText().toString().trim());

                networkRequestUtil.putData(BASE_URL + URL_FORGOT_USERNAME, jsonObject, new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        showProgressDialog(false);
                        printLog("Response:ForgotUserName" + response);
                        SurveySubmitResponse checkUser = new Gson().fromJson(response.toString(), SurveySubmitResponse.class);
                        if (checkUser != null) {
                            if (checkUser.getStatus().equalsIgnoreCase(STATUS_SUCCESS)) {
                                //linear_username.setVisibility(View.GONE);
                                //linear_signin.setVisibility(View.VISIBLE);
                                showDialogWithOkButtonSuccess(checkUser.getMessage());
                            } else {
                                showDialogWithOkButton(checkUser.getMessage());
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showDialogWithOkButtonSuccess(String message) {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        LayoutInflater layoutInflater = this.getLayoutInflater();
        View dialogView = layoutInflater.inflate(R.layout.layout_dialog_ok, null);
        builder.setView(dialogView);
        final android.support.v7.app.AlertDialog alertDialog = builder.create();
        TextView TextViewMessage = (TextView) dialogView.findViewById(R.id.messageTv);
        TextViewMessage.setText(message);
        Button buttonOk = (Button) dialogView.findViewById(R.id.ok_btn);
        buttonOk.setText(getString(R.string.ok));
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                keyBoardHandler.hideKeyboard(true);
                //Intent intent = new Intent(ForgotUsernameHolderActivity.this, LoginActivity.class);
                //startActivity(intent);
                finish();
            }
        });
        alertDialog.setCancelable(false);
        Window window = alertDialog.getWindow();
        if (window != null) {
            WindowManager.LayoutParams wmlp = window.getAttributes();
            wmlp.windowAnimations = R.style.dialog_animation;
            window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            window.setAttributes(wmlp);
        }
        alertDialog.show();
    }

}

























