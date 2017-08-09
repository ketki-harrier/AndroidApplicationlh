package com.harrier.lifecyclehealth.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.harrier.lifecyclehealth.R;
import com.harrier.lifecyclehealth.activities.MainActivity;
import com.harrier.lifecyclehealth.adapters.CustomExpandableListAdapter;
import com.harrier.lifecyclehealth.application.MyApplication;
import com.harrier.lifecyclehealth.callbacks.VolleyCallback;
import com.harrier.lifecyclehealth.dto.SurveyPlanDto;
import com.harrier.lifecyclehealth.model.PatientSurveyItem;
import com.nhaarman.supertooltips.ToolTipRelativeLayout;
import com.nhaarman.supertooltips.ToolTipView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import static com.harrier.lifecyclehealth.utils.AppConstants.BASE_URL;
import static com.harrier.lifecyclehealth.utils.AppConstants.CONST_SURV_LIST_COMPLETED;
import static com.harrier.lifecyclehealth.utils.AppConstants.CONST_SURV_LIST_SCHEDULE;
import static com.harrier.lifecyclehealth.utils.AppConstants.CONST_SURV_LIST_TODO;
import static com.harrier.lifecyclehealth.utils.AppConstants.PREF_IS_PATIENT;
import static com.harrier.lifecyclehealth.utils.AppConstants.STATUS_SUCCESS;
import static com.harrier.lifecyclehealth.utils.AppConstants.URL_SURVEY_PLAN_PATIENT;
import static com.harrier.lifecyclehealth.utils.AppConstants.URL_SURVEY_PLAN_Provider;

public class SurveyFragment extends BaseFragmentWithOptions {
    private MainActivity mainActivity;
    private ToolTipRelativeLayout mToolTipFrameLayout;
    private ToolTipView messageTooltipView, notificationTooltipView;
    private View viewMain;
    private ExpandableListView expandableListView;
    private CustomExpandableListAdapter expandableListAdapter;
    private LinkedHashMap<String, List<PatientSurveyItem>> expandableListDetail;
    boolean isPatient;
    List<String> titleList;

