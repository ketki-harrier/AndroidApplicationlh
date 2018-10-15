package com.lifecyclehealth.lifecyclehealth.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.lifecyclehealth.lifecyclehealth.R;
import com.lifecyclehealth.lifecyclehealth.activities.MainActivity;
import com.lifecyclehealth.lifecyclehealth.application.MyApplication;
import com.lifecyclehealth.lifecyclehealth.callbacks.VolleyCallback;
import com.lifecyclehealth.lifecyclehealth.model.PatientDiaryEpisodeModel;
import com.lifecyclehealth.lifecyclehealth.utils.AppConstants;

import org.json.JSONObject;

import java.util.ArrayList;

import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.BASE_URL;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.STATUS_SUCCESS;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.URL_GET_PATIENTDIARY_EPISODELIST;

/**
 * A simple {@link Fragment} subclass.
 */
public class PatientDiaryFragment extends BaseFragmentWithOptions implements View.OnClickListener {

    private MainActivity mainActivity;
    private Spinner spinnerEpisode, spinnerStage;
    String messageCount, notificationCount;
    ArrayList<String> EpisodeNameList;
    ArrayList<String> EpisodeNameListId;
    ArrayList<String> Stage;
    ArrayList<String> StageId;
    TextView countTextViewMessage, notificationCountTextViewMessage;

    public PatientDiaryFragment() {
        // Required empty public constructor
    }


    @Override
    String getFragmentTag() {
        return "PatientDiaryFragment";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_patient_diary, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initialiseView(view);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) context;
    }


    private void initialiseView(View view) {

        //spinnerEpisode = (Spinner) view.findViewById(R.id.spinnerEpisode);
        //spinnerStage = (Spinner) view.findViewById(R.id.spinnerStage);
        setNotificationMenu(view);

        //getEpisodeList();
    }

    private void setNotificationMenu(View view) {
        messageCount = MyApplication.getInstance().getFromSharedPreference(AppConstants.messageCount);
        notificationCount = MyApplication.getInstance().getFromSharedPreference(AppConstants.notificationCount);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        setupToolbarTitle(toolbar, getString(R.string.title_patient_diary));


        RelativeLayout notificationHolderLayout = (RelativeLayout) toolbar.findViewById(R.id.notificationHolder);
        RelativeLayout messageHolderLayout = (RelativeLayout) toolbar.findViewById(R.id.messageHolder);
        countTextViewMessage = (TextView) view.findViewById(R.id.countTextViewMessage);
        notificationCountTextViewMessage = (TextView) view.findViewById(R.id.countTextViewNotificatione);
        ImageView imageViewMessage = (ImageView) view.findViewById(R.id.imageViewMessage);
        ImageView imageViewNotification = (ImageView) view.findViewById(R.id.imageViewNotification);
        //get Notification count
        getNotificationCount(notificationCountTextViewMessage, mainActivity);

        if (!messageCount.equals("0")) {
            countTextViewMessage.setText(messageCount);
        } else {
            countTextViewMessage.setVisibility(View.GONE);
        }
        if (!notificationCount.equals("0")) {
            notificationCountTextViewMessage.setText(notificationCount);
        } else {
            notificationCountTextViewMessage.setVisibility(View.GONE);
        }

        if (mainActivity.chatList.size() > 0) {
            imageViewMessage.setColorFilter(getContext().getResources().getColor(R.color.colorPrimary));
        } else if (mainActivity.meetListMessage.size() > 0) {
            imageViewMessage.setColorFilter(getContext().getResources().getColor(R.color.colorPrimary));
        } else {
            imageViewMessage.setColorFilter(getContext().getResources().getColor(R.color.uvv_gray));
        }
        if (!notificationCount.equals("0")) {
            imageViewNotification.setColorFilter(getContext().getResources().getColor(R.color.colorPrimary));
        } else {
            imageViewNotification.setColorFilter(getContext().getResources().getColor(R.color.uvv_gray));
        }


        notificationHolderLayout.setOnClickListener(this);
        messageHolderLayout.setOnClickListener(this);
    }

    private void getEpisodeList() {
        showProgressDialog(true);
        if (isConnectedToNetwork(mainActivity)) {
            try {
                mainActivity.networkRequestUtil.getDataSecure(BASE_URL + URL_GET_PATIENTDIARY_EPISODELIST, new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        showProgressDialog(false);
                        printLog("Response Of Episode code:" + response);
                        if (response != null) {
                            PatientDiaryEpisodeModel surveyPlanDto = new Gson().fromJson(response.toString(), PatientDiaryEpisodeModel.class);
                            if (surveyPlanDto != null) {
                                if (surveyPlanDto.getStatus().equalsIgnoreCase(STATUS_SUCCESS)) {

                                    setEpisodeData(surveyPlanDto);

                                } else {
                                    showDialogWithOkButton(surveyPlanDto.getMessage());
                                }
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

    private void setEpisodeData(final PatientDiaryEpisodeModel episodeData) {

        EpisodeNameList = new ArrayList<>();
        EpisodeNameListId = new ArrayList<>();
        Stage = new ArrayList<>();
        StageId = new ArrayList<>();
        int selectedEpisodeId = -99;
        EpisodeNameList.add("Select Episode");
        EpisodeNameListId.add("-99");
        for (int i = 0; episodeData.getEpisodePlanList().size() > i; i++) {
            EpisodeNameList.add(episodeData.getEpisodePlanList().get(i).getEpisode_Care_Plan_Name());
            EpisodeNameListId.add(episodeData.getEpisodePlanList().get(i).getEpisode_Care_PlanID());
            if (selectedEpisodeId == -99) {
                if (episodeData.getEpisodePlanList().get(i).isPatient_Log_Exists()) {
                    selectedEpisodeId = i;
                }
            }
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, EpisodeNameList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEpisode.setAdapter(dataAdapter);

        if (selectedEpisodeId != -99) {
            spinnerEpisode.setSelection(selectedEpisodeId + 1);
        }

        spinnerEpisode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int selectedItemId = (int) spinnerEpisode.getSelectedItemId();
                String episodeId = EpisodeNameListId.get(selectedItemId);
                Stage = new ArrayList<>();
                StageId = new ArrayList<>();
                int selectedStageId = -99;
                Stage.add("Select Stage");
                for (int i = 0; episodeData.getEpisodePlanList().size() > i; i++) {
                    if (episodeData.getEpisodePlanList().get(i).getEpisode_Care_PlanID().equals(episodeId)) {
                        for (int j = 0; episodeData.getEpisodePlanList().get(j).getStage_Payment_Details().size() > j; j++) {
                            Stage.add(episodeData.getEpisodePlanList().get(i).getStage_Payment_Details().get(j).getName());
                            StageId.add(episodeData.getEpisodePlanList().get(i).getStage_Payment_Details().get(j).getEpisode_Care_Plan_Stage_MappingID());
                            if (selectedStageId == -99 && episodeData.getEpisodePlanList().get(i).getCurrent_Episode_Stage() != null) {
                                if (episodeData.getEpisodePlanList().get(i).isPatient_Log_Exists()) {
                                    selectedStageId = j;
                                }
                            }
                        }
                    }
                }
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, Stage);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerStage.setAdapter(dataAdapter);

                if (selectedStageId != -99) {
                    spinnerStage.setSelection(selectedStageId + 1);
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.notificationHolder:
                getDialogSetup(mainActivity, LEFT, notificationCountTextViewMessage);

                break;
            case R.id.messageHolder:
                getDialogSetup(mainActivity, RIGHT, countTextViewMessage);
                break;
        }
    }
}
