package com.harrier.lifecyclehealth.fragments;

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
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.harrier.lifecyclehealth.R;
import com.harrier.lifecyclehealth.activities.ForgotPasswordHolderActivity;
import com.harrier.lifecyclehealth.application.MyApplication;
import com.harrier.lifecyclehealth.callbacks.OnBackPressedInFragment;
import com.harrier.lifecyclehealth.callbacks.VolleyCallback;
import com.harrier.lifecyclehealth.dto.NormalResponse;

import org.json.JSONObject;

import static com.harrier.lifecyclehealth.utils.AppConstants.BASE_URL;
import static com.harrier.lifecyclehealth.utils.AppConstants.CONST_STEP_ONE;
import static com.harrier.lifecyclehealth.utils.AppConstants.PREF_FORGOT_PASS_CURRENT_STEP;
import static com.harrier.lifecyclehealth.utils.AppConstants.REQUEST_KEY_EMAIL;
import static com.harrier.lifecyclehealth.utils.AppConstants.STATUS_SUCCESS;
import static com.harrier.lifecyclehealth.utils.AppConstants.URL_FORGOT_PASSWORD_SEND_CODE;

/**
 * Created by satyam on 24/04/2017.
 */

public class ForgotPassword_StepOne extends BaseFragmentWithOptions implements OnBackPressedInFragment {
    private ForgotPasswordHolderActivity holderActivity;

    private EditText editTextUserName;
    private Button buttonCancel, buttonSendCode;
    private TextView errorViewUserNameTv;

    private static int FragmentNumberInFlowChain = 1;

    public static final String EXTRA_FRAGMENT_ONE = "step_one";

    public static ForgotPassword_StepOne newInstance() {
        ForgotPassword_StepOne stepOne = new ForgotPassword_StepOne();
        Bundle bundle = new Bundle();
        bundle.putInt(EXTRA_FRAGMENT_ONE, 1);
        stepOne.setArguments(bundle);
        return stepOne;
    }

    @Override
    String getFragmentTag() {
        return "ForgotPassword_StepOne";
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        holderActivity = (ForgotPasswordHolderActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_forgot_pass_step_one, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupView(view);
    }

    @Override
    public void onResume() {
        super.onResume();
        MyApplication.getInstance().addIntToSharedPreference(PREF_FORGOT_PASS_CURRENT_STEP, CONST_STEP_ONE);
    }

    @Override
    public void OnBackPressed(int fragmentNumber) {

    }

    private void setupView(View view) {
        errorViewUserNameTv = (TextView) view.findViewById(R.id.errorViewUserNameTv);
        editTextUserName = (EditText) view.findViewById(R.id.userNameEditText);
        buttonSendCode = (Button) view.findViewById(R.id.btnSendCode);
        buttonCancel = (Button) view.findViewById(R.id.btnCancel);

        buttonCancel.setOnClickListener(onClickListener);
        buttonSendCode.setOnClickListener(onClickListener);
        setupToolbarTitle(holderActivity.getToolbar(), getString(R.string.forgot_pass_title_step_one));
        editTextUserName.setText(holderActivity.userId);
        showError(false);
        editTextUserName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() <= 0) {
                    showError(true);
                } else showError(false);
            }
        });
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            switch (view.getId()) {
                case R.id.btnCancel:
                    holderActivity.finishActivity();
                    break;
                case R.id.btnSendCode:
                    proceedToNext();
                    break;
            }
        }
    };

    private void proceedToNext() {
        printLog("Error " + editTextUserName.getText().toString().trim().length());
        if (editTextUserName != null && editTextUserName.getText().toString().trim().length() <= 0) {
            showError(true);
            printLog("Error Called");
            return;
        } else showError(false);
        holderActivity.userId = editTextUserName.getText().toString().trim();

        sendCode();
    }

    private void showError(boolean show) {
        if (show) {
            errorViewUserNameTv.setVisibility(View.VISIBLE);
        } else errorViewUserNameTv.setVisibility(View.GONE);
    }

    private void sendCode() {
        showProgressDialog(true);
        if (isConnectedToNetwork(holderActivity)) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put(REQUEST_KEY_EMAIL, editTextUserName.getText().toString().trim());
                holderActivity.networkRequestUtil.putData(BASE_URL + URL_FORGOT_PASSWORD_SEND_CODE, jsonObject, new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        showProgressDialog(false);
                        printLog("Response Of Send code:" + response);
                        NormalResponse normalResponse = new Gson().fromJson(response.toString(), NormalResponse.class);
                        if (normalResponse != null) {
                            if (normalResponse.getStatus().equalsIgnoreCase(STATUS_SUCCESS)) {
                                holderActivity.proceedToStepTwo();
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
