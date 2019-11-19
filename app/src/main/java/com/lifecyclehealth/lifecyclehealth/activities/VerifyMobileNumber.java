package com.lifecyclehealth.lifecyclehealth.activities;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.lifecyclehealth.lifecyclehealth.R;
import com.lifecyclehealth.lifecyclehealth.callbacks.OnOkClick;
import com.lifecyclehealth.lifecyclehealth.callbacks.VolleyCallback;
import com.lifecyclehealth.lifecyclehealth.dto.NormalResponse;
import com.lifecyclehealth.lifecyclehealth.utils.KeyBoardHandler;
import com.lifecyclehealth.lifecyclehealth.utils.NetworkRequestUtil;

import org.json.JSONObject;

import zemin.notification.NotificationDelegater;
import zemin.notification.NotificationLocal;
import zemin.notification.NotificationView;

import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.BASE_URL;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.EXTRA_LOGIN_COUNTRY_CODE;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.EXTRA_LOGIN_MOBILE_NUMBER;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.REQUEST_KEY_VERIFY_COUNTRY;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.REQUEST_KEY_VERIFY_MOB;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.REQUEST_KEY_VERIFY_MOB_VERY;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.STATUS_SUCCESS;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.URL_VALIDATE_MOBILE_NUMBER;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.URL_VERIFY_CODE;

/**
 * Created by satyam on 20/04/2017.
 */

public class VerifyMobileNumber extends BaseActivity {
    private EditText editTextCountryCode, editTextMobileNumber,
            editTextFirstNumber, editTextSecondNumber, editTextThirdNumber,
            editTextFourthNumber;
    private ImageView editMobileNumberBTN;
    private Button buttonSendCode, buttonSendAndUpdate, buttonCancel, buttonVerify;
    private RelativeLayout rootLayout;
    private NetworkRequestUtil networkRequestUtil;
    private KeyBoardHandler keyBoardHandler;
    private NotificationDelegater mDelegater;
    private NotificationLocal mLocal;

