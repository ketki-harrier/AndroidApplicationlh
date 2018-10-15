package com.lifecyclehealth.lifecyclehealth.activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v7.widget.AppCompatCheckBox;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.lifecyclehealth.lifecyclehealth.R;
import com.lifecyclehealth.lifecyclehealth.application.MyApplication;
import com.lifecyclehealth.lifecyclehealth.callbacks.VolleyCallback;
import com.lifecyclehealth.lifecyclehealth.db.LifecycleDatabase;
import com.lifecyclehealth.lifecyclehealth.dto.AuthenticateUserDTO;
import com.lifecyclehealth.lifecyclehealth.dto.CheckUser;
import com.lifecyclehealth.lifecyclehealth.fingerprint.FingerPrintCallback;
import com.lifecyclehealth.lifecyclehealth.fingerprint.FingerPrintUtil;
import com.lifecyclehealth.lifecyclehealth.fragments.ChangePassword;
import com.lifecyclehealth.lifecyclehealth.model.AuthenticateUser;
import com.lifecyclehealth.lifecyclehealth.model.AuthenticateUserTouchId;
import com.lifecyclehealth.lifecyclehealth.model.SurveySubmitResponse;
import com.lifecyclehealth.lifecyclehealth.model.User;
import com.lifecyclehealth.lifecyclehealth.services.BackgroundTrackingService;
import com.lifecyclehealth.lifecyclehealth.utils.AESHelper;
import com.lifecyclehealth.lifecyclehealth.utils.AppConstants;
import com.lifecyclehealth.lifecyclehealth.utils.EncryDecry;
import com.lifecyclehealth.lifecyclehealth.utils.KeyBoardHandler;
import com.lifecyclehealth.lifecyclehealth.utils.NetworkRequestUtil;
import com.moxtra.sdk.ChatClient;
import com.moxtra.sdk.common.ApiCallback;
import com.segment.analytics.Analytics;
import com.segment.analytics.Properties;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.crypto.spec.SecretKeySpec;

import zemin.notification.NotificationDelegater;
import zemin.notification.NotificationLocal;
import zemin.notification.NotificationView;

import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.BASE_URL;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.EMAIL_ID;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.EXTRA_FORGOT_PASS_USERNAME;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.EXTRA_LOGIN_COUNTRY_CODE;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.EXTRA_LOGIN_MOBILE_NUMBER;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.IS_LOGOUT;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.IS_TIMEOUT;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.IS_USERNAME_EDITABLE;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.LOCAL_DB_TOUCH;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.LOGIN_ID;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.LOGIN_NAME;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.Moxtra_Access_Token;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.Moxtra_ORG_ID;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.Moxtra_uniqueId;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.STATUS_SUCCESS;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.TOUCH_EMAIL_ID;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.URL_AUTHENTICATE_USER;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.URL_CHANGE_TOUCH_STATE;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.URL_CHECK_IS_USER_TOUCH_ACCEPT;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.URL_CHECK_USERNAME;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.URL_TERM_N_CONDITION;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.USER_ROLE;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.USER_TOKEN;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.seedValue;


/**
 * Created by satyam on 19/04/2017.
 */

