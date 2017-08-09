package com.harrier.lifecyclehealth.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.harrier.lifecyclehealth.R;
import com.harrier.lifecyclehealth.activities.ForgotPasswordHolderActivity;
import com.harrier.lifecyclehealth.application.MyApplication;
import com.harrier.lifecyclehealth.callbacks.VolleyCallback;
import com.harrier.lifecyclehealth.dto.AuthenticateForgotCodeDTO;
import com.harrier.lifecyclehealth.dto.NormalResponse;

import org.json.JSONObject;

import static com.harrier.lifecyclehealth.utils.AppConstants.BASE_URL;
import static com.harrier.lifecyclehealth.utils.AppConstants.CONST_STEP_TWO;
import static com.harrier.lifecyclehealth.utils.AppConstants.PREF_FORGOT_PASS_CURRENT_STEP;
import static com.harrier.lifecyclehealth.utils.AppConstants.REQUEST_KEY_AUTH_ID;
import static com.harrier.lifecyclehealth.utils.AppConstants.REQUEST_KEY_AUTH_OTP;
import static com.harrier.lifecyclehealth.utils.AppConstants.REQUEST_KEY_EMAIL;
import static com.harrier.lifecyclehealth.utils.AppConstants.STATUS_SUCCESS;
import static com.harrier.lifecyclehealth.utils.AppConstants.URL_FORGOT_PASSWORD_AUTHENTICATE_CODE;
import static com.harrier.lifecyclehealth.utils.AppConstants.URL_FORGOT_PASSWORD_SEND_CODE;

/**
 * Created by satyam on 24/04/2017.
 */

public class ForgotPassword_StepTwo extends BaseFragmentWithOptions {

    private ForgotPasswordHolderActivity holderActivity;
    private TextView userNameTv, resendCodeTv;
    private EditText editTextFirstNumber, editTextSecondNumber, editTextThirdNumber,
            editTextFourthNumber;
    private Button btnCancel, btnContinue;

    public static ForgotPassword_StepTwo newInstance() {
        return new ForgotPassword_StepTwo();
    }

    @Override
    String getFragmentTag() {
        return "ForgotPassword_StepTwo";
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
        return inflater.inflate(R.layout.frag_forgot_pass_step_two, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupToolbarTitle(holderActivity.getToolbar(), getString(R.string.forgot_pass_title_step_two));
        hideKeyboard(view);
        setView(view);
    }

    @Override
    public void onResume() {
        super.onResume();
        MyApplication.getInstance().addIntToSharedPreference(PREF_FORGOT_PASS_CURRENT_STEP, CONST_STEP_TWO);
    }

    private void setView(View view) {
        userNameTv = (TextView) view.findViewById(R.id.userNameTv);
        userNameTv.setText(holderActivity.userId);
        resendCodeTv = (TextView) view.findViewById(R.id.resendCodeTv);
        resendCodeTv.setOnClickListener(onClickListener);

        editTextFirstNumber = (EditText) view.findViewById(R.id.editTextFirstNumber);
        editTextSecondNumber = (EditText) view.findViewById(R.id.editTextSecondNumber);
        editTextThirdNumber = (EditText) view.findViewById(R.id.editTextThirdNumber);
        editTextFourthNumber = (EditText) view.findViewById(R.id.editTextFourthNumber);

        btnCancel = (Button) view.findViewById(R.id.btnCancel);
        btnContinue = (Button) view.findViewById(R.id.btnContinue);

        btnCancel.setOnClickListener(onClickListener);
        btnContinue.setOnClickListener(onClickListener);

        editTextFirstNumber.addTextChangedListener(textWatcher);
        editTextSecondNumber.addTextChangedListener(textWatcher);
        editTextThirdNumber.addTextChangedListener(textWatcher);
        editTextFourthNumber.addTextChangedListener(textWatcher);
    }

    private boolean isValidCode(EditText editText) {
        return (editText.getText().toString().trim().length() > 0);
    }

    private String getCode() {
        return editTextFirstNumber.getText().toString() +
                editTextSecondNumber.getText().toString() +
                editTextThirdNumber.getText().toString() +
                editTextFourthNumber.getText().toString();
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.resendCodeTv:
                    reGenerateCode();
                    break;
                case R.id.btnCancel:
                    holderActivity.handelBackPressed();
                    break;
                case R.id.btnContinue:
                    if (isValidCode(editTextFirstNumber) && isValidCode(editTextSecondNumber) &&
                            isValidCode(editTextThirdNumber) && isValidCode(editTextFourthNumber)) {
                        verifyCode(getCode());
                    } else showDialogWithOkButton(getString(R.string.error_verify_code));
                    break;
            }
        }
    };

    /* for receive code  */
    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            EditText text = (EditText) holderActivity.getCurrentFocus();

            if (text != null && text.length() > 0) {
                View next = text.focusSearch(View.FOCUS_RIGHT); // or FOCUS_FORWARD
                if (next != null)
                    next.requestFocus();

            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    /* verify code Number */
    private void verifyCode(final String verificationCode) {
        showProgressDialog(true);
        if (isConnectedToNetwork(holderActivity)) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put(REQUEST_KEY_AUTH_ID, holderActivity.userId);
                jsonObject.put(REQUEST_KEY_AUTH_OTP, verificationCode);
                holderActivity.networkRequestUtil.putDataSecure(BASE_URL + URL_FORGOT_PASSWORD_AUTHENTICATE_CODE, jsonObject, new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        showProgressDialog(false);
                        printLog("Response:Verify" + response);
                        AuthenticateForgotCodeDTO normalResponse = new Gson().fromJson(response.toString(), AuthenticateForgotCodeDTO.class);
                        if (normalResponse != null) {
                            if (normalResponse.getStatus().equalsIgnoreCase(STATUS_SUCCESS)) {
                                holderActivity.proceedToStepTHREE(normalResponse.getHashOTP(), normalResponse.getId());
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

    /* For reGeneration Of Code*/
    private void reGenerateCode() {
        showProgressDialog(true);
        if (isConnectedToNetwork(holderActivity)) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put(REQUEST_KEY_EMAIL, holderActivity.userId);
                holderActivity.networkRequestUtil.putData(BASE_URL + URL_FORGOT_PASSWORD_SEND_CODE, jsonObject, new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        showProgressDialog(false);
                        printLog("Response Of Send code:" + response);
                        NormalResponse normalResponse = new Gson().fromJson(response.toString(), NormalResponse.class);
                        if (normalResponse != null) {
                            if (normalResponse.getStatus().equalsIgnoreCase(STATUS_SUCCESS)) {

                            } else {
                                showDialogWithOkButton(normalResponse.getMessage());
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
            } catch (Exception e) {
                showProgressDialog(false);
            }
        } else {
            showProgressDialog(false);
            showNoNetworkMessage();
        }
    }
}