    @Override
    String getTag() {
        return "VerifyMobileNumber";
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_mobile);
        networkRequestUtil = new NetworkRequestUtil(VerifyMobileNumber.this);

        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        cancelProcess();
    }

    /* initiate view*/
    private void initView() {
        rootLayout = (RelativeLayout) findViewById(R.id.rootLayout);
        keyBoardHandler = new KeyBoardHandler(VerifyMobileNumber.this, rootLayout);
        editTextCountryCode = (EditText) findViewById(R.id.editTextCountryCode);
        editTextMobileNumber = (EditText) findViewById(R.id.editTextMobileNo);
        editTextFirstNumber = (EditText) findViewById(R.id.editTextFirstNumber);
        editTextSecondNumber = (EditText) findViewById(R.id.editTextSecondNumber);
        editTextThirdNumber = (EditText) findViewById(R.id.editTextThirdNumber);
        editTextFourthNumber = (EditText) findViewById(R.id.editTextFourthNumber);

        editMobileNumberBTN = (ImageView) findViewById(R.id.editMobileNumberBTN);

        buttonSendCode = (Button) findViewById(R.id.btnSendCode);
        buttonSendAndUpdate = (Button) findViewById(R.id.btnUpdateNSend);
        buttonCancel = (Button) findViewById(R.id.btnCancel);
        buttonVerify = (Button) findViewById(R.id.btnVerify);
        /* Listener*/
        editMobileNumberBTN.setOnClickListener(onClickListener);
        buttonCancel.setOnClickListener(onClickListener);
        buttonSendAndUpdate.setOnClickListener(onClickListener);
        buttonVerify.setOnClickListener(onClickListener);
        buttonSendCode.setOnClickListener(onClickListener);
        /*Visibility*/
        buttonSendAndUpdate.setVisibility(View.GONE);
        /* set values*/
        editTextCountryCode.setText(getIntent().getStringExtra(EXTRA_LOGIN_COUNTRY_CODE));
        editTextMobileNumber.setText(getMobileNumber(0, getIntent().getStringExtra(EXTRA_LOGIN_MOBILE_NUMBER)));
        /*Control*/
        editTextCountryCode.setEnabled(false);
        editTextMobileNumber.setEnabled(false);

        mDelegater = NotificationDelegater.getInstance();
        mLocal = mDelegater.local();
        mLocal.setView((NotificationView) findViewById(R.id.nv));

        setupEditTextFunction();


    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.editMobileNumberBTN:
                    editMobileNumber(getIntent().getStringExtra(EXTRA_LOGIN_MOBILE_NUMBER));
                    break;
                case R.id.btnCancel:
                    cancelProcess();
                    break;
                case R.id.btnVerify:
                    if (isValidCode(editTextFirstNumber) && isValidCode(editTextSecondNumber) &&
                            isValidCode(editTextThirdNumber) && isValidCode(editTextFourthNumber)) {
                        verifyCode(getCode());
                    } else showDialogWithOkButton(getString(R.string.error_verify_code));
                    break;
                case R.id.btnUpdateNSend:
                    keyBoardHandler.hideKeyboard();

                    if (!isValidCode(editTextCountryCode) || (editTextCountryCode.length() < 2)) {
                        showDialogWithOkButton(getString(R.string.error_verify_country_code));
                        return;
                    }

                    if (!isValidCode(editTextMobileNumber) || editTextMobileNumber.getText().toString().trim().length() < 12) {
                        showDialogWithOkButton(getString(R.string.error_verify_contact_num));
                        return;
                    }

                    updateAndSendCode();
                    break;
                case R.id.btnSendCode:
                    keyBoardHandler.hideKeyboard();
                    sendCode();
                    buttonSendCode.setText("Resend Code");
                    break;

            }
        }
    };

    private boolean isValidCode(EditText editText) {
        return (editText.getText().toString().trim().length() > 0);
    }

    private String getCode() {
        return editTextFirstNumber.getText().toString() +
                editTextSecondNumber.getText().toString() +
                editTextThirdNumber.getText().toString() +
                editTextFourthNumber.getText().toString();
    }

    private void setupEditTextFunction() {
        /* for mobile number*/
        editTextMobileNumber.addTextChangedListener(new TextWatcher() {
            int len = 0;

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                String str = editTextMobileNumber.getText().toString();
                len = str.length();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String str = editTextMobileNumber.getText().toString();
                if (str.length() == 3 && len < str.length()) {//len check for backspace
                    editTextMobileNumber.append(" ");
                }
                if (str.length() == 7 && len < str.length()) {//len check for backspace
                    editTextMobileNumber.append(" ");
                }
            }
        });
        /*for country code */

        editTextCountryCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                EditText text = (EditText) getCurrentFocus();
                if (text != null && text.length() > 2) {
                    View next = text.focusSearch(View.FOCUS_RIGHT); // or FOCUS_FORWARD
                    if (next != null)
                        next.requestFocus();

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().contains("+")) {
                    editTextCountryCode.setText("+" + editable.toString());
                    Selection.setSelection(editTextCountryCode.getText(), editTextCountryCode.getText().length());
                }
            }
        });
        /* for receive code  */
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                EditText text = (EditText) getCurrentFocus();

               /* if (text != null && text.length() > 0) {
                    View next = text.focusSearch(View.FOCUS_RIGHT); // or FOCUS_FORWARD
                    if (next != null)
                        next.requestFocus();

                }
                if (text != null && text.length() < 0) {
                    View next = text.focusSearch(View.FOCUS_LEFT); // or FOCUS_LEFT
                    if (next != null)
                        next.requestFocus();

                }*/


                if (charSequence.length() == 0) {
                    View back = text.focusSearch(View.FOCUS_LEFT); // or FOCUS_LEFT
                    if (back != null)
                        back.requestFocus();
                }
                if (charSequence.length() == 1) {
                    View next = text.focusSearch(View.FOCUS_RIGHT); // or FOCUS_LEFT
                    if (next != null)
                        next.requestFocus();
                    text.setSelection(1);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
        editTextFirstNumber.addTextChangedListener(textWatcher);
        editTextSecondNumber.addTextChangedListener(textWatcher);
        editTextThirdNumber.addTextChangedListener(textWatcher);
        editTextFourthNumber.addTextChangedListener(textWatcher);
    }

    /* this will call from back button press as well as cancel button*/
    private void cancelProcess() {
        AlertDialog.Builder builder = new AlertDialog.Builder(VerifyMobileNumber.this);
        LayoutInflater layoutInflater = this.getLayoutInflater();
        View dialogView = layoutInflater.inflate(R.layout.layout_dialog_two_btn, null);
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();

        Button buttonCancel = (Button) dialogView.findViewById(R.id.btnCancel);
        Button buttonSignOut = (Button) dialogView.findViewById(R.id.btnSignOut);

        Window window = alertDialog.getWindow();
        if (window != null) {
            WindowManager.LayoutParams wmlp = window.getAttributes();
            wmlp.windowAnimations = R.style.dialog_animation;
            window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            window.setAttributes(wmlp);
        }


        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        buttonSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                Intent intent = new Intent(VerifyMobileNumber.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
        alertDialog.show();
    }

    /* for editing mobileNumber*/
    private void editMobileNumber(String mobleNumber) {
        buttonSendCode.setVisibility(View.GONE);
        buttonSendAndUpdate.setVisibility(View.VISIBLE);
        editTextCountryCode.setEnabled(true);
        editTextMobileNumber.setEnabled(true);
        editTextMobileNumber.requestFocus();
        editTextMobileNumber.setText(getMobileNumber(1, mobleNumber.trim()));
        editTextMobileNumber.setSelection(editTextMobileNumber.getText().length());
    }

    /*
      for getting mobile number hidden format or normal
      1=normal
      0=hidden
      mobile number should be trim
     */
    private String getMobileNumber(int value, String mobileNumber) {
        printLog("Mobile Number" + mobileNumber);
        mobileNumber = mobileNumber.trim();
        printLog("Mobile NumberAfter" + mobileNumber);
        if (value == 1) {
            if (mobileNumber.length() == 10) {
                return mobileNumber.substring(0, 3) + " " + mobileNumber.substring(3, 6) + " " + mobileNumber.substring(6, 10);
            }
            return mobileNumber;
        } else {
            if (mobileNumber.trim().length() >= 12) {
                String suffix = mobileNumber.trim().substring(7, 12);
                return "XXX XXX" + suffix;
            } else return mobileNumber;
        }

    }


    /* Send code Network Operation*/
    private void sendCode() {
        showProgressDialog(true);
        if (isConnectedToNetwork(VerifyMobileNumber.this)) {
            networkRequestUtil.getDataSecure(BASE_URL + URL_VALIDATE_MOBILE_NUMBER, new VolleyCallback() {
                @Override
                public void onSuccess(JSONObject response) {
                    showProgressDialog(false);
                    NormalResponse normalResponse = new Gson().fromJson(response.toString(), NormalResponse.class);
                    if (normalResponse != null) {
                        showDialogWithOkButton(normalResponse.getMessage());
                    } else {
                        showDialogWithOkButton(getString(R.string.error_someting_went_wrong));
                    }

                }

                @Override
                public void onError(VolleyError error) {

                }
            });
        } else {
            showProgressDialog(false);
            showNoNetworkMessage();
        }
    }

    /* update mobile Number and send code*/
    private void updateAndSendCode() {
        showProgressDialog(true);
        if (isConnectedToNetwork(VerifyMobileNumber.this)) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put(REQUEST_KEY_VERIFY_COUNTRY, editTextCountryCode.getText().toString());
                jsonObject.put(REQUEST_KEY_VERIFY_MOB, editTextMobileNumber.getText().toString());
                networkRequestUtil.putDataSecure(BASE_URL + URL_VALIDATE_MOBILE_NUMBER, jsonObject, new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        showProgressDialog(false);
                        printLog("ResponseUpdateAndSend" + response);
                        NormalResponse normalResponse = new Gson().fromJson(response.toString(), NormalResponse.class);
                        if (normalResponse != null) {
                            showDialogWithOkButton(normalResponse.getMessage());
                        } else {
                            showDialogWithOkButton(getString(R.string.error_someting_went_wrong));
                        }

                    }

                    @Override
                    public void onError(VolleyError error) {

                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            showProgressDialog(false);
            showNoNetworkMessage();
        }
    }

    /* verify mobile Number */
    private void verifyCode(String verificationCode) {
        showProgressDialog(true);
        if (isConnectedToNetwork(VerifyMobileNumber.this)) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put(REQUEST_KEY_VERIFY_MOB_VERY, verificationCode);
                networkRequestUtil.putDataSecure(BASE_URL + URL_VERIFY_CODE, jsonObject, new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        showProgressDialog(false);
                        printLog("Response:Verify" + response);
                        NormalResponse normalResponse = new Gson().fromJson(response.toString(), NormalResponse.class);
                        if (normalResponse != null) {
                            if (normalResponse.getStatus().equalsIgnoreCase(STATUS_SUCCESS)) {
                                showDialogWithOkButton(normalResponse.getMessage(), new OnOkClick() {
                                    @Override
                                    public void OnOkClicked() {
                                        Intent intent = new Intent(VerifyMobileNumber.this, MainActivity.class);
                                        intent.putExtra("from_notification", "0");
                                        startActivity(intent);
                                        finish();
                                    }


                                });
                            } else {
                                showDialogWithOkButton(normalResponse.getMessage());
                            }

                        } else {
                            showDialogWithOkButton(getString(R.string.error_someting_went_wrong));
                        }
                    }

                    @Override
                    public void onError(VolleyError error) {

                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            showProgressDialog(false);
            showNoNetworkMessage();
        }
    }
}
