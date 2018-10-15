package com.lifecyclehealth.lifecyclehealth.dialog;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.IdRes;
import android.support.v7.widget.AppCompatRadioButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.lifecyclehealth.lifecyclehealth.R;
import com.lifecyclehealth.lifecyclehealth.application.MyApplication;
import com.lifecyclehealth.lifecyclehealth.callbacks.OnOkClick;
import com.lifecyclehealth.lifecyclehealth.callbacks.VolleyCallback;
import com.lifecyclehealth.lifecyclehealth.dto.NormalResponse;

import org.json.JSONObject;

import java.util.ArrayList;

import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.BASE_URL;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.PREF_IS_PATIENT;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.REQUEST_KEY_EMAIL;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.STATUS_SUCCESS;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.URL_FORGOT_PASSWORD_SEND_CODE;

/**
 * Created by vaibhavi on 16-10-2017.
 */

public class CreateConversation {

    Activity activity;
    private Button btnInvite, btnCancel;
    private EditText nameOfConversation;
    private Spinner spinner_no_episode_PatientName, spinner_episode_name, spinnerPatientName;
    private TextView txt_patient_name, txt_episode_name, txt_no_episode_patient_name;
    private AppCompatRadioButton patient_related_episode, provider_patient_with_no_episode;
    private LinearLayout linear_provider_patient_with_no_episode, linear_patient_related_episode;
    private RadioGroup radioGroup;

    private boolean isPatient;


    public void showDialogCreateConversation(Activity activity) {
        this.activity = activity;
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(activity);
        LayoutInflater layoutInflater = activity.getLayoutInflater();
        View dialogView = layoutInflater.inflate(R.layout.meet_dialog_create_conversation, null);
        builder.setView(dialogView);
        final android.support.v7.app.AlertDialog alertDialog = builder.create();

        btnInvite = (Button) dialogView.findViewById(R.id.btnInvite);
        btnCancel = (Button) dialogView.findViewById(R.id.btnCancel);
        nameOfConversation = (EditText) dialogView.findViewById(R.id.nameOfConversation);
        spinner_no_episode_PatientName = (Spinner) dialogView.findViewById(R.id.spinner_no_episode_PatientName);
        spinner_episode_name = (Spinner) dialogView.findViewById(R.id.spinner_episode_name);
        spinnerPatientName = (Spinner) dialogView.findViewById(R.id.spinnerPatientName);
        txt_patient_name = (TextView) dialogView.findViewById(R.id.txt_patient_name);
        txt_episode_name = (TextView) dialogView.findViewById(R.id.txt_episode_name);
        txt_no_episode_patient_name = (TextView) dialogView.findViewById(R.id.txt_no_episode_patient_name);
        patient_related_episode = (AppCompatRadioButton) dialogView.findViewById(R.id.patient_related_episode);
        provider_patient_with_no_episode = (AppCompatRadioButton) dialogView.findViewById(R.id.provider_patient_with_no_episode);
        linear_provider_patient_with_no_episode = (LinearLayout) dialogView.findViewById(R.id.linear_provider_patient_with_no_episode);
        linear_patient_related_episode = (LinearLayout) dialogView.findViewById(R.id.linear_patient_related_episode);

        linear_provider_patient_with_no_episode.setVisibility(View.GONE);
        linear_patient_related_episode.setVisibility(View.GONE);

        radioGroup = (RadioGroup) dialogView.findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {

                switch (checkedId) {
                    case R.id.patient_related_episode:
                        linear_patient_related_episode.setVisibility(View.VISIBLE);
                        linear_provider_patient_with_no_episode.setVisibility(View.GONE);
                        break;

                    case R.id.provider_patient_with_no_episode:
                        linear_patient_related_episode.setVisibility(View.GONE);
                        linear_provider_patient_with_no_episode.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });


        isPatient = MyApplication.getInstance().getBooleanFromSharedPreference(PREF_IS_PATIENT);
        if (isPatient){

        }



        Window window = alertDialog.getWindow();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(alertDialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.windowAnimations = R.style.dialog_animation;
        window.setAttributes(lp);

        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog.show();
    }


}
