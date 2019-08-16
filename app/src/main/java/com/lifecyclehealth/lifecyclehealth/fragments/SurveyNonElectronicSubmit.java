package com.lifecyclehealth.lifecyclehealth.fragments;


import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.lifecyclehealth.lifecyclehealth.R;
import com.lifecyclehealth.lifecyclehealth.activities.MainActivity;
import com.lifecyclehealth.lifecyclehealth.application.MyApplication;
import com.lifecyclehealth.lifecyclehealth.callbacks.VolleyCallback;
import com.lifecyclehealth.lifecyclehealth.fingerprint.FingerPrintCallback;
import com.lifecyclehealth.lifecyclehealth.fingerprint.FingerPrintUtil;
import com.lifecyclehealth.lifecyclehealth.model.ColorCode;
import com.lifecyclehealth.lifecyclehealth.model.SurveyElectronicSubmitResponse;
import com.lifecyclehealth.lifecyclehealth.utils.AESHelper;
import com.lifecyclehealth.lifecyclehealth.utils.AppConstants;
import com.lifecyclehealth.lifecyclehealth.utils.KeyBoardHandler;
import com.lifecyclehealth.lifecyclehealth.utils.PreferenceUtils;
import com.segment.analytics.Analytics;
import com.segment.analytics.Properties;

import org.json.JSONObject;

import java.util.HashMap;

import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.BASE_URL;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.PREF_IS_PATIENT;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.STATUS_SUCCESS;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.URL_SURVEY_SUBMIT_ANSWER_ELECTRONIC_PATIENT;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.URL_SURVEY_SUBMIT_ANSWER_ELECTRONIC_PROVIDER;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.seedValue;

/**
 * A simple {@link Fragment} subclass.
 */
