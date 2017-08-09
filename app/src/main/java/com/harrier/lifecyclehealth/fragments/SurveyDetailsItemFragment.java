package com.harrier.lifecyclehealth.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.harrier.lifecyclehealth.R;
import com.harrier.lifecyclehealth.activities.MainActivity;
import com.harrier.lifecyclehealth.adapters.SurveyPagerAdapter;
import com.harrier.lifecyclehealth.application.MyApplication;
import com.harrier.lifecyclehealth.callbacks.VolleyCallback;
import com.harrier.lifecyclehealth.dto.SurveyDetailsQuestionDto;
import com.harrier.lifecyclehealth.model.PatientSurveyItem;
import com.harrier.lifecyclehealth.model.QuestionModel;
import com.harrier.lifecyclehealth.model.SurveyDetailsModel;
import com.harrier.lifecyclehealth.model.SurveySection;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.harrier.lifecyclehealth.utils.AppConstants.BASE_URL;
import static com.harrier.lifecyclehealth.utils.AppConstants.PREF_IS_PATIENT;
import static com.harrier.lifecyclehealth.utils.AppConstants.STATUS_SUCCESS;
import static com.harrier.lifecyclehealth.utils.AppConstants.URL_SURVEY_PLAN_QA_PATIENT;
import static com.harrier.lifecyclehealth.utils.AppConstants.URL_SURVEY_PLAN_QA_PROVIDER;

/**
 * Created by satyam on 17/04/2017.
 */

public class SurveyDetailsItemFragment extends BaseFragmentWithOptions {
    private MainActivity mainActivity;
    private ViewPager viewPager;
    private static final String SURVEY_EXTRAS_HOLDER = "holder_extra";
    //private SurveyModel surveyModel;
    private PatientSurveyItem surveyItem;
    private boolean isPatient;

    public static SurveyDetailsItemFragment newInstance(String data) {
        SurveyDetailsItemFragment holderFragment = new SurveyDetailsItemFragment();
        Bundle bundle = new Bundle();
        bundle.putString(SURVEY_EXTRAS_HOLDER, data);
        holderFragment.setArguments(bundle);
        return holderFragment;
    }

    @Override
    String getFragmentTag() {
        return "SurveyDetailsItemFragment";
    }

    @Override
    public void printLog(String message) {
        super.printLog(message);
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_survey_details_holder, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isPatient = MyApplication.getInstance().getBooleanFromSharedPreference(PREF_IS_PATIENT);
        surveyItem = new Gson().fromJson(getArguments().get(SURVEY_EXTRAS_HOLDER).toString(), PatientSurveyItem.class);
        initView(view);

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initView(View view) {
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        getSurveyList(surveyItem.getPatientSurveyResponseId());
    }

    /* Set view pager with values*/
    private void setViewPager(List<SurveySection> surveySection) {
        SurveyPagerAdapter pagerAdapter = new SurveyPagerAdapter(getChildFragmentManager(), getListFormattedForViewCreation(surveySection));
        viewPager.setAdapter(pagerAdapter);
    }

    /* reconstruct List for view creation*/
    private List<SurveyDetailsModel> getListFormattedForViewCreation(List<SurveySection> surveySection) {
        List<SurveyDetailsModel> detailsModels = new ArrayList<>();
        for (SurveySection section : surveySection) {
            for (QuestionModel questionModel : section.getQuestionModels()) {
                SurveyDetailsModel detailsModel = new SurveyDetailsModel();
                detailsModel.setDescription(section.getDescription());
                detailsModel.setName(section.getName());
                detailsModel.setPatientSurveySectionId(section.getPatientSurveySectionId());
                detailsModel.setTypeOfSection(section.getTypeOfSection());
                detailsModel.setQuestionModel(questionModel);
                detailsModels.add(detailsModel);
            }
        }
        return detailsModels;
    }

    private void getSurveyList(int id) {
        showProgressDialog(true);
        String url;
        if (isPatient) {
            url = BASE_URL + URL_SURVEY_PLAN_QA_PATIENT + id;
        } else {
            url = BASE_URL + URL_SURVEY_PLAN_QA_PROVIDER + id;
        }
        if (isConnectedToNetwork(mainActivity)) {
            try {
                mainActivity.networkRequestUtil.getDataSecure(url, new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        showProgressDialog(false);
                        //printLog("Response QA:" + response);
                        if (response != null) {
                            SurveyDetailsQuestionDto questionDto = new Gson().fromJson(response.toString(), SurveyDetailsQuestionDto.class);
                            if (questionDto != null) {
                               // printLog("QuestionDto" + questionDto);
                                if (questionDto.getStatus().equalsIgnoreCase(STATUS_SUCCESS)) {
                                    setViewPager(questionDto.getSurveyQuestion().getSurveyDetails().getSurveySection());
                                } else showDialogWithOkButton(questionDto.getMessage());

                            } else
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