public class LoginActivity extends BaseActivityLogin implements
        FingerPrintCallback, FingerPrintUtil.NextActivityCallback {
    /*Views*/

    private EditText editTextUserName, editTextPassword;
    private TextView TextViewErrorUserName, TextViewErrorPassword, TextViewTermPart1, TextViewTermPart2, forgotPasswordTv, forgotUserNameTv, IAcceptTermsAndConditions;
    private LinearLayout linearLayoutTermNCondition;
    private RelativeLayout rootLayout;
    private Button buttonSignIn;
    private ImageView imageChangeUsername;
    private AppCompatCheckBox appCompatCheckBox;
    //public static String loginToken;
    /* Flags*/
    int touchValue = 0;
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private boolean isFirstTimeLogin = false, isIAcceptTermsAndConditions = false;
    /* Util classes*/
    private KeyBoardHandler keyBoardHandler;
    private NetworkRequestUtil networkRequestUtil;
    private boolean touchStatus = true;
    private String touchEmailId = "";
    String emailId = null;
    /*Components*/

    private Switch touchSwitch;
    private TextView switchText;

    /*Database*/
    LifecycleDatabase lifecycleDatabase;

    Context context;
    private final int REQUEST_CODE_ASK_PERMISSIONS = 123;

    private NotificationDelegater mDelegater;
    private NotificationLocal mLocal;

    @Override
    String getTag() {
        return "LoginActivity";
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = LoginActivity.this;
        networkRequestUtil = new NetworkRequestUtil(this);
        checkForPermissions();
        Analytics.with(this).screen("Login");
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (fingerPrintUtil != null) {
            fingerPrintUtil.examineFingerPrintScanner(LoginActivity.this);
            if (fingerPrintUtil.isUserAllowToUseFingerPrint()) {
                setTouchSwitch();
            } else {
                touchSwitch.setVisibility(View.GONE);
            }
        } else {
            touchSwitch.setVisibility(View.GONE);
        }
        //touchSwitch.setVisibility(View.GONE);
        // switchText.setText("Sign in with\nTouchId");
        // switchText.setPaintFlags(switchText.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    /*View initialisation*/
    private void initView() {
        touchValue = 0;
        lifecycleDatabase = new LifecycleDatabase(getApplicationContext());
        rootLayout = (RelativeLayout) findViewById(R.id.rootLayout);
        linearLayoutTermNCondition = (LinearLayout) findViewById(R.id.termAndCondiLayout);

        editTextUserName = (EditText) findViewById(R.id.userNameEditText);
        editTextPassword = (EditText) findViewById(R.id.passwordEditText);

        mDelegater = NotificationDelegater.getInstance();
        mLocal = mDelegater.local();
        mLocal.setView((NotificationView) findViewById(R.id.nv));

        try {
            touchEmailId = AESHelper.decrypt(seedValue, MyApplication.getInstance().getFromSharedPreference(AppConstants.TOUCH_EMAIL_ID));
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (MyApplication.getInstance().getFromSharedPreference(AppConstants.EMAIL_ID) != null) {
            try {
                emailId = AESHelper.decrypt(seedValue, MyApplication.getInstance().getFromSharedPreference(AppConstants.EMAIL_ID));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (emailId != null) {
            if (!emailId.trim().equals("")) {
                editTextUserName.setText(emailId);
                editTextUserName.setSelection(emailId.length());
                checkUserName();
            }
        }

        TextViewErrorUserName = (TextView) findViewById(R.id.errorViewUserNameTv);
        TextViewErrorPassword = (TextView) findViewById(R.id.errorViewPasswordTv);

        IAcceptTermsAndConditions = (TextView) findViewById(R.id.IAcceptTermsAndConditions);
        TextViewTermPart1 = (TextView) findViewById(R.id.iAgreeTextView);
        TextViewTermPart2 = (TextView) findViewById(R.id.termAndCondiTextView);
        buttonSignIn = (Button) findViewById(R.id.signIn);
        forgotPasswordTv = (TextView) findViewById(R.id.forgotPasswordTv);
        forgotUserNameTv = (TextView) findViewById(R.id.forgotUserNameTv);
        imageChangeUsername = (ImageView) findViewById(R.id.imageChangeUsername);
        appCompatCheckBox = (AppCompatCheckBox) findViewById(R.id.termAndCondiCheckbox);
        IAcceptTermsAndConditions.setVisibility(View.GONE);

        touchSwitch = (Switch) findViewById(R.id.simpleSwitch);
        touchSwitch.setVisibility(View.GONE);
        switchText = (TextView) findViewById(R.id.switchText);
        switchText.setVisibility(View.GONE);
        touchSwitch.setChecked(MyApplication.getInstance().getBooleanFromSharedPreference(LOCAL_DB_TOUCH));
        /*Initialise touchId*/
        initFingerPrintElements();
            /* hide*/
        showTermNConditionLayout(false);
        /* keyboard*/
        keyBoardHandler = new KeyBoardHandler(LoginActivity.this, rootLayout);
        /* listeners*/
        forgotPasswordTv.setOnClickListener(onClickListener);
        forgotUserNameTv.setOnClickListener(onClickListener);
        buttonSignIn.setOnClickListener(onClickListener);
        TextViewTermPart1.setOnClickListener(onClickListener);
        TextViewTermPart2.setOnClickListener(onClickListener);
        touchSwitch.setOnClickListener(onClickListener);
        imageChangeUsername.setOnClickListener(onClickListener);
        appCompatCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isIAcceptTermsAndConditions = b;
            }
        });
        switchText.setOnClickListener(null);

        /* error*/
        showError(USERNAME, false);
        showError(PASSWORD, false);

        /* checkUserName*/
        editTextUserName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView TextView, int i, KeyEvent keyEvent) {

                if ((keyEvent != null && (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (i == EditorInfo.IME_ACTION_NEXT)) {
                    // printLog("Text" + editTextUserName.getText().toString().trim().isEmpty());
                    if (isUserNameTypedByUser()) {
                        //keyBoardHandler.hideKeyboard();
                        //checkUserName();
                    }
                }
                return false;
            }
        });

        editTextPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                editTextPassword.setCursorVisible(true);
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    if (isUserNameTypedByUser()) {
                        String username = editTextUserName.getText().toString();
                        try {
                            MyApplication.getInstance().addToSharedPreference(EMAIL_ID, AESHelper.encrypt(seedValue, username));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        editTextPassword.requestFocus();
                        editTextPassword.setSelection(0);
                        editTextPassword.setFocusableInTouchMode(true);
                        //keyBoardHandler.hideKeyboard();
                        checkUserName();
                        //fingerPrintStart();
                    }
                }
                return false;
            }
        });

        touchSwitch.setVisibility(View.GONE);
        switchText.setVisibility(View.GONE);
        imageChangeUsername.setVisibility(View.GONE);

        callMainActivity();
    }

    private void callMainActivity() {

        if (MyApplication.getInstance().getBooleanFromSharedPreference(AppConstants.IS_IN_MEET)) {
            MainActivity instance = MainActivity.getInstance();
            if (instance != null) {
                if (instance.mChatClientDelegate != null) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("from_notification", "1");
                    startActivity(intent);
                    finish();
                }
            }
        }

        if (MyApplication.getInstance().getBooleanFromSharedPreference(IS_LOGOUT) == false){
            if (MyApplication.getInstance().getBooleanFromSharedPreference(IS_TIMEOUT)) {
                MyApplication.getInstance().addBooleanToSharedPreference(AppConstants.IS_TIMEOUT, false);
                MainActivity instance = MainActivity.getInstance();
                if (instance != null) {
                    if (instance.mChatClientDelegate != null) {
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("from_notification", "1");
                        startActivity(intent);
                        finish();
                    }
                }
            }}

    }


    /*Touch Id*/
    private void setTouchSwitch() {
        try {
            if (MyApplication.getInstance().getBooleanFromSharedPreference(IS_USERNAME_EDITABLE) == false) {
                if (touchEmailId.trim().equals("")) {
                    touchSwitch.setVisibility(View.VISIBLE);
                    switchText.setPaintFlags(switchText.getPaintFlags() & (~Paint.UNDERLINE_TEXT_FLAG));
                    switchText.setVisibility(View.VISIBLE);
                    switchText.setText("Set Up \nTouchId ");
                    editTextUserName.setEnabled(true);
                    touchSwitch.setChecked(false);
                    imageChangeUsername.setVisibility(View.GONE);
                } else {
                    if (editTextUserName.getText().toString().trim().equals(touchEmailId.trim()) && !touchEmailId.equals("")) {
                        touchSwitch.setVisibility(View.VISIBLE);
                        switchText.setText("Set Up \nTouchId ");
                        switchText.setPaintFlags(switchText.getPaintFlags() & (~Paint.UNDERLINE_TEXT_FLAG));
                        switchText.setVisibility(View.VISIBLE);
                        imageChangeUsername.setVisibility(View.VISIBLE);
                        if (MyApplication.getInstance().getBooleanFromSharedPreference(IS_USERNAME_EDITABLE)) {
                            editTextUserName.setEnabled(true);
                        } else {
                            editTextUserName.setEnabled(false);
                        }
                        if (MyApplication.getInstance().getBooleanFromSharedPreference(LOCAL_DB_TOUCH)) {
                            touchSwitch.setChecked(true);
                        } else {
                            touchSwitch.setChecked(false);
                        }
                    } else {
                        touchSwitch.setVisibility(View.GONE);
                        switchText.setVisibility(View.GONE);
                        imageChangeUsername.setVisibility(View.GONE);
                        editTextUserName.setEnabled(true);
                        imageChangeUsername.setVisibility(View.GONE);
                    }
                }
            } else {
                touchSwitch.setVisibility(View.GONE);
                switchText.setVisibility(View.GONE);
                imageChangeUsername.setVisibility(View.GONE);
                editTextUserName.setEnabled(true);
                //  editTextUserName.requestFocus();
                editTextUserName.setSelection(0);
                editTextUserName.setText("");
                imageChangeUsername.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*Touch Id*/
    private void setTouchUser() {
        try {
            if (MyApplication.getInstance().getBooleanFromSharedPreference(IS_USERNAME_EDITABLE) == false) {
                if (touchEmailId.trim().equals("")) {
                    touchSwitch.setVisibility(View.VISIBLE);
                    switchText.setPaintFlags(switchText.getPaintFlags() & (~Paint.UNDERLINE_TEXT_FLAG));
                    switchText.setVisibility(View.VISIBLE);
                    switchText.setText("Set Up \nTouchId ");
                    editTextUserName.setEnabled(true);
                    touchSwitch.setChecked(false);
                    imageChangeUsername.setVisibility(View.GONE);
                } else {
                    if (editTextUserName.getText().toString().trim().equals(touchEmailId.trim()) && !touchEmailId.equals("")) {
                        touchSwitch.setVisibility(View.VISIBLE);
                        switchText.setText("Set Up \nTouchId ");
                        switchText.setPaintFlags(switchText.getPaintFlags() & (~Paint.UNDERLINE_TEXT_FLAG));
                        switchText.setVisibility(View.VISIBLE);
                        imageChangeUsername.setVisibility(View.VISIBLE);
                        if (MyApplication.getInstance().getBooleanFromSharedPreference(IS_USERNAME_EDITABLE)) {
                            editTextUserName.setEnabled(true);
                        } else {
                            editTextUserName.setEnabled(false);
                        }
                        if (MyApplication.getInstance().getBooleanFromSharedPreference(LOCAL_DB_TOUCH)) {
                            touchSwitch.setChecked(true);
                        } else {
                            touchSwitch.setChecked(false);
                        }
                    } else {
                        touchSwitch.setVisibility(View.GONE);
                        switchText.setVisibility(View.GONE);
                        imageChangeUsername.setVisibility(View.GONE);
                        editTextUserName.setEnabled(true);
                        imageChangeUsername.setVisibility(View.GONE);
                    }
                }
            } else {
                touchSwitch.setVisibility(View.GONE);
                switchText.setVisibility(View.GONE);
                imageChangeUsername.setVisibility(View.GONE);
                editTextUserName.setEnabled(true);
                // editTextUserName.requestFocus();
                editTextUserName.setSelection(0);
                imageChangeUsername.setVisibility(View.GONE);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    /* this method will tell you that user name is typed or not*/
    private boolean isUserNameTypedByUser() {
        return !editTextUserName.getText().toString().trim().isEmpty();
    }

    /* for Handling click*/
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.signIn:
                    //  keyBoardHandler.hideKeyboard();
                    try {
                        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    } catch (Exception e) {
                        // TODO: handle exception
                    }
                    validateAndProceed();
                    break;
                case R.id.termAndCondiTextView:
                    // keyBoardHandler.hideKeyboard();
                    divertToTermNCondition();
                    break;
                case R.id.iAgreeTextView:
                    // keyBoardHandler.hideKeyboard();
                    divertToTermNCondition();
                    break;
                case R.id.forgotPasswordTv:
                    //keyBoardHandler.hideKeyboard();
                    divertToForgetPassword();
                    break;

                case R.id.forgotUserNameTv:
                    //keyBoardHandler.hideKeyboard();
                    divertToForgetUsername();
                    break;
                case R.id.simpleSwitch: {
                    if (touchSwitch.isChecked()) {
                        touchValue = 1;
                    } else {
                        touchValue = 0;
                        //changeTouchEnable("false");
                    }
                    break;
                }
                case R.id.imageChangeUsername:
                    startActivity(new Intent(LoginActivity.this, ChangeUsername.class));
                    break;
            }
        }
    };

    /* Validation*/
    private void validateAndProceed() {

        if (editTextUserName.getText().toString().trim().isEmpty()) {
            showError(USERNAME, true);
            return;
        } else {
            showError(USERNAME, false);
        }
        if (editTextPassword.getText().toString().trim().isEmpty()) {
            showError(PASSWORD, true);
            return;
        } else {
            showError(PASSWORD, false);
        }
        if (isFirstTimeLogin && !appCompatCheckBox.isChecked()) {
            showDialogWithOkButton(getString(R.string.error_tern_condition));
            return;
        }

       /* if (value == 1) {
            touchSwitch.setChecked(true);
        }
*/
        try {
            MyApplication.getInstance().addToSharedPreference(EMAIL_ID, AESHelper.encrypt(seedValue, editTextUserName.getText().toString()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        authenticateUser();
        //authenticateUserWithTouchID();

    }

    /* check user is new or old or does not exist*/
    private void checkUserName() {
        showProgressDialog(true);
        if (isConnectedToNetwork(this)) {
            networkRequestUtil.getData(BASE_URL + URL_CHECK_USERNAME + editTextUserName.getText().toString().trim(), new VolleyCallback() {
                @Override
                public void onSuccess(JSONObject response) {
                    showProgressDialog(false);
                    printLog("Response:CheckUser" + response);
                    CheckUser checkUser = new Gson().fromJson(response.toString(), CheckUser.class);
                    if (checkUser != null) {
                        if (checkUser.getStatus().equalsIgnoreCase(STATUS_SUCCESS)) {
                            isFirstTimeLogin = checkUser.isFirstTimeLogin();
                            isIAcceptTermsAndConditions = checkUser.isIAcceptTermsAndConditions();
                            if (checkUser.isFirstTimeLogin() == false && checkUser.isIAcceptTermsAndConditions() == false) {
                                //if (checkUser.isIAcceptTermsAndConditions() == false) {
                                IAcceptTermsAndConditions.setVisibility(View.VISIBLE);
                            } else {
                                IAcceptTermsAndConditions.setVisibility(View.GONE);
                            }
                            if (checkUser.isIAcceptTermsAndConditions() == false) {
                                showTermNConditionLayout(true);
                            } else {
                                showTermNConditionLayout(false);
                            }
                            if (fingerPrintUtil.isUserAllowToUseFingerPrint()) {
                                checkIsUserTouchScreen();
                            }
                        } else {
                            keyBoardHandler.hideKeyboard();
                            if (checkUser.isUsernameExist() == false) {
                                if (checkUser.getMessage().contains("Username does not exists.")) {
                                    showDialogWithOkButton("User Not found.");
                                } else {
                                    showDialogWithOkButton(checkUser.getMessage());
                                }
                            } else {
                                showDialogWithOkButton(checkUser.getMessage());
                            }
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

    /*Check touch screen is true or not*/
    private void checkIsUserTouchScreen() {
        if (isConnectedToNetwork(this)) {
            networkRequestUtil.getData(BASE_URL + URL_CHECK_IS_USER_TOUCH_ACCEPT + editTextUserName.getText().toString().trim(), new VolleyCallback() {
                @Override
                public void onSuccess(JSONObject response) {
                    printLog("Response:CheckUser" + response);
                    CheckUser checkUser = new Gson().fromJson(response.toString(), CheckUser.class);
                    if (checkUser != null) {
                        switchText.setVisibility(View.VISIBLE);
                        imageChangeUsername.setVisibility(View.VISIBLE);
                        if (checkUser.getStatus().equalsIgnoreCase(STATUS_SUCCESS)) {
                            if (checkUser.isTouchEnable()) {
                                touchSwitch.setChecked(true);
                                if (!touchEmailId.equals("") && touchEmailId.trim().equals(editTextUserName.getText().toString().trim())) {
                                    MyApplication.getInstance().addBooleanToSharedPreference(AppConstants.LOCAL_DB_TOUCH, true);
                                    MyApplication.getInstance().addBooleanToSharedPreference(AppConstants.SERVER_DB_TOUCH, true);
                                    setTouchSwitch();
                                    if (touchStatus) {
                                        fingerPrintStart();
                                        touchStatus = false;
                                    }

                                } else {
                                    setTouchUser();
                                }
                            } else {
                                setTouchUser();
                                MyApplication.getInstance().addBooleanToSharedPreference(AppConstants.LOCAL_DB_TOUCH, false);
                                MyApplication.getInstance().addBooleanToSharedPreference(AppConstants.SERVER_DB_TOUCH, false);

                                try {
                                    //MyApplication.getInstance().addToSharedPreference(TOUCH_EMAIL_ID, AESHelper.encrypt(seedValue, ""));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
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
    }

    /*authentication user */
    private void authenticateUser() {
        printLog("Timezone" + TimeZone.getDefault().getID());
        showProgressDialog(true);
        if (isConnectedToNetwork(LoginActivity.this)) {
            try {
                AuthenticateUser authenticateUser = new AuthenticateUser();
                authenticateUser.setFirstTimeLogin(isFirstTimeLogin);
                authenticateUser.setRol(USER_ROLE);
                authenticateUser.setTimezone(TimeZone.getDefault().getID());
                authenticateUser.setPassword(editTextPassword.getText().toString().trim());
                authenticateUser.setUsername(editTextUserName.getText().toString().trim());
                authenticateUser.setIAcceptTermsAndConditions(isIAcceptTermsAndConditions);

                final JSONObject requestJson = new JSONObject(new Gson().toJson(authenticateUser));

                //printLog("SendingValue:" + requestJson.toString());
                networkRequestUtil.postData(BASE_URL + URL_AUTHENTICATE_USER, requestJson, new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        showProgressDialog(false);
                        printLog("response:AuthenticateUser" + response);
                        AuthenticateUserDTO userDTO = new Gson().fromJson(response.toString(), AuthenticateUserDTO.class);
                        if (userDTO != null) {
                            if (userDTO.getStatus().equalsIgnoreCase(STATUS_SUCCESS)) {
                                if (userDTO.getUser().isIncomplete_Profile()) {
                                    //showDialogWithOkButton("Your profile is incomplete. Please login to the website for complete it");
                                    Intent intent = new Intent(LoginActivity.this, InCompleteProfileActivity.class);
                                    startActivity(intent);
                                } else if (userDTO.getUser().isPassword_Expired()) {
                                    Intent intent = new Intent(LoginActivity.this, ChangePasswordAutogenerated.class);
                                    intent.putExtra("user", editTextUserName.getText().toString());
                                    //intent.putExtra("pass", editTextPassword.getText().toString());
                                    intent.putExtra("pass", userDTO.getPassword());
                                    startActivity(intent);
                                } else {
                                    proceedToNextStep(userDTO);
                                }

                            } else showDialogWithOkButton(userDTO.getMessage());
                        } else {
                            showDialogWithOkButton(getString(R.string.error_someting_went_wrong));
                        }
                    }

                    @Override
                    public void onError(VolleyError error) {
                        showProgressDialog(false);
                    }
                });

            } catch (Exception e) {
                showProgressDialog(false);
            }

        } else {
            showProgressDialog(false);
            showNoNetworkMessage();
        }
    }


    /*authentication user */
    private void authenticateUserWithTouchID() {
        printLog("Timezone" + TimeZone.getDefault().getID());
        showProgressDialog(true);
        if (isConnectedToNetwork(LoginActivity.this)) {
            try {
                AuthenticateUserTouchId authenticateUser = new AuthenticateUserTouchId();
                authenticateUser.setFirstTimeLogin(isFirstTimeLogin);
                authenticateUser.setTouchID(true);
                authenticateUser.setRole(USER_ROLE);
                authenticateUser.setTimezone(TimeZone.getDefault().getID());
                authenticateUser.setUsername(editTextUserName.getText().toString().trim());

                final JSONObject requestJson = new JSONObject(new Gson().toJson(authenticateUser));

                //printLog("SendingValue:" + requestJson.toString());
                networkRequestUtil.postData(BASE_URL + URL_AUTHENTICATE_USER, requestJson, new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        showProgressDialog(false);
                        printLog("response:AuthenticateUser" + response);
                        AuthenticateUserDTO userDTO = new Gson().fromJson(response.toString(), AuthenticateUserDTO.class);
                        if (userDTO != null) {
                            if (userDTO.getStatus().equalsIgnoreCase(STATUS_SUCCESS)) {
                                if (userDTO.getUser().isIncomplete_Profile()) {
                                    //showDialogWithOkButton("Your profile is incomplete. Please login to the website for complete it");
                                    Intent intent = new Intent(LoginActivity.this, InCompleteProfileActivity.class);
                                    startActivity(intent);
                                } else {
                                    proceedToNextStep(userDTO);
                                }
                            } else showDialogWithOkButton(userDTO.getMessage());
                        } else {
                            showDialogWithOkButton(getString(R.string.error_someting_went_wrong));
                        }
                    }

                    @Override
                    public void onError(VolleyError error) {
                        showProgressDialog(false);
                    }
                });

            } catch (Exception e) {
                showProgressDialog(false);
            }

        } else {
            showProgressDialog(false);
            showNoNetworkMessage();
        }
    }

    private void proceedToNextStep(AuthenticateUserDTO user) {

     /*   try {
            ChatClient.unlink(new ApiCallback<Void>() {
                @Override
                public void onCompleted(Void result) {
                    printLog("Unlink Moxtra account successfully.");
                }

                @Override
                public void onError(int errorCode, String errorMsg) {
                    printLog("Failed to unlink Moxtra account, errorCode=" + errorCode + ", errorMsg=" + errorMsg);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
*/

        try {
            MyApplication.getInstance().updateCurrentUser(user);
            //loginToken = user.getToken();
            lifecycleDatabase.deleteData();
            Log.e("Token", user.getToken());
            lifecycleDatabase.addData(user.getToken());
            MyApplication.getInstance().addToSharedPreference(USER_TOKEN, user.getToken());
            String LOGIN_ID_ENCRY = AESHelper.encrypt(seedValue, user.getUser().getPatientId());
            String LOGIN_NAME_ENCRY = AESHelper.encrypt(seedValue, user.getUser().getName());

            MyApplication.getInstance().addToSharedPreference(LOGIN_ID, LOGIN_ID_ENCRY);
            MyApplication.getInstance().addToSharedPreference(LOGIN_NAME, LOGIN_NAME_ENCRY);
            MyApplication.getInstance().addToSharedPreference(Moxtra_ORG_ID, user.getUser().getMoxtraOrgid());
            MyApplication.getInstance().addToSharedPreference(Moxtra_uniqueId, user.getUser().getMoxtraUniqueID());
            MyApplication.getInstance().addToSharedPreference(Moxtra_Access_Token, user.getUser().getMoxtraAccessToken());

            if (user.getUser().getRole().size() > 0) {
                for (int i = 0; i < user.getUser().getRole().size(); i++) {
                    if (user.getUser().getRole().get(i).equals("Caregiver")) {
                        MyApplication.getInstance().addBooleanToSharedPreference(AppConstants.Is_Care_Giver, true);
                        MyApplication.getInstance().addBooleanToSharedPreference(AppConstants.PREF_IS_PATIENT, false);
                    }
                    if (user.getUser().getRole().get(i).equals("Patient")) {
                        MyApplication.getInstance().addBooleanToSharedPreference(AppConstants.PREF_IS_PATIENT, true);
                        MyApplication.getInstance().addBooleanToSharedPreference(AppConstants.Is_Care_Giver, false);
                    }
                }
            }

        } catch (Exception e) {

        }
        User userData = user.getUser();
        printLog("User:" + user);
        if (userData.isPasswordAutogenerated()) {
            Intent intent = new Intent(LoginActivity.this, ChangePasswordAutogenerated.class);
            intent.putExtra("user", editTextUserName.getText().toString());
            //intent.putExtra("pass", editTextPassword.getText().toString());
            intent.putExtra("pass", user.getPassword());
            startActivity(intent);
        } else if (userData.isMobilePhoneVerify()) {
            divertToMobileVerification(userData.getCountryCode(), userData.getMobileNo());
        } else if (touchValue == 1) {
            startActivity(new Intent(LoginActivity.this, TouchIDActivity.class));
            finish();
        } else {
            boolean isCareGiver = false, isUserPatient = false;
            if (user.getUser().getRole().size() > 0) {
                for (int i = 0; i < user.getUser().getRole().size(); i++) {
                    if (user.getUser().getRole().get(i).equals("Caregiver")) {
                        isCareGiver = true;
                        MyApplication.getInstance().addBooleanToSharedPreference(AppConstants.Is_Care_Giver, true);
                        MyApplication.getInstance().addBooleanToSharedPreference(AppConstants.PREF_IS_PATIENT, false);
                    }
                    if (user.getUser().getRole().get(i).equals("Patient")) {
                        isUserPatient = true;
                        MyApplication.getInstance().addBooleanToSharedPreference(AppConstants.PREF_IS_PATIENT, true);
                        MyApplication.getInstance().addBooleanToSharedPreference(AppConstants.Is_Care_Giver, false);
                    }
                }
            }
            if (isCareGiver) {
                divertToCareGiverScreen();
            } else {
                divertToHomeScreen();
            }
        }
    }


    /* For diverting to homeScreen*/
    private void divertToHomeScreen() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.putExtra("from_notification", "2");
        startActivity(intent);
        //startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }

    private void divertToCareGiverScreen() {
        startActivity(new Intent(LoginActivity.this, CareGiverActivity.class));
        finish();
    }

    /* divert term and condition*/
    private void divertToTermNCondition() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(URL_TERM_N_CONDITION));
        startActivity(intent);
    }

    /* divertTo forgot password*/
    private void divertToForgetUsername() {
        Intent intent = new Intent(LoginActivity.this, ForgotUsernameHolderActivity.class);
        intent.putExtra(EXTRA_FORGOT_PASS_USERNAME, editTextUserName.getText().toString());
        startActivity(intent);
        //overridePendingTransition(R.anim.anim_slide_down, R.anim.anim_slide_up);
    }

    /* divertTo forgot password*/
    private void divertToForgetPassword() {
        Intent intent = new Intent(LoginActivity.this, ForgotPasswordHolderActivity.class);
        intent.putExtra(EXTRA_FORGOT_PASS_USERNAME, editTextUserName.getText().toString());
        startActivity(intent);
        //overridePendingTransition(R.anim.anim_slide_down, R.anim.anim_slide_up);
    }

    /* Verify mobile Number*/
    private void divertToMobileVerification(String countyCode, String mobileNumber) {
        startActivity(new Intent(LoginActivity.this, VerifyMobileNumber.class)
                .putExtra(EXTRA_LOGIN_COUNTRY_CODE, countyCode)
                .putExtra(EXTRA_LOGIN_MOBILE_NUMBER, mobileNumber));
        finish();
    }

    /* For showing and hiding error filed*/
    private void showError(String filed, boolean show) {
        switch (filed) {
            case USERNAME:
                if (show) TextViewErrorUserName.setVisibility(View.VISIBLE);
                else TextViewErrorUserName.setVisibility(View.INVISIBLE);
                break;
            case PASSWORD:
                if (show) TextViewErrorPassword.setVisibility(View.VISIBLE);
                else TextViewErrorPassword.setVisibility(View.INVISIBLE);
                break;
        }
    }

    /*For show/hide term and condition*/
    private void showTermNConditionLayout(boolean flag) {
        if (flag) linearLayoutTermNCondition.setVisibility(View.VISIBLE);
        else linearLayoutTermNCondition.setVisibility(View.INVISIBLE);
    }


    // Runtime permissions handling starts
    private void checkForPermissions() {
        List<String> permissionsNeeded = new ArrayList<String>();

        final List<String> permissionsList = new ArrayList<String>();
        if (!addPermission(permissionsList, Manifest.permission.READ_PHONE_STATE))
            permissionsNeeded.add("Read Phone State");
        if (!addPermission(permissionsList, Manifest.permission.RECORD_AUDIO))
            permissionsNeeded.add("Record Audio");
      /*   if (!addPermission(permissionsList, Manifest.permission.READ_CONTACTS))
            permissionsNeeded.add("Read Contacts");*/
        if (!addPermission(permissionsList, Manifest.permission.CAMERA))
            permissionsNeeded.add("CAMERA");
        if (!addPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE))
            permissionsNeeded.add("Write External Storage");
        if (!addPermission(permissionsList, Manifest.permission.READ_EXTERNAL_STORAGE))
            permissionsNeeded.add("Read External Storage");
        /*if (!addPermission(permissionsList, Settings.ACTION_MANAGE_OVERLAY_PERMISSION))
            permissionsNeeded.add("Read External Storage");*/

        if (permissionsList.size() > 0) {
            if (permissionsNeeded.size() > 0) {
                // Need Rationale
                String message = "You need to grant access to " + permissionsNeeded.get(0);
                for (int i = 1; i < permissionsNeeded.size(); i++) {
                    message = message + ", " + permissionsNeeded.get(i);
                }
                showMessageOKCancel(message,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(LoginActivity.this, permissionsList.toArray(new String[permissionsList.size()]), REQUEST_CODE_ASK_PERMISSIONS);
                            }
                        });
                return;
            }
            ActivityCompat.requestPermissions(this, permissionsList.toArray(new String[permissionsList.size()]), REQUEST_CODE_ASK_PERMISSIONS);
            return;
        }
        startProcessAfterGettingPermissions();
    }

    private void startProcessAfterGettingPermissions() {
        // initView();
        //Toast.makeText(getApplicationContext(),"Permission successful",Toast.LENGTH_SHORT).show();

    }

    private boolean addPermission(List<String> permissionsList, String permission) {
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
            // Check for Rationale Option
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, permission))
                return false;
        }
        return true;
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .create()
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                Map<String, Integer> perms = new HashMap<String, Integer>();
                // Initial
                perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.RECORD_AUDIO, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.READ_PHONE_STATE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                // Fill with results
                for (int i = 0; i < permissions.length; i++) {
                    perms.put(permissions[i], grantResults[i]);
                }
                // Check for ACCESS_FINE_LOCATION
                if (perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
//                    contactListAutoComplete();
                    startProcessAfterGettingPermissions();
                } else {
                    // Permission Denied
                    Toast.makeText(this, "Some Permission is Denied", Toast.LENGTH_SHORT).show();
                    LoginActivity.this.finish();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
    // Runtime permissions handling ends


    private FingerPrintUtil fingerPrintUtil;

    private void fingerPrintStart() {
        //fingerPrintUtil.updateUserMobileNumber(editTextUserName.getText().toString());
        fingerPrintLogic();
    }

    private void onSuccess() {
        // Toast.makeText(getApplicationContext(), "User not allow to use fingerprint", Toast.LENGTH_SHORT).show();
        //checkUserName();
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(editTextPassword, InputMethodManager.SHOW_IMPLICIT);
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
                    fingerPrintUtil.showConfigurationDialog(LoginActivity.this, getFragmentManager(), FingerPrintUtil.FLAG_REGISTER);
                } else {
                    /* this will only called if fingerprint login configure but skip using that and do traditional login*/
                    if (fingerPrintUtil.isUserWantTraditionalLogin()) {
                        // reset
                        fingerPrintUtil.updateUserWantTraditionalLogin(false);
                        defaultOperation();
                    } else if (!fingerPrintUtil.isTouchEnableInService()) {
                        fingerPrintUtil.showConfigurationDialog(LoginActivity.this, getFragmentManager(), FingerPrintUtil.FLAG_REGISTER);
                    } else {
                        // onSuccess(isEmailVerified, userBaseKey, response);
                        defaultOperation();
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


    @Override
    public void onSuccess(FingerprintManagerCompat.AuthenticationResult result) {
        //Toast.makeText(getApplicationContext(), "Successfully scan touch", Toast.LENGTH_SHORT).show();
        authenticateUserWithTouchID();
    }

    @Override
    public void onError(String error) {
        if (error != null && error.equalsIgnoreCase(FingerPrintUtil.SYSTEM_MESSAGE)) {
            fingerPrintUtil.showMaxAttemptDialog(LoginActivity.this);
        }
    }

    @Override
    public void onMaxAttemptReach() {
        fingerPrintUtil.showMaxAttemptDialog(LoginActivity.this);
        fingerPrintUtil.updateFingerPrintLoginEnabled(false);
    }

    @Override
    public void onCancelDialog() {
        showTextOnCancelTouchDialog();
        defaultOperation();
    }

    private void showTextOnCancelTouchDialog() {

        touchStatus = false;
        touchSwitch.setVisibility(View.GONE);
        switchText.setText("Sign in with\nTouchId");
        switchText.setPaintFlags(switchText.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        switchText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                touchStatus = true;
                fingerPrintStart();
            }
        });

    }

    /* init util object*/
    private void initFingerPrintElements() {
        fingerPrintUtil = new FingerPrintUtil(LoginActivity.this, this, this);
        if (fingerPrintUtil.isUserAllowToUseFingerPrint()) {

            setTouchSwitch();
        }
    }


    @Override
    public void onUserResponse(int flag) {
        switch (flag) {
            case FingerPrintUtil.CLICK_EVENT_OK:
                fingerPrintUtil.updateFingerPrintLoginEnabled(false);
                defaultOperation();
                break;
            case FingerPrintUtil.CLICK_EVENT_DONT_ASK:
                fingerPrintUtil.updateFingerPrintLoginEnabled(false);
                break;
            case FingerPrintUtil.CLICK_EVENT_NO:
                fingerPrintUtil.updateFingerPrintLoginEnabled(false);
                defaultOperation();
                break;
        }
    }

    /* default way of redirecting user to next screen*/
    private void defaultOperation() {
        onSuccess();
    }


    /* show succesfull config message*/
    public void showRegisterDialog(String message) {
        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(getApplicationContext());
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setPositiveButton(getApplicationContext().getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int arg1) {
                dialog.dismiss();
                fingerPrintUtil.updateFingerPrintLoginEnabled(true);
                defaultOperation();
            }
        });
        alertDialogBuilder.show();
    }


    @Override
    protected void onStop() {
        super.onStop();
        printLog("on stop");
        // startService(new Intent(this, BackgroundTrackingService.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        printLog("on destroy");

    }
}
