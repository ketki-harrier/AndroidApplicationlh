package com.harrier.lifecyclehealth.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.harrier.lifecyclehealth.R;
import com.harrier.lifecyclehealth.application.MyApplication;
import com.harrier.lifecyclehealth.callbacks.VolleyCallback;
import com.harrier.lifecyclehealth.dto.AuthenticateUserDTO;
import com.harrier.lifecyclehealth.dto.CheckUser;
import com.harrier.lifecyclehealth.model.AuthenticateUser;
import com.harrier.lifecyclehealth.model.User;
import com.harrier.lifecyclehealth.utils.KeyBoardHandler;
import com.harrier.lifecyclehealth.utils.NetworkRequestUtil;

import org.json.JSONObject;

import java.util.TimeZone;

import static com.harrier.lifecyclehealth.utils.AppConstants.BASE_URL;
import static com.harrier.lifecyclehealth.utils.AppConstants.EXTRA_FORGOT_PASS_USERNAME;
import static com.harrier.lifecyclehealth.utils.AppConstants.EXTRA_LOGIN_COUNTRY_CODE;
import static com.harrier.lifecyclehealth.utils.AppConstants.EXTRA_LOGIN_MOBILE_NUMBER;
import static com.harrier.lifecyclehealth.utils.AppConstants.STATUS_SUCCESS;
import static com.harrier.lifecyclehealth.utils.AppConstants.URL_AUTHENTICATE_USER;
import static com.harrier.lifecyclehealth.utils.AppConstants.URL_CHECK_USERNAME;
import static com.harrier.lifecyclehealth.utils.AppConstants.URL_TERM_N_CONDITION;
import static com.harrier.lifecyclehealth.utils.AppConstants.USER_ROLE;
import static com.harrier.lifecyclehealth.utils.AppConstants.USER_TOKEN;

/**
 * Created by satyam on 19/04/2017.
 */

public class LoginActivity extends BaseActivity {
    /*Views*/
    private EditText editTextUserName, editTextPassword;
    private TextView textViewErrorUserName, textViewErrorPassword, textViewTermPart1, textViewTermPart2, forgotPasswordTv;
    private LinearLayout linearLayoutTermNCondition;
    private RelativeLayout rootLayout;
    private Button buttonSignIn;
    private AppCompatCheckBox appCompatCheckBox;
    /* Flags*/
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private boolean isFirstTimeLogin = false;
    /* Util classes*/
    private KeyBoardHandler keyBoardHandler;
    private NetworkRequestUtil networkRequestUtil;
    /*Components*/


    @Override
    String getTag() {
        return "LoginActivity";
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        networkRequestUtil = new NetworkRequestUtil(this);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    /*View initialisation*/
    private void initView() {
        rootLayout = (RelativeLayout) findViewById(R.id.rootLayout);
        linearLayoutTermNCondition = (LinearLayout) findViewById(R.id.termAndCondiLayout);

        editTextUserName = (EditText) findViewById(R.id.userNameEditText);
        editTextPassword = (EditText) findViewById(R.id.passwordEditText);

        textViewErrorUserName = (TextView) findViewById(R.id.errorViewUserNameTv);
        textViewErrorPassword = (TextView) findViewById(R.id.errorViewPasswordTv);

        textViewTermPart1 = (TextView) findViewById(R.id.iAgreeTextView);
        textViewTermPart2 = (TextView) findViewById(R.id.termAndCondiTextView);
        buttonSignIn = (Button) findViewById(R.id.signIn);
        forgotPasswordTv = (TextView) findViewById(R.id.forgotPasswordTv);
        appCompatCheckBox = (AppCompatCheckBox) findViewById(R.id.termAndCondiCheckbox);


            /* hide*/
        showTermNConditionLayout(false);
        /* keyboard*/
        keyBoardHandler = new KeyBoardHandler(LoginActivity.this, rootLayout);
        /* listeners*/
        forgotPasswordTv.setOnClickListener(onClickListener);
        buttonSignIn.setOnClickListener(onClickListener);
        textViewTermPart1.setOnClickListener(onClickListener);
        textViewTermPart2.setOnClickListener(onClickListener);
        appCompatCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

            }
        });

        /* error*/
        showError(USERNAME, false);
        showError(PASSWORD, false);

        /* checkUserName*/
        editTextUserName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {

                if ((keyEvent != null && (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (i == EditorInfo.IME_ACTION_NEXT)) {
                    // printLog("Text" + editTextUserName.getText().toString().trim().isEmpty());
                    if (isUserNameTypedByUser()) {
                        keyBoardHandler.hideKeyboard();
                        checkUserName();
                    }
                }
                return false;
            }
        });

        editTextPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    if (isUserNameTypedByUser()) {
                        keyBoardHandler.hideKeyboard();
                        checkUserName();
                    }
                }
                return false;
            }
        });

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
                    validateAndProceed();
                    break;
                case R.id.termAndCondiTextView:
                    divertToTermNCondition();
                    break;
                case R.id.iAgreeTextView:
                    divertToTermNCondition();
                    break;
                case R.id.forgotPasswordTv:
                    divertToForgetPassword();
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
        authenticateUser();

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
                            showTermNConditionLayout(isFirstTimeLogin);
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
                                proceedToNextStep(userDTO);
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
        MyApplication.getInstance().updateCurrentUser(user);
        MyApplication.getInstance().addToSharedPreference(USER_TOKEN, user.getToken());
        User userData = user.getUser();
        printLog("User:" + user);
        if (userData.isMobilePhoneVerify()) {
            divertToMobileVerification(userData.getCountryCode(), userData.getMobileNo());
        } else {
            divertToHomeScreen();
        }
    }

    /* For diverting to homeScreen*/
    private void divertToHomeScreen() {
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }

    /* divert term and condition*/
    private void divertToTermNCondition() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(URL_TERM_N_CONDITION));
        startActivity(intent);
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
                if (show) textViewErrorUserName.setVisibility(View.VISIBLE);
                else textViewErrorUserName.setVisibility(View.INVISIBLE);
                break;
            case PASSWORD:
                if (show) textViewErrorPassword.setVisibility(View.VISIBLE);
                else textViewErrorPassword.setVisibility(View.INVISIBLE);
                break;
        }
    }

    /*For show/hide term and condition*/
    private void showTermNConditionLayout(boolean flag) {
        if (flag) linearLayoutTermNCondition.setVisibility(View.VISIBLE);
        else linearLayoutTermNCondition.setVisibility(View.INVISIBLE);
    }


}