public class SurveyNonElectronicSubmit extends BaseFragmentWithOptions implements View.OnClickListener,
        FingerPrintCallback, FingerPrintUtil.NextActivityCallback {

    MainActivity mainActivity;
    private static final String SURVEY_LIST_EXTRAS_HOLDER = "electronic_password_data";
    private static final String SURVEY_ID = "survey_id";
    private String ResponseId;
    private String surveyId;
    private String RequierESignature;
    //EditText password;
  //  private static final String PASSWORD = "password";
    Button submit;
    private KeyBoardHandler keyBoardHandler;
    private RelativeLayout rootLayout;
    private TextView TextViewErrorPassword;
    private boolean isPatient;
    private FingerPrintUtil fingerPrintUtil;
    private ColorCode colorCode;
    String Stringcode;

    public static SurveyNonElectronicSubmit newInstance(String data, String surveyId) {

        SurveyNonElectronicSubmit holderFragment = new SurveyNonElectronicSubmit();
        Bundle bundle = new Bundle();
        bundle.putString(SURVEY_LIST_EXTRAS_HOLDER, data);
        bundle.putString(SURVEY_ID, surveyId);
        holderFragment.setArguments(bundle);
        return holderFragment;
    }

    @Override
    String getFragmentTag() {
        return "SurveyElectronicSubmit";
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_survey_non_electronic_submit, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ResponseId = getArguments().getString(SURVEY_LIST_EXTRAS_HOLDER);
        surveyId = getArguments().getString(SURVEY_ID);
        isPatient = MyApplication.getInstance().getBooleanFromSharedPreference(PREF_IS_PATIENT);

        initView(view);
    }

    private void initView(View view) {
        // try {
        String resposne = MyApplication.getInstance().getColorCodeJson(AppConstants.SET_COLOR_CODE);
        colorCode = new Gson().fromJson(resposne, ColorCode.class);
        String demo = colorCode.getVisualBrandingPreferences().getColorPreference();
        String Stringcode = "";
        String hashcode = "";

        if (demo == null) {
            hashcode = "Green";
            Stringcode = "259b24";
        } else if (demo != null) {
            String[] arr = colorCode.getVisualBrandingPreferences().getColorPreference().split("#");
            hashcode = arr[0].trim();
            Stringcode = arr[1].trim();
    /*}
           else*/
            if (hashcode.equals("Black") && Stringcode.length() < 6) {
                Stringcode = "333333";
            }
        }
       // }catch (Exception e){e.printStackTrace();}
        Analytics.with(getContext()).screen("Survey Submit");
        rootLayout = (RelativeLayout) view.findViewById(R.id.rootLayout);
        keyBoardHandler = new KeyBoardHandler(mainActivity, rootLayout);
        submit = (Button) view.findViewById(R.id.submit);
        submit.setTextColor(Color.parseColor("#"+Stringcode));
        try {
            ShapeDrawable shapedrawable = new ShapeDrawable();
            shapedrawable.setShape(new RectShape());
            shapedrawable.getPaint().setColor(Color.parseColor("#"+Stringcode));
            shapedrawable.getPaint().setStrokeWidth(10f);
            shapedrawable.getPaint().setStyle(Paint.Style.STROKE);
            submit.setBackground(shapedrawable);
        }catch (Exception e){e.printStackTrace();}

        submit.setOnClickListener(this);
       // password = (EditText) view.findViewById(R.id.passwordEditText);
       // TextViewErrorPassword = (TextView) view.findViewById(R.id.errorViewPasswordTv);
        //showError(PASSWORD, false);

        ImageView imageView = (ImageView) view.findViewById(R.id.backArrowBtn);
        TextView text_non_electronic_sig = (TextView) view.findViewById(R.id.text_electronic_sig);
        imageView.setColorFilter(Color.parseColor("#"+Stringcode));
        text_non_electronic_sig.setTextColor(Color.parseColor("#"+Stringcode));
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backPage();
            }
        });

        TextView back = (TextView) view.findViewById(R.id.back);
        back.setTextColor(Color.parseColor("#"+Stringcode));
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backPage();
            }
        });
        fingerPrintUtil = new FingerPrintUtil(getActivity(), this, this);

        try {
            String touchEmailId = AESHelper.decrypt(seedValue, MyApplication.getInstance().getFromSharedPreference(AppConstants.TOUCH_EMAIL_ID));
            String emailId = AESHelper.decrypt(seedValue, MyApplication.getInstance().getFromSharedPreference(AppConstants.EMAIL_ID));
            if (fingerPrintUtil.isUserAllowToUseFingerPrint()) {
                if (emailId.trim().equals(touchEmailId.trim()) && !touchEmailId.trim().equals("")) {
                    fingerPrintStart();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

   /*     password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView TextView, int i, KeyEvent keyEvent) {

                if ((keyEvent != null && (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (i == EditorInfo.IME_ACTION_NEXT)) {
                    // printLog("Text" + editTextUserName.getText().toString().trim().isEmpty());
                    if (isUserNameTypedByUser()) {
                        keyBoardHandler.hideKeyboard();
                    }
                }
                return false;
            }
        });*/


        /*password.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    try {
                        String touchEmailId = AESHelper.decrypt(seedValue, MyApplication.getInstance().getFromSharedPreference(AppConstants.TOUCH_EMAIL_ID));

                        String emailId = AESHelper.decrypt(seedValue, MyApplication.getInstance().getFromSharedPreference(AppConstants.EMAIL_ID));

                        if (fingerPrintUtil.isUserAllowToUseFingerPrint()) {
                            if (emailId.trim().equals(touchEmailId.trim()) && !touchEmailId.trim().equals("")) {
                                fingerPrintStart();
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return false;
            }
        });*/


    }

    private void fingerPrintStart() {

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
                        defaultOperation();
                    } else if (!fingerPrintUtil.isTouchEnableInService()) {
                        fingerPrintUtil.showConfigurationDialog(getActivity(), getActivity().getFragmentManager(), FingerPrintUtil.FLAG_REGISTER);
                    } else {
                        // onSuccess(isEmailVerified, userBaseKey, response);
                        defaultOperation();
                    }
                }
            } else {
                defaultOperation();
            }
        } else {
            //printLog("User not allow to use fingerprint");
            defaultOperation();
        }
    }


    /*private boolean isUserNameTypedByUser() {
        return !password.getText().toString().trim().isEmpty();
    }*/


   /* private void showError(String filed, boolean show) {
        switch (filed) {

            case PASSWORD:
                if (show) TextViewErrorPassword.setVisibility(View.VISIBLE);
                else TextViewErrorPassword.setVisibility(View.INVISIBLE);
                break;
        }
    }*/

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


    private void backPage() {

        FragmentManager fm = getActivity()
                .getSupportFragmentManager();
        fm.popBackStack("SurveyElectronicSubmit", FragmentManager.POP_BACK_STACK_INCLUSIVE);
        //fm.popBackStack("com.lifecyclehealth.lifecyclehealth.fragments.SurveyFragment", 0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit: {
             /*   if (password.getText().toString().trim().isEmpty()) {
                    showError(PASSWORD, true);
                    return;
                } else {
                    showError(PASSWORD, false);
                }*/
                Analytics.with(getContext()).track("Submit Survey with submit Button", new Properties().putValue("category", "Mobile"));
                submitSurveyWithNonElectronicPassword();
                break;
            }
        }
    }


    private void submitSurveyWithNonElectronicPassword() {
        showProgressDialog(true);
        String url;
        if (isPatient) {
            url = BASE_URL + URL_SURVEY_SUBMIT_ANSWER_ELECTRONIC_PATIENT;
        } else {
            url = BASE_URL + URL_SURVEY_SUBMIT_ANSWER_ELECTRONIC_PROVIDER;
        }
        if (isConnectedToNetwork(mainActivity)) {
            try {
                HashMap<String, Object> params = new HashMap<>();
                params.put("SurveyId", surveyId);
                params.put("ResponseId", ResponseId);
                params.put("Requier_E_Signature", PreferenceUtils.getESignature(getContext()));
                params.put("Password", " ");

                HashMap<String, HashMap<String, Object>> hashMapRequest = new HashMap<>();
                hashMapRequest.put("SurveyDetails", params);
                JSONObject requestJson = new JSONObject(new Gson().toJson(hashMapRequest));

                mainActivity.networkRequestUtil.putDataSecure(url, requestJson, new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        showProgressDialog(false);
                        printLog("Response submit survey:" + response);
                        if (response != null) {
                            final SurveyElectronicSubmitResponse submitResponse = new Gson().fromJson(response.toString(), SurveyElectronicSubmitResponse.class);
                            if (submitResponse.getStatus().equals(STATUS_SUCCESS)) {
                                // mainActivity.SurveySubmittedProgressResult(submitResponse.getSubmittedScore());
                                AlertDialog.Builder builder;
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    builder = new AlertDialog.Builder(mainActivity);
                                } else {
                                    builder = new AlertDialog.Builder(mainActivity);
                                }
                                builder.setMessage("Survey submitted successfully.")
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                // continue with delete
                                                mainActivity.SurveySubmittedProgressResult(submitResponse.getSubmittedScore());
                                            }
                                        });

                                final AlertDialog dialog = builder.create();
                                dialog.show();

                                final Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                                LinearLayout.LayoutParams positiveButtonLL = (LinearLayout.LayoutParams) positiveButton.getLayoutParams();
                                positiveButtonLL.gravity = Gravity.CENTER;
                                dialog.getButton(dialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.black));
                                positiveButton.setLayoutParams(positiveButtonLL);

                            } else showDialogWithOkButton(submitResponse.getMessage());

                        } else
                            showDialogWithOkButton(getString(R.string.error_someting_went_wrong));
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


    @Override
    public void onSuccess(FingerprintManagerCompat.AuthenticationResult result) {
        submitSurveyWithElectronicPasswordByTouchID();
    }


    private void submitSurveyWithElectronicPasswordByTouchID() {
        showProgressDialog(true);
        String url;
        if (isPatient) {
            url = BASE_URL + URL_SURVEY_SUBMIT_ANSWER_ELECTRONIC_PATIENT;
        } else {
            url = BASE_URL + URL_SURVEY_SUBMIT_ANSWER_ELECTRONIC_PROVIDER;
        }
        if (isConnectedToNetwork(mainActivity)) {
            try {
                HashMap<String, String> params = new HashMap<>();
                params.put("SurveyId", surveyId);
                params.put("ResponseId", ResponseId);
                params.put("isTouchID", "true");

                HashMap<String, HashMap<String, String>> hashMapRequest = new HashMap<>();
                hashMapRequest.put("SurveyDetails", params);
                JSONObject requestJson = new JSONObject(new Gson().toJson(hashMapRequest));

                mainActivity.networkRequestUtil.putDataSecure(url, requestJson, new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        showProgressDialog(false);
                        printLog("Response submit survey:" + response);
                        if (response != null) {
                            final SurveyElectronicSubmitResponse submitResponse = new Gson().fromJson(response.toString(), SurveyElectronicSubmitResponse.class);
                            if (submitResponse.getStatus().equals(STATUS_SUCCESS)) {
                                // mainActivity.SurveySubmittedProgressResult(submitResponse.getSubmittedScore());
                                AlertDialog.Builder builder;
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    builder = new AlertDialog.Builder(mainActivity);
                                } else {
                                    builder = new AlertDialog.Builder(mainActivity);
                                }
                                builder.setMessage("Survey submitted successfully.")
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                // continue with delete
                                                mainActivity.SurveySubmittedProgressResult(submitResponse.getSubmittedScore());
                                            }
                                        });

                                final AlertDialog dialog = builder.create();
                                dialog.show();

                                final Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                                LinearLayout.LayoutParams positiveButtonLL = (LinearLayout.LayoutParams) positiveButton.getLayoutParams();
                                positiveButtonLL.gravity = Gravity.CENTER;
                                dialog.getButton(dialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.black));
                                positiveButton.setLayoutParams(positiveButtonLL);

                            } else showDialogWithOkButton(submitResponse.getMessage());

                        } else
                            showDialogWithOkButton(getString(R.string.error_someting_went_wrong));
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

    @Override
    public void onError(String error) {
        if (error != null && error.equalsIgnoreCase(FingerPrintUtil.SYSTEM_MESSAGE)) {
            fingerPrintUtil.showMaxAttemptDialog(getActivity());
        }
    }

    @Override
    public void onMaxAttemptReach() {
        fingerPrintUtil.showMaxAttemptDialog(getActivity());
        fingerPrintUtil.updateFingerPrintLoginEnabled(false);
    }

    @Override
    public void onCancelDialog() {
        defaultOperation();
    }

    private void defaultOperation() {
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
      //  inputMethodManager.showSoftInput(password, InputMethodManager.SHOW_IMPLICIT);
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
}