    @Override
    String getFragmentTag() {
        return "ToolTipRelativeLayout";
    }

    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.fragment_survey, parent, false);
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        isPatient = MyApplication.getInstance().getBooleanFromSharedPreference(PREF_IS_PATIENT);
        initializeView(view);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) context;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initializeView(View view) {
        viewMain = view;
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        setupToolbarTitle(toolbar, getString(R.string.title_survey));
//        mToolTipFrameLayout = (ToolTipRelativeLayout) view.findViewById(R.id.toolTipFrameLayout);


        RelativeLayout notificationHolderLayout = (RelativeLayout) toolbar.findViewById(R.id.notificationHolder);
        RelativeLayout messageHolderLayout = (RelativeLayout) toolbar.findViewById(R.id.messageHolder);

        notificationHolderLayout.setOnClickListener(onClickListener);
        messageHolderLayout.setOnClickListener(onClickListener);
        expandableListView = (ExpandableListView) view.findViewById(R.id.expandableListView);
        // for right indicator work
        DisplayMetrics metrics = new DisplayMetrics();
        mainActivity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
            expandableListView.setIndicatorBounds(width - GetPixelFromDips(50), width - GetPixelFromDips(10));
            expandableListView.setIndicatorBounds(width - GetPixelFromDips(50), width - GetPixelFromDips(10));
        } else {
            expandableListView.setIndicatorBoundsRelative(width - GetPixelFromDips(50), width - GetPixelFromDips(10));
            expandableListView.setIndicatorBoundsRelative(width - GetPixelFromDips(50), width - GetPixelFromDips(10));
        }

        titleList = getSurveyTitleList();
        expandableListDetail = new LinkedHashMap<String, List<PatientSurveyItem>>();
        expandableListDetail = getExpandableListDetail(expandableListDetail, titleList);
        setupList(expandableListDetail);
        getSurveyList();
    }

    public int GetPixelFromDips(float pixels) {
        // Get the screen's density scale
        final float scale = getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (pixels * scale + 0.5f);
    }

    /* for setting view before service call*/
    private LinkedHashMap<String, List<PatientSurveyItem>> getExpandableListDetail(LinkedHashMap<String, List<PatientSurveyItem>> listLinkedHashMap, List<String> titleList) {
        for (String s : titleList) {
            listLinkedHashMap.put(s, new ArrayList<PatientSurveyItem>());
        }
        return listLinkedHashMap;
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.notificationHolder:
                    getDialogSetup(mainActivity, LEFT);
                    break;
                case R.id.messageHolder:
                    getDialogSetup(mainActivity, RIGHT);
                    break;
            }
        }
    };

    /* For adding title in list before server call*/
    private List<String> getSurveyTitleList() {
        List<String> strings = new ArrayList<>();
        strings.add(CONST_SURV_LIST_TODO);
        strings.add(CONST_SURV_LIST_SCHEDULE);
        strings.add(CONST_SURV_LIST_COMPLETED);
        return strings;
    }

    String data = null;
    String title = null;

    /* Setting List*/
    private void setupList(final LinkedHashMap<String, List<PatientSurveyItem>> expandableListDetail) {
        data = null;
        expandableListAdapter = new CustomExpandableListAdapter(mainActivity, titleList, expandableListDetail);
        expandableListView.setAdapter(expandableListAdapter);
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
            }
        });

        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
            }
        });
        expandableListView.expandGroup(0);
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                printLog("ExpandableList:" + expandableListDetail.get(CONST_SURV_LIST_TODO));
                switch (groupPosition) {
                    case 0:
                        title = CONST_SURV_LIST_TODO;
                        data = new Gson().toJson(expandableListDetail.get(CONST_SURV_LIST_TODO));
                        break;
                    case 1:
                        title = CONST_SURV_LIST_SCHEDULE;
                        data = new Gson().toJson(expandableListDetail.get(CONST_SURV_LIST_SCHEDULE));
                        break;
                    case 2:
                        title = CONST_SURV_LIST_COMPLETED;
                        data = new Gson().toJson(expandableListDetail.get(CONST_SURV_LIST_COMPLETED));
                        break;
                    default:
                        title = CONST_SURV_LIST_TODO;
                        data = new Gson().toJson(expandableListDetail.get(CONST_SURV_LIST_TODO));


                }
                printLog("DataSending In fragment:" + data);
                mainActivity.changeToSurveyDetailsView(data, childPosition, title);
                return false;
            }
        });
    }

    private void getSurveyList() {
        showProgressDialog(true);
        String url;
        if (isPatient) {
            url = BASE_URL + URL_SURVEY_PLAN_PATIENT;
        } else {
            url = BASE_URL + URL_SURVEY_PLAN_Provider;
        }
        if (isConnectedToNetwork(mainActivity)) {
            try {
                mainActivity.networkRequestUtil.getDataSecure(url, new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        showProgressDialog(false);
                        printLog("Response Of Send code:" + response);
                        if (response != null) {
                            SurveyPlanDto surveyPlanDto = new Gson().fromJson(response.toString(), SurveyPlanDto.class);
                            if (surveyPlanDto != null) {
                                if (surveyPlanDto.getStatus().equalsIgnoreCase(STATUS_SUCCESS)) {
                                    setupList(getHashMapFromService(surveyPlanDto));
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

    private LinkedHashMap<String, List<PatientSurveyItem>> getHashMapFromService(SurveyPlanDto surveyPlanDto) {
        LinkedHashMap<String, List<PatientSurveyItem>> listLinkedHashMap = new LinkedHashMap<>();
        listLinkedHashMap.put(titleList.get(0), surveyPlanDto.getSurveyPlan().getSurveyPlanTodo());
        listLinkedHashMap.put(titleList.get(1), surveyPlanDto.getSurveyPlan().getSurveyPlanSchedule());
        listLinkedHashMap.put(titleList.get(2), surveyPlanDto.getSurveyPlan().getSurveyPlanCompleted());
        return listLinkedHashMap;
    }

}