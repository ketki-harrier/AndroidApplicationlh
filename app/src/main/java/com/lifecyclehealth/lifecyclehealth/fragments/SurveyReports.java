package com.lifecyclehealth.lifecyclehealth.fragments;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.lifecyclehealth.lifecyclehealth.R;
import com.lifecyclehealth.lifecyclehealth.activities.MainActivity;
import com.lifecyclehealth.lifecyclehealth.adapters.SurveyReportsAdapter;
import com.lifecyclehealth.lifecyclehealth.application.MyApplication;
import com.lifecyclehealth.lifecyclehealth.callbacks.VolleyCallback;
import com.lifecyclehealth.lifecyclehealth.model.ColorCode;
import com.lifecyclehealth.lifecyclehealth.model.SurveyReportResponse;
import com.lifecyclehealth.lifecyclehealth.utils.AppConstants;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.BASE_URL;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.PREF_IS_PATIENT;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.STATUS_SUCCESS;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.URL_FILTER_REPORT;

/**
 * A simple {@link Fragment} subclass.
 */
public class SurveyReports extends BaseFragmentWithOptions {

    private MainActivity mainActivity;
    private String patientSurveyResponseId;
    private static final String PATIENT_SURVEY_ID = "holder_extra";
    boolean isPatient;
    RecyclerView recyclerView;
    TextView survey_title, survey_name, patient_name, submitted_by, submitted_on, score_survey;
    private ColorCode colorCode;
    String Stringcode;

    public static SurveyReports newInstance(String data) {
        SurveyReports holderFragment = new SurveyReports();
        Bundle bundle = new Bundle();
        bundle.putString(PATIENT_SURVEY_ID, data);
        holderFragment.setArguments(bundle);
        return holderFragment;
    }


    @Override
    String getFragmentTag() {
        return "SurveyReports";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_survey_reports, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        isPatient = MyApplication.getInstance().getBooleanFromSharedPreference(PREF_IS_PATIENT);
        initializeView(view);
    }

    private void initializeView(View view) {
       // try {
            String resposne = MyApplication.getInstance().getColorCodeJson(AppConstants.SET_COLOR_CODE);
            colorCode = new Gson().fromJson(resposne, ColorCode.class);
        String demo = colorCode.getVisualBrandingPreferences().getColorPreference();
        String Stringcode = "";
        String hashcode = "";

        if(demo == null){
            hashcode = "Green";
            Stringcode = "259b24";
        }
        else if(demo !=null) {
            String[] arr = colorCode.getVisualBrandingPreferences().getColorPreference().split("#");
            hashcode = arr[0].trim();
            Stringcode = arr[1].trim();

            if (hashcode.equals("Black") && Stringcode.length() < 6) {
                Stringcode = "333333";
            }
        }
       // }catch (Exception e){e.printStackTrace();}
        patientSurveyResponseId = getArguments().get(PATIENT_SURVEY_ID).toString();
        survey_title = (TextView) view.findViewById(R.id.survey_title);
        ImageView backArrowBtn = (ImageView) view.findViewById(R.id.backArrowBtn);
        backArrowBtn.setColorFilter(Color.parseColor("#"+Stringcode));
        survey_name = (TextView) view.findViewById(R.id.survey_name);
        patient_name = (TextView) view.findViewById(R.id.patient_name);
        submitted_by = (TextView) view.findViewById(R.id.submitted_by);
        submitted_on = (TextView) view.findViewById(R.id.submitted_on);
        score_survey = (TextView) view.findViewById(R.id.score_survey);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        getSurveyReports();
    }

    private void getSurveyReports() {
        showProgressDialog(true);
        if (isConnectedToNetwork(mainActivity)) {
            try {
                mainActivity.networkRequestUtil.getDataSecure(BASE_URL + URL_FILTER_REPORT + patientSurveyResponseId, new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        showProgressDialog(false);
                        printLog("Response Of survey report code:" + response);
                        if (response != null) {
                            SurveyReportResponse surveyReportResponse = new Gson().fromJson(response.toString(), SurveyReportResponse.class);
                            if (surveyReportResponse != null) {
                                if (surveyReportResponse.getStatus().equalsIgnoreCase(STATUS_SUCCESS)) {
                                    survey_title.setText(surveyReportResponse.getSurveyQuestion().getSurveyDetails().getSurveyTemplateName());
                                    survey_name.setText(surveyReportResponse.getSurveyQuestion().getSurveyDetails().getSurveyName());
                                    patient_name.setText(surveyReportResponse.getSurveyQuestion().getSurveyDetails().getFirstName() + " " + surveyReportResponse.getSurveyQuestion().getSurveyDetails().getLastName());
                                    submitted_by.setText(surveyReportResponse.getSurveyQuestion().getSurveyDetails().getSubmittingUserFirstName() + " " + surveyReportResponse.getSurveyQuestion().getSurveyDetails().getSubmittingUserLastName());
                                    submitted_on.setText(surveyReportResponse.getSurveyQuestion().getSurveyDetails().getSubmissionDateAndTime());
                                    Float a = Float.parseFloat(surveyReportResponse.getSurveyQuestion().getSurveyDetails().getPercentTotalSurveyScore());

                                    int round = Math.round(a);
                                    score_survey.setText(round + "");
                                    List<SurveyReportResponse.QuestionModel> questionModels = new ArrayList<>();
                                    for (int i = 0; i < surveyReportResponse.getSurveyQuestion().getSurveyDetails().getSurveySection().size(); i++) {
                                        for (SurveyReportResponse.QuestionModel model : surveyReportResponse.getSurveyQuestion().getSurveyDetails().getSurveySection().get(i).getQuestionModels()) {
                                            questionModels.add(model);
                                        }
                                    }
                                    SurveyReportsAdapter surveyReportsAdapter = new SurveyReportsAdapter(questionModels, getContext());
                                    recyclerView.setAdapter(surveyReportsAdapter);

                                } else {
                                    showDialogWithOkButton(surveyReportResponse.getMessage());
                                }
                            } else
                                showDialogWithOkButton(getString(R.string.error_someting_went_wrong));
                        }
                    }

                    @Override
                    public void onError(VolleyError error) {
                    }
                });
            } catch (Exception e) {
            }
        } else {
            showNoNetworkMessage();
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) context;
    }

    private void backPage() {
        getFragmentManager().popBackStack();
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

}
